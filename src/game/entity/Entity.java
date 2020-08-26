package game.entity;

import engine.GameItem;
import engine.graph.Mesh;
import engine.graph.Texture;

import java.util.ArrayList;

import static game.ChunkHandling.ChunkData.getBlockInChunk;
import static game.ChunkHandling.ChunkData.getLightInChunk;
import static game.ChunkHandling.ChunkMath.genMapHash;
import static game.ChunkHandling.ChunkMath.mapHashInBounds;
import static game.blocks.BlockDefinition.*;
import static game.blocks.BlockDefinition.getBottomTexturePoints;

public class Entity {

    private final static float itemSize = 0.2f;
    private static int index = 0;

    public static void addItem(float x, float y, float z, ArrayList itemArray, int thisBlock) throws Exception {
        selfMeshFactory(x,y,z,itemArray,thisBlock);
    }

    private static void selfMeshFactory(float x, float y, float z, ArrayList itemArray, int thisBlock) throws Exception {

        int indicesCount = 0;

        ArrayList positions     = new ArrayList();
        ArrayList textureCoord  = new ArrayList();
        ArrayList indices       = new ArrayList();
        ArrayList light         = new ArrayList();


        //create the mesh

        float thisLight = 1f;//(float)Math.pow(Math.pow(15,1.5),1.5);


        //front
        positions.add( itemSize); positions.add( itemSize*2f); positions.add(itemSize);
        positions.add(-itemSize); positions.add( itemSize*2f); positions.add(itemSize);
        positions.add(-itemSize); positions.add(0f); positions.add(itemSize);
        positions.add( itemSize); positions.add(0f); positions.add(itemSize);
        //front
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //front
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;

        float[] textureFront = getFrontTexturePoints(thisBlock);
        //front
        textureCoord.add(textureFront[1]);textureCoord.add(textureFront[2]);
        textureCoord.add(textureFront[0]);textureCoord.add(textureFront[2]);
        textureCoord.add(textureFront[0]);textureCoord.add(textureFront[3]);
        textureCoord.add(textureFront[1]);textureCoord.add(textureFront[3]);



        //back
        positions.add(-itemSize); positions.add( itemSize*2f); positions.add(-itemSize);
        positions.add( itemSize); positions.add( itemSize*2f); positions.add(-itemSize);
        positions.add( itemSize); positions.add(0f); positions.add(-itemSize);
        positions.add(-itemSize); positions.add(0f); positions.add(-itemSize);
        //back

        //back
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //back
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;

        float[] textureBack = getBackTexturePoints(thisBlock);
        //back
        textureCoord.add(textureBack[1]);textureCoord.add(textureBack[2]);
        textureCoord.add(textureBack[0]);textureCoord.add(textureBack[2]);
        textureCoord.add(textureBack[0]);textureCoord.add(textureBack[3]);
        textureCoord.add(textureBack[1]);textureCoord.add(textureBack[3]);



        //right
        positions.add(itemSize); positions.add(itemSize*2f); positions.add(-itemSize );
        positions.add(itemSize); positions.add(itemSize*2f); positions.add(itemSize );
        positions.add(itemSize); positions.add(0f); positions.add(itemSize );
        positions.add(itemSize); positions.add(0f); positions.add(-itemSize );

        //right
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //right
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;

        float[] textureRight = getRightTexturePoints(thisBlock);
        //right
        textureCoord.add(textureRight[1]);textureCoord.add(textureRight[2]);
        textureCoord.add(textureRight[0]);textureCoord.add(textureRight[2]);
        textureCoord.add(textureRight[0]);textureCoord.add(textureRight[3]);
        textureCoord.add(textureRight[1]);textureCoord.add(textureRight[3]);



        //left
        positions.add(-itemSize ); positions.add(itemSize*2f); positions.add(itemSize);
        positions.add(-itemSize ); positions.add(itemSize*2f); positions.add(-itemSize);
        positions.add(-itemSize ); positions.add(0f); positions.add(-itemSize );
        positions.add(-itemSize ); positions.add(0f); positions.add(itemSize );

        //left
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //left
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;

        float[] textureLeft = getLeftTexturePoints(thisBlock);
        //left
        textureCoord.add(textureLeft[1]);textureCoord.add(textureLeft[2]);
        textureCoord.add(textureLeft[0]);textureCoord.add(textureLeft[2]);
        textureCoord.add(textureLeft[0]);textureCoord.add(textureLeft[3]);
        textureCoord.add(textureLeft[1]);textureCoord.add(textureLeft[3]);

        //top
        positions.add(-itemSize ); positions.add(itemSize*2f ); positions.add(-itemSize);
        positions.add(-itemSize ); positions.add(itemSize*2f ); positions.add(itemSize );
        positions.add(itemSize ); positions.add(itemSize*2f); positions.add(itemSize );
        positions.add(itemSize ); positions.add(itemSize*2f); positions.add(-itemSize );

        //top
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //top
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;

        float[] textureTop = getTopTexturePoints(thisBlock);
        //top
        textureCoord.add(textureTop[1]);textureCoord.add(textureTop[2]);
        textureCoord.add(textureTop[0]);textureCoord.add(textureTop[2]);
        textureCoord.add(textureTop[0]);textureCoord.add(textureTop[3]);
        textureCoord.add(textureTop[1]);textureCoord.add(textureTop[3]);


        //bottom
        positions.add(-itemSize ); positions.add(0f);positions.add(itemSize );
        positions.add(-itemSize); positions.add(0f);positions.add(-itemSize );
        positions.add(itemSize ); positions.add(0f);positions.add(-itemSize);
        positions.add(itemSize ); positions.add(0f);positions.add(itemSize );

        //bottom
        for (int i = 0; i < 12; i++){
            light.add(thisLight);
        }
        //bottom
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);

        float[] textureBottom = getBottomTexturePoints(thisBlock);
        //bottom
        textureCoord.add(textureBottom[1]);textureCoord.add(textureBottom[2]);
        textureCoord.add(textureBottom[0]);textureCoord.add(textureBottom[2]);
        textureCoord.add(textureBottom[0]);textureCoord.add(textureBottom[3]);
        textureCoord.add(textureBottom[1]);textureCoord.add(textureBottom[3]);


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

        Texture texture = new Texture("textures/textureAtlas.png");

        Mesh mesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, texture);


//        GameItem thisChunk = chunkArray[hash];
//
//        //prevent memory leak
//        if(thisChunk != null){
//            thisChunk.getMesh().cleanUp();
//        }

        GameItem thisThing = new GameItem(mesh, index + "");
        itemArray.add(thisThing);

        for (Object thisItem : itemArray){
            if (thisItem.equals(thisThing)){
                thisItem = (GameItem)thisItem;
                ((GameItem) thisItem).setPosition(x+0.5f,y+0.5f,z+0.5f);
                return;
            }
        }

    }
}
