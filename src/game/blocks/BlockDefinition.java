package game.blocks;

import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static engine.Chunk.setBlock;
import static engine.ItemEntity.createBlockObjectMesh;
import static engine.ItemEntity.createItem;
import static engine.TNTEntity.createTNT;
import static engine.sound.SoundAPI.playSound;

public class BlockDefinition {

    private final static BlockDefinition[] blockIDs = new BlockDefinition[256];
    private static Map<String, BlockShape> blockShapeMap = new HashMap<>();

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
    private final boolean walkable;
    private final boolean steppable;
    private final String drawType;
    private final String placeSound;
    private final String digSound;
    private final BlockModifier blockModifier;

    private BlockDefinition(
            int ID,String name,
            boolean dropsItem,
            int[] front,
            int[] back,
            int[] right,
            int[] left,
            int[] top,
            int[] bottom,
            String drawType,
            boolean walkable,
            boolean steppable,
            BlockModifier blockModifier,
            String placeSound,
            String digSound
    ) throws Exception {

        this.ID   = ID;
        this.name = name;
        this.dropsItem = dropsItem;
        this.frontTexture  = calculateTexture(  front[0],  front[1] );
        this.backTexture   = calculateTexture(   back[0],   back[1] );
        this.rightTexture  = calculateTexture(  right[0],  right[1] );
        this.leftTexture   = calculateTexture(   left[0],   left[1] );
        this.topTexture    = calculateTexture(    top[0],    top[1] );
        this.bottomTexture = calculateTexture( bottom[0], bottom[1] );
        this.drawType = drawType;
        this.walkable = walkable;
        this.steppable = steppable;
        this.blockModifier = blockModifier;
        this.placeSound = placeSound;
        this.digSound = digSound;
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
            if (!blockIDs[ID].digSound.equals("")) {
                playSound(blockIDs[ID].digSound, pos.add(0.5f, 0.5f, 0.5f));
            }
        }
    }

    public static void onPlaceCall(int ID, Vector3f pos) throws Exception {
        if(blockIDs[ID] != null && blockIDs[ID].blockModifier != null) {
            blockIDs[ID].blockModifier.onPlace(pos);
        }

        if (!blockIDs[ID].placeSound.equals("")) {
            playSound(blockIDs[ID].placeSound, pos.add(0.5f, 0.5f, 0.5f));
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

    public static String getBlockName(int ID){
        return blockIDs[ID].name;
    }

    public static String getBlockDrawType(int ID){
        return blockIDs[ID].drawType;
    }

    public static float[][] getBlockShape(int ID){
        return blockShapeMap.get(blockIDs[ID].drawType).getBoxes();
    }

    public static boolean isWalkable(int ID){
        return blockIDs[ID].walkable;
    }

    public static boolean isSteppable(int ID){
        return blockIDs[ID].steppable;
    }

    public static void initializeBlocks() throws Exception {

        blockShapeMap.put("air",
                new BlockShape(new float[][]{{0f,0f,0f,1f,1f,1f}})
        );

        blockShapeMap.put("normal",
                new BlockShape(new float[][]{{0f,0f,0f,1f,1f,1f}})
        );

        blockShapeMap.put("stair 1",
                new BlockShape(
                        new float[][]{
                                {0f,0f,0f,1f,0.5f,1f},
                                {0f,0f,0f,1f,1f,0.5f}
                        }
                        )
        );

        blockShapeMap.put("stair 2",
                new BlockShape(
                        new float[][]{
                                {0f,0f,0f,1f,0.5f,1f},

                                {0f,0f,0.5f,1f,1f,1f}
                        }
                )
        );

        blockShapeMap.put("stair 3",
                new BlockShape(
                        new float[][]{
                                {0f,0f,0f,1f,0.5f,1f},
                                {0f,0f,0f,0.5f,1f,1f}
                        }
                )
        );

        blockShapeMap.put("stair 4",
                new BlockShape(
                        new float[][]{
                                {0f,0f,0f,1f,0.5f,1f},
                                {0.5f,0f,0f,1f,1f,1f}
                        }
                )
        );


        blockShapeMap.put("slab",
                new BlockShape(
                        new float[][]{
                                {0f,0f,0f,1f,0.5f,1f}
                        }
                )
        );


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
                "air",
                false,
                false,
                null,
                "",
                ""
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
                "normal",
                true,
                false,
                null,
                "dirt_1",
                "dirt_2"
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
                "normal",
                true,
                false,
                null,
                "dirt_1",
                "dirt_2"
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
                "normal",
                true,
                false,
                null,
                "stone_1",
                "stone_2"
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
                "normal",
                true,
                false,
                null,
                "stone_3",
                "stone_2"
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
                "normal",
                true,
                false,
                null,
                "stone_1",
                "stone_1"
        );


        //tnt explosion
        BlockModifier kaboom = new BlockModifier() {
            @Override
            public void onDig(Vector3f pos) throws Exception {
                createTNT(pos, 0, true);
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
                "normal",
                true,
                false,
                kaboom,
                "dirt_1",
                "wood_2"
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
                "normal",
                true,
                false,
                splash,
                "",
                ""
        );

        new BlockDefinition(
                8,
                "coal ore",
                true,
                new int[]{11,0}, //front
                new int[]{11,0}, //back
                new int[]{11,0}, //right
                new int[]{11,0}, //left
                new int[]{11,0}, //top
                new int[]{11,0},  //bottom
                "normal",
                true,
                false,
                null,
                "stone_1",
                "stone_2"
        );

        new BlockDefinition(
                9,
                "iron ore",
                true,
                new int[]{12,0}, //front
                new int[]{12,0}, //back
                new int[]{12,0}, //right
                new int[]{12,0}, //left
                new int[]{12,0}, //top
                new int[]{12,0},  //bottom
                "normal",
                true,
                false,
                null,
                "stone_1",
                "stone_2"
        );

        new BlockDefinition(
                10,
                "gold ore",
                true,
                new int[]{13,0}, //front
                new int[]{13,0}, //back
                new int[]{13,0}, //right
                new int[]{13,0}, //left
                new int[]{13,0}, //top
                new int[]{13,0},  //bottom
                "normal",
                true,
                false,
                null,
                "stone_1",
                "stone_2"
        );

        new BlockDefinition(
                11,
                "diamond ore",
                true,
                new int[]{14,0}, //front
                new int[]{14,0}, //back
                new int[]{14,0}, //right
                new int[]{14,0}, //left
                new int[]{14,0}, //top
                new int[]{14,0},  //bottom
                "normal",
                true,
                false,
                null,
                "stone_1",
                "stone_2"
        );

        new BlockDefinition(
                12,
                "emerald ore",
                true,
                new int[]{15,0}, //front
                new int[]{15,0}, //back
                new int[]{15,0}, //right
                new int[]{15,0}, //left
                new int[]{15,0}, //top
                new int[]{15,0},  //bottom
                "normal",
                true,
                false,
                null,
                "stone_1",
                "stone_2"
        );

        new BlockDefinition(
                13,
                "lapis lazuli",
                true,
                new int[]{16,0}, //front
                new int[]{16,0}, //back
                new int[]{16,0}, //right
                new int[]{16,0}, //left
                new int[]{16,0}, //top
                new int[]{16,0},  //bottom
                "normal",
                true,
                false,
                null,
                "stone_1",
                "stone_2"
        );

        new BlockDefinition(
                14,
                "sapphire ore",
                true,
                new int[]{17,0}, //front
                new int[]{17,0}, //back
                new int[]{17,0}, //right
                new int[]{17,0}, //left
                new int[]{17,0}, //top
                new int[]{17,0},  //bottom
                "normal",
                true,
                false,
                null,
                "stone_1",
                "stone_2"
        );

        new BlockDefinition(
                15,
                "ruby ore",
                true,
                new int[]{18,0}, //front
                new int[]{18,0}, //back
                new int[]{18,0}, //right
                new int[]{18,0}, //left
                new int[]{18,0}, //top
                new int[]{18,0},  //bottom
                "normal",
                true,
                false,
                null,
                "stone_1",
                "stone_2"
        );

        new BlockDefinition(
                16,
                "cobblestone stair 1",
                true,
                new int[]{2,0}, //front
                new int[]{2,0}, //back
                new int[]{2,0}, //right
                new int[]{2,0}, //left
                new int[]{2,0}, //top
                new int[]{2,0},  //bottom
                "stair 1",
                true,
                true,
                null,
                "stone_3",
                "stone_2"
        );

        new BlockDefinition(
                17,
                "cobblestone stair 2",
                true,
                new int[]{2,0}, //front
                new int[]{2,0}, //back
                new int[]{2,0}, //right
                new int[]{2,0}, //left
                new int[]{2,0}, //top
                new int[]{2,0},  //bottom
                "stair 2",
                true,
                true,
                null,
                "stone_3",
                "stone_2"
        );

        new BlockDefinition(
                18,
                "cobblestone stair 3",
                true,
                new int[]{2,0}, //front
                new int[]{2,0}, //back
                new int[]{2,0}, //right
                new int[]{2,0}, //left
                new int[]{2,0}, //top
                new int[]{2,0},  //bottom
                "stair 3",
                true,
                true,
                null,
                "stone_3",
                "stone_2"
        );

        new BlockDefinition(
                19,
                "cobblestone stair 4",
                true,
                new int[]{2,0}, //front
                new int[]{2,0}, //back
                new int[]{2,0}, //right
                new int[]{2,0}, //left
                new int[]{2,0}, //top
                new int[]{2,0},  //bottom
                "stair 4",
                true,
                true,
                null,
                "stone_3",
                "stone_2"
        );

        new BlockDefinition(
                20,
                "pumpkin",
                true,
                new int[]{19,0}, //front
                new int[]{19,0}, //back
                new int[]{19,0}, //right
                new int[]{19,0}, //left
                new int[]{20,0}, //top
                new int[]{20,0},  //bottom
                "normal",
                true,
                false,
                null,
                "wood_1",
                "wood_2"
        );
        new BlockDefinition(
                21,
                "jack 'o lantern unlit",
                true,
                new int[]{21,0}, //front
                new int[]{19,0}, //back
                new int[]{19,0}, //right
                new int[]{19,0}, //left
                new int[]{20,0}, //top
                new int[]{20,0},  //bottom
                "normal",
                true,
                false,
                null,
                "wood_1",
                "wood_2"
        );
        new BlockDefinition(
                22,
                "jack 'o lantern lit",
                true,
                new int[]{22,0}, //front
                new int[]{19,0}, //back
                new int[]{19,0}, //right
                new int[]{19,0}, //left
                new int[]{20,0}, //top
                new int[]{20,0},  //bottom
                "normal",
                true,
                false,
                null,
                "wood_1",
                "wood_2"
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
