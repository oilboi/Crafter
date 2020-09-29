package game.chunk;

import static game.chunk.ChunkMesh.generateChunkMesh;
import static game.Crafter.getChunkRenderDistance;

public class ChunkUpdateHandler {
    private static int[][] queue = new int[0][2];
    private static int[] timers  = new int[0];

    public static void chunkUpdate( int x, int z ){
//        System.out.println("Chunk update at: " + x + " " + z);
        boolean found = false;
        for (int i = 0; i < queue.length; i++){
            if (queue[i][0] == x && queue[i][1] == z) {
                found = true;
                timers[i] = 0;
            }
        }
        if (!found){
            addToQueue(x,z);
        }
    }

    private static void addToQueue(int x, int z){
        int[][] newQueue = new int[queue.length + 1][2];
        int[]  newTimers = new int[timers.length + 1];
        for (int i = 0; i < queue.length; i++){
            newQueue[i] = queue[i].clone();
            newTimers[i] = timers[i];
        }
        queue = newQueue.clone();
        timers = newTimers.clone();
        queue[queue.length-1] = new int[]{x,z};
        timers[timers.length-1] = 0;
    }

    private static void removeFromQueue(int z){
        int[][] newQueue = new int[queue.length - 1][2];
        int[]  newTimers = new int[timers.length - 1];
        for (int i = 0; i < queue.length-1; i++){
            newQueue[i] = queue[i+1].clone();
            newTimers[i] = timers[i+1];
        }
        queue = newQueue.clone();
        timers = newTimers.clone();

//        System.out.println("A queue item was deleted: " + Arrays.deepToString(queue));
    }

    public static void chunkUpdater() throws Exception {
        int oldQueueLength = queue.length;
        for (int i = 0; i < queue.length; i++){
            timers[i] += 1;
            if(timers[i] > 50){
//                System.out.println("Generating chunk mesh: " + (queue[i][0]-chunkRenderDistance) + " " + (queue[i][1]-chunkRenderDistance));
                generateChunkMesh(queue[i][0]-getChunkRenderDistance(), queue[i][1]-getChunkRenderDistance());
                removeFromQueue(i);
                return;
            }
        }

//        if (queue.length == 0 && oldQueueLength > 0) {
//            System.out.println("------------------------------queue cleared");
//        }
    }
}
