/**
 * [MarqueePanel.java]
 * Creates JPanel with rolling text
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MarqueePanel extends JPanel implements ActionListener {
    private static final int RATE = 5;
    private final Timer mpTimer = new Timer(1000 / RATE, this);
    private final JLabel label = new JLabel();
    private String s;
    private final int n = 45;
    private int index;

    /**
     * MarqueePanel
     * a panel in the main JFrame to show which song is playing
     * @param s String object for
     */
    MarqueePanel(String s) {
        if (s == null) {
            s = "No song selected";
        }
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        this.s = sb + s + sb;
        label.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 20));
        label.setForeground(Color.GREEN);
        label.setText(sb.toString());
        label.setOpaque(false);
        this.setOpaque(false);
        this.add(label);
    }

    /**
     * setName
     * sets the title of the panel (name of the song)
     * @param name String object for the name of the song
     */
    public void setName(String name) {
        this.s = name;
        if (s == null) {
            s = "No selected song";
        }
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        this.s = sb + s + sb;
        label.setText(sb.toString());
    }

    /**
     * start
     * starts the song
     */
    public void start() {
        mpTimer.start();
    }

    /**
     * stop
     * stops the song
     */
    public void stop() {
        mpTimer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        index--;
        if (index < 0) {
            index = s.length() - n;
        }
        try {
            label.setText(s.substring(index, index + n));
        } catch (Exception ex) {
            index = s.length() - n;
        }
    }
}