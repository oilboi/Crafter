package game.player;

import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;

import static game.ChunkHandling.ChunkData.getBlockInChunk;
import static game.Crafter.getChunkRenderDistance;
import static game.collision.Collision.applyInertia;

public class Player {
    private static int renderDistance = getChunkRenderDistance();
    private Vector3f pos = new Vector3f(0,50,0);
    private float eyeHeight = 1.5f;
    private Vector3f inertia = new Vector3f(0,0,0);
    private float height = 1.9f;
    private float width = 0.3f;
    private int[] currentChunk = {0,0};

    private Boolean onGround =  new Boolean(false);
    private boolean jumpBuffer = false;
    private boolean mining = false;
    private float mineTimer = 0;
    private boolean placing = false;
    private float placeTimer = 0;
    private short selectedItem = 1;

    public short getSelectedItem(){
        return selectedItem;
    }

    public void setSelectedItem(short newItem){
        selectedItem = newItem;
    }

    public void setMining(){
        if (mineTimer == 0) {
            mining = true;
            mineTimer = 20;
        }
    }

    public boolean getMining(){
        return mining;
    }


    public void setPlacing() {
        if (placeTimer == 0) {
            placing = true;
            placeTimer = 20;
        }
    }

    public float getHeight(){
        return height;
    }
    public float getWidth(){
        return width;
    }

    public boolean getPlacing() {
        return placing;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getPosWithEyeHeight(){
        return new Vector3f(pos.x, pos.y + eyeHeight, pos.z);
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getInertia(){
        return inertia;
    }

    public void setInertia(float x,float y,float z){
        inertia.x = x;
        inertia.y = y;
        inertia.z = z;
    }

    public void addInertia(float x,float y,float z){
        inertia.x += x;
        inertia.y += y;
        inertia.z += z;

        //max speed todo: make this call from a player object's maxSpeed!
        Vector3f inertia2D = new Vector3f(inertia.x, 0, inertia.z);
        if(inertia2D.length() > 5){
            inertia2D = inertia2D.normalize().mul(5);
            inertia.x = inertia2D.x;
            inertia.z = inertia2D.z;
        }
    }



    public void onTick(){
//        if(jumpBuffer){
//            inertia.y += 12f;
//            jumpBuffer = false;
//        }
//        if(mining){
//            mining = false;
//        }
//        if(placing){
//            placing = false;
//        }
//
//        if(placeTimer > 0){
//            placeTimer -= 0.1f;
//            if (placeTimer < 0.1){
//                placeTimer = 0;
//            }
//        }

//        if(mineTimer > 0){
//            mineTimer -= 0.1f;
//            if (mineTimer < 0.1){
//                mineTimer = 0;
//            }
//        }

        onGround = applyInertia(pos, inertia, onGround, width, height,true);
        
//        int[] current = new int[2];
//        Vector3f flooredPos = pos;
//        flooredPos.x = (float)Math.floor(flooredPos.x);
//        flooredPos.z = (float)Math.floor(flooredPos.z);
//        current[0] = (int)(Math.floor(flooredPos.x / 16f));
//        current[1] = (int)(Math.floor(flooredPos.z / 16f));
//        currentChunk = current;

    }

    private short getBlock(float x, float y, float z){
        Vector3f flooredPos = pos;
        flooredPos.x = (float)Math.floor(flooredPos.x + x);
        flooredPos.y = (float)Math.floor(flooredPos.y + y);
        flooredPos.z = (float)Math.floor(flooredPos.z + z);

        int[] current = new int[2];
        current[0] = (int)(Math.floor(flooredPos.x / 16f));
        current[1] = (int)(Math.floor(flooredPos.z / 16f));

        Vector3f realPos = new Vector3f(flooredPos.x - (16*current[0]), flooredPos.y, flooredPos.z - (16*current[1]));

        return getBlockInChunk((int)realPos.x, (int)realPos.y, (int)realPos.z, current[0]+renderDistance, current[1]+renderDistance);
    }


    public Boolean isOnGround(){
        return onGround;
    }

    public void setJumpBuffer(){
        jumpBuffer = true;
    }
}
