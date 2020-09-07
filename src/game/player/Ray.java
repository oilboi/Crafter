package game.player;

import engine.sound.SoundManager;
import game.Crafter;
import org.joml.Vector3f;

import static engine.Chunk.getBlock;
import static engine.Chunk.setBlock;
import static engine.ItemEntity.createItem;
import static engine.sound.SoundAPI.playSound;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.Crafter.chunkRenderDistance;
import static game.blocks.BlockDefinition.onDigCall;
import static game.blocks.BlockDefinition.onPlaceCall;
import static game.collision.Collision.wouldCollidePlacing;
import static game.collision.CustomAABB.setAABB;
import static game.collision.CustomBlockBox.setBlockBox;
//import static game.light.Light.floodFill;

public class Ray {
    public static void rayCast(Vector3f pos, Vector3f dir, float length, boolean mining, boolean placing, Player player, boolean debugTest, SoundManager soundMgr) throws Exception {

        Vector3f finalPos = null;
        Vector3f newPos   = null;
        Vector3f lastPos  = null;
        Vector3f cachePos = null;

        for(float step = 0; step <= length ; step += 0.01f) {
            cachePos = new Vector3f(dir.x * step, dir.y * step, dir.z * step);
            newPos = new Vector3f((float)Math.floor(pos.x + cachePos.x), (float)Math.floor(pos.y + cachePos.y), (float)Math.floor(pos.z + cachePos.z));

            if (detectBlock(newPos)){
                finalPos = newPos;
                break;
            }
            lastPos = new Vector3f(newPos);
        }

        if(finalPos != null) {
            if(mining) {
                destroyBlock(finalPos);
            } else if (placing && lastPos != null){

                setAABB(player.getPos().x, player.getPos().y, player.getPos().z, player.getWidth(), player.getHeight());

                setBlockBox((int)lastPos.x,(int)lastPos.y,(int)lastPos.z);

                if (!wouldCollidePlacing()) {
                    placeBlock(lastPos, (short) 6)/*Math.floor(8+(Math.random() * 8)))*/;
                }
            }
        } else if (debugTest){
            createItem(2, lastPos);
        }
    }

    private static boolean detectBlock(Vector3f flooredPos){
        int[] current = new int[2];

        current[0] = (int)(Math.floor(flooredPos.x / 16f));
        current[1] = (int)(Math.floor(flooredPos.z / 16f));

        Vector3f realPos = new Vector3f(flooredPos.x - (16*current[0]), flooredPos.y, flooredPos.z - (16*current[1]));

        return getBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, current[0], current[1]) != 0;
    }

    private static void destroyBlock(Vector3f flooredPos) throws Exception {

        int currentChunkX = (int)(Math.floor(flooredPos.x / 16f));
        int currentChunkZ = (int)(Math.floor(flooredPos.z / 16f));


        int chunkPosX = (int)flooredPos.x - (16*currentChunkX);
        int chunkPosZ = (int)flooredPos.z - (16*currentChunkZ);

        Vector3f realPos = new Vector3f(chunkPosX, flooredPos.y, chunkPosZ);

        int thisBlock = getBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX,currentChunkZ);

        if (thisBlock == 5){
            return;
        }

        setBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX, currentChunkZ, (short) 0);

//        floodFill(currentChunkX, currentChunkZ);
//
//        generateChunkMesh(currentChunkX, currentChunkZ, true);
//
//        if (chunkPosX == 15 && currentChunkX < chunkRenderDistance){ //update neighbor
////            floodFill(currentChunkX+1, currentChunkZ);
//            generateChunkMesh(currentChunkX+1, currentChunkZ, true);
//        }
//        if (chunkPosX == 0 && currentChunkX > -chunkRenderDistance){
////            floodFill(currentChunkX-1, currentChunkZ);
//            generateChunkMesh(currentChunkX-1, currentChunkZ, true);
//        }
//        if (chunkPosZ == 15&& currentChunkZ < chunkRenderDistance){
////            floodFill(currentChunkX, currentChunkZ+1);
//            generateChunkMesh(currentChunkX, currentChunkZ+1, true);
//        }
//        if (chunkPosZ == 0 && currentChunkZ > -chunkRenderDistance){
////            floodFill(currentChunkX, currentChunkZ-1);
//            generateChunkMesh(currentChunkX, currentChunkZ-1, true);
//        }

        onDigCall(thisBlock, flooredPos);
    }
    private static void placeBlock(Vector3f flooredPos, short ID) throws Exception {
        int currentChunkX = (int)(Math.floor(flooredPos.x / 16f));
        int currentChunkZ = (int)(Math.floor(flooredPos.z / 16f));
        int chunkPosX = (int)flooredPos.x - (16*currentChunkX);
        int chunkPosZ = (int)flooredPos.z - (16*currentChunkZ);

        Vector3f realPos = new Vector3f(chunkPosX, flooredPos.y, chunkPosZ);

        setBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX, currentChunkZ, ID);

//        floodFill(currentChunkX, currentChunkZ);

//        generateChunkMesh(currentChunkX, currentChunkZ, true);


//        if (chunkPosX == 15){ //update neighbor
////            floodFill(currentChunkX+1, currentChunkZ);
//            generateChunkMesh(currentChunkX+1, currentChunkZ, true);
//        }
//        if (chunkPosX == 0){
////            floodFill(currentChunkX-1, currentChunkZ);
//            generateChunkMesh(currentChunkX-1, currentChunkZ, true);
//        }
//        if (chunkPosZ == 15){
////            floodFill(currentChunkX, currentChunkZ+1);
//            generateChunkMesh(currentChunkX, currentChunkZ+1, true);
//        }
//        if (chunkPosZ == 0){
////            floodFill(currentChunkX, currentChunkZ-1);
//            generateChunkMesh(currentChunkX, currentChunkZ-1, true);
//        }

        onPlaceCall(ID, flooredPos);
    }
}
