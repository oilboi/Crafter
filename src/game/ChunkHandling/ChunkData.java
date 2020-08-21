package game.ChunkHandling;


import game.Crafter;

import static game.Crafter.getChunkRenderDistance;

public class ChunkData {
    private final static int chunkRenderDistance = getChunkRenderDistance();

    private static Chunk chunkArray[][] = new Chunk[(chunkRenderDistance *2)+1][(chunkRenderDistance *2)+1];
    public static void storeChunk(int x, int z, Chunk chunk){

//        System.out.println((x + chunkRenderDistance) + " " + (z + chunkRenderDistance));

//        System.out.println(chunk);

        chunkArray[x + chunkRenderDistance][z + chunkRenderDistance] = chunk;

//        System.out.println(chunkArray[x + chunkRenderDistance][z + chunkRenderDistance]);
    }

    public static Chunk getChunkData(int x, int z){
        Chunk test = chunkArray[x + chunkRenderDistance][z + chunkRenderDistance];

        if (test != null){
            return test;
        }
        return null;
    }

    //TODO the other function needs to be removed or reworked or moved to this class maybe
    public static short getBlock(int x, int y, int z, int chunkX, int chunkZ){
        //System.out.println(x + " " + y + " " + z + " " + chunkX + " " + chunkZ);
        //System.out.println(Arrays.deepToString(chunkArray));
        if (chunkX < 0 || chunkX > chunkRenderDistance *2 || chunkZ < 0 || chunkZ > chunkRenderDistance *2 || y < 0 || y >= 128){
            return 0;
        }

        Chunk piece = chunkArray[chunkX][chunkZ];

        if (piece == null){
            return 0;
        }

        //System.out.println("x " + x + " | y: " + y + " | z:" + z);
        int hashy = ChunkMath.genHash(x, y, z);

        //System.out.println();
        short[] blocksYo = piece.getBlocks();

        return blocksYo[hashy];
    }


    public static Chunk getChunk(int chunkX, int chunkZ){

        chunkX += chunkRenderDistance;
        chunkZ += chunkRenderDistance;

        if (chunkX < 0 || chunkX > chunkRenderDistance *2 || chunkZ < 0 || chunkZ > chunkRenderDistance *2 ){
            return null;
        }

        Chunk piece = chunkArray[chunkX][chunkZ];

        if (piece == null){
            return null;
        }

        return piece;
    }


    public static void setBlock(int x, int y, int z, int chunkX, int chunkZ, short newBlock){
        if (chunkX < 0 || chunkX > chunkRenderDistance *2 || chunkZ < 0 || chunkZ > chunkRenderDistance *2 || y < 0 || y >= 128){
            return;
        }
        Chunk piece = chunkArray[chunkX][chunkZ];
        if (piece == null){
            return;
        }
        int hashy = ChunkMath.genHash(x, y, z);
        piece.setBlock(hashy, newBlock);
    }

    public static boolean chunkExists(int chunkX, int chunkZ){
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


    //this is full of magic numbers TODO: turn this into functions in ChunkMath
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
            return ChunkData.getBlock(x,y,z,chunkX,chunkZ);
        }
    }
}
