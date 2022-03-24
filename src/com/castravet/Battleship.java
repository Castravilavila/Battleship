package com.castravet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Battleship extends GameBoard {
    private final static String AIRCRAFT_CARRIER = "Aircraft Carrier";
    private final static String BATTLESHIP = "Battleship";
    private final static String SUBMARINE = "Submarine";
    private final static String CRUISER = "Cruiser";
    private final static String DESTROYER = "Destroyer";

    private List<Ship> shipList = new ArrayList<>();
    private String name;
    private Map<String, Integer> numberOfShipsByName = new HashMap<>();

    public Battleship() {
        super();
        this.name = "Battleship";
        initializeEachNumberOfShips();
        initializeGameBoard();
    }

    public Battleship(int rows, int columns) {
        super(rows, columns);
        this.name = "Battleship";
        initializeEachNumberOfShips();
        initializeGameBoard();
    }

    private void initializeEachNumberOfShips() {
        shipList.add(new Ship(AIRCRAFT_CARRIER, 5, 1));
        shipList.add(new Ship(BATTLESHIP, 4, 1));
        shipList.add(new Ship(SUBMARINE, 3, 1));
        shipList.add(new Ship(CRUISER, 3, 1));
        shipList.add(new Ship(DESTROYER, 2, 1));
    }

    public void startGame() {
        Player player1 = new Player(copyGameBoard(),"Player 1");
        Player player2 = new Player(copyGameBoard(), "Player 2");
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));

        try{
            System.out.println("Player 1, place your ships on the game field");
            printGameBoard(player1.getGameBoard(),SIMPLE_TABLE);
            enterAllCoordinates(player1, scanner);
            System.out.println("Press Enter and pass the move to another player");
            scanner.readLine();
            clearConsole();
            System.out.println("Player 2, place your ships on the game field");
            printGameBoard(player2.getGameBoard(),SIMPLE_TABLE);
            enterAllCoordinates(player2, scanner);
            System.out.println("Press Enter and pass the move to another player");
            scanner.readLine();
            clearConsole();
            startShooting(scanner,player1,player2);
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public void startShooting(BufferedReader scanner, Player player1, Player player2) {
        try {
            boolean isHit = false;
            boolean player1Turn = true;
            while (!isHit) {
                Player playerPlaying;
                Player playerWaiting;

                if (player1Turn){
                    playerPlaying=player1;
                    playerWaiting=player2;
                }else{
                    playerPlaying=player2;
                    playerWaiting=player1;
                }
                printGameBoard(playerWaiting.getGameBoard(),ATTACK_TABLE);
                System.out.println("---------------------");
                printGameBoard(playerPlaying.getGameBoard(),SIMPLE_TABLE);
                System.out.println();
                System.out.println(playerPlaying.getName()+", it's your turn:");
                String input = scanner.readLine().toUpperCase();
                boolean isInputCorrect = checkCoordinatesForHitting(input);
                if (isInputCorrect) {
                    addCoordinateForAttack(input,playerWaiting);
                    if (areAllShipsSunken(playerWaiting)) {
                        isHit = true;
                    }else{
                        if (player1Turn){
                            player1Turn=false;
                        }else{
                            player1Turn=true;
                        }
                        System.out.println("Press Enter and pass the move to another player");
                        String pressEnter = scanner.readLine();
                        clearConsole();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public void enterAllCoordinates(Player player, BufferedReader scanner) {
        for (Ship ship : shipList) {
            Ship shipToAdd = new Ship(ship.getName(),ship.getSize(),ship.getOccurence());
            for (int i = 0; i < ship.getOccurence(); i++) {
                player.addShip(shipToAdd);
                boolean isShipAddedCorrectly = false;
                boolean noError = true;
                while (!isShipAddedCorrectly) {
                    if (noError) {
                        System.out.println("Enter the coordinates of the " + ship.getName() + " (" + ship.getSize() + " cells):");
                    }
                    try {
                        String[] strInput = scanner.readLine().toUpperCase(Locale.ROOT).split(" ");
                        boolean areCoordinatesAndTextCorrect = checkCoordinates(strInput, player.getShipByName(ship.getName()), player);
                        if (areCoordinatesAndTextCorrect) {
                            addCoordinatesAndDisplayMap(strInput, player);
                            player.getShipByName(ship.getName()).addAllPositions(strInput);
                            isShipAddedCorrectly = true;
                        } else {
                            noError = false;
                        }
                    } catch (Exception e) {
                        System.out.println("Error");
                        noError = false;
                    }
                }
            }
        }
    }

    public boolean checkCoordinatesForHitting(String input) {
        String letter = input.substring(0, 1);
        int num = Integer.parseInt(input.substring(1));
        if ((!isLetterPresentInGameBoard(letter) && !isCoordNumPresentInGameBoard(num)) ||
                !isLetterPresentInGameBoard(letter) || !isCoordNumPresentInGameBoard(num)) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        }
        return true;
    }

    public boolean checkCoordinates(String[] input, Ship ship, Player player) throws Exception {
        if (input.length > 2 || input.length < 2) {
            throw new Exception();
        }
        String firstArgumentLetter = input[0].substring(0, 1);
        String secondArgumentLetter = input[1].substring(0, 1);
        int firstArgumentPosNumber = Integer.parseInt(input[0].substring(1)) - 1;
        int secondArgumentPosNumber = Integer.parseInt(input[1].substring(1)) - 1;

        if (!isLetterPresentInGameBoard(firstArgumentLetter)
                || !isLetterPresentInGameBoard(secondArgumentLetter)) {
            throw new Exception();
        }
        if (!isCoordNumPresentInGameBoard(firstArgumentPosNumber)
                || !isCoordNumPresentInGameBoard(secondArgumentPosNumber)) {
            throw new Exception();
        }
        if (!isInputLocationCorrect(firstArgumentPosNumber, secondArgumentPosNumber,
                firstArgumentLetter, secondArgumentLetter)) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }
        if (!isShipLengthInputCorrect(firstArgumentPosNumber, secondArgumentPosNumber,
                firstArgumentLetter, secondArgumentLetter, ship)) {
            System.out.println("Error! Wrong length of the Submarine! Try again:");
            return false;
        }
        if (!isDistanceBetweenShipsAcceptable(firstArgumentPosNumber, secondArgumentPosNumber,
                firstArgumentLetter, secondArgumentLetter, player)) {
            System.out.println("Error! You placed it too close to another one. Try again:");
            return false;
        }
        return true;
    }

    public void addCoordinateForAttack(String input, Player player) {
        int letterNumPosition = getRowByLetter(input.substring(0, 1));
        int numPosition = Integer.parseInt(input.substring(1)) - 1;
        String[][] gameBoard = player.getGameBoard();;

        if (gameBoard[letterNumPosition][numPosition] == "O" ||
                gameBoard[letterNumPosition][numPosition] == "X") {
            gameBoard[letterNumPosition][numPosition] = "X";

            Ship ship = Ship.findShipAndHit(player.getShips(), input);
            if (areAllShipsSunken(player)) {
                System.out.println("You sank the last ship. You won. Congratulations!");
            } else if (ship != null && ship.isShipSunken()) {
                System.out.println("You sank a ship!");
            } else {
                System.out.println("You hit a ship!");
            }
        } else if (gameBoard[letterNumPosition][numPosition] == "~" ||
                gameBoard[letterNumPosition][numPosition] == "M") {
            gameBoard[letterNumPosition][numPosition] = "M";
            System.out.println("You missed!");
        }
    }

    public void addCoordinatesAndDisplayMap(String[] input, Player player) {
        String firstArgumentLetter = getFirstArgumentLetter(input);
        String secondArgumentLetter = getSecondArgumentLetter(input);
        int firstArgumentPosNumber = getFirstArgumentPosNumber(input);
        int secondArgumentPosNumber = getSecondArgumentPosNumber(input);
        String[][] gameBoard = player.getGameBoard();

        if (firstArgumentLetter.equals(secondArgumentLetter)) {
            if (firstArgumentPosNumber > secondArgumentPosNumber) {
                int temp = firstArgumentPosNumber;
                firstArgumentPosNumber = secondArgumentPosNumber;
                secondArgumentPosNumber = temp;
            }
            int row = getRowByLetter(firstArgumentLetter);
            for (int i = firstArgumentPosNumber; i <= secondArgumentPosNumber; i++) {
                gameBoard[row][i] = "O";
            }
        } else {
            int firstCharPos = getRowByLetter(firstArgumentLetter);
            int secondCharPos = getRowByLetter(secondArgumentLetter);
            if (firstCharPos > secondCharPos) {
                int temp = firstCharPos;
                firstCharPos = secondCharPos;
                secondCharPos = temp;
            }
            for (int i = firstCharPos; i <= secondCharPos; i++) {
                gameBoard[i][firstArgumentPosNumber] = "O";
            }
        }

        printGameBoard(gameBoard,SIMPLE_TABLE);
    }

    public boolean isInputLocationCorrect(int firstPosNum, int secondPosNum,
                                          String firstLetter, String secondLetter) {
        if (!firstLetter.equals(secondLetter)) {
            if (firstPosNum != secondPosNum) {
                return false;
            }
        }
//        else{
//            if (firstPosNum == secondPosNum){
//                return false;
//            }
//        }
        return true;
    }

    public boolean isShipLengthInputCorrect(int firstPosNum, int secondPosNum,
                                            String firstLetter, String secondLetter, Ship ship) {
        if (firstLetter.equals(secondLetter)) {
            if (Math.abs(firstPosNum - secondPosNum) + 1 > ship.getSize()
                    || Math.abs(firstPosNum - secondPosNum) + 1 < ship.getSize()) {
                return false;
            }
        } else {
            if (Math.abs(getRowByLetter(firstLetter) - getRowByLetter(secondLetter)) + 1 > ship.getSize()
                    || Math.abs(getRowByLetter(firstLetter) - getRowByLetter(secondLetter)) + 1 < ship.getSize()) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllShipsSunken(Player player) {
        String[][] gameBoard = player.getGameBoard();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                if (gameBoard[i][j] == "O") {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isDistanceBetweenShipsAcceptable(int firstPosNum, int secondPosNum,
                                                    String firstLetter, String secondLetter, Player player) {
        String[][] gameBoard = player.getGameBoard();
        //if letters are the same then numbers should be different
        if (firstLetter.equals(secondLetter)) {
            int row = getRowByLetter(firstLetter);

            //check if before and after first and last positions ship present ~OOOO~
            // also change with place if first position higher than second
            if (firstPosNum > secondPosNum) {
                if (firstPosNum + 1 < gameBoard.length) {
                    if (gameBoard[row][firstPosNum + 1] == "O") {
                        return false;
                    }
                }
                if (secondPosNum - 1 >= 0) {
                    if (gameBoard[row][secondPosNum - 1] == "O") {
                        return false;
                    }
                }
                int temp = firstPosNum;
                firstPosNum = secondPosNum;
                secondPosNum = temp;
            } else {
                if (firstPosNum - 1 >= 0) {
                    if (gameBoard[row][firstPosNum - 1] == "O") {
                        return false;
                    }
                }
                if (secondPosNum + 1 < gameBoard.length) {
                    if (gameBoard[row][secondPosNum + 1] == "O") {
                        return false;
                    }
                }
            }
            //check if coordinates not occupied already with other ship
            for (int i = firstPosNum; i <= secondPosNum; i++) {
                if (row - 1 >= 0) {
                    if (gameBoard[row - 1][i] == "O") {
                        return false;
                    }
                }
                if (row + 1 < gameBoard.length) {
                    if (gameBoard[row + 1][i] == "O") {
                        return false;
                    }
                }

            }
            //if numbers are the same than letters should be different
        } else {
            int firstCharPos = getRowByLetter(firstLetter);
            int secondCharPos = getRowByLetter(secondLetter);
            //                                                                ~
            //check if before and after first and last positions ship present O
            // also change with place if first position higgher than second   ~
            if (firstCharPos > secondCharPos) {
                if (firstCharPos + 1 < gameBoard.length) {
                    if (gameBoard[firstCharPos + 1][firstPosNum] == "O") {
                        return false;
                    }
                }
                if (secondCharPos - 1 >= 0) {
                    if (gameBoard[secondCharPos - 1][firstPosNum] == "O") {
                        return false;
                    }
                }
                int temp = firstCharPos;
                firstCharPos = secondCharPos;
                secondCharPos = temp;
            } else {
                if (firstCharPos - 1 >= 0) {
                    if (gameBoard[firstCharPos - 1][firstPosNum] == "O") {
                        return false;
                    }
                }
                if (secondCharPos + 1 < gameBoard.length) {
                    if (gameBoard[secondCharPos + 1][firstPosNum] == "O") {
                        return false;
                    }
                }
            }
            //same as previous for
            for (int i = firstCharPos; i <= secondCharPos; i++) {
                if (firstPosNum - 1 >= 0) {
                    if (gameBoard[i][firstPosNum - 1] == "O") {
                        return false;
                    }
                }
                if (firstPosNum + 1 < gameBoard.length) {
                    if (gameBoard[i][firstPosNum + 1] == "O") {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    public String getFirstArgumentLetter(String[] input) {
        return input[0].substring(0, 1);
    }

    public String getSecondArgumentLetter(String[] input) {
        return input[1].substring(0, 1);
    }

    public int getFirstArgumentPosNumber(String[] input) {
        return Integer.parseInt(input[0].substring(1)) - 1;
    }

    public int getSecondArgumentPosNumber(String[] input) {
        return Integer.parseInt(input[1].substring(1)) - 1;
    }

    public void clearConsole(){
        System.out.println("\n".repeat(99));
    }
}