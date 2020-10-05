package game.particle;

import engine.graph.Mesh;
import org.joml.Vector3f;

public class ParticleObject {
    public Vector3f pos;
    public Vector3f inertia;
    public Mesh mesh;

    public ParticleObject(Vector3f pos, Vector3f inertia, Mesh mesh){
        this.pos = pos;
        this.inertia = inertia;
        this.mesh = mesh;
    }
}
