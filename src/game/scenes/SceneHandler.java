package game.scenes;

public class SceneHandler {
    private static String currentScene = "MainMenu";


    public static String getGameScene(){
        return currentScene;
    }
    public static void setGameScene(String newScene){
        currentScene = newScene;
    }
}
