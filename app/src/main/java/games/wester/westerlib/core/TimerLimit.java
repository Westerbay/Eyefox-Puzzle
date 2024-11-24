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
public class TimerLimit extends Timer {

    private final int _limit;
    private int _numberOfTasks = 0;

    public TimerLimit(Task task, long delayMilliSecond, int cycleCount) {
        super(task, delayMilliSecond);
        _limit = cycleCount;
    }

    public TimerLimit(Task task, long delayMilliSecond) {
        this(task, delayMilliSecond, 1);
    }

    @Override
    public void run() {
        super.run();
        while (isRunning() && _numberOfTasks < _limit) {
            sleep();
            doTask();
            _numberOfTasks ++;
        }
        sleep();
        _numberOfTasks = 0;
        stop();
    }

}
