import java.util.*;

public class SumOfSubset {
    public static void main(String[] args) {
        int[] example = { 2, 10, 13, 17, 22, 42 };
        Set<List<Integer>> subsets = sumOfSubset(example, 52);
        subsets.forEach(subset -> System.out.println(subset.toString()));
    }

    static Set<List<Integer>> sumOfSubset(int[] nums, int target) {
        // Sort the input numbers
        Arrays.sort(nums);
        // Compute the sum of all numbers in the input array
        int sum = Arrays.stream(nums).sum();
        // For holding valid subsets
        Set<boolean[]> results = new HashSet<>();
        // For holding the path along the state space tree
        boolean[] path = new boolean[nums.length];
        // Starting from the root, traverse through the state space tree
        sumOfSubsetHelper(results, path, nums, target, -1, 0, sum);
        // Construct a set of valid subsets
        Set<List<Integer>> subsets = new HashSet<>();
        List<Integer> temp;
        for (boolean[] choices: results) {
            temp = new ArrayList<>();
            for (int idx = 0; idx < choices.length; idx++)
                if (choices[idx]) temp.add(nums[idx]);
            subsets.add(temp);
        }
        return subsets;
    }

    static void sumOfSubsetHelper(
            Set<boolean[]> results,
            boolean[] path,
            int[] nums,
            int target,
            int idx,
            int proceedingSum,
            int succeedingSum) {
        if (idx >= 0) {
            // If there is a solution at the current node, add the subset and backtrack
            if (proceedingSum == target) { results.add(Arrays.copyOf(path, path.length)); return; }
            // If the current node is not promising, backtrack
            if (!isPromising(target, proceedingSum, succeedingSum)) { path[idx] = false; return; }
        }
        // Move to the next node
        idx++;
        // Consume the number
        succeedingSum -= nums[idx];
        // Don't select the current node
        path[idx] = false;
        sumOfSubsetHelper(results, path, nums, target, idx, proceedingSum, succeedingSum);
        // Select the current node
        path[idx] = true;
        sumOfSubsetHelper(results, path, nums, target, idx, proceedingSum + nums[idx], succeedingSum);
    }

    static boolean isPromising(int target, int proceedingSum, int succeedingSum) {
        boolean exceeded = proceedingSum > target;
        boolean notEnough = proceedingSum + succeedingSum < target;
        return !exceeded && !notEnough;
    }
}
