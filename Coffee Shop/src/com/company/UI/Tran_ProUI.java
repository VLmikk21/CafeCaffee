package com.company.UI;

import com.company.connection.DbAdapter;
import com.company.frame.MyTable;
import com.company.frame.QueryItem;
import com.company.relations.Tran_Pro;
import com.company.relations.Transaction;

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

public class Tran_ProUI extends JFrame {
    private final int COLUMN = 3;
    private final Vector<Vector<String>> dataModel = new Vector<>();
    private final QueryItem customerID = new QueryItem("TRANSACTION_ID：", 10);
    private final QueryItem firstName = new QueryItem("PRODUCT_ID：", 10);
    private final QueryItem lastName = new QueryItem("QUANTITY：", 10);
    private final JButton queryBtn = new JButton("SEARCH");
    private final JButton insertBtn = new JButton("INSERT");
    private final JButton deleteBtn = new JButton("DELETE");
    private final JTextArea textarea = new JTextArea(5, 5);
    private final MyTable table;

    private static DbAdapter dbAdapter;

    public Tran_ProUI(DbAdapter adapter) {
        this.setTitle("Tran_Pro");
        dbAdapter = adapter;

        List<String> TITLES = Arrays.asList(
                "TRANSACTION_ID", "PRODUCT_ID", "QUANTITY");
        Vector<String> titles = new Vector<>(TITLES);
        table = new MyTable(dataModel, titles);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(customerID);
        controlPanel.add(firstName);
        controlPanel.add(lastName);
        controlPanel.add(Box.createRigidArea(new Dimension(1000,0)));
        controlPanel.add(queryBtn);
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
            if (customerID.isSelected()) conditions.add("(TRANSACTION_ID = " + customerID.getText() + ")");
            if (firstName.isSelected()) conditions.add("(PRODUCT_ID = " + firstName.getText() + ")");
            if (lastName.isSelected()) conditions.add("(QUANTITY = '" + lastName.getText() + "')");

            StringBuilder sb = new StringBuilder();
            sb.append("select * from public.\"Tran_Pro\"");
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

        insertBtn.addActionListener(e -> {
            String id = customerID.getText();
            String sname = firstName.getText();
            String ssex = lastName.getText();

            String cmd = "insert into public.\"Tran_Pro\" values (" + id + ", " + sname + ", " +
                    ssex + ");";
            String updateProduct = "UPDATE public.\"Product\" " +
                    " SET COUNT = COUNT - " + ssex +
                    " WHERE PRODUCT_ID = " +  sname + ";";
            textarea.setText(cmd + "\n" + updateProduct);

            try {
                Statement statement = dbAdapter.getConnection().createStatement();
                String sqlInsert = "INSERT INTO public.\"Tran_Pro\" "+
                        "VALUES (" + id + ", " + sname + ", " + ssex +");";

                updateProduct = "UPDATE \"Product\" " +
                        " SET COUNT = COUNT - " + ssex +
                        " WHERE PRODUCT_ID = " +  sname + ";";
                System.out.println(sqlInsert);
                statement.executeUpdate(sqlInsert);
                statement.executeUpdate(updateProduct);

                String customerID = new Transaction(dbAdapter).getCustomerID(id);
                String updateCustomerPoints = "update \"Customer\" set my_points = my_points + "
                        + ssex  + " where customer_id = '" + customerID + "';";
                statement.executeUpdate(updateCustomerPoints);
                statement.close();
                dataModel.add(new Vector<>(Arrays.asList(
                        id, sname, ssex)));


                table.validate();
                table.updateUI();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            String sid = dataModel.get(row).get(0);
            String sql = "delete from \"Tran_Pro\" where customer_id = " + sid + ";";

            textarea.setText(sql);

            try {
                Statement statement = dbAdapter.getConnection().createStatement();
                String sqlDelete = "DELETE FROM \"Tran_Pro\" WHERE TRANSACTION_ID = " + sid + ";";
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





