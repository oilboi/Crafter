package game.player;

import static game.item.ItemEntity.createItem;
import static engine.graph.Camera.getCameraRotationVector;
import static game.player.Player.*;

public class Inventory {
    private static String[][] inventoryNames = new String[4][9];
    private static int[][] inventoryNumbers = new int[4][9];

    private static String mouseInventoryName;
    private static int mouseInventoryNumber;

    public static void generateRandomInventory(){
        for (int x = 0; x < 9; x++){
            for (int y = 0; y < 4; y++){
                inventoryNumbers[y][x] =(int)(Math.floor(Math.random() * 23)); //23
            }
        }
    }
    public static void resetInventory(){
        for (int x = 0; x < 9; x++){
            for (int y = 0; y < 4; y++){
                inventoryNumbers[y][x] = 0;
            }
        }
    }

    public static void tntFillErUp(){
        for (int x = 0; x < 9; x++){
            for (int y = 0; y < 4; y++){
                inventoryNumbers[y][x] = 23;
            }
        }
    }

    public static void addItemToInventory(int ID){
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                if (inventoryNumbers[y][x] == 0){
                    inventoryNumbers[y][x] = ID;
                    return;
                }
            }
        }
    }

    public static void throwItem(){
        createItem(getItemInInventorySlot(getPlayerInventorySelection(), 0), getPlayerPosWithEyeHeight(), getCameraRotationVector().mul(10f,5f,10f));
        removeItemFromInventory(getPlayerInventorySelection(), 0);
    }
    public static void setItemInInventory(int x, int y, int ID){
        inventoryNumbers[y][x] = ID;
    }

    public static void removeItemFromInventory(int x, int y){
        inventoryNumbers[y][x] = 0;
    }

    public static int getItemInInventorySlot(int x, int y){
        return inventoryNumbers[y][x];
    }


    public static int getMouseInventorySlot(){
        return mouseInventoryNumber;
    }

    public static void setMouseInventorySlot(int ID){
        mouseInventoryNumber = ID;
    }
}
