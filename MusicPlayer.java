package tictactoe;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private static MusicPlayer instance;
    private Clip clip;
    private boolean isMuted = false;
    private FloatControl volumeControl;

    private MusicPlayer() {
        // Private constructor for singleton
    }

    // Singleton pattern to ensure only one music player exists
    public static MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    /**
     * Load and play background music
     * @param filepath Path to the audio file (supports .wav format)
     */
    public void playMusic(String filepath) {
        try {
            // Stop any currently playing music
            stopMusic();

            // Load the audio file
            File audioFile = new File(filepath);
            if (!audioFile.exists()) {
                System.err.println("Music file not found: " + filepath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // Get clip and open audio stream
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);

            // Get volume control if available
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

            // Loop continuously
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

            System.out.println("Music started: " + filepath);

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file format: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading audio file: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error playing music: " + e.getMessage());
        }
    }

    /**
     * Toggle mute/unmute
     * @return true if now muted, false if unmuted
     */
    public boolean toggleMute() {
        if (clip != null) {
            if (isMuted) {
                // Unmute
                if (volumeControl != null) {
                    volumeControl.setValue(0.0f); // Normal volume
                }
                clip.start();
                isMuted = false;
                System.out.println("Music unmuted");
            } else {
                // Mute
                if (volumeControl != null) {
                    volumeControl.setValue(volumeControl.getMinimum()); // Min volume
                }
                clip.stop();
                isMuted = true;
                System.out.println("Music muted");
            }
        }
        return isMuted;
    }

    /**
     * Set mute state
     * @param mute true to mute, false to unmute
     */
    public void setMuted(boolean mute) {
        if (clip != null && isMuted != mute) {
            toggleMute();
        }
    }

    /**
     * Stop music completely
     */
    public void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            isMuted = false;
            System.out.println("Music stopped");
        }
    }

    /**
     * Pause music
     */
    public void pauseMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            System.out.println("Music paused");
        }
    }

    /**
     * Resume music
     */
    public void resumeMusic() {
        if (clip != null && !clip.isRunning() && !isMuted) {
            clip.start();
            System.out.println("Music resumed");
        }
    }

    /**
     * Set volume level
     * @param volume Value between 0.0 (min) and 1.0 (max)
     */
    public void setVolume(float volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float value = min + (max - min) * volume;
            volumeControl.setValue(value);
            System.out.println("Volume set to: " + volume);
        }
    }

    /**
     * Check if music is currently muted
     * @return true if muted
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Check if music is currently playing
     * @return true if playing
     */
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}