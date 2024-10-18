package org.alex.repeatfilesscan;

import cn.hutool.core.collection.CollectionUtil;
import org.alex.utils.HashUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

public class RepeatFilesScannerUI {
    private JFrame jFrame;

    public RepeatFilesScannerUI() {
        this.jFrame = new JFrame("重复文件扫描器");
        this.jFrame.setSize(200, 200);
        Container contentPane = this.jFrame.getContentPane();
        JButton chooserDirButton = new JButton("选择扫描文件夹");
        chooserDirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("选择账单表格");
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVlaue = jFileChooser.showOpenDialog(jFrame);
                if (returnVlaue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();
                    if (!selectedFile.isDirectory()) {
                        JOptionPane.showMessageDialog(jFrame, "请选择文件夹");
                        return;
                    }
                    List<File> allFileList = new ArrayList<>();
                    scanAllFilesRecursion(selectedFile, allFileList);
                    if (CollectionUtil.isEmpty(allFileList)) {
                        JOptionPane.showMessageDialog(jFrame, "所选目录下无文件");
                        return;
                    }
                    Map<String, List<String>> repeatFilePathMap = new HashMap<>();
                    for (File file : allFileList) {
                        String md5Str = HashUtils.calculateMD5(file);
                        List<String> orDefault = repeatFilePathMap.getOrDefault(md5Str, new ArrayList<>());
                        orDefault.add(file.getPath());
                        repeatFilePathMap.put(md5Str, orDefault);
                    }
                    Iterator<Map.Entry<String, List<String>>> iterator = repeatFilePathMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, List<String>> next = iterator.next();
                        if (next.getValue().size() <= 1) {
                            iterator.remove();
                        }
                    }
                    if (repeatFilePathMap.size() == 0) {
                        JOptionPane.showMessageDialog(jFrame, "目标目录下无重复文件");
                        return;
                    }else {
                        new RepeatFileReportUI(repeatFilePathMap);
                        jFrame.dispose();
                    }
                }
            }
        });
        contentPane.add(chooserDirButton);
        this.jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setVisible(true);
    }

    private static void scanAllFilesRecursion(File selectedFile, List<File> allFileList) {
        File[] files = selectedFile.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanAllFilesRecursion(file, allFileList);
                }else {
                    allFileList.add(file);
                }
            }
        }
    }
}
