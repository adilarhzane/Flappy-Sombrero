package flappyBird;

import javax.sound.sampled.*;

public class Sound 
{
    private Clip clip;
    // Change file name to match yours, of course
    public static Sound sound1 = new Sound("/Users/adilarhzane/Documents/workspace/Flappy/music1.wav");
    //public static Sound sound2 = new Sound("/sound2.wav");
    //public static Sound sound3 = new Sound("/sound3.wav");
    public Sound (String string1) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(string1));
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void play() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stop(){
        if(clip == null) return;
        clip.stop();
    }
    public void loop() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                            System.out.println("BAJS");
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isActive(){
        return clip.isActive();
    }
}