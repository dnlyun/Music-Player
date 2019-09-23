/**
 * [VolumeChanger.java]
 * Changes volume of computer
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

public class VolumeChanger {
    float ctrl;


    /**
     * setGain
     * sets the volume
     * @param ctrl float
     */
    public void setGain(float ctrl) {
        this.ctrl = ctrl;
            try {
                Mixer.Info[] infos = AudioSystem.getMixerInfo();
                for (Mixer.Info info : infos) {
                    Mixer mixer = AudioSystem.getMixer(info);
                    if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                        Port port = (Port) mixer.getLine(Port.Info.SPEAKER);
                        port.open();
                        if (port.isControlSupported(FloatControl.Type.VOLUME)) {
                            FloatControl volume = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
                            volume.setValue(ctrl);
                        }
                        port.close();
                    }
                }
            } catch (Exception e) {
            }

//        try {
//            Mixer.Info[] infos = AudioSystem.getMixerInfo();
//            for (Mixer.Info info : infos) {
//                Mixer mixer = AudioSystem.getMixer(info);
//                if (mixer.isLineSupported(Port.Info.HEADPHONE)) {
//                    Port port = (Port) mixer.getLine(Port.Info.HEADPHONE);
//                    port.open();
//                    if (port.isControlSupported(FloatControl.Type.VOLUME)) {
//                        FloatControl volume = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
//                        volume.setValue(ctrl);
//                    }
//                    port.close();
//                }
//            }
//        } catch (Exception e) {
//        }
    }

    /**
     * getGain
     * gets the volume
     * @return volume float
     */
    public float getGain() {
        return this.ctrl;
    }
}