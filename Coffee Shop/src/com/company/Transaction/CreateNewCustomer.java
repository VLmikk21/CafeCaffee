package com.company.Transaction;

import com.company.connection.DbAdapter;
import com.company.frame.Line;
import com.company.frame.TwoButton;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class CreateNewCustomer
{
    private final Line customerID = new Line("Customer ID: ",15);
    private final Line firstName = new Line("   First Name: ",15);
    private final Line lastName = new Line("   Last Name: ",15);
    private final Line address = new Line("       Address: ", 15);
    private final TwoButton button = new TwoButton("CONFIRM", "QUIT");

    private static DbAdapter dbAdapter;
    JFrame frame;


    public CreateNewCustomer(DbAdapter adapter)
    {
        dbAdapter = adapter;
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Create new Customer");
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
        });

    }

    private static void close()
    {
        try{ dispose(); }
        catch(Exception e){e.printStackTrace();}
    }

    private static void dispose() {
    }

    private void setActionListener()
    {
        button.getFirstButton().addActionListener(e -> {
            String id = customerID.getText();
            String sname = firstName.getText();
            String ssex = lastName.getText();
            String sclass = address.getText();

            try
            {
                String sql = "select customer_id from Customer where customer_id = '" + id + "';";
                PreparedStatement ps = dbAdapter.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next())
                {
                    if(rs.getString(1) != null)
                        return;
                }
            }
            catch (Exception error) {error.printStackTrace();}

            try {
                Statement statement = dbAdapter.getConnection().createStatement();
                String sqlInsert = "INSERT INTO Customer "+
                        "VALUES ('" + id + "', '" + sname + "', '" + ssex + "', '" + sclass +"', " + "10, );";
                System.out.println(sqlInsert);
                statement.executeUpdate(sqlInsert);
                statement.close();
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        button.getLastButton().addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
    }

    private class MenuPane extends JPanel
    {
        public MenuPane()
        {
            setBorder(new EmptyBorder(100, 0, 0, 0));
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTH;

            this.add(new JLabel("<html><h1><strong><i>CREATE CUSTOMER</i></strong></h1><hr><h2></h2><h3></h3></html>"), gbc);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;


            JPanel buttons = new JPanel(new GridBagLayout());

            buttons.add(customerID, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(firstName, gbc);
            buttons.add(lastName, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(address, gbc);
            buttons.add(Box.createVerticalStrut(50));
            buttons.add(Box.createRigidArea(new Dimension(0, 150)));
            buttons.add(button, gbc);

            gbc.weighty = 1;
            add(buttons, gbc);
        }
    }
}

