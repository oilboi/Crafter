package game.chunk;

import java.util.HashMap;
import java.util.Map;

import static game.chunk.ChunkMesh.generateChunkMesh;

public class ChunkUpdateHandler {

    private static final Map<String, ChunkUpdate> queue = new HashMap<>();

    public static void chunkUpdate( int x, int z , int y){
        String keyName = x + " " + z + " " + y;
        queue.put(keyName, new ChunkUpdate(x,z,y));
    }

    public static void chunkUpdater() {
        for (ChunkUpdate thisUpdate : queue.values()) {
            generateChunkMesh(thisUpdate.x, thisUpdate.z, thisUpdate.y);
            queue.remove(thisUpdate.key);
            return;
        }
    }
}
