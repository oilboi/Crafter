package game.ChunkHandling;


import engine.GameItem;

import java.util.ArrayList;
import java.util.Arrays;

import static game.ChunkHandling.ChunkMath.genHash;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.Crafter.getChunkRenderDistance;

public class ChunkData {

    private final static int chunkRenderDistance = getChunkRenderDistance();

    private static Chunk chunkArray[][] = new Chunk[(chunkRenderDistance *2)+1][(chunkRenderDistance *2)+1];

    public static void storeChunk(int x, int z, Chunk chunk){
        chunkArray[x + chunkRenderDistance][z + chunkRenderDistance] = chunk;
    }

    public static Chunk getChunkData(int x, int z){
        Chunk thisChunk = chunkArray[x + chunkRenderDistance][z + chunkRenderDistance];
        if (thisChunk != null){
            return thisChunk;
        }
        return null;
    }

    public static boolean chunkExists(int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;
        //safety checks
        if(chunkX >= chunkArray.length || chunkX < 0){
            return false;
        }
        if(chunkZ >= chunkArray.length || chunkZ < 0){
            return false;
        }
        //check if chunk exists
        if ( chunkArray[chunkX][chunkZ] == null){
            return false;
        }
        //all checks pass
        return true;
    }

    public static short getBlock(int x, int y, int z, int chunkX, int chunkZ){
        if (chunkX < 0 || chunkX > chunkRenderDistance *2 || chunkZ < 0 || chunkZ > chunkRenderDistance *2 || y < 0 || y >= 128){
            return 0;
        }
        Chunk thisChunk = chunkArray[chunkX][chunkZ];
        if (thisChunk == null){
            return 0;
        }
        return thisChunk.getBlocks()[genHash(x, y, z)];
    }

    public static byte getLight(int x, int y, int z, int chunkX, int chunkZ){
        if (chunkX < 0 || chunkX > chunkRenderDistance *2 || chunkZ < 0 || chunkZ > chunkRenderDistance *2 || y < 0 || y >= 128){
            return 0;
        }
        Chunk thisChunk = chunkArray[chunkX][chunkZ];
        if (thisChunk == null){
            return 0;
        }
        return thisChunk.getLights()[genHash(x, y, z)];
    }

    public static Chunk getChunk(int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;
        if (chunkX < 0 || chunkX > chunkRenderDistance *2 || chunkZ < 0 || chunkZ > chunkRenderDistance *2 ){
            return null;
        }
        Chunk thisChunk = chunkArray[chunkX][chunkZ];
        if (thisChunk == null){
            return null;
        }
        return thisChunk;
    }

    public static void setBlock(int x, int y, int z, int chunkX, int chunkZ, short newBlock){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        if (chunkX < 0 || chunkX > chunkRenderDistance *2 || chunkZ < 0 || chunkZ > chunkRenderDistance *2 || y < 0 || y >= 128){
            return;
        }
        Chunk thisChunk = chunkArray[chunkX][chunkZ];
        if (thisChunk == null) {
            return;
        }
        thisChunk.setBlock(genHash(x, y, z), newBlock);

//        updateLightColumn(x,z,chunkX-chunkRenderDistance, chunkZ-chunkRenderDistance);
//        updateLighting(x,y,z,chunkX,chunkZ);
        floodFill(chunkX,chunkZ);
    }

    //+ render distance is getting it to base count 0
    public static short getBlockInChunk(int x,int y,int z, int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;
        //neighbor checking
        if(x < 0) {
            if (chunkX - 1 >= 0) {
                return getBlock(x+16,y,z,chunkX-1,chunkZ);
            }
            return 0;
        } else if (x >= 16) {
            if ( chunkX + 1 <= chunkRenderDistance *2){
                return getBlock(x-16,y,z,chunkX+1,chunkZ);
            }
            return 0;
        } else if (y < 0 || y >= 128) { //Y is caught regardless in the else clause if in bounds
            return 0;
        } else if (z < 0) {
            if (chunkZ - 1 >= 0) {
                return getBlock(x,y,z+16,chunkX,chunkZ-1);
            }
            return 0;

        } else if (z >= 16) {
            if (chunkZ + 1 <= chunkRenderDistance *2){
                return getBlock(x,y,z-16,chunkX,chunkZ+1);
            }
            return 0;
        }
        //self chunk checking
        else {
            return getBlock(x,y,z,chunkX,chunkZ);
        }
    }

