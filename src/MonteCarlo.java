import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MonteCarlo {

    static final int NUM_OF_TRIALS = 20;

    public static void main(String[] args) {
        int[] estimates = new int[NUM_OF_TRIALS];

        for (int count = 0; count < NUM_OF_TRIALS; count++) {
            int estimate = estimateNumOfPromisingNodes(8);
            System.out.println("Trial " + (count + 1) + ": " + estimate);
            estimates[count] = estimate;
        }

        double average = Arrays.stream(estimates).average().getAsDouble();
        System.out.println("Average: " + average);
    }

    static int estimateNumOfPromisingNodes(int boardSize) {
        /* Keep track of estimated num of promising nodes */
        int numOfNodes = 0;
        /* Record the random path taken */
        List<Integer> pathRecord = new ArrayList<>();
        /* For holding promising children at each level */
        List<Integer> promisingCols;

        /* Keep descending the state space tree until leaf or dead end*/
        while (pathRecord.size() < boardSize) {
            /* Get promising children of the current node */
            promisingCols = new ArrayList<>();
            for (int col = 0; col < boardSize; col++) {
                if (isPromising(pathRecord, col))
                    promisingCols.add(col);
            }
            /* Add the estimated num of promising num of nodes at the current level */
            numOfNodes += promisingCols.size() * boardSize;
            /* If the current node is a dead end, return the estimated num of nodes */
            if (promisingCols.size() == 0) {
                return numOfNodes;
            }
            /* Pick a promising child at random */
            pathRecord.add(getRandomItem(promisingCols));
        }
        /* When the random path reaches a leaf, return the num of estimated nodes */
        return numOfNodes;
    }

    /* Promising function for n-queens */
    static boolean isPromising(List<Integer> pathRecord, int currentColIdx) {
        int currentRowIdx = pathRecord.size();

        int colIdx;
        for (int rowIdx = 0; rowIdx < currentRowIdx; rowIdx++) {
            colIdx = pathRecord.get(rowIdx);
            /* If current position is attacked by an already placed queen, return false */
            if (currentColIdx == rowIdx || currentRowIdx - rowIdx == Math.abs(currentColIdx - colIdx))
                return false;
        }

        return true;
    }

    /* Helper for getting a random promising child */
    static int getRandomItem(List<Integer> list) {
        int randomIdx = (int) Math.floor(Math.random() * list.size());
        return list.get(randomIdx);
    }
}
