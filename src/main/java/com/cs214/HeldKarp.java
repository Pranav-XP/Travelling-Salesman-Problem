package com.cs214;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HeldKarp {

    public static int solve(int[][] distanceMatrix) {
        int numCities = distanceMatrix.length;
        int numStates = 1 << numCities; // Total number of states (2^n)

        // Create a memoization table to store optimal tour lengths
        int[][] dp = new int[numStates][numCities];

        // Create a table to store the parent cities for tour reconstruction
        int[][] parent = new int[numStates][numCities];

        // Initialize the memoization and parent tables
        for (int i = 0; i < numStates; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE / 2);
            Arrays.fill(parent[i], -1);
        }

        // Initialize the starting state with city 0
        dp[1][0] = 0;

        // Iterate over all subsets of cities
        for (int mask = 1; mask < numStates; mask++) {
            for (int u = 0; u < numCities; u++) {
                if ((mask & (1 << u)) != 0) {
                    // Iterate over all possible previous cities
                    for (int v = 0; v < numCities; v++) {
                        if ((mask & (1 << v)) != 0 && v != u) {
                            int newDistance = dp[mask ^ (1 << u)][v] + distanceMatrix[v][u];
                            if (newDistance < dp[mask][u]) {
                                dp[mask][u] = newDistance;
                                parent[mask][u] = v;
                            }
                        }
                    }
                }
            }
        }

        // Find the minimum tour length
        int minTourLength = Integer.MAX_VALUE;
        int endingCity = -1;
        for (int u = 1; u < numCities; u++) {
            int tourLength = dp[numStates - 1][u] + distanceMatrix[u][0];
            if (tourLength < minTourLength) {
                minTourLength = tourLength;
                endingCity = u;
            }
        }

        // Reconstruct the tour
        List<Integer> tour = new ArrayList<>();
        int mask = numStates - 1;
        int currentCity = endingCity;
        while (currentCity != -1) {
            tour.add(currentCity);
            int nextCity = parent[mask][currentCity];
            mask ^= (1 << currentCity); // Update the mask by removing currentCity
            currentCity = nextCity;
        }
        tour.add(0); // Add the starting city to complete the tour

        Collections.reverse(tour);
        // Print the optimal tour
        System.out.print("HK: Optimal Tour:");
        for (int city : tour) {
            System.out.print(city + " -> ");
        }
        System.out.println("Back to start node");

        return minTourLength;
    }
}
