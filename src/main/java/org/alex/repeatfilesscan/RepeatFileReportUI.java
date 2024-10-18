package org.alex.repeatfilesscan;

import cn.hutool.core.io.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RepeatFileReportUI {
    private JFrame jFrame;

    private Map<String, List<String>> repeatFilePathMap; // md5:filePathList

    public RepeatFileReportUI(Map<String, List<String>> repeatFilePathMap) {
        this.repeatFilePathMap = repeatFilePathMap;
        this.jFrame = new JFrame("请确认是否清理重复文件");
        this.jFrame.setSize(400, 400);
        Container contentPane = this.jFrame.getContentPane();
        contentPane.setLayout(null);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : repeatFilePathMap.entrySet()) {
            for (String path : entry.getValue()) {
                stringBuilder.append(path);
                stringBuilder.append(System.lineSeparator());
            }
            stringBuilder.append(System.lineSeparator());
        }

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(new JTextArea(stringBuilder.toString()));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(50,0, 300, 300);
        contentPane.add(scrollPane);

        JButton deleteButton = new JButton("确认删除");
        deleteButton.setBounds(50, 350, 100, 20);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator<Map.Entry<String, List<String>>> iterator = repeatFilePathMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    List<String> value = iterator.next().getValue();
                    for (int i = 1; i < value.size(); i++) {
                        FileUtil.del(value.get(i));
                    }
                }
                JOptionPane.showMessageDialog(jFrame, "删除成功");
                jFrame.dispose();
            }
        });
        JButton cancleButton = new JButton("取消");
        cancleButton.setBounds(250, 350, 100, 20);
        cancleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
            }
        });
        contentPane.add(deleteButton);
        contentPane.add(cancleButton);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.jFrame.setVisible(true);
    }
}
