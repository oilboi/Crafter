package engine;

import engine.graph.Mesh;

public class Entity {
    public static void registerEntity(EntityInterface thisInterface){
        thisInterface.onCreate();
    }
}
