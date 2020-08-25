package game.collision;

import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;

import static game.ChunkHandling.ChunkData.getBlockInChunk;
import static game.Crafter.getChunkRenderDistance;

public class Collision {
    private static int renderDistance = getChunkRenderDistance();
    final private static float gameSpeed = 0.001f;
    public static boolean applyInertia(Vector3f pos, Vector3f inertia, boolean onGround, float width, float height, boolean gravity){
        if(gravity) {
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

        onGround = collisionDetect(pos, inertia, onGround, width, height);

        //apply friction
        inertia.x += -inertia.x * gameSpeed * 10; // do (10 - 9.5f) for slippery!
        inertia.z += -inertia.z * gameSpeed * 10;

        return onGround;
    }

    private static boolean collisionDetect(Vector3f pos, Vector3f inertia, boolean onGround, float width, float height){
        onGround = false;

        //get the real positions of the blocks
        Vector3f fPos = new Vector3f(pos);

        fPos.x = (float)Math.floor(fPos.x);
        fPos.y = (float)Math.floor(fPos.y);
        fPos.z = (float)Math.floor(fPos.z);


        int y = 2;
        int x = -1;
        int z = -1;
        //collide with all blocks in local area
        for (int i = 0; i < 36; i++){
            if (detectBlock(new Vector3f(fPos.x + x, fPos.y + y, fPos.z + z))) {
                CustomAABB us = new CustomAABB(pos.x, pos.y, pos.z, width, height);
                onGround = collide(us, new CustomBlockBox((int) fPos.x + x, (int) fPos.y + y, (int) fPos.z + z), pos, inertia, width, height, onGround);
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

        return onGround;
    }


    //this is where actual collision events occur!
    public static boolean collide(CustomAABB us, CustomBlockBox block, Vector3f pos, Vector3f inertia, float width, float height, boolean onGround){
        boolean xWithin = !(us.getLeft()   > block.getRight() || us.getRight() < block.getLeft());
        boolean yWithin = !(us.getBottom() > block.getTop()   || us.getTop()   < block.getBottom());
        boolean zWithin = !(us.getFront()  > block.getBack()  || us.getBack()  < block.getFront());

        //double check to stop clipping if not enough space
        if (xWithin && zWithin && yWithin &&
                !detectBlock(new Vector3f(block.getLeft(), block.getBottom()+1,block.getFront())) &&
                !detectBlock(new Vector3f(block.getLeft(), block.getBottom()+2,block.getFront()))) {

            //floor detection
            if (block.getTop() > us.getBottom() && inertia.y < 0 && us.getBottom() - block.getTop() > -0.15f) {
                //this is the collision debug sphere for terrain
                float oldPos = pos.y;
                pos.y = block.getTop();
                //don't move up if too high
                if (pos.y - oldPos > 1) {
                    pos.y = (int)oldPos;
                }
                inertia.y = 0;
                onGround = true;
            }
        }

        //stop getting shot across the map
        if (xWithin && zWithin && yWithin  &&
                !detectBlock(new Vector3f(block.getLeft(), block.getBottom()-1,block.getFront())) &&
                !detectBlock(new Vector3f(block.getLeft(), block.getBottom()-2,block.getFront()))) {
            //head detection
            if (block.getBottom() < us.getTop() && inertia.y >= 0 && us.getTop() - block.getBottom() < 0.15f) {
                pos.y = block.getBottom() - height;
                inertia.y = 0;
            }
        }

        float averageX = Math.abs(((block.getLeft() + block.getRight())/2f) - pos.x);
        float averageY = Math.abs(((block.getBottom() + block.getTop())/2f) - pos.y);
        float averageZ = Math.abs(((block.getFront() + block.getBack())/2f) - pos.z);
        if (averageX > averageZ) {
            if (!detectBlock(new Vector3f(block.getLeft()+1, block.getBottom(),block.getFront()))) {
                us = new CustomAABB(pos.x, pos.y + 0.1501f, pos.z, width, height - 0.3001f);
                xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
                yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
                zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

                //x- detection
                if (xWithin && zWithin && yWithin) {
                    if (block.getRight() > us.getLeft() && inertia.x < 0) {
                        pos.x = block.getRight() + width + 0.00001f;
                        inertia.x = 0;
                    }
                }
            }
            if (!detectBlock(new Vector3f(block.getLeft()-1, block.getBottom(),block.getFront()))) {
                us = new CustomAABB(pos.x, pos.y + 0.1501f, pos.z, width, height - 0.3001f);
                xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
                yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
                zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

                //x+ detection
                if (xWithin && zWithin && yWithin) {
                    if (block.getLeft() < us.getRight() && inertia.x > 0) {
                        pos.x = block.getLeft() - width - 0.00001f;
                        inertia.x = 0;
                    }
                }
            }
        } else {
            if (!detectBlock(new Vector3f(block.getLeft(), block.getBottom(),block.getFront()+1))) {
                us = new CustomAABB(pos.x, pos.y + 0.1501f, pos.z, width, height - 0.3001f);
                xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
                yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
                zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

                //z- detection
                if (xWithin && zWithin && yWithin) {
                    if (block.getBack() > us.getFront() && inertia.z < 0) {
                        pos.z = block.getBack() + width + 0.00001f;
                        inertia.z = 0;
                    }
                }
            }
            if (!detectBlock(new Vector3f(block.getLeft(), block.getBottom(),block.getFront()-1))) {
                us = new CustomAABB(pos.x, pos.y + 0.1501f, pos.z, width, height - 0.3001f);
                xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
                yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
                zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

                //z+ detection
                if (xWithin && zWithin && yWithin) {
                    if (block.getFront() < us.getBack() && inertia.z > 0) {
                        pos.z = block.getFront() - width - 0.00001f;
                        inertia.z = 0;
                    }
                }
            }
        }
        return onGround;
    }

    private static boolean detectBlock(Vector3f flooredPos){
        int currentChunkX = (int)(Math.floor(flooredPos.x / 16f));
        int currentChunkZ = (int)(Math.floor(flooredPos.z / 16f));

        Vector3f realPos = new Vector3f(flooredPos.x - (16*currentChunkX), flooredPos.y, flooredPos.z - (16*currentChunkZ));

        return getBlockInChunk((int)realPos.x, (int)realPos.y, (int)realPos.z, currentChunkX, currentChunkZ) != 0;
    }

    //this is used for block placing
    public static boolean wouldCollide(CustomAABB us, CustomBlockBox block){
        boolean xWithin = !(us.getLeft()   > block.getRight() || us.getRight() < block.getLeft());
        boolean yWithin = !(us.getBottom() > block.getTop()   || us.getTop()   < block.getBottom());
        boolean zWithin = !(us.getFront()  > block.getBack()  || us.getBack()  < block.getFront());
        return (xWithin && yWithin && zWithin);
    }
}
