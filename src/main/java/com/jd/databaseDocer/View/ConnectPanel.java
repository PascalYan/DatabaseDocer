package com.jd.databaseDocer.View;

import com.jd.databaseDocer.Dao.Mysql;
import com.jd.databaseDocer.entity.DBInfo;
import com.jd.databaseDocer.entity.TableInfo;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by yanghui10 on 2016/8/19.
 */
public class ConnectPanel extends JPanel{

    private JLabel passLabel=new JLabel("password:",JLabel.RIGHT);
    private JLabel dbUrlLabel=new JLabel("db_url:",JLabel.RIGHT);
    private JLabel userLabel=new JLabel("user:",JLabel.RIGHT);
    private JLabel dbNameLabel=new JLabel("db_name:",JLabel.RIGHT);

    private JTextField urlInput=new JTextField();
    private JTextField userInput=new JTextField();
    private JTextField passInput=new JPasswordField();
    private JTextField dbNameInput=new JTextField();

    private JButton connButton=new JButton("连接");

    private JTextArea errorInfoArea=new JTextArea();

    public ConnectPanel(){
        initPanel();
        //setBackground(Color.gray);
    }
    public void initPanel(){

        this.setLayout(null);
        this.setBounds(0,0, 400, 400);
//      /  this.setBounds(frame.getHeight()/2,frame.getWidth()/2,400,400);

        //this.setBackground(Color.black);
        connButton.setBounds(160,230, 60, 30);
//        connButton.setFont(new Font("微软雅黑",Font.BOLD,13));
        connButton.setBackground(Color.cyan);
        connButton.setBorder(new Border() {
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(Color.BLACK);
                g.drawRoundRect(0, 0, c.getWidth()-1, c.getHeight()-1, 10, 10);
            }

            public Insets getBorderInsets(Component c) {
                return new Insets(0,0,0,0);
            }

            public boolean isBorderOpaque() {
                return false;
            }
        });

        connButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    List<TableInfo> tableInfos=Mysql.connectDB(new DBInfo(dbNameInput.getText(),urlInput.getText(),passInput.getText(),userInput.getText()));
                    MainFrame.showInfoPanel(tableInfos);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    showCnnectError(e1);
                }
            }
        });

        this.add(connButton);
        //this.add(connButton);
        //this.add(startButton);
        dbUrlLabel.setBounds(10,100,60,30);
        urlInput.setBounds(75,100,295,30);
        urlInput.setText("jdbc:mysql://172.24.7.89:3306/jr_geious?useUnicode=true&characterEncoding=utf8");
//        urlInput.setText("jdbc:mysql://localhost:3306/world");

        dbNameLabel.setBounds(10,140,60,30);
        dbNameInput.setBounds(75,140,100,30);
        dbNameInput.setText("jr_geious");

//        userLabel.setOpaque(true);
//        userLabel.setBackground(Color.CYAN);
        userLabel.setBounds(10,180,60,30);
        userInput.setBounds(75,180,100,30);
        userInput.setText("mysql");

        passLabel.setBounds(185,180,80,30);
        passInput.setBounds(270,180,100,30);
        passInput.setText("123456");


        this.add(dbUrlLabel);
        this.add(urlInput);
        this.add(dbNameLabel);
        this.add(dbNameInput);
        this.add(userLabel);
        this.add(userInput);
        this.add(passLabel);
        this.add(passInput);


    }
    public void showCnnectError(Exception e){
        errorInfoArea.setBounds(10,300,360,50);
        errorInfoArea.setFont(new Font("微软雅黑",Font.BOLD,13));
        errorInfoArea.setForeground(Color.RED);
        errorInfoArea.setText("信息有误，连接失败！\n"+e.getMessage());
        JScrollPane scrollPane=new JScrollPane(errorInfoArea);
        scrollPane.setBounds(10,270,360,80);
//        this.add(errorInfoArea);
        this.add(scrollPane);
    }

}
