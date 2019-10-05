// javac -cp mysql.jar MySQL.java
// java -cp mysql.jar:. MySQL 
import java.sql.*; // Import required packages 

public class CountMySQLSalTableSize
{
    static final String MYDB = "database_Timmie"; 
    static final String USER = "timn"; 
    static final String PASS = "timmie"; 
    static final String DB_URL = "jdbc:mysql://mydb.itu.dk/" + MYDB;     
    /**
     * @param sal dette er den pågældende sal hvor størrelsen ønskes at blive fundet
     */
    public int rowSize(String sal) { 
        Connection connection = null; 
        Statement statement = null; 
        int count = 0;
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
            ResultSet rs = statement.executeQuery("SELECT * FROM " + sal +" ;"); 
            while (rs.next()) { 
                count++;
            } 
            rs.close();
        } catch(Exception e) { 
            e.printStackTrace();  
        } finally { 
            try{
                connection.close();    
            } catch(Exception e) {
            }
    } 
    return count;
  }
  
  /**
     * @param   sal dette er den pågældende sal hvor størrelsen ønskes at blive fundet
     */
  public String lastColumnName(String sal) { 
        Connection connection = null; 
        Statement statement = null; 
        String lastName = "";
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
            
            ResultSet rs = statement.executeQuery("SELECT * FROM " + sal +" ;");
            ResultSetMetaData metaData = rs.getMetaData(); 
            int count = metaData.getColumnCount();
            for(int i = 1; i <= count; i++)
            {
                lastName = metaData.getColumnLabel(i);
            }
            rs.close();
        } catch(Exception e) {
            e.printStackTrace();  
        } finally { 
            try{
                connection.close();    
            } catch(Exception e) {
            }
    } 
    return lastName;
  }
  
  /**
     * @param   sal dette er den pågældende sal hvor størrelsen ønskes at blive fundet
     */
  public int lastColumnNumber(String sal) { 
        Connection connection = null; 
        Statement statement = null; 
        int lastNumber = 0;
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
            
            ResultSet rs = statement.executeQuery("SELECT * FROM " + sal +" ;");
            ResultSetMetaData metaData = rs.getMetaData(); 
            lastNumber = metaData.getColumnCount();
            
            rs.close();
        } catch(Exception e) {
            e.printStackTrace();  
        } finally { 
            try{
                connection.close();    
            } catch(Exception e) {
            }
    } 
    return lastNumber-1; //"-1" da den første column er "seat_number"
  }
} 
