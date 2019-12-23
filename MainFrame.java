import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame { //MAIN GUI
    private AbstractWorldMap field;
    private JLabel mapDrawer = new JLabel();
    private DetalisPanel detalisPanel = new DetalisPanel();
    private ButtonPanel buttonPanel;


    public MainFrame(AbstractWorldMap field) {
        this.field = field;
        this.buttonPanel = new ButtonPanel(field, this);

        //SET LAYOUT
        Font font = new Font(Font.MONOSPACED,Font.PLAIN ,12);
        mapDrawer.setFont(font);
        setLayout(new GridBagLayout());
        setVisible(true);
        setSize(1400,1200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor =GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        add(buttonPanel, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        add(detalisPanel,gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weighty = 18;
        gc.gridx = 0;
        gc.gridy = 3;
        add(mapDrawer, gc);



    }

    public void actualiseStatistics(String text){
        detalisPanel.actualiseDailyStatistics(text);
    }

    public void changeText(String text) throws InterruptedException {
        text = "<html>" + text.replaceAll("<","&lt;").
                replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>";
        mapDrawer.setText(text);
    }

}
