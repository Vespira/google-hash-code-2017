package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Alex on 23/02/2017.
 */
public class ParseInputFile {

    private Situation situation = new Situation();

    private List<Video> videoList;
    Map<Integer, Endpoint> endpointList;
    List<Request> requestList;
    Map<Integer, Cache> cacheList;

    private int lineNumber = 0;
    private int nextEndpointLines = 0;

    public Situation parseFile(String path) {

        try (Stream<String> stream = Files.lines(Paths.get(path))) {

            stream.forEach(line -> parseLine(line));

        } catch (IOException e) {
            e.printStackTrace();
        }

        situation.setVideoList(videoList);
        situation.setEndpointList(endpointList);
        situation.setRequestList(requestList);
        situation.setCacheList(cacheList);
        System.out.println("DONE");
        return situation;
    }

    public void parseLine(String line) {

        String[] splittedCharacters = line.split(" ");

        switch(lineNumber) {

            //cas du nombre d'éléments
            case 0:
                videoList = new ArrayList<>();
                for(int i = 0; i < Integer.valueOf(splittedCharacters[0]); i++) {
                    videoList.add(new Video(i, 0));
                }
                endpointList = new HashMap<>(Integer.valueOf(splittedCharacters[1]));
                for(int i = 0; i < Integer.valueOf(splittedCharacters[1]); i++) {
                    endpointList.put(i, new Endpoint(i, null, 0));
                }
                requestList = new ArrayList<>(Integer.valueOf(splittedCharacters[2]));
                for(int i = 0; i < Integer.valueOf(splittedCharacters[2]); i++) {
                    requestList.add(new Request(null, 0, null));
                }
                cacheList = new HashMap<>(Integer.valueOf(splittedCharacters[3]));
                for(int i = 0; i < Integer.valueOf(splittedCharacters[3]); i++) {
                    cacheList.put(i, new Cache(i, Integer.valueOf(splittedCharacters[4])));
                }

                break;
            //cas des tailles des vidéos
            case 1:
                for(int i = 0 ; i < splittedCharacters.length; i++) {
                    videoList.get(i).setSize(Integer.valueOf(splittedCharacters[i]));
                }
                break;
            //cest parti pour les endpoints
            default:
                if(lineNumber==2) {
                    endpointList.put(0, new Endpoint(0, new HashMap<>(), 0));
                    nextEndpointLines = Integer.valueOf(splittedCharacters[1]);
                } else {
                    if(nextEndpointLines > 0) {
                        endpointList.get(Integer.valueOf(splittedCharacters[0])).setLatencyToDataCenter(Integer.valueOf(splittedCharacters[1]));
                        nextEndpointLines--;
                    }
                }

                break;

        }

        lineNumber++;
    }
}
