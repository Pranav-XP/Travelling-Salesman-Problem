package com.cs214;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        int[][] ft53 = readDistanceMatrix("src/main/resources/ft53.atsp");
        int[][] ft70 = readDistanceMatrix("src/main/resources/ft70.atsp");
        int[][] ftv33 = readDistanceMatrix("src/main/resources/ftv33.atsp");
        int[][] ftv170 = readDistanceMatrix("src/main/resources/ftv170.atsp");
        int[][] rbg443 = readDistanceMatrix("src/main/resources/rbg443.atsp");
        int[][] br17 = readDistanceMatrix("src/main/resources/br17.atsp");
        int[][] test = readDistanceMatrix("src/main/resources/test20.atsp");

        printMatrix(test); //For testing and verification, test data can be printed
        GeneticAlgorithm ga  = new GeneticAlgorithm(test);
        List<Integer> solution = ga.solve();


        System.out.println();
        System.out.println("GA: Optimal Tour: " + solution);
        System.out.println("GA Total Cost: " + ga.evaluate(solution));

        int optimalTour = HeldKarp.solve(test);
        System.out.println("Optimal Cost: " +optimalTour);
    }

    public static int[][] readDistanceMatrix(String fileName){
        try{
            // Create a Scanner object to read the file.
            Scanner scanner = new Scanner(new File(fileName));

            // Read the first line of the file to get the number of cities.
            int n = scanner.nextInt();

            // Create a 2D array to store the distance matrix.
            int[][] distanceMatrix = new int[n][n];

            // Read each line of the file and parse the distances into the distance matrix.
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    distanceMatrix[i][j] = scanner.nextInt();
                }
            }
            // Close the Scanner object.
            scanner.close();

            return distanceMatrix;
        }catch(FileNotFoundException e){
            System.err.println("Error reading file: "+e.getMessage());
        }
        return null;

    }

    public static void printMatrix(int[][] matrix) {
        // Display the distance matrix
        System.out.println("Distance Matrix:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
