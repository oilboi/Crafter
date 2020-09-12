package engine;

import engine.graph.Mesh;
import engine.graph.Texture;
import org.joml.Vector3f;

public class Entity {

    public final static int MAX_ID_AMOUNT = 126_000;

    private static Texture textureAtlas;

    private static String[]          entityIDList     = new String          [MAX_ID_AMOUNT];
    private static Mesh[]            meshStorage      = new Mesh            [MAX_ID_AMOUNT];
    private static EntityInterface[] interfaceStorage = new EntityInterface [MAX_ID_AMOUNT];
    private static int currentEntityID = 0;
    private static int totalObjects = 0;

    private static int[]             thisMeshID       = new int             [MAX_ID_AMOUNT];
    private static Vector3f[]        position         = new Vector3f        [MAX_ID_AMOUNT];
    private static float[]           scale            = new float           [MAX_ID_AMOUNT];
    private static float[]           hover            = new float           [MAX_ID_AMOUNT];
    private static float[]           timer            = new float           [MAX_ID_AMOUNT];
    private static boolean[]         floatUp          = new boolean         [MAX_ID_AMOUNT];
    private static boolean[]         exists           = new boolean         [MAX_ID_AMOUNT];
    private static boolean[]         collecting       = new boolean         [MAX_ID_AMOUNT];
    private static Vector3f[]        rotation         = new Vector3f        [MAX_ID_AMOUNT];
    private static Vector3f[]        inertia          = new Vector3f        [MAX_ID_AMOUNT];

    public static void registerEntity(String thisName, Mesh thisMesh, EntityInterface thisInterface){
        entityIDList[currentEntityID]     = thisName;
        meshStorage[currentEntityID]      = thisMesh;
        interfaceStorage[currentEntityID] = thisInterface;
        currentEntityID++;
    }
}
