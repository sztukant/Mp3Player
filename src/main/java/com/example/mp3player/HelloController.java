package com.example.mp3player;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

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
                //mediaPlayer.pause();
                pause = true;
            } else {
                mediaView.getMediaPlayer().play();
                //mediaPlayer.play();
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
    @FXML
    protected void onProgressBarClick(){

    }
    protected void progressBarMove(){
        while (mediaView.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING)){

        }
    }
    private void playNextVideo() {
        disposePlayer();

        if (lc.songPosition+1 == lc.songList.size()) {
            return; // no more videos to play
        }
        lc.setSong(lc.getSongPosition()+1);
        MediaPlayer player = new MediaPlayer((new Media(new File(lc.getSong()).toURI().toString())));
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