/**
 * [MusicScrubber.java]
 * Custom JSlider
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import java.awt.*;
import javax.swing.*;

public class MusicScrubber {

    /**
     * getSlider
     * returns custom JSlider
     * @param width the width to set the JSlider
     * @return the custom JSlider
     */
    public JSlider getSlider(int width) {
        UIDefaults d = new UIDefaults();
        d.put("Slider:SliderTrack[Enabled].backgroundPainter", new Painter<JSlider>() {
            @Override public void paint(Graphics2D g, JSlider c, int w, int h) {
                int arc         = 10;
                int trackHeight = 8;
                int trackWidth  = w - 2;
                int fillTop     = 4;
                int fillLeft    = 1;

                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(1.5f));
                g.setColor(Color.GRAY);
                g.fillRoundRect(fillLeft, fillTop, trackWidth, trackHeight, arc, arc);

                int fillBottom = fillTop + trackHeight;
                int fillRight  = xPositionForValue(
                        c.getValue(), c,
                        new Rectangle(fillLeft, fillTop, trackWidth, fillBottom - fillTop));

                g.setColor(Color.GREEN);
                g.fillRoundRect(fillLeft + 1, fillTop + 1, fillRight - fillLeft, fillBottom - fillTop, arc, arc);

                g.setColor(Color.LIGHT_GRAY);
                g.drawRoundRect(fillLeft, fillTop, trackWidth, trackHeight, arc, arc);
            }

            protected int xPositionForValue(int value, JSlider slider, Rectangle trackRect) {
                int min = slider.getMinimum();
                int max = slider.getMaximum();
                int trackLength = trackRect.width;
                double valueRange = (double) max - (double) min;
                double pixelsPerValue = (double) trackLength / valueRange;
                int trackLeft = trackRect.x;
                int trackRight = trackRect.x + (trackRect.width - 1);
                int xPosition;

                xPosition = trackLeft;
                xPosition += Math.round(pixelsPerValue * ((double) value - min));

                xPosition = Math.max(trackLeft, xPosition);
                xPosition = Math.min(trackRight, xPosition);

                return xPosition;
            }
        });

        JSlider slider = new JSlider();
        slider.setPreferredSize(new Dimension((int) (width / 1.75), 50));
        slider.putClientProperty("Nimbus.Overrides", d);

        return slider;
    }
}