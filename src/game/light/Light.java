package game.light;

import game.ChunkHandling.Chunk;

import java.util.ArrayList;

import static game.ChunkHandling.ChunkData.*;
import static game.ChunkHandling.ChunkMath.genHash;

public class Light {

    private static final byte debugLightLevel = 15;

    public static void floodFill(int chunkX, int chunkZ) {
        Chunk thisChunk = getChunk(chunkX,chunkZ);
        for (int y = 127; y >= 0; y--){
            for (int x = 0; x <= 15; x++){
                for (int z = 0; z <= 15; z++){

                    short thisBlock = getBlockInChunk(x,y,z,chunkX,chunkZ);

                    if(underSunlight(x,y,z,chunkX,chunkZ) && thisBlock == 0){
                        thisChunk.setLight(genHash(x,y,z), debugLightLevel);
                    } else {
                        thisChunk.setLight(genHash(x,y,z), (byte) 9);
                    }
                }
            }
        }
    }

////        long startTime = System.nanoTime();
//
//        Chunk thisChunk = getChunk(chunkX,chunkZ);
//
//        if (thisChunk == null){
//            return;
//        }
//        int[] pseudoChunk = new int[16*128*16];
//
//        short[] theseBlocks = thisChunk.getBlocks();
//        if (theseBlocks == null){
//            return;
//        }
//        byte[]  theseLights = thisChunk.getLights();
//
//        for(int i = 0; i < theseLights.length; i++){
//            theseLights[i] = 0;
//        }
//
//        int x = 0;
//        int y = 127;
//        int z = 0;
//        byte sunLight = debugLightLevel;
//
//        for (int i = 0; i < 16*128*16; i++){
//            short thisBlock = theseBlocks[genHash(x,y,z)];//getBlockInChunk(x,y,z, chunkX,chunkZ);
//
//            //lit air
//            if (thisBlock == 0 && sunLight == 15) {
//                pseudoChunk[genHash(x,y,z)] = 2;
//                theseLights[genHash(x,y,z)] = sunLight;
//                //cave air
//            } else if (thisBlock == 0){
//                pseudoChunk[genHash(x,y,z)] = 1;
//                theseLights[genHash(x,y,z)] = sunLight;
//                //solid blocks
//            } else {
//                pseudoChunk[genHash(x,y,z)] = -1;
//                theseLights[genHash(x,y,z)] = sunLight;
//                sunLight = 0;
//            }
//            y--;
//            if( y < 0){
//                y = 127;
//                x++;
//                sunLight = debugLightLevel;
//                if( x > 15 ){
//                    x = 0;
//                    z++;
//                }
//            }
//        }
//
////        long endTime = System.nanoTime();
////        System.out.println("Data Collection Time: " + (endTime - startTime));
////
////        //2 is air light source
////        //1 is cave light
////        //-1 is any solid block
////
//        x = 0;
//        y = 127;
//        z = 0;
//
//        byte localX = 0;
//        byte localY = 0;
//        byte localZ = 0;
//
//        for (int i = 0; i < 16*128*16; i++){
//            //only get if cave light
//            if(pseudoChunk[genHash(x,y,z)] == 1) {
//                for (int w = 0; w < 6; w++){
//                    switch (w){
//                        case 0:
//                            localX = -1;
//                            localY =  0;
//                            localZ =  0;
//                            break;
//                        case 1:
//                            localX =  0;
//                            localY = -1;
//                            localZ =  0;
//                            break;
//                        case 2:
//                            localX =  0;
//                            localY =  0;
//                            localZ = -1;
//                            break;
//                        case 3:
//                            localX =  1;
//                            localY =  0;
//                            localZ =  0;
//                            break;
//                        case 4:
//                            localX =  0;
//                            localY =  1;
//                            localZ =  0;
//                            break;
//                        case 5:
//                            localX =  0;
//                            localY =  0;
//                            localZ =  1;
//                            break;
//                    }
//
//                    int thisIndexX = x + localX;
//                    int thisIndexY = y + localY;
//                    int thisIndexZ = z + localZ;
//
//                    if (thisIndexX >= 0 && thisIndexX <= 15 && thisIndexY >= 0 && thisIndexY <= 127 && thisIndexZ >= 0 && thisIndexZ <= 15) {
//                        //get if sunlight
//                        if (pseudoChunk[genHash(thisIndexX,thisIndexY,thisIndexZ)] == 2){
//                            pseudoChunk[genHash(thisIndexX,thisIndexY,thisIndexZ)] = -1;
//                            processLightQueue(thisIndexX, thisIndexY, thisIndexZ,pseudoChunk, theseLights);
//                        }
//                    }
//                }
//            }
//            y--;
//            if( y < 0){
//                y = 127;
//                x++;
//                if( x > 15 ){
//                    x = 0;
//                    z++;
//                }
//            }
//        }
//    }


//    public static void processLightQueue(int thisIndexX, int thisIndexY, int thisIndexZ,int[] pseudoChunk, byte[] theseLights){
//        //begin distribution queue
//
//        ArrayList queue = new ArrayList();
//
//        queue.add(new int[]{thisIndexX,thisIndexY,thisIndexZ, (int) debugLightLevel});
//
////        int count = 0;
//
//        byte localX = 0;
//        byte localY = 0;
//        byte localZ = 0;
//
//        while(queue.size() > 0){
//
//            //stop infinite loops
//            if (queue.size() > 256){
//                break;
//            }
//            int[] firstIndex = (int[]) queue.get(0);
//
//            int currentLight = firstIndex[3];
//            //only pass on light if not pitch black
//            if(currentLight > 1) {
//                //index surroundings
//                for (int w = 0; w < 6; w++) {
//                    switch (w) {
//                        case 0:
//                            localX = -1;
//                            localY = 0;
//                            localZ = 0;
//                            break;
//                        case 1:
//                            localX = 0;
//                            localY = -1;
//                            localZ = 0;
//                            break;
//                        case 2:
//                            localX = 0;
//                            localY = 0;
//                            localZ = -1;
//                            break;
//                        case 3:
//                            localX = 1;
//                            localY = 0;
//                            localZ = 0;
//                            break;
//                        case 4:
//                            localX = 0;
//                            localY = 1;
//                            localZ = 0;
//                            break;
//                        case 5:
//                            localX = 0;
//                            localY = 0;
//                            localZ = 1;
//                            break;
//                    }
//
//                    int thisIndexX2 = firstIndex[0] + localX;
//                    int thisIndexY2 = firstIndex[1] + localY;
//                    int thisIndexZ2 = firstIndex[2] + localZ;
//                    //catch boundaries
//                    if (thisIndexX2 >= 0 && thisIndexX2 <= 15 && thisIndexY2 >= 0 && thisIndexY2 <= 127 && thisIndexZ2 >= 0 && thisIndexZ2 <= 15) {
//                        //only spread to cave light that's darker
//                        if (pseudoChunk[genHash(thisIndexX2, thisIndexY2, thisIndexZ2)] == 1 && theseLights[genHash(thisIndexX2, thisIndexY2, thisIndexZ2)] < currentLight) {
//                            theseLights[genHash(thisIndexX2, thisIndexY2, thisIndexZ2)] = (byte) (currentLight - 1);
//                            queue.add(new int[]{thisIndexX2, thisIndexY2, thisIndexZ2, currentLight - 1});
//                        }
//                    }
//                }
//            }
//            //NEVER REMOVE THIS
//            queue.remove(firstIndex);
//        }
//    }
}