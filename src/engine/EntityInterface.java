package engine;

import engine.graph.Mesh;

public interface EntityInterface {
    default void onCreate(){
        System.out.println("Your on create method is blank!");
    }

    default void onTick(){
        System.out.println("Your on tick method is blank!");
    }

    default void onDelete(){
        System.out.println("Your on delete method is blank!");
    }
}
