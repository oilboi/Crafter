package game.chunk;

import engine.graph.Mesh;
import engine.graph.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import static game.chunk.Chunk.*;
import static game.blocks.BlockDefinition.*;

public class ChunkMesh {

    private static Texture textureAtlas;

    static {
        try {
            textureAtlas = new Texture("textures/textureAtlas.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Texture getTextureAtlas(){
        return textureAtlas;
    }

    private final static float maxLight = 15;


    public static void generateChunkMesh(int chunkX, int chunkZ, int yHeight) {

        ChunkObject thisChunk = getChunk(chunkX, chunkZ);

        if (thisChunk == null){
            return;
        }
        if (thisChunk.mesh == null || thisChunk.liquidMesh == null){
            return;
        }

        //normal block stuff
        float offsetX = chunkX * 16;
        float offsetZ = chunkZ * 16;

        float[] positions = new float[589_824];
        int positionsCount = 0;

        float[] textureCoord = new float[589_824];
        int textureCoordCount = 0;

        int[] indices = new int[589_824];
        int indicesTableCount = 0;
        int indicesCount = 0;

        float[] light = new float[589_824];
        int lightCount = 0;

        //liquid stuff
        //liquid stuff
        float[] liquidPositions = new float[24_576];
        int liquidPositionsCount = 0;

        float[] liquidTextureCoord = new float[24_576];
        int liquidTextureCoordCount = 0;

        int[] liquidIndices = new int[24_576];
        int liquidIndicesCount = 0;
        int liquidIndicesTableCount = 0;

        float[] liquidLight = new float[24_576];
        int liquidLightCount = 0;

        for (int x = 0; x < 16; x++) {
            int realX = (int)Math.floor(chunkX * 16f) + x;
            for (int z = 0; z < 16; z++) {
                int realZ = (int)Math.floor(chunkZ * 16f) + z;
                for (int y = yHeight * 16; y < (yHeight+1) * 16; y++) {

                    int thisBlock = thisChunk.block[y][x][z];

                    if (thisBlock > 0) {
                        //todo --------------------------------------- THE LIQUID DRAWTYPE
                        if (getIfLiquid(thisBlock)) {
                            int neighborBlock = getBlock(realX, y, realZ + 1);

                            if (neighborBlock >= 0 && !getBlockDrawType(neighborBlock).equals("normal")) {
                                //front
                                liquidPositions[liquidPositionsCount + 0] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 1] = (1f + y);
                                liquidPositions[liquidPositionsCount + 2] = (1f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 3] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 4] = (1f + y);
                                liquidPositions[liquidPositionsCount + 5] = (1f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 6] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 7] = (0f + y);
                                liquidPositions[liquidPositionsCount + 8] = (1f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 9] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 10] = (0f + y);
                                liquidPositions[liquidPositionsCount + 11] = (1f + z + offsetZ);

                                liquidPositionsCount += 12;

                                //front
                                float frontLight = getLight(realX, y, realZ + 1) / maxLight;

                                frontLight = convertLight(frontLight);

                                //front
                                for (int i = 0; i < 12; i++) {
                                    liquidLight[liquidLightCount + i] = (frontLight);
                                }

                                liquidLightCount += 12;

                                //front
                                liquidIndices[liquidIndicesTableCount + 0] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 1] = (1 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 2] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 3] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 4] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 5] = (3 + liquidIndicesCount);
                                liquidIndicesCount += 4;
                                liquidIndicesTableCount += 6;

                                float[] textureFront = getFrontTexturePoints(thisBlock);
                                //front
                                liquidTextureCoord[liquidTextureCoordCount + 0] = (textureFront[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 1] = (textureFront[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 2] = (textureFront[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 3] = (textureFront[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 4] = (textureFront[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 5] = (textureFront[3]);
                                liquidTextureCoord[liquidTextureCoordCount + 6] = (textureFront[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 7] = (textureFront[3]);
                                liquidTextureCoordCount += 8;
                            }


                            neighborBlock = getBlock(realX, y, realZ - 1);
                            if (neighborBlock >= 0 && !getBlockDrawType(neighborBlock).equals("normal")) {
                                //back
                                liquidPositions[liquidPositionsCount + 0] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 1] = (1f + y);
                                liquidPositions[liquidPositionsCount + 2] = (0f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 3] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 4] = (1f + y);
                                liquidPositions[liquidPositionsCount + 5] = (0f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 6] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 7] = (0f + y);
                                liquidPositions[liquidPositionsCount + 8] = (0f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 9] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 10] = (0f + y);
                                liquidPositions[liquidPositionsCount + 11] = (0f + z + offsetZ);

                                liquidPositionsCount += 12;

                                //back
                                float backLight = getLight(realX, y, realZ - 1) / maxLight;
                                backLight = convertLight(backLight);
                                //back
                                for (int i = 0; i < 12; i++) {
                                    liquidLight[liquidLightCount + i] = (backLight);
                                }

                                liquidLightCount += 12;

                                //back
                                liquidIndices[liquidIndicesTableCount + 0] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 1] = (1 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 2] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 3] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 4] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 5] = (3 + liquidIndicesCount);
                                liquidIndicesCount += 4;
                                liquidIndicesTableCount += 6;

                                float[] textureBack = getBackTexturePoints(thisBlock);
                                //back
                                liquidTextureCoord[liquidTextureCoordCount + 0] = (textureBack[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 1] = (textureBack[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 2] = (textureBack[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 3] = (textureBack[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 4] = (textureBack[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 5] = (textureBack[3]);
                                liquidTextureCoord[liquidTextureCoordCount + 6] = (textureBack[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 7] = (textureBack[3]);
                                liquidTextureCoordCount += 8;
                            }

                            neighborBlock = getBlock(realX + 1, y, realZ);

                            if (neighborBlock >= 0 && !getBlockDrawType(neighborBlock).equals("normal")) {
                                //right
                                liquidPositions[liquidPositionsCount + 0] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 1] = (1f + y);
                                liquidPositions[liquidPositionsCount + 2] = (0f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 3] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 4] = (1f + y);
                                liquidPositions[liquidPositionsCount + 5] = (1f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 6] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 7] = (0f + y);
                                liquidPositions[liquidPositionsCount + 8] = (1f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 9] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 10] = (0f + y);
                                liquidPositions[liquidPositionsCount + 11] = (0f + z + offsetZ);

                                liquidPositionsCount += 12;

                                //right
                                float rightLight = getLight(realX + 1, y, realZ) / maxLight;
                                rightLight = convertLight(rightLight);
                                //right
                                for (int i = 0; i < 12; i++) {
                                    liquidLight[liquidLightCount + i] = (rightLight);
                                }

                                liquidLightCount += 12;

                                //right
                                liquidIndices[liquidIndicesTableCount + 0] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 1] = (1 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 2] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 3] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 4] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 5] = (3 + liquidIndicesCount);
                                liquidIndicesCount += 4;
                                liquidIndicesTableCount += 6;

                                float[] textureRight = getRightTexturePoints(thisBlock);
                                //right
                                liquidTextureCoord[liquidTextureCoordCount + 0] = (textureRight[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 1] = (textureRight[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 2] = (textureRight[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 3] = (textureRight[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 4] = (textureRight[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 5] = (textureRight[3]);
                                liquidTextureCoord[liquidTextureCoordCount + 6] = (textureRight[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 7] = (textureRight[3]);
                                liquidTextureCoordCount += 8;
                            }

                            neighborBlock = getBlock(realX - 1, y, realZ);

                            if (neighborBlock >= 0 && !getBlockDrawType(neighborBlock).equals("normal")) {
                                //left
                                liquidPositions[liquidPositionsCount + 0] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 1] = (1f + y);
                                liquidPositions[liquidPositionsCount + 2] = (1f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 3] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 4] = (1f + y);
                                liquidPositions[liquidPositionsCount + 5] = (0f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 6] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 7] = (0f + y);
                                liquidPositions[liquidPositionsCount + 8] = (0f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 9] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 10] = (0f + y);
                                liquidPositions[liquidPositionsCount + 11] = (1f + z + offsetZ);

                                liquidPositionsCount += 12;

                                //left
                                float leftLight = getLight(realX - 1, y, realZ) / maxLight;
                                leftLight = convertLight(leftLight);
                                //left
                                for (int i = 0; i < 12; i++) {
                                    liquidLight[liquidLightCount + i] = (leftLight);
                                }

                                liquidLightCount += 12;

                                //left
                                liquidIndices[liquidIndicesTableCount + 0] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 1] = (1 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 2] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 3] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 4] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 5] = (3 + liquidIndicesCount);
                                liquidIndicesCount += 4;
                                liquidIndicesTableCount += 6;

                                float[] textureLeft = getLeftTexturePoints(thisBlock);
                                //left
                                liquidTextureCoord[liquidTextureCoordCount + 0] = (textureLeft[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 1] = (textureLeft[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 2] = (textureLeft[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 3] = (textureLeft[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 4] = (textureLeft[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 5] = (textureLeft[3]);
                                liquidTextureCoord[liquidTextureCoordCount + 6] = (textureLeft[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 7] = (textureLeft[3]);
                                liquidTextureCoordCount += 8;
                            }

                            neighborBlock = getBlock(realX, y + 1, realZ);

                            if (y == 127 || (neighborBlock >= 0 && !getBlockDrawType(neighborBlock).equals("normal"))) {
                                //top
                                liquidPositions[liquidPositionsCount + 0] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 1] = (1f + y);
                                liquidPositions[liquidPositionsCount + 2] = (0f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 3] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 4] = (1f + y);
                                liquidPositions[liquidPositionsCount + 5] = (1f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 6] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 7] = (1f + y);
                                liquidPositions[liquidPositionsCount + 8] = (1f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 9] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 10] = (1f + y);
                                liquidPositions[liquidPositionsCount + 11] = (0f + z + offsetZ);

                                liquidPositionsCount += 12;

                                //top
                                float topLight = getLight(realX, y + 1, realZ) / maxLight;
                                topLight = convertLight(topLight);
                                //top
                                for (int i = 0; i < 12; i++) {
                                    liquidLight[liquidLightCount + i] = (topLight);
                                }

                                liquidLightCount += 12;

                                //top
                                liquidIndices[liquidIndicesTableCount + 0] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 1] = (1 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 2] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 3] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 4] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 5] = (3 + liquidIndicesCount);
                                liquidIndicesCount += 4;
                                liquidIndicesTableCount += 6;

                                float[] textureTop = getTopTexturePoints(thisBlock);
                                //top
                                liquidTextureCoord[liquidTextureCoordCount + 0] = (textureTop[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 1] = (textureTop[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 2] = (textureTop[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 3] = (textureTop[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 4] = (textureTop[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 5] = (textureTop[3]);
                                liquidTextureCoord[liquidTextureCoordCount + 6] = (textureTop[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 7] = (textureTop[3]);
                                liquidTextureCoordCount += 8;
                            }

                            neighborBlock = getBlock(realX, y - 1, realZ);

                            if (neighborBlock >= 0 && !getBlockDrawType(neighborBlock).equals("normal") && y != 0) {
                                //bottom
                                liquidPositions[liquidPositionsCount + 0] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 1] = (0f + y);
                                liquidPositions[liquidPositionsCount + 2] = (1f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 3] = (0f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 4] = (0f + y);
                                liquidPositions[liquidPositionsCount + 5] = (0f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 6] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 7] = (0f + y);
                                liquidPositions[liquidPositionsCount + 8] = (0f + z + offsetZ);

                                liquidPositions[liquidPositionsCount + 9] = (1f + x + offsetX);
                                liquidPositions[liquidPositionsCount + 10] = (0f + y);
                                liquidPositions[liquidPositionsCount + 11] = (1f + z + offsetZ);

                                liquidPositionsCount += 12;

                                //bottom
                                float bottomLight = getLight(realX, y - 1, realZ) / maxLight;
                                bottomLight = convertLight(bottomLight);
                                //bottom
                                for (int i = 0; i < 12; i++) {
                                    liquidLight[liquidLightCount + i] = (bottomLight);
                                }

                                liquidLightCount += 12;

                                //bottom
                                liquidIndices[liquidIndicesTableCount + 0] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 1] = (1 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 2] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 3] = (0 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 4] = (2 + liquidIndicesCount);
                                liquidIndices[liquidIndicesTableCount + 5] = (3 + liquidIndicesCount);
                                liquidIndicesCount += 4;
                                liquidIndicesTableCount += 6;

                                float[] textureBottom = getBottomTexturePoints(thisBlock);
                                //bottom
                                liquidTextureCoord[liquidTextureCoordCount + 0] = (textureBottom[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 1] = (textureBottom[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 2] = (textureBottom[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 3] = (textureBottom[2]);
                                liquidTextureCoord[liquidTextureCoordCount + 4] = (textureBottom[0]);
                                liquidTextureCoord[liquidTextureCoordCount + 5] = (textureBottom[3]);
                                liquidTextureCoord[liquidTextureCoordCount + 6] = (textureBottom[1]);
                                liquidTextureCoord[liquidTextureCoordCount + 7] = (textureBottom[3]);
                                liquidTextureCoordCount += 8;
                            }
                        }

                        //todo --------------------------------------- THE NORMAL DRAWTYPE
                        else if (getBlockDrawType(thisBlock).equals("normal")) {

                            int neighborBlock = getBlock(realX, y, realZ + 1);

                            if (neighborBlock >= 0 && (!getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock))) {
                                //front
                                positions[positionsCount + 0] = (1f + x + offsetX);
                                positions[positionsCount + 1] = (1f + y);
                                positions[positionsCount + 2] = (1f + z + offsetZ);

                                positions[positionsCount + 3] = (0f + x + offsetX);
                                positions[positionsCount + 4] = (1f + y);
                                positions[positionsCount + 5] = (1f + z + offsetZ);

                                positions[positionsCount + 6] = (0f + x + offsetX);
                                positions[positionsCount + 7] = (0f + y);
                                positions[positionsCount + 8] = (1f + z + offsetZ);

                                positions[positionsCount + 9] = (1f + x + offsetX);
                                positions[positionsCount + 10] = (0f + y);
                                positions[positionsCount + 11] = (1f + z + offsetZ);

                                positionsCount += 12;

                                //front
                                float frontLight = getLight(realX, y, realZ + 1) / maxLight;

                                frontLight = convertLight(frontLight);

                                //front
                                for (int i = 0; i < 12; i++) {
                                    light[i + lightCount] = (frontLight);
                                }

                                lightCount += 12;


                                //front
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);

                                indicesCount += 4;
                                indicesTableCount += 6;

                                float[] textureFront = getFrontTexturePoints(thisBlock);
                                //front
                                textureCoord[textureCoordCount + 0] = (textureFront[1]);
                                textureCoord[textureCoordCount + 1] = (textureFront[2]);
                                textureCoord[textureCoordCount + 2] = (textureFront[0]);
                                textureCoord[textureCoordCount + 3] = (textureFront[2]);
                                textureCoord[textureCoordCount + 4] = (textureFront[0]);
                                textureCoord[textureCoordCount + 5] = (textureFront[3]);
                                textureCoord[textureCoordCount + 6] = (textureFront[1]);
                                textureCoord[textureCoordCount + 7] = (textureFront[3]);
                                textureCoordCount += 8;
                            }


                            neighborBlock = getBlock(realX, y, realZ - 1);
                            if (neighborBlock >= 0 && (!getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock))) {
                                //back
                                positions[positionsCount + 0] = (0f + x + offsetX);
                                positions[positionsCount + 1] = (1f + y);
                                positions[positionsCount + 2] = (0f + z + offsetZ);

                                positions[positionsCount + 3] = (1f + x + offsetX);
                                positions[positionsCount + 4] = (1f + y);
                                positions[positionsCount + 5] = (0f + z + offsetZ);

                                positions[positionsCount + 6] = (1f + x + offsetX);
                                positions[positionsCount + 7] = (0f + y);
                                positions[positionsCount + 8] = (0f + z + offsetZ);

                                positions[positionsCount + 9] = (0f + x + offsetX);
                                positions[positionsCount + 10] = (0f + y);
                                positions[positionsCount + 11] = (0f + z + offsetZ);

                                positionsCount += 12;

                                //back
                                float backLight = getLight(realX, y, realZ - 1) / maxLight;
                                backLight = convertLight(backLight);
                                //back
                                for (int i = 0; i < 12; i++) {
                                    light[i + lightCount] = (backLight);
                                }

                                lightCount += 12;

                                //back
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;

                                float[] textureBack = getBackTexturePoints(thisBlock);
                                //back
                                textureCoord[textureCoordCount + 0] = (textureBack[1]);
                                textureCoord[textureCoordCount + 1] = (textureBack[2]);
                                textureCoord[textureCoordCount + 2] = (textureBack[0]);
                                textureCoord[textureCoordCount + 3] = (textureBack[2]);
                                textureCoord[textureCoordCount + 4] = (textureBack[0]);
                                textureCoord[textureCoordCount + 5] = (textureBack[3]);
                                textureCoord[textureCoordCount + 6] = (textureBack[1]);
                                textureCoord[textureCoordCount + 7] = (textureBack[3]);
                                textureCoordCount += 8;
                            }

                            neighborBlock = getBlock(realX + 1, y, realZ);
                            if (neighborBlock >= 0 && (!getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock))) {
                                //right
                                positions[positionsCount + 0] = (1f + x + offsetX);
                                positions[positionsCount + 1] = (1f + y);
                                positions[positionsCount + 2] = (0f + z + offsetZ);

                                positions[positionsCount + 3] = (1f + x + offsetX);
                                positions[positionsCount + 4] = (1f + y);
                                positions[positionsCount + 5] = (1f + z + offsetZ);

                                positions[positionsCount + 6] = (1f + x + offsetX);
                                positions[positionsCount + 7] = (0f + y);
                                positions[positionsCount + 8] = (1f + z + offsetZ);

                                positions[positionsCount + 9] = (1f + x + offsetX);
                                positions[positionsCount + 10] = (0f + y);
                                positions[positionsCount + 11] = (0f + z + offsetZ);

                                positionsCount += 12;

                                //right
                                float rightLight = getLight(realX + 1, y, realZ) / maxLight;
                                rightLight = convertLight(rightLight);
                                //right
                                for (int i = 0; i < 12; i++) {
                                    light[i + lightCount] = (rightLight);
                                }

                                lightCount += 12;

                                //right
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;

                                float[] textureRight = getRightTexturePoints(thisBlock);
                                //right
                                textureCoord[textureCoordCount + 0] = (textureRight[1]);
                                textureCoord[textureCoordCount + 1] = (textureRight[2]);
                                textureCoord[textureCoordCount + 2] = (textureRight[0]);
                                textureCoord[textureCoordCount + 3] = (textureRight[2]);
                                textureCoord[textureCoordCount + 4] = (textureRight[0]);
                                textureCoord[textureCoordCount + 5] = (textureRight[3]);
                                textureCoord[textureCoordCount + 6] = (textureRight[1]);
                                textureCoord[textureCoordCount + 7] = (textureRight[3]);
                                textureCoordCount += 8;
                            }

                            neighborBlock = getBlock(realX - 1, y, realZ);
                            if (neighborBlock >= 0 && (!getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock))) {
                                //left
                                positions[positionsCount + 0] = (0f + x + offsetX);
                                positions[positionsCount + 1] = (1f + y);
                                positions[positionsCount + 2] = (1f + z + offsetZ);

                                positions[positionsCount + 3] = (0f + x + offsetX);
                                positions[positionsCount + 4] = (1f + y);
                                positions[positionsCount + 5] = (0f + z + offsetZ);

                                positions[positionsCount + 6] = (0f + x + offsetX);
                                positions[positionsCount + 7] = (0f + y);
                                positions[positionsCount + 8] = (0f + z + offsetZ);

                                positions[positionsCount + 9] = (0f + x + offsetX);
                                positions[positionsCount + 10] = (0f + y);
                                positions[positionsCount + 11] = (1f + z + offsetZ);

                                positionsCount += 12;

                                //left
                                float leftLight = getLight(realX - 1, y, realZ) / maxLight;
                                leftLight = convertLight(leftLight);
                                //left
                                for (int i = 0; i < 12; i++) {
                                    light[i + lightCount] = (leftLight);
                                }

                                lightCount += 12;

                                //left
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;

                                float[] textureLeft = getLeftTexturePoints(thisBlock);
                                //left
                                textureCoord[textureCoordCount + 0] = (textureLeft[1]);
                                textureCoord[textureCoordCount + 1] = (textureLeft[2]);
                                textureCoord[textureCoordCount + 2] = (textureLeft[0]);
                                textureCoord[textureCoordCount + 3] = (textureLeft[2]);
                                textureCoord[textureCoordCount + 4] = (textureLeft[0]);
                                textureCoord[textureCoordCount + 5] = (textureLeft[3]);
                                textureCoord[textureCoordCount + 6] = (textureLeft[1]);
                                textureCoord[textureCoordCount + 7] = (textureLeft[3]);
                                textureCoordCount += 8;
                            }

                            neighborBlock = getBlock(realX, y + 1, realZ);
                            if (y == 127 || (neighborBlock >= 0 && !getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock))) {
                                //top
                                positions[positionsCount + 0] = (0f + x + offsetX);
                                positions[positionsCount + 1] = (1f + y);
                                positions[positionsCount + 2] = (0f + z + offsetZ);

                                positions[positionsCount + 3] = (0f + x + offsetX);
                                positions[positionsCount + 4] = (1f + y);
                                positions[positionsCount + 5] = (1f + z + offsetZ);

                                positions[positionsCount + 6] = (1f + x + offsetX);
                                positions[positionsCount + 7] = (1f + y);
                                positions[positionsCount + 8] = (1f + z + offsetZ);

                                positions[positionsCount + 9] = (1f + x + offsetX);
                                positions[positionsCount + 10] = (1f + y);
                                positions[positionsCount + 11] = (0f + z + offsetZ);

                                positionsCount += 12;

                                //top
                                float topLight = getLight(realX, y + 1, realZ) / maxLight;
                                topLight = convertLight(topLight);
                                //top
                                for (int i = 0; i < 12; i++) {
                                    light[i + lightCount] = (topLight);
                                }

                                lightCount += 12;

                                //top
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;

                                float[] textureTop = getTopTexturePoints(thisBlock);
                                //top
                                textureCoord[textureCoordCount + 0] = (textureTop[1]);
                                textureCoord[textureCoordCount + 1] = (textureTop[2]);
                                textureCoord[textureCoordCount + 2] = (textureTop[0]);
                                textureCoord[textureCoordCount + 3] = (textureTop[2]);
                                textureCoord[textureCoordCount + 4] = (textureTop[0]);
                                textureCoord[textureCoordCount + 5] = (textureTop[3]);
                                textureCoord[textureCoordCount + 6] = (textureTop[1]);
                                textureCoord[textureCoordCount + 7] = (textureTop[3]);
                                textureCoordCount += 8;
                            }

                            neighborBlock = getBlock(realX, y - 1, realZ);
                            if (neighborBlock >= 0 && (!getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock)) && y != 0) {
                                //bottom
                                positions[positionsCount + 0] = (0f + x + offsetX);
                                positions[positionsCount + 1] = (0f + y);
                                positions[positionsCount + 2] = (1f + z + offsetZ);

                                positions[positionsCount + 3] = (0f + x + offsetX);
                                positions[positionsCount + 4] = (0f + y);
                                positions[positionsCount + 5] = (0f + z + offsetZ);

                                positions[positionsCount + 6] = (1f + x + offsetX);
                                positions[positionsCount + 7] = (0f + y);
                                positions[positionsCount + 8] = (0f + z + offsetZ);

                                positions[positionsCount + 9] = (1f + x + offsetX);
                                positions[positionsCount + 10] = (0f + y);
                                positions[positionsCount + 11] = (1f + z + offsetZ);

                                positionsCount += 12;

                                //bottom
                                float bottomLight = getLight(realX, y - 1, realZ) / maxLight;
                                bottomLight = convertLight(bottomLight);
                                //bottom
                                for (int i = 0; i < 12; i++) {
                                    light[i + lightCount] = (bottomLight);
                                }

                                lightCount += 12;

                                //bottom
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;

                                float[] textureBottom = getBottomTexturePoints(thisBlock);
                                //bottom
                                textureCoord[textureCoordCount + 0] = (textureBottom[1]);
                                textureCoord[textureCoordCount + 1] = (textureBottom[2]);
                                textureCoord[textureCoordCount + 2] = (textureBottom[0]);
                                textureCoord[textureCoordCount + 3] = (textureBottom[2]);
                                textureCoord[textureCoordCount + 4] = (textureBottom[0]);
                                textureCoord[textureCoordCount + 5] = (textureBottom[3]);
                                textureCoord[textureCoordCount + 6] = (textureBottom[1]);
                                textureCoord[textureCoordCount + 7] = (textureBottom[3]);
                                textureCoordCount += 8;
                            }
                            //todo: ---------------------------------------------------------- the block box draw type
                        } else {
                            for (float[] thisBlockBox : getBlockShape(thisBlock)) {
                                // 0, 1, 2, 3, 4, 5
                                //-x,-y,-z, x, y, z
                                // 0, 0, 0, 1, 1, 1

                                //front
                                positions[positionsCount + 0] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 1] = (thisBlockBox[4] + y);
                                positions[positionsCount + 2] = (thisBlockBox[5] + z + offsetZ);

                                positions[positionsCount + 3] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 4] = (thisBlockBox[4] + y);
                                positions[positionsCount + 5] = (thisBlockBox[5] + z + offsetZ);

                                positions[positionsCount + 6] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 7] = (thisBlockBox[1] + y);
                                positions[positionsCount + 8] = (thisBlockBox[5] + z + offsetZ);

                                positions[positionsCount + 9] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 10] = (thisBlockBox[1] + y);
                                positions[positionsCount + 11] = (thisBlockBox[5] + z + offsetZ);

                                positionsCount += 12;

                                //front
                                float frontLight = getLight(realX, y, realZ + 1) / maxLight;

                                frontLight = convertLight(frontLight);

                                //front
                                for (int i = 0; i < 12; i++) {
                                    light[lightCount + i] = (frontLight);
                                }

                                lightCount += 12;

                                //front
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;

                                // 0, 1,  2, 3
                                //-x,+x, -y,+y

                                float[] textureFront = getFrontTexturePoints(thisBlock);

                                //front
                                textureCoord[textureCoordCount + 0] = (textureFront[1] - ((1 - thisBlockBox[3]) / 32f)); //x positive
                                textureCoord[textureCoordCount + 1] = (textureFront[2] + ((1 - thisBlockBox[4]) / 32f)); //y positive
                                textureCoord[textureCoordCount + 2] = (textureFront[0] - ((0 - thisBlockBox[0]) / 32f)); //x negative
                                textureCoord[textureCoordCount + 3] = (textureFront[2] + ((1 - thisBlockBox[4]) / 32f)); //y positive

                                textureCoord[textureCoordCount + 4] = (textureFront[0] - ((0 - thisBlockBox[0]) / 32f)); //x negative
                                textureCoord[textureCoordCount + 5] = (textureFront[3] - ((thisBlockBox[1]) / 32f));   //y negative
                                textureCoord[textureCoordCount + 6] = (textureFront[1] - ((1 - thisBlockBox[3]) / 32f)); //x positive
                                textureCoord[textureCoordCount + 7] = (textureFront[3] - ((thisBlockBox[1]) / 32f));   //y negative

                                textureCoordCount += 8;


                                //back
                                positions[positionsCount + 0] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 1] = (thisBlockBox[4] + y);
                                positions[positionsCount + 2] = (thisBlockBox[2] + z + offsetZ);

                                positions[positionsCount + 3] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 4] = (thisBlockBox[4] + y);
                                positions[positionsCount + 5] = (thisBlockBox[2] + z + offsetZ);

                                positions[positionsCount + 6] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 7] = (thisBlockBox[1] + y);
                                positions[positionsCount + 8] = (thisBlockBox[2] + z + offsetZ);

                                positions[positionsCount + 9] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 10] = (thisBlockBox[1] + y);
                                positions[positionsCount + 11] = (thisBlockBox[2] + z + offsetZ);

                                positionsCount += 12;


                                //back
                                float backLight = getLight(realX, y, realZ - 1) / maxLight;
                                backLight = convertLight(backLight);
                                //back
                                for (int i = 0; i < 12; i++) {
                                    light[lightCount + i] = (backLight);
                                }

                                lightCount += 12;

                                //back
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;

                                float[] textureBack = getBackTexturePoints(thisBlock);

                                // 0, 1, 2, 3, 4, 5
                                //-x,-y,-z, x, y, z
                                // 0, 0, 0, 1, 1, 1

                                // 0, 1,  2, 3
                                //-x,+x, -y,+y


                                //back
                                textureCoord[textureCoordCount + 0] = (textureBack[1] - ((1 - thisBlockBox[0]) / 32f));
                                textureCoord[textureCoordCount + 1] = (textureBack[2] + ((1 - thisBlockBox[4]) / 32f));
                                textureCoord[textureCoordCount + 2] = (textureBack[0] - ((0 - thisBlockBox[3]) / 32f));
                                textureCoord[textureCoordCount + 3] = (textureBack[2] + ((1 - thisBlockBox[4]) / 32f));

                                textureCoord[textureCoordCount + 4] = (textureBack[0] - ((0 - thisBlockBox[3]) / 32f));
                                textureCoord[textureCoordCount + 5] = (textureBack[3] - ((thisBlockBox[1]) / 32f));
                                textureCoord[textureCoordCount + 6] = (textureBack[1] - ((1 - thisBlockBox[0]) / 32f));
                                textureCoord[textureCoordCount + 7] = (textureBack[3] - ((thisBlockBox[1]) / 32f));
                                textureCoordCount += 8;


                                //right
                                positions[positionsCount + 0] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 1] = (thisBlockBox[4] + y);
                                positions[positionsCount + 2] = (thisBlockBox[2] + z + offsetZ);

                                positions[positionsCount + 3] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 4] = (thisBlockBox[4] + y);
                                positions[positionsCount + 5] = (thisBlockBox[5] + z + offsetZ);

                                positions[positionsCount + 6] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 7] = (thisBlockBox[1] + y);
                                positions[positionsCount + 8] = (thisBlockBox[5] + z + offsetZ);

                                positions[positionsCount + 9] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 10] = (thisBlockBox[1] + y);
                                positions[positionsCount + 11] = (thisBlockBox[2] + z + offsetZ);

                                positionsCount += 12;

                                //right
                                float rightLight = getLight(realX + 1, y, realZ) / maxLight;
                                rightLight = convertLight(rightLight);
                                //right
                                for (int i = 0; i < 12; i++) {
                                    light[lightCount + i] = (rightLight);
                                }

                                lightCount += 12;

                                //right
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;


                                // 0, 1, 2, 3, 4, 5
                                //-x,-y,-z, x, y, z
                                // 0, 0, 0, 1, 1, 1

                                // 0, 1,  2, 3
                                //-x,+x, -y,+y


                                float[] textureRight = getRightTexturePoints(thisBlock);
                                //right
                                textureCoord[textureCoordCount + 0] = (textureRight[1] - ((1 - thisBlockBox[2]) / 32f));
                                textureCoord[textureCoordCount + 1] = (textureRight[2] + ((1 - thisBlockBox[4]) / 32f));
                                textureCoord[textureCoordCount + 2] = (textureRight[0] - ((0 - thisBlockBox[5]) / 32f));
                                textureCoord[textureCoordCount + 3] = (textureRight[2] + ((1 - thisBlockBox[4]) / 32f));

                                textureCoord[textureCoordCount + 4] = (textureRight[0] - ((0 - thisBlockBox[5]) / 32f));
                                textureCoord[textureCoordCount + 5] = (textureRight[3] - ((thisBlockBox[1]) / 32f));
                                textureCoord[textureCoordCount + 6] = (textureRight[1] - ((1 - thisBlockBox[2]) / 32f));
                                textureCoord[textureCoordCount + 7] = (textureRight[3] - ((thisBlockBox[1]) / 32f));
                                textureCoordCount += 8;


                                //left
                                positions[positionsCount + 0] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 1] = (thisBlockBox[4] + y);
                                positions[positionsCount + 2] = (thisBlockBox[5] + z + offsetZ);

                                positions[positionsCount + 3] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 4] = (thisBlockBox[4] + y);
                                positions[positionsCount + 5] = (thisBlockBox[2] + z + offsetZ);

                                positions[positionsCount + 6] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 7] = (thisBlockBox[1] + y);
                                positions[positionsCount + 8] = (thisBlockBox[2] + z + offsetZ);

                                positions[positionsCount + 9] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 10] = (thisBlockBox[1] + y);
                                positions[positionsCount + 11] = (thisBlockBox[5] + z + offsetZ);

                                positionsCount += 12;

                                //left
                                float leftLight = getLight(realX - 1, y, realZ) / maxLight;
                                leftLight = convertLight(leftLight);
                                //left
                                for (int i = 0; i < 12; i++) {
                                    light[lightCount + i] = (leftLight);
                                }

                                lightCount += 12;

                                //left
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;

                                float[] textureLeft = getLeftTexturePoints(thisBlock);
                                //left
                                textureCoord[textureCoordCount + 0] = (textureLeft[1] - ((1 - thisBlockBox[5]) / 32f));
                                textureCoord[textureCoordCount + 1] = (textureLeft[2] + ((1 - thisBlockBox[4]) / 32f));
                                textureCoord[textureCoordCount + 2] = (textureLeft[0] - ((0 - thisBlockBox[2]) / 32f));
                                textureCoord[textureCoordCount + 3] = (textureLeft[2] + ((1 - thisBlockBox[4]) / 32f));

                                textureCoord[textureCoordCount + 4] = (textureLeft[0] - ((0 - thisBlockBox[2]) / 32f));
                                textureCoord[textureCoordCount + 5] = (textureLeft[3] - ((thisBlockBox[1]) / 32f));
                                textureCoord[textureCoordCount + 6] = (textureLeft[1] - ((1 - thisBlockBox[5]) / 32f));
                                textureCoord[textureCoordCount + 7] = (textureLeft[3] - ((thisBlockBox[1]) / 32f));
                                textureCoordCount += 8;


                                //top
                                positions[positionsCount + 0] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 1] = (thisBlockBox[4] + y);
                                positions[positionsCount + 2] = (thisBlockBox[2] + z + offsetZ);

                                positions[positionsCount + 3] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 4] = (thisBlockBox[4] + y);
                                positions[positionsCount + 5] = (thisBlockBox[5] + z + offsetZ);

                                positions[positionsCount + 6] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 7] = (thisBlockBox[4] + y);
                                positions[positionsCount + 8] = (thisBlockBox[5] + z + offsetZ);

                                positions[positionsCount + 9] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 10] = (thisBlockBox[4] + y);
                                positions[positionsCount + 11] = (thisBlockBox[2] + z + offsetZ);

                                positionsCount += 12;

                                //top
                                float topLight = getLight(realX, y + 1, realZ) / maxLight;
                                topLight = convertLight(topLight);
                                //top
                                for (int i = 0; i < 12; i++) {
                                    light[lightCount + i] = (topLight);
                                }

                                lightCount += 12;

                                //top
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;

                                // 0, 1, 2, 3, 4, 5
                                //-x,-y,-z, x, y, z
                                // 0, 0, 0, 1, 1, 1

                                // 0, 1,  2, 3
                                //-x,+x, -y,+y

                                float[] textureTop = getTopTexturePoints(thisBlock);
                                //top
                                textureCoord[textureCoordCount + 0] = (textureTop[1] - ((1 - thisBlockBox[5]) / 32f));
                                textureCoord[textureCoordCount + 1] = (textureTop[2] + ((1 - thisBlockBox[0]) / 32f));
                                textureCoord[textureCoordCount + 2] = (textureTop[0] - ((0 - thisBlockBox[2]) / 32f));
                                textureCoord[textureCoordCount + 3] = (textureTop[2] + ((1 - thisBlockBox[0]) / 32f));

                                textureCoord[textureCoordCount + 4] = (textureTop[0] - ((0 - thisBlockBox[2]) / 32f));
                                textureCoord[textureCoordCount + 5] = (textureTop[3] - ((thisBlockBox[3]) / 32f));
                                textureCoord[textureCoordCount + 6] = (textureTop[1] - ((1 - thisBlockBox[5]) / 32f));
                                textureCoord[textureCoordCount + 7] = (textureTop[3] - ((thisBlockBox[3]) / 32f));
                                textureCoordCount += 8;


                                //bottom
                                positions[positionsCount + 0] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 1] = (thisBlockBox[1] + y);
                                positions[positionsCount + 2] = (thisBlockBox[5] + z + offsetZ);

                                positions[positionsCount + 3] = (thisBlockBox[0] + x + offsetX);
                                positions[positionsCount + 4] = (thisBlockBox[1] + y);
                                positions[positionsCount + 5] = (thisBlockBox[2] + z + offsetZ);

                                positions[positionsCount + 6] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 7] = (thisBlockBox[1] + y);
                                positions[positionsCount + 8] = (thisBlockBox[2] + z + offsetZ);

                                positions[positionsCount + 9] = (thisBlockBox[3] + x + offsetX);
                                positions[positionsCount + 10] = (thisBlockBox[1] + y);
                                positions[positionsCount + 11] = (thisBlockBox[5] + z + offsetZ);

                                positionsCount += 12;

                                //bottom
                                float bottomLight = getLight(realX, y - 1, realZ) / maxLight;
                                bottomLight = convertLight(bottomLight);
                                //bottom
                                for (int i = 0; i < 12; i++) {
                                    light[lightCount + i] = (bottomLight);
                                }

                                lightCount += 12;

                                //bottom
                                indices[indicesTableCount + 0] = (0 + indicesCount);
                                indices[indicesTableCount + 1] = (1 + indicesCount);
                                indices[indicesTableCount + 2] = (2 + indicesCount);
                                indices[indicesTableCount + 3] = (0 + indicesCount);
                                indices[indicesTableCount + 4] = (2 + indicesCount);
                                indices[indicesTableCount + 5] = (3 + indicesCount);
                                indicesCount += 4;
                                indicesTableCount += 6;


                                // 0, 1, 2, 3, 4, 5
                                //-x,-y,-z, x, y, z
                                // 0, 0, 0, 1, 1, 1

                                // 0, 1,  2, 3
                                //-x,+x, -y,+y

                                float[] textureBottom = getBottomTexturePoints(thisBlock);
                                //bottom
                                textureCoord[textureCoordCount + 0] = (textureBottom[1] - ((1 - thisBlockBox[5]) / 32f));
                                textureCoord[textureCoordCount + 1] = (textureBottom[2] + ((1 - thisBlockBox[0]) / 32f));
                                textureCoord[textureCoordCount + 2] = (textureBottom[0] - ((0 - thisBlockBox[2]) / 32f));
                                textureCoord[textureCoordCount + 3] = (textureBottom[2] + ((1 - thisBlockBox[0]) / 32f));

                                textureCoord[textureCoordCount + 4] = (textureBottom[0] - ((0 - thisBlockBox[2]) / 32f));
                                textureCoord[textureCoordCount + 5] = (textureBottom[3] - ((thisBlockBox[3]) / 32f));
                                textureCoord[textureCoordCount + 6] = (textureBottom[1] - ((1 - thisBlockBox[5]) / 32f));
                                textureCoord[textureCoordCount + 7] = (textureBottom[3] - ((thisBlockBox[3]) / 32f));
                                textureCoordCount += 8;
                            }
                        }
                    }
                        //todo: ------------------------------------------------------------------------------------------------=-=-=-=
                }
            }
        }
        if (positionsCount > 0) {
//        convert the position objects into usable array
            float[] positionsArray = new float[positionsCount];
            if (positionsCount >= 0) System.arraycopy(positions, 0, positionsArray, 0, positionsCount);

            //convert the light objects into usable array
            float[] lightArray = new float[lightCount];
            if (lightCount >= 0) System.arraycopy(light, 0, lightArray, 0, lightCount);

            //convert the indices objects into usable array
            int[] indicesArray = new int[indicesTableCount];
            if (indicesTableCount >= 0) System.arraycopy(indices, 0, indicesArray, 0, indicesTableCount);

            //convert the textureCoord objects into usable array
            float[] textureCoordArray = new float[textureCoordCount];
            if (textureCoordCount >= 0) System.arraycopy(textureCoord, 0, textureCoordArray, 0, textureCoordCount);

            setChunkMesh(chunkX, chunkZ, yHeight, new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, textureAtlas));
        } else {
            setChunkMesh(chunkX, chunkZ, yHeight, null);
        }

        //do the same thing for liquids

        if (liquidPositionsCount > 0) {
//        convert the position objects into usable array
            float[] liquidPositionsArray = new float[liquidPositionsCount];
            if (liquidPositionsCount >= 0)
                System.arraycopy(liquidPositions, 0, liquidPositionsArray, 0, liquidPositionsCount);

            //convert the light objects into usable array
            float[] liquidLightArray = new float[liquidLightCount];
            if (liquidLightCount >= 0) System.arraycopy(liquidLight, 0, liquidLightArray, 0, liquidLightCount);

            //convert the indices objects into usable array
            int[] liquidIndicesArray = new int[liquidIndicesTableCount];
            if (liquidIndicesTableCount >= 0)
                System.arraycopy(liquidIndices, 0, liquidIndicesArray, 0, liquidIndicesTableCount);

            //convert the textureCoord objects into usable array
            float[] liquidTextureCoordArray = new float[liquidTextureCoordCount];
            if (liquidTextureCoordCount >= 0)
                System.arraycopy(liquidTextureCoord, 0, liquidTextureCoordArray, 0, liquidTextureCoordCount);

            setChunkLiquidMesh(chunkX, chunkZ, yHeight, new Mesh(liquidPositionsArray, liquidLightArray, liquidIndicesArray, liquidTextureCoordArray, textureAtlas));
        }
        else {
            setChunkLiquidMesh(chunkX, chunkZ, yHeight, null);
        }
    }

    private static float convertLight(float lightByte){
        return (float) Math.pow(Math.pow(lightByte, 1.5), 1.5);
    }
}
