package game.ChunkHandling;

import engine.FastNoise;

import java.util.Arrays;

public class Chunk {
    //y x z - longest used for memory efficiency
    private final static short chunkSizeX = 16;
    private final static short chunkSizeY = 128;
    private final static short chunkSizeZ = 16;

    private short[] block    = new short[chunkSizeX * chunkSizeY * chunkSizeZ];
    private byte[]  light    = new byte[chunkSizeX * chunkSizeY * chunkSizeZ];
    private byte[]  rotation = new byte[chunkSizeX * chunkSizeY * chunkSizeZ];

    public Chunk(int chunkX,int chunkZ){
//        if (Math.random() > 0.5) {
//            genRandom(); //this is for performance testing and uses A LOT of memory
//        } else {
//        genDebug();
//        }
//        genDebug();
        genRandom();
//        genBiome(chunkX,chunkZ);
//        genFlat();
//        System.out.println(Arrays.toString(block));
    }
    public short[] getBlocks(){
        return block;
    }

    public byte[] getLights(){
        return light;
    }

    //randomly assign block ids
    public void genRandom(){
        int x = 0;
        int y = 127;
        int z = 0;
        byte lightLevel = 16;
        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
            short currblock = (short)(Math.random() * 2);

            block[ChunkMath.genHash(x, y, z)] = currblock;

            light[ChunkMath.genHash(x, y, z)] = lightLevel;//(byte)(Math.random()*16);

            if (currblock != 0 ){
                lightLevel = 0;
            }
            y--;

            if( y < 0){
                y = 127;
                lightLevel = 16;
                x++;
                if( x > chunkSizeX - 1 ){
                    x = 0;
                    z++;
                }
            }
        }
    }

    //generate flat
    public void genFlat(){
        int x = 0;
        int y = 0;
        int z = 0;
        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
            if (y < 20) {
                block[ChunkMath.genHash(x, y, z)] = 1;
            }else{
                block[ChunkMath.genHash(x, y, z)] = 0;
            }
            light[ChunkMath.genHash(x, y, z)] = (byte)(Math.random()*16);
            y++;
            if( y > chunkSizeY - 1){
                y = 0;
                x++;
                if( x > chunkSizeX - 1 ){
                    x = 0;
                    z++;
                }
            }
        }
    }



    private FastNoise noisey = new FastNoise();

    private int heightAdder = 40;
    private byte dirtHeight = 4;
    //a basic biome test for terrain generation
    public void genBiome(int chunkX, int chunkZ){

        int x = 0;
        int y = 0;
        int z = 0;

        FastNoise noise = new FastNoise();

        byte height = (byte)(Math.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);

        //System.out.println(height);
        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
            //block[ChunkMath.genHash(x, y, z)] = (short)(FastMath.nextRandomInt(0,5));


//            if (y <= height) {
//                block[ChunkMath.genHash(x, y, z)] = (short)(FastMath.nextRandomInt(1,5));
//            } else {
//                block[ChunkMath.genHash(x, y, z)] = 0;
//            }
            if (y == height) {
                block[ChunkMath.genHash(x, y, z)] = 5;
            } else if (y < height && y >= height - dirtHeight){
                block[ChunkMath.genHash(x, y, z)] = 1;
            } else if (y < height - dirtHeight){
                block[ChunkMath.genHash(x, y, z)] = 2;
            }

            light[ChunkMath.genHash(x, y, z)] = (byte)(Math.random()*16f);

            y++;
            if( y > chunkSizeY - 1){
                y = 0;
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

    //debug testing for now
    public void genDebug(){
        int x = 0;
        int y = 0;
        int z = 0;

        short counter = 0;
        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
            short newBlock = 1;//(short)Math.ceil((Math.random()*5));
//            counter++;
//            if (counter > 19){
//                counter = 0;
//            }

//            System.out.println(newBlock);


            //TextureCalculator.calculateTextureMap(newBlock);

            int hashedPos = ChunkMath.genHash(x, y, z);

            //TODO: these are marked TODO because it shows up in yellow in my IDE

//            System.out.println("NEW BLOCK"); //TODO

//            System.out.println(hashedPos); //TODO


//            int[] tempOutput = {x, y, z};

//            System.out.println(Arrays.toString(tempOutput)); //TODO

//            int[] newHash = ChunkMath.getHash(hashedPos);

//            System.out.println(Arrays.toString(newHash)); //TODO

            block[hashedPos] = newBlock;

//            System.out.println("--------");

            y++;
            if( y > chunkSizeY - 1){
                y = 0;
                x++;
                if( x > chunkSizeX - 1 ){
                    x = 0;
                    z++;
                }
            }
        }
    }

    public void printChunk(){
        for (int i = 0; i < block.length; i++){
//            System.out.println(block[i]);
//            if(block[i] != 1){ //this is debug
//                System.out.printf("WARNING!");
//            }
        }
    }
}