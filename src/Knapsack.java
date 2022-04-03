import java.io.File;
import java.util.*;

public class Knapsack {
    /* Input file assumed to exist where this program is called */
    static final String INPUT_FILE_PATH = "knap_input.txt";

    public static void main(String[] args) throws Exception {
        Set<Item> items = getItemsFromFile(INPUT_FILE_PATH);
        /* Get max weight of the knapsack from user input */
        double maxWeight = getMaxWeight();
        /* Get the solution */
        Set<Item> solution = findOptimalSet(items, maxWeight);
        /* Get total weight and value of the solution set */
        double weight = 0;
        double value = 0;
        for (Item item : solution) {
            System.out.print(item.name + ", ");
            weight += item.weight;
            value += item.value;
        }
        System.out.println();
        System.out.println("Total weight: " + weight);
        System.out.println("Total value: " + value);
    }

    static Set<Item> findOptimalSet(Set<Item> items, double weightLimit) {
        /* Sort the items by non-increasing order on per weight value */
        Item[] sortedItems =
            items.stream().sorted(Comparator.comparingDouble(Item::getPerWeightValue).reversed()).toArray(Item[]::new);

        Set<Item> path = new HashSet<>();
        Set<Item> bestSet = new HashSet<>();
        helper(sortedItems, path, -1,0, weightLimit, 0, bestSet, 0);

        return bestSet;
    }

    static void helper (
            Item[] items,
            Set<Item> path,
            int idx,
            double weight,
            double weightLimit,
            double value,
            Set<Item> bestSet,
            double best
    ) {
        /* If the weight is not exceeded and total value is greater than the current best, update the current best
         and best set */
        if (weight <= weightLimit && value > best) {
            best = value;
            /* Update the best set */
            bestSet.clear();
            bestSet.addAll(path);
        }
        /* If the current node is not promising, backtrack */
        if (!isPromising(items, idx, weight, weightLimit, value, best)) return;
        /* Move to the next item */
        idx++;
        /* Don't select the next item */
        helper(items, path, idx, weight, weightLimit, value, bestSet, best);
        /* Select the next item */
        Item next = items[idx];
        path.add(next);
        helper(items, path, idx, weight + next.weight, weightLimit, value + next.value, bestSet, best);
        /* When backtracking to the previous node, remove the current node */
        path.remove(items[idx]);
    }

    static boolean isPromising(Item[] items, int idx, double weight, double weightLimit, double value, double best) {
        /* If the weight limit is exceeded (or the same), the node is not promising */
        if (weight >= weightLimit) return false;

        /* Check if a value greater than the current best might be accomplished
        by descending the state space tree further */
        idx++;
        /* Greedily take an item with the most per weight value */
        while (idx < items.length && weight + items[idx].weight <= weightLimit) {
            value += items[idx].value;
            weight += items[idx].weight;
            idx++;
        }
        /* Take a fraction of the next item if there is any */
        if (idx < items.length - 1) value += (weightLimit - weight) * items[idx].getPerWeightValue();
        /* Check if the upper bound of value is greater than the current best */
        return value > best;
    }

    static class Item {
        String name;
        double weight;
        double value;

        Item(String name, double weight, double value) {
            this.name = name;
            this.weight = weight;
            this.value = value;
        }

        double getPerWeightValue() { return value / weight; }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Item)) return false;
            Item another = (Item) o;
            return this.name == another.name;
        }
    }

    static Set<Item> getItemsFromFile(String filePath) throws Exception {
        /* Open the file */
        File file = new File(filePath);
        /* Initialize scanner to read from the opened file */
        Scanner scanner = new Scanner(file);
        /* For storing read items */
        Set<Item> items = new HashSet<>();
        /* Read the file line by line */
        while (scanner.hasNextLine()) {
            /* Get the first line of an item */
            String line = scanner.nextLine();
            /* Ignore blank line at the end of the file, if any */
            if (line.isBlank()) break;
            /* Get the item name */
            String name = line;
            /* Get the item weight */
            int weight = Integer.parseInt(scanner.nextLine());
            /* Get the item value */
            int value = Integer.parseInt(scanner.nextLine());
            /* Instantiate an Item object and append it to the list of items */
            items.add(new Item(name, weight, value));
        }
        /* Return the set of items */
        return items;
    }

    static int getMaxWeight() {
        /* Instantiate a scanner to read from the command line */
        Scanner scanner = new Scanner(System.in);
        /* Prompt the user to type the max weight */
        System.out.print("Max weight for knapsack: ");
        /* Read, parse, and return the max weight */
        return Integer.parseInt(scanner.next());
    }
}
