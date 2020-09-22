package engine;

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
import static engine.Window.*;
import static game.player.Inventory.getItemInInventorySlot;
import static game.player.Player.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private static float FOV = (float) Math.toRadians(60.0f); //todo: make this a calculator method ala calculateFOV(float);

    private static final float HUD_Z_NEAR = 0.001f;
    private static final float HUD_Z_FAR = 1120.f;

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1120.f;

    private static Vector2d windowSize = new Vector2d();

    private static Transformation transformation = new Transformation();

    private static ShaderProgram shaderProgram;

    private static ShaderProgram hudShaderProgram;

    public static Vector2d getWindowSize(){
        return windowSize;
    }

    public static void initRenderer() throws Exception{
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


        setWindowClearColor(0.53f,0.81f,0.92f,0.f);

        windowSize.x = getWindowWidth();
        windowSize.y = getWindowHeight();
    }

    public static void clearScreen(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void renderGame(){
        clearScreen();

        if (isWindowResized()){
            windowSize.x = getWindowWidth();
            windowSize.y = getWindowHeight();
            glViewport(0,0, getWindowWidth(), getWindowHeight());
            setWindowResized(false);
        }

        //todo: BEGIN WORLD SHADER PROGRAM!
        shaderProgram.bind();

        //update projection matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, getWindowWidth(), getWindowHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        //update the view matrix
        Matrix4f viewMatrix = transformation.getViewMatrix();

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

        Matrix4f hudProjectionMatrix = transformation.getProjectionMatrix(FOV, getWindowWidth(), getWindowHeight(), HUD_Z_NEAR, HUD_Z_FAR);
        hudShaderProgram.setUniform("projectionMatrix", hudProjectionMatrix);
        Matrix4f hudViewMatrix = new Matrix4f();
        hudShaderProgram.setUniform("texture_sampler", 0);



        //draw wield hand or item
        {
            if (getItemInInventorySlot(getPlayerInventorySelection(),0) == 0){
                Mesh thisMesh = getWieldHandMesh();
                Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPosRotationScale(getWieldHandAnimationPos(), getWieldHandAnimationRot(), new Vector3f(20f, 20f, 20f), hudViewMatrix);
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            } else {

                Mesh thisMesh = getItemMeshByBlock(getItemInInventorySlot(getPlayerInventorySelection(),0));

                Vector3f rot = new Vector3f(getWieldHandAnimationRot());
                Vector3f pos = new Vector3f(getWieldHandAnimationPos());

                rot.x += 130f;
                rot.y += -10f;
                rot.z += 20f;

                pos.x += -2f;
                pos.y += 0;

                Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPosRotationScale(pos, rot, new Vector3f(20f, 20f, 20f), hudViewMatrix);
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

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

                        Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPosRotationScale(new Vector3f(-7.35f + xer, -1.25f - yer, -14f), new Vector3f(-20 + (y * 3), -45, 0), new Vector3f(2.25f, 2.25f, 2.25f), hudViewMatrix);
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

                    Matrix4f modelViewMatrix = transformation.getGenericMatrixWithPosRotationScale(new Vector3f(-7.35f + xer, -7.5f, -14f), new Vector3f(-10, -45, 0), new Vector3f(2.25f, 2.25f, 2.25f), hudViewMatrix);
                    hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                    thisMesh.render();
                }
            }



        }
        hudShaderProgram.unbind();
    }

    public static void cleanupRenderer(){
        if (shaderProgram != null){
            shaderProgram.cleanup();
        }

        if (hudShaderProgram != null){
            hudShaderProgram.cleanup();
        }
    }
}
