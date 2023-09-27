package com.cs219;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ATSPMatrixReader {
    public static void main(String[] args) {
        int[][] ft53 = read("src/main/resources/ft53.atsp");
        int[][]ft70 = read("src/main/resources/ft70.atsp");
        int[][] ftv33 = read("src/main/resources/ftv33.atsp");
        int [][] ftv170 = read("src/main/resources/ftv170.atsp");
        int[][] rbg443 = read("src/main/resources/rbg443.atsp");
int[][] br17 = read("src/main/resources/br17.atsp");

        // Display the read adjacency matrix
        /*System.out.println("ATSP Adjacency Matrix:");
        for (int i = 0; i < rbg443.length; i++) {
            for (int j = 0; j < rbg443.length; j++) {
                System.out.print(rbg443[i][j] + " ");
            }
            System.out.println();
        }*/

        int minTourLengthHeldKarp = heldKarp(br17);
        int minTourLength = solveATSP(br17);
        System.out.println("HeldKarp " + minTourLengthHeldKarp);
        System.out.println("Vanilla " + minTourLength);

    }

    public static int[][] read(String filePath){
        try {
        Queue<Integer> entries = new LinkedList<Integer>();


        // Open the file for reading
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        // Read the number of cities from the first line of the file
        int size = Integer.parseInt(reader.readLine());
        int[][] matrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (entries.isEmpty()) {
                    readNextLine(reader, entries);
                }

                matrix[i][j] = entries.poll();
            }
        }
        reader.close();
        return matrix;

        // Close the file

    } catch (IOException e) {
        System.err.println("Error reading the file: " + e.getMessage());
    }
        return null;
    }
public static void readNextLine(BufferedReader reader, Queue<Integer> entries)
            throws IOException {
        String line = null;

        do {
            line = reader.readLine();

            if (line == null) {
                throw new EOFException("unexpectedly reached EOF");
            }
        } while ((line = line.trim()).isEmpty());

        String[] tokens = line.split("\\s+");

        for (int i = 0; i < tokens.length; i++) {
            entries.offer(Integer.parseInt(tokens[i]));
        }
    }
    public static int solveATSP(int[][] distanceMatrix) {
        int numCities = distanceMatrix.length;
        int numStates = 1 << numCities; // Total number of states (2^n)

        // Create a memoization table to store optimal tour lengths
        int[][] dp = new int[numStates][numCities];

        // Initialize the memoization table with a large value (infinity)
        for (int i = 0; i < numStates; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE / 2);
        }

        // Initialize the starting state with city 0
        dp[1][0] = 0;

        // Iterate over all subsets of cities
        for (int mask = 1; mask < numStates; mask += 2) {
            for (int u = 1; u < numCities; u++) {
                if ((mask & (1 << u)) != 0) {
                    // Iterate over all possible previous cities
                    for (int v = 0; v < numCities; v++) {
                        if ((mask & (1 << v)) != 0 && v != u) {
                            dp[mask][u] = Math.min(dp[mask][u], dp[mask ^ (1 << u)][v] + distanceMatrix[v][u]);
                        }
                    }
                }
            }
        }

        // Calculate the minimum tour length
        int minTourLength = Integer.MAX_VALUE;
        for (int u = 1; u < numCities; u++) {
            minTourLength = Math.min(minTourLength, dp[numStates - 1][u] + distanceMatrix[u][0]);
        }

        return minTourLength;
    }



        public static int heldKarp(int[][] distanceMatrix) {
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
            for (int mask = 1; mask < numStates; mask += 2) {
                for (int u = 1; u < numCities; u++) {
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

            // Reverse the tour to start from the beginning
            List<Integer> reconstructedTour = new ArrayList<>();
            for (int i = tour.size() - 1; i >= 0; i--) {
                reconstructedTour.add(tour.get(i));
            }

            // Print the reconstructed tour
            System.out.println("Reconstructed Tour: " + reconstructedTour);
            return minTourLength;
        }

}


