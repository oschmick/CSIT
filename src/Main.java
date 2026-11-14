/**
 * 
 * @author Olivia Schmick and Claire Wilkening
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
  public static void main(String[] args) {
    connect();
    createNewTable();
    insert("harsw013-1", "10.1.3.192", "AB:CD:EF:12:34", "WS-C2960-48TC-L", "15.0(2)SE11");
    printTable();
  }

  private static void printTable() {
    String url = "jdbc:sqlite:inventory.db";
    String sql = "SELECT * FROM Switches";

    try (Connection conn = DriverManager.getConnection(url);
         java.sql.Statement stmt = conn.createStatement();
         java.sql.ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        System.out.println(rs.getInt("id") + "\t" +
                           rs.getString("hostname") + "\t" +
                           rs.getString("ip") + "\t" +
                           rs.getString("mac") + "\t" +
                           rs.getString("model") + "\t" +
                           rs.getString("swversion"));
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
  private static void insert(String hostname, String ip, String mac, String model, String swversion) {
    String url = "jdbc:sqlite:inventory.db";
    String sql = "INSERT OR IGNORE INTO Switches(hostname, ip, mac, model, swversion) VALUES(?, ?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(url);
         java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, hostname);
      pstmt.setString(2, ip);
      pstmt.setString(3, mac);
      pstmt.setString(4, model);
      pstmt.setString(5, swversion);
      pstmt.executeUpdate();
      System.out.println("Data created.");
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Creates new tables Switches and IDFs
   */
  private static void createNewTable() {
    String url = "jdbc:sqlite:inventory.db";

    String sql = "CREATE TABLE IF NOT EXISTS Switches (\n" +
                            "id INTEGER PRIMARY KEY,\n" +
                            "hostname TEXT NOT NULL,\n" +
                            "ip TEXT NOT NULL UNIQUE,\n" +
                            "mac TEXT NOT NULL UNIQUE,\n" +
                            "model TEXT,\n" +
                            "swversion TEXT\n" +
                            ");";

    try (Connection conn = DriverManager.getConnection(url);
         java.sql.Statement stmt = conn.createStatement()) {

      stmt.execute(sql);
      System.out.println("Table created.");
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