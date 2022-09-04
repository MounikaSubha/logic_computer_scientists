import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class NQueens
{
    public static void main(String[] args) throws Exception
    {

        // let n be the number of queens on the board or it can be length of the board.
        int n = 0;

        if (args.length == 1)
        {
            n = Integer.parseInt(args[0]);
        } else
        {
            System.out.println("Please enter the number of queens");
            return;
        }

        int[][] board = new int[n][n];
        int counter = 1;
        // Initialization of board
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                // Identifier number for each variable, required by dimacs format
                board[i][j] = counter;
                counter++;
            }
        }

        ArrayList<ArrayList<Integer>> cnfLists = new ArrayList<ArrayList<Integer>>();

        // Generate boolean clauses for each horizontal row
        for (int i = 0; i < n; i++)
        {
            ArrayList<Integer> literalList = new ArrayList<Integer>();
            for (int j = 0; j < n; j++)
            {
                literalList.add(board[i][j]);
            }
            horizontalRows(literalList.size(), literalList, cnfLists);
        }

        // Generate boolean clauses for each vertical column
        for (int i = 0; i < n; i++)
        {
            ArrayList<Integer> literalList = new ArrayList<Integer>();
            for (int j = 0; j < n; j++)
            {
                literalList.add(board[j][i]);
            }
            verticalCols(literalList.size(), literalList, cnfLists);
        }

        // All diagonal lines of in the square board
        allDiagonalLines(n, board, cnfLists);

        String inputFile = "input" + n + ".cnf";

        // Print into a file for Minisat
        printFile(inputFile, n, cnfLists);
    }

    private static void horizontalRows(int size, ArrayList<Integer> literalList, ArrayList<ArrayList<Integer>> cnfLists)
    {

        // There is one and only one queen in each row, such as x11 V x12 V x13 V x14 = true
        cnfLists.add(literalList);

        // Other clauses, since we only have one queen on each row, so x1i ->
        // !x1j = !x1i V !x1j for any i != j,
        for (int i = 0; i < size - 1; i++)
        {
            for (int j = i + 1; j < size; j++)
            {
                ArrayList<Integer> clauseList = new ArrayList<Integer>();
                clauseList.add(-literalList.get(i));
                clauseList.add(-literalList.get(j));
                cnfLists.add(clauseList);
            }
        }

    }

    private static void verticalCols(int size, ArrayList<Integer> literalList, ArrayList<ArrayList<Integer>> cnfLists)
    {

        // There is one and only one queen in each column, such as x11 V x21 V x31 V x41 = true
        cnfLists.add(literalList);

        // Other clauses, since we only have one queen on each column, so xi1 ->
        // !xj1 = !xi1 V !xj1 for any i != j,
        for (int i = 0; i < size - 1; i++)
        {
            for (int j = i + 1; j < size; j++)
            {
                ArrayList<Integer> clauseList = new ArrayList<Integer>();
                clauseList.add(-literalList.get(i));
                clauseList.add(-literalList.get(j));
                cnfLists.add(clauseList);
            }
        }

    }

    private static void allDiagonalLines(int n, int[][] board, ArrayList<ArrayList<Integer>> cnfLists)
    {

        // Go through all diagonal lines of the board square
        for (int i = 0; i < n - 1; i++)
        {
            ArrayList<Integer> literalList = new ArrayList<Integer>();
            for (int j = 0; j < n - i; j++)
            {
                literalList.add(board[j][j + i]);
            }
            diagonalLine(literalList.size(), literalList, cnfLists);
        }
        for (int i = 1; i < n - 1; i++)
        {
            ArrayList<Integer> literalList = new ArrayList<Integer>();
            for (int j = 0; j < n - i; j++)
            {
                literalList.add(board[j + i][j]);
            }
            diagonalLine(literalList.size(), literalList, cnfLists);
        }
        for (int i = 0; i < n - 1; i++)
        {
            ArrayList<Integer> literalList = new ArrayList<Integer>();
            for (int j = 0; j < n - i; j++)
            {
                literalList.add(board[j][n - 1 - (j + i)]);
            }
            diagonalLine(literalList.size(), literalList, cnfLists);
        }
        for (int i = 1; i < n - 1; i++)
        {
            ArrayList<Integer> literalList = new ArrayList<Integer>();
            for (int j = 0; j < n - i; j++)
            {
                literalList.add(board[j + i][n - 1 - j]);
            }
            diagonalLine(literalList.size(), literalList, cnfLists);
        }

    }

    private static void diagonalLine(int size, ArrayList<Integer> literalList, ArrayList<ArrayList<Integer>> cnfLists)
    {

        // There is at most one queen in each diagonal line, which means we do
        // not have to put a queen in diagonal line
        // Therefore, we do not need the clause such as x11 V x22 V x33 V x44 = true
        for (int i = 0; i < size - 1; i++)
        {
            for (int j = i + 1; j < size; j++)
            {
                ArrayList<Integer> clauseList = new ArrayList<Integer>();
                clauseList.add(-literalList.get(i));
                clauseList.add(-literalList.get(j));
                cnfLists.add(clauseList);
            }
        }

    }

    private static void printFile(String path, int n, ArrayList<ArrayList<Integer>> cnfLists) throws IOException
    {

        // Print into a file for Minisat
        File file = new File(path);
        if (!file.exists())
        {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        StringBuilder sb = new StringBuilder();
        sb.append("c input.cnf from Queens problem for Minisat\n");
        sb.append("c\n");
        sb.append("p cnf " + n * n + " " + cnfLists.size() + "\n");

        for (ArrayList<Integer> list : cnfLists)
        {
            for (int i : list)
            {
                sb.append(i + " ");
            }
            sb.append(" 0\n");
        }
        fileWriter.write(sb.toString());
        fileWriter.close();
    }

}