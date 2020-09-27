package game.player;

import org.joml.*;

import java.lang.Math;

import static engine.Hud.createSelection;
import static engine.graph.Camera.*;
import static engine.sound.SoundAPI.playSound;
import static game.Crafter.getChunkRenderDistance;
import static game.collision.Collision.applyInertia;
import static game.player.Inventory.getItemInInventorySlot;
import static game.player.Ray.rayCast;


public class Player {
    private static int renderDistance            = getChunkRenderDistance();
    private static Vector3f pos                  = new Vector3f(0,53,0);
    private static int[] blockPos                = {0,50,0};
    private static int[] oldBlockPos             = {0,50,0};
    private static float eyeHeight               = 1.5f;
    private static float collectionHeight        = 0.7f;
    private static Vector3f inertia              = new Vector3f(0,0,0);
    private static float height                  = 1.9f;
    private static float width                   = 0.3f;
    private static int[] currentChunk            = {0,0};
    private static boolean onGround              =  false;
    private static boolean jumpBuffer            = false;
    private static boolean mining                = false;
    private static float mineTimer               = 0;
    private static boolean placing               = false;
    private static float placeTimer              = 0;
    private static Vector3f oldPos               = new Vector3f(0,0,0);
    private static float accelerationMultiplier  = 0.07f;
    private static String name                   = "singleplayer";
    private static boolean sneaking              = false;
    private static Vector3f viewBobbing          = new Vector3f(0,0,0);
    private static int currentInventorySelection = 0;
    private static boolean inventoryOpen         = false;
    private static Vector3f worldSelectionPos    = null;


    public static void setPlayerWorldSelectionPos(Vector3f thePos){
        worldSelectionPos = thePos;
    }

    public static void setPlayerWorldSelectionPos(){
        worldSelectionPos = null;
    }

    public static Vector3f getPlayerWorldSelectionPos(){
        return worldSelectionPos;
    }

    public static void togglePlayerInventory(){
        inventoryOpen = !inventoryOpen;
    }

    public static boolean isPlayerInventoryOpen(){
        return inventoryOpen;
    }

    public static String getPlayerName(){
        return name;
    }

    public static int getPlayerInventorySelection(){
        return currentInventorySelection;
    }

    public static Vector3f getPlayerViewBobbing(){
        return viewBobbing;
    }

    public static void setPlayerMining( boolean isMining){
//        if (mineTimer == 0) {
//            mining = true;
//            mineTimer = 20;
//        }
        mining = isMining;
    }

    public static boolean getPlayerMining(){
        return mining;
    }


    public static void setPlayerPlacing( boolean isPlacing) {
//        if (placeTimer == 0) {
//            placing = true;
//            placeTimer = 20;
//        }
        placing = isPlacing;
    }


    //TODO --- begin wield hand stuff!
    private static final Vector3f wieldHandAnimationPosBaseEmpty = new Vector3f(13, -15, -14f);
    private static final Vector3f wieldHandAnimationPosBaseItem = new Vector3f(13, -15, -14f);

    private static final Vector3f wieldRotationEmptyBegin = new Vector3f((float) Math.toRadians(30f), 0f, (float) Math.toRadians(-10f));
    private static final Vector3f wieldRotationEmptyEnd   = new Vector3f((float) Math.toRadians(40f), (float) Math.toRadians(20f), (float) Math.toRadians(20f));

    private static Vector3f wieldHandAnimationPos = new Vector3f(0, 0, 0);
    private static Vector3f wieldHandAnimationRot = new Vector3f(0, 0, 0);

    private static float diggingAnimation = 0f;


    public static void resetWieldHandSetupTrigger(){
        handSetUp = false;
    }

