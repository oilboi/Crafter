package game.player;

public class Inventory {
    private static String[][] inventoryNames = new String[4][9];
    private static int[][] inventoryNumbers = new int[4][9];

    public static void generateRandomInventory(){
        for (int x = 0; x < 9; x++){
            for (int y = 0; y < 4; y++){
                inventoryNumbers[y][x] =(int)(Math.floor(Math.random() * 0)); //23
            }
        }
    }

    public static int getItemInInventorySlot(int x, int y){
        return inventoryNumbers[y][x];
    }
}
