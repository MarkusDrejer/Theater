// javac -cp mysql.jar MySQL.java
// java -cp mysql.jar:. MySQL 
import java.sql.*; // Import required packages
import java.util.ArrayList;
public class EditBooking
{
    static final String MYDB = "database_Timmie"; 
    static final String USER = "timn"; 
    static final String PASS = "timmie"; 
    static final String DB_URL = "jdbc:mysql://mydb.itu.dk/" + MYDB;     


    /**
     * Denne metode bruges til at finde bookinger(ved booking numre). Med hjælp fra indputs som
     * navn og telefonnummer.
     * @param name er navnet på brugeren
     *      tlf_nr er på brugeren
     */
    public String[] providePersonalInfo(String name, String tlf_nr)
    {
        Connection connection = null; 
        Statement statement = null;

        ArrayList<String> list = new ArrayList<>();
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement

            String message = "SELECT * FROM booking WHERE "
                + "name='" + name + "' AND "
                + "tlf_nr='" + tlf_nr + "'"; 
            ResultSet rs = statement.executeQuery(message); 
            while(rs.next())
            {
                list.add(rs.getString("booking_nr"));
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

        if(list.size() == 0)
        {
            return null;
        }

        String[] array = new String[list.size()];
        for(int i=0; i < array.length; i++)
        {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Denne metode frembringer dobblet array med infomation relateret til booking_nummmeret intastet
     * Infomationen indebære: booking_nr, film, dato, tid, sal, række og sæde_nr
     * @param booking_nr er et string array der indeholder op til flere booking numre
     */
    public String[][] getBookedTickets(String[] booking_nr)
    {
        Connection connection = null; 
        Statement statement = null;

        String[][] array;

        ArrayList<String> book = new ArrayList<>();
        ArrayList<String> show = new ArrayList<>();
        ArrayList<String> film = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        ArrayList<String> sal = new ArrayList<>();
        ArrayList<String> row = new ArrayList<>();
        ArrayList<String> seat_nr = new ArrayList<>();
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement

            for(int i=0; i < booking_nr.length; i++)
            {
                String message = "SELECT seat_booking.booking_nr,seat_booking.show_nr,film,date,time,sal,row,seat_nr FROM "
                    + "booking,forestilling,seat_booking WHERE "
                    + "booking.booking_nr ='" + booking_nr[i] + "' AND "
                    + "booking.booking_nr = seat_booking.booking_nr AND "
                    + "seat_booking.show_nr = forestilling.show_nr";

                ResultSet rs = statement.executeQuery(message);  
                while(rs.next())
                {
                    book.add(rs.getString("booking_nr"));
                    show.add(rs.getString("show_nr"));
                    film.add(rs.getString("film"));
                    date.add(rs.getString("date"));
                    time.add(rs.getString("time"));
                    sal.add(rs.getString("sal"));
                    row.add(rs.getString("row"));
                    seat_nr.add(rs.getString("seat_nr"));
                }
                rs.close();
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

        if(film.size() == 0)
        {
            return null;
        }

        array = new String[film.size()][8];
        for(int i = 0 ; i < film.size(); i++)
        {
            array[i][0] = book.get(i);
            array[i][1] = show.get(i);
            array[i][2] = film.get(i);
            array[i][3] = date.get(i);
            array[i][4] = time.get(i);
            array[i][5] = sal.get(i);
            array[i][6] = row.get(i);
            array[i][7] = seat_nr.get(i);
        }
        return array;
    }

    /**
     * Denne metode fjerner en enkelt booking i "seat_booking" tablet og tjekker samtidig om
     * booking nummeret fortsat er relateret til begge tables i databasen.
     * Er dette ikke tilfældet slettes de resterende bestillinger i den pågældende database
     * @param booking_nr er booking_nr
     *      show_nr er forstillings nummeret
     *      row er rækken
     *      seat_number er sæde nummeret
     */
    public void deleteOneBooking(String booking_nr, String show_nr, String row, String seat_number)
    {
        Connection connection = null; 
        Statement statement = null;

        ArrayList<String> list = new ArrayList<>();
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement

            String message = "DELETE FROM seat_booking WHERE "
                + "booking_nr='" + booking_nr + "' AND "
                + "show_nr='" + show_nr + "' AND "
                + "row='" + row + "' AND "
                + "seat_nr='" + seat_number + "'"; 
            statement.execute(message);
            statement.close();
        } catch(Exception e) {
            e.printStackTrace();  
        } finally { 
            try{
                connection.close();    
            } catch(Exception e) {
            }
        }
        this.checkBooking(booking_nr);
    }

    /**
     * Denne metode sletter alt der har med den pågældende bestilling
     * @param booking_nr er booking nummeret
     */
    public void deleteWholeBooking(String booking_nr)
    {
        deleteBookingNr_seatBooking(booking_nr);
        deleteBookingNr_booking(booking_nr);
    }

    //Denne checker om "booking" og "seat_booking" indeholde data med samme booking numre - hvis ikke sletter den i det table hvor det der er "for meget"
    private void checkBooking(String booking_nummer)
    {
        Connection connection = null; 
        Statement statement = null;

        ArrayList<String> seatBookingList = new ArrayList<>();
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement

            String message2 = "SELECT DISTINCT booking_nr FROM seat_booking";

            ResultSet rs2 = statement.executeQuery(message2);
            while(rs2.next())
            {
                seatBookingList.add(rs2.getString("booking_nr"));
            }
            rs2.close();
            statement.close();
        } catch(Exception e) {
            e.printStackTrace();  
        } finally { 
            try{
                connection.close();    
            } catch(Exception e) {
            }
        }

        boolean seatCheck = true;
        for(int i = 0; i < seatBookingList.size(); i++)
        {
            if(booking_nummer.equals(seatBookingList.get(i)))
            {
                seatCheck = false;
            }
        }

        if(seatCheck)
        {
            deleteBookingNr_booking(booking_nummer);
        }
    }

    //sletter bookingen med det pågældende booking_nr
    private void deleteBookingNr_booking(String booking_nr)
    {
        Connection connection = null; 
        Statement statement = null;

        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement

            String message = "DELETE FROM booking WHERE "
                + "booking_nr='" + booking_nr + "'";
            statement.execute(message);
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

    //sletter alle "seat_bookings" med det pågældende booking_nr
    private void deleteBookingNr_seatBooking(String booking_nr)
    {
        Connection connection = null; 
        Statement statement = null;

        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement

            String message = "DELETE FROM seat_booking WHERE "
                + "booking_nr='" + booking_nr + "'"; 
            statement.execute(message);            
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