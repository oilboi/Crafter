package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.Window;
import engine.graph.Mesh;
import engine.graph.Texture;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class DummyGame implements IGameLogic {

    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;

    private int direction = 0;

    private float color = 0.0f;

    private final Renderer renderer;

    Mesh mesh;

    private GameItem[] gameItems;

    public DummyGame(){
        renderer = new Renderer();
    }

    @Override
    public void init(Window window) throws Exception{
        renderer.init(window);

        // Create the Mesh
        float[] positions = new float[] {
                // VO
                -0.5f,  0.5f,  0.5f,
                // V1
                -0.5f, -0.5f,  0.5f,
                // V2
                0.5f, -0.5f,  0.5f,
                // V3
                0.5f,  0.5f,  0.5f,
                // V4
                -0.5f,  0.5f, -0.5f,
                // V5
                0.5f,  0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
        };


        float this0 = 1.0f;
        float this1 = 1.0f;
        float this2 = 1.0f;
        float this3 = 1.0f;
        float this4 = 1.0f;
        float this5 = 1.0f;
        float[] colors = new float[]{
                //front
                this0, this0, this0,
                this0, this0, this0,
                this0, this0, this0,
                this0, this0, this0,
                //right
                this1, this1, this1,
                this1, this1, this1,
                this1, this1, this1,
                this1, this1, this1,
//                //top
                this2, this2, this2,
                this2, this2, this2,
                this2, this2, this2,
                this2, this2, this2,
//
//                this3, this3, this3,
//                this3, this3, this3,
//                this3, this3, this3,
//                this3, this3, this3,
//
//                0.0f, 0.5f, 0.5f,
//                0.5f, 0.0f, 0.0f,
//
//                0.0f, 0.5f, 0.0f,
//                0.0f, 0.0f, 0.5f,
        };
        float[] textCoords = new float[]{
                //front
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                //back
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.5f, 0.5f,
                0.0f, 0.5f,

                //top
                0.0f, 0.5f,
                0.0f, 0.5f,
                0.0f, 0.5f,
                0.0f, 0.5f,

                //4, 0, 3, 5, 4, 3,

//                //left
//                0.5f, 0.5f,
//                0.5f, 0.5f,
//                0.5f, 0.5f,
//                0.0f, 0.5f,



        };

        int[] indices = new int[] {
//                Front face
                0, 1, 3, 3, 1, 2,
//                Right face
                3, 2, 7, 5, 3, 7,
//                // Top Face
                4, 0, 3, 5, 4, 3,
//                // Left face
                6, 1, 0, 6, 0, 4,
//                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5,
        };

        Texture texture = new Texture("textures/grassblock.png");

        mesh = new Mesh(positions, colors, indices, textCoords, texture);

        GameItem gameItem = new GameItem(mesh);

        gameItem.setPosition(0,0,-2);

        gameItems = new GameItem[] {gameItem};
    }

    @Override
    public void input(Window window){
        if ( window.isKeyPressed(GLFW_KEY_UP) ){
            direction = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN)){
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(float interval){
        color += direction * 0.01f;
        if ( color > 1){
            color = 1.0f;
        } else if ( color < 0 ){
            color = 0.0f;
        }

        for (GameItem gameItem : gameItems){
            //update position
            Vector3f itemPos = gameItem.getPosition();
            float posx = itemPos.x + displxInc * 0.01f;
            float posy = itemPos.y + displyInc * 0.01f;
            float posz = itemPos.z + displzInc * 0.01f;
            gameItem.setPosition(posx, posy, posz);

            //Update scale
            float scale = gameItem.getScale();
            scale += scaleInc * 0.05f;
            if (scale < 0) {
                scale = 0;
            }
            gameItem.setScale(scale);

            //update rotation angle
            float rotation = gameItem.getRotation().y + 1.5f;

            if (rotation > 360) {
                rotation -= 360;
            }

            gameItem.setRotation(30, rotation, 0);
        }
    }

    @Override
    public void render(Window window){
        renderer.render(window, gameItems);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        mesh.cleanUp();
    }
}
