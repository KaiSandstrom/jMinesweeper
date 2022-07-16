package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;

public class NumDisplayTimer extends NumericDisplay {

    private int timeCount;
    private final Timer timer;

    public NumDisplayTimer() {
        super();
        timeCount = 0;
        ActionListener timerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeCount < 1000) {
                    timeCount++;
                    setNumsFromInt(timeCount);
                }
            }
        };
        timer = new Timer(1000, timerListener);
        clearTimer();
    }

    public void clearTimer() {
        haltTimer();
        timeCount = 0;
        setNumsFromInt(timeCount);
    }

    public void startTimer() {
        timer.start();
    }

    public void haltTimer() {
        timer.stop();
    }

}
