package engine;

import engine.graph.Mesh;

import static engine.ChunkUpdateHandler.chunkUpdate;
import static game.Crafter.chunkRenderDistance;

public class Chunk {

    private static int [][][][][] block;
    private static byte [][][][][] rotation;
    private static byte[][][][][] light;
    private static Mesh[][]   mesh;

    private static int limit = 0;

    public static void initializeChunkHandler(int chunkRenderDistance){
        block = new  int[((chunkRenderDistance * 2) + 1)][((chunkRenderDistance * 2) + 1)][128][16][16];
        light = new byte[((chunkRenderDistance * 2) + 1)][((chunkRenderDistance * 2) + 1)][128][16][16];
        rotation = new byte[((chunkRenderDistance * 2) + 1)][((chunkRenderDistance * 2) + 1)][128][16][16];
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
            mesh[chunkX][chunkZ].cleanUp(false);
        }
        mesh[chunkX][chunkZ] = newMesh;
    }

    public static int getLimit(){
        return limit;
    }

    public static Mesh getChunkMesh(int chunkX, int chunkZ){
        return mesh[chunkX][chunkZ];
    }

    public static byte getRotation(int x,int y,int z, int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        return getInternalRotation(x,y,z,chunkX,chunkZ);
    }
    private static byte getInternalRotation(int x,int y,int z, int chunkX, int chunkZ){

        if(chunkX < 0 || chunkZ < 0 || chunkX >= limit || chunkZ >= limit){
            return 0;
        }

        //neighbor checking
        if (y < 0 || y >= 128) { //Y is caught regardless in the else clause if in bounds
            return 0;
        }
        if(x < 0) {
            return getInternalRotation(x+16,y,z,chunkX-1,chunkZ);
        }
        if (x >= 16) {
            return getInternalRotation(x-16,y,z,chunkX+1,chunkZ);
        }
        if (z < 0) {
            return getInternalRotation(x,y,z+16,chunkX,chunkZ-1);
        }
        if (z >= 16) {
            return getInternalRotation(x,y,z-16,chunkX,chunkZ+1);
        }

        //self chunk checking
        return rotation[chunkX][chunkZ][y][x][z];
    }

    public static void setRotation(int x,int y,int z, int chunkX, int chunkZ, byte newRotation){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        if(chunkX < 0 || chunkZ < 0 || chunkX >= limit || chunkZ >= limit){
            return;
        }
        if(z >= 16 || z < 0){
            return;
        }
        if(x >= 16 || x < 0){
            return;
        }
        if(y > 127 || y < 0){
            return;
        }

        rotation[chunkX][chunkZ][y][x][z] = newRotation;
        chunkUpdate(chunkX,chunkZ);
        updateNeighbor(chunkX, chunkZ,x,y,z);
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
        if(z >= 16 || z < 0){
            return;
        }
        if(x >= 16 || x < 0){
            return;
        }
        if(y > 127 || y < 0){
            return;
        }

        block[chunkX][chunkZ][y][x][z] = newBlock;
        chunkUpdate(chunkX,chunkZ);
        updateNeighbor(chunkX, chunkZ,x,y,z);
    }

    public static void setBlock(int x, int y, int z, int newBlock){
        int currentChunkX = (int) (Math.floor((float) x / 16f));
        int currentChunkZ = (int) (Math.floor((float) z / 16f));
        int currentPosX = x - (16 * currentChunkX);
        int currentPosZ = z - (16 * currentChunkZ);

        currentChunkX += chunkRenderDistance;
        currentChunkZ += chunkRenderDistance;
        if(currentChunkX < 0 || currentChunkZ < 0 || currentChunkX >= limit || currentChunkZ >= limit){
            return;
        }
        if(y > 127 || y < 0){
            return;
        }

        block[currentChunkX][currentChunkZ][y][currentPosX][currentPosZ] = newBlock;
        chunkUpdate(currentChunkX,currentChunkZ);
        updateNeighbor(currentChunkX, currentChunkZ,x,y,z);
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
        chunkUpdate(chunkX,chunkZ);
        updateNeighbor(chunkX, chunkZ,x,y,z);
    }

    private static void updateNeighbor(int chunkX, int chunkZ, int x, int y, int z){
        if (x == 15){ //update neighbor
            chunkUpdate(chunkX+1, chunkZ);
        }

        if (x == 0){
            chunkUpdate(chunkX-1, chunkZ);
        }

        if (z == 15){
            chunkUpdate(chunkX, chunkZ+1);
        }
        if (z == 0){
            chunkUpdate(chunkX, chunkZ-1);
        }
    }

    private static FastNoise noise = new FastNoise();
    private static int heightAdder = 40;
    private static byte dirtHeight = 4;
    private static byte waterHeight = 50;
    //a basic biome test for terrain generation
    public static void genBiome(int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;
        short currBlock;

        byte height = (byte)(Math.abs(noise.GetCubicFractal((chunkX*16),(chunkZ*16)))*127+heightAdder);
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

                    block[chunkX][chunkZ][y][x][z] = currBlock;

//            if (currBlock == 0) {
                    light[chunkX][chunkZ][y][x][z] = 15;//0;
//            }else{
//                light[chunkX][chunkZ][y][x][z] = 0;
//            }
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
                    thisMesh.cleanUp(true);
                }
            }
        }
    }
}
