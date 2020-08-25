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

    //todo this does not belong in here
    private static float getDistance(float x1, float y1, float z1, float x2, float y2, float z2){
        float x = x1 - x2;
        float y = y1 - y2;
        float z = z1 - z2;
        return (float)Math.hypot(x, Math.hypot(y,z));
    }
}
