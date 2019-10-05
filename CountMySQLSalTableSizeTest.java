import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class CountMySQLSalTableSizeTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class CountMySQLSalTableSizeTest
{
    /**
     * Default constructor for test class CountMySQLSalTableSizeTest
     */
    public CountMySQLSalTableSizeTest()
    {
    }

    CountMySQLSalTableSize cMSTS;
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        cMSTS = new CountMySQLSalTableSize();
    }

    @Test
    public void test_rowSize()
    {
        boolean requestDidHappen = true;
        //int rowSize(String sal)
        int row = cMSTS.rowSize("sal_1");
        if(row != 10){
            requestDidHappen = false;
        }
        assertEquals(true,requestDidHappen);
    }
    
    @Test
    public void test_lastColumnName()
    {
        boolean requestDidHappen = true;
        //String lastColumnName(String sal)
        String rowName = cMSTS.lastColumnName("sal_1");
        if(!rowName.equals("i")){
            requestDidHappen = false;
        }
        assertEquals(true,requestDidHappen);
    }
    
    @Test
    public void test_lastColumnNumber()
    {
        boolean requestDidHappen = true;
        //int lastColumnNumber(String sal)
        int rowNr = cMSTS.lastColumnNumber("sal_1");
        if(rowNr != 9){
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
        cMSTS = null;
    }
}
