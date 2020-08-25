package game.ChunkHandling;


import java.util.ArrayList;

import static game.ChunkHandling.ChunkMath.genHash;

import static game.Crafter.chunkRenderDistance;
import static game.light.Light.floodFill;

public class ChunkData {

    private static Chunk[] map = new Chunk[(chunkRenderDistance*(4*chunkRenderDistance)+(chunkRenderDistance*4)+2)];

    public static void storeChunk(int x, int z, Chunk chunk){
//        for(int i = 0; i < map.size(); i++){
//            if (map.get(i).getName().equals(x + " " + z)){
//                map.set(i, chunk);
//                return;
//            }
//        }
        //fails to find it
//        map.add(chunk);
    }

    public static Chunk getChunk(int x, int z){
//        for(int i = 0; i < map.size(); i++) {
//            if (map.get(i).getName().equals(x + " " + z)) {
//                System.out.println("oh no");
//                return map.get(i);
//
//            }
//        }
        return null;
    }

    public static boolean chunkExists(int x, int z){
//        for(int i = 0; i < map.size(); i++) {
//            if (map.get(i).getName().equals(x + " " + z)) {
//                return true;
//            }
//        }
        //failed to find it
        return false;
    }

    public static void setBlock(int x, int y, int z, int chunkX, int chunkZ, short newBlock){
        Chunk thisChunk = getChunk(chunkX,chunkZ);
        if (thisChunk == null) {
            return;
        }
        thisChunk.setBlock(genHash(x, y, z), newBlock);
//        floodFill(chunkX,chunkZ);
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
