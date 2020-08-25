package game.ChunkHandling;


import engine.GameItem;

import java.util.ArrayList;
import java.util.Arrays;

import static game.ChunkHandling.ChunkMath.genHash;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.Crafter.getChunkRenderDistance;
import static game.light.Light.floodFill;

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




    private static final byte torchDistance = 10;
    public static void oldFlood(int posX, int posY, int posZ, ArrayList gameItems) throws Exception {
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
            generateChunkMesh(index[0], index[1], gameItems, true);
        }
    }

    private static float getDistance(float x1, float y1, float z1, float x2, float y2, float z2){
        float x = x1 - x2;
        float y = y1 - y2;
        float z = z1 - z2;
        return (float)Math.hypot(x, Math.hypot(y,z));
    }


}
