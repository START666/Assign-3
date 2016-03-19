package acme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by Xuhao Chen on 16/3/4.
 */
public class AcmeDistributingV2 extends JFrame implements ItemListener{

    private JTextField date,time,orderNum,itemNum,quantity;
    private JLabel dateLabel,timeLabel,orderNumLabel,itemNumLabel,quantityLabel;
    private JTextArea address;
    private JButton ok,quit;

    public AcmeDistributingV2(){
        super("Acme Distributing");
        JPanel fieldPanel = new JPanel(new GridLayout(3,2));
        JPanel addressPanel = new JPanel(new GridLayout(1,2));

        date = new JTextField(20);
        time = new JTextField(20);
        orderNum = new JTextField(20);

        dateLabel = new JLabel("Date");
        dateLabel.setLabelFor(date);
        timeLabel = new JLabel("Time");
        timeLabel.setLabelFor(time);
        orderNumLabel = new JLabel("Order#");
        orderNumLabel.setLabelFor(orderNum);

        fieldPanel.add(dateLabel,BorderLayout.NORTH);
        fieldPanel.add(date);
        fieldPanel.add(timeLabel);
        fieldPanel.add(time);
        fieldPanel.add(orderNumLabel);
        fieldPanel.add(orderNum);

        add(fieldPanel);
        add(addressPanel);

        validate();

//        add(date);
//        add(time);
    }


    public static void main(String[] args){
        JFrame acme = new AcmeDistributingV2();
        acme.pack();
        acme.setVisible(true);
    }


    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
