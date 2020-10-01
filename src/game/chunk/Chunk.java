package game.chunk;

import engine.FastNoise;
import engine.graph.Mesh;

import java.util.HashMap;
import java.util.Map;

import static game.chunk.ChunkUpdateHandler.chunkUpdate;
import static game.Crafter.getChunkRenderDistance;

public class Chunk {

    private static final Map<String, ChunkObject> map = new HashMap<>();

    public static void setChunkMesh(int chunkX, int chunkZ, int yHeight, Mesh newMesh){
        ChunkObject thisChunk = map.get(chunkX + " " + chunkZ);
        if (thisChunk == null){
            newMesh.cleanUp(false);
            return;
        }
        if (thisChunk.mesh == null){
            newMesh.cleanUp(false);
            return;
        }
        if (thisChunk.mesh[yHeight] != null){
            thisChunk.mesh[yHeight].cleanUp(false);
        }
        thisChunk.mesh[yHeight] = newMesh;
    }

    public static void setChunkLiquidMesh(int chunkX, int chunkZ, int yHeight, Mesh newMesh){
        ChunkObject thisChunk = map.get(chunkX + " " + chunkZ);
        if (thisChunk == null){
            newMesh.cleanUp(false);
            return;
        }
        if (thisChunk.liquidMesh == null){
            newMesh.cleanUp(false);
            return;
        }
        if (thisChunk.liquidMesh[yHeight] != null){
            thisChunk.liquidMesh[yHeight].cleanUp(false);
        }
        thisChunk.liquidMesh[yHeight] = newMesh;
    }

    public static Mesh getChunkMesh(int chunkX, int chunkZ, int yHeight){
        ChunkObject thisChunk = map.get(chunkX + " " + chunkZ);
        if (thisChunk == null){
            return null;
        }
        if (thisChunk.mesh == null){
            return null;
        }
        if (thisChunk.mesh[yHeight] != null){
            return thisChunk.mesh[yHeight];
        }
        return null;
    }

    public static Mesh getChunkLiquidMesh(int chunkX, int chunkZ, int yHeight){
        ChunkObject thisChunk = map.get(chunkX + " " + chunkZ);
        if (thisChunk == null){
            return null;
        }
        if (thisChunk.liquidMesh == null){
            return null;
        }
        if (thisChunk.liquidMesh[yHeight] != null){
            return thisChunk.liquidMesh[yHeight];
        }
        return null;
    }

    public static int getBlock(int x,int y,int z){
        if (y > 127 || y < 0){
            return -1;
        }
        int chunkX = (int)Math.floor(x/16f);
        int chunkZ = (int)Math.floor(z/16f);
        int blockX = (int)(x - (16f*chunkX));
        int blockZ = (int)(z - (16f*chunkZ));
        String key = chunkX + " " + chunkZ;
        ChunkObject thisChunk = map.get(key);
        if (thisChunk == null){
            return -1;
        }
        if (thisChunk.block == null){
            return -1;
        }
        return thisChunk.block[y][blockX][blockZ];
    }

    public static void setBlock(int x,int y,int z, int newBlock){
        if (y > 127 || y < 0){
            return;
        }
        int yPillar = (int)Math.floor(y/16f);
        int chunkX = (int)Math.floor(x/16f);
        int chunkZ = (int)Math.floor(z/16f);
        int blockX = (int)(x - (16f*chunkX));
        int blockZ = (int)(z - (16f*chunkZ));
        String key = chunkX + " " + chunkZ;
        ChunkObject thisChunk = map.get(key);

        if (thisChunk == null){
            return;
        }
        if (thisChunk.block == null){
            return;
        }
        thisChunk.block[y][blockX][blockZ] = newBlock;
        chunkUpdate(chunkX,chunkZ,yPillar);
        updateNeighbor(chunkX, chunkZ,blockX,y,blockZ);
    }


    private static void updateNeighbor(int chunkX, int chunkZ, int x, int y, int z){
        if (y > 127 || y < 0){
            return;
        }
        int yPillar = (int)Math.floor(y/16f);
        switch (y){
            case 112:
            case 96:
            case 80:
            case 64:
            case 48:
            case 32:
            case 16:
                chunkUpdate(chunkX, chunkZ, yPillar-1);
                break;
            case 111:
            case 95:
            case 79:
            case 63:
            case 47:
            case 31:
            case 15:
                chunkUpdate(chunkX, chunkZ, yPillar+1);
                break;
        }
        if (x == 15){ //update neighbor
            chunkUpdate(chunkX+1, chunkZ, yPillar);
        }
        if (x == 0){
            chunkUpdate(chunkX-1, chunkZ, yPillar);
        }
        if (z == 15){
            chunkUpdate(chunkX, chunkZ+1, yPillar);
        }
        if (z == 0){
            chunkUpdate(chunkX, chunkZ-1, yPillar);
        }
    }

    private static final FastNoise noise = new FastNoise();
    private static final int heightAdder = 40;
    private static final byte dirtHeight = 4;
    private static final byte waterHeight = 50;

    public static void genBiome(int chunkX, int chunkZ){
            short currBlock;
            byte height;
            ChunkObject thisChunk = map.get(chunkX + " " + chunkZ);
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    float dirtHeightRandom = (float)Math.floor(Math.random() * 2f);
                    height = (byte)(Math.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);
                    for (int y = 127; y >= 0; y--) {
                    if (y <= 0 + dirtHeightRandom) {
                        currBlock = 5;
                    } else if (y == height) {
                        currBlock = 2;
                    } else if (y < height && y >= height - dirtHeight - dirtHeightRandom) {
                        currBlock = 1;
                    } else if (y < height - dirtHeight) { //TODO: stone level
                        if (y <= 30 && y > 0) {
                            if (Math.random() > 0.95) {
                                currBlock = (short) Math.floor(8 + (Math.random() * 8));
                            } else {
                                currBlock = 3;
                            }
                        } else {
                            currBlock = 3;
                        }
                    } else {
                        if (y <= waterHeight) {
                            currBlock = 7;
                        } else {
                            currBlock = 0;
                        }
                    }

                    thisChunk.block[y][x][z] = currBlock;

    //            if (currBlock == 0) {
                    thisChunk.light[y][x][z] = 15;//0;
    //            }else{
    //                light[chunkX][chunkZ][y][x][z] = 0;
                }
            }
        }
    }
    public static void cleanUp(){
        for (ChunkObject thisChunk : map.values()){
            if (thisChunk == null){
                continue;
            }
            if (thisChunk.mesh != null){
                for (Mesh thisMesh : thisChunk.mesh){
                    if (thisMesh != null){
                        thisMesh.cleanUp(true);
                    }
                }
            }
        }
    }
}
