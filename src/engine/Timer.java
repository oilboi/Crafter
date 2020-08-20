package engine;

public class Timer {
    private double lastLoopTime;

    public void init(){
        lastLoopTime = getTime();
    }

    public double getTime(){
        return System.nanoTime();
    }

    public double getElapsedTime(){
        double time = getTime();
        double elapsedTime = time - lastLoopTime;
        lastLoopTime = time;
        return elapsedTime;
    }

    public double getLastLoopTime(){
        return lastLoopTime;
    }
}
