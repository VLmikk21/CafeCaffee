package com.company.frame;

import javax.swing.*;
import java.awt.Font;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.util.Vector;

public class MyTable extends JTable {
    public MyTable(Vector data, Vector title) {
        super(data, title);


    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0) return false;
        else return true;
    }
}