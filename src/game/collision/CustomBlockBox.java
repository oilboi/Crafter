package game.collision;

public class CustomBlockBox {

    private float[] aabb;

    public CustomBlockBox(int x, int y, int z){

        aabb = new float[]{(float)x, (float)y, (float)z, x+1f, y+1f, z+1f};

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
}
