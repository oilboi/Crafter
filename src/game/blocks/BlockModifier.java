package game.blocks;

import org.joml.Vector3f;

public interface BlockModifier {
    default public void onDig() {
        System.out.println("worked");
    }
}
