package com.example.omur.navigationavm;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alparslan on 22.5.2016.
 */
public class VoiceManager {
    private int i = 0;
    private Uri move, stores, turnLeft, turnRight;
    private Timer timer;
    private MediaPlayer mp;
    List<Uri> uriList = new ArrayList<>();

    public VoiceManager() {
    }

    public void playNext(final Context context) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mp.reset();
                mp = MediaPlayer.create(context, uriList.get(++i));
                mp.start();
                if (uriList.size() > i+1) {
                    playNext(context);
                }
            }
        },mp.getDuration()+100);
    }

    public void makeVoice(List<String> pathList, Context context) {
        if (pathList != null) {
            move = Uri.parse("android.resource://com.example.omur.navigationavm/raw/move");
            stores = Uri.parse("android.resource://com.example.omur.navigationavm/raw/stores");
            turnLeft = Uri.parse("android.resource://com.example.omur.navigationavm/raw/turnleft");
            turnRight = Uri.parse("android.resource://com.example.omur.navigationavm/raw/turnright");

            if (!isCorridorChanged(pathList)) {
                int numberOfStores = pathList.size();

                uriList.add(move);
                Uri myUri = Uri.parse("android.resource://com.example.omur.navigationavm/raw/n" + numberOfStores);

                uriList.add(myUri);
                uriList.add(stores);

                mp = MediaPlayer.create(context, uriList.get(0));
                mp.start();
                timer = new Timer();
                if(uriList.size()>1) playNext(context);

                // Upload voice of number here. Number to text call voice

            } else { // Corridor changes and
                String corridor = whichWayCorridorChanges(pathList);
                if (corridor.equals("1")) {
                    if (pathList.contains("1.3.0")) {
                        // please pass numberOfStoresBeforeTurn Stores and turn right and go numberOfStoresAfterTurn stores from 2nd corridor to 1st corridor

                        int numberOfStoresBeforeTurn = howManyStoresBeforeTurn(pathList, "2");
                        int numberOfStoresAfterTurn = howManyStoresAfterTurn(pathList, "1");
                        numberOfStoresAfterTurn = numberOfStoresAfterTurn + 1;
                        uriList.add(move);
                        Uri myUri = Uri.parse("android.resource://com.example.omur.navigationavm/raw/n" + numberOfStoresBeforeTurn);
                        uriList.add(myUri);
                        uriList.add(stores);
                        uriList.add(turnRight);
                        Uri afterTurn = Uri.parse("android.resource://com.example.omur.navigationavm/raw/n" + numberOfStoresAfterTurn);
                        uriList.add(afterTurn);
                        uriList.add(stores);
                        mp = MediaPlayer.create(context, uriList.get(0));
                        mp.start();
                        timer = new Timer();
                        if(uriList.size()>1) playNext(context);

                    } else {
                        // please pass numberOfStoresBeforeTurn Stores and turn left and go numberOfStoresAfterTurn stores
                        int numberOfStoresBeforeTurn = howManyStoresBeforeTurn(pathList, "2");
                        int numberOfStoresAfterTurn = howManyStoresAfterTurn(pathList, "1");

                        uriList.add(move);
                        Uri myUri = Uri.parse("android.resource://com.example.omur.navigationavm/raw/n" + numberOfStoresBeforeTurn);
                        uriList.add(myUri);
                        uriList.add(stores);
                        uriList.add(turnLeft);
                        Uri afterTurn = Uri.parse("android.resource://com.example.omur.navigationavm/raw/n" + numberOfStoresAfterTurn);
                        uriList.add(afterTurn);
                        uriList.add(stores);
                        mp = MediaPlayer.create(context, uriList.get(0));
                        mp.start();
                        timer = new Timer();
                        if(uriList.size()>1) playNext(context);
                    }
                } else {
                    // Here user is in 1st corridor and will go to 2nd
                    if (pathList.contains("1.3.0")) {
                        // please pass numberOfStoresBeforeTurn Stores and turn left and go numberOfStoresAfterTurn stores

                        int numberOfStoresBeforeTurn = howManyStoresBeforeTurn(pathList, "1");
                        int numberOfStoresAfterTurn = howManyStoresAfterTurn(pathList, "2");
                        numberOfStoresAfterTurn = numberOfStoresAfterTurn + 1;

                        uriList.add(move);
                        Uri myUri = Uri.parse("android.resource://com.example.omur.navigationavm/raw/n" + numberOfStoresBeforeTurn);
                        uriList.add(myUri);
                        uriList.add(stores);
                        uriList.add(turnLeft);
                        Uri afterTurn = Uri.parse("android.resource://com.example.omur.navigationavm/raw/n" + numberOfStoresAfterTurn);
                        uriList.add(afterTurn);
                        uriList.add(stores);
                        mp = MediaPlayer.create(context, uriList.get(0));
                        mp.start();
                        timer = new Timer();
                        if(uriList.size()>1) playNext(context);

                        //Add voice code

                    } else {
                        // please pass numberOfStoresBeforeTurn Stores and turn right and go numberOfStoresAfterTurn stores
                        int numberOfStoresBeforeTurn = howManyStoresBeforeTurn(pathList, "1");
                        int numberOfStoresAfterTurn = howManyStoresAfterTurn(pathList, "2");
                        uriList.add(move);
                        Uri myUri = Uri.parse("android.resource://com.example.omur.navigationavm/raw/n" + numberOfStoresBeforeTurn);
                        uriList.add(myUri);
                        uriList.add(stores);
                        uriList.add(turnRight);
                        Uri afterTurn = Uri.parse("android.resource://com.example.omur.navigationavm/raw/n" + numberOfStoresAfterTurn);
                        uriList.add(afterTurn);
                        uriList.add(stores);
                        mp = MediaPlayer.create(context, uriList.get(0));
                        mp.start();
                        timer = new Timer();
                        if(uriList.size()>1) playNext(context);
                    }
                }

            }


        }//End of nullcheck of pathList

    }

    private boolean isCorridorChanged(List<String> pathList) {
        /**
         * First we get first Zone's Corridor number if this first corridor number changes, then corridor is changed.
         */
        int counter = 0;
        String firstCorridorNumber = pathList.get(0).split("\\.")[1];
        Iterator<String> entries = pathList.iterator();

        while (entries.hasNext()) {
            String[] zoneArray = entries.next().split("\\.");
            if (!firstCorridorNumber.equals(zoneArray[1])) {
                counter = 1;
            }
        }
        if (counter == 0) {
            return false;
        } else {
            return true;
        }
    }

    private String whichWayCorridorChanges(List<String> pathList) {
        int counter = 0;
        String firstCorridorNumber = pathList.get(0).split("\\.")[1];
        Iterator<String> entries = pathList.iterator();

        while (entries.hasNext()) {
            String[] zoneArray = entries.next().split("\\.");
            if (!firstCorridorNumber.equals(zoneArray[1])) {
                counter = Integer.parseInt(zoneArray[1]);
            }
        }
        return String.valueOf(counter);
    }

    private int howManyStoresBeforeTurn(List<String> pathList, String number) {
        int numberOfStores = 0;
        Iterator<String> entries = pathList.iterator();
        while (entries.hasNext()) {
            if (entries.next().split("\\.")[1].equals(number)) {
                numberOfStores += 1;
            }
        }
        return numberOfStores;
    }

    private int howManyStoresAfterTurn(List<String> pathList, String number) {
        int numberOfStores = 0;
        Iterator<String> entries = pathList.iterator();
        while (entries.hasNext()) {
            if (entries.next().split("\\.")[1].equals(number)) {
                numberOfStores += 1;
            }
        }
        return numberOfStores;
    }

}
