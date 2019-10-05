
import java.sql.*; // Import required packages
import java.util.ArrayList;

public class CheckInput
{
    static final String MYDB = "database_Timmie"; 
    static final String USER = "timn"; 
    static final String PASS = "timmie"; 
    static final String DB_URL = "jdbc:mysql://mydb.itu.dk/" + MYDB;     

    /**
     * Denne metode checker om navn-inputtet er valideret og umiddelbart kan medføre skade på databasen
     * @param name er det navn der gives som input
     */
    public void checkName(String name)
    {
        if(name.matches(""))
        {
            throw new IllegalInputException("The name is empty");
            //throw NoNumbersException();
        }
        if(name.toLowerCase().contains("  ".toLowerCase()))
        {
            throw new IllegalInputException("The name contains double spacing");
        }      
        if(name.matches("[a-zA-Z ]+") == false)
        {
            throw new IllegalInputException("The name contains numbers or special characters");
            //throw NoNumbersException();
        }
        if(name.toLowerCase().contains("table".toLowerCase()))
        {
            throw new IllegalInputException("The name contains wording which might harm the program and the database");
        }
        if(name.toLowerCase().contains("drop".toLowerCase()))
        {
            throw new IllegalInputException("The name contains wording which might harm the program and the database");
        }
        if(name.toLowerCase().contains("delete".toLowerCase()))
        {
            throw new IllegalInputException("The name contains wording which might harm the program and the database");
        }
    }

    /**
     * Denne metode checker om tlf nummer - inputtet er valideret og umiddelbart kan medføre skade på databasen
     * @param tlf_number er det telefon nummer der gives som input
     */
    public void checkPhoneNr(String tlf_number)
    {
        if(tlf_number.length() > 13)
        {
            throw new IllegalInputException("The phone Number is too large");
        }
        if(tlf_number.matches("[0-9]+") == false)
        {
            throw new IllegalInputException("The phone number contains letter");
        }
        if(tlf_number.length() == 0)
        {
            throw new IllegalInputException("The phone Number is empty");
        }
        //Maybe make a check for phone number too small
    }

    /**
     * Denne metode checker om booking_nr-inputtet er valideret og umiddelbart kan medføre skade på databasen
     * @param booking_nr er det booking nummer der gives som input
     * Bliver ikke brugt. Beholdt hvis den bruges i fremtiden
     */
    /*public void checkBooking_nr(String booking_nr)
    {
        if(booking_nr.length() > 9)
        {
            throw new IllegalInputException("The booking_nr is out of bounds");
        }
        if(booking_nr.matches("[0-9]+") == false)
        {
            throw new IllegalInputException("The booking_nr cannot contain letters");
        }
    }*/

    /**
     * Denne metode checker om navn-inputtet er valideret og umiddelbart kan medføre skade på databasen
     * @param name er det navn der gives som input
     */
    public void checkNameDatabase(String name)
    {
        Connection connection = null; 
        Statement statement = null;

        ArrayList<String> list = new ArrayList<>();
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement

            String message = "SELECT name FROM booking";

            ResultSet rs = statement.executeQuery(message); 
            while(rs.next())
            {
                list.add(rs.getString("name"));
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
        boolean check = false;
        for(int i = 0; i < list.size(); i++)
        {
            if(name.equals(list.get(i)))
            {
                check = true;
            }
        }
        if(!check){
            throw new IllegalInputException("The name is not in our system");
        }
    }

    /**
     * Denne metode checker om tlf nummer - inputtet er valideret og umiddelbart kan medføre skade på databasen
     * @param tlf_number er det telefon nummer der gives som input
     */
    public void checkPhoneNrDatabase(String tlf_number)
    {
        Connection connection = null; 
        Statement statement = null;

        ArrayList<String> list = new ArrayList<>();
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement

            String message = "SELECT tlf_nr FROM booking";

            ResultSet rs = statement.executeQuery(message); 
            while(rs.next())
            {
                list.add(rs.getString("tlf_nr"));
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
        boolean check = false;
        for(int i = 0; i < list.size(); i++)
        {
            if(tlf_number.equals(list.get(i)))
            {
                check = true;
            }
        }
        if(!check){
            throw new IllegalInputException("The name is not in our system");
        }

    }

}