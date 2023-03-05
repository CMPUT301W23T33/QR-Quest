package com.example.base;

import java.util.Comparator;
import java.util.LinkedList;

public class Leaderboard {

    // Leaderboard
    private LinkedList<Player> playerList;
    private LinkedList<Player> scoreRank;
    private LinkedList<Player> scannedRank;
    private LinkedList<Player> qrCodeRank;
    private LinkedList<Player> regionalScoreRank;



    public Leaderboard(LinkedList<Player> playerList){
        this.playerList = playerList;
        this.scoreRank = playerList;
        this.scannedRank = playerList;
        this.regionalScoreRank = playerList;
    }

    public LinkedList<Player> getScoreRank(){
        this.scoreRank.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getScore() > o2.getScore()){
                    return 1;
                }
                else if (o1.getScore() == o2.getScore()){
                    return 0;
                }
                else{
                    return -1;
                }
            }
        });
        return this.scoreRank;
    }

    public LinkedList<Player> getScannedRank(){
        this.scannedRank.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return 0;
            }
        });
        return this.scannedRank;
    }

    public LinkedList<Player> getQRCodeRank(){
        this.qrCodeRank.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return 0;
            }
        });
        return this.qrCodeRank;
    }

    // For regions, should we use HashMap to store
    public LinkedList<Player> getRegionalScoreRank(int region){
        this.regionalScoreRank.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return 0;
            }
        });
        return this.regionalScoreRank;
    }

}