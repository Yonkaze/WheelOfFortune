package proj1.shuqi;

/**
 * Created by ShuqiChen on 16/12/24.
 */
import javax.swing.JFrame;

public class WheelOfFortune
{
    public static void main(String [] args)
    {
        //This will be the main frame that contains the game interface...
        WheelOfFortuneFrame gameFrame;

        //These can be changed as desired...
        int randomSeedVal = 100;
        int numPlayers = 3;

        gameFrame = new WheelOfFortuneFrame(randomSeedVal, numPlayers);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.pack();

        //Show the game interface and start the game!
        gameFrame.setVisible(true);
    }
}
