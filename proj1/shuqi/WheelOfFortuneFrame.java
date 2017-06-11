package proj1.shuqi;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URISyntaxException;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created by ShuqiChen on 16/12/24.
 */
public class WheelOfFortuneFrame extends JFrame {
    private static final String PLAYER_NAME_INPUT = "Player Name Input";
    private static final String PUZZLE_INPUT = "Puzzle Input";
    private static final String SOLVE_PUZZLE_TITLE = "Solve the Puzzle";
    private static final String IMAGE_EXTENSION = "jpg";
    private static final int NUM_WHEEL_SPACES = 24;
    private static final int LOSE_A_TURN = -1;
    private static final int BANKRUPT = -2;
    private static final int VOWEL_DEBT = -250;
    private Player[] players;
    private int numPlayers;
    private WheelSpace[] wheelSpaces;
    private String thePuzzle;
    private String upperCasePuzzle;
    private JLabel underline = new JLabel();
    private char[] hint;

    private int indexOfPlayer = 0;
    private int amountOfWheel;

    private JButton[] buttons;
    private boolean[] isButtonPressed = new boolean[26];
    private JButton buyButton;
    private JButton spinButton;
    private JButton solveButton;
    private JLabel imageLabel;
    private int wheelSpaceIndex = 0;
    private Random randomGenerator;

    private static class WheelSpaceImageFilter implements FileFilter

    {

        String prefix;  //The prefix of the filename we're looking

        //for - what comes before the first underscore


        WheelSpaceImageFilter(int inPref)

        {

            //Sets the prefix member to string version of space number

            prefix = new Integer(inPref).toString();

        }


        //Test whether the file provided should be accepted by our

        //file filter. In the FileFilter interface.

        public boolean accept(File fname)

        {

            boolean isAccepted = false;


            //Accepted if matched "<prefix>_<...>.jpg" where IMAGE_EXTENSION

            //is assumed to be "jpg" for this example

            if (fname.getName().startsWith(prefix + "_") &&

                    fname.getName().endsWith("." + IMAGE_EXTENSION))

            {

                isAccepted = true;

            }


            return (isAccepted);

        }


        //Parses the provided filename to determine the dollar value

        //associated with this wheel space image's filename.

        public static int getSpaceValue(File fname)

        {
            if (fname.getName().toLowerCase().contains("loseaturn")) {
                return LOSE_A_TURN;
            } else if (fname.getName().toLowerCase().contains("bankrupt")) {
                return BANKRUPT;
            }

            int start = fname.getName().indexOf('_') + 1;
            int end = fname.getName().indexOf('.');
            return Integer.valueOf(fname.getName().substring(start, end));
        }

    }

    private void findImageFiles() {
        File[] fileList;

        File myDir = null;

        int i;


        //Allocate array for number of spaces, which is set to a constant

        //for now as opposed to being able to change run-to-run

        wheelSpaces = new WheelSpace[NUM_WHEEL_SPACES];


        //Get a File object for the directory containing the images

        try

        {
            myDir = new File(getClass().getClassLoader().getResource(
                    "proj1/shuqi/images").toURI());

        } catch (URISyntaxException uriExcep)

        {

            System.out.println("Caught a URI syntax exception");

            System.exit(4); //Just bail for simplicity in this project

        }


        //Loop from 1 to the number of spaces expected, so we can look

        //for files named <spaceNumber>_<value>.jpg.  Note: Space numbers

        //in image filenames are 1-based, NOT 0-based.

        for (i = 1; i <= NUM_WHEEL_SPACES; i++)

        {

            //Get a listing of files named appropriately for an image

            //for wheel space #i.  There should only be one, and this

            //will be checked below.

            fileList = myDir.listFiles(new WheelSpaceImageFilter(i));


            if (fileList.length == 1)

            {

                //System.out.println("Space: " + i + " img: " + fileList[0] +

                //        " val: " + WheelSpaceImageFilter.getSpaceValue(fileList[0]));

                //Index starts at 0, space numbers start at 1 -- hence the "- 1"

                wheelSpaces[i - 1] = new WheelSpace(

                        WheelSpaceImageFilter.getSpaceValue(fileList[0]),

                        new ImageIcon(fileList[0].toString()));

            } else

            {

                System.out.println("ERROR: Invalid number of images for space: " + i);

                System.out.println("       Expected 1, but found " + fileList.length);

            }

        }
    }

    WheelOfFortuneFrame(final int randomSeedVal, final int numPlayers) {
        randomGenerator = new Random(randomSeedVal);
        this.numPlayers = numPlayers;
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            UserInputDialog playerInputDialog = new UserInputDialog(this, PLAYER_NAME_INPUT, i);
            players[i] = new Player(playerInputDialog.getName());
        }

