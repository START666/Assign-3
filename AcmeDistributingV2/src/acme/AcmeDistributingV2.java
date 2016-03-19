package acme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AcmeDistributingV2 extends JFrame implements ItemListener{

    private JPanel checkedPanel,uncheckedPanel,buttonPanel;


    public static void main(String[] args){
        JFrame frame;
        frame = new AcmeDistributingV2();

        frame.pack();
        int height = frame.getHeight();
        frame.setPreferredSize(new Dimension(860,height));
        frame.pack();
        frame.setVisible(true);
    }

    public AcmeDistributingV2(){

        initPanels();

        addComponentsToChecked();
        addComponentsToUnchecked();
        addComponentsToButton();

        add("North",checkedPanel);
        add("Center",uncheckedPanel);
        add("South",buttonPanel);

    }


    private void initPanels(){
        checkedPanel = new JPanel();
        uncheckedPanel = new JPanel();
        buttonPanel = new JPanel();

        checkedPanel.setLayout(new GridLayout(0,4));
        uncheckedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setLayout(new FlowLayout());
    }

    private void addComponentsToChecked(){
        JLabel dateLabel, timeLabel, orderLabel, itemLabel, quantityLabel;
        JTextField dateField, timeField,orderField, itemField, quantityField;

        dateLabel = new JLabel("Date");
        timeLabel = new JLabel("Time");
        orderLabel = new JLabel("Order #");
        itemLabel = new JLabel("Item #");
        quantityLabel = new JLabel("Quantity");

        dateField = new JTextField();
        timeField = new JTextField();
        orderField = new JTextField();
        itemField = new JTextField();
        quantityField = new JTextField();

        checkedPanel.add(dateLabel);
        checkedPanel.add(dateField);
        checkedPanel.add(timeLabel);
        checkedPanel.add(timeField);
        checkedPanel.add(orderLabel);
        checkedPanel.add(orderField);
        checkedPanel.add(itemLabel);
        checkedPanel.add(itemField);
        checkedPanel.add(quantityLabel);
        checkedPanel.add(quantityField);

    }

    private void addComponentsToUnchecked(){
        JLabel addressLabel = new JLabel("Address");
        JTextArea addressArea = new JTextArea(5,60);

        uncheckedPanel.add(addressLabel);
        uncheckedPanel.add(addressArea);
    }

    private void addComponentsToButton(){
        JButton okButton, quitButton;
        okButton = new JButton("OK");
        quitButton = new JButton("Quit");

        buttonPanel.add(okButton);
        buttonPanel.add(quitButton);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}