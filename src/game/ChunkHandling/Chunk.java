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

    public Chunk(int chunkX,int chunkZ){
//        genBiome(chunkX,chunkZ);
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





    public void setBlock(int hash, short newBlock){
        block[hash] = newBlock;
    }

    public void setLight(int hash, byte light){
        this.naturalLight[hash] = light;
    }


}