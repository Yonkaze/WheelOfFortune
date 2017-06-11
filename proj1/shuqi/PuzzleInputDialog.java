package proj1.shuqi;

import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ShuqiChen on 16/12/24.
 */
public class PuzzleInputDialog extends JDialog{

    JTextField puzzleField;
    JButton okButton;

    public PuzzleInputDialog(JFrame mainFrame, String title) {

        super(mainFrame, title, true);

        setLayout(new FlowLayout());
        add(new JLabel("Enter Your Puzzle: "));
        puzzleField = new JTextField(20);
        add(puzzleField);

        okButton = new JButton(" Submit ");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getPuzzle().isEmpty() ) {
                    return;
                }
                setVisible(false);
            }
        });

        add(okButton);

        pack();
        setVisible(true);
    }

    public String getPuzzle() {
        return (puzzleField.getText());
    }

}
