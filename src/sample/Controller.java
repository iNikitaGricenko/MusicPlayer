package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Controller {

    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrevious;

    @FXML
    private ListView<String> Playlist;
    private ObservableList<File> fileObservableList = FXCollections.observableArrayList();
    private ObservableList<String> soundFiles = FXCollections.observableArrayList();
    @FXML
    private MediaView mediaView;

    private static Stage stage;

    public static void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        primaryStage.show();
    }

    @FXML
    void ToOpenFile(MouseEvent event) {
        try{
            File file = new FileChooser().showOpenDialog(null);
            playerStart(new Media(file.toURI().toString()));
            fileObservableList.add(file);
            soundFiles.add(file.getName());
        } catch (NullPointerException exception) { }
    }
    List<File> file;
    @FXML
    void ToPlaylistAdd(MouseEvent event) {
        try{
            file = new FileChooser().showOpenMultipleDialog(null);
            for (int i = 0; i <file.size() ; i++) {
                fileObservableList.add(file.get(i));
                soundFiles.add(file.get(i).getName());
            }
        } catch (NullPointerException exception) { }
    }

    @FXML
    void ToCleanPlayList(MouseEvent event) {
        soundFiles.removeAll(soundFiles);
        fileObservableList.removeAll(fileObservableList);
    }

    @FXML
    void ToShuffle(MouseEvent event) {
        try {
            Collections.shuffle(fileObservableList);
            soundFiles.removeAll();
            if (!Playlist.getItems().isEmpty()) Playlist.getItems().removeAll(Playlist.getItems());
            for (int i = 0; i < fileObservableList.size(); i++) {
                soundFiles.add(fileObservableList.get(i).getName());
            }
            Playlist.setItems(soundFiles);
        } catch (IndexOutOfBoundsException e) { }
        Playlist.getSelectionModel().select(0);
    }

    @FXML
    void initialize () {
        Playlist.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            System.out.println(Playlist.getSelectionModel().getSelectedItem() + " " + Playlist.getSelectionModel().getSelectedIndex());
            try {
                if(player.getStatus().equals(MediaPlayer.Status.PLAYING)) player.stop();
                playerStart(new Media(fileObservableList.get(Playlist.getSelectionModel().getSelectedIndex()).toURI().toString()));
            }catch (NullPointerException | IndexOutOfBoundsException e) {}
        });
        btnStart.setOnMouseClicked(e -> {
            if (!Playlist.getItems().isEmpty()) playerStart(new Media(fileObservableList.get(0).toURI().toString()));
            else {
                try{
                    File file = new FileChooser().showOpenDialog(null);
                    playerStart(new Media(file.toURI().toString()));
                    fileObservableList.add(file);
                    soundFiles.add(file.getName());
                } catch (NullPointerException exception) { }
            }
        });
        btnNext.setOnMouseClicked(e -> {
            if (Playlist.getSelectionModel().isSelected(soundFiles.size()-1)) Playlist.getSelectionModel().select(0);
            Playlist.getSelectionModel().selectNext();
        });
        btnPrevious.setOnMouseClicked(e -> {
            if (Playlist.getSelectionModel().isSelected(0)) Playlist.getSelectionModel().selectLast();
            Playlist.getSelectionModel().selectPrevious();
        });
        Playlist.setItems(soundFiles);
    }

    private MediaPlayer player;
    private void playerStart (Media media) {
        player = new MediaPlayer(media);
        player.setAutoPlay(true);
        mediaView.setMediaPlayer(player);
        player.setVolume(0.1);
        player.play();

        btnPause.setOnMouseClicked(e -> {
            Duration time = player.getCurrentTime();
            if(player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                player.pause();
            }
            else {
                player.setStartTime(time);
                player.play();
            }
        });
        btnStop.setOnMouseClicked(e -> {
            player.stop();
        });
        btnStart.setOnMouseClicked(e -> {
            player.play();
        });
    }
}