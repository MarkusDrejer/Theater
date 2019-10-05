// javac -cp mysql.jar MySQL.java
// java -cp mysql.jar:. MySQL 
import java.sql.*; // Import required packages
import java.util.ArrayList;

public class MakeBooking
{
    static final String MYDB = "database_Timmie"; 
    static final String USER = "timn"; 
    static final String PASS = "timmie"; 
    static final String DB_URL = "jdbc:mysql://mydb.itu.dk/" + MYDB;     
    
    public static ArrayList<Integer[]> ar = new ArrayList<Integer[]>(); 
    public MakeBooking()
    {   
    }
    
    /**
     * Denne Metode opretter den faktiske booking. Både med oprettelse af booking_nr tilknyttet tlf og navn
     * Samt til den specifikke forestilling(sal, time, date, film) og sæder(række og sæde nummer)
     * @param booked_seat er de sædder der bookes
     *  show_nr er det pågældende forestillings nummer
     *  name er bookeren's navn
     *  tilNr er bookeren's telefon nummer
     */
    public void request(ArrayList<Integer[]> booked_seat, String show_nr, String name, String tlf_nr)
    {   
        Connection connection = null; 
        Statement statement = null;
        
        Conversion convert = new Conversion();
        String[][] rowAndSeat = convert.toDoubleArray(booked_seat);
        String[] seat_number = new String[rowAndSeat.length];
        String[] row = new String[rowAndSeat.length];
        
        String booking_nr = "";
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
            
            //Her oprettes der en ny bestilling (med addresse og tlf_nr)
            String message = "INSERT INTO booking ("
            + "tlf_nr,name)"
            + " VALUES ("
            + "'" + tlf_nr+"',"
            + "'" + name +"'"
            + ");";
            statement.execute(message); 

            //Her hentes booking_nr fra den ovenstående "nye" bestilling
            String message2 = "Select booking_nr from booking "
            + "WHERE tlf_nr='" + tlf_nr +"' "
            + "AND name='" + name +"' "
            + "ORDER BY booking_nr DESC LIMIT 1";
            ResultSet rs = statement.executeQuery(message2);
            while (rs.next())
            {
                booking_nr = rs.getString("booking_nr");
            }
            rs.close();
            
            //Her oprettes så den specifikke booking med det "nye" booking_nr
            String message3 = "";
            for(int i = 0; i < rowAndSeat.length; i++)
            {
                
                
                    message3 = "INSERT INTO seat_booking ("
                    + "booking_nr,show_nr,row,seat_nr)"
                    + " VALUES ("
                    + "'" + booking_nr + "',"
                    + "'" + show_nr + "',"
                    + "'" + rowAndSeat[i][0] + "',"
                    + "'" + rowAndSeat[i][1] + "'"
                    + ");";
                    statement.execute(message3);
                
            }
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
}