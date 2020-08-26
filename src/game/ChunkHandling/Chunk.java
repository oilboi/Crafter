package game.ChunkHandling;

import engine.FastNoise;

public class Chunk {

    private final byte debugLightLevel = 15;

    //y x z - longest used for memory efficiency
    private final static short chunkSizeX = 16;
    private final static short chunkSizeY = 128;
    private final static short chunkSizeZ = 16;

    private String name;
    private short[] block       = new short[chunkSizeX * chunkSizeY * chunkSizeZ];
    private byte[] naturalLight = new  byte[chunkSizeX * chunkSizeY * chunkSizeZ];
    private byte[] torchLight   = new  byte[chunkSizeX * chunkSizeY * chunkSizeZ];
    private byte[]  rotation    = new  byte[chunkSizeX * chunkSizeY * chunkSizeZ];

    public Chunk(int chunkX,int chunkZ){
        genBiome(chunkX,chunkZ);
        name = chunkX + " " + chunkZ;
    }

    public short[] getBlocks(){
        return block;
    }

    public byte[] getLights(){
        return naturalLight;
    }

    public void setAllLights(byte[] light){
        this.naturalLight = light;
    }

    private FastNoise noise = new FastNoise();
    private int heightAdder = 40;
    private byte dirtHeight = 4;

    //a basic biome test for terrain generation
    public void genBiome(int chunkX, int chunkZ){
        int x = 0;
        int y = 127;
        int z = 0;
        byte height = (byte)(Math.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);

        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){

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


            block[ChunkMath.genHash(x, y, z)] = currBlock;

            if (currBlock == 0) {
                naturalLight[ChunkMath.genHash(x, y, z)] = debugLightLevel;//0;
            }else{
                naturalLight[ChunkMath.genHash(x, y, z)] = 0;
            }

            y--;
            if( y < 0){
                y = 127;
                x++;
                height = (byte)(Math.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);
                if( x > chunkSizeX - 1 ){
                    x = 0;
                    height = (byte)(Math.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);
                    z++;
                }
            }
        }
    }



    public void setBlock(int hash, short newBlock){
        block[hash] = newBlock;
    }

    public void setLight(int hash, byte light){
        this.naturalLight[hash] = light;
    }



    public String getName(){
        return name;
    }

    public String setName(){
        return name;
    }

}