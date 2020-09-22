package engine;

public class GameEngine {

    private static void input() {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    private static void update(float interval) throws Exception {
        gameLogic.update(interval, mouseInput);
    }

    private static void render() {
        gameLogic.render(window);
        window.update();
    }

    private static void cleanup(){
        gameLogic.cleanup();
    }
}
