package com.company.UI;

import com.company.connection.DbAdapter;
import com.company.frame.MyTable;
import com.company.frame.QueryItem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SessionManagementUI extends JFrame {
    private final int COLUMN = 4;
    private final Vector<Vector<String>> dataModel = new Vector<>();
    private final QueryItem productID = new QueryItem("SESSION_ID：", 10);
    private final QueryItem name = new QueryItem("USER_ID：", 10);
    private final QueryItem count = new QueryItem("LOGIN_TIME：", 10);
    private final QueryItem price = new QueryItem("LOGOUT_TIME：", 22);
    private final JButton queryBtn = new JButton("SEARCH");
    private final JButton deleteBtn = new JButton("DELETE");
    private final JTextArea textarea = new JTextArea(5, 5);
    private final MyTable table;
    private static DbAdapter dbAdapter;

    public SessionManagementUI(DbAdapter adapter) {
        this.setTitle("Session Management");
        dbAdapter = adapter;

        List<String> TITLES = Arrays.asList(
                "SESSION_ID", "USER_ID");
        Vector<String> titles = new Vector<>(TITLES);
        table = new MyTable(dataModel, titles);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(productID);
        controlPanel.add(name);
        controlPanel.add(count);
        controlPanel.add(price);
        controlPanel.add(Box.createRigidArea(new Dimension(1000,0)));
        controlPanel.add(queryBtn);
        controlPanel.add(deleteBtn);
        controlPanel.setPreferredSize(new Dimension(0, 130));

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        tablePanel.add(table.getTableHeader());
        tablePanel.add(new JScrollPane(table));

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(textarea, BorderLayout.NORTH);
        container.add(tablePanel, BorderLayout.CENTER);

        this.add(controlPanel, BorderLayout.NORTH);
        this.add(container, BorderLayout.CENTER);
        this.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.WEST);
        this.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.EAST);
        this.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.SOUTH);
        setActionListener();
    }
    private void setActionListener() {

        queryBtn.addActionListener(e -> {
            ArrayList<String> conditions = new ArrayList<>();
            if (productID.isSelected()) conditions.add("(SESSION_ID = '" + productID.getText() + "')");
            if (name.isSelected()) conditions.add("(USER_ID = '" + name.getText() + "')");

            StringBuilder sb = new StringBuilder();
            sb.append("select * from \"session_management\"");
            int length = conditions.size();
            if (length != 0) sb.append(" where ");
            for (int i = 0; i < length; i++) {
                sb.append(conditions.get(i));
                if (i != length - 1) sb.append(" AND ");
            }
            sb.append(";");
            String queryString = sb.toString();
            textarea.setText(queryString);

            dataModel.clear();
            Statement stmt;
            try {
                stmt = dbAdapter.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(queryString);
                Vector<String> record;
                while (rs.next()) {
                    record = new Vector<>();
                    for (int i = 0; i < COLUMN; i++) {
                        record.add(rs.getString(i + 1));
                    }
                    dataModel.add(record);
                }
                stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            table.validate();
            table.updateUI();
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            String sid = dataModel.get(row).get(0);
            String sql = "delete from \"session_management\" where product_id = '" + sid + "';";

            textarea.setText(sql);

            try {
                Statement statement = dbAdapter.getConnection().createStatement();
                String sqlDelete = "DELETE FROM \"session_management\" WHERE SESSION_ID = '" + sid + "';";
                statement.executeUpdate(sqlDelete);
                ((DefaultTableModel)table.getModel()).removeRow(row);
                statement.close();

                table.validate();
                table.updateUI();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
    }
}





