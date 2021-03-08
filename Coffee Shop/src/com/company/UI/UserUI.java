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

public class UserUI extends JFrame {
    private final int COLUMN = 5;
    private final Vector<Vector<String>> dataModel = new Vector<>();
    private final QueryItem customerID = new QueryItem("USER_ID：", 10);
    private final QueryItem firstName = new QueryItem("FIRST_NAME：", 10);
    private final QueryItem lastName = new QueryItem("LAST_NAME：", 10);
    private final QueryItem address = new QueryItem("PRIVILEGE：", 10);
    private final QueryItem password = new QueryItem("PASSWORD：", 10);
    private final JButton queryBtn = new JButton("SEARCH");
    private final JButton saveBtn = new JButton("UPDATE");
    private final JButton insertBtn = new JButton("INSERT");
    private final JButton deleteBtn = new JButton("DELETE");
    private final JTextArea textarea = new JTextArea(5, 5);
    private final MyTable table;

    private static DbAdapter dbAdapter;


    public UserUI(DbAdapter adapter) {
        this.setTitle("User");
        dbAdapter = adapter;

        List<String> TITLES = Arrays.asList(
                "USER_ID", "FIRST_NAME", "LAST_NAME", "PRIVILEGE", "PASSWORD");
        Vector<String> titles = new Vector<>(TITLES);
        table = new MyTable(dataModel, titles);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(customerID);
        controlPanel.add(firstName);
        controlPanel.add(lastName);
        controlPanel.add(address);
        controlPanel.add(password);
        controlPanel.add(Box.createRigidArea(new Dimension(1000,0)));
        controlPanel.add(queryBtn);
        controlPanel.add(saveBtn);
        controlPanel.add(insertBtn);
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
            if (customerID.isSelected()) conditions.add("(USER_ID = '" + customerID.getText() + "')");
            if (firstName.isSelected()) conditions.add("(FIRST_NAME = '" + firstName.getText() + "')");
            if (lastName.isSelected()) conditions.add("(LAST_NAME = '" + lastName.getText() + "')");
            if (address.isSelected()) conditions.add("(PRIVILEGE = " + address.getText() + ")");
            if (password.isSelected()) conditions.add("(PASSWORD = " + password.getText() + ")");

            StringBuilder sb = new StringBuilder();
            sb.append("select * from \"user_\"");
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

        saveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            String sid = dataModel.get(row).get(0);
            ArrayList<String> conditions = new ArrayList<>();
            if (firstName.isSelected())
            {
                table.setValueAt(firstName.getText(), row, 1);
                conditions.add("FIRST_NAME = '" + firstName.getText() + "'");
            }
            if (lastName.isSelected())
            {
                table.setValueAt(lastName.getText(), row, 2);
                conditions.add("LAST_NAME = '" + lastName.getText() + "'");
            }
            if (address.isSelected())
            {
                table.setValueAt(address.getText(), row, 3);
                conditions.add("PRIVILEGE = " + address.getText() + "");
            }
            if(password.isSelected())
            {
                table.setValueAt(password.getText(), row, 4);
                conditions.add("PASSWORD = " + password.getText() + "");
            }

            if(conditions.size() == 0) return;

            StringBuilder sb = new StringBuilder();
            sb.append("update \"user_\" set ");
            for(int i = 0; i < conditions.size(); i++)
            {
                sb.append(conditions.get(i));
                if(i != conditions.size() - 1)
                    sb.append(", ");
            }
            sb.append(" where user_id = '").append(sid).append("';");
            String queryString = sb.toString();
            textarea.setText(queryString);

            Statement stmt;
            try {
                stmt = dbAdapter.getConnection().createStatement();
                stmt.executeUpdate(queryString);
                stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            table.validate();
            table.updateUI();
        });


        insertBtn.addActionListener(e -> {
            String id = customerID.getText();
            String sname = firstName.getText();
            String ssex = lastName.getText();
            String sclass = address.getText();
            String word = password.getText();


            String cmd = "insert into \"user_\" values ('" + id + "', '" + sname + "', '" +
                    ssex + "', " + sclass + ", '" + word + "');";
            textarea.setText(cmd);

            try {
                Statement statement = dbAdapter.getConnection().createStatement();
                String sqlInsert = "INSERT INTO \"user_\" (USER_ID, FIRST_NAME, LAST_NAME, PRIVILEGE, PASSWORD) "+
                        "VALUES ('" + id + "', '" + sname + "', '" + ssex + "', " + sclass +", '" + word + "');";
                System.out.println(sqlInsert);
                statement.executeUpdate(sqlInsert);
                statement.close();
                dataModel.add(new Vector<>(Arrays.asList(
                        id, sname, ssex, sclass, word)));


                table.validate();
                table.updateUI();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });


        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            String sid = dataModel.get(row).get(0);
            String sql = "delete from \"user_\" where user_id = '" + sid + "';";


            textarea.setText(sql);

            try {
                Statement statement = dbAdapter.getConnection().createStatement();
                String sqlDelete = "DELETE FROM \"user_\" WHERE USER_ID = '" + sid + "';";
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





