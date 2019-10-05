import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;

public class Gui extends JComponent {
    private  JFrame frame; //main frame
    private Container contentPane;
    private JTabbedPane tabbedPane; //tabs

    private GetForestillingListe gFL = new GetForestillingListe(); //object to access database

    private JTextField textFieldEditNavn; //edit textfields
    private JTextField textFieldEditTlfnr;
    private ArrayList<String[]> parametersArrayList = new ArrayList<String[]>(); //ArrayList for the edit pane

    private int traversalKey = 0; //key to determine path
    private String currentlySelectedFilm = ""; //selected film
    private String currentlySelectedSal = ""; //selected sal
    private String onlyDateString = ""; //selected date
    private String onlyTimeString = ""; //selected time
    private String[] filmArray = new String[]{"Ikke en film"}; //make an array of a default value for film
    private String[][] dateTimeArray = new String[][]{{"Ikke en dato"},{"Ikke en tid"},{"Ikke en sal"}}; //make an array of default values for date, time and sal
    private String forestillingsNumber = "0"; //code for the specific showing

    private int tab4Flag = 0; //flag to only runs parts of tab4 later in the program cycle
    private JTextField textFieldNavn; //textfields for tab4
    private JTextField textFieldTlfnr;
    private static JLabel seatNumberSelectedLabel; //label for displaying number of seats selected
    /**
     * Constructor
     */
    public Gui(){
        makeFrame();
    }

    private void makeFrame(){

        frame = new JFrame("Booking System");

        contentPane = frame.getContentPane();  

        frame.setSize(950,900);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane.add(returntabbedPane());

        frame.setVisible(true);
    }

    public static JLabel returnSeatNumberSelectedLabel(){
        return seatNumberSelectedLabel;
    }

