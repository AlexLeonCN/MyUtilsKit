package org.alex.billstransfer.ui;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.csv.*;
import org.alex.billstransfer.handler.AlipayBillsTransferHandler;
import org.alex.billstransfer.handler.StandardBillObt;
import org.alex.billstransfer.handler.WechatBillsTransferHandler;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class BillsTransferUI {
    private JFrame jFrame = new JFrame();

    private JRadioButton alipayBillType = new JRadioButton("支付宝");
    private JRadioButton wechatBillType = new JRadioButton("微信");

    private String filePath;

    private String outputPath;

    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private String[] headers = new String[]{"日期","时间","金额","标签","账户","备注","分类","子分类"};


    public void init() {
        jFrame.setSize(300,300);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        Container contentPane = jFrame.getContentPane();
        contentPane.setLayout(null);
        JLabel jLabel = new JLabel("请选择源账单类型");
        jLabel.setBounds(75, 20, 150, 20);
        contentPane.add(jLabel);
        ButtonGroup billTypeGroup = new ButtonGroup();
        billTypeGroup.add(alipayBillType);
        billTypeGroup.add(wechatBillType);
        alipayBillType.setSelected(true);
        alipayBillType.setBounds( 50, 50, 100, 20);
        wechatBillType.setBounds(50, 80, 100, 20);
        contentPane.add(alipayBillType);
        contentPane.add(wechatBillType);
        JButton fileChooserButton = new JButton("请选择账单表格");
        fileChooserButton.setBounds(75, 120, 150, 30);
        contentPane.add(fileChooserButton);
        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("选择账单表格");
                int returnVlaue = jFileChooser.showOpenDialog(jFrame);
                if (returnVlaue == JFileChooser.APPROVE_OPTION) {
                    filePath = jFileChooser.getSelectedFile().getPath();
                }
                if (StringUtils.isBlank(filePath)) {
                    JOptionPane.showMessageDialog(null, "请选择账单文件");
                    return;
                }else {
                    outputPath = jFileChooser.getSelectedFile().getParent() + File.separator +
                            "StandardBills_" + dateFormat.format(new Date()) + ".csv";
                }
            }
        });
        JButton transferButton = new JButton("开始转换");
        transferButton.setBounds(100, 200, 100, 30);
        contentPane.add(transferButton);
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (StringUtils.isBlank(filePath)) {
                    JOptionPane.showMessageDialog(null, "请选择账单文件");
                    return;
                }
                String charSetStr = "UTF-8";
                if (alipayBillType.isSelected()) {
                    charSetStr = "GBK";
                }
                if (wechatBillType.isSelected()) {
                    charSetStr = "UTF-8";
                }
                try (FileInputStream fileInputStream = new FileInputStream(filePath);
                     InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charSetStr);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    List<List<String>> csvData = new ArrayList<>();
                    CsvReader csvReader = CsvUtil.getReader(bufferedReader);
                    Iterator<CsvRow> iterator = csvReader.iterator();
                    while (iterator.hasNext()) {
                        csvData.add(iterator.next().getRawList());
                    }
                    if (CollectionUtil.isEmpty(csvData)) {
                        JOptionPane.showMessageDialog(null, "文件中无数据");
                        return;
                    }else {
                        if (alipayBillType.isSelected()) {
                            AlipayBillsTransferHandler alipayBillsTransferHandler = new AlipayBillsTransferHandler();
                            List<StandardBillObt> standardBillObtList = alipayBillsTransferHandler.handle(csvData);
                            if (CollectionUtil.isEmpty(standardBillObtList)) {
                                JOptionPane.showMessageDialog(null, "表格无数据或格式异常");
                                return;
                            }
                            CsvWriter writer = CsvUtil.getWriter(outputPath, StandardCharsets.UTF_8);
                            writer.writeHeaderLine(headers);
                            for (StandardBillObt standardBillObt : standardBillObtList) {
                                writer.writeLine(standardBillObt.toStringArr());
                            }
                            writer.flush();
                            JOptionPane.showMessageDialog(null, "转换成功");
                        } else if (wechatBillType.isSelected()) {
                            WechatBillsTransferHandler wechatBillsTransferHandler = new WechatBillsTransferHandler();
                            List<StandardBillObt> standardBillObtList = wechatBillsTransferHandler.handle(csvData);
                            if (CollectionUtil.isEmpty(standardBillObtList)) {
                                JOptionPane.showMessageDialog(null, "表格无数据或格式异常");
                                return;
                            }
                            CsvWriter writer = CsvUtil.getWriter(outputPath, StandardCharsets.UTF_8);
                            writer.writeHeaderLine(headers);
                            for (StandardBillObt standardBillObt : standardBillObtList) {
                                writer.writeLine(standardBillObt.toStringArr());
                            }
                            writer.flush();
                            JOptionPane.showMessageDialog(null, "转换成功");
                        }else {
                            JOptionPane.showMessageDialog(null, "源账单类型不能为空");
                        }
                    }
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(null, "转换异常");
                }
            }
        });
        jFrame.setVisible(true);
    }
}
