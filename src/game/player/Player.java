package game.player;

import engine.graph.Camera;
import org.joml.Vector3f;

import java.util.ArrayList;

import static game.Crafter.chunkRenderDistance;
import static game.Crafter.getChunkRenderDistance;
import static game.collision.Collision.applyInertia;
import static game.player.Ray.rayCast;


public class Player {
    private static ArrayList<Player> playerList = new ArrayList();

    private static int renderDistance = getChunkRenderDistance();
    private Vector3f pos = new Vector3f(0,129,0);

    private int[] blockPos = {0,129,0};
    private int[] oldBlockPos = {0,129,0};


    private float eyeHeight = 1.5f;
    private float collectionHeight = 0.7f;
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
    private String name;
    private boolean sneaking;
    private Vector3f viewBobbing = new Vector3f(0,0,0);

    public Player(String name){
        this.name = name;
        playerList.add(this);
    }

    public String getName(){
        return this.name;
    }

    public static Player getPlayer(String name){
        for (Player thisPlayer : playerList){
            if (thisPlayer.getName().equals(name)){
                return thisPlayer;
            }
        }
        return null;
    }

    public Vector3f getViewBobbing(){
        return this.viewBobbing;
    }

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


    public Vector3f getPosWithViewBobbing(){
        return new Vector3f(pos.x, pos.y + eyeHeight, pos.z);
    }

    public Vector3f getPosWithCollectionHeight(){
        return new Vector3f(pos.x, pos.y + collectionHeight, pos.z);
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
    public boolean isSneaking(){
        return sneaking;
    }

    public void setForward(boolean isForward){
        forward = isForward;
    }
    public void setBackward(boolean isBackward){
        backward = isBackward;
    }
    public void setLeft(boolean isLeft){
        left = isLeft;
    }
    public void setRight(boolean isRight){
        right = isRight;
    }
    public void setJump(boolean isJump){
        jump = isJump;
    }
    public void setSneaking(boolean isSneaking){
        sneaking = isSneaking;
    }

    private boolean playerIsMoving(){
        return forward || backward || left || right;
    }

    private float movementSpeed = 1.5f;

    public void setInertiaBuffer(Camera camera){
        if (this.forward){
            float yaw = (float)Math.toRadians(camera.getRotation().y) + (float)Math.PI;
            this.inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier) * movementSpeed;
            this.inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier) * movementSpeed;
        }
        if (this.backward){
            //no mod needed
            float yaw = (float)Math.toRadians(camera.getRotation().y);
            this.inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier) * movementSpeed;
            this.inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier) * movementSpeed;
        }

        if (this.right){
            float yaw = (float)Math.toRadians(camera.getRotation().y) - (float)(Math.PI /2);
            this.inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier) * movementSpeed;
            this.inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier) * movementSpeed;
        }

        if (this.left){
            float yaw = (float)Math.toRadians(camera.getRotation().y) + (float)(Math.PI /2);
            this.inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier) * movementSpeed;
            this.inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier) * movementSpeed;
        }

//
//        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
//            //cameraInc.y = -1;
//        }
        if (this.jump && this.isOnGround()){
            inertia.y += 12f;
        }
    }

    private void applyInertiaBuffer(Camera camera){
        setInertiaBuffer(camera);

        inertia.x += inertiaBuffer.x;
        inertia.y += inertiaBuffer.y;
        inertia.z += inertiaBuffer.z;

        //max speed todo: make this call from a player object's maxSpeed!
        Vector3f inertia2D = new Vector3f(inertia.x, 0, inertia.z);

        float maxSpeed = 5f;
        if(sneaking){
            maxSpeed = 1f;
        }
        if(inertia2D.length() > maxSpeed){
            inertia2D = inertia2D.normalize().mul(maxSpeed);
            inertia.x = inertia2D.x;
            inertia.z = inertia2D.z;
        }

        inertiaBuffer.x = 0f;
        inertiaBuffer.y = 0f;
        inertiaBuffer.z = 0f;
    }



    public Boolean isOnGround(){
        return onGround;
    }


    public void onTick(Camera camera) throws Exception {

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


        onGround = applyInertia(pos, inertia, true, width, height,true, sneaking);

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
            rayCast(camera.getPosition(), camera.getRotationVector(), 4f, true, false, this, false);
            mineTimer = 0.5f;
        } else if (placing && placeTimer <= 0){
            rayCast(camera.getPosition(), camera.getRotationVector(), 4f, false, true, this, false);
            placeTimer = 0.5f;
        } else if (mining) {
            rayCast(camera.getPosition(), camera.getRotationVector(), 4f, false, false, this, true);
        }

        oldPos = new Vector3f(pos);

        if(playerIsMoving()){
            applyViewBobbing();
        } else {
            returnViewBobbing();
        }

//        blockPos = new int[]{(int)Math.floor(pos.x), (int)Math.floor(pos.y),(int)Math.floor(pos.z)};
//
//        if(blockPos[0] != oldBlockPos[0] || blockPos[1] != oldBlockPos[1] || blockPos[2] != oldBlockPos[2]){
//
//            floodFillTest(blockPos[0], blockPos[1], blockPos[2], gameItems, chunkNames);
//        }
//
//        oldBlockPos = blockPos.clone();
    }
    private boolean xPositive = true;
    private boolean yPositive = true;
    private int xBobPos = 0;
    private int yBobPos = 0;

    private void applyViewBobbing(){
        if (xPositive) {
            xBobPos += 1;
            if (xBobPos >= 150){
                xPositive = !xPositive;
                yPositive = !yPositive;
            }
        } else {
            xBobPos -= 1;
            if (xBobPos <= -150){
                xPositive = !xPositive;
                yPositive = !yPositive;
            }
        }

        if(xBobPos == 0){
            yPositive = !yPositive;
        }

        if (yPositive){
            yBobPos += 1;
        } else {
            yBobPos -= 1;
        }

        viewBobbing.x = xBobPos/2000f;
        viewBobbing.y = yBobPos/2000f;
    }

    private void returnViewBobbing(){
        if (xBobPos > 0){
            xBobPos -= 1;
        } else if (xBobPos < 0){
            xBobPos += 1;
        }
        if (yBobPos > 0){
            yBobPos -= 1;
        } else if (yBobPos < 0){
            yBobPos += 1;
        }
        viewBobbing.x = xBobPos/2000f;
        viewBobbing.y = yBobPos/2000f;
    }
}
