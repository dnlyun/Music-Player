/**
 * [TextPrompt.java]
 * Format JLabel on JTextField
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class TextPrompt extends JLabel implements FocusListener, DocumentListener {

    public enum Show {
        ALWAYS,
        FOCUS_GAINED,
        FOCUS_LOST
    }

    private JTextComponent component;
    private Document document;

    private Show show;

    /**
     * TextPromt
     * constructor for this c;ass
     * @param text text to format
     * @param component the component to add the text too
     */
    public TextPrompt(String text, JTextComponent component) {
        this.component = component;
        document = component.getDocument();
        setShow(Show.ALWAYS);

        setText(text);
        setFont(component.getFont());
        setHorizontalAlignment(JLabel.LEADING);

        component.addFocusListener(this);
        document.addDocumentListener(this);

        component.setLayout(new BorderLayout());
        component.add(this);
        checkForPrompt();
    }

    /**
     * changeAlpha
     * Changes the alpha of the text
     * @param alpha value in the range of 0 - 1.0.
     */
    public void changeAlpha(float alpha)
    {
        changeAlpha( (int)(alpha * 255) );
    }

    /**
     * changeAlpha
     * Changes the alpha of the text
     * @param alpha value in the range of 0 - 255.
     */
    public void changeAlpha(int alpha)
    {
        alpha = alpha > 255 ? 255 : alpha < 0 ? 0 : alpha;

        Color foreground = getForeground();
        int red = foreground.getRed();
        int green = foreground.getGreen();
        int blue = foreground.getBlue();

        Color withAlpha = new Color(red, green, blue, alpha);
        super.setForeground( withAlpha );
    }

    /**
     * setShow
     * Sets the state of text
     * @param show a valid Show enum
     */
    public void setShow(Show show)
    {
        this.show = show;
    }

    /**
     * checkForPrompt
     * Checks for prompt and sets the state of text
     */
    private void checkForPrompt()
    {
        //  Text has been entered, remove the prompt

        if (document.getLength() > 0)
        {
            setVisible( false );
            return;
        }

        //  Check the Show property and component focus to determine if the
        //  prompt should be displayed.

        if (component.hasFocus())
        {
            if (show == Show.ALWAYS
                    ||  show ==	Show.FOCUS_GAINED)
                setVisible( true );
            else
                setVisible( false );
        }
        else
        {
            if (show == Show.ALWAYS
                    ||  show ==	Show.FOCUS_LOST)
                setVisible( true );
            else
                setVisible( false );
        }
    }

    /**
     * focusGained
     * Check if focus is gained
     * @param e
     */
    public void focusGained(FocusEvent e)
    {
        checkForPrompt();
    }

    /**
     * focusLost
     * Check if focus is lost
     * @param e
     */
    public void focusLost(FocusEvent e)
    {
        checkForPrompt();
    }

    /**
     * insertUpdate
     * Check if text is updated
     * @param e
     */
    public void insertUpdate(DocumentEvent e)
    {
        checkForPrompt();
    }

    /**
     * removeUpdate
     * Removes update from text
     * @param e
     */
    public void removeUpdate(DocumentEvent e)
    {
        checkForPrompt();
    }

    /**
     * changedUpdate
     * @param e
     */
    public void changedUpdate(DocumentEvent e) {}
}