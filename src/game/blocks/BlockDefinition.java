package game.blocks;

import org.joml.Vector3f;

import java.util.ArrayList;

import static engine.ItemEntity.createBlockObjectMesh;
import static game.player.TNT.boom;

public class BlockDefinition {

    private final static ArrayList<BlockDefinition> blockIDs = new ArrayList<>();

    //fixed fields for the class
    private static final byte atlasSizeX = 32;
    private static final byte atlasSizeY = 32;

    //actual block object fields
    private final int     ID;
    private final String  name;
    private final float[] frontTexture;  //front
    private final float[] backTexture;   //back
    private final float[] rightTexture;  //right
    private final float[] leftTexture;   //left
    private final float[] topTexture;    //top
    private final float[] bottomTexture; //bottom

    private final BlockModifier blockModifier;


    private BlockDefinition(int ID, String name, int[] front, int[] back, int[] right, int[] left, int[] top, int[] bottom, BlockModifier blockModifier) throws Exception {
        this.ID   = ID;
        this.name = name;
        this.frontTexture  = calculateTexture(  front[0],  front[1] );
        this.backTexture   = calculateTexture(   back[0],   back[1] );
        this.rightTexture  = calculateTexture(  right[0],  right[1] );
        this.leftTexture   = calculateTexture(   left[0],   left[1] );
        this.topTexture    = calculateTexture(    top[0],    top[1] );
        this.bottomTexture = calculateTexture( bottom[0], bottom[1] );
        this.blockModifier = blockModifier;
        blockIDs.add(this);
        //TODO: INITIALIZE NEW OBJECT MESH FOR THIS BLOCK
        createBlockObjectMesh(ID);
    }

    public static void onDigCall(int ID, Vector3f pos) throws Exception {
        if(blockIDs.get(ID) != null && blockIDs.get(ID).blockModifier != null){
            blockIDs.get(ID).blockModifier.onDig(pos);
        }
    }

    private static float[] calculateTexture(int x, int y){
        float[] texturePoints = new float[4];
        texturePoints[0] = (float)x/(float)atlasSizeX;     //min x (-)
        texturePoints[1] = (float)(x+1)/(float)atlasSizeX; //max x (+)

        texturePoints[2] = (float)y/(float)atlasSizeY;     //min y (-)
        texturePoints[3] = (float)(y+1)/(float)atlasSizeY; //max y (+)
        return texturePoints;
    }

    public static void initializeBlocks() throws Exception {

        new BlockDefinition(
                0,
                "air",
                new int[]{-1,-1}, //front
                new int[]{-1,-1}, //back
                new int[]{-1,-1}, //right
                new int[]{-1,-1}, //left
                new int[]{-1,-1}, //top
                new int[]{-1,-1},  //bottom
                null
        );

        new BlockDefinition(
                1,
                "dirt",
                new int[]{0,0}, //front
                new int[]{0,0}, //back
                new int[]{0,0}, //right
                new int[]{0,0}, //left
                new int[]{0,0}, //top
                new int[]{0,0},  //bottom
                null
        );

        new BlockDefinition(
                2,
                "grass",
                new int[]{5,0}, //front
                new int[]{5,0}, //back
                new int[]{5,0}, //right
                new int[]{5,0}, //left
                new int[]{4,0}, //top
                new int[]{0,0},  //bottom
                null
        );

        new BlockDefinition(
                3,
                "stone",
                new int[]{1,0}, //front
                new int[]{1,0}, //back
                new int[]{1,0}, //right
                new int[]{1,0}, //left
                new int[]{1,0}, //top
                new int[]{1,0},  //bottom
                null
        );

        new BlockDefinition(
                4,
                "cobblestone",
                new int[]{2,0}, //front
                new int[]{2,0}, //back
                new int[]{2,0}, //right
                new int[]{2,0}, //left
                new int[]{2,0}, //top
                new int[]{2,0},  //bottom
                null
        );

        new BlockDefinition(
                5,
                "bedrock",
                new int[]{6,0}, //front
                new int[]{6,0}, //back
                new int[]{6,0}, //right
                new int[]{6,0}, //left
                new int[]{6,0}, //top
                new int[]{6,0},  //bottom
                null
        );


        //tnt explosion
        BlockModifier kaboom = new BlockModifier() {
            @Override
            public void onDig(Vector3f pos) throws Exception {
                boom((int)pos.x, (int)pos.y, (int)pos.z, 5);
            }
        };

        new BlockDefinition(
                6,
                "tnt",
                new int[]{7,0}, //front
                new int[]{7,0}, //back
                new int[]{7,0}, //right
                new int[]{7,0}, //left
                new int[]{8,0}, //top
                new int[]{9,0},  //bottom
                kaboom
        );

        new BlockDefinition(
                7,
                "water",
                new int[]{10,0}, //front
                new int[]{10,0}, //back
                new int[]{10,0}, //right
                new int[]{10,0}, //left
                new int[]{10,0}, //top
                new int[]{10,0},  //bottom
                null
        );
    }

    public static BlockDefinition getID(int ID){
        return blockIDs.get(ID);
    }

    public static float[] getFrontTexturePoints(int ID){
        return blockIDs.get(ID).frontTexture;
    }
    public static float[] getBackTexturePoints(int ID){
        return blockIDs.get(ID).backTexture;
    }
    public static float[] getRightTexturePoints(int ID){
        return blockIDs.get(ID).rightTexture;
    }
    public static float[] getLeftTexturePoints(int ID){
        return blockIDs.get(ID).leftTexture;
    }
    public static float[] getTopTexturePoints(int ID){
        return blockIDs.get(ID).topTexture;
    }
    public static float[] getBottomTexturePoints(int ID){
        return blockIDs.get(ID).bottomTexture;
    }

}
