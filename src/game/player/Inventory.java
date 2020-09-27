package game.player;

public class Inventory {
    private static String[][] inventoryNames = new String[4][9];
    private static int[][] inventoryNumbers = new int[4][9];

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

    public static int getItemInInventorySlot(int x, int y){
        return inventoryNumbers[y][x];
    }
}
