package acme;

import BasicIO.ASCIIDataFile;
import BasicIO.ASCIIOutputFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class AcmeDistributingV2 extends JFrame{

    private JPanel panel_checked, panel_unchecked, panel_buttons;
    private JLabel label_date, label_time, label_orderNum, label_itemNum, label_quantity, label_address;
    private JTextField field_date, field_time,field_orderNum, field_itemNum, field_quantity;
    private JTextArea area_address;
    private JButton button_ok, button_quit;

    private boolean allValid = true;

    private ASCIIDataFile data;
    private Queue<Order> orderQueue = new LinkedList<Order>();

    public static void main(String[] args){
        new AcmeDistributingV2();
    }

    public AcmeDistributingV2(){

        try{
            data = new ASCIIDataFile();
        }catch(NullPointerException e){
            System.err.println("User choose cancel when read file.");
            System.exit(-1);
        }

        makeQueue();

        buildForm();
        showDataFromQueue();
        setVisible(true);


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
            tmp = new Order(Order.makeDate(date),Order.makeTime(time), Order.makeString(orderNum),Order.makeString(itemNum),Order.makeInteger(quantity),address);
        }catch(ParseException e){
            System.err.println(e.getMessage());
            return;
        }
        orderQueue.offer(tmp);

    }

    private boolean showDataFromQueue(){
        if(!orderQueue.isEmpty()){
            Order tmp = orderQueue.poll();

            String date = tmp.getDateString();
            String time = tmp.getTimeString();
            String orderNUm = tmp.getOrderNum();
            String itemNum = tmp.getItemNum();
            String quantity = tmp.getQuantityString();
            String address = tmp.getAddress();

            field_date.setText(date);
            field_time.setText(time);
            field_orderNum.setText(orderNUm);
            field_itemNum.setText(itemNum);
            field_quantity.setText(quantity);
            area_address.setText(address);

            allValid=true;

            label_date.setForeground(Color.BLACK);
            label_time.setForeground(Color.BLACK);
            label_orderNum.setForeground(Color.BLACK);
            label_itemNum.setForeground(Color.BLACK);
            label_quantity.setForeground(Color.BLACK);

            return true;
        }
        System.err.println("Order Queue is empty.");
        return false;
    }

    private void makeQueue(){
        Date date;
        Date time;
        String orderNum;
        String itemNum;
        Number quantity;
        String address;
        try{
            while (true) {
                String tmp = data.readString();
                if(data.isEOF()) break;
                date = Order.makeDate(tmp);
                time = Order.makeTime(data.readString());
                orderNum = Order.makeString(data.readString());
                itemNum = Order.makeString(data.readString());
                quantity = Order.makeInteger(data.readString());
                address = readAddress();
                System.out.println("add to queue");
                orderQueue.add(new Order(date,time,orderNum,itemNum,quantity,address));
            }
        }catch(ParseException e){            //when Format is invalid
            System.err.println(e.getMessage());
        }catch(IllegalStateException e2){    //when queue is full
            System.err.println("IllegalStateException: Queue is full");
            throw e2;
        }
    }
    private String readAddress(){
        String result="";
        while(true){
            Character c = data.readC();
            System.out.println("ASCII: "+(int)c);
            if((int)c==10) break;
            if((int)c==92){
                Character next = data.readC();
                System.out.println("ASCII: "+(int)next);
                if((int)next==110) c=(char)10;
            }
            result += c;
        }
        return result;
    }

    private void buildForm(){
        initPanels();

        addComponentsToChecked();
        addComponentsToUnchecked();
        addComponentsToButton();

        add("North", panel_checked);
        add("Center", panel_unchecked);
        add("South", panel_buttons);

        addToListener();

        pack();
        setPreferredSize(new Dimension(760,getHeight()));   //height: auto, width: 860
        pack();
    }

    private void initPanels(){
        panel_checked = new JPanel();
        panel_unchecked = new JPanel();
        panel_buttons = new JPanel();

        panel_checked.setLayout(new GridLayout(0,4));
        panel_unchecked.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel_buttons.setLayout(new FlowLayout());
    }

    private void addComponentsToChecked(){


        label_date = new JLabel("Date");
        label_time = new JLabel("Time");
        label_orderNum = new JLabel("Order #");
        label_itemNum = new JLabel("Item #");
        label_quantity = new JLabel("Quantity");

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

    private void addComponentsToUnchecked(){
        label_address = new JLabel("Address");
        area_address = new JTextArea(5,50);

        panel_unchecked.add(label_address);
        panel_unchecked.add(area_address);
    }

    private void addComponentsToButton(){

        button_ok = new JButton("OK");
        button_quit = new JButton("Quit");

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

    class QuitButtonListener implements ActionListener{

        private ASCIIOutputFile output;
        @Override
        public void actionPerformed(ActionEvent e) {
            addDataToQueue();
            try{
                output = new ASCIIOutputFile();
            }catch(NullPointerException e1){
                System.err.println("User choose cancel when saving");
                System.exit(-1);
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

    class OkButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            addDataToQueue();
            showDataFromQueue();
        }

    }
    private boolean dateValid=true,timeValid=true,orderNumValid=true,itemNumValid=true,quantityValid = true;
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
            }else if(focused == field_orderNum||focused == field_itemNum){
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
//            System.out.println("Focused text is: "+text);
            allValid = dateValid && timeValid && orderNumValid && itemNumValid && quantityValid;
            if(!allValid) button_ok.setEnabled(false);
            else button_ok.setEnabled(true);
        }
    }


}