package engine;

import engine.graph.Mesh;
import engine.graph.Texture;

import java.util.ArrayList;

import static engine.Chunk.getBlock;
import static engine.Chunk.getLight;
import static game.blocks.BlockDefinition.*;

public class Hud {
    private static Texture fontTextureAtlas;
    private static Texture hotBar;
    private static Texture selection;
    private static Texture mainInventory;
    private static Texture worldSelection;
    private static Texture crossHair;

//    private static Mesh thisDebugMesh;
    private static Mesh thisHotBarMesh;
    private static Mesh thisSelectionMesh;
    private static Mesh thisInventoryMesh;
    private static Mesh thisWorldSelectionMesh;
    private static Mesh thisCrossHairMesh;
    private static Mesh debugTestMesh;

    public static void initializeFontTextureAtlas() throws Exception {
        fontTextureAtlas = new Texture("textures/font.png");
        hotBar = new Texture("textures/hotbar.png");
        selection = new Texture("textures/hotbar_selected.png");
        mainInventory = new Texture("textures/inventory.png");
        worldSelection = new Texture("textures/selection.png");
        crossHair = new Texture("textures/crosshair.png");
    }

    private final static float scale = 1f;//0.00000002f;

    private static float currentScale = 1f;

//    public static Mesh getHudMesh(){
//        return thisDebugMesh;
//    }
//
    public static Mesh getHotBarMesh(){
        return thisHotBarMesh;
    }

    public static Mesh getSelectionMesh(){
        return thisSelectionMesh;
    }

    public static Mesh getInventoryMesh(){
        return thisInventoryMesh;
    }

    public static Mesh testTextMesh() {
        return debugTestMesh;
    }

    public static Mesh getWorldSelectionMesh(){
        return thisWorldSelectionMesh;
    }

    public static Mesh getCrossHairMesh(){
        return thisCrossHairMesh;
    }

    public static void createHudDebug(String text){
        createDebugHotbar();
        createInventory();
        createSelection(0);
        createWorldSelectionMesh();
        createCrossHair();

        createCustomHudText("wow", 1,0,0);
    }


    private static void createDebugHotbar(){
        ArrayList positions = new ArrayList();
        ArrayList textureCoord = new ArrayList();
        ArrayList indices = new ArrayList();
        ArrayList light = new ArrayList();


        int indicesCount = 0;


        float thisTest = -5f;

        float yPos = (-7.1f * scale * currentScale);

        //front
        positions.add(scale*8.27272727273f * currentScale);
        positions.add((scale * currentScale) + yPos);
        positions.add(thisTest);

        positions.add(-scale*8.27272727273f * currentScale);
        positions.add((scale * currentScale) + yPos);
        positions.add(thisTest);

        positions.add(-scale*8.27272727273f * currentScale);
        positions.add((-scale * currentScale) + yPos);
        positions.add(thisTest);

        positions.add(scale*8.27272727273f * currentScale);
        positions.add((-scale * currentScale) + yPos);
        positions.add(thisTest);
        //front
        float frontLight = 1f;//getLight(x, y, z + 1, chunkX, chunkZ) / maxLight;

        //front
        for (int i = 0; i < 12; i++) {
            light.add(frontLight);
        }
        //front
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);

        indicesCount += 4;

        //-x +x   -y +y
        // 0  1    2  3

        //front
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3


