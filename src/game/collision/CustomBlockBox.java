package game.collision;

public class CustomBlockBox {

    private static float[] aabb = new float[6];

    public static void setBlockBox(int x, int y, int z){

        aabb[0] = (float)x;
        aabb[1] = (float)y;
        aabb[2] = (float)z;
        aabb[3] = x+1f;
        aabb[4] = y+1f;
        aabb[5] = z+1f;

    }

    //getters

    public static float BlockBoxGetLeft(){
        return aabb[0];
    }

    public static float BlockBoxGetBottom(){
        return aabb[1];
    }

    public static float BlockBoxGetFront(){
        return aabb[2];
    }

    public static float BlockBoxGetRight(){
        return aabb[3];
    }

    public static float BlockBoxGetTop(){
        return aabb[4];
    }

    public static float getBack(){
        return aabb[5];
    }
}
