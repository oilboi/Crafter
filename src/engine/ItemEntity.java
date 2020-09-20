package engine;

import engine.graph.Mesh;
import engine.graph.Texture;
import engine.sound.SoundManager;

import org.joml.Vector3f;

import java.util.ArrayList;

import static engine.Chunk.getLight;
import static engine.FancyMath.*;
import static engine.sound.SoundAPI.playSound;
import static game.blocks.BlockDefinition.*;
import static game.blocks.BlockDefinition.getBottomTexturePoints;
import static game.collision.Collision.applyInertia;
import static game.player.Player.getPlayerPosWithCollectionHeight;

public class ItemEntity {
    private final static float itemSize   = 0.4f;
    public final static int MAX_ID_AMOUNT = 126_000;
    private static Texture textureAtlas;

    private static int totalObjects       = 0;
    //TODO: pseudo object holder
    private static Mesh[] meshStorage  =     new Mesh[MAX_ID_AMOUNT];
    private static int[] thisMeshID    =      new int[MAX_ID_AMOUNT];
    private static Vector3f[] position = new Vector3f[MAX_ID_AMOUNT];
    private static float[] scale       =    new float[MAX_ID_AMOUNT];
    private static float[] hover       =    new float[MAX_ID_AMOUNT];
    private static float[] timer       =    new float[MAX_ID_AMOUNT];
    private static boolean[] floatUp =    new boolean[MAX_ID_AMOUNT];
    private static boolean[] exists =    new boolean[MAX_ID_AMOUNT];
    private static boolean[] collecting =    new boolean[MAX_ID_AMOUNT];
    private static Vector3f[] rotation = new Vector3f[MAX_ID_AMOUNT];
    private static Vector3f[] inertia  = new Vector3f[MAX_ID_AMOUNT];
    public static int getTotalObjects(){
        return totalObjects;
    }

    public static void createItem(int blockID, Vector3f pos){
        thisMeshID[totalObjects] = blockID;
        pos.x+=0.5f;
        pos.y+=0.5f;
        pos.z+=0.5f;
        position[totalObjects] = new Vector3f(pos);
        inertia[totalObjects] = new Vector3f(randomForceValue(9f),(float)Math.random()*10f,randomForceValue(9f));
        rotation[totalObjects] = new Vector3f(0,0,0);
        floatUp[totalObjects] = true;
        exists[totalObjects] = true;
        collecting[totalObjects] = false;
        scale[totalObjects] = 1f;
        timer[totalObjects] = 0f;
        totalObjects++;
        System.out.println("Created new Item. Total items: " + totalObjects);
    }

    public static void onStep(SoundManager soundMgr) throws Exception {
        for (int i = 0; i < totalObjects; i++){
            timer[i] += 0.001f;

            //delete items that are too old
            if (timer[i] > 10f){
                deleteItem(i);
            }

            if (itemExists(i) && timer[i] > 3){
                if (getDistance(position[i], getPlayerPosWithCollectionHeight()) < 3f){
                    if (collecting[i] == false){
                        /*soundMgr.playSoundSource(*/playSound("pickup", position[i])/*Crafter.Sounds.PICKUP.toString())*/;
                    }
                    collecting[i] = true;
                    Vector3f normalizedPos = new Vector3f(getPlayerPosWithCollectionHeight());
                    normalizedPos.sub(position[i]).normalize().mul(15f);
                    inertia[i] = normalizedPos;
                }

                if (getDistance(position[i], getPlayerPosWithCollectionHeight()) < 0.2f){
                    deleteItem(i);
                    continue;
                }
            }
            if (itemExists(i)) {
                if (collecting[i]) {
                    applyInertia(position[i], inertia[i], true, itemSize, itemSize * 2, false, false, false);
                } else {
                    applyInertia(position[i], inertia[i], true, itemSize, itemSize * 2, true, false, true);
                }
                rotation[i].y += 0.1f;
                if (floatUp[i]){
                    hover[i] += 0.00025f;
                    if (hover[i] >= 0.5f){
                        floatUp[i] = false;
                    }
                } else {
                    hover[i] -= 0.00025f;
                    if (hover[i] <= 0.0f){
                        floatUp[i] = true;
                    }
                }
            }

            if (itemExists(i) && position[i].y < 0){
                deleteItem(i);
            }
        }
    }


