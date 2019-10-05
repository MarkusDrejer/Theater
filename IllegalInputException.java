import javax.swing.JOptionPane;
public class IllegalInputException extends RuntimeException
{
    private String s;
    public IllegalInputException(String s)
    {
        this.s = s;
        //Prints the exception-message in a pop-up box
        JOptionPane.showMessageDialog(null, this.s, "Error", JOptionPane.INFORMATION_MESSAGE);
    }
}
