package com.company.UI;

import com.company.connection.DbAdapter;
import com.company.frame.MyTable;
import com.company.frame.QueryItem;

import java.awt.*;
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

public class ProductUI extends JFrame {
    private final int COLUMN = 5;
    private final Vector<Vector<String>> dataModel = new Vector<>();
    private final QueryItem productID = new QueryItem("PRODUCT_ID：", 10);
    private final QueryItem name = new QueryItem("NAME：", 10);
    private final QueryItem count = new QueryItem("COUNT：", 10);
    private final QueryItem price = new QueryItem("PRICE：", 22);
    private final QueryItem categoryType = new QueryItem("CATEGORY_TYPE：", 5);
    private final JButton queryBtn = new JButton("SEARCH");
    private final JButton saveBtn = new JButton("UPDATE");
    private final JButton insertBtn = new JButton("INSERT");
    private final JButton deleteBtn = new JButton("DELETE");
    private final JTextArea textarea = new JTextArea(5, 5);
    private final MyTable table;

    private static DbAdapter dbAdapter;

    public ProductUI(DbAdapter adapter) {
        this.setTitle("Product");
        dbAdapter = adapter;

        List<String> TITLES = Arrays.asList(
                "PRODUCT_ID", "NAME", "COUNT", "PRICE", "CATEGORY_TYPE");
        Vector<String> titles = new Vector<>(TITLES);
        table = new MyTable(dataModel, titles);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);


        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(productID);
        controlPanel.add(name);
        controlPanel.add(count);
        controlPanel.add(price);
        controlPanel.add(categoryType);
        controlPanel.add(Box.createRigidArea(new Dimension(100,0)));
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
            if (productID.isSelected()) conditions.add("(PRODUCT_ID = " + productID.getText() + ")");
            if (name.isSelected()) conditions.add("(NAME = '" + name.getText() + "')");
            if (count.isSelected()) conditions.add("(COUNT = " + count.getText() + ")");
            if (price.isSelected()) conditions.add("(PRICE = " + price.getText() + ")");
            if (categoryType.isSelected()) conditions.add("(CATEGORY_TYPE = '" + categoryType.getText() + "')");

            StringBuilder sb = new StringBuilder();
            sb.append("select * from \"Product\"");
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
            String id = productID.getText();
            String sname = name.getText();
            String ssex = count.getText();
            String sclass = price.getText();
            String sdept = categoryType.getText();


            String cmd = "insert into \"Product\" values (" + id + ", '" + sname + "', " +
                    ssex + ", " + sclass + ", '" + sdept + "');";
            textarea.setText(cmd);

            try {
                Statement statement = dbAdapter.getConnection().createStatement();
                String sqlInsert = "INSERT INTO \"Product\" (PRODUCT_ID, NAME, COUNT, PRICE, CATEGORY_TYPE) "+
                        "VALUES (" + id + ", '" + sname + "', " + ssex + ", " + sclass + ", '" + sdept +"');";
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
            if (name.isSelected())
            {
                table.setValueAt(name.getText(), row, 1);
                conditions.add("name = '" + name.getText() + "'");
            }
            if (count.isSelected())
            {
                table.setValueAt(count.getText(), row, 2);
                conditions.add("count = " + count.getText() + "");
            }
            if (price.isSelected())
            {
                table.setValueAt(price.getText(), row, 3);
                conditions.add("price = " + price.getText() + "");
            }
            if (categoryType.isSelected())
            {
                table.setValueAt(categoryType.getText(), row, 4);
                conditions.add("category_type = '" + categoryType.getText() + "'");
            }

            if(conditions.size() == 0) return;

            StringBuilder sb = new StringBuilder();
            sb.append("update \"Product\" set ");
            for(int i = 0; i < conditions.size(); i++)
            {
                sb.append(conditions.get(i));
                if(i != conditions.size() - 1)
                    sb.append(", ");
            }
            sb.append(" where product_id = ").append(sid).append(";");
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
            String sql = "delete from \"Product\" where product_id = " + sid + ";";


            textarea.setText(sql);

            try {
                Statement statement = dbAdapter.getConnection().createStatement();
                String sqlDelete = "DELETE FROM \"Product\" WHERE PRODUCT_ID = " + sid + ";";
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




