

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class SalWithBookingsTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SalWithBookingsTest
{
    /**
     * Default constructor for test class SalWithBookingsTest
     */
    public SalWithBookingsTest()
    {
    }
    
    SalWithBookings sWB;
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        sWB = new SalWithBookings();
    }
    
    @Test
    public void test_requestWithBookings()
    {
        boolean requestDidHappen = true;
        //int[][] requestWithBookings(String show_nr)
        int[][] array = sWB.requestWithBookings("7");
        if(array.length != 7 || array[0].length != 6){
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
        sWB = null;
    }
}
