/**
 * Main Java file. Includes database connection and data management
 * 
 * Connection, table creation, and data insertion from this tutorial by mintype on YouTube: https://youtu.be/nAEX7kqfM_o?is=keSxFtF6p32KHLwZ
 * @author Olivia Schmick and Claire Wilkening
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
  public static void main(String[] args) {
    connect();
    createSwitchTable();
    createIDFTable();
    insertSwitch("harsw013-1", "10.1.3.192", "AB:CD:EF:12:34", "WS-C2960-48TC-L", "15.0(2)SE11", 6);
    insertIDFs(2, "Li", "059");
    printSwitchTable();
    printIDFTable();
  }

  /**
   * Prints Switch table, columns separated by tabs.
   */
  private static void printSwitchTable() {
    String url = "jdbc:sqlite:inventory.db";
    String sql = "SELECT * FROM Switches";

    try (Connection conn = DriverManager.getConnection(url);
         java.sql.Statement stmt = conn.createStatement();
         java.sql.ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        System.out.println(rs.getInt("swid") + "\t" +
                           rs.getString("hostname") + "\t" +
                           rs.getString("ip") + "\t" +
                           rs.getString("mac") + "\t" +
                           rs.getString("model") + "\t" +
                           rs.getString("swversion") + "\t" +
                           rs.getString("idf"));
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Prints IDF table, columns separated by tabs.
   */
  private static void printIDFTable() {
    String url = "jdbc:sqlite:inventory.db";
    String sql = "SELECT * FROM IDFs";

    try (Connection conn = DriverManager.getConnection(url);
         java.sql.Statement stmt = conn.createStatement();
         java.sql.ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        System.out.println(rs.getInt("idf") + "\t" +
                           rs.getString("bn") + "\t" +
                           rs.getString("room"));
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Inserts given attributes as a tuple into the Switches table
   * @param hostname Hostname of the switch
   * @param ip IP of the switch
   * @param mac MAC of the switch
   * @param model Model of the switch
   * @param swversion SW Version of the switch
   */
  private static void insertSwitch(String hostname, String ip, String mac, String model, String swversion, int idf) {
    String url = "jdbc:sqlite:inventory.db";
    String sql = "INSERT OR IGNORE INTO Switches(hostname, ip, mac, model, swversion, idf) VALUES(?, ?, ?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(url);
         java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, hostname);
      pstmt.setString(2, ip);
      pstmt.setString(3, mac);
      pstmt.setString(4, model);
      pstmt.setString(5, swversion);
      pstmt.setInt(6, idf);
      pstmt.executeUpdate();
      System.out.println("Switch data created.");
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Inserts given attributes as a tuple into the IDFs table
   * @param idf
   * @param bn
   * @param room
   * @param swid
   */
  private static void insertIDFs(int idf, String bn, String room) {
    String url = "jdbc:sqlite:inventory.db";
    String sql = "INSERT OR IGNORE INTO IDFs(idf, bn, room) VALUES(?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(url);
         java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, idf);
      pstmt.setString(2, bn);
      pstmt.setString(3, room);
      pstmt.executeUpdate();
      System.out.println("IDF data created.");
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Creates new table 'Switches'
   */
  private static void createSwitchTable() {
    String url = "jdbc:sqlite:inventory.db";

    String sql = 
      "CREATE TABLE IF NOT EXISTS Switches (\n" +
          "swid INTEGER PRIMARY KEY,\n" +
          "hostname TEXT NOT NULL,\n" +
          "ip TEXT NOT NULL UNIQUE,\n" +
          "mac TEXT NOT NULL UNIQUE,\n" +
          "model TEXT,\n" +
          "swversion TEXT,\n" +
          "idf INTEGER,\n" +
          "FOREIGN KEY(idf) REFERENCES Switch(swid)" +
      ");";

    try (Connection conn = DriverManager.getConnection(url);
         java.sql.Statement stmt = conn.createStatement()) {

      stmt.execute(sql);
      System.out.println("Switch table created.");
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Creates new table 'IDFs'
   */
  private static void createIDFTable() {
    String url = "jdbc:sqlite:inventory.db";

    String sql = 
      "CREATE TABLE IF NOT EXISTS IDFs (\n" +
          "idf INTEGER PRIMARY KEY,\n" +
          "bn TEXT NOT NULL,\n" +
          "room TEXT NOT NULL\n" +
      ");";

    try (Connection conn = DriverManager.getConnection(url);
         java.sql.Statement stmt = conn.createStatement()) {

      stmt.execute(sql);
      System.out.println("IDF table created.");
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Tests connection to database
   */
  private static void connect() {
    String url = "jdbc:sqlite:inventory.db";

    try (Connection conn = DriverManager.getConnection(url)) {
      if (conn != null) {
        System.out.println("Connected to the database");
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}