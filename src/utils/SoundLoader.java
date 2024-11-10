// A Class that acts as the sound reader of this game

package utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Arrays;

import static javafx.scene.media.MediaPlayer.INDEFINITE;
import static utils.GameConstants.soundVolume;

public class SoundLoader {
    private String name;
    private MediaPlayer mediaPlayer;
    private AudioClip audioClip;
    private static final ArrayList<String> BACKGROUNDS = new ArrayList<>(Arrays.asList(
            "StartSceneSong", "ClassSelectionSceneSong", "FightSceneSong1", "FightSceneSong2", "FightSceneSong3",
            "FightSceneSong4", "FightSceneSong5", "FightSceneSong6", "MarketSceneSong", "BossSceneSong"
    ));
    public boolean isPlay = false;

    public SoundLoader(String name, String url) {
        this.name = name;
        this.mediaPlayer = new MediaPlayer(new Media(ClassLoader.getSystemResource(url).toExternalForm()));
        this.audioClip = new AudioClip(ClassLoader.getSystemResource(url).toString());
    }

    public void play() {
        mediaPlayer.setVolume(2* soundVolume);
        audioClip.setVolume(2* soundVolume);
        isPlay = true;
        if (BACKGROUNDS.contains(name)) {
            mediaPlayer.setVolume(soundVolume);
            audioClip.setVolume(soundVolume);
            mediaPlayer.setCycleCount(INDEFINITE);
            mediaPlayer.play();
        } else if (!audioClip.isPlaying()){
            audioClip.play();
        }
    }

    public void stop() {
        isPlay = false;
        if (BACKGROUNDS.contains(name)) {
            mediaPlayer.stop();
        } else if (audioClip.isPlaying()) {
            audioClip.stop();
        }
    }

    public String getName() {
        return name;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
