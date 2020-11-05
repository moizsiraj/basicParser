import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class parser {
    private String code;
    private int noOfStates;
    private int noOfTerminals;
    private int noOfNonTerminals;
    private int noOfProductions;
    private int currentChar;
    private String[][] actionTable;
    private String[][] gotoTable;
    private String[][] productions;

    public parser(String code, int noOfStates, int noOfTerminals, int noOfNonTerminals, int noOfProductions) throws FileNotFoundException {
        this.code = code;
        this.noOfStates = noOfStates + 1;
        this.noOfTerminals = noOfTerminals;
        this.noOfNonTerminals = noOfNonTerminals;
        this.noOfProductions = noOfProductions;
        currentChar = -1;
        actionTable = new String[this.noOfStates][this.noOfTerminals];
        gotoTable = new String[this.noOfStates][this.noOfNonTerminals];
        productions = new String[this.noOfProductions][2];
        populateTables();
    }

    private String getToken() {
        currentChar++;
        return String.valueOf(code.charAt(currentChar));
    }

    public void print(String[][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.print("\n");
        }
    }


    private void populateTables() throws FileNotFoundException {
        //Scanner input = new Scanner(System.in);
        File code = new File("C:\\Users\\moizs\\OneDrive\\Documents\\NetBeansProjects\\basicParser\\src\\parse table.txt");
        Scanner myReader = new Scanner(code);
        String str;
        System.out.println("Enter terminals in form \n" +
                "<terminal 1> <terminal 2> <terminal 3>");
        str = myReader.nextLine();
        String[] terminals = str.split(" ");
        for (int i = 0; i < actionTable[0].length; i++) {
            actionTable[0][i] = terminals[i];
        }
        System.out.println("Enter non-terminals in form \n" +
                "<non-terminal 1> <non-terminal 2> <non-terminal 3>");
        str = myReader.nextLine();
        String[] nonTerminals = str.split(" ");
        for (int i = 0; i < gotoTable[0].length; i++) {
            gotoTable[0][i] = nonTerminals[i];
        }
        System.out.println("Enter the transition for terminals in form \n" +
                "<state> <terminal> <transition> examples: 1 a s2, 1 a r3, 1 $ acc \n" +
                "input \"end\" when done");
        str = myReader.nextLine();
        while (!str.equals("end")) {
            String[] values = str.split(" ");
            int row = Integer.parseInt(values[0]);
            int col = getCol(values[1], actionTable);
            String transition = values[2];
            actionTable[row][col] = transition;
            str = myReader.nextLine();
        }
        System.out.println("Enter the transition for non-terminals in form \n" +
                "<state> <non-terminal> <transition> examples: 1 A g1, 3 B g8\n" +
                "input \"end\" when done");
        str = myReader.nextLine();
        while (!str.equals("end")) {
            String[] values = str.split(" ");
            int row = Integer.parseInt(values[0]);
            int col = getCol(values[1], gotoTable);
            String transition = values[2];
            gotoTable[row][col] = transition;
            str = myReader.nextLine();
        }
        System.out.println("Enter the production rules in form \n" +
                "<Right side> <Left side> examples: S aABe, A Abc\n" +
                "input \"end\" when done");
        str = myReader.nextLine();
        int index = 0;
        while (!str.equals("end")) {
            String[] values = str.split(" ");
            productions[index][0] = values[0];
            productions[index][1] = values[1];
            index++;
            str = myReader.nextLine();
        }
    }

    private int getCol(String str, String[][] table) {
        for (int i = 0; i < table[0].length; i++) {
            if (str.equals(table[0][i])) {
                return i;
            }
        }
        return -1;
    }

    private int getRow(String str) {
        switch (str.length()) {
            case 2:
                return Integer.parseInt(String.valueOf(str.charAt(1)));
            case 3:
                return Integer.parseInt(str.substring(1, 3));
            case 4:
                return Integer.parseInt(str.substring(1, 4));
            default:
                return -1;
        }
    }

    private Stack<String> stack = new Stack<>();

    public String parse() {
        stack.push("s1");
        String word = getToken();
        while (true) {
            try {
                String currentState = stack.peek();
                int row = getRow(currentState);
                int col = getCol(word, actionTable);
                if (actionTable[row][col].charAt(0) == 's') {
                    stack.push(word);
                    stack.push(actionTable[row][col]);
                    word = getToken();
                } else if (actionTable[row][col].charAt(0) == 'r') {
                    int ruleNo = getRow(actionTable[row][col]);
                    int rowNo = ruleNo - 1;
                    String LHS = productions[rowNo][0];
                    String RHS = productions[rowNo][1];
                    for (int i = 0; i < (RHS.length() * 2); i++) {
                        stack.pop();
                    }
                    String newState = stack.peek();
                    row = getRow(newState);
                    col = getCol(LHS, gotoTable);
                    stack.push(LHS);
                    stack.push(gotoTable[row][col]);
                } else if (actionTable[row][col].equals("acc")) {
                    return "The code is valid";
                }
            } catch (Exception e) {
                return "The code is invalid";
            }
        }
    }
}
