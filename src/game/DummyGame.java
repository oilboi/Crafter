package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.Camera;
import engine.graph.Mesh;
import engine.graph.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;

    private static final float CAMERA_POS_STEP = 0.05f;

    public DummyGame(){
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception{
        renderer.init(window);

        // Create the Mesh
        float[] positions = new float[] {

                //front
                 0.5f,  0.5f, 0.5f,
                -0.5f,  0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                 0.5f, -0.5f, 0.5f,

                //back
                -0.5f,  0.5f, -0.5f,
                 0.5f,  0.5f, -0.5f,
                 0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,

                //right
                 0.5f,  0.5f, -0.5f,
                 0.5f,  0.5f,  0.5f,
                 0.5f, -0.5f,  0.5f,
                 0.5f, -0.5f, -0.5f,

                //left
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,

                //top
                -0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f,  0.5f,
                 0.5f,  0.5f,  0.5f,
                 0.5f,  0.5f, -0.5f,

                //bottom
                -0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,
                 0.5f, -0.5f, -0.5f,
                 0.5f, -0.5f,  0.5f,
        };


        float frontLight  = 1.0f;
        float backLight   = 1.0f;
        float rightLight  = 1.0f;
        float leftLight   = 1.0f;
        float topLight    = 1.0f;
        float bottomLight = 1.0f;
        float[] colors = new float[]{
                //front
                frontLight,frontLight,frontLight,
                frontLight,frontLight,frontLight,
                frontLight,frontLight,frontLight,
                frontLight,frontLight,frontLight,

                //back
                backLight,backLight, backLight,
                backLight,backLight, backLight,
                backLight,backLight, backLight,
                backLight,backLight, backLight,

                //right
                rightLight,rightLight,rightLight,
                rightLight,rightLight,rightLight,
                rightLight,rightLight,rightLight,
                rightLight,rightLight,rightLight,

                //left
                leftLight,leftLight,leftLight,
                leftLight,leftLight,leftLight,
                leftLight,leftLight,leftLight,
                leftLight,leftLight,leftLight,

                //top
                topLight,topLight,topLight,
                topLight,topLight,topLight,
                topLight,topLight,topLight,
                topLight,topLight,topLight,

                //bottom
                bottomLight,bottomLight,bottomLight,
                bottomLight,bottomLight,bottomLight,
                bottomLight,bottomLight,bottomLight,
                bottomLight,bottomLight,bottomLight,

        };
        float[] textCoords = new float[]{
                //front
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                //back
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                //right
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                //left
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                //top
                0.5f, 0.5f,
                0.0f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                //bottom
                1.0f, 0.0f,
                0.5f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };

        int[] indices = new int[] {
//                Front face
                0, 1, 2, 0, 2, 3,
//                Back face
                0+4, 1+4, 2+4, 0+4, 2+4, 3+4,
//                Right face
                0+8, 1+8, 2+8, 0+8, 2+8, 3+8,
//                Left face
                0+12, 1+12, 2+12, 0+12, 2+12, 3+12,
//                Top Face
                0+16, 1+16, 2+16, 0+16, 2+16, 3+16,
//                Bottom Face
                0+20, 1+20, 2+20, 0+20, 2+20, 3+20,
        };

        Texture texture = new Texture("textures/grassblock.png");
        Mesh mesh = new Mesh(positions, colors, indices, textCoords, texture);

        GameItem gameItem = new GameItem(mesh);
        gameItem.setPosition(0,0,-2);

        GameItem gameItem2 = new GameItem(mesh);
        gameItem2.setPosition(1,0,-1);

        gameItems = new GameItem[] {gameItem, gameItem2};
    }

    @Override
    public void input(Window window, MouseInput input){
        cameraInc.set(0,0,0);
        if (window.isKeyPressed(GLFW_KEY_W)){
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)){
            cameraInc.z = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_A)){
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)){
            cameraInc.x = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)){
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput){

        //update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP,
                cameraInc.y * CAMERA_POS_STEP,
                cameraInc.z * CAMERA_POS_STEP);

        //update camera based on mouse
        if (mouseInput.isRightButtonPressed()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

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
