package engine;

import engine.graph.Mesh;
import game.ChunkHandling.ChunkMath;

import static game.ChunkHandling.ChunkMath.genChunkHash;
import static game.ChunkHandling.ChunkMath.genHash;
import static game.Crafter.chunkRenderDistance;

public class Chunk {

    private static int [][] block;
    private static byte[][] light;
    private static Mesh[]   mesh;

    private static int limit = 0;

    public static void initializeChunkHandler(int chunkRenderDistance){

        block = new int[((chunkRenderDistance * 2) + 1) * ((chunkRenderDistance * 2) + 1)][16*128*16];
        light = new byte[((chunkRenderDistance * 2) + 1) * ((chunkRenderDistance * 2) + 1)][16*128*16];
        mesh  = new Mesh[((chunkRenderDistance * 2) + 1) * ((chunkRenderDistance * 2) + 1)];
        limit = ((chunkRenderDistance * 2) + 1) * ((chunkRenderDistance * 2) + 1);

    }

    public static void setChunkMesh(int chunkX, int chunkZ, Mesh newMesh){
        if (mesh[genChunkHash(chunkX,chunkZ)] != null){
            mesh[genChunkHash(chunkX,chunkZ)].cleanUp();
        }
        mesh[genChunkHash(chunkX,chunkZ)] = newMesh;
    }

    public static int getLimit(){
        return limit;
    }

    public static Mesh getChunkMesh(int i){
        return mesh[i];
    }

    public static int getBlock(int x,int y,int z, int chunkX, int chunkZ){

        if(chunkX < -chunkRenderDistance || chunkZ < -chunkRenderDistance || chunkX > chunkRenderDistance || chunkZ > chunkRenderDistance || genChunkHash(chunkX,chunkZ) < 0 || genChunkHash(chunkX,chunkZ) >= limit){
            return 0;
        }

        //neighbor checking
        if (y < 0 || y >= 128) { //Y is caught regardless in the else clause if in bounds
            return 0;
        }
        if(x < 0) {
            return getBlock(x+16,y,z,chunkX-1,chunkZ);
        }
        if (x >= 16) {
            return getBlock(x-16,y,z,chunkX+1,chunkZ);
        }
        if (z < 0) {
            return getBlock(x,y,z+16,chunkX,chunkZ-1);
        }
        if (z >= 16) {
            return getBlock(x,y,z-16,chunkX,chunkZ+1);
        }

        //self chunk checking
        return block[genChunkHash(chunkX,chunkZ)][genHash(x,y,z)];
    }

    public static void setBlock(int x,int y,int z, int chunkX, int chunkZ, int newBlock){
        block[genChunkHash(chunkX,chunkZ)][genHash(x,y,z)] = newBlock;
    }


    public static byte getLight(int x,int y,int z, int chunkX, int chunkZ){

        if(chunkX < -chunkRenderDistance || chunkZ < -chunkRenderDistance || chunkX > chunkRenderDistance || chunkZ > chunkRenderDistance || genChunkHash(chunkX,chunkZ) < 0 || genChunkHash(chunkX,chunkZ) >= limit){
            return 0;
        }

        //neighbor checking
        if (y < 0 || y >= 128) { //Y is caught regardless in the else clause if in bounds
            return 0;
        }
        if(x < 0) {
            return getLight(x+16,y,z,chunkX-1,chunkZ);
        }
        if (x >= 16) {
            return getLight(x-16,y,z,chunkX+1,chunkZ);
        }
        if (z < 0) {
            return getLight(x,y,z+16,chunkX,chunkZ-1);
        }
        if (z >= 16) {
            return getLight(x,y,z-16,chunkX,chunkZ+1);
        }

        //self chunk checking
        return light[genChunkHash(chunkX,chunkZ)][genHash(x,y,z)];
    }

    public static void setLight(int x,int y,int z, int chunkX, int chunkZ, byte newLight){
        light[genChunkHash(chunkX,chunkZ)][genHash(x,y,z)] = newLight;
    }

    private static FastNoise noise = new FastNoise();
    private static int heightAdder = 40;
    private static byte dirtHeight = 4;
    //a basic biome test for terrain generation
    public static void genBiome(int chunkX, int chunkZ){
        int x = 0;
        int y = 127;
        int z = 0;

        byte height = (byte)(Math.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);

        for ( int i = 0; i < (16 * 128 * 16); i++){

            short currBlock;

            if (y == 0 ){
                currBlock = 5;
            } else if (y == height) {
                currBlock = 2;
            } else if (y < height && y >= height - dirtHeight) {
                currBlock = 1;
            } else if (y < height - dirtHeight) {
                currBlock = 3;
            } else {
                currBlock = 0;
            }

            block[genChunkHash(chunkX,chunkZ)][ChunkMath.genHash(x, y, z)] = currBlock;

            if (currBlock == 0) {
                light[genChunkHash(chunkX,chunkZ)][ChunkMath.genHash(x, y, z)] = 15;//0;
            }else{
                light[genChunkHash(chunkX,chunkZ)][ChunkMath.genHash(x, y, z)] = 0;
            }

            y--;
            if( y < 0){
                y = 127;
                x++;
                height = (byte)(Math.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);
                if( x > 15 ){
                    x = 0;
                    height = (byte)(Math.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);
                    z++;
                }
            }
        }
    }

    public static boolean underSunlight(int x, int y, int z, int chunkX, int chunkZ){
        if(chunkX < -chunkRenderDistance || chunkZ < -chunkRenderDistance || chunkX > chunkRenderDistance || chunkZ > chunkRenderDistance || genChunkHash(chunkX,chunkZ) < 0 || genChunkHash(chunkX,chunkZ) >= limit){
            return false;
        }
        for (int indexY = 127; indexY > y; indexY--) {
            if (getBlock(x, indexY, z, chunkX, chunkZ) != 0){
                return false;
            }
        }
        return true;
    }

    public static void cleanUp(){
        for (Mesh thisMesh : mesh){
            if (thisMesh != null){
                thisMesh.cleanUp();
            }
        }
    }
}
