package org.sol.script.grenwall.util;

import org.sol.script.grenwall.OmniGrenwalls;

import javax.swing.*;
import java.awt.*;

public class GrenwallGui extends JFrame {
    public JButton startButton;
    public JComboBox<TeleportMethod.Method> teleportMethodBox;
    public JComboBox<OmniGrenwalls.Setup> setupBox;

    public GrenwallGui() {
        initComponents();
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel();
        startButton = new JButton();
        JScrollPane scrollPane2 = new JScrollPane();
        JTextPane textPane1 = new JTextPane();
        JPanel panel1 = new JPanel();
        JLabel label1 = new JLabel();
        teleportMethodBox = new JComboBox<TeleportMethod.Method>();
        JLabel label2 = new JLabel();
        setupBox = new JComboBox<OmniGrenwalls.Setup>();

        setTitle("OmniGrenwalls Options");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPanel.setLayout(new BorderLayout());

        startButton.setText("START");
        contentPanel.add(startButton, BorderLayout.SOUTH);

        textPane1.setText("Welcome to OmniGrenwalls!\n" +
                "Select your preferred options below and start!\n" +
                "Start at location/bank with inventory ready.");
        textPane1.setEditable(false);
        scrollPane2.setViewportView(textPane1);

        contentPanel.add(scrollPane2, BorderLayout.NORTH);

        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 75, 15));

        label1.setText("Teleportation Method:");
        panel1.add(label1);

        teleportMethodBox.setModel(new DefaultComboBoxModel<TeleportMethod.Method>(TeleportMethod.Method.values()));
        panel1.add(teleportMethodBox);

        label2.setText("Box Setup:");
        panel1.add(label2);

        setupBox.setModel(new DefaultComboBoxModel<OmniGrenwalls.Setup>(OmniGrenwalls.Setup.values()));

        panel1.add(setupBox);

        contentPanel.add(panel1, BorderLayout.CENTER);
        contentPane.add(contentPanel, BorderLayout.CENTER);
        setSize(245, 250);
        setLocationRelativeTo(getOwner());
    }
}