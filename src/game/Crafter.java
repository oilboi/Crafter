package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.Camera;
import game.ChunkHandling.Chunk;
import game.ChunkHandling.ChunkData;
import game.player.Player;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.blocks.BlockDefinition.initializeBlocks;
import static game.player.TNT.boom;
import static org.lwjgl.glfw.GLFW.*;

public class Crafter implements IGameLogic {

    public static int chunkRenderDistance = 2;

    private static final float MOUSE_SENSITIVITY = 0.008f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] chunkMeshes = new GameItem[(chunkRenderDistance*(4*chunkRenderDistance)+(chunkRenderDistance*4)) + 1];

    private boolean fButtonPushed = false;

    private boolean rButtonPushed = false;

    private boolean tButtonPushed = false;

    private boolean boomBuffer = false;

    private Player player;

    public static int getChunkRenderDistance(){
        return chunkRenderDistance;
    }

    public Crafter(){
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception{
        renderer.init(window);

        //this initializes the block definitions
        initializeBlocks();

        //create the initial map
        for (int x = -chunkRenderDistance; x <= chunkRenderDistance; x++) {
            for (int z = -chunkRenderDistance; z <= chunkRenderDistance; z++) {
                ChunkData.storeChunk(x, z, new Chunk(x, z));
                generateChunkMesh(x, z, chunkMeshes, false);
            }
        }

        player = new Player();
    }

    @Override
    public void input(Window window, MouseInput input){
        if (window.isKeyPressed(GLFW_KEY_W)){
            float yaw = (float)Math.toRadians(camera.getRotation().y) + (float)Math.PI;
            float x = (float)Math.sin(-yaw);
            float z = (float)Math.cos(yaw);
            player.addInertia(x,0,z);
        }
        if (window.isKeyPressed(GLFW_KEY_S)){
            //no mod needed
            float yaw = (float)Math.toRadians(camera.getRotation().y);
            float x = (float)Math.sin(-yaw);
            float z = (float)Math.cos(yaw);
            player.addInertia(x,0,z);
        }

        if (window.isKeyPressed(GLFW_KEY_A)){
            float yaw = (float)Math.toRadians(camera.getRotation().y) + (float)(Math.PI /2);
            float x = (float)Math.sin(-yaw);
            float z = (float)Math.cos(yaw);
            player.addInertia(x,0,z);
        }
        if (window.isKeyPressed(GLFW_KEY_D)){
            float yaw = (float)Math.toRadians(camera.getRotation().y) - (float)(Math.PI /2);
            float x = (float)Math.sin(-yaw);
            float z = (float)Math.cos(yaw);
            player.addInertia(x,0,z);
        }



        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
            //cameraInc.y = -1;
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE) && player.isOnGround()){
            player.addInertia(0,12f,0);
        }

        //prototype toggle locking mouse - F KEY
        if (window.isKeyPressed(GLFW_KEY_F)) {
            if (!fButtonPushed) {
                input.setMouseLocked(!input.isMouseLocked());
                fButtonPushed = true;
                if(!input.isMouseLocked()) {
                    glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                } else{
                    glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                }
            }
        } else if (!window.isKeyPressed(GLFW_KEY_F)){
            fButtonPushed = false;
        }

        //prototype reset position - R KEY
        if (window.isKeyPressed(GLFW_KEY_R)) {
            if (!rButtonPushed) {
                rButtonPushed = true;
                player.setPos(new Vector3f(0,129,0));
                System.out.println("Position reset!");
            }
        } else if (!window.isKeyPressed(GLFW_KEY_R)){
            rButtonPushed = false;
        }


        //prototype explosion - T KEY
        if (window.isKeyPressed(GLFW_KEY_T)) {
            if (!tButtonPushed) {
                tButtonPushed = true;
                boomBuffer = true;
                System.out.println("boom");
            }
        } else if (!window.isKeyPressed(GLFW_KEY_T)){
            tButtonPushed = false;
        }

        //mouse left button input
        if(input.isLeftButtonPressed()){
            player.setMining(true);
        } else {
            player.setMining(false);
        }

        //mouse right button input
        if(input.isRightButtonPressed()){
            player.setPlacing(true);
        } else {
            player.setPlacing(false);
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {

        camera.setPosition(player.getPosWithEyeHeight().x, player.getPosWithEyeHeight().y, player.getPosWithEyeHeight().z);

        //update camera based on mouse
        Vector2f rotVec = mouseInput.getDisplVec();
        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        //limit camera pitch
        if (camera.getRotation().x < -90f) {
            camera.moveRotation((90f + camera.getRotation().x) * -1f, 0, 0);
        }
        if (camera.getRotation().x > 90f){
            camera.moveRotation((camera.getRotation().x - 90f) * -1f , 0, 0);
        }

        //loop camera yaw
        if (camera.getRotation().y < -180f){
            camera.moveRotation(0,360, 0);
        }
        if (camera.getRotation().y > 180f){
            camera.moveRotation(0,-360, 0);
        }

        player.onTick(camera, chunkMeshes);

        if(boomBuffer){
            boom((int)Math.floor(player.getPos().x),(int)Math.floor(player.getPos().y),(int)Math.floor(player.getPos().z), chunkMeshes);
            boomBuffer = false;
        }

    }

    @Override
    public void render(Window window){
        renderer.render(window, camera, chunkMeshes);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        for (GameItem gameItem : chunkMeshes){
            if (gameItem != null) {
                gameItem.getMesh().cleanUp();
            }
        }
    }
}
