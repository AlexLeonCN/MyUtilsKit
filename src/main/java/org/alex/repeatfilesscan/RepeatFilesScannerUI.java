package org.alex.repeatfilesscan;

import cn.hutool.core.collection.CollectionUtil;
import org.alex.utils.HashUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.*;
import java.util.List;

public class RepeatFilesScannerUI {
    private JFrame jFrame;

    private JProgressBar jProgressBar;

    private Task task;

    private boolean scanningFlag = false;

    public RepeatFilesScannerUI() {
        this.jFrame = new JFrame("重复文件扫描器");
        this.jFrame.setSize(400, 200);
        Container contentPane = this.jFrame.getContentPane();
        contentPane.setLayout(null);
        JButton chooserDirButton = new JButton("选择扫描文件夹");
        chooserDirButton.setBounds(50, 40, 300, 50);
        chooserDirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scanningFlag) {
                    JOptionPane.showMessageDialog(jFrame, "已有扫描任务执行中");
                    return;
                }else {
                    scanningFlag = true;
                }
                jProgressBar.setValue(0);
                jProgressBar.setString(null);
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("选择扫描目录");
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVlaue = jFileChooser.showOpenDialog(jFrame);
                if (returnVlaue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();
                    if (!selectedFile.isDirectory()) {
                        JOptionPane.showMessageDialog(jFrame, "请选择文件夹");
                        return;
                    }
                    // 启动任务
                    task = new RepeatFilesScannerUI.Task(selectedFile);
                    task.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress".equals(evt.getPropertyName())) {
                                int progress = (Integer) evt.getNewValue();
                                jProgressBar.setValue(progress); // 更新进度条值
                            }
                        }
                    });
                    task.execute(); // 使用 SwingWorker 的 execute() 方法启动任务
                }
            }
        });
        contentPane.add(chooserDirButton);
        this.jProgressBar = new JProgressBar(0, 100);
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(true);
        jProgressBar.setBounds(50, 130, 300, 20);
        contentPane.add(jProgressBar);
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

    // 使用 SwingWorker 来处理后台任务并更新 UI
    class Task extends SwingWorker<Void, Void> {
        File selectedFile;
        public Task(File selectedFile) {
            this.selectedFile = selectedFile;
        }

        @Override
        protected Void doInBackground() throws Exception {
            List<File> allFileList = new ArrayList<>();
            scanAllFilesRecursion(selectedFile, allFileList);
            if (CollectionUtil.isEmpty(allFileList)) {
                JOptionPane.showMessageDialog(jFrame, "所选目录下无文件");
                return null;
            }
            Map<String, List<String>> repeatFilePathMap = new HashMap<>();
            for (int i = 0; i < allFileList.size(); i++) {
                File file = allFileList.get(i);
                String md5Str = HashUtils.calculateMD5(file);
                List<String> orDefault = repeatFilePathMap.getOrDefault(md5Str, new ArrayList<>());
                orDefault.add(file.getPath());
                repeatFilePathMap.put(md5Str, orDefault);
                setProgress((i * 100) / (allFileList.size() - 1));
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
            }else {
                new RepeatFileReportUI(repeatFilePathMap);
                jFrame.dispose();
            }
            scanningFlag = false;
            return null;
        }

        @Override
        protected void done() {
            // 任务完成后更新 UI（停止进度条并显示完成文字）
            jProgressBar.setString("扫描完成");
        }
    }
}