    private static void deleteItem(int ID){
        thisMeshID[ID] = 0;
        position[ID] = null;
        inertia[ID] = null;
        rotation[ID] = null;
        floatUp[ID] = false;
        collecting[ID] = false;
        exists[ID] = false;
        scale[ID] = 0;
        timer[ID] = 0;

        for ( int i = ID; i < totalObjects; i ++){
            thisMeshID[i] = thisMeshID[i+1];
            position[i] = position[i+1];
            inertia[i] = inertia[i+1];
            rotation[i] = rotation[i+1];
            floatUp[i] = floatUp[i+1];
            exists[i] = exists[i+1];
            collecting[i] = collecting[i+1];
            scale[i] = scale[i+1];
            timer[i] = timer[i+1];
            hover[i] = hover[i+1];
        }

        thisMeshID[totalObjects - 1] = 0;
        position[totalObjects - 1] = null;
        inertia[totalObjects - 1] = null;
        rotation[totalObjects - 1] = null;
        floatUp[totalObjects - 1] = false;
        collecting[totalObjects - 1] = false;
        exists[totalObjects - 1] = false;
        scale[totalObjects - 1] = 0;
        timer[totalObjects - 1] = 0;

        totalObjects -= 1;
//        System.out.println("An Item was Deleted. Remaining: " + totalObjects);
    }

    public static void clearItems(){
        for (int i = 0; i < totalObjects; i++){
            if(exists[i]){
                thisMeshID[i] = 0;
                position[i] = null;
                inertia[i] = null;
                rotation[i] = null;
                floatUp[i] = false;
                collecting[i] = false;
                exists[i] = false;
                scale[i] = 0;
                timer[i] = 0;
            }
        }
        totalObjects = 0;
    }

    public static boolean itemExists(int ID){
        return exists[ID];
    }

    public static Vector3f getPosition(int ID){
        return position[ID];
    }

    public static Vector3f getPositionWithHover(int ID){
        return new Vector3f(position[ID].x, position[ID].y + hover[ID], position[ID].z);
    }

    public static void setPosition(float x, float y, float z, int ID){
        position[ID].x = x;
        position[ID].y = y;
        position[ID].z = z;
    }

    public static float getScale(int ID){
        return scale[ID];
    }

    public static void setScale(float newScale, int ID){
        scale[ID] = newScale;
    }

    public static Vector3f getRotation(int ID){
        return rotation[ID];
    }

    public static void setRotation(float x, float y, float z, int ID){
        rotation[ID].x = x;
        rotation[ID].y = y;
        rotation[ID].z = z;
    }

    public static Mesh getMesh(int ID){
        return meshStorage[thisMeshID[ID]];
    }

    public static void initializeItemTextureAtlas() throws Exception {
        textureAtlas = new Texture("textures/textureAtlas.png");
    }

