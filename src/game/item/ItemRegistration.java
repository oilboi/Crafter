package game.item;

import static game.item.ItemDefinition.registerItem;

public class ItemRegistration {
    public static void registerTools(){
        registerItem("stone_pickaxe", "textures/stone_pickaxe.png");

        registerItem("door", "textures/door.png");
    }
}
