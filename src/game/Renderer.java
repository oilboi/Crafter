package game;

import engine.Utils;
import engine.Window;
import engine.graph.*;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;

import static engine.Chunk.getChunkMesh;
import static engine.Chunk.getLimit;
import static engine.Hud.*;
import static engine.ItemEntity.*;
import static engine.TNTEntity.*;
import static game.player.Inventory.getItemInInventorySlot;
import static game.player.Player.getPlayerWorldSelectionPos;
import static game.player.Player.isPlayerInventoryOpen;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    /**
     * Field of View in Radians
     */

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float HUD_Z_NEAR = 0.001f;
    private static final float HUD_Z_FAR = 1120.f;

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1120.f;

    private static Vector2d windowSize = new Vector2d();

    private final Transformation transformation;

    private ShaderProgram shaderProgram;

    private ShaderProgram hudShaderProgram;

    public Renderer(){
        transformation = new Transformation();
    }

    public static Vector2d getWindowSize(){
        return windowSize;
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

        //todo ----------------------------------------------------------------------------------------
        hudShaderProgram = new ShaderProgram();
        hudShaderProgram.createVertexShader(Utils.loadResource("/resources/hud_vertex.vs"));
        hudShaderProgram.createFragmentShader(Utils.loadResource("/resources/hud_fragment.fs"));
        hudShaderProgram.link();

        //create uniforms for world and projection matrices
        hudShaderProgram.createUniform("projectionMatrix");
        //create uniforms for model view matrix
        hudShaderProgram.createUniform("modelViewMatrix");
        //create uniforms for texture sampler
        hudShaderProgram.createUniform("texture_sampler");


        window.setClearColor(0.53f,0.81f,0.92f,0.f);

        windowSize.x = window.getWidth();
        windowSize.y = window.getHeight();
    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera){
        clear();

        if (window.isResized()){
            windowSize.x = window.getWidth();
            windowSize.y = window.getHeight();
            glViewport(0,0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        //todo: BEGIN WORLD SHADER PROGRAM!
        shaderProgram.bind();

        //update projection matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        //update the view matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("texture_sampler", 0);

        //render each chunk
        for(int x = 0; x < getLimit(); x++) {
            for (int z = 0; z < getLimit(); z++) {

                Mesh thisMesh = getChunkMesh(x,z);

                if (thisMesh == null) {
                    System.out.println("wow that doesn't exist!");
                    continue;
                }

                Matrix4f modelViewMatrix = transformation.getModelViewMatrix(viewMatrix);
                shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

                thisMesh.render();
            }
        }


        //render each item entity
        for (int i = 0; i < getTotalObjects(); i++){
            if (!itemExists(i)){
                continue;
            }
            Matrix4f modelViewMatrix = transformation.getEntityModelViewMatrix(i, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            getItemMesh(i).render();
        }

        //render each TNT entity
        Mesh tntMesh = getTNTMesh();
        for (int i = 0; i < getTotalTNT(); i++){
            if (!tntExists(i)){
                continue;
            }
            Matrix4f modelViewMatrix = transformation.getTNTModelViewMatrix(i, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            tntMesh.render();
        }

        //render world selection mesh
        if (getPlayerWorldSelectionPos() != null){
            Mesh selectionMesh = getWorldSelectionMesh();
            Matrix4f modelViewMatrix = transformation.getWorldSelectionViewMatrix(getPlayerWorldSelectionPos(), viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            selectionMesh.render();
        }

        shaderProgram.unbind();

        glClear(GL_DEPTH_BUFFER_BIT);



        //TODO: BEGIN HUD SHADER PROGRAM!
        hudShaderProgram.bind();

        Matrix4f hudProjectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), HUD_Z_NEAR, HUD_Z_FAR);
        hudShaderProgram.setUniform("projectionMatrix", hudProjectionMatrix);
        Matrix4f hudViewMatrix = new Matrix4f();
        hudShaderProgram.setUniform("texture_sampler", 0);



        //draw wield hand
        {
            Mesh thisMesh = getWieldHandMesh();
            Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPosRotationScale(new Vector3f(14, -15, -14f), new Vector3f(-130, 10, -20), new Vector3f(6f, 6f, 6f), hudViewMatrix);
            hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            thisMesh.render();
        }

        glClear(GL_DEPTH_BUFFER_BIT);
        hudViewMatrix = new Matrix4f();

        if (isPlayerInventoryOpen()) {
            {
                Mesh thisMesh = getInventoryMesh();
                Matrix4f modelViewMatrix = transformation.getModelViewMatrix(hudViewMatrix);
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            hudViewMatrix = new Matrix4f();

            {
                Mesh thisMesh = getPlayerMesh();
                Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPosRotationScale(getPlayerHudPos(),getPlayerHudRot(),getPlayerHudScale(),hudViewMatrix);
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }


            glClear(GL_DEPTH_BUFFER_BIT);

            hudViewMatrix = new Matrix4f();

            {
                if (getInvSelection() != null) {
                    Mesh thisMesh = getInventorySelectionMesh();
                    int[] invSelection = getInvSelection();
                    float xer = (float)(invSelection[0]-1) * 1.8375f;
                    float yer = (float)(invSelection[1]-1) * 1.715f;

                    Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPosRotationScale(new Vector3f(-7.35f + xer, -0.85f - yer, 0), new Vector3f(0,0,0), new Vector3f(0.75f,0.75f,1), hudViewMatrix);
                    hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                    thisMesh.render();
                }
            }


            //render items in inventory
            for (int x = 1; x <= 9; x++){
                for (int y = 1; y <= 4; y++){

                    if (getItemInInventorySlot(x-1,y-1) != 0) {
                        glClear(GL_DEPTH_BUFFER_BIT);
                        hudViewMatrix = new Matrix4f();

                        Mesh thisMesh = getItemMeshByBlock(getItemInInventorySlot(x-1,y-1));

                        float xer = (float) (x - 1) * 1.8375f;

                        float yer = (float) (y - 1) * 1.715f;

                        Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPosRotationScale(new Vector3f(-7.35f + xer, -1.25f - yer, -14f), new Vector3f(-20 + (y * 3), 45, 0), new Vector3f(2.25f, 2.25f, 2.25f), hudViewMatrix);
                        hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                        thisMesh.render();
                    }
                }
            }

        } else {

            hudViewMatrix = new Matrix4f();
            glClear(GL_DEPTH_BUFFER_BIT);
            {
                Mesh thisMesh = getSelectionMesh();
                Matrix4f modelViewMatrix = transformation.getModelViewMatrix(hudViewMatrix);
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            hudViewMatrix = new Matrix4f();

            {
                Mesh thisMesh = getHotBarMesh();
                Matrix4f modelViewMatrix = transformation.getModelViewMatrix(hudViewMatrix);
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            hudViewMatrix = new Matrix4f();

//            {
//                Mesh thisMesh = getCrossHairMesh();
//                Matrix4f modelViewMatrix = transformation.getModelViewMatrix(hudViewMatrix);
//                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
//                thisMesh.render();
//            }


            //THESE GO LAST!
            {
                Mesh thisMesh = getVersionInfoTextShadow();
                Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPos(getVersionInfoShadowPos(), hudViewMatrix);
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            hudViewMatrix = new Matrix4f();
            glClear(GL_DEPTH_BUFFER_BIT);

            {
                Mesh thisMesh = getVersionInfoText();
                Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPos(getVersionInfoPos(), hudViewMatrix);
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            //render items in hotbar
            for (int x = 1; x <= 9; x++){

                if (getItemInInventorySlot(x-1,0) != 0) {

                    glClear(GL_DEPTH_BUFFER_BIT);
                    hudViewMatrix = new Matrix4f();

                    Mesh thisMesh = getItemMeshByBlock(getItemInInventorySlot(x-1,0));

                    float xer = (float) (x - 1) * 1.8375f;

                    float yer = (float) (- 1) * 1.715f;

                    Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPosRotationScale(new Vector3f(-7.35f + xer, -7.5f, -14f), new Vector3f(-10, 45, 0), new Vector3f(2.25f, 2.25f, 2.25f), hudViewMatrix);
                    hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                    thisMesh.render();
                }
            }



        }
        hudShaderProgram.unbind();
    }

    public void cleanup(){
        if (shaderProgram != null){
            shaderProgram.cleanup();
        }

        if (hudShaderProgram != null){
            hudShaderProgram.cleanup();
        }
    }
}
