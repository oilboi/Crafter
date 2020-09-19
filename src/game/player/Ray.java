package game.player;

import engine.sound.SoundManager;
import org.joml.Vector3f;

import static engine.Chunk.*;
import static engine.ItemEntity.createItem;
import static game.blocks.BlockDefinition.*;
import static game.collision.Collision.wouldCollidePlacing;
import static game.collision.CustomAABB.setAABB;
import static game.collision.CustomBlockBox.setBlockBox;
import static game.player.Player.*;

public class Ray {
    public static void rayCast(Vector3f pos, Vector3f dir, float length, boolean mining, boolean placing, boolean debugTest, SoundManager soundMgr) throws Exception {

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

                setAABB(getPlayerPos().x, getPlayerPos().y, getPlayerPos().z, getPlayerWidth(), getPlayerHeight());

                setBlockBox((int)lastPos.x,(int)lastPos.y,(int)lastPos.z, getBlockShape(1)[0]);

                if (!wouldCollidePlacing()) {
                    placeBlock(lastPos, getCurrentInventorySelection()+11);
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

        System.out.println(getRotation((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX,currentChunkZ));

        int thisBlock = getBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX,currentChunkZ);

//        if (thisBlock == 5){
//            return;
//        }

        setBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX, currentChunkZ, (short) 0);

        onDigCall(thisBlock, flooredPos);
    }
    private static void placeBlock(Vector3f flooredPos, int ID) throws Exception {
        int currentChunkX = (int)(Math.floor(flooredPos.x / 16f));
        int currentChunkZ = (int)(Math.floor(flooredPos.z / 16f));
        int chunkPosX = (int)flooredPos.x - (16*currentChunkX);
        int chunkPosZ = (int)flooredPos.z - (16*currentChunkZ);

        Vector3f realPos = new Vector3f(chunkPosX, flooredPos.y, chunkPosZ);

        setBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX, currentChunkZ, ID);

        onPlaceCall(ID, flooredPos);
    }
}
