package game.collision;

import org.joml.Vector3f;

import java.util.Arrays;

import static engine.Chunk.getBlock;
import static game.blocks.BlockDefinition.*;
import static game.collision.CollisionMath.floorPos;
import static game.collision.CustomAABB.*;
import static game.collision.CustomBlockBox.*;

public class Collision {
    final private static float gameSpeed = 0.001f;

    public static boolean applyInertia(Vector3f pos, Vector3f inertia, boolean onGround, float width, float height, boolean gravity, boolean sneaking, boolean applyCollision){

        if(gravity && !sneaking) {
            inertia.y -= 40f * gameSpeed; //gravity
        }

        //limit speed
        if (inertia.y <= -70f){
            inertia.y = -70f;
        } else if (inertia.y > 70f){
            inertia.y = 70f;
        }

        if (applyCollision) {
            onGround = collisionDetect(pos, inertia, width, height);
        } else {
            pos.x += inertia.x * gameSpeed;
            pos.y += inertia.y * gameSpeed;
            pos.z += inertia.z * gameSpeed;
        }

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
    private static int cachedBlock;
    private static Vector3f cachedPos = new Vector3f(0,0,0);

    private static boolean collisionDetect(Vector3f pos, Vector3f inertia, float width, float height){
        onGround = false;


        pos.y += inertia.y * gameSpeed;

        fPos = floorPos(new Vector3f(pos));

        int up = 0;

        //todo: begin Y collision detection
        switch (inertiaToDir(inertia.y)){
            case -1:
                y = (int)fPos.y;
                break;
            case 1:
                y = (int)Math.floor(pos.y + height);
                up = 1;
                break;
            default:
                y = 777;
                break;
        }

        if (y != 777) {
            switch (up){
                case 0:
                    for (x = -1; x <= 1; x++) {
                        for (z = -1; z <= 1; z++) {
                            cachedPos.x = fPos.x + x;
                            cachedPos.y = y;
                            cachedPos.z = fPos.z + z;

                            cachedBlock = detectBlock(cachedPos);

//                            if (isWalkable(cachedBlock)) {
//                                System.out.println(Arrays.deepToString(getBlockShape(cachedBlock)));
//                            }

                            if (isWalkable(cachedBlock)) {
                                onGround = collideYNegative((int) fPos.x + x, y, (int) fPos.z + z, pos, inertia, width, height, onGround, cachedBlock);
                            }
                        }
                    }
                    break;
                case 1:
                    for (x = -1; x <= 1; x++) {
                        for (z = -1; z <= 1; z++) {
                            cachedPos.x = fPos.x + x;
                            cachedPos.y = y;
                            cachedPos.z = fPos.z + z;
                            cachedBlock = detectBlock(cachedPos);

                            if (isWalkable(cachedBlock)) {
                                collideYPositive((int) fPos.x + x, y, (int) fPos.z + z, pos, inertia, width, height, cachedBlock);
                            }
                        }
                    }
                    break;
            }
        }


        //todo: begin X collision detection
        pos.x += inertia.x * gameSpeed;

        fPos = floorPos(new Vector3f(pos));

        boolean doIt = true;

        int positive = 0;

        switch (inertiaToDir(inertia.x)){
            case -1:
                x = (int)Math.floor(pos.x - width);
                break;
            case 1:
                x = (int)Math.floor(pos.x + width);
                positive = 1;
                break;
            default:
                doIt = false;
                break;
        }

        if (doIt) {
            switch (positive){
                case 1:
                    for (float yy = 0; yy <= height; yy = yy + 0.5f) {
                        for (z = -1; z <= 1; z++) {
                            cachedPos.x = x;
                            cachedPos.y = yy + pos.y;
                            cachedPos.z = fPos.z + z;

                            cachedBlock = detectBlock(floorPos(cachedPos));

                            if (isWalkable(cachedBlock)) {
                                collideXPositive(x, (int)(yy + pos.y), (int) fPos.z + z, pos, inertia, width, height, cachedBlock);
                            }
                        }
                    }
                    break;
                case 0:
                    for (float yy = 0; yy <= height; yy = yy + 0.5f) {
                        for (z = -1; z <= 1; z++) {
                            cachedPos.x = x;
                            cachedPos.y = yy + pos.y;
                            cachedPos.z = fPos.z + z;

                            cachedBlock = detectBlock(floorPos(cachedPos));

                            if (isWalkable(cachedBlock)) {
                                collideXNegative(x, (int)(yy + pos.y), (int) fPos.z + z, pos, inertia, width, height, cachedBlock);
                            }
                        }
                    }
                    break;
            }
        }


        //todo: Begin Z collision detection

        pos.z += inertia.z * gameSpeed;

        fPos = floorPos(new Vector3f(pos));

        doIt = true;

        positive = 0;

        switch (inertiaToDir(inertia.z)){
            case -1:
                z = (int)Math.floor(pos.z - width);
                break;
            case 1:
                z = (int)Math.floor(pos.z + width);
                positive = 1;
                break;
            default:
                doIt = false;
                break;
        }

        if (doIt) {
            switch (positive){
                case 1:
                    for (float yy = 0; yy <= height; yy = yy + 0.5f) {
                        for (x = -1; x <= 1; x++) {
                            cachedPos.x = fPos.x + x;
                            cachedPos.y = yy + pos.y;
                            cachedPos.z = z;
                            cachedBlock = detectBlock(floorPos(cachedPos));
                            if (isWalkable(cachedBlock)) {
                                collideZPositive((int)fPos.x + x, (int)(yy + pos.y), z, pos, inertia, width, height, cachedBlock);
                            }
                        }
                    }
                    break;
                case 0:
                    for (float yy = 0; yy <= height; yy = yy + 0.5f) {
                        for (x = -1; x <= 1; x++) {
                            cachedPos.x = fPos.x + x;
                            cachedPos.y = yy + pos.y;
                            cachedPos.z = z;
                            cachedBlock = detectBlock(floorPos(cachedPos));
                            if (isWalkable(cachedBlock)) {
                                collideZNegative((int)fPos.x + x, (int)(yy + pos.y), z, pos, inertia, width, height, cachedBlock);
                            }
                        }
                    }
                    break;
            }
        }
        return onGround;
    }

    public static boolean collideYNegative(int blockPosX, int blockPosY, int blockPosz, Vector3f pos, Vector3f inertia, float width, float height, boolean onGround, int blockID){
        for (float[] thisBlockBox : getBlockShape(blockID)) {
            setAABB(pos.x, pos.y, pos.z, width, height);
            setBlockBox(blockPosX,blockPosY,blockPosz,thisBlockBox);
            if (isWithin() && BlockBoxGetTop() > AABBGetBottom() && AABBGetBottom() - BlockBoxGetTop() > -0.1f) {
                pos.y = BlockBoxGetTop() + 0.0001f;
                inertia.y = 0;
                onGround = true;
            }
        }
        return onGround;
    }

    public static void collideYPositive(int blockPosX, int blockPosY, int blockPosz, Vector3f pos, Vector3f inertia, float width, float height, int blockID){
        for (float[] thisBlockBox : getBlockShape(blockID)) {
            setAABB(pos.x, pos.y, pos.z, width, height);
            setBlockBox(blockPosX, blockPosY, blockPosz,thisBlockBox);
            //head detection
            if (isWithin() && BlockBoxGetBottom() < AABBGetTop() && AABBGetTop() - BlockBoxGetBottom() < 0.1f) {
                pos.y = BlockBoxGetBottom() - height - 0.001f;
                inertia.y = 0;
            }
        }
    }

    public static void collideXPositive(int blockPosX, int blockPosY, int blockPosz, Vector3f pos, Vector3f inertia, float width, float height, int blockID){
        float lastY = pos.y;
        for (float[] thisBlockBox : getBlockShape(blockID)) {
            setAABB(pos.x, pos.y, pos.z, width, height);
            setBlockBox(blockPosX, blockPosY, blockPosz, thisBlockBox);
            if (isWithin() && BlockBoxGetLeft() < AABBGetRight() && BlockBoxGetLeft() - AABBGetRight() > -0.1f) {
                if (isSteppable(blockID) && AABBGetBottom() - BlockBoxGetTop() < -0.48f) {
                    pos.y = BlockBoxGetTop() + 0.0001f;
                } else {
                    pos.x = BlockBoxGetLeft() - width - 0.001f;
                    inertia.x = 0;
                }
            }
        }

        //correction for the sides of stairs
        if (pos.y - lastY > 0.51) {
            pos.y = lastY;
            for (float[] thisBlockBox : getBlockShape(blockID)) {
                setAABB(pos.x, pos.y, pos.z, width, height);
                setBlockBox(blockPosX, blockPosY, blockPosz, thisBlockBox);
                if (isWithin() && BlockBoxGetLeft() < AABBGetRight() && BlockBoxGetLeft() - AABBGetRight() > -0.1f) {
                    pos.x = BlockBoxGetLeft() - width - 0.001f;
                    inertia.x = 0;
                }
            }
        }
    }

    public static void collideXNegative(int blockPosX, int blockPosY, int blockPosz, Vector3f pos, Vector3f inertia, float width, float height, int blockID){
        float lastY = pos.y;
        for (float[] thisBlockBox : getBlockShape(blockID)) {
            setAABB(pos.x, pos.y, pos.z, width, height);
            setBlockBox(blockPosX, blockPosY, blockPosz, thisBlockBox);
            if (isWithin() && BlockBoxGetRight() > AABBGetLeft() && BlockBoxGetRight() - AABBGetLeft() < 0.1f) {
                if (isSteppable(blockID) && AABBGetBottom() - BlockBoxGetTop() < -0.48f) {
                    pos.y = BlockBoxGetTop() + 0.0001f;
                }else {
                    pos.x = BlockBoxGetRight() + width + 0.001f;
                    inertia.x = 0;
                }
            }
        }

        //correction for the sides of stairs
        if (pos.y - lastY > 0.51) {
            pos.y = lastY;
            for (float[] thisBlockBox : getBlockShape(blockID)) {
                setAABB(pos.x, pos.y, pos.z, width, height);
                setBlockBox(blockPosX, blockPosY, blockPosz, thisBlockBox);
                if (isWithin() && BlockBoxGetRight() > AABBGetLeft() && BlockBoxGetRight() - AABBGetLeft() < 0.1f) {
                    pos.x = BlockBoxGetRight() + width + 0.001f;
                    inertia.x = 0;
                }
            }
        }
    }


    public static void collideZPositive(int blockPosX, int blockPosY, int blockPosz, Vector3f pos, Vector3f inertia, float width, float height, int blockID){
        float lastY = pos.y;
        for (float[] thisBlockBox : getBlockShape(blockID)) {
            setAABB(pos.x, pos.y, pos.z, width, height);
            setBlockBox(blockPosX, blockPosY, blockPosz, thisBlockBox);
            if (isWithin() && BlockBoxGetFront() < AABBGetBack() && BlockBoxGetFront() - AABBGetBack() > -0.1f) {
                if (isSteppable(blockID) && AABBGetBottom() - BlockBoxGetTop() < -0.48f) {
                    pos.y = BlockBoxGetTop() + 0.0001f;
                }else {
                    pos.z = BlockBoxGetFront() - width - 0.001f;
                    inertia.z = 0;
                }
            }
        }

        //correction for the sides of stairs
        if (pos.y - lastY > 0.51) {
            pos.y = lastY;
            for (float[] thisBlockBox : getBlockShape(blockID)) {
                setAABB(pos.x, pos.y, pos.z, width, height);
                setBlockBox(blockPosX, blockPosY, blockPosz, thisBlockBox);
                if (isWithin() && BlockBoxGetFront() < AABBGetBack() && BlockBoxGetFront() - AABBGetBack() > -0.1f) {
                    pos.z = BlockBoxGetFront() - width - 0.001f;
                    inertia.z = 0;
                }
            }
        }
    }

    public static void collideZNegative(int blockPosX, int blockPosY, int blockPosz, Vector3f pos, Vector3f inertia, float width, float height, int blockID){
        float lastY = pos.y;
        for (float[] thisBlockBox : getBlockShape(blockID)) {
            setAABB(pos.x, pos.y, pos.z, width, height);
            setBlockBox(blockPosX, blockPosY, blockPosz, thisBlockBox);
            if (isWithin() && BlockBoxGetBack() > AABBGetFront() && BlockBoxGetBack() - AABBGetFront() < 0.1f) {
                if (isSteppable(blockID) && AABBGetBottom() - BlockBoxGetTop() < -0.48f) {
                    pos.y = BlockBoxGetTop() + 0.0001f;
                }else {
                    pos.z = BlockBoxGetBack() + width + 0.001f;
                    inertia.z = 0;
                }
            }
        }

        //correction for the sides of stairs
        if (pos.y - lastY > 0.51) {
            pos.y = lastY;
            for (float[] thisBlockBox : getBlockShape(blockID)) {
                setAABB(pos.x, pos.y, pos.z, width, height);
                setBlockBox(blockPosX, blockPosY, blockPosz, thisBlockBox);
                if (isWithin() && BlockBoxGetBack() > AABBGetFront() && BlockBoxGetBack() - AABBGetFront() < 0.1f) {
                    pos.z = BlockBoxGetBack() + width + 0.001f;
                    inertia.z = 0;
                }
            }
        }
    }


    private static boolean isWithin(){
        return !(AABBGetLeft() > BlockBoxGetRight() ||
                 AABBGetRight() < BlockBoxGetLeft() ||
                 AABBGetBottom() > BlockBoxGetTop() ||
                 AABBGetTop() < BlockBoxGetBottom() ||
                 AABBGetFront() > BlockBoxGetBack() ||
                 AABBGetBack() < BlockBoxGetFront());
    }

    private static int detectBlock(Vector3f flooredPos){
        int currentChunkX = (int)(Math.floor(flooredPos.x / 16f));
        int currentChunkZ = (int)(Math.floor(flooredPos.z / 16f));
        Vector3f realPos = new Vector3f(flooredPos.x - (16*currentChunkX), flooredPos.y, flooredPos.z - (16*currentChunkZ));
        return getBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX, currentChunkZ);
    }


    private static int inertiaToDir(float thisInertia){
        if (thisInertia > 0.0001f){
            return 1;
        } else if (thisInertia < -0.0001f){
            return -1;
        }

        return 0;
    }

    public static boolean wouldCollidePlacing(){
        return !(AABBGetLeft() > BlockBoxGetRight() ||
                AABBGetRight() < BlockBoxGetLeft() ||
                AABBGetBottom() > BlockBoxGetTop() ||
                AABBGetTop() < BlockBoxGetBottom() ||
                AABBGetFront() > BlockBoxGetBack() ||
                AABBGetBack() < BlockBoxGetFront());
    }
}