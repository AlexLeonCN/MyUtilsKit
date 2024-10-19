package org.alex.uidemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgressBarExample {
    private JFrame frame;
    private JProgressBar progressBar;
    private JButton startButton;

    public ProgressBarExample() {
        // 创建 JFrame
        frame = new JFrame("进度条示例");
        frame.setSize(400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // 创建 JProgressBar
        progressBar = new JProgressBar(0, 100); // 设置最小值为0，最大值为100
        progressBar.setValue(0); // 初始值为0
        progressBar.setStringPainted(true); // 显示进度百分比

        // 创建启动按钮
        startButton = new JButton("开始任务");

        // 按钮事件处理
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 启动任务
                Task task = new Task();
                task.start();
            }
        });

        // 将进度条和按钮添加到框架中
        frame.add(progressBar);
        frame.add(startButton);

        frame.setVisible(true);
    }

    // 模拟耗时任务的内部类
    class Task extends Thread {
        public void run() {
            for (int i = 0; i <= 100; i++) {
                try {
                    // 模拟任务执行时间
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 更新进度条的值
                progressBar.setValue(i);
            }
        }
    }

    public static void main(String[] args) {
        // 启动程序
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProgressBarExample();
            }
        });
    }
}