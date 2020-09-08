package game.ChunkHandling;

import engine.graph.Mesh;
import engine.graph.Texture;

import java.util.ArrayList;

import static engine.Chunk.*;
import static game.blocks.BlockDefinition.*;

public class ChunkMesh {

    private static Texture textureAtlas;
    private final static float maxLight = 15;
    public static void initializeChunkTextureAtlas() throws Exception {
         textureAtlas = new Texture("textures/textureAtlas.png");
    }

    //caches
    private static int indicesCount;
    private static int offsetX;
    private static int offsetZ;

    public static void generateChunkMesh(int chunkX, int chunkZ, boolean updating) throws Exception {
        indicesCount = 0;

        ArrayList positions = new ArrayList();
        ArrayList textureCoord = new ArrayList();
        ArrayList indices = new ArrayList();
        ArrayList light = new ArrayList();

        offsetX = chunkX * 16;
        offsetZ = chunkZ * 16;

        //create the mesh
        for (int y = 0; y < 128; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {

                    int thisBlock = getBlock(x, y, z, chunkX, chunkZ);

                    if (thisBlock != 0) {
                        int neighborBlock = getBlock(x, y, z + 1, chunkX, chunkZ);
                        if (neighborBlock == 0) {
                            //front
                            positions.add(1f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(1f + z + offsetZ);
                            positions.add(0f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(1f + z + offsetZ);
                            positions.add(0f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(1f + z + offsetZ);
                            positions.add(1f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(1f + z + offsetZ);
                            //front
                            float frontLight = getLight(x, y, z + 1, chunkX, chunkZ) / maxLight;

                            frontLight = convertLight(frontLight);

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

                            float[] textureFront = getFrontTexturePoints(thisBlock);
                            //front
                            textureCoord.add(textureFront[1]);
                            textureCoord.add(textureFront[2]);
                            textureCoord.add(textureFront[0]);
                            textureCoord.add(textureFront[2]);
                            textureCoord.add(textureFront[0]);
                            textureCoord.add(textureFront[3]);
                            textureCoord.add(textureFront[1]);
                            textureCoord.add(textureFront[3]);
                        }


                        neighborBlock = getBlock(x, y, z - 1, chunkX, chunkZ);
                        if (neighborBlock == 0) {
                            //back
                            positions.add(0f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(0f + z + offsetZ);
                            positions.add(1f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(0f + z + offsetZ);
                            positions.add(1f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(0f + z + offsetZ);
                            positions.add(0f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(0f + z + offsetZ);
                            //back
                            float backLight = getLight(x, y, z - 1, chunkX, chunkZ) / maxLight;
                            backLight = convertLight(backLight);
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

                            float[] textureBack = getBackTexturePoints(thisBlock);
                            //back
                            textureCoord.add(textureBack[1]);
                            textureCoord.add(textureBack[2]);
                            textureCoord.add(textureBack[0]);
                            textureCoord.add(textureBack[2]);
                            textureCoord.add(textureBack[0]);
                            textureCoord.add(textureBack[3]);
                            textureCoord.add(textureBack[1]);
                            textureCoord.add(textureBack[3]);
                        }

                        neighborBlock = getBlock(x + 1, y, z, chunkX, chunkZ);
                        if (neighborBlock == 0) {
                            //right
                            positions.add(1f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(0f + z + offsetZ);
                            positions.add(1f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(1f + z + offsetZ);
                            positions.add(1f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(1f + z + offsetZ);
                            positions.add(1f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(0f + z + offsetZ);
                            //right
                            float rightLight = getLight(x + 1, y, z, chunkX, chunkZ) / maxLight;
                            rightLight = convertLight(rightLight);
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

                            float[] textureRight = getRightTexturePoints(thisBlock);
                            //right
                            textureCoord.add(textureRight[1]);
                            textureCoord.add(textureRight[2]);
                            textureCoord.add(textureRight[0]);
                            textureCoord.add(textureRight[2]);
                            textureCoord.add(textureRight[0]);
                            textureCoord.add(textureRight[3]);
                            textureCoord.add(textureRight[1]);
                            textureCoord.add(textureRight[3]);
                        }

                        neighborBlock = getBlock(x - 1, y, z, chunkX, chunkZ);
                        if (neighborBlock == 0) {
                            //left
                            positions.add(0f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(1f + z + offsetZ);
                            positions.add(0f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(0f + z + offsetZ);
                            positions.add(0f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(0f + z + offsetZ);
                            positions.add(0f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(1f + z + offsetZ);
                            //left
                            float leftLight = getLight(x - 1, y, z, chunkX, chunkZ) / maxLight;
                            leftLight = convertLight(leftLight);
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

                            float[] textureLeft = getLeftTexturePoints(thisBlock);
                            //left
                            textureCoord.add(textureLeft[1]);
                            textureCoord.add(textureLeft[2]);
                            textureCoord.add(textureLeft[0]);
                            textureCoord.add(textureLeft[2]);
                            textureCoord.add(textureLeft[0]);
                            textureCoord.add(textureLeft[3]);
                            textureCoord.add(textureLeft[1]);
                            textureCoord.add(textureLeft[3]);
                        }

                        neighborBlock = getBlock(x, y + 1, z, chunkX, chunkZ);
                        if (neighborBlock == 0) {
                            //top
                            positions.add(0f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(0f + z + offsetZ);
                            positions.add(0f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(1f + z + offsetZ);
                            positions.add(1f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(1f + z + offsetZ);
                            positions.add(1f + x + offsetX);
                            positions.add(1f + y);
                            positions.add(0f + z + offsetZ);
                            //top
                            float topLight = getLight(x, y + 1, z, chunkX, chunkZ) / maxLight;
                            topLight = convertLight(topLight);
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

                            float[] textureTop = getTopTexturePoints(thisBlock);
                            //top
                            textureCoord.add(textureTop[1]);
                            textureCoord.add(textureTop[2]);
                            textureCoord.add(textureTop[0]);
                            textureCoord.add(textureTop[2]);
                            textureCoord.add(textureTop[0]);
                            textureCoord.add(textureTop[3]);
                            textureCoord.add(textureTop[1]);
                            textureCoord.add(textureTop[3]);
                        }

                        neighborBlock = getBlock(x, y - 1, z, chunkX, chunkZ);
                        if (neighborBlock == 0 && y != 0) {
                            //bottom
                            positions.add(0f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(1f + z + offsetZ);
                            positions.add(0f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(0f + z + offsetZ);
                            positions.add(1f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(0f + z + offsetZ);
                            positions.add(1f + x + offsetX);
                            positions.add(0f + y);
                            positions.add(1f + z + offsetZ);
                            //bottom
                            float bottomLight = getLight(x, y - 1, z, chunkX, chunkZ) / maxLight;
                            bottomLight = convertLight(bottomLight);
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

                            float[] textureBottom = getBottomTexturePoints(thisBlock);
                            //bottom
                            textureCoord.add(textureBottom[1]);
                            textureCoord.add(textureBottom[2]);
                            textureCoord.add(textureBottom[0]);
                            textureCoord.add(textureBottom[2]);
                            textureCoord.add(textureBottom[0]);
                            textureCoord.add(textureBottom[3]);
                            textureCoord.add(textureBottom[1]);
                            textureCoord.add(textureBottom[3]);
                        }
                    }
                }
            }
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

        Mesh mesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, textureAtlas);

        setChunkMesh(chunkX, chunkZ, mesh);
    }

    private static float convertLight(float lightByte){
        return (float) Math.pow(Math.pow(lightByte, 1.5), 1.5);
    }
}
