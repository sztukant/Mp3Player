package com.example.mp3player;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class HelloController {
    @FXML
    public Button playBtn;
    public Button stopBtn;
    public Button pauseBtn;
    public Button prevBtn;
    public Button nextBtn;
    public ProgressBar progressBar;
    public ListView<String> playLst;
    public Button addBtn;
    public Slider volume;
    public Button delBtn;

    ListControl lc = new ListControl();
    MediaPlayer mediaPlayer;
    boolean pause = false;
    MediaPlayer.Status status;
    MediaView mediaView;

    public void initialize() {
            lc.listLoad();
        playLst.setItems(lc.listShow());
    }

    @FXML
    protected void onAddBtnClick() throws IOException {

        Window fileChoseWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = new File(fileChooser.showOpenDialog(fileChoseWindow).getPath());

        lc.listAdd(file.getName(), file.getPath());
        playLst.setItems(lc.listShow());
        lc.listWrite();
    }
    @FXML
    protected void onDelBtnClick() throws IOException {
        lc.listDel();
        playLst.setItems(lc.listShow());
        lc.listWrite();
        lc.setSong(0);
    }

    @FXML
    protected void onPlayLstClick() {
        if (playLst.getSelectionModel().selectedIndexProperty().getValue() != -1) {
            lc.setSong(playLst.getSelectionModel().selectedIndexProperty().getValue());
        }
    }

    @FXML
    protected void onPlayBtnClick() {
        if (!lc.songList.isEmpty()) {
            if (lc.getSong().isEmpty() || lc.getSong().isBlank()) {
                lc.setSong(0);
            }
                mediaPlayer = new MediaPlayer(new Media(new File(lc.getSong()).toURI().toString()));
                mediaView = new MediaView(mediaPlayer);
                mediaView.getMediaPlayer().play();
            volume.valueProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                    if (volume.isValueChanging()) {
                        mediaView.getMediaPlayer().setVolume(volume.getValue() / 100.0);
                    }
                }
            });
                mediaView.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<>() {

                @Override
                public void changed(ObservableValue<? extends Duration> observable,
                                    Duration oldTime, Duration newTime) {
                    progressBar.setProgress(newTime.toMillis() / mediaView.getMediaPlayer().getTotalDuration().toMillis() );
                }
            });
                mediaView.getMediaPlayer().setOnEndOfMedia(this::playNextVideo);
                }}

    @FXML
    protected void onStopBtnClick(){
        if (!lc.songList.isEmpty()) {
            mediaView.getMediaPlayer().stop();
            //mediaPlayer.stop();
        }
    }
    @FXML
    protected void onPauseBtnClick(){
        if (!lc.songList.isEmpty()) {
            if (!pause) {
                mediaView.getMediaPlayer().pause();
                pause = true;
            } else {
                mediaView.getMediaPlayer().play();
                pause = false;
            }
        }
    }
    @FXML
    protected void onPrevBtnClick(){
        if (lc.getSongPosition() > 0){
            lc.setSong(lc.getSongPosition()-1);
            onPlayBtnClick();
        }
    }
    @FXML
    protected void onNextBtnClick(){
        if (lc.getSongPosition() < lc.songList.size()-1){
            lc.setSong(lc.getSongPosition()+1);
            onPlayBtnClick();
        }
    }

    private void playNextVideo() {
        disposePlayer();

        if (lc.songPosition+1 == lc.songList.size()) {
            return; // no more videos to play
        }
        lc.setSong(lc.getSongPosition()+1);
        MediaPlayer player = new MediaPlayer((new Media(new File(lc.getSong()).toURI().toString())));
        volume.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volume.isValueChanging()) {
                    player.setVolume(volume.getValue() / 100.0);
                }
            }
        });
        player.currentTimeProperty().addListener(new ChangeListener<>() {

            @Override
            public void changed(ObservableValue<? extends Duration> observable,
                                Duration oldTime, Duration newTime) {
                progressBar.setProgress(newTime.toMillis() / player.getTotalDuration().toMillis() );
            }
        });
        player.setAutoPlay(true); // play ASAP
        player.setOnEndOfMedia(this::playNextVideo); // play next video when this one ends
        mediaView.setMediaPlayer(player);
    }

    private void disposePlayer() {
        MediaPlayer player = mediaView.getMediaPlayer();
        if (player != null) {
            player.dispose(); // release resources
        }
    }
}