    public static void testPlayerDiggingAnimation(){

        if (!diggingAnimationGo && handSetUp){
            return;
        }

        if (handSetUp) {
            diggingAnimation += 0.0035f;
        }

        if ((!diggingAnimationBuffer || diggingAnimation >= 1f) && handSetUp){
            diggingAnimationGo = false;
            diggingAnimation = 0f;

            System.out.println("digging animation reset");
            return;
        }

        if(!handSetUp){
            handSetUp = true;
        }

        if (getItemInInventorySlot(getPlayerInventorySelection(),0) == 0) {
            wieldHandAnimationPos.x = (float) (-5f * Math.sin(Math.pow(diggingAnimation, 0.8f) * Math.PI)) + wieldHandAnimationPosBaseEmpty.x;
            wieldHandAnimationPos.y = (float) (5f * Math.sin(diggingAnimation * 2f * Math.PI)) + wieldHandAnimationPosBaseEmpty.y;
            wieldHandAnimationPos.z = wieldHandAnimationPosBaseEmpty.z;
            wieldHandAnimationRot.x = 180f;

            Quaternionf quatBegin = new Quaternionf().rotateXYZ(wieldRotationEmptyBegin.x, wieldRotationEmptyBegin.y, wieldRotationEmptyBegin.z);
            Quaternionf quatEnd = new Quaternionf().rotateXYZ(wieldRotationEmptyEnd.x, wieldRotationEmptyEnd.y, wieldRotationEmptyEnd.z);
            quatEnd = quatBegin.slerp(quatEnd, (float) Math.sin(diggingAnimation * Math.PI));

            wieldHandAnimationRot = quatEnd.getEulerAnglesXYZ(wieldHandAnimationRot);
            wieldHandAnimationRot.x = (float) Math.toDegrees(wieldHandAnimationRot.x);
            wieldHandAnimationRot.y = (float) Math.toDegrees(wieldHandAnimationRot.y);
            wieldHandAnimationRot.z = (float) Math.toDegrees(wieldHandAnimationRot.z);
            wieldHandAnimationRot.x += 180f;

            wieldHandAnimationRot = wieldHandAnimationRot;

        } else {
            wieldHandAnimationPos.x = wieldHandAnimationPosBaseEmpty.x;
            wieldHandAnimationPos.y = wieldHandAnimationPosBaseEmpty.y;
            wieldHandAnimationPos.z = wieldHandAnimationPosBaseEmpty.z;
            wieldHandAnimationRot.x = 180f;

            Vector3f wieldRotation = new Vector3f((float)Math.toRadians(0f), (float)Math.toRadians(45f), (float)Math.toRadians(0f));
            Quaternionf quatBegin = new Quaternionf().rotateXYZ(wieldRotation.x, wieldRotation.y, wieldRotation.z);
            Quaternionf quatEnd = new Quaternionf().rotateXYZ((float) Math.toRadians(90f), (float) Math.toRadians(45f), (float) Math.toRadians(0f));

            quatEnd = quatBegin.slerp(quatEnd, (float) Math.sin(diggingAnimation * Math.PI));

            wieldRotation = quatEnd.getEulerAnglesXYZ(wieldRotation);

            wieldRotation.x = (float) Math.toDegrees(wieldRotation.x);
            wieldRotation.y = (float) Math.toDegrees(wieldRotation.y);
            wieldRotation.z = (float) Math.toDegrees(wieldRotation.z);

            wieldHandAnimationRot = wieldRotation;
        }

    }

    public static Vector3f getWieldHandAnimationPos(){
        return wieldHandAnimationPos;
    }

    public static Vector3f getWieldHandAnimationRot(){
        return wieldHandAnimationRot;
    }


    private static boolean diggingAnimationGo = false;
    private static boolean diggingAnimationBuffer = false;
    private static boolean handSetUp = false;
    public static void startDiggingAnimation(){
        diggingAnimationGo = true;
        diggingAnimationBuffer = true;
    }


    //TODO ----- end hand stuff!


    public static float getPlayerHeight(){
        return height;
    }
    public static float getPlayerWidth(){
        return width;
    }

    public static boolean getPlayerPlacing() {
        return placing;
    }

    public static Vector3f getPlayerPos() {
        return pos;
    }

    public static Vector3f getPlayerPosWithEyeHeight(){
        return new Vector3f(pos.x, pos.y + eyeHeight, pos.z);
    }


    public static Vector3f getPlayerPosWithViewBobbing(){
        return new Vector3f(pos.x, pos.y + eyeHeight, pos.z);
    }

    public static Vector3f getPlayerPosWithCollectionHeight(){
        return new Vector3f(pos.x, pos.y + collectionHeight, pos.z);
    }

    public static void setPlayerPos(Vector3f newPos) {
        pos = newPos;
    }

    public static Vector3f getPlayerInertia(){
        return inertia;
    }

    public static void setPlayerInertia(float x,float y,float z){
        inertia.x = x;
        inertia.y = y;
        inertia.z = z;
    }

    private static Vector3f inertiaBuffer = new Vector3f();

    private static boolean forward = false;
    private static boolean backward = false;
    private static boolean left = false;
    private static boolean right = false;
    private static boolean jump = false;

    public static boolean getPlayerForward(){
        return forward;
    }
    public static boolean getPlayerBackward(){
        return backward;
    }
    public static boolean getPlayerLeft(){
        return left;
    }
    public static boolean getPlayerRight(){
        return right;
    }
    public static boolean getPlayerJump(){
        return jump;
    }
    public static boolean isPlayerSneaking(){
        return sneaking;
    }

    public static void setPlayerForward(boolean isForward){
        forward = isForward;
    }
    public static void setPlayerBackward(boolean isBackward){
        backward = isBackward;
    }
    public static void setPlayerLeft(boolean isLeft){
        left = isLeft;
    }
    public static void setPlayerRight(boolean isRight){
        right = isRight;
    }
    public static void setPlayerJump(boolean isJump){
        jump = isJump;
    }
    public static void setPlayerSneaking(boolean isSneaking){
        sneaking = isSneaking;
    }

