package com.company.UI;

import com.company.Transaction.AddTransaction;
import com.company.connection.DbAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmployeeUI
{
    private final JButton quick = new JButton("QUICK OPERATION");
    private final JButton customer = new JButton("CUSTOMER");
    private final JButton product = new JButton("PRODUCT");

    private static DbAdapter dbAdapter;
    private final String userID;
    private final int maxID;

    public EmployeeUI(DbAdapter adapter, String userID, int maxID)
    {
        dbAdapter = adapter;
        this.userID = userID;
        this.maxID = maxID + 1;
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Coffee Cafe");
            frame.add(new MenuPane());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setMinimumSize(new Dimension(1280, 960));
            frame.setVisible(true);
            frame.setResizable(false);
            setActionListener();
            Image icon = Toolkit.getDefaultToolkit().getImage("pain.png");
            frame.setIconImage(icon);
            frame.setLayout(null);
            frame.setSize(400,400);
            frame.setVisible(true);
            frame.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    updateSessionManagement();
                    dbAdapter.disConnect();
                    System.exit(0);
                }
            });
        });
    }

    private void updateSessionManagement()
    {
        try
        {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Statement statement = dbAdapter.getConnection().createStatement();
            String sqlInsert = "INSERT INTO \"session_management\" (SESSION_ID, USER_ID) "+
                    "VALUES (" + maxID + ", '" + userID + "');";
            statement.executeUpdate(sqlInsert);
            statement.close();
        }
        catch(Exception e){e.printStackTrace();}
    }

    private class MenuPane extends JPanel {

        public MenuPane() {
            setBorder(new EmptyBorder(100, 0, 0, 0));
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTH;

            this.add(new JLabel("<html><h1><strong><i>Welcome to Coffee Cafe!</i></strong></h1><hr>" +
                    "<h2><center>The project was done by: VLmikk</center></h2></html>"), gbc);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());
            buttons.add(quick, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(customer, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(product, gbc);
            buttons.add(Box.createVerticalStrut(50));
            gbc.weighty = 1;
            add(buttons, gbc);
        }
    }

    private void setActionListener()
    {
        customer.addActionListener(e -> {
            CustomerUI frame = new CustomerUI(dbAdapter);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(750, 500));
            frame.setVisible(true);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            int x = (screenSize.width - frame.getWidth()) / 2;
            int y = (screenSize.height - frame.getHeight()) / 2;
            frame.setLocation(x, y);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });

        product.addActionListener(e -> {
            ProductUI frame = new ProductUI(dbAdapter);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(750, 500));
            frame.setVisible(true);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            int x = (screenSize.width - frame.getWidth()) / 2;
            int y = (screenSize.height - frame.getHeight()) / 2;
            frame.setLocation(x, y);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });

        quick.addActionListener(e -> {
            AddTransaction frame = new AddTransaction(dbAdapter);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(750, 500));
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            int x = (screenSize.width - frame.getWidth()) / 2;
            int y = (screenSize.height - frame.getHeight()) / 2;
            frame.setLocation(x, y);
            frame.setVisible(true);
            frame.setResizable(false);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                }
            });
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });
    }
}
