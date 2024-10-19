package org.alex.uidemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SwingWorkerProgressBarExample {
    private JFrame frame;
    private JProgressBar progressBar;
    private JButton startButton;
    private Task task;

    public SwingWorkerProgressBarExample() {
        // 创建 JFrame
        frame = new JFrame("SwingWorker 进度条示例");
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
                task = new Task();
                task.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress".equals(evt.getPropertyName())) {
                            int progress = (Integer) evt.getNewValue();
                            progressBar.setValue(progress); // 更新进度条值
                        }
                    }
                });
                task.execute(); // 使用 SwingWorker 的 execute() 方法启动任务
            }
        });

        // 将进度条和按钮添加到框架中
        frame.add(progressBar);
        frame.add(startButton);

        frame.setVisible(true);
    }

    // 使用 SwingWorker 来处理后台任务并更新 UI
    class Task extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() throws Exception {
            // 模拟耗时任务并更新进度
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(50); // 模拟任务耗时
                setProgress(i); // 设置当前进度
            }
            return null;
        }

        @Override
        protected void done() {
            // 任务完成后更新 UI（停止进度条并显示完成文字）
            progressBar.setString("任务完成");
        }
    }

    public static void main(String[] args) {
        // 启动程序
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SwingWorkerProgressBarExample();
            }
        });
    }
}