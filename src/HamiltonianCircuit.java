import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HamiltonianCircuit {
    public static void main(String[] args) {
        /*
           (0)--(1)--(2)
            |   / \   |
            |  /   \  |
            | /     \ |
           (3)-------(4)   */
        boolean graph1[][] =
        {
                {false, true, false, true, false},
                {true, false, true, true, true},
                {false, true, false, false, true},
                {true, true, false, false, true},
                {false, true, true, true, false},
        };

        List<int[]> circuits1 = findHamiltonianCircuit(graph1);
        System.out.println("-- Example 1 Answers --");
        circuits1.forEach(circuit -> System.out.println(Arrays.toString(circuit)));

        boolean ex26Graph[][] =
        {
                {false, true, false, false, true, false, false, false, false, false, false, false},
                {true, false, true, false, false, false, true, true, false, false, false, false},
                {false, true, false, true, false, false, false, true, false, false, false, false},
                {false, false, true, false, false, false, false, false, true, false, false, false},
                {true, false, false, false, false, true, false, false, false, true, false, false},
                {false, false, false, false, true, false, true, false, false, false, true, false},
                {false, true, false, false, false, true, false, true, false, false, false, false},
                {false, true, true, false, false, false, true, false, true, false, false, false},
                {false, false, false, true, false, false, false, true, false, false, false, true},
                {false, false, false, false, true, false, false, false, false, false, true, false},
                {false, false, false, false, false, true, false, false, false, true, false, true},
                {false, false, false, false, false, false, false, false, true, false, true, false}
        };

        List<int[]> ex26Answers = findHamiltonianCircuit(ex26Graph);
        System.out.println("-- Exercise 26 Answers --");
        ex26Answers.forEach(circuit -> System.out.println(Arrays.toString(circuit)));
    }

    static List<int[]> findHamiltonianCircuit(boolean[][] graph) {
        List<int[]> circuits = new ArrayList<>();
        int[] path = new int[graph.length];
        /* Start from vertex 0 */
        path[0] = 0;
        /* Starting from level 1, descend the state space tree */
        hamiltonianHelper(graph, 0, path, circuits);
        /* Return a list of Hamiltonian circuits */
        return circuits;
    }

    static void hamiltonianHelper(boolean[][] graph, int idx, int[] path, List<int[]> circuits) {
        /* If a current path will not lead to a valid circuit, backtrack */
        if (!isPromising(graph, idx, path)) return;
        /* If a leaf has been reached, record the path as a valid circuit */
        if (idx == graph.length - 1) { circuits.add(Arrays.copyOf(path, path.length)); return; }
        /* Go to the next vertex */
        idx++;
        for (int vertex = 1 /* skip the start vertex */; vertex < graph.length; vertex++) {
            path[idx] = vertex;
            hamiltonianHelper(graph, idx, path, circuits);
        }
    }

    static boolean isPromising(boolean[][] graph, int idx, int[] path) {
        /* Check if the current vertex is not already in the path */
        for (int i = 0; i < idx; i++) if (path[i] == path[idx]) return false;
        /* Check if the current vertex and previous vertex is connected */
        if (idx != 0 && !graph[path[idx]][path[idx - 1]]) return false;
        /* If on the leaf, check if the first and last vertices are connected */
        if (idx == graph.length - 1 && !graph[path[idx]][path[0]]) return false;
        return true;
    }

}
