package com.company.Transaction;

import com.company.connection.DbAdapter;import com.company.frame.Line;import java.awt.event.WindowEvent;import javax.swing.border.EmptyBorder;
import com.company.frame.TwoButton;
import com.company.frame.MyTable;
import com.company.frame.QueryItem;
import com.company.frame.QueryItem2;
import com.company.relations.Customer;
import com.company.relations.Product;
import com.company.relations.Tran_Pro;
import com.company.relations.Transaction;
import javax.swing.*;
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

public class AddTransaction extends JFrame
{
    private final int COLUMN = 3;
    private final Vector<Vector<String>> dataModel = new Vector<>();
    private final QueryItem customerID = new QueryItem("CUSTOMER_ID: ", 8);
    private final QueryItem productID = new QueryItem("PRODUCT_ID: ", 8);
    private final QueryItem quantity = new QueryItem("QUANTITY: ", 4);
    private final QueryItem2 usePoints = new QueryItem2("USE POINTS");
    private final JButton confirmTransaction = new JButton("CONFIRM");
    private final JTextArea textarea = new JTextArea(5, 5);
    private final MyTable table;

    private static DbAdapter dbAdapter;
    JFrame frame;

    public AddTransaction(DbAdapter adapter)
    {
        dbAdapter = adapter;
        this.setTitle("Create a new Transaction");
        List<String> TITLES = Arrays.asList(
                "TRANSACTION_ID", "PRODUCT_ID", "QUANTITY");
        Vector<String> titles = new Vector<>(TITLES);
        table = new MyTable(dataModel, titles);
       table.getColumnModel().getColumn(2).setPreferredWidth(130);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 25));
        controlPanel.add(customerID);
        controlPanel.add(productID);
        controlPanel.add(quantity);
        controlPanel.add(usePoints);
        confirmTransaction.setPreferredSize(new Dimension(130, 25));
        controlPanel.add(confirmTransaction);
        controlPanel.setPreferredSize(new Dimension(0, 130));

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        tablePanel.add(table.getTableHeader());
        tablePanel.add(new JScrollPane(table));

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JTextArea textarea = new JTextArea(5, 5);
        container.add(textarea, BorderLayout.NORTH);
        container.add(tablePanel, BorderLayout.CENTER);

        this.add(controlPanel, BorderLayout.NORTH);
        this.add(container, BorderLayout.CENTER);
        this.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.WEST);
        this.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.EAST);
        this.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.SOUTH);

        setActionListener();
    }

    private void setActionListener()
    {
        confirmTransaction.addActionListener(e -> {
            ArrayList<String> conditions = new ArrayList<>();
            if (customerID.isSelected()) conditions.add("(TRANSACTION_ID = " + customerID.getText() + ")");
            if (productID.isSelected()) conditions.add("(PRODUCT_ID = " + productID.getText() + ")");
            if (quantity.isSelected()) conditions.add("(QUANTITY = '" + quantity.getText() + "')");

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

    }
}
