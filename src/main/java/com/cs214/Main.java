package com.cs214;

import java.io.*;
import java.util.*;

public class Main {
    //READ TEST CASES AND STORE IN 2D PRIMITIVE ARRAY
    public  static int[][] ft53 = readDistanceMatrix("src/main/resources/ft53.atsp");
    public static int[][] ft70 = readDistanceMatrix("src/main/resources/ft70.atsp");
    public static int[][] ftv33 = readDistanceMatrix("src/main/resources/ftv33.atsp");
    public static int[][] ftv170 = readDistanceMatrix("src/main/resources/ftv170.atsp");
    public static int[][] rbg443 = readDistanceMatrix("src/main/resources/rbg443.atsp");
    public static int[][] br17 = readDistanceMatrix("src/main/resources/br17.atsp");
    public static int[][] test4 = readDistanceMatrix("src/main/resources/test4.atsp");
    public static int[][] test6 = readDistanceMatrix("src/main/resources/test6.atsp");
    public static int[][] test20 = readDistanceMatrix("src/main/resources/test20.atsp");
    public static int[][] test15 = readDistanceMatrix("src/main/resources/p01_d.tsp");

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    testAlgorithmsMenu(scanner);
                    break;
                case 2:
                    runEmpiricalAnalysis();
                    break;
                case 3:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }

        } while (choice != 3);

        scanner.close();

    }

    private static void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Test Algorithms");
        System.out.println("2. Run Empirical Analysis");
        System.out.println("3. Exit");
    }

    private static void testAlgorithms(int[][] matrix) {
        System.out.println();
        System.out.println("TEST CASE SIZE: "+matrix.length+" cities");
        printMatrix(matrix); //For testing and verification, test data can be printed
        System.out.println();
        GeneticAlgorithm ga  = new GeneticAlgorithm(matrix);
        List<Integer> solution = ga.solve();

        if(matrix.length>300){
            System.out.println("ABORTING: Held Karp Heap Memory will be exceeded for this Size");
            System.out.println();
            System.out.println("Genetic Algorithm Results");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("GA: Optimal Tour: " + solution);
            System.out.println("GA Total Cost: " + ga.evaluate(solution));
            System.out.println();
            return;
        }

        System.out.println("Held Karp Algorithm Results");
        System.out.println("--------------------------------------------------------------------------------");
        int optimalTour = HeldKarp.solve(matrix);
        System.out.println("Optimal Cost: " +optimalTour);
        System.out.println();

        System.out.println("Genetic Algorithm Results");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("GA: Optimal Tour: " + solution);
        System.out.println("GA Total Cost: " + ga.evaluate(solution));
        System.out.println();

    }

    private static void runEmpiricalAnalysis() {
        System.out.println();
        System.out.println("TEST CASE SIZE: "+test20.length+" cities\n");
        printMatrix(test20); //For testing and verification, test data can be printed
        System.out.println();
        System.out.println("Running empirical analysis...\n");
        List<Integer> heldKarpCounter = new ArrayList<>();
        List<Integer> gaCounter = new ArrayList<>();


        for (int i = 0;i<5;i++){
            GeneticAlgorithm ga  = new GeneticAlgorithm(test20);
            List<Integer> solution = ga.solve();

            System.out.println("Held Karp Algorithm Result "+i);
            System.out.println("--------------------------------------------------------------------------------");
            int optimalTour = HeldKarp.solve(test20);
            System.out.println("Optimal Cost: " +optimalTour);
            System.out.println();

            System.out.println("Genetic Algorithm Result "+i);
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("GA: Optimal Tour: " + solution);
            System.out.println("GA Total Cost: " + ga.evaluate(solution));
            System.out.println();

            heldKarpCounter.add(HeldKarp.getCounter());
            gaCounter.add(ga.getMAX_GENERATIONS());
        }
        // Find minimum, maximum, and average
        int heldKarpMin = Collections.min(heldKarpCounter);
        int heldKarpMax = Collections.max(heldKarpCounter);
        double heldKarpAverage = heldKarpCounter.stream().mapToInt(Integer::intValue).average().orElse(0);

        // Output the results
        System.out.println("HELD-KARP ANALYSIS");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Minimum NFC: " + heldKarpMin);
        System.out.println("Maximum NFC: " + heldKarpMax);
        System.out.println("Average NFC: " + heldKarpAverage);
        System.out.println();

        // Find minimum, maximum, and average
        int gaMin = Collections.min(gaCounter);
        int gaMax = Collections.max(gaCounter);
        double gaAverage = gaCounter.stream().mapToInt(Integer::intValue).average().orElse(0);

        // Output the results
        System.out.println("GENETIC ALGORITHM ANALYSIS");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Best NFC: " + gaMin);
        System.out.println("Maximum NFC: " + gaMax);
        System.out.println("Average NFC: " + gaAverage);
        System.out.println();

    }

    private static void testAlgorithmsMenu(Scanner scanner) {
        int testChoice;
        do {
            displayTestAlgorithmsMenu();
            System.out.print("Enter your test choice (1-11): ");
            testChoice = scanner.nextInt();
            scanner.nextLine();

            switch (testChoice) {
                case 1:
                    testAlgorithms(test4);
                    break;
                case 2:
                    testAlgorithms(test6);
                    break;
                case 3:
                    testAlgorithms(test15);
                    break;
                case 4:
                    testAlgorithms(br17);
                    break;
                case 5:
                    testAlgorithms(test20);
                    break;
                case 6:
                    testAlgorithms(ftv33);
                    break;
                case 7:
                    testAlgorithms(ft53);
                    break;
                case 8:
                    testAlgorithms(ft70);
                    break;
                case 9:
                    testAlgorithms(ftv170);
                    break;
                case 10:
                    testAlgorithms(rbg443);
                    break;
                case 11:
                    System.out.println("Returning to Main Menu");
                    break;
                default:
                    System.out.println("Invalid test choice. Please enter a valid option.");
            }

        } while (testChoice != 11);
    }

    private static void displayTestAlgorithmsMenu() {
        System.out.println("Test Algorithms Menu:");
        System.out.println("1. 4 cities");
        System.out.println("2. 6 cities");
        System.out.println("3. 15 cities");
        System.out.println("4. 17 cities");
        System.out.println("5. 20 cities");
        System.out.println("6. 33 cities");
        System.out.println("7. 53 cities");
        System.out.println("8. 70 cities");
        System.out.println("9. 170 cities");
        System.out.println("10. 443 cities");
        System.out.println("11. Return to Main Menu");
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
