package game.mob;

import org.joml.Vector3f;

import static game.mob.Mob.getMobDefinition;

public class MobObject {
    public Vector3f pos;
    public Vector3f inertia;
    public int mobTableKey;
    public String mobDefinitionKey;
    public float timer;
    public final float width;
    public final float height;
    public float rotation;


    public MobObject(Vector3f pos, Vector3f inertia, String mobDefinitionKey, int mobTableKey){
        this.pos = pos;
        this.inertia = inertia;
        this.mobDefinitionKey = mobDefinitionKey;
        this.mobTableKey = mobTableKey;
        this.timer = 0f;
        //inheritance to prevent lookup every frame
        this.height = getMobDefinition(mobDefinitionKey).height;
        this.width = getMobDefinition(mobDefinitionKey).width;
        this.rotation = 0f;
    }

}
