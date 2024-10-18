package org.alex.questionbank.ui;

import org.alex.questionbank.entity.ExamInfo;
import org.alex.utils.SnowFlakeGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExamListUI {
    private JFrame jFrame = new JFrame("Alex题库工具");
    private JList<ExamInfo> examInfoJList = new JList<>();

    private JScrollPane examInfoScrollPane = new JScrollPane();

    private JButton startExamButton = new JButton("开始刷题");
    private JButton addExamButton = new JButton("添加题库");

    private JButton importExamButton = new JButton("导入题库");

    private JButton exportExamButton = new JButton("导出题库");
    private JButton deleteExamButton = new JButton("删除题库");


    public void init() {
        jFrame.setLayout(new FlowLayout());
        jFrame.setSize(800,800);
        ExamInfo examInfo = new ExamInfo();
        examInfo.setId(SnowFlakeGenerator.nextId());
        examInfo.setName("考试1");
        examInfo.setCategory("数学");
        examInfo.setId(SnowFlakeGenerator.nextId());
        List<ExamInfo> examInfoList = new ArrayList<>();
        examInfoList.add(examInfo);

        JPanel jPanel = buildJPanelByExamInfo(examInfo);
        jPanel.setSize(800, 100);
        jFrame.add(jPanel);
        jFrame.add(addExamButton);
        jFrame.add(deleteExamButton);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }

    private JPanel buildJPanelByExamInfo(ExamInfo examInfo) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.add(new JLabel(examInfo.getName()));
        jPanel.add(new JLabel(examInfo.getCategory()));
        jPanel.add(new Button("删除"));
        jPanel.add(new Button("刷题"));
        jPanel.add(new Button("编辑"));
        jPanel.add(new Button("导出"));
        return jPanel;
    }

    public static void main(String[] args) {
        new ExamListUI().init();
    }
}
