/**
 * [SearchBar.java]
 * Custom JTextField
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javax.swing.*;
import java.awt.*;

public class SearchBar extends JTextField {

    /**
     * SearchBar
     * constructor for this class
     */
    SearchBar() {
        super(25);
        this.setFont(new Font("Dialog", Font.PLAIN, 14));
        TextPrompt tp = new TextPrompt("Search", this);
        tp.changeAlpha(0.5f);
        ImageIcon imageIcon = new ImageIcon("src/res/Search.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newImage = image.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newImage);
        tp.setIcon(imageIcon);
    }

    /**
     * paintComponent
     * paints the search bar
     * @param g Graphics object
     */
    protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        super.paintComponent(g);
    }

    /**
     * paintBorder
     * paints the border of the search bar
     * @param g Graphics object
     */
    protected void paintBorder(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
    }

    /**
     * getSearched
     * gets the text from the search bar
     * @return text String object
     */
    public String getSearched() {
        return getText();
    }
}