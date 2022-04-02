import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NQueens {
    public static void main(String[] args) {
        List<int[]> solutions = nQueens(8);
        System.out.println("Num of solutions: " + solutions.size());
        solutions.forEach(cols -> {
            for (int col : cols) System.out.print(col + " ");
            System.out.println();
        });
    }

     static List<int[]> nQueens(int boardSize) {
        List<int[]> solutions = new ArrayList<>();

        /* For keeping track of a path along the state space tree */
        int path[] = new int[boardSize];
        /* Starting from the root node, traverse the state space tree for valid solutions */
        nQueensHelper(solutions, path, -1, boardSize);

        return solutions;
    }

    static void nQueensHelper(List<int[]> solutions, int[] path, int rowIdx, int boardSize) {
        /* Check if the current node is not promising, backtrack (if root node, don't check) */
        if (rowIdx != -1 && !isPromising(path, rowIdx)) return;
        /* If the current node is a leaf, solution is found */
        if (rowIdx == boardSize - 1) { solutions.add(Arrays.copyOf(path, boardSize)); return; }
        rowIdx++;
        /* Recursively call the helper on child nodes (next row) */
        for (int col = 0; col < boardSize; col++) {
            path[rowIdx] = col;
            nQueensHelper(solutions, path, rowIdx, boardSize);
        }
    }

    static boolean isPromising(int[] path, int currentRowIdx) {
        /* Get the col index of the node (cell) being checked */
        int currentColIdx = path[currentRowIdx];
        /* Check if the cell can be attacked by already placed queens */
        int colIdx;
        boolean colSafe;
        boolean diagonalSafe;
        for (int rowIdx = 0; rowIdx < currentRowIdx; rowIdx++) {
            colIdx = path[rowIdx];
            /* Check if another queen is on the same col */
            colSafe = colIdx != currentColIdx;
            /* Check if another queen is on the diagonal line */
            diagonalSafe = currentRowIdx - rowIdx != Math.abs(currentColIdx - colIdx);
            /* If the queen can be attacked, return false */
            if (!colSafe || !diagonalSafe) return false;
        }
        /* Return true if the node is promising */
        return true;
    }
}
