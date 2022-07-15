package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumDisplayTimer extends NumericDisplay {

    private int timeCount;
    private boolean running;

    public NumDisplayTimer() {
        super();
        running = false;
        timeCount = 0;
        clearTimer();
    }

    public void clearTimer() {
        running = false;
        timeCount = 0;
        setNums();
    }

    public void startTimer() {
        running = true;
        ActionListener timerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    timeCount++;
                    setNums();
                }
            }
        };
        new Timer(1000, timerListener).start();
    }

    @Override
    public void setNums() {
        if (timeCount > 999)
            return;
        setNumsFromInt(timeCount);
    }
}
