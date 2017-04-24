package com.test.dtnprotocol.database;

import java.sql.*;

public class SQLiteJDBC
{

  private Connection c = null;

  /*
  *********************************************************************************
  * Connect to an existing database. 
  * If database does not exist, then it will be created and finally a database 
  * object will be returned.
  *********************************************************************************
  */
  public Connection getDatabaseConnection (){
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:conf/test.db");
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Opened database successfully");
    return c ; 
  }

/*
 **********************************************************************************
 * Following method will be used to create a table in previously 
 * created database:
 **********************************************************************************
*/
  public void createTable (){

    Statement stmt = null;
    try {

      // The object used for executing a static SQL statement and returning 
      // the results it produces.
      stmt = c.createStatement();

      String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                   "(ID INT PRIMARY KEY     NOT NULL," +
                   " NAME           TEXT    NOT NULL, " + 
                   " AGE            INT     NOT NULL, " + 
                   " ADDRESS        CHAR(50), " + 
                   " SALARY         REAL)"; 
      stmt.executeUpdate(sql);
      stmt.close();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Table created successfully");

  }

}