import java.awt.*; //color
import java.awt.event.*; //mouselistener
import javax.swing.*; //jpanel
import java.util.*; //arrays

public class Seats  {
    private static final int seatSpace = 20; //space between seats
    private static final int seatSize = 40; //size of seats
    private static final int offSet = 150; // where to begin the seats, represents both x and y

    private static ArrayList<Integer[]> ai; //arrayLists of integer[] represents the selected seats
    //antallet af indre {} er antallet af rækker - outer
    //indre {} er længden af rækkerne/mængden af kolonner - inner

    private static Color seatColor;
    private static int selectedAmount = 0; //number of seats selected

    public static JPanel drawSeats(int[][] inputArray){
        JPanel myPanel = new JPanel();
        myPanel.setLayout(null);
        ai = new ArrayList<Integer[]>();

        for(int y= 0; y<inputArray.length;y++){ //number of inner arrays
            for(int x = 0; x<inputArray[y].length;x++){ //size of inner arrays
                try{
                    if(inputArray[y][x] < 0 || inputArray[y][x] > 1) throw new IllegalInputException("Seat color error, not 0 or 1");
                }
                catch(IllegalInputException e){
                    //does they input array contain a number that is not 0 or 1
                    //System.out.println("Seat color error, not 0 or 1");
                    System.exit(1);
                }
                //makes a label for each seats
                JLabel lbl = new JLabel();
                lbl.setLocation(x*seatSize+x*seatSpace+offSet,y*seatSize+y*seatSpace+offSet);
                lbl.setSize(seatSize, seatSize);
                
                //0 = free seat, red = taken seat
                if(inputArray[y][x] == 0) seatColor = Color.GREEN;  
                else seatColor = Color.RED;

                lbl.addMouseListener(new MouseAdapter(){ 
                        //code that runs when a label is clicked on
                        public void mouseClicked(MouseEvent e) //inner method
                        {  
                            if(lbl.getBackground().equals(Color.YELLOW)){
                                //Return to original color, unselect seat
                                if(inputArray[(convert(lbl.getLocation().getY())-offSet)/(seatSpace+seatSize)][(convert(lbl.getLocation().getX())-offSet)/(seatSpace+seatSize)] == 0){
                                    //if the seats corresponding number in the input array is a 0 then turn green
                                    seatColor = Color.GREEN;
                                }
                                else {
                                    //may be obsolete, a red seat never becomes yellow
                                    seatColor = Color.RED;
                                }
                                //array of size 2, hold the x, y coordinates of the seat that are about to be unselected
                                Integer[] t = new Integer[2];
                                t[0] = (convert(lbl.getLocation().getX())-offSet)/(seatSpace+seatSize);
                                t[1] = (convert(lbl.getLocation().getY())-offSet)/(seatSpace+seatSize);

                                for(Integer[] i : ai){
                                    if (Arrays.equals(t, i)){
                                        //finds the seat in ai and removes it.
                                        ai.remove(i);
                                        break;
                                    }
                                }
                                //changes the label to display the correct number
                                selectedAmount--;
                                Gui.returnSeatNumberSelectedLabel().setText("You have selected " + selectedAmount + " seat(s)   ");
                            }
                            else{
                                //Make yellow, select seat
                                if(lbl.getBackground().equals(Color.GREEN)){
                                    //if green, make yellow
                                    seatColor = Color.YELLOW;
                                    Integer[] t = new Integer[2];
                                    t[0] = (convert(lbl.getLocation().getX())-offSet)/(seatSpace+seatSize);
                                    t[1] = (convert(lbl.getLocation().getY())-offSet)/(seatSpace+seatSize);
                                    //add the seat to ai
                                    ai.add(t);
                                    //changes the label to display the correct number
                                    selectedAmount++;
                                    Gui.returnSeatNumberSelectedLabel().setText("You have selected " + selectedAmount + " seat(s)   ");
                                }
                                else seatColor = Color.RED;
                            }
                            //makes label the correct color when altered
                            lbl.setBackground(seatColor);
                            seatColor = null;
                        }  
                    });
                //sets the label color and adds it to the paneæ
                lbl.setBackground(seatColor);
                seatColor = null;
                lbl.setOpaque(true);
                myPanel.add(lbl);
            }
        }
        return myPanel;
    }

    private static int convert(double d){
        //converts the double value returned by the .getLocation().getX / .getY to an int
        int i = (int) d;
        return i;
    }

    public static ArrayList<Integer[]> returnai(){
        //returns ai
        return ai;
    }
}
