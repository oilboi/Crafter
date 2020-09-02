package game.blocks;

import org.joml.Vector3f;

import java.util.ArrayList;

import static engine.Chunk.setBlock;
import static engine.ItemEntity.createBlockObjectMesh;
import static engine.ItemEntity.createItem;
import static engine.TNTEntity.createTNT;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.light.Light.floodFill;
import static game.player.TNT.boom;

public class BlockDefinition {

    private final static BlockDefinition[] blockIDs = new BlockDefinition[256];

    //fixed fields for the class
    private static final byte atlasSizeX = 32;
    private static final byte atlasSizeY = 32;

    //actual block object fields
    private final int     ID;
    private final String  name;
    private final boolean dropsItem;
    private final float[] frontTexture;  //front
    private final float[] backTexture;   //back
    private final float[] rightTexture;  //right
    private final float[] leftTexture;   //left
    private final float[] topTexture;    //top
    private final float[] bottomTexture; //bottom

    private final BlockModifier blockModifier;


    private BlockDefinition(int ID, String name, boolean dropsItem, int[] front, int[] back, int[] right, int[] left, int[] top, int[] bottom, boolean walkable, BlockModifier blockModifier) throws Exception {
        this.ID   = ID;
        this.name = name;
        this.dropsItem = dropsItem;
        this.frontTexture  = calculateTexture(  front[0],  front[1] );
        this.backTexture   = calculateTexture(   back[0],   back[1] );
        this.rightTexture  = calculateTexture(  right[0],  right[1] );
        this.leftTexture   = calculateTexture(   left[0],   left[1] );
        this.topTexture    = calculateTexture(    top[0],    top[1] );
        this.bottomTexture = calculateTexture( bottom[0], bottom[1] );
        this.blockModifier = blockModifier;
        blockIDs[ID] = this;
        //TODO: INITIALIZE NEW OBJECT MESH FOR THIS BLOCK
        createBlockObjectMesh(ID);
    }

    public static void onDigCall(int ID, Vector3f pos) throws Exception {
        if(blockIDs[ID] != null){
            if(blockIDs[ID].dropsItem){
                createItem(ID, pos);
            }
            if(blockIDs[ID].blockModifier != null){
                blockIDs[ID].blockModifier.onDig(pos);
            }
        }
    }

    public static void onPlaceCall(int ID, Vector3f pos) throws Exception {
        if(blockIDs[ID] != null && blockIDs[ID].blockModifier != null){
            blockIDs[ID].blockModifier.onPlace(pos);
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
                false,
                new int[]{-1,-1}, //front
                new int[]{-1,-1}, //back
                new int[]{-1,-1}, //right
                new int[]{-1,-1}, //left
                new int[]{-1,-1}, //top
                new int[]{-1,-1},  //bottom
                false,
                null
        );

        new BlockDefinition(
                1,
                "dirt",
                true,
                new int[]{0,0}, //front
                new int[]{0,0}, //back
                new int[]{0,0}, //right
                new int[]{0,0}, //left
                new int[]{0,0}, //top
                new int[]{0,0},  //bottom
                true,
                null
        );

        new BlockDefinition(
                2,
                "grass",
                true,
                new int[]{5,0}, //front
                new int[]{5,0}, //back
                new int[]{5,0}, //right
                new int[]{5,0}, //left
                new int[]{4,0}, //top
                new int[]{0,0},  //bottom
                true,
                null
        );

        new BlockDefinition(
                3,
                "stone",
                true,
                new int[]{1,0}, //front
                new int[]{1,0}, //back
                new int[]{1,0}, //right
                new int[]{1,0}, //left
                new int[]{1,0}, //top
                new int[]{1,0},  //bottom
                true,
                null
        );

        new BlockDefinition(
                4,
                "cobblestone",
                true,
                new int[]{2,0}, //front
                new int[]{2,0}, //back
                new int[]{2,0}, //right
                new int[]{2,0}, //left
                new int[]{2,0}, //top
                new int[]{2,0},  //bottom
                true,
                null
        );

        new BlockDefinition(
                5,
                "bedrock",
                false,
                new int[]{6,0}, //front
                new int[]{6,0}, //back
                new int[]{6,0}, //right
                new int[]{6,0}, //left
                new int[]{6,0}, //top
                new int[]{6,0},  //bottom
                true,
                null
        );


        //tnt explosion
        BlockModifier kaboom = new BlockModifier() {
            @Override
            public void onDig(Vector3f pos) throws Exception {
                //boom((int)pos.x, (int)pos.y, (int)pos.z, 5);
                createTNT(pos);
            }
        };

        new BlockDefinition(
                6,
                "tnt",
                false,
                new int[]{7,0}, //front
                new int[]{7,0}, //back
                new int[]{7,0}, //right
                new int[]{7,0}, //left
                new int[]{8,0}, //top
                new int[]{9,0},  //bottom
                true,
                kaboom
        );

        //water thing
        BlockModifier splash = new BlockModifier() {
            @Override
            public void onPlace(Vector3f pos) throws Exception {
                int currentChunkX = (int) (Math.floor((float) pos.x / 16f));
                int currentChunkZ = (int) (Math.floor((float) pos.z / 16f));
                int currentPosX = (int) (pos.x - (16 * currentChunkX));
                int currentPosZ = (int) (pos.z - (16 * currentChunkZ));

                for(int y = 0; y < 128; y++){
                    setBlock(currentPosX, y, currentPosZ, currentChunkX, currentChunkZ,7);
                }

                floodFill(currentChunkX, currentChunkZ);
                generateChunkMesh(currentChunkX, currentChunkZ, true);
            }
        };

        new BlockDefinition(
                7,
                "water",
                true,
                new int[]{10,0}, //front
                new int[]{10,0}, //back
                new int[]{10,0}, //right
                new int[]{10,0}, //left
                new int[]{10,0}, //top
                new int[]{10,0},  //bottom
                true,
                splash
        );

        new BlockDefinition(
                8,
                "flarg",
                true,
                new int[]{1,0}, //front
                new int[]{4,0}, //back
                new int[]{2,0}, //right
                new int[]{6,0}, //left
                new int[]{7,0}, //top
                new int[]{3,0},  //bottom
                true,
                kaboom
        );
    }

    public static BlockDefinition getID(int ID){
        return blockIDs[ID];
    }

    public static float[] getFrontTexturePoints(int ID){
        return blockIDs[ID].frontTexture;
    }
    public static float[] getBackTexturePoints(int ID){
        return blockIDs[ID].backTexture;
    }
    public static float[] getRightTexturePoints(int ID){
        return blockIDs[ID].rightTexture;
    }
    public static float[] getLeftTexturePoints(int ID){
        return blockIDs[ID].leftTexture;
    }
    public static float[] getTopTexturePoints(int ID){
        return blockIDs[ID].topTexture;
    }
    public static float[] getBottomTexturePoints(int ID){
        return blockIDs[ID].bottomTexture;
    }

}