    private static boolean playerIsMoving(){
        return forward || backward || left || right;
    }

    private static float movementSpeed = 1.5f;

    public static void setPlayerInertiaBuffer(){
        if (forward){
            float yaw = (float)Math.toRadians(getCameraRotation().y) + (float)Math.PI;
            inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier) * movementSpeed;
            inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier) * movementSpeed;
        }
        if (backward){
            //no mod needed
            float yaw = (float)Math.toRadians(getCameraRotation().y);
            inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier) * movementSpeed;
            inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier) * movementSpeed;
        }

        if (right){
            float yaw = (float)Math.toRadians(getCameraRotation().y) - (float)(Math.PI /2);
            inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier) * movementSpeed;
            inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier) * movementSpeed;
        }

        if (left){
            float yaw = (float)Math.toRadians(getCameraRotation().y) + (float)(Math.PI /2);
            inertia.x += (float)(Math.sin(-yaw) * accelerationMultiplier) * movementSpeed;
            inertia.z += (float)(Math.cos(yaw)  * accelerationMultiplier) * movementSpeed;
        }

//
//        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
//            //cameraInc.y = -1;
//        }
        if (jump && isPlayerOnGround()){
            inertia.y += 10.5f;
        }
    }

    private static void applyPlayerInertiaBuffer(){
        setPlayerInertiaBuffer();

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



    public static Boolean isPlayerOnGround(){
        return onGround;
    }


    public static void playerOnTick() {

        applyPlayerInertiaBuffer();

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


        onGround = applyInertia(pos, inertia, true, width, height,true, sneaking, true);

        //map boundary check TODO: ID 1000
        if (pos.x > ((getChunkRenderDistance() + 1) * 16)-0.5f) {
            pos.x = oldPos.x;
        }
        if (pos.x < (getChunkRenderDistance() * -16) + 0.5f){
            pos.x = oldPos.x;
        }
        if (pos.z > ((getChunkRenderDistance() + 1) * 16)-0.5f) {
            pos.z = oldPos.z;
        }
        if (pos.z < (getChunkRenderDistance() * -16) + 0.5f){
            pos.z = oldPos.z;
        }
        //END TODO: ID 1000


        if(mining && mineTimer <= 0) {
            rayCast(getCameraPosition(), getCameraRotationVector(), 4f,  true, false);
            mineTimer = 0.5f;
        } else if (placing && placeTimer <= 0){
            rayCast(getCameraPosition(), getCameraRotationVector(), 4f,  false, true);
            placeTimer = 0.5f;
        } else {
            rayCast(getCameraPosition(), getCameraRotationVector(), 4f,  false, false);
        }

        oldPos = new Vector3f(pos);

        if(playerIsMoving()){
            applyViewBobbing();
        } else {
            returnPlayerViewBobbing();
        }

//        blockPos = new int[]{(int)Math.floor(pos.x), (int)Math.floor(pos.y),(int)Math.floor(pos.z)};

//        if(blockPos[0] != oldBlockPos[0] || blockPos[1] != oldBlockPos[1] || blockPos[2] != oldBlockPos[2]){
//
//            floodFillTest(blockPos[0], blockPos[1], blockPos[2], gameItems, chunkNames);
//        }

//        oldBlockPos = blockPos.clone();
    }
    private static boolean xPositive = true;
    private static boolean yPositive = true;
    private static int xBobPos = 0;
    private static int yBobPos = 0;

    private static void applyViewBobbing() {
        if (xPositive) {
            xBobPos += 1;
            if (xBobPos >= 200){
                xPositive = false;
                yPositive = false;
                playSound("dirt_" + (int)(Math.ceil(Math.random()*3)), pos);
            }
        } else {
            xBobPos -= 1;
            if (xBobPos <= -200){
                xPositive = true;
                yPositive = false;
                playSound("dirt_"  + (int)(Math.ceil(Math.random()*3)), pos);
            }
        }

        if(xBobPos == 0){
            yPositive = true;
        }

        if (yPositive){
            yBobPos += 1;
        } else {
            yBobPos -= 1;
        }

        if (yBobPos < 0){
            yBobPos = 0;
        }

        viewBobbing.x = xBobPos/2000f;
        viewBobbing.y = yBobPos/2000f;
    }

    private static void returnPlayerViewBobbing(){
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

    public static void changeScrollSelection(int i){
        currentInventorySelection += i;
        if (currentInventorySelection < 0) {
            currentInventorySelection = 8;
        }
        if (currentInventorySelection > 8) {
            currentInventorySelection = 0;
        }

        createSelection(currentInventorySelection);

        resetWieldHandSetupTrigger();
    }

    public static int getCurrentInventorySelection(){
        return currentInventorySelection;
    }
}
