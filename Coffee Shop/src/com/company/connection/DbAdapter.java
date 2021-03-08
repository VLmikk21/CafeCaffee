package com.company.connection;
import java.sql.*;

public class DbAdapter
{
    Connection connection = null;
    String host = "localhost";
    String port = "5432";
    String db_name = "d";
    String username = "postgres";
    String password = "AsKul312";

    public DbAdapter() { }

    public void connect()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+db_name, username, password);
            System.out.println("Database connection established!");

            if(connection!=null) {
                System.out.println("Database opened successfully \n");

            } else {
                System.out.println("Database failed to open \n");
            }
        }
        catch(SQLException e) { e.printStackTrace(); }
    }

    public void disConnect()
    {
        try
        {
            if(connection != null) connection.close();
            System.out.println("Database connection disconnect!");
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public Connection getConnection() { return connection; }
}
