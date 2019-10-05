import java.util.ArrayList;
public class Conversion
{
    public static ArrayList<Integer[]> ar = new ArrayList<Integer[]>(); 
    
    /**
     * Denne Constructor initialsiere en arrayListe, mest brugt til testing
     */
    public Conversion()
    {
    }

    public void ConversionTESTING()
    {
        Integer[] t = new Integer[2];
        t[0] = 0;
        t[1] = 1;
        ar.add(t);
        Integer[] t2 = new Integer[2];
        t2[0] = 0;
        t2[1] = 2;
        ar.add(t2);
        Integer[] t3 = new Integer[2];
        t3[0] = 4;
        t3[1] = 2;
        ar.add(t3);
    }
    
    /**
     * Denne metode tager imod en ArrayList af Integer Arrays og omformer den til et dobbelt array
     * Samt ændre lidt på indputtet således at det stemmer overens med biograffens rækker og sæder
     * @param list er en ArrayListe af Integer arrays
     */
    public String[][] toDoubleArray(ArrayList<Integer[]> list)
    {
        String [][] doubleArray = new String[list.size()][2];
        for(int j = 0; j < list.size(); j++)
        {
            String[] simpleArray = new String[2];
            for(int i = 0; i < 2; i++)
            {
                if(i==0)
                {
                    simpleArray[0] = toChar(list.get(j)[i].toString());
                } else {
                    simpleArray[i] = list.get(j)[i].toString();    
                }
            }
            doubleArray[j][0] = simpleArray[0];
            doubleArray[j][1] = simpleArray[1];
        }
        return doubleArray;
    }
    
    /**
     * Denne metode ændre en string med tal til bogstav værdier
     * @param stringValue er et String(af tal) der konverteres til bogstaver(0 til a)
     */
    public String toChar(String stringValue)
    {
        int tal = Integer.parseInt(stringValue);
        char bogstav = 'a';
        for(int i = 0; i < tal; i++)
        {
            bogstav++;
        }
        String hello = bogstav + "";
        return hello;
    }
}
