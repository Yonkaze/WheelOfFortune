package proj1.shuqi;

import javax.swing.*;

/**
 * Created by ShuqiChen on 16/12/24.
 */
public class WheelSpace {


    private int spaceValue;
    private ImageIcon imageIcon;

    public WheelSpace(final int spaceValue, final ImageIcon imageIcon) {
        this.spaceValue = spaceValue;
        this.imageIcon = imageIcon;
    }

    public int getSpaceValue() {
        return spaceValue;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

}
