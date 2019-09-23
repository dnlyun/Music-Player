/**
 * [ControlButton.java]
 * Custom JButton
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javax.swing.*;
import java.awt.*;

public class ControlButton extends JButton {

    /**
     * ControlButton
     * constructor that runs the class
     * @param image ImageIcon object
     */
    ControlButton(ImageIcon image) {
        super(image);
        super.setBackground(new Color(0, 0, 0, 0f));
        super.setPreferredSize(new Dimension(37, 37));
    }
}