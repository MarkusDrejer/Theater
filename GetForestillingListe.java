// javac -cp mysql.jar MySQL.java
// java -cp mysql.jar:. MySQL 
import java.sql.*; // Import required packages 
import java.util.ArrayList;

public class GetForestillingListe
{
    static final String MYDB = "database_Timmie"; 
    static final String USER = "timn"; 
    static final String PASS = "timmie"; 
    static final String DB_URL = "jdbc:mysql://mydb.itu.dk/" + MYDB;     
    
    
    public String[][] array2;
    /**
     * Denne metode beder om den første request fra klienten, af enten typen "date" eller "film"
     * @param request er den kategori der ønskes at få en liste med unikke titler fra.
     *  "film" or "date".
     */
    public String[] requestFilm()
    {
        Connection connection = null; 
        Statement statement = null;
        
        ArrayList<String> list = new ArrayList<>();
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
            
            ResultSet rs = statement.executeQuery("SELECT DISTINCT film FROM forestilling ORDER BY film");  
           while (rs.next())
            {
                list.add(rs.getString("film"));
            }
            rs.close();
            statement.close();
        } catch(Exception e) { // handle errors:
            e.printStackTrace();  
        } finally { 
            try{
                connection.close();    
            } catch(Exception e) {
            }
        } 
        String[] array = new String[list.size()];
        for(int i=0; i < array.length; i++)
        {
            array[i] = list.get(i);
        }
        return array;
    }

    
    /**
     * Denne metode er efter at have meddelt den første information til ekspedienten,
     * Hvorefter en list af den resterende information omkring bestillinger bringes op.
     * Metoden bruger to indputs:
     * @param requestType er den kategori hvorfra klienten først har ønsket at bruge (film titel eller dato)
     *      "film", "date"
     *  request dette er den specifikke navn på filmen, hvis date er valgt er request en ikke relevant input
     */
    public String[][] madeYourFirstChoice(String requestType, String request)
    {
        Connection connection = null; 
        Statement statement = null;   
        
        ArrayList<String> filmList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<String> timeList = new ArrayList<>();
        ArrayList<String> salList = new ArrayList<>();
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
            
            String message = "";
            if(requestType.equals("film"))
            {
                message = "SELECT date,time,sal FROM forestilling WHERE film='" + request + "' ORDER BY date";
            }else if(requestType.equals("date"))
            {
                message = "SELECT date,time FROM forestilling ORDER BY date";
            }
            ResultSet rs = statement.executeQuery(message);  
            while (rs.next())
            {
               dateList.add(rs.getString("date"));
               timeList.add(rs.getString("time"));
               if(requestType.equals("film"))
                {
                    salList.add(rs.getString("sal"));
                }
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
        
        String[][] array = new String[3][timeList.size()];
        if(requestType.equals("film"))
        {
            for(int i=0; i < array[0].length; i++)
            {
                array[0][i] = dateList.get(i);
                array[1][i] = timeList.get(i);
                array[2][i] = salList.get(i);
            }
            return array;
        }
        
        /*String[][]*/ array2 = new String[2][timeList.size()];
        if(requestType.equals("date"))
        {
            for(int i=0; i < array2[0].length; i++)
            {
                array2[0][i] = dateList.get(i);
                array2[1][i] = timeList.get(i);
            }
            return array2;
        }   
        return null;
    }
    
    /**
     * Denne metode bringer film med tilhørende sal ud fra databasen på baggrund af dato og tid
     * @param date er datoen
     *  time er den pågældende afspilningstidspunkt
     */
    public String[][] requestFilmFromDate(String date, String time)
    {
        Connection connection = null; 
        Statement statement = null;
        
        ArrayList<String> filmList = new ArrayList<>();
        ArrayList<String> salList = new ArrayList<>();
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
            
            String message = "SELECT film,sal FROM forestilling WHERE "
            + "date='" + date + "' AND "
            + "time='" + time + "'";
            
            ResultSet rs = statement.executeQuery(message);  
            while (rs.next())
            {
                filmList.add(rs.getString("film"));
                salList.add(rs.getString("sal"));
            }
            rs.close();
            statement.close();
        } catch(Exception e) { // handle errors:
            e.printStackTrace();  
        } finally { 
            try{
                connection.close();    
            } catch(Exception e) {
            }
        } 
        
        String[][] array = new String[filmList.size()][2];
        for(int i=0; i < array.length; i++)
        {
            array[i][0] = filmList.get(i);
            array[i][1] = salList.get(i);
        }
        return array;
    }

    /**
     * Denne metode henter forestillingsnummeret ud fra databasen ved brug af samtlige andre parametre.
     * @param   film er film titlen for den specifikke film
     *  date er datoen, af type: "08122016"
     *  time er afspillingstidspunktet, af type: "18:00"
     *  sal er den aktuelle sal for forestillingen, følgende sale:
     *      "sal_1", "sal_2", "sal_3"
     */
    public String getForestillingsNummer(String film, String date, String time, String sal)
    {
        Connection connection = null; 
        Statement statement = null;
        
        String forestillings_nr = "";        
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
            
            String message = "SELECT show_nr FROM forestilling WHERE " 
            + "film='" + film + "' AND "
            + "date='" + date + "' AND "
            + "time='" + time + "' AND "
            + "sal='" + sal + "'";

            ResultSet rs = statement.executeQuery(message);  
            while (rs.next())
            {
                forestillings_nr = rs.getString("show_nr");
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
        return forestillings_nr;
    }
    
    /**
     * Denne metode henter den pågælden sal ud fra databasen ved brug af forestillings nummeret.
     * @param   show_nr er det pågældende forestillingsnummer
     */
    public String getSal(String show_nr)
    {
        Connection connection = null; 
        Statement statement = null;
        
        String sal = "";
        try { 
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());    // Register driver 
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // Open connection 
            statement = connection.createStatement(); // Create statement
            
            String message = "SELECT sal FROM forestilling WHERE " 
            + "show_nr='" + show_nr + "'";

            ResultSet rs = statement.executeQuery(message);  
            while (rs.next())
            {
                sal = rs.getString("sal");
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
        return sal;
    }
}

