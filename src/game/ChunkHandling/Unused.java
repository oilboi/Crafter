package game.ChunkHandling;

import java.util.ArrayList;

import static game.ChunkHandling.ChunkMath.genHash;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;

public class Unused {
    //randomly assign block ids
//    public void genRandom(){
//        int x = 0;
//        int y = 127;
//        int z = 0;
//        byte lightLevel = 16;
//        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
//            short currblock = (short)(Math.random() * 2);
//
//            block[ChunkMath.genHash(x, y, z)] = currblock;
//
//            naturalLight[ChunkMath.genHash(x, y, z)] = debugLightLevel;//lightLevel;//(byte)(Math.random()*16);
//
////            if (currblock != 0 && lightLevel > 0){
////                lightLevel --;
////            }
//            y--;
//
//            if( y < 0){
//                y = 127;
////                lightLevel = 16;
//                x++;
//                if( x > chunkSizeX - 1 ){
//                    x = 0;
//                    z++;
//                }
//            }
//        }
//    }
//
//    //generate flat
//    public void genFlat(){
//        int x = 0;
//        int y = 127;
//        int z = 0;
//        byte lightLevel = debugLightLevel;
//        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
//
//            short currBlock;
//
//            if (y < 20) {
//                currBlock = 1;
//            }else{
//                currBlock = 0;
//            }
//
//            block[ChunkMath.genHash(x, y, z)] = currBlock;
//            naturalLight[ChunkMath.genHash(x, y, z)] = lightLevel;
//
//            if (currBlock != 0 && lightLevel > 0){
//                lightLevel --;
//            }
//
//            y--;
//            if( y < 0){
//                y = 127;
//                lightLevel = debugLightLevel;
//                x++;
//                if( x > chunkSizeX - 1 ){
//                    x = 0;
//                    z++;
//                }
//            }
//        }
//    }
//    //debug testing for now
//    public void genDebug(){
//        int x = 0;
//        int y = 0;
//        int z = 0;
//
//        short counter = 0;
//        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
//            short newBlock = 1;//(short)Math.ceil((Math.random()*5));
////            counter++;
////            if (counter > 19){
////                counter = 0;
////            }
//
////            System.out.println(newBlock);
//
//
//            //TextureCalculator.calculateTextureMap(newBlock);
//
//            int hashedPos = ChunkMath.genHash(x, y, z);
//
//            //TODO: these are marked TODO because it shows up in yellow in my IDE
//
////            System.out.println("NEW BLOCK"); //TODO
//
////            System.out.println(hashedPos); //TODO
//
//
////            int[] tempOutput = {x, y, z};
//
////            System.out.println(Arrays.toString(tempOutput)); //TODO
//
////            int[] newHash = ChunkMath.getHash(hashedPos);
//
////            System.out.println(Arrays.toString(newHash)); //TODO
//
//            block[hashedPos] = newBlock;
//
////            System.out.println("--------");
//
//            y++;
//            if( y > chunkSizeY - 1){
//                y = 0;
//                x++;
//                if( x > chunkSizeX - 1 ){
//                    x = 0;
//                    z++;
//                }
//            }
//        }
//    }
//
//    public void printChunk(){
//        for (int i = 0; i < block.length; i++){
////            System.out.println(block[i]);
////            if(block[i] != 1){ //this is debug
////                System.out.printf("WARNING!");
////            }
//        }
//    }
//    private static final byte torchDistance = 10;
//    public static void oldFlood(int posX, int posY, int posZ, ArrayList gameItems) throws Exception {
//        ArrayList<int[]> chunkBuffer = new ArrayList();
//
//        //System.out.println(posX + " " + posY + " " + posZ);
//        byte lightLevel;
//        for (int x = posX-torchDistance; x < posX + torchDistance; x++){
//            for (int y = posY - torchDistance; y < posY + torchDistance; y++) {
//                for (int z = posZ - torchDistance; z < posZ + torchDistance; z++) {
//                    int currentChunkX = (int) (Math.floor((float) x / 16f));
//                    int currentChunkZ = (int) (Math.floor((float) z / 16f));
//
//
//                    int currentPosX = x - (16*currentChunkX);
//                    int currentPosZ = z - (16*currentChunkZ);
//
//                    if (currentChunkX >= -chunkRenderDistance && currentChunkX <= chunkRenderDistance && currentChunkZ >= -chunkRenderDistance && currentChunkZ <= chunkRenderDistance){
//                        if(y >= 0 && y <= 127) {
//
//
//                            lightLevel = (byte)(torchDistance - getDistance(posX, posY, posZ, x, y, z));
//                            if(lightLevel < 0){
//                                lightLevel = 0;
//                            } else if (lightLevel > 16){
//                                lightLevel = 16;
//                            }
//
//
//                            getChunk(currentChunkX, currentChunkZ).setLight(genHash(currentPosX, y, currentPosZ), lightLevel);
//
//                            //add chunks to chunk generation buffer ID: 555
//                            boolean found = false;
//                            if (chunkBuffer.size() > 0) {
//                                for (int[] index : chunkBuffer) {
//                                    if (index[0] == currentChunkX && index[1] == currentChunkZ) {
//                                        found = true;
//                                    }
//                                }
//                            }
//                            if (!found) {
//                                chunkBuffer.add(new int[]{currentChunkX, currentChunkZ});
//                            }
//                            //end: ID: 555
//                        }
//                    }
//                }
//            }
//        }
//
//        for (int[] index : chunkBuffer) {
////            System.out.println(index[0]);
//            generateChunkMesh(index[0], index[1], gameItems, true);
//        }
//    }
}
