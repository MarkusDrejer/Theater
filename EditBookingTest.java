

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class EditBookingTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class EditBookingTest
{
    /**
     * Default constructor for test class EditBookingTest
     */
    public EditBookingTest()
    {
    }

    EditBooking eB;
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        eB = new EditBooking();
    }
    
    @Test
    public void test_providePersonalInfo()
    {
        boolean requestDidHappen = true;
        //String[] providePersonalInfo(String name, String tlf_nr)
        String[] array = eB.providePersonalInfo("Timmie Nielsen","32597827");
        if(!array[0].equals("8")){
            requestDidHappen = false;
        }
        assertEquals(true,requestDidHappen);
    }
    
    @Test
    public void test_getBookedTickets()
    {
        boolean requestDidHappen = true;
        //String[][] getBookedTickets(String[] booking_nr)
        String[][] array = eB.getBookedTickets(new String[]{"8"});
        if(!array[0][0].equals("8") || !array[0][1].equals("2") || !array[0][2].equals("Rogue One: A Star Wars Story") || !array[0][3].equals("17122016") ||
        !array[0][4].equals("20:00") || !array[0][5].equals("sal_1") || !array[0][6].equals("g")){
            requestDidHappen = false;
        }
        assertEquals(true,requestDidHappen);
    }
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        eB = null;
    }
}
