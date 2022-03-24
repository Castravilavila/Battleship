package com.castravet;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Ship> ships;
    private String[][] gameBoard;

    public Player(String[][] gameBoard, String name) {
        this.gameBoard = gameBoard;
        this.name=name;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public void addShip(Ship ship){
        if (ships==null){
            ships = new ArrayList<>();
        }
        ships.add(ship);
    }

    public Ship getShipByName(String name){
        for (Ship ship: ships){
            if (ship.getName().equals(name)){
                return ship;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(String[][] gameBoard) {
        this.gameBoard = gameBoard;
    }
}
