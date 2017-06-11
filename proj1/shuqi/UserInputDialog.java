package proj1.shuqi;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Created by ShuqiChen on 16/12/24.
 */
public class UserInputDialog extends JDialog{

    JTextField nameField;
    JButton okButton;

    public UserInputDialog(JFrame mainFrame, String title, int playerNumber) {
        super(mainFrame, title, true);

        setLayout(new FlowLayout());

        if (playerNumber == -1) {
            add(new JLabel("Enter your guess: "));
        }

        //for player to guess the puzzle
        else {
            add(new JLabel("Enter player name for #" + playerNumber));

        }
        nameField = new JTextField(20);
        add(nameField);

        okButton = new JButton(" Submit ");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getName().isEmpty()) {
                    return;
                }
                setVisible(false);
            }
        });

        add(okButton);

        pack();
        setVisible(true);
    }

    public String getName() {
        return (nameField.getText());
    }
}
