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

public class MainUI extends JFrame
{
    private final JButton quick = new JButton("QUICK OPERATION");
    private final JButton user = new JButton("USER");
    private final JButton sessionManagement = new JButton("SESSION MANAGEMENT");
    private final JButton customer = new JButton("CUSTOMER");
    private final JButton product = new JButton("PRODUCT");
    private final JButton tran_pro = new JButton("TRANS_PRO");
    private final JButton transaction = new JButton("TRANSACTION");

    private final String userID;
    private final int maxID;

    private static DbAdapter dbAdapter;

    public MainUI(DbAdapter adapter, String userID, int maxID)
    {
        dbAdapter = adapter;
        this.userID = userID;
        this.maxID = maxID + 1;
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Coffee cafe");
            frame.add(new MenuPane());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setMinimumSize(new Dimension(500, 500));
            frame.setVisible(true);
            frame.setResizable(false);
            frame.setSize(400, 600);
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
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTH;

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());
            buttons.add(quick, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(user, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(sessionManagement, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(customer, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(product, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(tran_pro, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(transaction, gbc);

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
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            int x = (screenSize.width - frame.getWidth()) / 2;
            int y = (screenSize.height - frame.getHeight()) / 2;
            frame.setLocation(x, y);
            frame.setVisible(true);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Image icon = Toolkit.getDefaultToolkit().getImage("pain.png");
            frame.setIconImage(icon);
            frame.setLayout(null);
            frame.setSize(400,400);
            frame.setVisible(true);
        });

        tran_pro.addActionListener(e -> {
            Tran_ProUI frame = new Tran_ProUI(dbAdapter);
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

        user.addActionListener(e -> {
            UserUI frame = new UserUI(dbAdapter);
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

        sessionManagement.addActionListener(e -> {
            SessionManagementUI frame = new SessionManagementUI(dbAdapter);
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

        transaction.addActionListener(e -> {
            TransactionUI frame = new TransactionUI(dbAdapter);
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
