import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    JLabel label2 = new JLabel();

    public MainFrame() {
        Font font = new Font(Font.MONOSPACED,Font.PLAIN ,12);
        label2.setFont(font);
        add(label2);
        setLayout(new GridLayout(1,1));
        setVisible(true);
        setSize(1000,1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void changeText(String text) throws InterruptedException {
        text = "<html>" + text.replaceAll("<","&lt;").
                replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>";
        label2.setText(text);
        Thread.sleep(1000);
    }

}
