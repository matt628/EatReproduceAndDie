import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimalJudgementPanel extends JFrame { //SHOWS FRAME FOR ANIMAL JUDGEMENT
    JLabel aniamalGenome = new JLabel();
    JLabel animalDetails = new JLabel();
    JTextField days = new JTextField();
    JButton chceckHistory = new JButton("Jude carefully");
    Animal animal;

    AnimalJudgementPanel(Animal animal){
        this.animal = animal;
    //BASIC INFORMATION SETUP
        String tmp=null;
        if(animal.getDeathDay() < 0)  tmp = "Still bothered";
        else tmp = String.valueOf(animal.getDeathDay());
        this.aniamalGenome.setText("Genome: ".concat(animal.getGenome().toString()).
                concat(" Eternal happy since: ").concat(tmp));
        days.setText("Judge till day: ");

    //BUTTON SETUP
        chceckHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    judgeCarefully();
                } catch (NumberFormatException ex){
                    days.setText("Provide valid day argument.");
                }
            }
        });

    //LAYOUT
        setVisible(true);
        setLayout(new GridBagLayout());
        setSize(400,200);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 0;
        add(aniamalGenome, gc);

        gc.gridy = 1;
        add(days, gc);

        gc.gridy = 2;
        add(chceckHistory,gc);

        gc.gridy = 3;
        add(animalDetails, gc);


    }

    public void judgeCarefully() throws NumberFormatException{
        int days = Integer.parseInt(this.days.getText());
        String text = "Total children number: ".concat(String.valueOf(animal.countChildrenTillDay(days))).
                 concat(" Total offspring number: ").concat(String.valueOf(animal.countOffspringTillDay(days)));
        animalDetails.setText(text);
    }


}
