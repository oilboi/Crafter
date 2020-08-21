package game.ChunkHandling;

import static game.Crafter.chunkRenderDistance;

public class ChunkData {
    private static Chunk chunkArray[][] = new Chunk[(chunkRenderDistance *2)+1][(chunkRenderDistance *2)+1];

//    private static int[] currentChunk = {0,0};

    public static void storeChunk(int x, int z, Chunk chunk){
//        System.out.println();
//        System.out.println("this is the data:");
//        System.out.println("max:" + ((renderDistance*2)+1));
//        System.out.println(x + renderDistance);

        chunkArray[x + chunkRenderDistance][z + chunkRenderDistance] = chunk;

//        System.out.println(Arrays.deepToString(chunkArray));
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
        //System.out.println(x + " " + y + " " + z + " " + chunkX + " " + chunkZ);
        //System.out.println(Arrays.deepToString(chunkArray));
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
}
