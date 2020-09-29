package game.chunk;

import java.util.HashMap;
import java.util.Map;

import static game.chunk.ChunkMesh.generateChunkMesh;
import static game.Crafter.getChunkRenderDistance;

public class ChunkUpdateHandler {

    private static Map<String, ChunkUpdate> queue = new HashMap<>();

    public static void chunkUpdate( int x, int z , int y){
        String keyName = x + " " + z + " " + y;

        ChunkUpdate thisUpdate = queue.get(keyName);

        if (thisUpdate == null){
//            System.out.println("adding " + keyName + " to queue");
            queue.put(keyName, new ChunkUpdate(x,z,y));
        } else {
            thisUpdate.timer = 0f; //reset timer
        }
    }

    public static void chunkUpdater() {

        for (ChunkUpdate thisUpdate : queue.values()) {
            thisUpdate.timer += 1;
            //only one update at a time
            if (thisUpdate.timer >= 50) {
//                System.out.println("Generating chunk mesh: " + (thisUpdate.x-getChunkRenderDistance()) +
//                        " " + (thisUpdate.z-getChunkRenderDistance()) +
//                        " " + thisUpdate.y
//                );
                generateChunkMesh(thisUpdate.x-getChunkRenderDistance(), thisUpdate.z-getChunkRenderDistance(),thisUpdate.y);

                queue.remove(thisUpdate.key);
                return;
            }
        }
    }
}
