package engine;

import engine.graph.Mesh;
import game.ChunkHandling.ChunkMath;

import static game.ChunkHandling.ChunkMath.genChunkHash;

public class ChunkObject {

    private static int [][] block;
    private static byte[][] light;
    private static Mesh[]   mesh;

    private static int limit;

    public static void initializeChunkHandler(int chunkRenderDistance){
        block = new int[((chunkRenderDistance * 2) + 1) * ((chunkRenderDistance * 2) + 1)][16*128*16];
        light = new byte[((chunkRenderDistance * 2) + 1) * ((chunkRenderDistance * 2) + 1)][16*128*16];
        mesh  = new Mesh[((chunkRenderDistance * 2) + 1) * ((chunkRenderDistance * 2) + 1)];
        limit = ((chunkRenderDistance * 2) + 1) * ((chunkRenderDistance * 2) + 1);
    }

    public static void setChunkMesh(int chunkX, int chunkZ, Mesh newMesh){
        mesh[genChunkHash(chunkX,chunkZ)] = newMesh;
    }

    public static int getLimit(){
        return limit;
    }

    public static Mesh getChunkMesh(int i){
        return mesh[i];
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

}
