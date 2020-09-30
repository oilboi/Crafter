package game.player;

import org.joml.Vector3f;

import static game.chunk.Chunk.getBlock;
import static game.chunk.Chunk.setBlock;
import static engine.FancyMath.getDistance;
import static game.item.ItemEntity.createItem;
import static engine.TNTEntity.createTNT;
//import static game.light.Light.floodFill;

public class TNT {
    private static int[][] chunkBuffer;
    private static int chunkBufferIndex;
    private static int currentChunkX;
    private static int currentChunkZ;
    private static int currentPosX;
    private static int currentPosZ;
    private static int currentBlock;
    private static boolean found = false;
    private static int x,y,z;

    public static void boom(int posX, int posY, int posZ, int boomDistance) throws Exception {
        chunkBufferIndex = 0;
        chunkBuffer = new int[boomDistance][2];

        for (x = posX - boomDistance; x < posX + boomDistance; x++) {
            for (y = posY - boomDistance; y < posY + boomDistance; y++) {
                for (z = posZ - boomDistance; z < posZ + boomDistance; z++) {

                    currentChunkX = (int)(Math.floor((float) x / 16f));
                    currentChunkZ = (int)(Math.floor((float) z / 16f));

                    currentPosX = x - (16 * currentChunkX);
                    currentPosZ = z - (16 * currentChunkZ);

                    if (getDistance(posX, posY, posZ, x, y, z) <= boomDistance) {

                        currentBlock = getBlock(currentPosX, y, currentPosZ, currentChunkX, currentChunkZ);

                        //don't destroy bedrock
                        if(currentBlock != 5) {
                            setBlock(currentPosX, y, currentPosZ, currentChunkX, currentChunkZ, (short) 0);
                            //todo: make this an API callback!!
                            if (currentBlock != 0 && currentBlock != 6 && Math.random() > 0.98) {
                                createItem(currentBlock, new Vector3f(currentPosX+(currentChunkX*16), y, currentPosZ+(currentChunkZ*16)));
                            } else if (currentBlock == 6){
                                createTNT(new Vector3f(x, y, z), (float)(1f+Math.random()), false);
                            }

                        }

                        //add chunks to chunk generation buffer ID: 555
                        found = false;
                        for (int[] index : chunkBuffer) {
                            if (index[0] == currentChunkX && index[1] == currentChunkZ) {
                                found = true;
                            }
                        }
                        if (!found) {
                            chunkBuffer[chunkBufferIndex] = new int[]{currentChunkX, currentChunkZ};
                            chunkBufferIndex++;
                        }
                        //end: ID: 555
                    }
                }
            }
        }

        for (int[] index : chunkBuffer) {
//            floodFill(index[0], index[1]);
//            generateChunkMesh(index[0], index[1], false);
        }
    }
}
