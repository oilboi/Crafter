package game.collision;

import org.joml.Vector3f;

public class CustomBlockBox {
    private float[] aabb;
    private Vector3f basePos;
    public CustomBlockBox(int x, int y, int z){
        aabb = new float[]{(float)x, (float)y, (float)z, x+1f, y+1f, z+1f};
        basePos = new Vector3f((float)x,(float)y,(float)z);
    }
    //getters
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
    //the easy way
    public float[] getAsArray(){
        return aabb;
    }
    //base pos
    public Vector3f getBasePos(){
        return basePos;
    }
}
