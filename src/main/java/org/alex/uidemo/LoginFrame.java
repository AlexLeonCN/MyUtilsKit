package org.alex.uidemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField usernameField;

    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("登录");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(300, 100);

        JPanel mnPanel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("用户名:");

        JLabel passwordLabel = new JLabel("密码：");

        usernameField = new JTextField(20);

        passwordField = new JPasswordField(20);

        mnPanel.add(usernameLabel);

        mnPanel.add(usernameField);

        mnPanel.add(passwordLabel);

        mnPanel.add(passwordField);

        JButton loginButton = new JButton("登录");

        loginButton.addActionListener(new LoginActionListener());

        mnPanel.add(loginButton);

        JButton registButton = new JButton("注册");

        mnPanel.add(registButton);
        setContentPane(mnPanel);

        setVisible(true);

    }

    private class LoginActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String username = usernameField.getText();

            String password = new String(passwordField.getPassword());
        }

    }

    public static void main(String[] args) {

        new LoginFrame();

    }
}

