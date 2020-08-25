package game.blocks;

import java.util.ArrayList;

public class BlockDefinition {

    private static ArrayList<BlockDefinition> blockIDs = new ArrayList<>();

    //fixed fields for the class
    private static final byte atlasSizeX = 64;
    private static final byte atlasSizeY = 64;

    //actual block object fields
    private int     ID;
    private String  name;
    private float[] frontTexture;  //front
    private float[] backTexture;   //back
    private float[] rightTexture;  //right
    private float[] leftTexture;   //left
    private float[] topTexture;    //top
    private float[] bottomTexture; //bottom

    private BlockDefinition(int ID, String name, int[] front, int[] back, int[] right, int[] left, int[] top, int[] bottom){
        this.ID   = ID;
        this.name = name;
        this.frontTexture  = calculateTexture(  front[0],  front[1] );
        this.backTexture   = calculateTexture(   back[0],   back[1] );
        this.rightTexture  = calculateTexture(  right[0],  right[1] );
        this.leftTexture   = calculateTexture(   left[0],   left[1] );
        this.topTexture    = calculateTexture(    top[0],    top[1] );
        this.bottomTexture = calculateTexture( bottom[0], bottom[1] );

        blockIDs.add(this);
    }

    private static float[] calculateTexture(int x, int y){
        float[] texturePoints = new float[4];
        texturePoints[0] = (float)x/(float)atlasSizeX;     //min x
        texturePoints[1] = (float)(x+1)/(float)atlasSizeX; //max x
        texturePoints[2] = (float)y/(float)atlasSizeY;     //min y
        texturePoints[3] = (float)(y+1)/(float)atlasSizeY; //max y
        return texturePoints;
    }

    public static void initializeBlocks(){

        new BlockDefinition(
                0,
                "air",
                new int[]{-1,-1}, //front
                new int[]{-1,-1}, //back
                new int[]{-1,-1}, //right
                new int[]{-1,-1}, //left
                new int[]{-1,-1}, //top
                new int[]{-1,-1}  //bottom
        );

        new BlockDefinition(
                1,
                "dirt",
                new int[]{0,0}, //front
                new int[]{0,0}, //back
                new int[]{0,0}, //right
                new int[]{0,0}, //left
                new int[]{0,0}, //top
                new int[]{0,0}  //bottom
        );
    }

    public static BlockDefinition getID(int ID){
        return blockIDs.get(ID);
    }
    
}
