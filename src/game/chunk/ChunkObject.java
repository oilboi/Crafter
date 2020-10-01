package game.chunk;

import engine.graph.Mesh;

public class ChunkObject {
    public final String ID;

    public final int x;
    public final int z;

    public final int [][][] block;
    public final byte[][][] rotation;
    public final byte[][][] light;

    public final Mesh[] mesh;
    public final Mesh[] liquidMesh;


    public ChunkObject(int x, int z){
        this.ID = x + " " + z;

        this.x = x;
        this.z = z;

        this.block    = new int [128][16][16];
        this.rotation = new byte[128][16][16];
        this.light    = new byte[128][16][16];

        this.mesh       = new Mesh[8];
        this.liquidMesh = new Mesh[8];
    }
}
