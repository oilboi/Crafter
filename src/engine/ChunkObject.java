package engine;

import engine.graph.Mesh;
import org.joml.Vector3f;

public class ChunkObject {

    private final Mesh mesh;

    public ChunkObject(Mesh mesh){
        this.mesh = mesh;
    }

    public Mesh getMesh(){
        return mesh;
    }

}
