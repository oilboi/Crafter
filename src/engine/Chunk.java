package engine;

import engine.graph.Mesh;
import static game.Crafter.chunkRenderDistance;

public class Chunk {

    private static int [][][][][] block;
    private static byte[][][][][] light;
    private static Mesh[][]   mesh;

    private static int limit = 0;

    public static void initializeChunkHandler(int chunkRenderDistance){

        block = new  int[((chunkRenderDistance * 2) + 1)][((chunkRenderDistance * 2) + 1)][128][16][16];
        light = new byte[((chunkRenderDistance * 2) + 1)][((chunkRenderDistance * 2) + 1)][128][16][16];
        mesh  = new Mesh[((chunkRenderDistance * 2) + 1)][((chunkRenderDistance * 2) + 1)];
        limit = ((chunkRenderDistance * 2) + 1);

    }

    public static void setChunkMesh(int chunkX, int chunkZ, Mesh newMesh){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        if(chunkX < 0 || chunkZ < 0 || chunkX >= limit || chunkZ >= limit){
            return;
        }

        if (mesh[chunkX][chunkZ] != null){
            mesh[chunkX][chunkZ].cleanUp();
        }
        mesh[chunkX][chunkZ] = newMesh;
    }

    public static int getLimit(){
        return limit;
    }

    public static Mesh getChunkMesh(int chunkX, int chunkZ){
        return mesh[chunkX][chunkZ];
    }

    public static int getBlock(int x,int y,int z, int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        return getInternalBlock(x,y,z,chunkX,chunkZ);
    }
    private static int getInternalBlock(int x,int y,int z, int chunkX, int chunkZ){

        if(chunkX < 0 || chunkZ < 0 || chunkX >= limit || chunkZ >= limit){
            return 0;
        }

        //neighbor checking
        if (y < 0 || y >= 128) { //Y is caught regardless in the else clause if in bounds
            return 0;
        }
        if(x < 0) {
            return getInternalBlock(x+16,y,z,chunkX-1,chunkZ);
        }
        if (x >= 16) {
            return getInternalBlock(x-16,y,z,chunkX+1,chunkZ);
        }
        if (z < 0) {
            return getInternalBlock(x,y,z+16,chunkX,chunkZ-1);
        }
        if (z >= 16) {
            return getInternalBlock(x,y,z-16,chunkX,chunkZ+1);
        }

        //self chunk checking
        return block[chunkX][chunkZ][y][x][z];
    }

    public static void setBlock(int x,int y,int z, int chunkX, int chunkZ, int newBlock){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        if(chunkX < 0 || chunkZ < 0 || chunkX >= limit || chunkZ >= limit){
            return;
        }
        if(y > 127 || y < 0){
            return;
        }

        block[chunkX][chunkZ][y][x][z] = newBlock;
    }

    public static byte getLight(int x,int y,int z, int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;
        return getInternalLight(x,y,z,chunkX,chunkZ);
    }

    private static byte getInternalLight(int x,int y,int z, int chunkX, int chunkZ){

        if(chunkX < 0 || chunkZ < 0 || chunkX >= limit || chunkZ >= limit){
            return 0;
        }

        //neighbor checking
        if (y < 0) { //Y is caught regardless in the else clause if in bounds
            return 0;
        }
        if(y >= 128){
            return 15;
        }
        if(x < 0) {
            return getInternalLight(x+16,y,z,chunkX-1,chunkZ);
        }
        if (x >= 16) {
            return getInternalLight(x-16,y,z,chunkX+1,chunkZ);
        }
        if (z < 0) {
            return getInternalLight(x,y,z+16,chunkX,chunkZ-1);
        }
        if (z >= 16) {
            return getInternalLight(x,y,z-16,chunkX,chunkZ+1);
        }

        //self chunk checking
        return light[chunkX][chunkZ][y][x][z];
    }

    public static void setLight(int x,int y,int z, int chunkX, int chunkZ, byte newLight){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        if(chunkX < 0 || chunkZ < 0 || chunkX >= limit || chunkZ >= limit){
            return;
        }

        light[chunkX][chunkZ][y][x][z] = newLight;
    }

    private static FastNoise noise = new FastNoise();
    private static int heightAdder = 40;
    private static byte dirtHeight = 4;
    private static byte waterHeight = 50;
    //a basic biome test for terrain generation
    public static void genBiome(int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

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
                if (y <= waterHeight){
                    currBlock = 7;
                } else {
                    currBlock = 0;
                }
            }

            block[chunkX][chunkZ][y][x][z] = currBlock;

            if (currBlock == 0) {
                light[chunkX][chunkZ][y][x][z] = 15;//0;
            }else{
                light[chunkX][chunkZ][y][x][z] = 0;
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

        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        if(chunkX < 0 || chunkZ < 0 || chunkX >= limit || chunkZ >= limit){
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
        for (Mesh[] thisMeshArray : mesh){
            for (Mesh thisMesh : thisMeshArray) {
                if (thisMesh != null) {
                    thisMesh.cleanUp();
                }
            }
        }
    }
}
