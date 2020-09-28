package game.chunk;

import engine.graph.Mesh;
import engine.graph.Texture;

import java.util.ArrayList;

import static game.chunk.Chunk.*;
import static game.blocks.BlockDefinition.*;

public class ChunkMesh {

    private static Texture textureAtlas;
    private final static float maxLight = 15;
    public static void initializeChunkTextureAtlas() throws Exception {
         textureAtlas = new Texture("textures/textureAtlas.png");
    }

    //caches
    private static int indicesCount;
    private static int liquidIndicesCount;
    private static int offsetX;
    private static int offsetZ;

    public static void generateChunkMesh(int chunkX, int chunkZ, boolean updating) throws Exception {
        indicesCount = 0;
        liquidIndicesCount = 0;

        ArrayList positions = new ArrayList();
        ArrayList textureCoord = new ArrayList();
        ArrayList indices = new ArrayList();
        ArrayList light = new ArrayList();

        ArrayList liquidPositions = new ArrayList();
        ArrayList liquidTextureCoord = new ArrayList();
        ArrayList liquidIndices = new ArrayList();
        ArrayList liquidLight = new ArrayList();

        offsetX = chunkX * 16;
        offsetZ = chunkZ * 16;

        for (int y = 0; y < 128; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {

                    //todo --------------------------------------- THE LIQUID

                    int thisBlock = getBlock(x, y, z, chunkX, chunkZ);
                    //liquid storage
                    if (thisBlock != 0 && getIfLiquid(thisBlock)) {
                        int neighborBlock = getBlock(x, y, z + 1, chunkX, chunkZ);

                        if (!getBlockDrawType(neighborBlock).equals("normal")) {
                            //front
                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(1f + z + offsetZ);

                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(1f + z + offsetZ);

                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(1f + z + offsetZ);

                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(1f + z + offsetZ);
                            //front
                            float frontLight = getLight(x, y, z + 1, chunkX, chunkZ) / maxLight;

                            frontLight = convertLight(frontLight);

                            //front
                            for (int i = 0; i < 12; i++) {
                                liquidLight.add(frontLight);
                            }
                            //front
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(1 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(3 + liquidIndicesCount);
                            liquidIndicesCount += 4;

                            float[] textureFront = getFrontTexturePoints(thisBlock);
                            //front
                            liquidTextureCoord.add(textureFront[1]);
                            liquidTextureCoord.add(textureFront[2]);
                            liquidTextureCoord.add(textureFront[0]);
                            liquidTextureCoord.add(textureFront[2]);
                            liquidTextureCoord.add(textureFront[0]);
                            liquidTextureCoord.add(textureFront[3]);
                            liquidTextureCoord.add(textureFront[1]);
                            liquidTextureCoord.add(textureFront[3]);
                        }



                        neighborBlock = getBlock(x, y, z - 1, chunkX, chunkZ);
                        if (!getBlockDrawType(neighborBlock).equals("normal")) {
                            //back
                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(0f + z + offsetZ);

                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(0f + z + offsetZ);

                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(0f + z + offsetZ);

                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(0f + z + offsetZ);
                            //back
                            float backLight = getLight(x, y, z - 1, chunkX, chunkZ) / maxLight;
                            backLight = convertLight(backLight);
                            //back
                            for (int i = 0; i < 12; i++) {
                                liquidLight.add(backLight);
                            }
                            //back
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(1 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(3 + liquidIndicesCount);
                            liquidIndicesCount += 4;

                            float[] textureBack = getBackTexturePoints(thisBlock);
                            //back
                            liquidTextureCoord.add(textureBack[1]);
                            liquidTextureCoord.add(textureBack[2]);
                            liquidTextureCoord.add(textureBack[0]);
                            liquidTextureCoord.add(textureBack[2]);
                            liquidTextureCoord.add(textureBack[0]);
                            liquidTextureCoord.add(textureBack[3]);
                            liquidTextureCoord.add(textureBack[1]);
                            liquidTextureCoord.add(textureBack[3]);
                        }

                        neighborBlock = getBlock(x + 1, y, z, chunkX, chunkZ);
                        if (!getBlockDrawType(neighborBlock).equals("normal")) {
                            //right
                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(0f + z + offsetZ);

                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(1f + z + offsetZ);

                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(1f + z + offsetZ);

                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(0f + z + offsetZ);
                            //right
                            float rightLight = getLight(x + 1, y, z, chunkX, chunkZ) / maxLight;
                            rightLight = convertLight(rightLight);
                            //right
                            for (int i = 0; i < 12; i++) {
                                liquidLight.add(rightLight);
                            }
                            //right
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(1 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(3 + liquidIndicesCount);
                            liquidIndicesCount += 4;

                            float[] textureRight = getRightTexturePoints(thisBlock);
                            //right
                            liquidTextureCoord.add(textureRight[1]);
                            liquidTextureCoord.add(textureRight[2]);
                            liquidTextureCoord.add(textureRight[0]);
                            liquidTextureCoord.add(textureRight[2]);
                            liquidTextureCoord.add(textureRight[0]);
                            liquidTextureCoord.add(textureRight[3]);
                            liquidTextureCoord.add(textureRight[1]);
                            liquidTextureCoord.add(textureRight[3]);
                        }

                        neighborBlock = getBlock(x - 1, y, z, chunkX, chunkZ);
                        if (!getBlockDrawType(neighborBlock).equals("normal")) {
                            //left
                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(1f + z + offsetZ);

                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(0f + z + offsetZ);

                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(0f + z + offsetZ);

                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(1f + z + offsetZ);
                            //left
                            float leftLight = getLight(x - 1, y, z, chunkX, chunkZ) / maxLight;
                            leftLight = convertLight(leftLight);
                            //left
                            for (int i = 0; i < 12; i++) {
                                liquidLight.add(leftLight);
                            }
                            //left
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(1 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(3 + liquidIndicesCount);
                            liquidIndicesCount += 4;

                            float[] textureLeft = getLeftTexturePoints(thisBlock);
                            //left
                            liquidTextureCoord.add(textureLeft[1]);
                            liquidTextureCoord.add(textureLeft[2]);
                            liquidTextureCoord.add(textureLeft[0]);
                            liquidTextureCoord.add(textureLeft[2]);
                            liquidTextureCoord.add(textureLeft[0]);
                            liquidTextureCoord.add(textureLeft[3]);
                            liquidTextureCoord.add(textureLeft[1]);
                            liquidTextureCoord.add(textureLeft[3]);
                        }

                        neighborBlock = getBlock(x, y + 1, z, chunkX, chunkZ);
                        if (!getBlockDrawType(neighborBlock).equals("normal") || y == 127 && getIfLiquid(neighborBlock)) {
                            //top
                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(0f + z + offsetZ);

                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(1f + z + offsetZ);

                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(1f + z + offsetZ);

                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(1f + y);
                            liquidPositions.add(0f + z + offsetZ);
                            //top
                            float topLight = getLight(x, y + 1, z, chunkX, chunkZ) / maxLight;
                            topLight = convertLight(topLight);
                            //top
                            for (int i = 0; i < 12; i++) {
                                liquidLight.add(topLight);
                            }
                            //top
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(1 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(3 + liquidIndicesCount);
                            liquidIndicesCount += 4;

                            float[] textureTop = getTopTexturePoints(thisBlock);
                            //top
                            liquidTextureCoord.add(textureTop[1]);
                            liquidTextureCoord.add(textureTop[2]);
                            liquidTextureCoord.add(textureTop[0]);
                            liquidTextureCoord.add(textureTop[2]);
                            liquidTextureCoord.add(textureTop[0]);
                            liquidTextureCoord.add(textureTop[3]);
                            liquidTextureCoord.add(textureTop[1]);
                            liquidTextureCoord.add(textureTop[3]);
                        }

                        neighborBlock = getBlock(x, y - 1, z, chunkX, chunkZ);
                        if (!getBlockDrawType(neighborBlock).equals("normal") && y != 0) {
                            //bottom
                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(1f + z + offsetZ);

                            liquidPositions.add(0f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(0f + z + offsetZ);

                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(0f + z + offsetZ);

                            liquidPositions.add(1f + x + offsetX);
                            liquidPositions.add(0f + y);
                            liquidPositions.add(1f + z + offsetZ);
                            //bottom
                            float bottomLight = getLight(x, y - 1, z, chunkX, chunkZ) / maxLight;
                            bottomLight = convertLight(bottomLight);
                            //bottom
                            for (int i = 0; i < 12; i++) {
                                liquidLight.add(bottomLight);
                            }
                            //bottom
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(1 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(0 + liquidIndicesCount);
                            liquidIndices.add(2 + liquidIndicesCount);
                            liquidIndices.add(3 + liquidIndicesCount);
                            liquidIndicesCount += 4;

                            float[] textureBottom = getBottomTexturePoints(thisBlock);
                            //bottom
                            liquidTextureCoord.add(textureBottom[1]);
                            liquidTextureCoord.add(textureBottom[2]);
                            liquidTextureCoord.add(textureBottom[0]);
                            liquidTextureCoord.add(textureBottom[2]);
                            liquidTextureCoord.add(textureBottom[0]);
                            liquidTextureCoord.add(textureBottom[3]);
                            liquidTextureCoord.add(textureBottom[1]);
                            liquidTextureCoord.add(textureBottom[3]);
                        }
                    }

                    //todo --------------------------------------- THE NORMAL DRAWTYPE
                    else if (thisBlock != 0 && getBlockDrawType(thisBlock).equals("normal")) {
                        int neighborBlock = getBlock(x, y, z + 1, chunkX, chunkZ);

                        if (!getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock)) {
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
                        if (!getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock)) {
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
                        if (!getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock)) {
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
                        if (!getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock)) {
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
                        if (!getBlockDrawType(neighborBlock).equals("normal") || y == 127 && getIfLiquid(neighborBlock) || getIfLiquid(neighborBlock)) {
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
                        if ((!getBlockDrawType(neighborBlock).equals("normal") || getIfLiquid(neighborBlock)) && y != 0) {
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

                    //todo: ---------------------------------------------------------- the block box draw type


                    else if (thisBlock != 0) {
                        for (float[] thisBlockBox : getBlockShape(thisBlock)) {
                            // 0, 1, 2, 3, 4, 5
                            //-x,-y,-z, x, y, z
                            // 0, 0, 0, 1, 1, 1

                            //front
                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);
                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);
                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);
                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);
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
                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);

                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);

                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);

                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);

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
                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);

                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);

                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);

                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);
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
                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);

                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);

                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);

                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);

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
                            textureCoord.add(textureLeft[1] - ((1-thisBlockBox[5])/32f));
                            textureCoord.add(textureLeft[2] + ((1-thisBlockBox[4])/32f));
                            textureCoord.add(textureLeft[0] - ((0-thisBlockBox[2])/32f));
                            textureCoord.add(textureLeft[2] + ((1-thisBlockBox[4])/32f));

                            textureCoord.add(textureLeft[0] - ((0-thisBlockBox[2])/32f));
                            textureCoord.add(textureLeft[3] - ((thisBlockBox[1])/32f));
                            textureCoord.add(textureLeft[1] - ((1-thisBlockBox[5])/32f));
                            textureCoord.add(textureLeft[3] - ((thisBlockBox[1])/32f));




                            //top
                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);

                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);

                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);

                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[4] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);
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
                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);

                            positions.add(thisBlockBox[0] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);

                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[2] + z + offsetZ);

                            positions.add(thisBlockBox[3] + x + offsetX);
                            positions.add(thisBlockBox[1] + y);
                            positions.add(thisBlockBox[5] + z + offsetZ);
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


        //do the same thing for liquids

        //convert the position objects into usable array
        float[] liquidPositionsArray = new float[liquidPositions.size()];
        for (int i = 0; i < liquidPositions.size(); i++) {
            liquidPositionsArray[i] = (float) liquidPositions.get(i);
        }

        //convert the light objects into usable array
        float[] liquidLightArray = new float[liquidLight.size()];
        for (int i = 0; i < liquidLight.size(); i++) {
            liquidLightArray[i] = (float) liquidLight.get(i);
        }

        //convert the indices objects into usable array
        int[] liquidIndicesArray = new int[liquidIndices.size()];
        for (int i = 0; i < liquidIndices.size(); i++) {
            liquidIndicesArray[i] = (int) liquidIndices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] liquidTextureCoordArray = new float[liquidTextureCoord.size()];
        for (int i = 0; i < liquidTextureCoord.size(); i++) {
            liquidTextureCoordArray[i] = (float) liquidTextureCoord.get(i);
        }

        Mesh liquidMesh = new Mesh(liquidPositionsArray, liquidLightArray, liquidIndicesArray, liquidTextureCoordArray, textureAtlas);

        setChunkLiquidMesh(chunkX, chunkZ, liquidMesh);

    }

    private static float convertLight(float lightByte){
        return (float) Math.pow(Math.pow(lightByte, 1.5), 1.5);
    }
}
