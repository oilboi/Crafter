package game.chunk;

import java.util.HashMap;
import java.util.Map;

import static game.chunk.ChunkMesh.generateChunkMesh;
import static game.Crafter.getChunkRenderDistance;

public class ChunkUpdateHandler {

    private static final Map<String, ChunkUpdate> queue = new HashMap<>();

    public static void chunkUpdate( int x, int z , int y){
        String keyName = x + " " + z + " " + y;
        ChunkUpdate thisUpdate = queue.get(keyName);
        if (thisUpdate == null){
            queue.put(keyName, new ChunkUpdate(x,z,y));
        } else {
            thisUpdate.timer = 0f; //reset timer
        }
    }

    public static void chunkUpdater() {
//        Map<Integer, String> deletionQueue = new HashMap<>();
//        int queueAmount = 0;
        for (ChunkUpdate thisUpdate : queue.values()) {
            generateChunkMesh(thisUpdate.x, thisUpdate.z,thisUpdate.y);
            queue.remove(thisUpdate.key);
            return;
//            deletionQueue.put(queueAmount, thisUpdate.key);
//            queueAmount++;
//            if (queueAmount >= 2) {
//                break;
//            }
        }

//        for (String thisKey : deletionQueue.values()) {
//            queue.remove(thisKey);
//        }
    }
}
