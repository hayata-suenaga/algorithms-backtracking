import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphColoring {
    public static void main(String[] args) {
        final boolean[][] ex18Graph = {
                {false, true, false, true, false, false},
                {true, false, true, false, true, false},
                {false, true, false, false, false, true},
                {true, false, false, false, true, false},
                {false, true, false, true, false, true},
                {false, false, true, false, true, false}
        };
        final int ex18NumOfColors = 3;

        final boolean[][] eg55Graph = {
            {false, true, true, true},
            {true, false, true, false},
            {true, true, false, true},
            {true, false, true, false}
        };
        final int eg55NumOfColors = 3;
        /* With two colors, there should be no solution */
        final int eg55AnotherNumOfColors = 2;

        /* P.246 Exercise 18 */
        List<int[]> results = colorGraph(ex18Graph, ex18NumOfColors);
        System.out.println("Number of colorings: " + results.size());
        results.forEach(coloring -> System.out.println(Arrays.toString(coloring)));

        /* P.226 Example 5.5 */
        final String[] colors = {"red", "green", "white"};
        results = colorGraph(eg55Graph, eg55NumOfColors);
        System.out.println("Number of colorings: " + results.size());
        List<Object[]> resultsInText =
                results.stream().map(
                        coloring -> Arrays.stream(coloring).mapToObj(selection -> colors[selection]).toArray()
                ).toList();
        resultsInText.forEach(coloring -> System.out.println(Arrays.toString(coloring)));

        /* P. 226 Example 5.5 No solution with two colors */
        results = colorGraph(eg55Graph, eg55AnotherNumOfColors);
        System.out.println("Number of colorings: " + results.size());
        results.forEach(coloring -> System.out.println(Arrays.toString(coloring)));
    }

    /* Each vertex is named from 0 - V with V being the number of vertices.
    * Input undirected graph is represented by an adjacency matrix */
    static List<int[]> colorGraph(boolean[][] graph, int numOfColors) {
        /* For holding all valid colorings */
        List<int[]> results = new ArrayList<>();
        /* For keeping track of a path on the state space tree */
        int[] coloring = new int[graph.length];
        /* From the root, descend along the state space tree */
        colorGraphHelper(graph, numOfColors, -1, coloring, results);
        return results;
    }

    static void colorGraphHelper(
            boolean[][] graph,
            int numOfColors,
            int currentVertex,
            int[] coloring,
            List<int[]> results
    ) {
        /* If the current node is root, skip checking if it's promising */
        if (currentVertex >= 0) {
            /* If the current node is not promising, backtrack */
            if (!isPromising(graph, currentVertex, coloring)) return;
            /* If the current node is a leaf, record the coloring */
            if (currentVertex == graph.length - 1) { results.add(Arrays.copyOf(coloring, coloring.length)); return; }
        }

        /* Move to the next node (vertex) */
        currentVertex++;
        for (int color = 0; color < numOfColors; color++) {
            /* Paint the vertex */
            coloring[currentVertex] = color;
            /* Recursive call */
            colorGraphHelper(graph, numOfColors, currentVertex, coloring, results);
        }
    }

    static boolean isPromising(boolean[][] graph, int currentVertex, int[] coloring) {
        /* For each of the already colored vertex, check if the color of the current node violates the coloring role */
        for (int vertex = 0; vertex < currentVertex; vertex++) {
            boolean adjacent = graph[currentVertex][vertex];
            boolean hasSameColor = coloring[vertex] == coloring[currentVertex];
            if (adjacent && hasSameColor) return false;
        }

        return true;
    }
}
