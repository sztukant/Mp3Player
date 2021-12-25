package com.example.mp3player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.io.*;

public class ListControl {

    protected ObservableList<Pair<String, String>> songList = FXCollections.observableArrayList();
    private String set = "";
    int songPosition = 0;

    void listAdd(String name, String filePath) {
        Pair<String, String> pair = new Pair<>(name, filePath);
        songList.add(pair);

    }
    ObservableList<String> listShow(){
        ObservableList<String> playList = FXCollections.observableArrayList();
        for (Pair<String,String> pair : songList){
            playList.add(pair.getKey());
        }
        return playList;
    }

    void setSong(int song){
        set = songList.get(song).getValue();
        songPosition = song;
    }
    void setSongPosition(int position){
        songPosition = position;
    }
    int getSongPosition(){ return songPosition;}


    String getSong(){ return set;}
    //String getNext(){ return (songPosition+1).toString();}

    void listDel(){
        this.songList.remove(songPosition);

    }

    void listLoad() {try {
        try(BufferedReader rd = new BufferedReader(new FileReader("src/main/resources/list.txt"))){
            for (String line ; (line = rd.readLine()) != null;){
                this.listAdd(line.substring(0,line.indexOf(";")),line.substring(line.indexOf(";")+1));
            }
        }
    } catch (IOException exception){
        //file is empty or broken
    }}
    void listWrite() throws IOException {
        FileWriter fw = new FileWriter("src/main/resources/list.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        for (Pair<String,String> pair: songList){
            bw.write(pair.getKey()+";"+pair.getValue());
            bw.newLine();
        }
        bw.close();
    }
}
