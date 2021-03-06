package com.company.relations;

import com.company.connection.DbAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer
{
    private static DbAdapter dbAdapter;

    public Customer(DbAdapter adapter)
    {
        dbAdapter = adapter;
    }

    public void createCustomerTable()
    {
        try
        {
            Statement statement = dbAdapter.getConnection().createStatement();
            String sqlCreate = "CREATE TABLE CUSTOMER              " +
                    "(CUSTOMER_ID       VARCHAR(50)      NOT NULL," +
                    "FIRST_NAME         VARCHAR(50)              ," +
                    "LAST_NAME          VARCHAR(50)      NOT NULL," +
                    "ADDRESS            VARCHAR(200)             ," +
                    "MY_POINTS          INT                      ," +
                    "PRIMARY KEY (CUSTOMER_ID));";
            statement.executeUpdate(sqlCreate);
            statement.close();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public int getLifePoints(String customerID)
    {
        int lifePoints = -1;
        try
        {
            String sql = "select my_points from \"Customer\" where customer_id = '" + customerID + "';";
            System.out.println(sql);
            PreparedStatement ps = dbAdapter.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                lifePoints = rs.getInt(1);
            ps.close();
            rs.close();
        }
        catch(Exception e){ e.printStackTrace(); }

        return lifePoints;
    }

    public int getAvgPoints(String customerID)
    {
        int myPoints = -1;
        try
        {
            String sql = "select customer_id from Customer where customer_id = '" + customerID + "';";
            System.out.println(sql);
            PreparedStatement ps = dbAdapter.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                myPoints = rs.getInt(1);
            ps.close();
            rs.close();
        }
        catch(Exception e){ e.printStackTrace(); }

        return myPoints;
    }

    public void updateAvgPoints(String customerID)
    {
        try
        {
            String sql = "update \"Customer\" set my_points = my_points - 25 where customer_id = '" + customerID +"';";
            Statement statement = dbAdapter.getConnection().createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void addPoints(String customerID, String quantity)
    {
        try
        {
            String sql = "update \"Customer\" set my_points = my_points + " + quantity +
                    ", my_points = my_points + " + quantity + " where customer_id = '" +
                    customerID + "';";
            Statement statement = dbAdapter.getConnection().createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }
        catch(Exception e) { e.printStackTrace(); }
    }

    public void dropCustomerTable()
    {
        try
        {
            Statement statement = dbAdapter.getConnection().createStatement();
            String sqlDrop = "DROP TABLE Customer;";
            statement.executeUpdate(sqlDrop);
            statement.close();
        }
        catch(Exception e){ e.printStackTrace(); }
    }

    public void deleteAll()
    {
        try
        {
            Statement statement = dbAdapter.getConnection().createStatement();
            String sqlDrop = "DELETE FROM Customer;";
            statement.executeUpdate(sqlDrop);
            statement.close();
        }
        catch(Exception e){ e.printStackTrace(); }
    }
}