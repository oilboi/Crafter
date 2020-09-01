package game.collision;

import org.joml.Vector3f;

public class CustomAABB {

    private float[] aabb;
    private float width;
    private float height;
    public CustomAABB(float x, float y, float z, float width, float height){

        aabb = new float[]{x-width, y, z-width, x+width, y+height, z+width};

        this.width = width;
        this.height = height;
        //                    0     1     2        3        4         5
    }

    public void updatePos(Vector3f pos){
        aabb[0] = pos.x-width;
        aabb[1] = pos.y;
        aabb[2] = pos.z-width;
        aabb[3] = pos.x+width;
        aabb[4] = pos.y+height;
        aabb[5] = pos.z+width;
    }

    public void updateWidth(float width){
        this.width = width;
    }

    public float getLeft(){
        return aabb[0];
    }

    public float getBottom(){
        return aabb[1];
    }

    public float getFront(){
        return aabb[2];
    }

    public float getRight(){
        return aabb[3];
    }

    public float getTop(){
        return aabb[4];
    }

    public float getBack(){
        return aabb[5];
    }
}
