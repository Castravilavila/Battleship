package com.castravet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameBoard {
        public final static String SIMPLE_TABLE = "Simple";
        public final static String ATTACK_TABLE = "Attack";
        public final static String TOTAL_FOG_TABLE = "Total Fog";


        public final static List<String> listLetters = List.of("A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        private String[][] gameBoard;
        private int rows;
        private int columns;

        public GameBoard() {
                this.rows = 10;
                this.columns = 10;
                this.gameBoard = new String[rows][columns];
        }

        public GameBoard(int rows, int columns) {
                this.rows = rows;
                this.columns = columns;
                this.gameBoard = new String[rows][columns];
        }

        public String[][] getGameBoard() {
                return gameBoard;
        }

        public int getRows() {
                return rows;
        }

        public int getColumns() {
                return columns;
        }

        public void initializeGameBoard() {
                if (this.gameBoard[0][0] == null) {
                        for (int i = 0; i < this.rows; i++) {
                                for (int j = 0; j < this.columns; j++) {
                                        this.gameBoard[i][j] = "~";
                                }
                        }
                }
        }

        public void printGameBoard(String tableKind) {
                if (gameBoard[0][0] == null) {
                        initializeGameBoard();
                }
                for (int i = 0; i < this.rows; i++) {
                        if (i == 0) {
                                System.out.print(" ");
                        }
                        System.out.print(" " + (i + 1));
                }
                System.out.println();

                for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                                String letterToPrint="";
                                if (tableKind.equals(SIMPLE_TABLE)) {
                                        letterToPrint = gameBoard[i][j];
                                } else if(tableKind.equals(ATTACK_TABLE)){
                                        letterToPrint = gameBoard[i][j].equals("O")? "~" : gameBoard[i][j];
                                }else if(tableKind.equals(TOTAL_FOG_TABLE)){
                                        letterToPrint = "~";
                                }
                                if (j == 0) {
                                        System.out.print(listLetters.get(i) + " " + letterToPrint);
                                } else {
                                        System.out.print(" " + letterToPrint);
                                }
                        }
                        System.out.println();
                }
        }

        public void printGameBoard(String[][] gameBoard, String tableKind) {
                if (gameBoard[0][0] == null) {
                        initializeGameBoard();
                }
                for (int i = 0; i < this.rows; i++) {
                        if (i == 0) {
                                System.out.print(" ");
                        }
                        System.out.print(" " + (i + 1));
                }
                System.out.println();

                for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                                String letterToPrint="";
                                if (tableKind.equals(SIMPLE_TABLE)) {
                                        letterToPrint = gameBoard[i][j];
                                } else if(tableKind.equals(ATTACK_TABLE)){
                                        letterToPrint = gameBoard[i][j].equals("O")? "~" : gameBoard[i][j];
                                }else if(tableKind.equals(TOTAL_FOG_TABLE)){
                                        letterToPrint = "~";
                                }
                                if (j == 0) {
                                        System.out.print(listLetters.get(i) + " " + letterToPrint);
                                } else {
                                        System.out.print(" " + letterToPrint);
                                }
                        }
                        System.out.println();
                }
        }

        public String[][] copyGameBoard(){
                return Arrays.stream(gameBoard).map(String[]::clone).toArray(String[][]::new);
        }
        public boolean isLetterPresentInGameBoard(String l) {
                for (int i = 0; i < gameBoard.length; i++) {
                        if (listLetters.get(i).equalsIgnoreCase(l)) {
                                return true;
                        }
                }
                return false;
        }

        public boolean isCoordNumPresentInGameBoard(int c) {
                if (c < 0 || c > gameBoard.length) {
                        return false;
                }
                return true;
        }

        public int getRowByLetter(String letter) {
                return listLetters.indexOf(letter);
        }
}