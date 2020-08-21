package game.ChunkHandling;

public class ChunkMath {
    private final static short chunkSizeX = 16;
    private final static short chunkSizeY = 128;
    private final static short chunkSizeZ = 16;

    public static int genHash(int x, int y, int z){
        return((x*chunkSizeY) + y + (z*(chunkSizeX * chunkSizeY)));
    }

    public static int[] getHash(int i) {
        int z = (int)(Math.floor(i/(chunkSizeX * chunkSizeY)));
        i %= (chunkSizeX * chunkSizeY);
        int x = (int)(Math.floor(i/chunkSizeY));
        i %= chunkSizeY;
        int y = (int)(Math.floor(i));
        int[] result = {x,y,z};
        return result;
    }
}
