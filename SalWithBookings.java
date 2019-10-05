// javac -cp mysql.jar MySQL.java
// java -cp mysql.jar:. MySQL 
import java.sql.*; // Import required packages 

public class SalWithBookings
{
    static final String MYDB = "database_Timmie"; 
    static final String USER = "timn"; 
    static final String PASS = "timmie"; 
    static final String DB_URL = "jdbc:mysql://mydb.itu.dk/" + MYDB;     
    
    //The first collum is the amount of seat rows, and the second collumn is the amount of seats in each row
    //in the end the the array will tell if the seat is avalibel or not
    private int[][] a; 
    /**
     * Denne metode kommunikere med databasen og bringer layoutet for salen til den pågældende forestillinge from
     * I form af et dobbelt array, bestående udelukkende af 0'er (databasens standard konfiguration)
     * @param show_nr er det forestillingsnummer hvorfra sallen hentes fra
     */
    private void request(String show_nr)
    {        
        Connection connection = null; 
        Statement statement = null;
        
        GetForestillingListe forestilling = new GetForestillingListe();
        String sal = forestilling.getSal(show_nr);
        
        CountMySQLSalTableSize myTable = new CountMySQLSalTableSize();
        int row = myTable.rowSize(sal);
        int columnSize = myTable.lastColumnNumber(sal);
        String c = myTable.lastColumnName(sal);
        char column = c.charAt(0);
        a = new int[columnSize][row];
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
                     
            ResultSet rs = statement.executeQuery("SELECT * FROM " + sal); 
            int i = 0;
            while (rs.next())
            {
                int j = 0;
                for(char alphabet = 'a'; alphabet <= column; alphabet++)
                {
                    String letter = "" + alphabet;
                    a[j][i] = rs.getInt(letter);
                    j++;
                }
                i++;
            }
            rs.close();
            statement.close();
        } catch(Exception e) {
            e.printStackTrace();  
        } finally { 
            try{
                connection.close();    
            } catch(Exception e) {
            }
        } 
    }
    
    /**
     * Dette metode viser salen+allerede booked sædder. Dette gør den ved at kalde på request metoden,
     * samt kigge i databasen for bookinger med samme forestillings_nr(show_nr).
     * @param show_nr er forestillingsnummeret der kan bruges til at få sal, række, sæde og film.
     */
     public int[][] requestWithBookings(String show_nr)
    {
        Connection connection = null; 
        Statement statement = null;
        
        request(show_nr);
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
            
            String message = "SELECT row,seat_nr FROM seat_booking WHERE show_nr='" + show_nr + "' ORDER BY row";
            ResultSet rs = statement.executeQuery(message); 
            while (rs.next())
            {
                String rowBook = rs.getString("row");
                String seatBook = rs.getString("seat_nr");
                int j = 0;
                for(char alphabet = 'a'; alphabet <= rowBook.charAt(0); alphabet++)
                {
                    j++;
                    if(alphabet == 'a')
                    {
                        j = 0;
                    }
                }
                int rowBookNr = j;
                int seatBookNr = Integer.parseInt(seatBook);
                
                a[rowBookNr][seatBookNr] = 1;
            }
            rs.close();
            statement.close();
        } catch(Exception e) {
            e.printStackTrace();  
        } finally { 
            try{
                connection.close();    
            } catch(Exception e) {
            }
        } 
        return a;
    }    
}