    //+ render distance is getting it to base count 0
    public static byte getLightInChunk(int x,int y,int z, int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        //neighbor checking
        if(x < 0) {
            if (chunkX - 1 >= 0) {
                return getLight(x+16,y,z,chunkX-1,chunkZ);
            }
            return 0;
        } else if (x >= 16) {
            if ( chunkX + 1 <= chunkRenderDistance *2){
                return getLight(x-16,y,z,chunkX+1,chunkZ);
            }
            return 0;
        } else if (y < 0 || y >= 128) { //Y is caught regardless in the else clause if in bounds
            return 0;
        } else if (z < 0) {
            if (chunkZ - 1 >= 0) {
                return getLight(x,y,z+16,chunkX,chunkZ-1);
            }
            return 0;

        } else if (z >= 16) {
            if (chunkZ + 1 <= chunkRenderDistance *2){
                return getLight(x,y,z-16,chunkX,chunkZ+1);
            }
            return 0;
        }
        //self chunk checking
        else {
            return getLight(x,y,z,chunkX,chunkZ);
        }
    }

    public static void updateLightColumn(int x,int z, int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;
        if (chunkX < 0 || chunkX > chunkRenderDistance *2 || chunkZ < 0 || chunkZ > chunkRenderDistance *2){
            return;
        }
        Chunk thisChunk = chunkArray[chunkX][chunkZ];
        if (thisChunk == null) {
            return;
        }

        byte lightLevel = 16;
        for (int y = 127; y >= 0; y--) {
            thisChunk.setLight(genHash(x, y, z), lightLevel);
            if (getBlock(x,y,z,chunkX,chunkZ) != 0 && lightLevel > 0){
                lightLevel --;
            }
        }
    }

    public static boolean underSunlight(int x, int y, int z, int chunkX, int chunkZ){
        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        if (chunkX < 0 || chunkX > chunkRenderDistance *2 || chunkZ < 0 || chunkZ > chunkRenderDistance *2){
            return false;
        }
        Chunk thisChunk = chunkArray[chunkX][chunkZ];
        if (thisChunk == null) {
            return false;
        }
        for (int indexY = 127; indexY > y; indexY--) {
            if (getBlock(x,indexY,z,chunkX,chunkZ) != 0){
                return false;
            }
        }
        return true;
    }


