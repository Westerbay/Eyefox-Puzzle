package games.wester.westerlib.core;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

/**
 * @author Wester
 */
public abstract class Timer implements Runnable {

    private boolean _running = false;
    private final Task _task;
    private Task _endTask;
    private long _delayMilliSecond;

    public Timer(Task task, long delayMilliSecond) {
        _task = task;
        _endTask = () -> {};
        _delayMilliSecond = delayMilliSecond;
    }

    public void setEndTask(Task endTask) {
        _endTask = endTask;
    }

    public void setDelay(long delayMilliSecond) {
        _delayMilliSecond = delayMilliSecond;
    }

    public boolean isRunning() {
        return _running;
    }

    public void start() {
        _running = true;
        new Thread(this).start();
    }

    public void run() {
        _running = true;
    }

    public void stop() {
        _endTask.run();
        _running = false;
    }

    public void sleep() {
        try {
            Thread.sleep(_delayMilliSecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void doTask() {
        new Thread(_task).start();
    }

}
