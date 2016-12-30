package com.jd.databaseDocer.View;

import com.jd.databaseDocer.entity.ColumnInfo;
import com.jd.databaseDocer.entity.TableInfo;
import com.jd.databaseDocer.service.DocProduce;
import com.jd.databaseDocer.service.DocProduceImpl;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

/**
 * Created by yanghui10 on 2016/8/20.
 */
public class InfoPanel extends JPanel {

    private DocProduce docer=new DocProduceImpl();
    private List<TableInfo> tableInfos;

    private JTextField pathInput=new JTextField();
    private JLabel pathLabel=new JLabel("导出路径:",JLabel.RIGHT);

//    private JFileChooser fileChooser=new JFileChooser();
    private JLabel successInfoLabel=new JLabel("",JLabel.CENTER);
    private JTextArea infoArea=new JTextArea();

    private JButton exportButton=new JButton("开始生成");;

    public InfoPanel(List<TableInfo> tableInfos){
        this.tableInfos=tableInfos;
        initPanel();
        showTablesInfos();
        //setBackground(Color.gray);
    }
    public void initPanel(){


        this.setLayout(null);
//      /  this.setBounds(frame.getHeight()/2,frame.getWidth()/2,400,400);

        //this.setBackground(Color.black);//±»JPanel¸²¸ÇÁË£¬Ã»ÓÐÐ§¹û£¬ºÙºÙ
        this.setBounds(160,260, 60, 30);
//        exportButton.setFont(new Font("微软雅黑",Font.BOLD,13));
        exportButton.setBackground(Color.cyan);
        exportButton.setBorder(new Border() {
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

        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("开始导出.........");
                try {
                    long startTime=System.currentTimeMillis();
                    docer.export(tableInfos, pathInput.getText());
                    showExportedSuccess( System.currentTimeMillis()-startTime);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    MainFrame.showError(e1);
                }


            }
        });

        successInfoLabel.setBounds(10,10,380,30);
        successInfoLabel.setFont(new Font("微软雅黑",Font.BOLD,13));
        successInfoLabel.setForeground(Color.RED);

        infoArea.setBounds(10,50,350,200);
        JScrollPane scrollPane=new JScrollPane(infoArea);
        scrollPane.setBounds(10,50,350,200);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        exportButton.setBounds(160,320,80,30);


        pathInput.setBounds(75,270,250,30);
        pathInput.setText("e://test.xls");
        pathLabel.setBounds(10,270,60,30);

        this.add(successInfoLabel);
        this.add(scrollPane);
        this.add(exportButton);
        this.add(pathInput);
        this.add(pathLabel);


    }

    public void showExportingInfo(int count,String info){
        successInfoLabel.setText("正在生成第"+count+"个张表结构数据sheet...");
        exportButton.setText("正在生成...");
        exportButton.setEnabled(false);
        infoArea.setText(info);
    }
    public void showExportedSuccess(Long costTime){
        successInfoLabel.setText("导出成功！耗时："+costTime+"ms");
        exportButton.setText("导出完成");
    }

    public void showSuccess(int tableCount){
        successInfoLabel.setText("连接成功！共"+tableCount+"张表结构数据需要生成...");
    }
    public void showInfo(String info){
        infoArea.setText(info);
    }
    public void showError(Exception e){
        successInfoLabel.setText("导出异常!");
        infoArea.setText(e.getMessage());
        Writer sw=new StringWriter();
        PrintWriter pw=new PrintWriter(sw);
        e.printStackTrace(pw);
        infoArea.setText(sw.toString());

    }
    public void showTablesInfos() {
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (TableInfo table : tableInfos) {
            count++;
            sb.append(table);
        }
        this.showInfo(sb.toString());
        this.showSuccess(count);
    }
}
