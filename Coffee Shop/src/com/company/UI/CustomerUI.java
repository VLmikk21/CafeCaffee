package com.company.UI;

import com.company.connection.DbAdapter;
import com.company.frame.MyTable;
import com.company.frame.QueryItem;
import com.company.relations.Customer;
import com.company.relations.Product;
import com.company.relations.Tran_Pro;
import com.company.relations.Transaction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerUI extends JFrame {
    private final int COLUMN = 5;
    private final Vector<Vector<String>> dataModel = new Vector<>();
    private final QueryItem customerID = new QueryItem("CUSTOMER_ID：", 10);
    private final QueryItem firstName = new QueryItem("FIRST_NAME：", 10);
    private final QueryItem lastName = new QueryItem("LAST_NAME：", 10);
    private final QueryItem address = new QueryItem("ADDRESS：", 22);
    private final QueryItem myPoints = new QueryItem("MY_POINTS：", 5);
    private final JButton queryBtn = new JButton("SEARCH");
    private final JButton saveBtn = new JButton("UPDATE");
    private final JButton insertBtn = new JButton("INSERT");
    private final JButton deleteBtn = new JButton("DELETE");
    private final JTextArea textarea = new JTextArea(5, 5);
    private final MyTable table;

    private static DbAdapter dbAdapter;


    public CustomerUI(DbAdapter adapter)
    {
        dbAdapter = adapter;
        this.setTitle("Customer");

        List<String> TITLES = Arrays.asList(
                "CUSTOMER_ID", "FIRST_NAME", "LAST_NAME", "ADDRESS", "MY_POINTS");
        Vector<String> titles = new Vector<>(TITLES);
        table = new MyTable(dataModel, titles);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(customerID);
        controlPanel.add(firstName);
        controlPanel.add(lastName);
        controlPanel.add(address);
        controlPanel.add(myPoints);
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
            if (customerID.isSelected()) conditions.add("(CUSTOMER_ID = '" + customerID.getText() + "')");
            if (firstName.isSelected()) conditions.add("(FIRST_NAME = '" + firstName.getText() + "')");
            if (lastName.isSelected()) conditions.add("(LAST_NAME = '" + lastName.getText() + "')");
            if (address.isSelected()) conditions.add("(ADDRESS = '" + address.getText() + "')");
            if (myPoints.isSelected()) conditions.add("(MY_POINTS = '" + myPoints.getText() + "')");

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT *FROM public.\"Customer\"");
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
                rs.close();
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
            String sdept = myPoints.getText();



            String cmd = "insert into \"Costumer\" values ('" + id + "', '" + sname + "', '" +
                    ssex + "', '" + sclass + "', " + sdept + ");";
            textarea.setText(cmd);

            try {
                Statement statement = dbAdapter.getConnection().createStatement();
                String sqlInsert = "INSERT INTO public.\"Customer\" (CUSTOMER_ID, FIRST_NAME, LAST_NAME, ADDRESS, MY_POINTS) "+
                        "VALUES ('" + id + "', '" + sname + "', '" + ssex + "', '" + sclass + "', " + sdept + ");";
                System.out.println(sqlInsert);
                statement.executeUpdate(sqlInsert);
                statement.close();
                dataModel.add(new Vector<>(Arrays.asList(
                        id, sname, ssex, sclass, sdept)));


                table.validate();
                table.updateUI();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
                conditions.add("ADDRESS = '" + address.getText() + "'");
            }
            if (myPoints.isSelected())
            {
                table.setValueAt(myPoints.getText(), row, 4);
                conditions.add("MY_POINTS = " + myPoints.getText() + "");
            }
            if (myPoints.isSelected())
            {
                table.setValueAt(myPoints.getText(), row, 5);
                conditions.add("MY_POINTS = " + myPoints.getText() + "");
                Customer customer = new Customer(dbAdapter);
                int previousPoints = customer.getAvgPoints(sid);
                int different = previousPoints - Integer.parseInt(myPoints.getText());
                if(different > 0 && different % 25 == 0)
                {

                    Transaction transaction = new Transaction(dbAdapter);
                    String transactionID = String.valueOf(transaction.getMaxTransactionID());
                    transaction.insertTransaction(transactionID, sid);

                    Product product = new Product(dbAdapter);
                    Tran_Pro tran_pro = new Tran_Pro(dbAdapter);
                    String productID = product.getCheapestProductID();
                    tran_pro.insertTran_Pro(transactionID, productID, "1");
                    product.updateCount(productID, "1");
                }
            }

            if(conditions.size() == 0) return;

            StringBuilder sb = new StringBuilder();
            sb.append("update Customer set ");
            for(int i = 0; i < conditions.size(); i++)
            {
                sb.append(conditions.get(i));
                if(i != conditions.size() - 1)
                    sb.append(", ");
            }
            sb.append(" where customer_id = '").append(sid).append("';");
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


        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            String sid = dataModel.get(row).get(0);
            String sql = "delete from \"Costumer\" where customer_id = '" + sid + "';";


            textarea.setText(sql);

            try {
                Statement statement = dbAdapter.getConnection().createStatement();
                String sqlDelete = "DELETE FROM \"Costumer\" WHERE CUSTOMER_ID = '" + sid + "';";
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




