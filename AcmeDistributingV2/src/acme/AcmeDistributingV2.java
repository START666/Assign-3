package acme;

import BasicIO.ASCIIOutputFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Queue;


public class AcmeDistributingV2 extends JFrame{

    private JPanel panel_checked, panel_unchecked, panel_buttons;
    private JLabel label_date, label_time, label_orderNum,
            label_itemNum, label_quantity, label_address;
    private JTextField field_date, field_time,field_orderNum,
            field_itemNum, field_quantity;
    private JTextArea area_address;
    private JButton button_ok, button_quit;

    private boolean allValid = true;

    private Queue<Order> orderQueue = new LinkedList<Order>();

    public static void main(String[] args){new AcmeDistributingV2();}

    public AcmeDistributingV2(){
        super("Acme Distributing V2");
        setLayout(new BorderLayout());

        initPanels();

        addComponentsToCheckedPanel();
        addComponentsToUncheckedPanel();
        addComponentsToButtonsPanel();

        add("North", panel_checked);
        add("Center", panel_unchecked);
        add("South", panel_buttons);

        addToListener();

        pack();
        setPreferredSize(new Dimension(760,getHeight()));   //height: auto, width: 860
        pack();
        setVisible(true);

    }

    private void initPanels(){
        panel_checked = new JPanel();
        panel_unchecked = new JPanel();
        panel_buttons = new JPanel();

        panel_checked.setLayout(new GridLayout(0,4));
        panel_unchecked.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel_buttons.setLayout(new FlowLayout());
    }

    private void addComponentsToCheckedPanel(){

        label_date = new JLabel("Date");
        label_time = new JLabel("Time");
        label_orderNum = new JLabel("Order #");
        label_itemNum = new JLabel("Item #");
        label_quantity = new JLabel("Quantity");

        label_date.setForeground(Color.RED);
        label_time.setForeground(Color.RED);
        label_orderNum.setForeground(Color.RED);
        label_itemNum.setForeground(Color.RED);
        label_quantity.setForeground(Color.RED);

        field_date = new JTextField();
        field_time = new JTextField();
        field_orderNum = new JTextField();
        field_itemNum = new JTextField();
        field_quantity = new JTextField();

        panel_checked.add(label_date);
        panel_checked.add(field_date);
        panel_checked.add(label_time);
        panel_checked.add(field_time);
        panel_checked.add(label_orderNum);
        panel_checked.add(field_orderNum);
        panel_checked.add(label_itemNum);
        panel_checked.add(field_itemNum);
        panel_checked.add(label_quantity);
        panel_checked.add(field_quantity);

    }

    private void addComponentsToUncheckedPanel(){
        label_address = new JLabel("Address");
        area_address = new JTextArea(5,50);

        panel_unchecked.add(label_address);
        panel_unchecked.add(area_address);
    }

    private void addComponentsToButtonsPanel(){

        button_ok = new JButton("OK");
        button_quit = new JButton("Quit");

        button_ok.setEnabled(false);
        panel_buttons.add(button_ok);
        panel_buttons.add(button_quit);
    }

    private void addToListener(){
        field_date.addFocusListener(new checkedFieldListener());
        field_time.addFocusListener(new checkedFieldListener());
        field_orderNum.addFocusListener(new checkedFieldListener());
        field_itemNum.addFocusListener(new checkedFieldListener());
        field_quantity.addFocusListener(new checkedFieldListener());

        button_ok.addActionListener(new OkButtonListener());
        button_quit.addActionListener(new QuitButtonListener());
    }

    private void addDataToQueue(){
        String date = field_date.getText();
        String time = field_time.getText();
        String orderNum = field_orderNum.getText();
        String itemNum = field_itemNum.getText();
        String quantity = field_quantity.getText();
        String address = area_address.getText();
        Order tmp;
        try{
            tmp = new Order(Order.makeDate(date),Order.makeTime(time),
                    Order.makeString(orderNum),Order.makeString(itemNum),
                    Order.makeInteger(quantity),address);
        }catch(ParseException e){
            System.err.println(e.getMessage());
            return;
        }
        orderQueue.offer(tmp);
    }

    class OkButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            addDataToQueue();
            field_date.setText("");
            field_time.setText("");
            field_orderNum.setText("");
            field_itemNum.setText("");
            field_quantity.setText("");
            area_address.setText("");

            label_date.setForeground(Color.RED);
            label_time.setForeground(Color.RED);
            label_orderNum.setForeground(Color.RED);
            label_itemNum.setForeground(Color.RED);
            label_quantity.setForeground(Color.RED);

            button_ok.setEnabled(false);
        }

    }

    class QuitButtonListener implements ActionListener{
        private ASCIIOutputFile output;

        @Override
        public void actionPerformed(ActionEvent e) {
            while (true){
                try{
                    output = new ASCIIOutputFile();
                    break;
                }catch(NullPointerException e1){
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Are you sure want to close without saving?",
                            "Close without saving",JOptionPane.YES_NO_CANCEL_OPTION);

                    if(confirm==0) System.exit(-1);
                    if(confirm==2) return;
                }
            }

            while(!orderQueue.isEmpty()){
                Order order = orderQueue.poll();
                output.writeString(order.getDateString());
                output.writeString(order.getTimeString());
                output.writeString(order.getOrderNum());
                output.writeString(order.getItemNum());
                output.writeString(order.getQuantityString());
                output.writeString(Order.Type.getAddressString(order));
                output.newLine();

            }

            System.exit(0);
        }

    }


    private boolean dateValid=false,timeValid=false,
            orderNumValid=false,itemNumValid=false,quantityValid = false;
    class checkedFieldListener implements FocusListener{


        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField focused = (JTextField) e.getSource();
            String text = focused.getText();
            if(focused == field_date){
                if(!Order.checkDateValid(text)){
                    allValid=false;
                    dateValid=false;
                    label_date.setForeground(Color.RED);
                }else{
                    dateValid=true;
                    label_date.setForeground(Color.BLACK);
                }
            }else if(focused == field_time){
                if(!Order.checkTimeValid(text)){
                    allValid=false;
                    timeValid=false;
                    label_time.setForeground(Color.RED);
                }else{
                    timeValid = true;
                    label_time.setForeground(Color.BLACK);
                }
            }else if(focused == field_quantity){
                if(!Order.checkInteger(text)){
                    allValid=false;
                    quantityValid=false;
                    label_quantity.setForeground(Color.RED);
                }else{
                    quantityValid = true;
                    label_quantity.setForeground(Color.BLACK);
                }
            }else if(focused == field_orderNum || focused == field_itemNum){
                if(!Order.checkStringValid(text)){
                    allValid=false;
                    if(focused == field_orderNum){
                        orderNumValid=false;
                        label_orderNum.setForeground(Color.RED);
                    }else{
                        itemNumValid=false;
                        label_itemNum.setForeground(Color.RED);
                    }
                }else{
                    if(focused == field_orderNum){
                        orderNumValid = true;
                        label_orderNum.setForeground(Color.BLACK);
                    }else{
                        itemNumValid=true;
                        label_itemNum.setForeground(Color.BLACK);
                    }
                }
            }else{
                System.err.println("Unknown JTextField");
                return;
            }
            allValid = dateValid && timeValid
                    && orderNumValid && itemNumValid && quantityValid;
            if(!allValid) button_ok.setEnabled(false);
            else button_ok.setEnabled(true);
        }
    }


}