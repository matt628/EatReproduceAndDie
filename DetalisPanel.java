import javax.swing.*;
import java.awt.*;

public class DetalisPanel extends JPanel {//DISPLAY DAILY STATISTICS ABOUT WORLD ON MAIN FRAME
    private JLabel dailyStatistics = new JLabel();
    DetalisPanel(){
        setLayout(new GridBagLayout());
        setVisible(true);

        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.LAST_LINE_END;
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 0;
        add(dailyStatistics, gc);


    }
    void actualiseDailyStatistics(String text){
        dailyStatistics.setText(text);

    }
}
