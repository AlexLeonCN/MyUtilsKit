package org.alex;

import org.alex.billstransfer.ui.BillsTransferUI;
import org.alex.repeatfilesscan.RepeatFilesScannerUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyToolsMainUI {

    private JFrame jFrame = new JFrame("我的工具箱");

    public void init() {
        this.jFrame.setSize(300, 600);
        this.jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.jFrame.setLocationRelativeTo(null);
        Container contentPane = this.jFrame.getContentPane();
        contentPane.setLayout(null); // 绝对布局,我喜欢，可控

        JButton jButton = new JButton("卡片记账账单转换工具"); //创建
        jButton.setBounds(50,20,200,50);
        contentPane.add(jButton);
        //点击这个按钮的时候，弹出一个弹窗
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //弹窗
                new BillsTransferUI().init();
            }
        });

        JButton repeatFilesScannerBotton = new JButton("重复文件扫描器"); //创建
        repeatFilesScannerBotton.setBounds(50,90,200,50);
        contentPane.add(repeatFilesScannerBotton);
        //点击这个按钮的时候，弹出一个弹窗
        repeatFilesScannerBotton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //弹窗
                new RepeatFilesScannerUI();
            }
        });
        this.jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new MyToolsMainUI().init();
    }
}