    public static void floodFill(int chunkX, int chunkZ) {
        chunkX -= chunkRenderDistance;
        chunkZ -= chunkRenderDistance;

        Chunk thisChunk = getChunk(chunkX,chunkZ);

        int    [][][] pseudoChunk     = new     int[16][128][16];
        int    [][][] caveLight       = new     int[16][128][16];
        boolean[][][] lightRayChunk   = new boolean[16][128][16];

        //shove everything into the 3D arrays
        //fill sun rays with sunlight
        for (int x = 0; x <= 15; x++){
            for(int z = 0; z <= 15; z++){
                for (int y = 0; y <= 127; y++){
                    short thisBlock = getBlockInChunk(x,y,z, chunkX,chunkZ);
                    //lit air
                    if (thisBlock == 0 && underSunlight(x,y,z,chunkX,chunkZ)) {
                        pseudoChunk[x][y][z] = 2;
                        thisChunk.setLight(genHash(x,y,z), (byte)16);
                        //cave air
                    } else if (thisBlock == 0 && !underSunlight(x,y,z,chunkX,chunkZ)){
                        pseudoChunk[x][y][z] = 1;
                        thisChunk.setLight(genHash(x,y,z), (byte)0);
                    } else {
                        pseudoChunk[x][y][z] = -1;
                        thisChunk.setLight(genHash(x,y,z), (byte)0);
                    }
                }
            }
        }

        //2 is air light source
        //1 is cave light
        //-1 is any solid block

        //flood the whole thing with sunlight
        //this is the most ridiculous code I've ever written
        for(int y = 127; y > 0; y--){
            for (int x = 0; x <= 15; x++){
                for(int z = 0; z <= 15; z++) {
                    //only get if cave light
                    if(pseudoChunk[x][y][z] == 1) {
                        //index surroundings
                        for (int xx = -1; xx <= 1; xx++) {
                            for (int yy = -1; yy <= 1; yy++) {
                                for (int zz = -1; zz <= 1; zz++) {
                                    //only do binary direction
                                    if(Math.abs(xx) + Math.abs(yy) + Math.abs(zz) == 1) {
                                        int thisIndexX = x + xx;
                                        int thisIndexY = y + yy;
                                        int thisIndexZ = z + zz;
                                        //catch boundaries
                                        if (thisIndexX >= 0 && thisIndexX <= 15 && thisIndexY >= 0 && thisIndexY <= 127 && thisIndexZ >= 0 && thisIndexZ <= 15) {
                                            //get if sunlight
                                            if (pseudoChunk[thisIndexX][thisIndexY][thisIndexZ] == 2){
                                                //begin distribution queue
                                                ArrayList queue = new ArrayList();
                                                queue.add(new int[]{thisIndexX,thisIndexY,thisIndexZ,16});
                                                while(queue.size() > 0){
                                                    int[] firstIndex = (int[]) queue.get(0);
                                                    int currentLight = firstIndex[3];
                                                    //only pass on light if not pitch black
                                                    if(currentLight > 1) {
                                                        //index surroundings
                                                        for (int xxx = -1; xxx <= 1; xxx++) {
                                                            for (int yyy = -1; yyy <= 1; yyy++) {
                                                                for (int zzz = -1; zzz <= 1; zzz++) {
                                                                    if(Math.abs(xxx) + Math.abs(yyy) + Math.abs(zzz) == 1) {
                                                                        int thisIndexX2 = firstIndex[0] + xxx;
                                                                        int thisIndexY2 = firstIndex[1] + yyy;
                                                                        int thisIndexZ2 = firstIndex[2] + zzz;
                                                                        //catch boundaries
                                                                        if (thisIndexX2 >= 0 && thisIndexX2 <= 15 && thisIndexY2 >= 0 && thisIndexY2 <= 127 && thisIndexZ2 >= 0 && thisIndexZ2 <= 15) {
                                                                            //only spread to cave light that's darker
                                                                            if (pseudoChunk[thisIndexX2][thisIndexY2][thisIndexZ2] == 1 && caveLight[thisIndexX2][thisIndexY2][thisIndexZ2] < currentLight) {
                                                                                caveLight[thisIndexX2][thisIndexY2][thisIndexZ2] = currentLight - 1;
                                                                                queue.add(new int[]{thisIndexX2, thisIndexY2, thisIndexZ2, currentLight - 1});
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    //NEVER REMOVE THIS
                                                    queue.remove(firstIndex);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for(int y = 127; y > 0; y--) {
            for (int x = 0; x <= 15; x++) {
                for (int z = 0; z <= 15; z++) {
                    if(pseudoChunk[x][y][z] == 1) {
                        int newCaveLight = caveLight[x][y][z];
                        thisChunk.setLight(genHash(x, y, z), (byte) newCaveLight);
                    }
                }
            }
        }
    }

    private static final byte torchDistance = 10;
    public static void oldFlood(int posX, int posY, int posZ, GameItem[] gameItems, String[] chunkNames) throws Exception {
        ArrayList<int[]> chunkBuffer = new ArrayList();

        //System.out.println(posX + " " + posY + " " + posZ);
        byte lightLevel;
        for (int x = posX-torchDistance; x < posX + torchDistance; x++){
            for (int y = posY - torchDistance; y < posY + torchDistance; y++) {
                for (int z = posZ - torchDistance; z < posZ + torchDistance; z++) {
                    int currentChunkX = (int) (Math.floor((float) x / 16f));
                    int currentChunkZ = (int) (Math.floor((float) z / 16f));


                    int currentPosX = x - (16*currentChunkX);
                    int currentPosZ = z - (16*currentChunkZ);

                    if (currentChunkX >= -chunkRenderDistance && currentChunkX <= chunkRenderDistance && currentChunkZ >= -chunkRenderDistance && currentChunkZ <= chunkRenderDistance){
                        if(y >= 0 && y <= 127) {


                            lightLevel = (byte)(torchDistance - getDistance(posX, posY, posZ, x, y, z));
                            if(lightLevel < 0){
                                lightLevel = 0;
                            } else if (lightLevel > 16){
                                lightLevel = 16;
                            }


                            getChunk(currentChunkX, currentChunkZ).setLight(genHash(currentPosX, y, currentPosZ), lightLevel);

                            //add chunks to chunk generation buffer ID: 555
                            boolean found = false;
                            if (chunkBuffer.size() > 0) {
                                for (int[] index : chunkBuffer) {
                                    if (index[0] == currentChunkX && index[1] == currentChunkZ) {
                                        found = true;
                                    }
                                }
                            }
                            if (!found) {
                                chunkBuffer.add(new int[]{currentChunkX, currentChunkZ});
                            }
                            //end: ID: 555
                        }
                    }
                }
            }
        }

        for (int[] index : chunkBuffer) {
//            System.out.println(index[0]);
            generateChunkMesh(index[0], index[1], gameItems, chunkNames, true);
        }
    }

    private static float getDistance(float x1, float y1, float z1, float x2, float y2, float z2){
        float x = x1 - x2;
        float y = y1 - y2;
        float z = z1 - z2;
        return (float)Math.hypot(x, Math.hypot(y,z));
    }


}
