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
public class TimerIndefinite extends Timer {

    public TimerIndefinite(Task task, long delayMilliSecond) {
        super(task, delayMilliSecond);
    }

    @Override
    public void run() {
        super.run();
        while (isRunning()) {
            sleep();
            doTask();
        }
    }

}
