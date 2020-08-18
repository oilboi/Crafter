package game;

import engine.GameItem;
import engine.Window;
import engine.graph.Mesh;
import engine.graph.ShaderProgram;
import engine.graph.Transformation;
import engine.graph.Utils;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    /**
     * Field of View in Radians
     */

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private Matrix4f projectionMatrix;

    private final Transformation transformation;

    private ShaderProgram shaderProgram;

    public Renderer(){
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception{
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/resources/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/resources/fragment.fs"));
        shaderProgram.link();


        //create projection matrix
        float aspectRatio = (float) window.getWidth()/window.getHeight();

        projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);

        //create uniforms for world and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        //create uniforms for world and projection matrices
        shaderProgram.createUniform("worldMatrix");
        //create uniforms for texture sampler
        shaderProgram.createUniform("texture_sampler");

        window.setClearColor(0.f,0.f,0.f,0.f);
    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, GameItem[] gameItems){
        clear();

        if (window.isResized()){
            glViewport(0,0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        //update projection matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);



        //render each gameItem
        for(GameItem gameItem : gameItems){
            //set world matrix for this item
            Matrix4f worldMatrix =
                    transformation.getWorldMatrix(
                            gameItem.getPosition(),
                            gameItem.getRotation(),
                            gameItem.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            shaderProgram.setUniform("texture_sampler", 0);

            //render the mesh for this game item
            gameItem.getMesh().render();
        }

        shaderProgram.unbind();
    }

    public void cleanup(){
        if (shaderProgram != null){
            shaderProgram.cleanup();
        }
    }
}