        //convert the position objects into usable array
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = (float) positions.get(i);
        }

        //convert the light objects into usable array
        float[] lightArray = new float[light.size()];
        for (int i = 0; i < light.size(); i++) {
            lightArray[i] = (float) light.get(i);
        }

        //convert the indices objects into usable array
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = (int) indices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] textureCoordArray = new float[textureCoord.size()];
        for (int i = 0; i < textureCoord.size(); i++) {
            textureCoordArray[i] = (float) textureCoord.get(i);
        }

        thisHotBarMesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, hotBar);
    }

    public static void createSelection(int currentInventorySelection){
        ArrayList positions = new ArrayList();
        ArrayList textureCoord = new ArrayList();
        ArrayList indices = new ArrayList();
        ArrayList light = new ArrayList();


        int indicesCount = 0;

        float yPos = (-7.1f * scale * currentScale);

        float thisTest = -5.f;

        float currentSelection = (currentInventorySelection-4f) * ((scale * currentScale) * 1.815f);
        //front
        positions.add((scale * currentScale) + currentSelection);
        positions.add((scale * currentScale) + yPos);
        positions.add(thisTest); //z (how close it is to screen)

        positions.add((-scale * currentScale) + currentSelection);
        positions.add((scale * currentScale) + yPos);
        positions.add(thisTest);

        positions.add((-scale * currentScale) + currentSelection);
        positions.add((-scale * currentScale) + yPos);
        positions.add(thisTest);

        positions.add((scale * currentScale) + currentSelection);
        positions.add((-scale * currentScale) + yPos);
        positions.add(thisTest);
        //front
        float frontLight = 1f;//getLight(x, y, z + 1, chunkX, chunkZ) / maxLight;

        //front
        for (int i = 0; i < 12; i++) {
            light.add(frontLight);
        }
        //front
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);

        indicesCount += 4;

        //-x +x   -y +y
        // 0  1    2  3

        //front
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3


        //convert the position objects into usable array
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = (float) positions.get(i);
        }

        //convert the light objects into usable array
        float[] lightArray = new float[light.size()];
        for (int i = 0; i < light.size(); i++) {
            lightArray[i] = (float) light.get(i);
        }

        //convert the indices objects into usable array
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = (int) indices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] textureCoordArray = new float[textureCoord.size()];
        for (int i = 0; i < textureCoord.size(); i++) {
            textureCoordArray[i] = (float) textureCoord.get(i);
        }
        thisSelectionMesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, selection);
    }

    private static void createInventory(){
        ArrayList positions = new ArrayList();
        ArrayList textureCoord = new ArrayList();
        ArrayList indices = new ArrayList();
        ArrayList light = new ArrayList();


        float thisScale = 8f * scale;

        float thisTest = -5f;

        int indicesCount = 0;

        float yPos = (0 * thisScale);

        //front
        positions.add(1.07908163265f * thisScale);
        positions.add(thisScale + yPos);
        positions.add(thisTest);

        positions.add(1.07908163265f * -thisScale);
        positions.add(thisScale + yPos);
        positions.add(thisTest);

        positions.add(1.07908163265f * -thisScale);
        positions.add(-thisScale + yPos);
        positions.add(thisTest);

        positions.add(1.07908163265f * thisScale);
        positions.add(-thisScale + yPos);
        positions.add(thisTest);
        //front
        float frontLight = 1f;//getLight(x, y, z + 1, chunkX, chunkZ) / maxLight;

        //front
        for (int i = 0; i < 12; i++) {
            light.add(frontLight);
        }
        //front
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);

        indicesCount += 4;

        //-x +x   -y +y
        // 0  1    2  3

        //front
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3


        //convert the position objects into usable array
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = (float) positions.get(i);
        }

        //convert the light objects into usable array
        float[] lightArray = new float[light.size()];
        for (int i = 0; i < light.size(); i++) {
            lightArray[i] = (float) light.get(i);
        }

        //convert the indices objects into usable array
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = (int) indices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] textureCoordArray = new float[textureCoord.size()];
        for (int i = 0; i < textureCoord.size(); i++) {
            textureCoordArray[i] = (float) textureCoord.get(i);
        }

        thisInventoryMesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, mainInventory);
    }

    private static void createWorldSelectionMesh() {

        float min = -0.01f;
        float max = 1.01f;
        int indicesCount = 0;

        ArrayList positions = new ArrayList();
        ArrayList textureCoord = new ArrayList();
        ArrayList indices = new ArrayList();
        ArrayList light = new ArrayList();

        //front
        positions.add(max);
        positions.add(max);
        positions.add(max);

        positions.add(min);
        positions.add(max);
        positions.add(max);

        positions.add(min);
        positions.add(min);
        positions.add(max);

        positions.add(max);
        positions.add(min);
        positions.add(max);

        //front
        float frontLight = 1f;

        //front
        for (int i = 0; i < 12; i++) {
            light.add(frontLight);
        }
        //front
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);
        indicesCount += 4;



        //-x +x  -y +y
        // 0  1   2  3
        //front
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3


        //todo///////////////////////////////////////////////////////

        //back
        positions.add(min);
        positions.add(max);
        positions.add(min);

        positions.add(max);
        positions.add(max);
        positions.add(min);

        positions.add(max);
        positions.add(min);
        positions.add(min);

        positions.add(min);
        positions.add(min);
        positions.add(min);
        //back
        float backLight = 2f;
        //back
        for (int i = 0; i < 12; i++) {
            light.add(backLight);
        }
        //back
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);
        indicesCount += 4;


        //-x +x  -y +y
        // 0  1   2  3
        //back
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3


        //todo///////////////////////////////////////////////////////

        //right
        positions.add(max);
        positions.add(max);
        positions.add(min);

        positions.add(max);
        positions.add(max);
        positions.add(max);

        positions.add(max);
        positions.add(min);
        positions.add(max);

        positions.add(max);
        positions.add(min);
        positions.add(min);
        //right
        float rightLight = 1f;

        //right
        for (int i = 0; i < 12; i++) {
            light.add(rightLight);
        }
        //right
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);
        indicesCount += 4;


        // 0  1   0  1
        // 0  1   2  3
        //right
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3


        //todo///////////////////////////////////////////////////////

        //left
        positions.add(min);
        positions.add(max);
        positions.add(max);

        positions.add(min);
        positions.add(max);
        positions.add(min);

        positions.add(min);
        positions.add(min);
        positions.add(min);

        positions.add(min);
        positions.add(min);
        positions.add(max);
        //left
        float leftLight = 1f;
        //left
        for (int i = 0; i < 12; i++) {
            light.add(leftLight);
        }
        //left
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);
        indicesCount += 4;


        //-x +x  -y +y
        // 0  1   2  3
        //left
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3


        //todo///////////////////////////////////////////////////////

        //top
        positions.add(min);
        positions.add(max);
        positions.add(min);

        positions.add(min);
        positions.add(max);
        positions.add(max);

        positions.add(max);
        positions.add(max);
        positions.add(max);

        positions.add(max);
        positions.add(max);
        positions.add(min);
        //top
        float topLight = 1f;
        //top
        for (int i = 0; i < 12; i++) {
            light.add(topLight);
        }
        //top
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);
        indicesCount += 4;


        //-x +x  -y +y
        // 0  1   2  3
        //top
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3


        //todo///////////////////////////////////////////////////////

        //bottom
        positions.add(min);
        positions.add(min);
        positions.add(max);

        positions.add(min);
        positions.add(min);
        positions.add(min);

        positions.add(max);
        positions.add(min);
        positions.add(min);

        positions.add(max);
        positions.add(min);
        positions.add(max);
        //bottom
        float bottomLight = 1f;

        //bottom
        for (int i = 0; i < 12; i++) {
            light.add(bottomLight);
        }
        //bottom
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);
        indicesCount += 4;


        //-x +x  -y +y
        // 0  1   2  3
        //bottom
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3

        //convert the position objects into usable array
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = (float) positions.get(i);
        }

        //convert the light objects into usable array
        float[] lightArray = new float[light.size()];
        for (int i = 0; i < light.size(); i++) {
            lightArray[i] = (float) light.get(i);
        }

        //convert the indices objects into usable array
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = (int) indices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] textureCoordArray = new float[textureCoord.size()];
        for (int i = 0; i < textureCoord.size(); i++) {
            textureCoordArray[i] = (float) textureCoord.get(i);
        }

        thisWorldSelectionMesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, worldSelection);
    }

    private static void createCrossHair(){
        ArrayList positions = new ArrayList();
        ArrayList textureCoord = new ArrayList();
        ArrayList indices = new ArrayList();
        ArrayList light = new ArrayList();


        int indicesCount = 0;
        float thisTest = -5f;

        //front
        positions.add(scale * currentScale);
        positions.add(scale * currentScale);
        positions.add(thisTest); //z (how close it is to screen)

        positions.add(-scale * currentScale);
        positions.add(scale * currentScale);
        positions.add(thisTest);

        positions.add(-scale * currentScale);
        positions.add(-scale * currentScale);
        positions.add(thisTest);

        positions.add(scale * currentScale);
        positions.add(-scale * currentScale);
        positions.add(thisTest);
        //front
        float frontLight = 1f;//getLight(x, y, z + 1, chunkX, chunkZ) / maxLight;

        //front
        for (int i = 0; i < 12; i++) {
            light.add(frontLight);
        }
        //front
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);

        indicesCount += 4;

        //-x +x   -y +y
        // 0  1    2  3

        //front
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3


        //convert the position objects into usable array
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = (float) positions.get(i);
        }

        //convert the light objects into usable array
        float[] lightArray = new float[light.size()];
        for (int i = 0; i < light.size(); i++) {
            lightArray[i] = (float) light.get(i);
        }

        //convert the indices objects into usable array
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = (int) indices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] textureCoordArray = new float[textureCoord.size()];
        for (int i = 0; i < textureCoord.size(); i++) {
            textureCoordArray[i] = (float) textureCoord.get(i);
        }

        thisCrossHairMesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, crossHair);
    }

    private static final float FONT_WIDTH = 216f;
    private static final float LETTER_WIDTH = 6f;

    private static final float FONT_HEIGHT = 16f;
    private static final float LETTER_HEIGHT = 8f;
    //what have I done?
    public static float[] translateCharToArray(char thisChar){
        float[] letterArray = new float[]{0,0};
        switch (thisChar){
            case 'a':
                letterArray[1] = 1;
                break;
            case 'A':
                break;

            case 'b':
                letterArray[0] = 1;
                letterArray[1] = 1;
                break;
            case 'B':
                letterArray[0] = 1;
                letterArray[1] = 0;
                break;

            case 'c':
                letterArray[0] = 2;
                letterArray[1] = 1;
                break;
            case 'C':
                letterArray[0] = 2;
                letterArray[1] = 0;
                break;

            case 'd':
                letterArray[0] = 3;
                letterArray[1] = 1;
                break;
            case 'D':
                letterArray[0] = 3;
                letterArray[1] = 0;
                break;

            case 'e':
                letterArray[0] = 4;
                letterArray[1] = 1;
                break;
            case 'E':
                letterArray[0] = 4;
                letterArray[1] = 0;
                break;

            case 'f':
                letterArray[0] = 5;
                letterArray[1] = 1;
                break;
            case 'F':
                letterArray[0] = 5;
                letterArray[1] = 0;
                break;

            case 'g':
                letterArray[0] = 6;
                letterArray[1] = 1;
                break;
            case 'G':
                letterArray[0] = 6;
                letterArray[1] = 0;
                break;

            case 'h':
                letterArray[0] = 7;
                letterArray[1] = 1;
                break;
            case 'H':
                letterArray[0] = 7;
                letterArray[1] = 0;
                break;

            case 'i':
                letterArray[0] = 8;
                letterArray[1] = 1;
                break;
            case 'I':
                letterArray[0] = 8;
                letterArray[1] = 0;
                break;

            case 'j':
                letterArray[0] = 9;
                letterArray[1] = 1;
                break;
            case 'J':
                letterArray[0] = 9;
                letterArray[1] = 0;
                break;

            case 'k':
                letterArray[0] = 10;
                letterArray[1] = 1;
                break;
            case 'K':
                letterArray[0] = 10;
                letterArray[1] = 0;
                break;

            case 'l':
                letterArray[0] = 11;
                letterArray[1] = 1;
                break;
            case 'L':
                letterArray[0] = 11;
                letterArray[1] = 0;
                break;

            case 'm':
                letterArray[0] = 12;
                letterArray[1] = 1;
                break;
            case 'M':
                letterArray[0] = 12;
                letterArray[1] = 0;
                break;

            case 'n':
                letterArray[0] = 13;
                letterArray[1] = 1;
                break;
            case 'N':
                letterArray[0] = 13;
                letterArray[1] = 0;
                break;

            case 'o':
                letterArray[0] = 14;
                letterArray[1] = 1;
                break;
            case 'O':
                letterArray[0] = 14;
                letterArray[1] = 0;
                break;

            case 'p':
                letterArray[0] = 15;
                letterArray[1] = 1;
                break;
            case 'P':
                letterArray[0] = 15;
                letterArray[1] = 0;
                break;

            case 'q':
                letterArray[0] = 16;
                letterArray[1] = 1;
                break;
            case 'Q':
                letterArray[0] = 16;
                letterArray[1] = 0;
                break;

            case 'r':
                letterArray[0] = 17;
                letterArray[1] = 1;
                break;
            case 'R':
                letterArray[0] = 17;
                letterArray[1] = 0;
                break;

            case 's':
                letterArray[0] = 18;
                letterArray[1] = 1;
                break;
            case 'S':
                letterArray[0] = 18;
                letterArray[1] = 0;
                break;

            case 't':
                letterArray[0] = 19;
                letterArray[1] = 1;
                break;
            case 'T':
                letterArray[0] = 19;
                letterArray[1] = 0;
                break;

            case 'u':
                letterArray[0] = 20;
                letterArray[1] = 1;
                break;
            case 'U':
                letterArray[0] = 20;
                letterArray[1] = 0;
                break;

            case 'v':
                letterArray[0] = 21;
                letterArray[1] = 1;
                break;
            case 'V':
                letterArray[0] = 21;
                letterArray[1] = 0;
                break;

            case 'w':
                letterArray[0] = 22;
                letterArray[1] = 1;
                break;
            case 'W':
                letterArray[0] = 22;
                letterArray[1] = 0;
                break;

            case 'x':
                letterArray[0] = 23;
                letterArray[1] = 1;
                break;
            case 'X':
                letterArray[0] = 23;
                letterArray[1] = 0;
                break;

            case 'y':
                letterArray[0] = 24;
                letterArray[1] = 1;
                break;
            case 'Y':
                letterArray[0] = 24;
                letterArray[1] = 0;
                break;

            case 'z':
                letterArray[0] = 25;
                letterArray[1] = 1;
                break;
            case 'Z':
                letterArray[0] = 25;
                letterArray[1] = 0;
                break;
                //now I know my ABCs

            case '0':
                letterArray[0] = 26;
                break;
            case '1':
                letterArray[0] = 27;
                break;
            case '2':
                letterArray[0] = 28;
                break;
            case '3':
                letterArray[0] = 29;
                break;
            case '4':
                letterArray[0] = 30;
                break;
            case '5':
                letterArray[0] = 31;
                break;
            case '6':
                letterArray[0] = 32;
                break;
            case '7':
                letterArray[0] = 33;
                break;
            case '8':
                letterArray[0] = 34;
                break;
            case '9':
                letterArray[0] = 35;
                break;
                //now you've learned to count, that will be 5 dollars

            case '.':
                letterArray[0] = 26;
                letterArray[1] = 1;
                break;
            case '!':
                letterArray[0] = 27;
                letterArray[1] = 1;
                break;
            case '?':
                letterArray[0] = 28;
                letterArray[1] = 1;
                break;
            default: //all unknown end up as "AAAAAAAAAA"  ¯\_(ツ)_/¯
                break;
        }

        float[] returningArray = new float[4];

        returningArray[0] = (letterArray[0] * LETTER_WIDTH) / FONT_WIDTH; //-x
        returningArray[1] = ((letterArray[0] * LETTER_WIDTH) + LETTER_WIDTH - 1) / FONT_WIDTH; //+x
        returningArray[2] = (letterArray[1] * LETTER_HEIGHT) / FONT_HEIGHT; //-y
        returningArray[3] = ((letterArray[1] * LETTER_HEIGHT) + LETTER_HEIGHT - 1) / FONT_HEIGHT; //+y

        return returningArray;
    }

    public static void createCustomHudText(String text, float r, float g, float b){

        float x = 0f;
        float z = 0f;


        ArrayList positions = new ArrayList();
        ArrayList textureCoord = new ArrayList();
        ArrayList indices = new ArrayList();
        ArrayList light = new ArrayList();


        int indicesCount = 0;

        float thisTest = -10f;

        for (char letter : text.toCharArray()) {
            System.out.println(letter);
            //front
            positions.add((scale * currentScale) + (x * scale * 2.25f));
            positions.add(scale * currentScale);
            positions.add(thisTest); //z (how close it is to screen)

            positions.add((-scale * currentScale) + (x * scale * 2.25f));
            positions.add(scale * currentScale);
            positions.add(thisTest);

            positions.add((-scale * currentScale) + (x * scale * 2.25f));
            positions.add(-scale * currentScale);
            positions.add(thisTest);

            positions.add((scale * currentScale) + (x  * scale * 2.25f));
            positions.add(-scale * currentScale);
            positions.add(thisTest);

            //front
            for (int i = 0; i < 4; i++) {
                light.add(r);
                light.add(g);
                light.add(b);
            }
            //front
            indices.add(0 + indicesCount);
            indices.add(1 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(0 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(3 + indicesCount);

            indicesCount += 4;

            //-x +x   -y +y
            // 0  1    2  3

            float[] thisCharacterArray = translateCharToArray(letter);

            //front
            textureCoord.add(thisCharacterArray[1]);//1
            textureCoord.add(thisCharacterArray[2]);//2
            textureCoord.add(thisCharacterArray[0]);//0
            textureCoord.add(thisCharacterArray[2]);//2
            textureCoord.add(thisCharacterArray[0]);//0
            textureCoord.add(thisCharacterArray[3]);//3
            textureCoord.add(thisCharacterArray[1]);//1
            textureCoord.add(thisCharacterArray[3]);//3

            x++;
        }


        //convert the position objects into usable array
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = (float) positions.get(i);
        }

        //convert the light objects into usable array
        float[] lightArray = new float[light.size()];
        for (int i = 0; i < light.size(); i++) {
            lightArray[i] = (float) light.get(i);
        }

        //convert the indices objects into usable array
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = (int) indices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] textureCoordArray = new float[textureCoord.size()];
        for (int i = 0; i < textureCoord.size(); i++) {
            textureCoordArray[i] = (float) textureCoord.get(i);
        }

        debugTestMesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, fontTextureAtlas);
    }
}
