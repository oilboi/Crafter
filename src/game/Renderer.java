package game;

import engine.Window;
import engine.graph.*;
import org.joml.Matrix4f;

import static engine.Chunk.getChunkMesh;
import static engine.Chunk.getLimit;
import static engine.ItemEntity.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    /**
     * Field of View in Radians
     */

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1120.f;

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
        //create uniforms for world and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        //create uniforms for model view matrix
        shaderProgram.createUniform("modelViewMatrix");
        //create uniforms for texture sampler
        shaderProgram.createUniform("texture_sampler");

        window.setClearColor(0.53f,0.81f,0.92f,0.f);
    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera){
        clear();

        if (window.isResized()){
            glViewport(0,0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        //update projection matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        //update the view matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("texture_sampler", 0);

        //render each chunk
        for(int i = 0; i < getLimit(); i++){
            Mesh thisMesh = getChunkMesh(i);
            if (thisMesh == null){
                System.out.println("wow that doesn't exist!");
                continue;
            }

            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
//            System.out.println(thisMesh);
            thisMesh.render();
        }


        //render each item entity
        for (int i = 0; i < getTotalObjects(); i++){
            if (!itemExists(i)){
                continue;
            }
            Matrix4f modelViewMatrix = transformation.getEntityModelViewMatrix(i, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            getMesh(i).render();
        }


        shaderProgram.unbind();
    }

    public void cleanup(){
        if (shaderProgram != null){
            shaderProgram.cleanup();
        }
    }
}
