package com.pascalx.databaseDocer.View;

import com.pascalx.databaseDocer.entity.TableInfo;

import java.util.List;

import javax.swing.*;

/**
 * Created by yanghui10 on 2016/8/19.
 */
public class MainFrame {

    public static JFrame frame=new JFrame();

    private static InfoPanel infoPanel;
    private static ConnectPanel connectPanel;

    public static void showInfoPanel(List<TableInfo> showInfos){
        frame.getContentPane().setVisible(false);
        infoPanel=new InfoPanel(showInfos);
        frame.setContentPane(infoPanel);
    }
    public static void showExportingInfo(int count,String info){
        if(infoPanel!=null){
            infoPanel.showExportingInfo(count,info);
        }
    }

    public static void showError(Exception e){
        if(infoPanel!=null){
            infoPanel.showError(e);;
        }
    }

    public static void starup(){

        frame.setTitle("数据库设计文档自动生成器");
        frame.setVisible(true);
        frame.setBounds(500,100,400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setVisible(false);
        connectPanel=new ConnectPanel();
        frame.setContentPane(connectPanel);
    }
    public static void main(String[] args) {
        starup();
    }

}
