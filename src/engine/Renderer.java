package engine;

import engine.graph.*;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static engine.MouseInput.getMousePos;
import static engine.graph.Transformation.*;
import static engine.graph.Transformation.buildOrthoProjModelMatrix;
import static game.chunk.Chunk.getChunkMesh;
import static game.chunk.Chunk.getLimit;
import static engine.Hud.*;
import static engine.ItemEntity.*;
import static engine.TNTEntity.*;
import static engine.Window.*;
import static game.player.Inventory.getItemInInventorySlot;
import static game.player.Inventory.getMouseInventorySlot;
import static game.player.Player.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private static float FOV = (float) Math.toRadians(72.0f); //todo: make this a calculator method ala calculateFOV(float);

    private static float HUD_FOV = (float)Math.toRadians(60f);

    private static final float HUD_Z_NEAR = 0.001f;
    private static final float HUD_Z_FAR = 1120.f;

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1120.f;

    private static float itemRotation = 0f;

    private static float windowScale = 0f;

    private static Vector2d windowSize = new Vector2d();

    private static ShaderProgram shaderProgram;

    private static ShaderProgram hudShaderProgram;

    public static Vector2d getWindowSize(){
        return windowSize;
    }

    private static void resetWindowScale(){
        if (windowSize.x <= windowSize.y){
            windowScale = (float)windowSize.x;
        } else {
            windowScale = (float) windowSize.y;
        }
        System.out.println("Window scale is now: " + windowScale);
    }

    public static float getWindowScale(){
        return windowScale;
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
//        hudShaderProgram.createUniform("projectionMatrix");
        //create uniforms for model view matrix
        hudShaderProgram.createUniform("modelViewMatrix");
        //create uniforms for texture sampler
        hudShaderProgram.createUniform("texture_sampler");


        setWindowClearColor(0.53f,0.81f,0.92f,0.f);

        windowSize.x = getWindowWidth();
        windowSize.y = getWindowHeight();

        resetWindowScale();
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
            resetWindowScale();
        }


        //todo: BEGIN WORLD SHADER PROGRAM!
        shaderProgram.bind();

        //update projection matrix
        Matrix4f projectionMatrix = getProjectionMatrix(FOV, getWindowWidth(), getWindowHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        //update the view matrix
        Matrix4f viewMatrix = getViewMatrix();

        shaderProgram.setUniform("texture_sampler", 0);

        //render each chunk
        for(int x = 0; x < getLimit(); x++) {
            for (int z = 0; z < getLimit(); z++) {

                Mesh thisMesh = getChunkMesh(x,z);

                if (thisMesh == null) {
                    System.out.println("wow that doesn't exist!");
                    continue;
                }

                Matrix4f modelViewMatrix = getModelViewMatrix(viewMatrix);
                shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

                thisMesh.render();
            }
        }


        //render each item entity
        for (int i = 0; i < getTotalObjects(); i++){
            if (!itemExists(i)){
                continue;
            }
            Matrix4f modelViewMatrix = getEntityModelViewMatrix(i, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            getItemMesh(i).render();
        }

        //render each TNT entity
        Mesh tntMesh = getTNTMesh();
        for (int i = 0; i < getTotalTNT(); i++){
            if (!tntExists(i)){
                continue;
            }
            Matrix4f modelViewMatrix = getTNTModelViewMatrix(i, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            tntMesh.render();
        }

        //render world selection mesh
        if (getPlayerWorldSelectionPos() != null){
            Mesh selectionMesh = getWorldSelectionMesh();
            Matrix4f modelViewMatrix = getWorldSelectionViewMatrix(getPlayerWorldSelectionPos(), viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            selectionMesh.render();
        }


        glClear(GL_DEPTH_BUFFER_BIT);

        //draw wield hand or item
        {
            if (getItemInInventorySlot(getPlayerInventorySelection(),0) == 0){
                Mesh thisMesh = getWieldHandMesh();
                Matrix4f modelViewMatrix = getGenericMatrixWithPosRotationScale(getWieldHandAnimationPos(), getWieldHandAnimationRot(), new Vector3f(5f, 5f, 5f), new Matrix4f());
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            } else {

                Mesh thisMesh = getItemMeshByBlock(getItemInInventorySlot(getPlayerInventorySelection(),0));
                Matrix4f modelViewMatrix = getGenericMatrixWithPosRotationScale(getWieldHandAnimationPos(), getWieldHandAnimationRot(), new Vector3f(20f, 20f, 20f), new Matrix4f());
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

        }

        shaderProgram.unbind();

        glClear(GL_DEPTH_BUFFER_BIT);



        //TODO: BEGIN HUD SHADER PROGRAM!
        hudShaderProgram.bind();

        hudShaderProgram.setUniform("texture_sampler", 0);

        resetOrthoProjectionMatrix(); // needed to get current screen size

        {
            Mesh thisMesh = getCrossHairMesh();
            Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f(0,0,0),new Vector3f(0,0,0), new Vector3f(windowScale/20f,windowScale/20f,windowScale/20f));
            hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            thisMesh.render();
        }


        glClear(GL_DEPTH_BUFFER_BIT);

        if (isPlayerInventoryOpen()) {
            {
                Mesh thisMesh = getInventoryMesh();
                Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(windowScale, windowScale, windowScale));
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            glClear(GL_DEPTH_BUFFER_BIT);

            {
                Mesh thisMesh = getPlayerMesh();
                Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f(-(windowScale/3.75f), (windowScale/2.8f), 0), getPlayerHudRotation(), new Vector3f((windowScale/18f), (windowScale/18f), (windowScale/18f)));
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            glClear(GL_DEPTH_BUFFER_BIT);

            {

                Mesh thisMesh = getInventorySlotMesh();
                Mesh selectionMesh = getInventorySlotSelectedMesh();
                //render the actual inventory
                for (int x = 1; x <= 9; x++) {
                    for (int y = -2; y > -5; y--) {
                        Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f((x - 5) * (windowScale / 9.5f), (y+0.3f) * (windowScale / 9.5f), 0), new Vector3f(0, 0, 0), new Vector3f(windowScale/10.5f, windowScale/10.5f, windowScale/10.5f));
                        hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

                        if (getInvSelection() != null && (x-1) == getInvSelection()[0] && ((y*-1) - 1) == getInvSelection()[1]) {
                            selectionMesh.render();
                        } else {
                            thisMesh.render();
                        }

                    }
                }

                //render the inventory hotbar (top row)
                for (int x = 1; x <= 9; x++) {
                    Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f((x - 5) * (windowScale / 9.5f), -0.5f * (windowScale / 9.5f), 0), new Vector3f(0, 0, 0), new Vector3f(windowScale/10.5f, windowScale/10.5f, windowScale/10.5f));
                    hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

                    if (getInvSelection() != null && (x-1) == getInvSelection()[0] && 0 == getInvSelection()[1]) {
                        selectionMesh.render();
                    } else {
                        thisMesh.render();
                    }
                }
            }


            glClear(GL_DEPTH_BUFFER_BIT);


            boolean itemSelected = false;

            //render items in inventory
            for (int x = 1; x <= 9; x++) {
                for (int y = -2; y > -5; y--) {
                    glClear(GL_DEPTH_BUFFER_BIT);

                    Matrix4f modelViewMatrix;

                    if (getInvSelection() != null && (x-1) == getInvSelection()[0] && ((y*-1) - 1) == getInvSelection()[1]) {
                        itemSelected = true;
                        modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f((x - 5) * (windowScale / 9.5f), ((y+0.3f) * (windowScale / 9.5f))-(windowScale / 55f), 0), new Vector3f(45, 45 + itemRotation, 0), new Vector3f(windowScale/8f, windowScale/8f, windowScale/8f));
                    } else {
                        modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f((x - 5) * (windowScale / 9.5f), ((y+0.3f) * (windowScale / 9.5f))-(windowScale / 55f), 0), new Vector3f(45, 45, 0), new Vector3f(windowScale/8f, windowScale/8f, windowScale/8f));
                    }
                    hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                    Mesh thisMesh = getItemMeshByBlock(getItemInInventorySlot(x-1,((y*-1) - 1)));
                    thisMesh.render();

                }
            }

            //render items in inventory hotbar (upper part)

            for (int x = 1; x <= 9; x++) {
                glClear(GL_DEPTH_BUFFER_BIT);
                Matrix4f modelViewMatrix;

                if (getInvSelection() != null && (x-1) == getInvSelection()[0] && 0 == getInvSelection()[1]) {
                    itemSelected = true;
                    modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f((x - 5) * (windowScale / 9.5f), (-0.5f * (windowScale / 9.5f))-(windowScale / 55f), 0), new Vector3f(45, 45 + itemRotation, 0), new Vector3f(windowScale/8f, windowScale/8f, windowScale/8f));
                } else {
                    modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f((x - 5) * (windowScale / 9.5f), (-0.5f * (windowScale / 9.5f))-(windowScale / 55f), 0), new Vector3f(45, 45, 0), new Vector3f(windowScale/8f, windowScale/8f, windowScale/8f));
                }

                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                Mesh thisMesh = getItemMeshByBlock(getItemInInventorySlot(x-1,0));
                thisMesh.render();
            }
            if (!itemSelected){
                itemRotation = 0f;
            } else {
                itemRotation += 1f;
            }


            //debug testing for rendered item
            {
                if (getMouseInventorySlot() != 0) {
                    glClear(GL_DEPTH_BUFFER_BIT);
                    //need to create new object or the mouse position gets messed up
                    Vector2d mousePos = new Vector2d(getMousePos());

                    //work from the center
                    mousePos.x -= (getWindowSize().x / 2f);
                    mousePos.y -= (getWindowSize().y / 2f);
                    mousePos.y *= -1f;

                    Mesh thisMesh = getItemMeshByBlock(getMouseInventorySlot());

                    Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f((float) mousePos.x, (float) mousePos.y - (windowScale / 55f), 0), new Vector3f(45, 45, 0), new Vector3f(windowScale/8f, windowScale/8f, windowScale/8f));
                    hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                    thisMesh.render();
                }
            }
        } else {


            {
                Mesh thisMesh = getHotBarMesh();
                Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f(0,(float)(-windowSize.y/2f) + (windowScale/16.5f),0),new Vector3f(0,0,0), new Vector3f((windowScale),(windowScale),(windowScale)));
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            glClear(GL_DEPTH_BUFFER_BIT);

            {
                Mesh thisMesh = getSelectionMesh();
                Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f((getPlayerInventorySelection()-4) * (windowScale/9.1f),(float)(-windowSize.y/2f)+((windowScale/8.25f) / 2f),0),new Vector3f(0,0,0), new Vector3f(windowScale/8.25f,windowScale/8.25f,windowScale/8.25f));
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            //THESE GO LAST!
            {
                Mesh thisMesh = getVersionInfoTextShadow();
                Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f((float)((-windowSize.x/2f)+(windowSize.x/600f)),(float)((windowSize.y/2f)-(windowSize.y/600f)),0),new Vector3f(0,0,0), new Vector3f(windowScale/30f,windowScale/30f,windowScale/30f));
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            glClear(GL_DEPTH_BUFFER_BIT);

            {
                Mesh thisMesh = getVersionInfoText();
                Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f((float)-windowSize.x/2f,(float)(windowSize.y/2f),0),new Vector3f(0,0,0), new Vector3f(windowScale/30f,windowScale/30f,windowScale/30f));
                hudShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                thisMesh.render();
            }

            //render items in hotbar
            for (int x = 1; x <= 9; x++){

                if (getItemInInventorySlot(x-1,0) != 0) {

                    glClear(GL_DEPTH_BUFFER_BIT);

                    Mesh thisMesh = getItemMeshByBlock(getItemInInventorySlot(x-1,0));

                    Matrix4f modelViewMatrix = buildOrthoProjModelMatrix(new Vector3f(((x - 5f) * (windowScale/9.1f)), (float)(-windowSize.y/2f)+(windowScale/24f), 0), new Vector3f(45, 45, 0), new Vector3f(windowScale/8f, windowScale/8f, windowScale/8f));
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
