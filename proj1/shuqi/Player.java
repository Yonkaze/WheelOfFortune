package proj1.shuqi;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by ShuqiChen on 16/12/24.
 */
public class Player {
    String name;
    int amount;
    private static final Border BLACK_LINE = BorderFactory.createLineBorder(Color.black);
    private static final Border RED_LINE = BorderFactory.createLineBorder(Color.red);
    JPanel playerPanel;
    TitledBorder playerPanelTitle;
    JLabel amountLabel;


    public Player(String name) {
        amount = 0;
        this.name = name;
        playerPanelTitle = BorderFactory.createTitledBorder(BLACK_LINE, name);
        playerPanelTitle.setTitleJustification(TitledBorder.LEFT);
        playerPanel = new JPanel();
        playerPanel.setBorder(playerPanelTitle);
        amountLabel = new JLabel(Integer.toString(amount));
        playerPanel.add(amountLabel);
    }

    public void changeAmount(int delta) {
        amount += delta;
        amountLabel.setText(String.valueOf(amount));
    }

    public void amountToZero() {
        amount = 0;
        amountLabel.setText(String.valueOf(amount));
    }

    public void activatePlayer() {
        playerPanelTitle.setBorder(RED_LINE);
    }

    public void deactivatePlayer() {
        playerPanelTitle.setBorder(BLACK_LINE);
    }
}