    private JTabbedPane returntabbedPane(){
        tabbedPane = new JTabbedPane();

        //Removes traversal keys(left & right arrows) from tabs
        tabbedPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("LEFT"), "none");
        tabbedPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("RIGHT"), "none");

        //adds tabs
        tabbedPane.addTab("", null, returntab0Panel(), "Film or time"); //(Name, icon, panel, tooltip)
        tabbedPane.addTab("", null, returntab1Panel(), "Edit an existing reservation"); 
        tabbedPane.addTab("", null, returntab2Panel(), "Choose a film");
        tabbedPane.addTab("", null, returntab3Panel(), "Choose a date and time");
        tabbedPane.addTab("", null, returntab4Panel(), "Make a new reservation");

        //set text and size of tabs
        JLabel tabText0 = new JLabel("Film or time");
        JLabel tabText1 = new JLabel("Edit reservation");
        JLabel tabText2 = new JLabel("Choose film");
        JLabel tabText3 = new JLabel("Choose date & time");
        JLabel tabText4 = new JLabel("Reservation");

        tabText0.setPreferredSize(new Dimension(100,75));
        tabText1.setPreferredSize(new Dimension(100,75));
        tabText2.setPreferredSize(new Dimension(100,75));
        tabText3.setPreferredSize(new Dimension(100,75));
        tabText4.setPreferredSize(new Dimension(100,75));

        tabbedPane.setTabComponentAt(0, tabText0);
        tabbedPane.setTabComponentAt(1, tabText1);
        tabbedPane.setTabComponentAt(2, tabText2);
        tabbedPane.setTabComponentAt(3, tabText3);
        tabbedPane.setTabComponentAt(4, tabText4);

        //disables them, meaning unclickable
        for(int i = 0; i < tabbedPane.getTabCount(); i++){
            tabbedPane.setEnabledAt(i, false); 
        }
        return tabbedPane;
    }

    private JPanel returntab0Panel(){
        //Choose time or film panel
        JPanel tab0Panel = new JPanel();
        tab0Panel.setLayout(new GridLayout(0,2));
        JPanel tab0PanelBorder = new JPanel();
        tab0PanelBorder.setLayout(new BorderLayout());
        //1st button
        JButton timeFirstButton = new JButton("Vælg tid først");
        timeFirstButton.setFont(new Font("Arial", Font.PLAIN, 32));
        timeFirstButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        timeFirstButton.addActionListener(e -> {
                traversalKey = 1;
                //get all date, times and sale
                changeDateTimeTo(gFL.madeYourFirstChoice("date",""));

                tabbedPane.setSelectedIndex(3);
            });
        //2nd button
        JButton filmFirstButton = new JButton("Vælg film først");
        filmFirstButton.setFont(new Font("Arial", Font.PLAIN, 32));
        filmFirstButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        filmFirstButton.addActionListener(e -> {
                traversalKey = 2;
                //select all films here
                changeFilmTo(gFL.requestFilm());

                tabbedPane.setSelectedIndex(3);
            });
        //3rd button
        JButton editButton = new JButton("Rediger en bestilling");
        editButton.setFont(new Font("Arial", Font.PLAIN, 32));
        editButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        editButton.addActionListener(e -> {
                tabbedPane.setSelectedIndex(1);
            });
        editButton.setPreferredSize(new Dimension(1, 250)); //the 1 means nothing, button gets stretched to framesize
        tab0Panel.add(timeFirstButton);
        tab0Panel.add(filmFirstButton); 
        tab0PanelBorder.add(tab0Panel, BorderLayout.CENTER);
        tab0PanelBorder.add(editButton, BorderLayout.PAGE_END);
        return tab0PanelBorder;
    }

    private JPanel returntab1Panel(){
        //Edit panel
        JPanel tab1Panel = new JPanel(); 
        tab1Panel.setLayout(new BorderLayout());
        //Middle part, where the info is shown
        JPanel tab1PanelMiddle = new JPanel();
        tab1PanelMiddle.setLayout(new BoxLayout(tab1PanelMiddle, BoxLayout.PAGE_AXIS)); 

        tab1Panel.add(tab1PanelMiddle, BorderLayout.CENTER);

        //The top part with name, nr input and button
        JPanel tab1PanelTop = new JPanel();
        FlowLayout tab1PanelTopLayout = new FlowLayout();
        tab1PanelTopLayout.setAlignment(FlowLayout.LEADING);
        tab1PanelTop.setLayout(tab1PanelTopLayout);

        tab1PanelTop.add(new JLabel("Navn:"));
        textFieldEditNavn = new JTextField(25);
        tab1PanelTop.add(textFieldEditNavn);
        tab1PanelTop.add(new JLabel("Tlf nr:"));
        textFieldEditTlfnr = new JTextField(15);
        tab1PanelTop.add(textFieldEditTlfnr);

        JButton confirmButton = new JButton("Bekræft");
        tab1PanelTop.add(confirmButton);
        confirmButton.addActionListener(e -> {
                EditBooking EB = new EditBooking();
                //gets booking nr
                String[] bookingNumre = EB.providePersonalInfo(textFieldEditNavn.getText(), textFieldEditTlfnr.getText()); 
                //gets info from booking nr
                String[][] output = EB.getBookedTickets(bookingNumre);
                try {
                    CheckInput check = new CheckInput();
                    check.checkName(textFieldEditNavn.getText());
                    check.checkNameDatabase(textFieldEditNavn.getText());
                    check.checkPhoneNr(textFieldEditTlfnr.getText());
                    check.checkPhoneNrDatabase(textFieldEditTlfnr.getText());
                } catch(IllegalInputException ex) {
                    return;
                }
                //disabler textFields & button
                textFieldEditNavn.setEnabled(false);
                textFieldEditTlfnr.setEnabled(false);
                confirmButton.setEnabled(false);

                for(int i = 0; i < output.length; i++){
                    //Iterates equal to the number of bookings
                    JPanel tempPanel = new JPanel();

                    FlowLayout tempPanelLayout = new FlowLayout();
                    tempPanelLayout.setAlignment(FlowLayout.LEADING);
                    tempPanel.setLayout(tempPanelLayout);
                    String t1 = "";
                    String t2 = "";
                    String t3 = "";
                    String t4 = "";

                    String tempString = "";
                    //byg en string
                    for(int j = 0; j < output[i].length; j++){ 
                        switch(j){
                            case 0:
                            tempString += "Booking nr: " + output[i][j];
                            t1 = output[i][j];
                            break;
                            case 1:
                            tempString += " - Show nr: " + output[i][j];
                            t2 = output[i][j];
                            break;
                            case 2:
                            tempString += " - Film: " + output[i][j];
                            break;
                            case 3:
                            tempString += " - Dato: " + output[i][j];
                            break;
                            case 4:
                            tempString += " - Tid: " + output[i][j];
                            break;
                            case 5:
                            tempString += " - Sal: " + output[i][j];
                            break;
                            case 6:
                            tempString += " - Række: " + output[i][j];
                            t3 = output[i][j];
                            break;
                            case 7:
                            tempString += " - Sæde: " + output[i][j];
                            t4 = output[i][j];
                            break;
                            default:                       
                            break;
                        }
                    }
                    JButton tempButton = new JButton("Slet");
                    //sets the name of the button to the current iteration
                    tempButton.setName(String.valueOf(i));
                    //adds the info needed to delete a booking to this arraylist
                    parametersArrayList.add(new String[]{t1,t2,t3,t4});

                    tempButton.addActionListener(q -> {
                            //the name is then used to determine which button is pressed
                            int nameInt = Integer.parseInt(tempButton.getName());
                            String[] parametersArray = new String[4];
                            parametersArray = parametersArrayList.get(nameInt);

                            //deletes then specific button
                            EB.deleteOneBooking(parametersArray[0], parametersArray[1], parametersArray[2], parametersArray[3]);
                            tempButton.setText("Slettet");
                            tempButton.setEnabled(false);
                        });
                    JLabel tempLabel = new JLabel();
                    tempLabel.setText(tempString);

                    tab1PanelMiddle.add(tempPanel);
                    tempPanel.add(tempLabel);
                    tempPanel.add(tempButton);

                }
                //Updates the middle panel to display the right info
                tab1PanelMiddle.validate();
                tab1PanelMiddle.repaint();
            });

        //Bottom panel and exit button
        JButton exitButton = new JButton("Afslut");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 24));
        exitButton.setPreferredSize(new Dimension(150, 50));
        exitButton.addActionListener(e -> {
                System.exit(0);
            });

        FlowLayout tab1PanelBottomLayout = new FlowLayout();
        tab1PanelBottomLayout.setAlignment(FlowLayout.TRAILING);

        JPanel tab1PanelBottom = new JPanel();
        tab1PanelBottom.setLayout(tab1PanelBottomLayout);

        tab1PanelBottom.add(exitButton);
        tab1Panel.add(tab1PanelBottom, BorderLayout.PAGE_END);

        tab1Panel.add(tab1PanelTop, BorderLayout.PAGE_START);
        return tab1Panel;
    }

    private void setFilmArray(String[] x){
        //Changes the current array of film
        filmArray = x;
    }

    private String[] getFilmArray(){
        return filmArray;
    }

    private JPanel returntab2Panel(){
        //Panel with film
        JPanel tab2Panel = new JPanel();
        tab2Panel.setLayout(new GridLayout(0,2));

        for(int i = 0; i < getFilmArray().length; i++){
            //Add buttons to Gridlayout
            JButton filmButton = new JButton(getFilmArray()[i]);
            filmButton.setFont(new Font("Arial", Font.PLAIN, 24));
            filmButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            filmButton.addActionListener(e -> {
                    switch(traversalKey){
                        case 1: //dateTime first                       
                        //get filmtitlen
                        currentlySelectedFilm = filmButton.getText().substring(0, filmButton.getText().lastIndexOf(' ')); 
                        //get salen
                        currentlySelectedSal = filmButton.getText().substring(filmButton.getText().lastIndexOf(' ')+1, (filmButton.getText().length())); 

                        tab4Flag = 1;
                        changetab4Panel();
                        tabbedPane.setSelectedIndex(4);
                        break;
                        case 2:
                        //film først
                        currentlySelectedFilm = filmButton.getText();
                        changeDateTimeTo(gFL.madeYourFirstChoice("film", currentlySelectedFilm));

                        //Special instance of weird labeling
                        JLabel tabTextSpecial = new JLabel("Choose film");
                        tabTextSpecial.setPreferredSize(new Dimension(100,75));
                        tabbedPane.setTabComponentAt(2, tabTextSpecial);

                        tabbedPane.setSelectedIndex(3);                
                        break;
                        default:
                        break;
                    }
                });
            tab2Panel.add(filmButton);
        }
        return tab2Panel;
    }

    private JPanel returntab3Panel(){
        //Date, time and sal

        JPanel tab3Panel = new JPanel();
        tab3Panel.setLayout(new GridLayout(0,2));

        //The weird order dobbeltArray
        for(int j = 0; j < getdateTimeArray()[0].length; j++){
            String str = "";

            for(int i = 0; i < 2; i++){
                //build a label
                str += (getdateTimeArray()[i][j] + " ");
            }

            JButton dateTimeButton = new JButton(str);
            dateTimeButton.setFont(new Font("Arial", Font.PLAIN, 24));

            Integer jInteger = new Integer(j);
            dateTimeButton.setName(jInteger.toString());
            dateTimeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            dateTimeButton.addActionListener(e -> {
                    switch(traversalKey){
                        case 1:
                        //Date, time and sal først

                        //seperates into date and time strings
                        onlyDateString = dateTimeButton.getText().substring(0, 8);

                        onlyTimeString = dateTimeButton.getText().substring(9, 14); 

                        //gets specific films
                        String[][] tempStringDoubleArray = gFL.requestFilmFromDate(onlyDateString,onlyTimeString);

                        String[] tempStringArray = new String[tempStringDoubleArray.length];
                        //Converts the double array to a single array
                        for(int x = 0; x < tempStringDoubleArray.length; x++){
                            for(int y = 0; y < 2; y++){
                                if(y == 0) tempStringArray[x] = ((tempStringDoubleArray[x][y]) + " ");
                                else tempStringArray[x] += (tempStringDoubleArray[x][y]);
                            }
                        }

                        changeFilmTo(tempStringArray); 
                        tabbedPane.setSelectedIndex(3);
                        break;
                        case 2:
                        currentlySelectedSal = getdateTimeArray()[2][Integer.parseInt(dateTimeButton.getName())];
                        onlyDateString = dateTimeButton.getText().substring(0, 8);
                        onlyTimeString = dateTimeButton.getText().substring(9, 14); 

                        tab4Flag = 1;
                        changetab4Panel();
                        tabbedPane.setSelectedIndex(4);                
                        break;
                        default:
                        break;
                    }
                });
            tab3Panel.add(dateTimeButton);
        }
        return tab3Panel;
    }

    private void setdateTimeArray(String[][] x){
        //changes the dateTimeArray
        dateTimeArray = x;
    }

    private String[][] getdateTimeArray(){
        return dateTimeArray;
    }

    public void changeDateTimeTo(String[][] i){
        setdateTimeArray(i);

        tabbedPane.remove(4); //delete seats panel & dateTime panel
        tabbedPane.remove(3);
        //adds seats panel & dateTime panel back with new variables
        tabbedPane.addTab("", null, returntab3Panel(), "Choose a date and time");
        JLabel tabTextNew = new JLabel("Choose date & time");
        tabTextNew.setPreferredSize(new Dimension(100,75));
        tabbedPane.setTabComponentAt(3, tabTextNew);

        tabbedPane.addTab("", null, returntab4Panel(), "Make a new reservation");
        JLabel tabTextNew2 = new JLabel("Reservation");
        tabTextNew2.setPreferredSize(new Dimension(100,75));
        tabbedPane.setTabComponentAt(4, tabTextNew2);

        tabbedPane.setEnabledAt(3, false);
        tabbedPane.setEnabledAt(4, false);
    }

    public void changeFilmTo(String[] i){

        setFilmArray(i);

        tabbedPane.remove(4); //deletes seats panel & film panel
        tabbedPane.remove(2);
        //adds seats panel & film panel
        tabbedPane.addTab("", null, returntab2Panel(), "Choose a film");
        JLabel tabTextNew = new JLabel("Choose film");
        tabTextNew.setPreferredSize(new Dimension(100,75));
        tabbedPane.setTabComponentAt(3, tabTextNew); //the 3 is intentional, the timeDate and film panes switch position here

        tabbedPane.addTab("", null, returntab4Panel(), "Make a new reservation");
        JLabel tabTextNew2 = new JLabel("Reservation");
        tabTextNew2.setPreferredSize(new Dimension(100,75));
        tabbedPane.setTabComponentAt(4, tabTextNew2);

        tabbedPane.setEnabledAt(3, false);
        tabbedPane.setEnabledAt(4, false);
    }

    private JPanel returntab4Panel(){
        JPanel tab4Panel = new JPanel(new BorderLayout());

        //lærred label
        JLabel screenLabel = new JLabel("Lærred", SwingConstants.CENTER);
        screenLabel.setLocation(100,100);
        screenLabel.setBackground(Color.BLACK);
        screenLabel.setForeground(Color.WHITE);
        screenLabel.setOpaque(true);
        tab4Panel.add(screenLabel);

        //bottom part
        tab4Panel.add(returntab4Bottom(), BorderLayout.PAGE_END);
        //flag is needed because some code should only run after selecting film and timeDate
        if(tab4Flag == 1){
            //gets forestillingsNummer
            forestillingsNumber = gFL.getForestillingsNummer(currentlySelectedFilm, onlyDateString, onlyTimeString, currentlySelectedSal);
        }

        SalWithBookings sWB = new SalWithBookings();
        if(tab4Flag == 1){
            //draws sal
            tab4Panel.add(Seats.drawSeats(sWB.requestWithBookings(forestillingsNumber)));

            //dynamisk sizechange af screenLabel til biograf size, når seats tegnes
            //60 er seatSize + seatSpace. 100 er fordi screenLabel skal være 50 større end seats i hver side. 13 er højden.
            screenLabel.setSize(sWB.requestWithBookings(forestillingsNumber)[0].length * 60 - 20 + 100, 13);
        }
        return tab4Panel;
    }

    private JPanel returntab4Bottom(){
        JPanel tab4Bottom = new JPanel();

        //Set layout
        FlowLayout tab4BottomLayout = new FlowLayout();
        tab4Bottom.setLayout(tab4BottomLayout);
        tab4BottomLayout.setAlignment(FlowLayout.TRAILING);
        //make the label with default value 0
        seatNumberSelectedLabel = new JLabel("You have selected " + 0 + " seat(s)   ");
        tab4Bottom.add(seatNumberSelectedLabel);

        //textFields
        tab4Bottom.add(new JLabel("Navn:"));
        textFieldNavn = new JTextField(25);
        tab4Bottom.add(textFieldNavn);
        tab4Bottom.add(new JLabel("Tlf nr:"));
        textFieldTlfnr = new JTextField(15);
        tab4Bottom.add(textFieldTlfnr);

        //add btn
        tab4Bottom.add(returnbtn());
        return tab4Bottom;
    }

    public void changetab4Panel(){
        tabbedPane.remove(4); //delete seats panel
        //adds seats panel
        tabbedPane.addTab("", null, returntab4Panel(), "Make a new reservation");
        JLabel tabTextNew2 = new JLabel("Reservation");
        tabTextNew2.setPreferredSize(new Dimension(100,75));
        tabbedPane.setTabComponentAt(4, tabTextNew2);

        tabbedPane.setEnabledAt(4, false);
    }

    private JButton returnbtn(){
        JButton btn = new JButton("Bekræft reservation");
        btn.setPreferredSize(new Dimension(150, 50));
        btn.addActionListener(e -> {
                try {
                    CheckInput check = new CheckInput();
                    check.checkName(textFieldNavn.getText());
                    check.checkPhoneNr(textFieldTlfnr.getText());
                } catch(IllegalInputException ex) {
                    return;
                }

                if(Seats.returnai().size() > 0){
                    //Saves booking
                    MakeBooking MB = new MakeBooking();
                    MB.request(flipArrayList(Seats.returnai()),forestillingsNumber, textFieldNavn.getText(), textFieldTlfnr.getText());
                    //closes the program
                    System.exit(0);
                }
                else{
                    //creates an error msg
                    JOptionPane.showMessageDialog(null, "Choose a seat", "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        return btn;
    }

    private ArrayList<Integer[]> flipArrayList(ArrayList<Integer[]> i){
        //flip the values in Integer[], of size 2, in an arraylist
        //is needed for saving a booking to the database. Because the x and y values need to be flipped.
        ArrayList<Integer[]> returnArrayList = new ArrayList<Integer[]>();
        for(Integer[] q : i){
            Integer[] newIntegerArray = new Integer[2];
            newIntegerArray[0] = q[1];
            newIntegerArray[1] = q[0];
            returnArrayList.add(newIntegerArray);
        }
        return returnArrayList;
    }
}