        PuzzleInputDialog puzzleInputDialog = new PuzzleInputDialog(this, PUZZLE_INPUT);
        thePuzzle = puzzleInputDialog.getPuzzle();
        upperCasePuzzle = thePuzzle.toUpperCase();

        findImageFiles();

        int numPuzzle = thePuzzle.length();

        hint = new char[numPuzzle];

        for (int i = 0; i < numPuzzle; i++) {
            if (Character.isLetter(thePuzzle.charAt(i))) {
                hint[i] = '_';
            } else {
                hint[i] = thePuzzle.charAt(i);
            }
        }

        underline.setText(String.valueOf(hint));

        mainFrameBorder("WheelOfFortune");

        players[0].activatePlayer();
        nextRound(false);
    }

    private void mainFrameBorder(String inTitle) {
        //topPan
        //for player
        JPanel topPan = new JPanel(new GridLayout(1, numPlayers));

        for (int i = 0; i < numPlayers; ++i) {
            topPan.add(players[i].playerPanel);
        }

        //centerPan = leftPan + rightPan
        JPanel centerPan = new JPanel(new GridLayout(1, 2));

        //leftPan
        JPanel leftPan = new JPanel(new GridLayout(3, 1));
        buyButton = new JButton(" Buy a Vowel ");
        buyButton.addActionListener(new buyButtonListener());
        spinButton = new JButton(" Spin the Wheel ");
        spinButton.addActionListener(new spinButtonListener());
        solveButton = new JButton(" Solve the Puzzle ");
        solveButton.addActionListener(new solveButtonListener());
        leftPan.add(buyButton);
        leftPan.add(spinButton);
        leftPan.add(solveButton);

        //rightPan
        JPanel rightPan = new JPanel();
        imageLabel = new JLabel(wheelSpaces[0].getImageIcon());
        rightPan.add(imageLabel);

        //centerPan = leftPan + rightPan;
        centerPan.add(leftPan);
        centerPan.add(rightPan);

        //bottomPan = tPan + bPan
        //tPan = vowelPan + consonantsPan
        //bPan = puzzle
        JPanel bottomPan = new JPanel(new BorderLayout());

        JPanel tPan = new JPanel(new FlowLayout());

        JPanel vowelPan = new JPanel(new GridLayout(3, 2));
        TitledBorder vowelPanelTitle = BorderFactory.createTitledBorder(" Vowel ");
        vowelPanelTitle.setTitleJustification(TitledBorder.LEFT);
        vowelPan.setBorder(vowelPanelTitle);

        JPanel consonantsPan = new JPanel(new GridLayout(3, 7));
        TitledBorder consonantsPanelTitle = BorderFactory.createTitledBorder(" Consonants ");
        consonantsPanelTitle.setTitleJustification(TitledBorder.LEFT);
        consonantsPan.setBorder(consonantsPanelTitle);

        //26 buttons
        buttons = new JButton[26];
        for (int i = 0; i < 26; i++) {
            buttons[i] = new JButton(Character.toString((char) ('A' + i)));
            buttons[i].addActionListener(new LetterButtonListener());
            if ((char) ('A' + i) == 'A' || (char) ('A' + i) == 'E' || (char) ('A' + i) == 'I' ||
                    (char) ('A' + i) == 'O' || (char) ('A' + i) == 'U') {
                vowelPan.add(buttons[i]);
            } else {
                consonantsPan.add(buttons[i]);
            }
        }
        tPan.add(vowelPan);
        tPan.add(consonantsPan);

        //bPan: Puzzle
        JPanel bPan = new JPanel(new FlowLayout());
        bPan.add(underline);

        bottomPan.add(tPan, BorderLayout.NORTH);
        bottomPan.add(bPan, BorderLayout.SOUTH);


        setLayout(new BorderLayout());

        add(topPan, BorderLayout.NORTH);
        add(centerPan, BorderLayout.CENTER);
        add(bottomPan, BorderLayout.SOUTH);

    }

    private void setVowelButtonsEnabled(boolean setTo) {
        for (int i = 0; i < 26; i++) {
            if ((char) ('A' + i) == 'A' || (char) ('A' + i) == 'E' || (char) ('A' + i) == 'I' ||
                    (char) ('A' + i) == 'O' || (char) ('A' + i) == 'U') {
                if (!isButtonPressed[i]) {
                    buttons[i].setEnabled(setTo);
                }
            }
        }
    }

    private void setConsonantButtonsEnabled(boolean setTo) {
        for (int i = 0; i < 26; i++) {
            if (!((char) ('A' + i) == 'A' || (char) ('A' + i) == 'E' || (char) ('A' + i) == 'I' ||
                    (char) ('A' + i) == 'O' || (char) ('A' + i) == 'U')) {
                if (!isButtonPressed[i]) {
                    buttons[i].setEnabled(setTo);
                }
            }
        }
    }

    private void nextRound(boolean isNextPlayer) {
        spinButton.setEnabled(true);
        solveButton.setEnabled(true);

        if (isNextPlayer) {
            players[indexOfPlayer].deactivatePlayer();
            indexOfPlayer = (indexOfPlayer + 1) % players.length;
            players[indexOfPlayer].activatePlayer();
            repaint();
        }

        if (players[indexOfPlayer].amount >= -VOWEL_DEBT) {
            buyButton.setEnabled(true);
        } else {
            buyButton.setEnabled(false);
        }

        setConsonantButtonsEnabled(false);
        setVowelButtonsEnabled(false);
    }

    private void setAllActionButtonsDisabled() {
        spinButton.setEnabled(false);
        buyButton.setEnabled(false);
        solveButton.setEnabled(false);
    }

    public class buyButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buyButton) {
                setVowelButtonsEnabled(true);
                setAllActionButtonsDisabled();
            }
        }
    }

    public class spinButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == spinButton) {
                setConsonantButtonsEnabled(true);
                setAllActionButtonsDisabled();
                wheelSpaceIndex = randomGenerator.nextInt(NUM_WHEEL_SPACES);
                imageLabel.setIcon(wheelSpaces[wheelSpaceIndex].getImageIcon());
                amountOfWheel = wheelSpaces[wheelSpaceIndex].getSpaceValue();
                if (amountOfWheel == BANKRUPT) {
                    players[indexOfPlayer].amountToZero();
                    nextRound(true);
                } else if (amountOfWheel == LOSE_A_TURN) {
                    nextRound(true);
                }
            }
        }
    }

    public class solveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == solveButton) {
                //disable all buttons
                setAllActionButtonsDisabled();

                //pop up a dialog to input the guessing
                UserInputDialog solvePuzzel = new UserInputDialog
                        (WheelOfFortuneFrame.this, SOLVE_PUZZLE_TITLE, -1);

                if (solvePuzzel.getName().toUpperCase().equals(upperCasePuzzle)) {
                    JOptionPane.showMessageDialog(WheelOfFortuneFrame.this,
                            players[indexOfPlayer].name + " wins $" + players[indexOfPlayer].amount,
                            "Game Over", JOptionPane.INFORMATION_MESSAGE);

                    //exit
                    setVisible(false);
                    dispose();
                }

                else {
                    JOptionPane.showMessageDialog(WheelOfFortuneFrame.this,
                            players[indexOfPlayer].name + " has an incorrect guess",
                            "Wrong Answer", JOptionPane.ERROR_MESSAGE);

                    //next player
                    nextRound(true);
                }
            }
        }
    }

    private void changeHint(char[] newHint) {
        hint = newHint;
        underline.setText(String.valueOf(hint));

    }

    private boolean isGuessingRight(int i) {
        if (upperCasePuzzle.contains(String.valueOf((char) ('A' + i)))) {
            for (int j = 0; j < upperCasePuzzle.length(); j++) {
                if ((char)('A' + i) == upperCasePuzzle.charAt(j)) {
                    hint[j] = upperCasePuzzle.charAt(j);
                }
            }
            changeHint(hint);
            return true;
        }
        return false;
    }

    public class LetterButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            for (int i = 0; i < 26; i++) {
                if (e.getSource() == buttons[i]) {
                    boolean isGuessingRight = isGuessingRight(i);
                    isButtonPressed[i] = true;
                    buttons[i].setEnabled(false);
                    //if vowel
                    if ((char) ('A' + i) == 'A' || (char) ('A' + i) == 'E' || (char) ('A' + i) == 'I' ||
                            (char) ('A' + i) == 'O' || (char) ('A' + i) == 'U') {
                        players[indexOfPlayer].changeAmount(VOWEL_DEBT);
                    } else {
                        if (isGuessingRight) {
                            // award the money
                            players[indexOfPlayer].changeAmount(wheelSpaces[wheelSpaceIndex].getSpaceValue());
                        }
                    }
                    if (!isGuessingRight) {
                        nextRound(true);
                    } else {
                        nextRound(false);
                    }
                }

            }
        }

    }


}
