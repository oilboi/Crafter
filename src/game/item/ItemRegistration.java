package game.item;

import org.joml.Vector3f;

import static game.chunk.Chunk.setBlock;
import static game.item.ItemDefinition.registerItem;

public class ItemRegistration {
    public static void registerTools(){

        registerItem("stone_pickaxe", "textures/stone_pickaxe.png", null);


        ItemModifier test = new ItemModifier() {
            @Override
            public void onPlace(Vector3f pos) {
                setBlock((int)pos.x,(int)pos.y,(int)pos.z, 25);
                setBlock((int)pos.x,(int)pos.y+1,(int)pos.z, 24);
            }
        };

        registerItem("door", "textures/door.png", test);
    }
}
