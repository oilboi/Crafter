package game.player;

//import static game.ChunkHandling.ChunkData.getBlock;
//import static game.ChunkHandling.ChunkData.setBlock;

//import static game.light.Light.floodFill;

import static engine.Chunk.getBlock;
import static engine.Chunk.setBlock;
import static game.ChunkHandling.ChunkMath.getDistance;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.Crafter.chunkRenderDistance;
import static game.light.Light.floodFill;

public class TNT {
    public static final int boomDistance = 128;
    public static void boom(int posX, int posY, int posZ) throws Exception {
        int[][] chunkBuffer = new int[256][2];
        int chunkBufferIndex = 0;
        for (int x = posX - boomDistance; x < posX + boomDistance; x++) {
            for (int y = posY - boomDistance; y < posY + boomDistance; y++) {
                for (int z = posZ - boomDistance; z < posZ + boomDistance; z++) {

                    int currentChunkX = (int) (Math.floor((float) x / 16f));
                    int currentChunkZ = (int) (Math.floor((float) z / 16f));


                    int currentPosX = x - (16 * currentChunkX);
                    int currentPosZ = z - (16 * currentChunkZ);

                    if (getDistance(posX, posY, posZ, x, y, z) <= boomDistance) {
                        if (currentChunkX >= -chunkRenderDistance && currentChunkX <= chunkRenderDistance && currentChunkZ >= -chunkRenderDistance && currentChunkZ <= chunkRenderDistance) {
                            if (y >= 0 && y <= 127) {

                                //don't destroy bedrock
                                if(getBlock(currentPosX, y, currentPosZ, currentChunkX, currentChunkZ) != 5) {
                                    setBlock(currentPosX, y, currentPosZ, currentChunkX, currentChunkZ, (short) 0);
                                }

                                //add chunks to chunk generation buffer ID: 555
                                boolean found = false;
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
            }
        }

        for (int[] index : chunkBuffer) {
            floodFill(index[0], index[1]);
            generateChunkMesh(index[0], index[1], true);
        }
    }
}
