package game.collision;

public class CustomAABB {
    private float[] aabb;
    public CustomAABB(float x, float y, float z, float width, float height){
        aabb = new float[]{x-width, y, z-width, x+width, y+height, z+width};
        //                    0     1     2        3        4         5
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
}
