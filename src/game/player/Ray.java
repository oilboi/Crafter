package game.player;

import engine.GameItem;
import game.collision.CustomAABB;
import game.collision.CustomBlockBox;
import org.joml.Vector3f;

import static game.ChunkHandling.ChunkData.*;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.Crafter.getChunkRenderDistance;
import static game.collision.Collision.wouldCollide;

public class Ray {
    public static void rayCast(Vector3f pos, Vector3f dir, float length, GameItem[] gameItems, String[] chunkNames, boolean mining, boolean placing, Player player) throws Exception {

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

        //System.out.println(finalPos);
        if(finalPos != null) {
            if(mining) {
                destroyBlock(finalPos, gameItems, chunkNames);
            } else if (placing && lastPos != null){
                if (!wouldCollide(new CustomAABB(player.getPos().x, player.getPos().y+0.01f, player.getPos().z, player.getWidth(), player.getHeight()-0.02f), new CustomBlockBox((int)lastPos.x, (int)lastPos.y, (int)lastPos.z))) {
                    placeBlock(lastPos, gameItems, chunkNames, (short) 1);
                }
            }
        } else {
//            rootNode.getChild("selector").setLocalTranslation(0, -1000f, 0);
        }
    }

    private static boolean detectBlock(Vector3f flooredPos){
        int[] current = new int[2];

        current[0] = (int)(Math.floor(flooredPos.x / 16f));
        current[1] = (int)(Math.floor(flooredPos.z / 16f));

        Vector3f realPos = new Vector3f(flooredPos.x - (16*current[0]), flooredPos.y, flooredPos.z - (16*current[1]));

        return getBlockInChunk((int)realPos.x, (int)realPos.y, (int)realPos.z, current[0], current[1]) != 0;
    }

    private static short destroyBlock(Vector3f flooredPos, GameItem[] gameItems, String[] chunkNames) throws Exception {
        int currentChunkX = (int)(Math.floor(flooredPos.x / 16f));
        int currentChunkZ = (int)(Math.floor(flooredPos.z / 16f));
        int chunkPosX = (int)flooredPos.x - (16*currentChunkX);
        int chunkPosZ = (int)flooredPos.z - (16*currentChunkZ);

        Vector3f realPos = new Vector3f(chunkPosX, flooredPos.y, chunkPosZ);

//        short thisBlock = getBlockInChunk((int)realPos.x, (int)realPos.y, (int)realPos.z, current[0], current[1]);
        setBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX, currentChunkZ, (short) 0);
        generateChunkMesh(getChunk(currentChunkX,currentChunkZ), currentChunkX, currentChunkZ, gameItems, chunkNames, true);


        if (chunkPosX == 15){ //update neighbor
            generateChunkMesh(getChunk(currentChunkX+1,currentChunkZ), currentChunkX+1, currentChunkZ, gameItems, chunkNames, true);
        }
        if (chunkPosX == 0){
            generateChunkMesh(getChunk(currentChunkX-1,currentChunkZ), currentChunkX-1, currentChunkZ, gameItems, chunkNames, true);
        }
        if (chunkPosZ == 15){
            generateChunkMesh(getChunk(currentChunkX,currentChunkZ+1), currentChunkX, currentChunkZ+1, gameItems, chunkNames, true);
        }
        if (chunkPosZ == 0){
            generateChunkMesh(getChunk(currentChunkX,currentChunkZ-1), currentChunkX, currentChunkZ-1, gameItems, chunkNames, true);
        }
        return 0;
    }
    private static short placeBlock(Vector3f flooredPos, GameItem[] gameItems, String[] chunkNames, short id) throws Exception {
        int currentChunkX = (int)(Math.floor(flooredPos.x / 16f));
        int currentChunkZ = (int)(Math.floor(flooredPos.z / 16f));
        int chunkPosX = (int)flooredPos.x - (16*currentChunkX);
        int chunkPosZ = (int)flooredPos.z - (16*currentChunkZ);

        Vector3f realPos = new Vector3f(chunkPosX, flooredPos.y, chunkPosZ);

//        short thisBlock = getBlockInChunk((int)realPos.x, (int)realPos.y, (int)realPos.z, current[0], current[1]);
        setBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX, currentChunkZ, (short) id);
        generateChunkMesh(getChunk(currentChunkX,currentChunkZ), currentChunkX, currentChunkZ, gameItems, chunkNames, true);


        if (chunkPosX == 15){ //update neighbor
            generateChunkMesh(getChunk(currentChunkX+1,currentChunkZ), currentChunkX+1, currentChunkZ, gameItems, chunkNames, true);
        }
        if (chunkPosX == 0){
            generateChunkMesh(getChunk(currentChunkX-1,currentChunkZ), currentChunkX-1, currentChunkZ, gameItems, chunkNames, true);
        }
        if (chunkPosZ == 15){
            generateChunkMesh(getChunk(currentChunkX,currentChunkZ+1), currentChunkX, currentChunkZ+1, gameItems, chunkNames, true);
        }
        if (chunkPosZ == 0){
            generateChunkMesh(getChunk(currentChunkX,currentChunkZ-1), currentChunkX, currentChunkZ-1, gameItems, chunkNames, true);
        }
        return 0;
    }
}
