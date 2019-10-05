

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class GetForestillngListeTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class GetForestillngListeTest
{
    /**
     * Default constructor for test class GetForestillngListeTest
     */
    public GetForestillngListeTest()
    {
    }
    
    GetForestillingListe gFL;
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        gFL = new GetForestillingListe();
    }

    @Test
    public void test_requestFilm()
    {
        String[] str = gFL.requestFilm();
        boolean duplicates = false;
        for(int i = 0; i<str.length;i++){
            for(int j = i+1; j<str.length;j++){
                if(j!=i && str[j] == str[i]){
                    duplicates = true;
                }
            }
        }
        assertEquals(false,duplicates);
    }
    
    @Test
    public void test_madeYourFirstChoice_film()
    {
        String[][] str = gFL.madeYourFirstChoice("film","Arrival");
        boolean onlyDates = true;
        for(int i = 0; i<str[0].length;i++){
            if(str[0][i].length() != 8 || !str[0][i].matches("[0-9]+")){
                onlyDates = false;
            }
        }
        boolean onlyTime = true;
        for(int i = 0; i<str[0].length;i++){
            if(str[1][i].length() != 5 || str[1][i].matches("[a-zA-Z]+") ){
                onlyTime = false;
            }
        }
        boolean onlySal = true;
        for(int i = 0; i<str[0].length;i++){
            if(str[2][i].length() != 5 || !str[2][i].contains("sal_")){
                onlySal = false;
            }
        }
        assertEquals(true,onlyDates);
        assertEquals(true,onlyTime);
        assertEquals(true,onlySal);
    }
    
    @Test
    public void test_madeYourFirstChoice_date()
    {
        String[][] str = gFL.madeYourFirstChoice("date","");
        boolean onlyDates = true;
        for(int i = 0; i<str[0].length;i++){
            if(str[0][i].length() != 8 || !str[0][i].matches("[0-9]+")){
                onlyDates = false;
            }
        }
        boolean onlyTime = true;
        for(int i = 0; i<str[0].length;i++){
            if(str[1][i].length() != 5 || str[1][i].matches("[a-zA-Z]+") ){
                onlyTime = false;
            }
        }
        assertEquals(true,onlyDates);
        assertEquals(true,onlyTime);
    }
    
    @Test
    public void test_requestFilmFromDate()
    {
        String[][] str = gFL.requestFilmFromDate("16122016","21:00");
        boolean onlyFilm = true;
        for(int i = 0; i<str.length;i++){
            if(str[i][0] == null){
                onlyFilm = false;
            }
        }
        boolean onlySal = true;
        for(int i = 0; i<str.length;i++){
            if(str[i][1].length() != 5 || !str[i][1].contains("sal_")){
                onlySal = false;
            }
        }
        assertEquals(true,onlyFilm);
        assertEquals(true,onlySal);
    }
    
    @Test
    public void test_getForestillingsNummer()
    {
        String str = gFL.getForestillingsNummer("Arrival","16122016","21:00","sal_2");
        boolean onlyShow_nr = true;
        if(str.matches("[a-zA-Z]+") || !str.equals("4")){
            onlyShow_nr = false;
        }
        assertEquals(true,onlyShow_nr);
    }
    
    @Test
    public void test_getSal()
    {
        String str = gFL.getSal("4");
        boolean onlySal = true;
        if(str.length() != 5 || !str.contains("sal_")){
            onlySal = false;
        }
        assertEquals(true,onlySal);
    }
    
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        gFL = null;
    }
}
