package com.company.frame;

import javax.swing.*;

public class Line extends JPanel
{
    private final JTextField textfield;

    public Line(String labelText, int textWidth)
    {
        JLabel label = new JLabel(labelText);
        textfield = new JTextField(textWidth);
        this.add(label);
        this.add(textfield);
    }

    public String getText() {
        return textfield.getText();
    }
}
