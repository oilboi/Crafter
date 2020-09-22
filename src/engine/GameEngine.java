package engine;

import static engine.MouseInput.initMouseInput;
import static engine.Window.*;

public class GameEngine {

    public static final int TARGET_FPS = 75;

    public static final int TARGET_UPS = 60; //TODO: IMPLEMENT THIS PROPERLY

    private static Timer timer;

    public static void initializeGameEngine(String windowTitle, int width, int height, boolean vSync) {
        createWindow(windowTitle, width, height, vSync);
        timer = new Timer();
    }

    public static void runGameEngine(){
        try{
            init();
            gameLoop();
        } catch (Exception excp){
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private static void init() throws Exception{
        initWindow();
        timer.init();
        initMouseInput();
    }

    private static void gameLoop() throws Exception {
        double elapsedTime;
        double accumulator = 0d;
//        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while(running && !windowShouldClose()){

            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= 1_000_000){
                update(0f);
                accumulator -= 1_000_000;
            }

            render();

            if (!isvSync()){
                sync();
            }
        }
    }

    private static void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while(timer.getTime() < endTime){
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie){
            }
        }
    }

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
