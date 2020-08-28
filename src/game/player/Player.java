package game.player;

import engine.ChunkObject;
import engine.graph.Camera;
import org.joml.Vector3f;


import java.util.ArrayList;

import static game.ChunkHandling.ChunkData.getBlockInChunk;
import static game.Crafter.chunkRenderDistance;
import static game.Crafter.getChunkRenderDistance;
import static game.collision.Collision.applyInertia;
import static game.player.Ray.rayCast;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class Player {
    private static int renderDistance = getChunkRenderDistance();
    private Vector3f pos = new Vector3f(0,129,0);

    private int[] blockPos = {0,129,0};
    private int[] oldBlockPos = {0,129,0};


    private float eyeHeight = 1.5f;
    private Vector3f inertia = new Vector3f(0,0,0);
    private float height = 1.9f;
    private float width = 0.3f;

    private int[] currentChunk = {0,0};

    private boolean onGround =  false;
    private boolean jumpBuffer = false;
    private boolean mining = false;
    private float mineTimer = 0;
    private boolean placing = false;
    private float placeTimer = 0;
    private short selectedItem = 1;
    private Vector3f oldPos;
    private float accelerationMultiplier = 0.07f;

    public short getSelectedItem(){
        return selectedItem;
    }

    public void setSelectedItem(short newItem){
        selectedItem = newItem;
    }

    public void setMining( boolean mining){
//        if (mineTimer == 0) {
//            mining = true;
//            mineTimer = 20;
//        }
        this.mining = mining;
    }

    public boolean getMining(){
        return mining;
    }


    public void setPlacing( boolean placing) {
//        if (placeTimer == 0) {
//            placing = true;
//            placeTimer = 20;
//        }

        this.placing = placing;
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

    private Vector3f inertiaBuffer = new Vector3f();

    private boolean forward = false;
    private boolean backward = false;
    private boolean left = false;
    private boolean right = false;
    private boolean jump = false;

    public boolean getForward(){
        return forward;
    }
    public boolean getBackward(){
        return backward;
    }
    public boolean getLeft(){
        return left;
    }
    public boolean getRight(){
        return right;
    }
    public boolean getJump(){
        return jump;
    }

    public void setForward(){
        forward = true;
    }
    public void setBackward(){
        backward = true;
    }
    public void setLeft(){
        left = true;
    }
    public void setRight(){
        right = true;
    }
    public void setJump(){
        jump = true;
    }

    private void clearInputBuffer(){
        this.forward = false;
        this.backward = false;
        this.left = false;
        this.right = false;
        this.jump = false;
    }

    public void setInertiaBuffer(Camera camera){
        if (this.forward){
            float yaw = (float)Math.toRadians(camera.getRotation().y) + (float)Math.PI;
            this.inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier);
            this.inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier);

            this.forward = false;
        }
        if (this.backward){
            //no mod needed
            float yaw = (float)Math.toRadians(camera.getRotation().y);
            this.inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier);
            this.inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier);

            this.backward = false;
        }

        if (this.right){
            float yaw = (float)Math.toRadians(camera.getRotation().y) - (float)(Math.PI /2);
            this.inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier);
            this.inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier);

            this.right = false;
        }

        if (this.left){
            float yaw = (float)Math.toRadians(camera.getRotation().y) + (float)(Math.PI /2);
            this.inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier);
            this.inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier);

            this.left = false;
        }

//
//        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
//            //cameraInc.y = -1;
//        }
        if (this.jump && this.isOnGround()){
            inertia.y += 12f;

            this.jump = false;
        }
    }

    private void applyInertiaBuffer(Camera camera){
        setInertiaBuffer(camera);

        inertia.x += inertiaBuffer.x;
        inertia.y += inertiaBuffer.y;
        inertia.z += inertiaBuffer.z;

        //max speed todo: make this call from a player object's maxSpeed!
        Vector3f inertia2D = new Vector3f(inertia.x, 0, inertia.z);
        if(inertia2D.length() > 5){
            inertia2D = inertia2D.normalize().mul(5);
            inertia.x = inertia2D.x;
            inertia.z = inertia2D.z;
        }

        inertiaBuffer.x = 0f;
        inertiaBuffer.y = 0f;
        inertiaBuffer.z = 0f;
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

    public void onTick(Camera camera, ChunkObject[][] chunkObjects) throws Exception {

        this.applyInertiaBuffer(camera);

        if(placeTimer > 0){
            placeTimer -= 0.003f;
            if (placeTimer < 0.1){
                placeTimer = 0;
            }
        }

        if(mineTimer > 0){
            mineTimer -= 0.003f;
            if (mineTimer < 0.1){
                mineTimer = 0;
            }
        }

        onGround = applyInertia(pos, inertia, onGround, width, height,true);

        //map boundary check TODO: ID 1000
        if (this.pos.x > ((chunkRenderDistance + 1) * 16)-0.5f) {
            pos.x = oldPos.x;
        }
        if (this.pos.x < (chunkRenderDistance * -16) + 0.5f){
            pos.x = oldPos.x;
        }
        if (this.pos.z > ((chunkRenderDistance + 1) * 16)-0.5f) {
            pos.z = oldPos.z;
        }
        if (this.pos.z < (chunkRenderDistance * -16) + 0.5f){
            pos.z = oldPos.z;
        }
        //END TODO: ID 1000

        if(mining && mineTimer <= 0) {
            rayCast(camera.getPosition(), camera.getRotationVector(), 4f, chunkObjects, true, false, this);
            mineTimer = 0.5f;
        } else if (placing && placeTimer <= 0){
            rayCast(camera.getPosition(), camera.getRotationVector(), 4f, chunkObjects, false, true, this);
            placeTimer = 0.5f;
        }




        oldPos = new Vector3f(pos);
//        blockPos = new int[]{(int)Math.floor(pos.x), (int)Math.floor(pos.y),(int)Math.floor(pos.z)};
//
//        if(blockPos[0] != oldBlockPos[0] || blockPos[1] != oldBlockPos[1] || blockPos[2] != oldBlockPos[2]){
//
//            floodFillTest(blockPos[0], blockPos[1], blockPos[2], gameItems, chunkNames);
//        }
//
//        oldBlockPos = blockPos.clone();

        clearInputBuffer();
    }
}
