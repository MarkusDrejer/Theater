import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * The test class ConversionTest
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ConversionTest
{
    /**
     * Default constructor for test class ConversionTest
     */
    public ConversionTest()
    {
    }

    Conversion conv;
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        conv = new Conversion();
    }

    @Test
    public void test_toDoubleArray()
    {
        ArrayList<Integer[]> arr = new ArrayList<Integer[]>();
        Integer[] p = new Integer[2];
        p[0] = 0;
        p[1] = 1;
        arr.add(p);
        Integer[] p2 = new Integer[2];
        p2[0] = 0;
        p2[1] = 2;
        arr.add(p2);
        Integer[] p3 = new Integer[2];
        p3[0] = 4;
        p3[1] = 2;
        arr.add(p3);
        boolean requestDidHappen = true;
        //String[][] toDoubleArray(ArrayList<Integer[]> list)
        String[][] array = conv.toDoubleArray(arr);
        //if(array
        if(!array[0][0].equals("a") || !array[0][1].equals("1") || 
        !array[1][0].equals("a") || !array[1][1].equals("2") || 
        !array[2][0].equals("e") || !array[2][1].equals("2")){
            requestDidHappen = false;
        }
        assertEquals(true,requestDidHappen);
    }
    
    @Test
    public void test_toChar()
    {
        boolean requestDidHappen = true;
        //String toChar(String stringValue)
        String letter1 = conv.toChar("10");
        String letter2 = conv.toChar("0");
        if(!letter1.equals("k") || !letter2.equals("a")){
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
        conv = null;
    }
}
