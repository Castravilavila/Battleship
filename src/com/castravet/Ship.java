package com.castravet;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private String name;
    private int size;
    private int occurence;
    private ArrayList<String> positions;

    public Ship (String name, int size, int occurence) {
        this.name=name;
        this.size=size;
        this.occurence=occurence;
        this.positions = new ArrayList<>();
    }

    public Ship (String name, int size, int occurence, ArrayList<String> positions) {
        this.name=name;
        this.size=size;
        this.occurence=occurence;
        this.positions = positions;
    }


    public static Ship findShipAndHit(List<Ship> ships, String positions){
        for(Ship ship:ships) {
            if (ship.hit(positions)){
                return ship;
            }
        }
        return null;
    }

    public ArrayList<String> getPositions() {
        return positions;
    }

    public void addPosition(String position){
        this.positions.add(position);
    }

    public void addAllPositions(String[] positionsArray){
        String firstLetter = positionsArray[0].substring(0,1);
        String secondLetter = positionsArray[1].substring(0,1);
        int firstNumPos = Integer.parseInt(positionsArray[0].substring(1));
        int secondNumPos = Integer.parseInt(positionsArray[1].substring(1));

        if (firstLetter.equals(secondLetter)){
            for (int i = firstNumPos;i<=secondNumPos;i++){
                positions.add(firstLetter+i);
            }
        }else {
            List<String> letterList = GameBoard.listLetters;
            int firstLetterIndex = letterList.indexOf(firstLetter);
            int secondLetterIndex = letterList.indexOf(secondLetter);
            if (firstLetterIndex>secondLetterIndex){
                int temp = firstLetterIndex;
                firstLetterIndex = secondLetterIndex;
                secondLetterIndex = temp;
            }
            for (int i = firstLetterIndex;i<=secondLetterIndex;i++){
                positions.add(letterList.get(i)+firstNumPos);
            }

        }
    }

    private boolean hit(String position){
        int pos = positions.indexOf(position);
        if (pos!=-1){
            positions.set(pos,"X");
            return true;
        }
        return false;
    }

    public boolean isShipSunken(){
        for (String pos : positions){
            if (!pos.equalsIgnoreCase("X")){
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getOccurence() {
        return occurence;
    }

    public static Ship copyShip(Ship ship){
        return new Ship(ship.name,ship.size,ship.occurence,ship.positions);
    }

}

