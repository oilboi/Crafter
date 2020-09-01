package game.blocks;

import org.joml.Vector3f;

public interface BlockModifier {
    default public void onDig(Vector3f pos) throws Exception {
        System.out.println("worked");
    }
}
