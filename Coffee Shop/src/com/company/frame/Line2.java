package com.company.frame;

import javax.swing.*;

public class Line2 extends JPanel
{
    private final JPasswordField textfield;

    public Line2(String labelText, int textWidth)
    {
        JLabel label = new JLabel(labelText);
        textfield = new JPasswordField(textWidth);
        textfield.setEchoChar('*');
        this.add(label);
        this.add(textfield);
    }

    public String getText() { return textfield.getText(); }
}
