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
import static org.lwjgl.glfw.GLFW.*;

public class Crafter implements IGameLogic {

    public final static int chunkRenderDistance = 3;

    private static final float MOUSE_SENSITIVITY = 0.01f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;

    private static final float CAMERA_POS_STEP = 0.5f;

    private boolean fButtonPushed = false;

    private boolean rButtonPushed = false;

    private String[] chunkNames;

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
        //this is a lazy way to deduce the amount of chunks there are in the map
        int index = 0;
        for (int x = -chunkRenderDistance; x <= chunkRenderDistance; x++) {
            for (int z = -chunkRenderDistance; z <= chunkRenderDistance; z++) {
                index++;
            }
        }

        gameItems = new GameItem[index];
        chunkNames = new String[index];

        index = 0;
        //build an index of names for assignment/access
        for (int x = -chunkRenderDistance; x <= chunkRenderDistance; x++) {
            for (int z = -chunkRenderDistance; z <= chunkRenderDistance; z++) {
                chunkNames[index] = x + " " + z;
                index++;
            }
        }

        //create the initial map
        for (int x = -chunkRenderDistance; x <= chunkRenderDistance; x++) {
            for (int z = -chunkRenderDistance; z <= chunkRenderDistance; z++) {

                ChunkData.storeChunk(x, z, new Chunk(x, z));
                generateChunkMesh(x, z, gameItems, chunkNames, false);
//                System.out.println(x + " " + z);
            }
        }
//        System.out.println("Wow! It's done!");
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
            }
        } else if (!window.isKeyPressed(GLFW_KEY_R)){
            rButtonPushed = false;
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

        player.onTick(camera, gameItems, chunkNames);


        //this is the game item loop
        for (GameItem gameItem : gameItems){
            //update position
//            Vector3f itemPos = gameItem.getPosition();
//            float posx = itemPos.x + displxInc * 0.01f;
//            float posy = itemPos.y + displyInc * 0.01f;
//            float posz = itemPos.z + displzInc * 0.01f;

//            gameItem.setPosition(posx, posy, posz);

            //Update scale
//            float scale = gameItem.getScale();
//            scale += scaleInc * 0.05f;
//            if (scale < 0) {
//                scale = 0;
//            }
//            gameItem.setScale(scale);

            //gameItem.setPosition((float)Math.random()-0.5f,(float)Math.random()-0.5f,-2f);

            //update rotation angle
//            float rotation = gameItem.getRotation().y - 1.5f;
//
//
//            if (rotation > 360) {
//                rotation -= 360;
//            }
//
//            gameItem.setRotation(rotation, rotation, rotation);
        }
    }

    @Override
    public void render(Window window){
        renderer.render(window, camera, gameItems);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        for (GameItem gameItem : gameItems){
            gameItem.getMesh().cleanUp();
        }
    }
}
