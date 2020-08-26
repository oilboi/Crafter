package game.ChunkHandling;


import java.util.ArrayList;

import static game.ChunkHandling.ChunkMath.*;
import static game.Crafter.chunkRenderDistance;
import static game.light.Light.floodFill;

public class ChunkData {

    private static int arrayLimit = (chunkRenderDistance*2)+1;

    private static Chunk[][] map = new Chunk[(chunkRenderDistance*2)+1][(chunkRenderDistance*2)+1];

    public static void storeChunk(int chunkX, int chunkZ, Chunk chunk){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;
        if (chunkX < 0 || chunkX >= arrayLimit || chunkZ < 0 || chunkZ >= arrayLimit){
            return;
        }
        map[chunkX][chunkZ] = chunk;
    }

    public static Chunk getChunk(int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;
        if (chunkX < 0 || chunkX >= arrayLimit || chunkZ < 0 || chunkZ >= arrayLimit){
            return null;
        }
        return map[chunkX][chunkZ];
    }

    public static boolean chunkExists(int chunkX, int chunkZ){
        return getChunk(chunkX,chunkZ) != null;
    }

    public static void setBlock(int x, int y, int z, int chunkX, int chunkZ, short newBlock){
        Chunk thisChunk = getChunk(chunkX,chunkZ);
        if (thisChunk == null) {
            return;
        }
        thisChunk.setBlock(genHash(x, y, z), newBlock);

    }

    //todo COMBINE THESE!
    public static short getBlock(int x, int y, int z, int chunkX, int chunkZ){
        Chunk thisChunk = getChunk(chunkX,chunkZ);
        if (thisChunk == null){
            return 0;
        }
        //todo throw exception here if out of bounds
        return thisChunk.getBlocks()[genHash(x, y, z)];
    }
    public static short getBlockInChunk(int x,int y,int z, int chunkX, int chunkZ){
        //neighbor checking
        if(x < 0) {
            return getBlock(x+16,y,z,chunkX-1,chunkZ);
        } else if (x >= 16) {
            return getBlock(x-16,y,z,chunkX+1,chunkZ);
        } else if (y < 0 || y >= 128) { //Y is caught regardless in the else clause if in bounds
            return 0;
        } else if (z < 0) {
            return getBlock(x,y,z+16,chunkX,chunkZ-1);
        } else if (z >= 16) {
            return getBlock(x,y,z-16,chunkX,chunkZ+1);
        }
        //self chunk checking
        else {
            return getBlock(x,y,z,chunkX,chunkZ);
        }
    }

    //TODO COMBINE THESE!!!
    public static byte getLight(int x, int y, int z, int chunkX, int chunkZ){
        Chunk thisChunk = getChunk(chunkX,chunkZ);
        if (thisChunk == null){
            return 0;
        }
        return thisChunk.getLights()[genHash(x, y, z)];
    }
    public static byte getLightInChunk(int x,int y,int z, int chunkX, int chunkZ){
        //neighbor checking
        if(x < 0) {
            return getLight(x+16,y,z,chunkX-1,chunkZ);
        } else if (x >= 16) {
            return getLight(x-16,y,z,chunkX+1,chunkZ);
        } else if (y < 0 || y >= 128) { //Y is caught regardless in the else clause if in bounds
            return 0;
        } else if (z < 0) {
            return getLight(x,y,z+16,chunkX,chunkZ-1);
        } else if (z >= 16) {
            return getLight(x,y,z-16,chunkX,chunkZ+1);
        }
        //self chunk checking
        else {
            return getLight(x,y,z,chunkX,chunkZ);
        }
    }

    public static boolean underSunlight(int x, int y, int z, int chunkX, int chunkZ){
        Chunk thisChunk = getChunk(chunkX,chunkZ);
        if (thisChunk == null){
            return false;
        }
        for (int indexY = 127; indexY > y; indexY--) {
            if (getBlock(x,indexY,z,chunkX,chunkZ) != 0){
                return false;
            }
        }
        return true;
    }
}
