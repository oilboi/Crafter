package engine;

public class Timer {
    private static double lastLoopTime = System.nanoTime();

    public static double timerGetTime(){
        return System.nanoTime();
    }

    public static double timerGetElapsedTime(){
        double time = timerGetTime();
        double elapsedTime = time - lastLoopTime;
        lastLoopTime = time;
        return elapsedTime;
    }

    public static double timerGetLastLoopTime(){
        return lastLoopTime;
    }
}