    public static void createBlockObjectMesh(int thisBlock) throws Exception {

        int indicesCount = 0;

        ArrayList positions     = new ArrayList();
        ArrayList textureCoord  = new ArrayList();
        ArrayList indices       = new ArrayList();
        ArrayList light         = new ArrayList();

        //create the mesh

        float thisLight = 1f;//(float)Math.pow(Math.pow(15,1.5),1.5);



        for (float[] thisBlockBox : getBlockShape(thisBlock)) {
            // 0, 1, 2, 3, 4, 5
            //-x,-y,-z, x, y, z
            // 0, 0, 0, 1, 1, 1

            //front
            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize + (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            //front
            for (int i = 0; i < 12; i++) {
                light.add(thisLight);
            }
            //front
            indices.add(0 + indicesCount);
            indices.add(1 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(0 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(3 + indicesCount);
            indicesCount += 4;

            // 0, 1,  2, 3
            //-x,+x, -y,+y

            float[] textureFront = getFrontTexturePoints(thisBlock);

            //front
            textureCoord.add(textureFront[1] - ((1-thisBlockBox[3])/32f)); //x positive
            textureCoord.add(textureFront[2] + ((1-thisBlockBox[4])/32f)); //y positive
            textureCoord.add(textureFront[0] - ((0-thisBlockBox[0])/32f)); //x negative
            textureCoord.add(textureFront[2] + ((1-thisBlockBox[4])/32f)); //y positive

            textureCoord.add(textureFront[0] - ((0-thisBlockBox[0])/32f)); //x negative
            textureCoord.add(textureFront[3] - ((thisBlockBox[1])/32f));   //y negative
            textureCoord.add(textureFront[1] - ((1-thisBlockBox[3])/32f)); //x positive
            textureCoord.add(textureFront[3] - ((thisBlockBox[1])/32f));   //y negative



            //back
            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);


            //back
            for (int i = 0; i < 12; i++) {
                light.add(thisLight);
            }
            //back
            indices.add(0 + indicesCount);
            indices.add(1 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(0 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(3 + indicesCount);
            indicesCount += 4;

            float[] textureBack = getBackTexturePoints(thisBlock);

            // 0, 1, 2, 3, 4, 5
            //-x,-y,-z, x, y, z
            // 0, 0, 0, 1, 1, 1

            // 0, 1,  2, 3
            //-x,+x, -y,+y


            //back
            textureCoord.add(textureBack[1] - ((1-thisBlockBox[0])/32f));
            textureCoord.add(textureBack[2] + ((1-thisBlockBox[4])/32f));
            textureCoord.add(textureBack[0] - ((0-thisBlockBox[3])/32f));
            textureCoord.add(textureBack[2] + ((1-thisBlockBox[4])/32f));

            textureCoord.add(textureBack[0] - ((0-thisBlockBox[3])/32f));
            textureCoord.add(textureBack[3] - ((  thisBlockBox[1])/32f));
            textureCoord.add(textureBack[1] - ((1-thisBlockBox[0])/32f));
            textureCoord.add(textureBack[3] - ((  thisBlockBox[1])/32f));





            //right
            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            //right
            for (int i = 0; i < 12; i++) {
                light.add(thisLight);
            }
            //right
            indices.add(0 + indicesCount);
            indices.add(1 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(0 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(3 + indicesCount);
            indicesCount += 4;


            // 0, 1, 2, 3, 4, 5
            //-x,-y,-z, x, y, z
            // 0, 0, 0, 1, 1, 1

            // 0, 1,  2, 3
            //-x,+x, -y,+y


            float[] textureRight = getRightTexturePoints(thisBlock);
            //right
            textureCoord.add(textureRight[1] - ((1-thisBlockBox[2])/32f));
            textureCoord.add(textureRight[2] + ((1-thisBlockBox[4])/32f));
            textureCoord.add(textureRight[0] - ((0-thisBlockBox[5])/32f));
            textureCoord.add(textureRight[2] + ((1-thisBlockBox[4])/32f));

            textureCoord.add(textureRight[0] - ((0-thisBlockBox[5])/32f));
            textureCoord.add(textureRight[3] - ((  thisBlockBox[1])/32f));
            textureCoord.add(textureRight[1] - ((1-thisBlockBox[2])/32f));
            textureCoord.add(textureRight[3] - ((  thisBlockBox[1])/32f));




            //left
            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            //left
            for (int i = 0; i < 12; i++) {
                light.add(thisLight);
            }
            //left
            indices.add(0 + indicesCount);
            indices.add(1 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(0 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(3 + indicesCount);
            indicesCount += 4;

            float[] textureLeft = getLeftTexturePoints(thisBlock);
            //left
            textureCoord.add(textureLeft[1] - ((1-thisBlockBox[5])/32f));
            textureCoord.add(textureLeft[2] + ((1-thisBlockBox[4])/32f));
            textureCoord.add(textureLeft[0] - ((0-thisBlockBox[2])/32f));
            textureCoord.add(textureLeft[2] + ((1-thisBlockBox[4])/32f));

            textureCoord.add(textureLeft[0] - ((0-thisBlockBox[2])/32f));
            textureCoord.add(textureLeft[3] - ((thisBlockBox[1])/32f));
            textureCoord.add(textureLeft[1] - ((1-thisBlockBox[5])/32f));
            textureCoord.add(textureLeft[3] - ((thisBlockBox[1])/32f));




            //top
            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[4] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            //top
            for (int i = 0; i < 12; i++) {
                light.add(thisLight);
            }
            //top
            indices.add(0 + indicesCount);
            indices.add(1 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(0 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(3 + indicesCount);
            indicesCount += 4;

            // 0, 1, 2, 3, 4, 5
            //-x,-y,-z, x, y, z
            // 0, 0, 0, 1, 1, 1

            // 0, 1,  2, 3
            //-x,+x, -y,+y

            float[] textureTop = getTopTexturePoints(thisBlock);
            //top
            textureCoord.add(textureTop[1] - ((1-thisBlockBox[5])/32f));
            textureCoord.add(textureTop[2] + ((1-thisBlockBox[0])/32f));
            textureCoord.add(textureTop[0] - ((0-thisBlockBox[2])/32f));
            textureCoord.add(textureTop[2] + ((1-thisBlockBox[0])/32f));

            textureCoord.add(textureTop[0] - ((0-thisBlockBox[2])/32f));
            textureCoord.add(textureTop[3] - ((  thisBlockBox[3])/32f));
            textureCoord.add(textureTop[1] - ((1-thisBlockBox[5])/32f));
            textureCoord.add(textureTop[3] - ((  thisBlockBox[3])/32f));



            //bottom
            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            positions.add((thisBlockBox[0] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[2] - 0.5f) * itemSize);

            positions.add((thisBlockBox[3] - 0.5f) * itemSize);
            positions.add((thisBlockBox[1] - 0.5f) * itemSize+ (itemSize/2));
            positions.add((thisBlockBox[5] - 0.5f) * itemSize);

            //bottom
            for (int i = 0; i < 12; i++) {
                light.add(thisLight);
            }
            //bottom
            indices.add(0 + indicesCount);
            indices.add(1 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(0 + indicesCount);
            indices.add(2 + indicesCount);
            indices.add(3 + indicesCount);
            indicesCount += 4;


            // 0, 1, 2, 3, 4, 5
            //-x,-y,-z, x, y, z
            // 0, 0, 0, 1, 1, 1

            // 0, 1,  2, 3
            //-x,+x, -y,+y

            float[] textureBottom = getBottomTexturePoints(thisBlock);
            //bottom
            textureCoord.add(textureBottom[1] - ((1-thisBlockBox[5])/32f));
            textureCoord.add(textureBottom[2] + ((1-thisBlockBox[0])/32f));
            textureCoord.add(textureBottom[0] - ((0-thisBlockBox[2])/32f));
            textureCoord.add(textureBottom[2] + ((1-thisBlockBox[0])/32f));

            textureCoord.add(textureBottom[0] - ((0-thisBlockBox[2])/32f));
            textureCoord.add(textureBottom[3] - ((  thisBlockBox[3])/32f));
            textureCoord.add(textureBottom[1] - ((1-thisBlockBox[5])/32f));
            textureCoord.add(textureBottom[3] - ((  thisBlockBox[3])/32f));

        }
        //todo: ------------------------------------------------------------------------------------------------=-=-=-=


        //convert the position objects into usable array
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = (float)positions.get(i);
        }

        //convert the light objects into usable array
        float[] lightArray = new float[light.size()];
        for (int i = 0; i < light.size(); i++) {
            lightArray[i] = (float)light.get(i);
        }

        //convert the indices objects into usable array
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = (int)indices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] textureCoordArray = new float[textureCoord.size()];
        for (int i = 0; i < textureCoord.size(); i++) {
            textureCoordArray[i] = (float)textureCoord.get(i);
        }

        Mesh mesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, textureAtlas);

        meshStorage[thisBlock] = mesh;
    }

    public static void cleanUp(){
        for (Mesh thisMesh : meshStorage){
            if (thisMesh != null){
                thisMesh.cleanUp(true);
            }
        }
    }
}