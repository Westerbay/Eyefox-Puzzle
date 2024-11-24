package games.wester.westerlib.core;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Wester
 */
public class TimerTest {

    private Timer timer;
    private boolean endTaskDone;

    @Test
    public void limit_timer_on_stop_should_not_be_running() {
        timer = new TimerLimit(() -> {}, 1000);
        timer.start();
        assertTrue(timer.isRunning());
        timer.stop();
        assertFalse(timer.isRunning());
    }

    @Test
    public void indefinite_timer_on_stop_should_not_be_running() {
        timer = new TimerIndefinite(() -> {}, 1000);
        timer.start();
        assertTrue(timer.isRunning());
        timer.stop();
        assertFalse(timer.isRunning());
    }

    @Test
    public void limit_timer_end_task_should_be_done_at_the_end() {
        endTaskDone = false;
        timer = new TimerLimit(() -> {}, 1000);
        timer.setEndTask(() -> endTaskDone = true);
        assertFalse(endTaskDone);
        timer.start();
        timer.stop();
        assertTrue(endTaskDone);
    }

    @Test
    public void indefinite_timer_end_task_should_be_done_at_the_end() {
        endTaskDone = false;
        timer = new TimerIndefinite(() -> {}, 1000);
        timer.setEndTask(() -> endTaskDone = true);
        assertFalse(endTaskDone);
        timer.start();
        timer.stop();
        assertTrue(endTaskDone);
    }

}
