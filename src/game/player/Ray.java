package game.player;

import org.joml.Vector3f;

import static game.chunk.Chunk.*;
import static game.blocks.BlockDefinition.*;
import static game.collision.Collision.wouldCollidePlacing;
import static game.collision.CustomAABB.setAABB;
import static game.collision.CustomBlockBox.setBlockBox;
import static game.player.Inventory.getItemInInventorySlot;
import static game.player.Inventory.removeItemFromInventory;
import static game.player.Player.*;

public class Ray {
    public static void rayCast(Vector3f pos, Vector3f dir, float length, boolean mining, boolean placing) {
        Vector3f finalPos = null;
        Vector3f newPos   = null;
        Vector3f lastPos  = null;
        Vector3f cachePos = null;

        for(float step = 0; step <= length ; step += 0.01f) {
            cachePos = new Vector3f(dir.x * step, dir.y * step, dir.z * step);
            newPos = new Vector3f((float)Math.floor(pos.x + cachePos.x), (float)Math.floor(pos.y + cachePos.y), (float)Math.floor(pos.z + cachePos.z));

            if (getBlock((int)newPos.x, (int)newPos.y, (int)newPos.z) != 0){
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

                if (!wouldCollidePlacing() && getItemInInventorySlot(getPlayerInventorySelection(),0) != null && !getItemInInventorySlot(getPlayerInventorySelection(),0).definition.isTool) {
                    placeBlock(lastPos, getItemInInventorySlot(getPlayerInventorySelection(),0).definition.blockID);
                }
            }
            else {
                setPlayerWorldSelectionPos(finalPos); //position
            }
        }
        else {
            setPlayerWorldSelectionPos(); //null
        }
    }

    private static void destroyBlock(Vector3f flooredPos) {
        int thisBlock = getBlock((int)flooredPos.x, (int)flooredPos.y, (int)flooredPos.z);
        setBlock((int)flooredPos.x, (int)flooredPos.y, (int)flooredPos.z, 0);
        onDigCall(thisBlock, flooredPos);
    }
    private static void placeBlock(Vector3f flooredPos, int ID) {
        setBlock((int)flooredPos.x, (int)flooredPos.y, (int)flooredPos.z, ID);
        onPlaceCall(ID, flooredPos);
        removeItemFromInventory(getCurrentInventorySelection(), 0);
    }
}
