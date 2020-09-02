package game.collision;

import org.joml.Vector3f;

import static engine.Chunk.getBlock;
import static game.collision.CollisionMath.floorPos;
import static game.collision.CustomAABB.*;
import static game.collision.CustomBlockBox.*;

public class Collision {
    final private static float gameSpeed = 0.001f;
    public static boolean applyInertia(Vector3f pos, Vector3f inertia, boolean onGround, float width, float height, boolean gravity, boolean sneaking){

        if(gravity && !sneaking) {
            inertia.y -= 50f * gameSpeed; //gravity
        }

        //limit speed
        if (inertia.y <= -70f){
            inertia.y = -70f;
        } else if (inertia.y > 70f){
            inertia.y = 70f;
        }



        pos.x += inertia.x * gameSpeed;
        pos.y += inertia.y * gameSpeed;
        pos.z += inertia.z * gameSpeed;

        onGround = collisionDetect(pos, inertia, width, height);

        //apply friction
        inertia.x += -inertia.x * gameSpeed * 10; // do (10 - 9.5f) for slippery!
        inertia.z += -inertia.z * gameSpeed * 10;

        if (sneaking) {
            inertia.y = 0;
        }

        return onGround;
    }

    //these are class/method caches!! NOT FIELDS!
    private static Vector3f fPos;
    private static boolean onGround;
    private static int x,y,z;
    private static Vector3f oldPos;
    private static Vector3f oldInertia;

    private static boolean collisionDetect(Vector3f pos, Vector3f inertia, float width, float height){
        onGround = false;

        //get the real positions of the blocks
        fPos = floorPos(pos);

        y = 2;
        x = -1;
        z = -1;

        oldPos = new Vector3f(pos);
        oldInertia = new Vector3f(inertia);

        int xMin;
        int xMax;

        switch (inertiaConversion(inertia.x)){
            case -1:
                xMin = -1;
                xMax = 0;
                break;
            case 1:
                xMin = 0;
                xMax = 1;
                break;
            default:
                xMin = 0;
                xMax = 0;
                break;
        }

        for (;xMin <= xMax; xMin++){
            System.out.println(xMin);
        }

        //collide with all blocks in local area
        for (int i = 0; i < 36; i++){
            if (detectBlock(new Vector3f(fPos.x + x, fPos.y + y, fPos.z + z))) {
                onGround = collide((int) fPos.x + x, (int) fPos.y + y, (int) fPos.z + z, pos, inertia, width, height, onGround);
            }
            y--;
            if (y < -1){
                y=2;
                x++;
                if(x > 1){
                    x=-1;
                    z++;
                }
            }
        }

        //final touches to make sure player is not clipping
        if(pos.y < oldPos.y){
            if(pos.x != oldPos.x || pos.z != oldPos.z){
                pos.y = oldPos.y;
                inertia.y = oldInertia.y;
            }
        }

        return onGround;
    }


    private final static float FOOT_ADJUSTMENT_HEIGHT = 0.3f;
    private final static float HEAD_ADJUSTMENT_HEIGHT = 0.6f;
    //this is where actual collision events occur!
    public static boolean collide(int blockPosX, int blockPosY, int blockPosz, Vector3f pos, Vector3f inertia, float width, float height, boolean onGround){

        setAABB(pos.x, pos.y, pos.z, width, height);
        setBlockBox(blockPosX,blockPosY,blockPosz);

        //floor detection
        if (isWithin()) {
            if (BlockBoxGetTop() > AABBGetBottom() && inertia.y < 0 && AABBGetBottom() - BlockBoxGetTop() > -0.15f) {
                //this is the collision debug sphere for terrain
                pos.y = BlockBoxGetTop() + 0.0001f;
                inertia.y = 0;
                onGround = true;
            }
        }
//
//        us.updatePos(pos);
//
//        within = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft() || us.getBottom() > block.getTop() || us.getTop() < block.getBottom() || us.getFront() > block.getBack() || us.getBack() < block.getFront());
//        //head detection
//        if (within) {
//            if (block.getBottom() < us.getTop() && inertia.y >= 0 && us.getTop() - block.getBottom() < 0.15f) {
//                pos.y = block.getBottom() - height - 0.001f;
//                inertia.y = 0;
//            }
//        }
//
//        us.updatePos(pos);
//
//        float averageX = Math.abs(((block.getLeft() + block.getRight())/2f) - pos.x);
//        float averageZ = Math.abs(((block.getFront() + block.getBack())/2f) - pos.z);
//
//        within = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft() || us.getBottom() > block.getTop() || us.getTop() < block.getBottom() || us.getFront() > block.getBack() || us.getBack() < block.getFront());
//        if (within) {
//            if (averageX > averageZ) {
//                //x- detection
//                if (block.getRight() > us.getLeft() && inertia.x < 0) {
//                    pos.x = block.getRight() + width + 0.001f;
//                    inertia.x = 0;
//                }
//                //x+ detection
//                if (block.getLeft() < us.getRight() && inertia.x > 0) {
//                    pos.x = block.getLeft() - width - 0.001f;
//                    inertia.x = 0;
//                }
//            } else {
//                //z- detection
//                if (block.getBack() > us.getFront() && inertia.z < 0) {
//                    pos.z = block.getBack() + width + 0.001f;
//                    inertia.z = 0;
//                }
//                 //z+ detection
//                if (block.getFront() < us.getBack() && inertia.z > 0) {
//                    pos.z = block.getFront() - width - 0.001f;
//                    inertia.z = 0;
//                }
//            }
//        }
        return onGround;
    }

    private static boolean isWithin(){
        return !(AABBGetLeft() > BlockBoxGetRight() || AABBGetRight() < BlockBoxGetLeft() || AABBGetBottom() > BlockBoxGetTop() || AABBGetTop() < BlockBoxGetBottom() || AABBGetFront() > BlockBoxGetBack() || AABBGetBack() < BlockBoxGetFront());
    }

    private static boolean detectBlock(Vector3f flooredPos){
        int currentChunkX = (int)(Math.floor(flooredPos.x / 16f));
        int currentChunkZ = (int)(Math.floor(flooredPos.z / 16f));
        Vector3f realPos = new Vector3f(flooredPos.x - (16*currentChunkX), flooredPos.y, flooredPos.z - (16*currentChunkZ));
        return getBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX, currentChunkZ) != 0;
    }

    //this is used for block placing
//    public static boolean wouldCollide(CustomAABB us, CustomBlockBox block){
//        return !(AABBGetLeft() > BlockBoxGetRight() || AABBGetRight() < BlockBoxGetLeft() || AABBGetBottom() > BlockBoxGetTop() || AABBGetTop() < BlockBoxGetBottom() || AABBGetFront() > BlockBoxGetBack() || AABBGetBack() < BlockBoxGetFront());
//    }

    private static boolean inertiaIsZero(float thisInertia){
        return Math.abs(thisInertia) < 0.0001;
    }

    private static int inertiaConversion(float thisInertia){
        if (thisInertia > 0.0001){
            return 1;
        } else if (thisInertia < -0.0001){
            return -1;
        }
        return 0;
    }
}