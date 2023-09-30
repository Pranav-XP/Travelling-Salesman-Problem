package com.cs214;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    // Constants for the genetic algorithm
    private static final int POPULATION_SIZE = 100;
    private static final double MUTATION_RATE = 0.01;

    // Maximum number of generations, randomly initialized
    public int MAX_GENERATIONS = (int) (2000 * Math.random());

    public int getMAX_GENERATIONS() {
        return MAX_GENERATIONS;
    }

    public void setMAX_GENERATIONS(int MAX_GENERATIONS) {
        this.MAX_GENERATIONS = MAX_GENERATIONS;
    }

    // Variables for storing the distance matrix and the number of cities
    private int[][] distanceMatrix;
    private int numCities;


    public GeneticAlgorithm(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.numCities = distanceMatrix.length;
    }

    // Main method to initiate the genetic algorithm and find the best solution
    public List<Integer> solve() {
        // Step 1: Initialize the population
        List<List<Integer>> population = initializePopulation();

        // Step 2: Evolve the population over multiple generations
        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            // Step 3: Generate offspring
            List<List<Integer>> offspring = generateOffspring(population);
            // Combine parents and offspring to form a new population
            population.addAll(offspring);
            // Select the next generation
            population = selectNextGeneration(population);
        }
        // Step 4: Return the best solution found
        return findBestSolution(population);
    }

    // Helper method to initialize the population
    private List<List<Integer>> initializePopulation() {
        List<List<Integer>> population = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<Integer> individual = new ArrayList<>();
            for (int j = 0; j < numCities; j++) {
                individual.add(j);
            }
            Collections.shuffle(individual);
            population.add(individual);
        }

        return population;
    }

    // Helper method to generate offspring through crossover and mutation
    private List<List<Integer>> generateOffspring(List<List<Integer>> population) {
        List<List<Integer>> offspring = new ArrayList<>();

        while (offspring.size() < POPULATION_SIZE) {
            List<Integer> parent1 = selectParent(population);
            List<Integer> parent2 = selectParent(population);

            List<Integer> child = crossover(parent1, parent2);
            mutate(child);

            offspring.add(child);
        }

        return offspring;
    }

    // Helper method to select a parent based on fitness
    private List<Integer> selectParent(List<List<Integer>> population) {
        Collections.shuffle(population);
        return Collections.min(population, (a, b) -> evaluate(a) - evaluate(b));
    }

    // Helper method for crossover operation
    private List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        int start = new Random().nextInt(numCities);
        int end = new Random().nextInt(numCities - start) + start;

        // Create a child by copying a segment from parent1 and filling in with the order from parent2
        List<Integer> child = new ArrayList<>(parent1.subList(start, end));
        for (int city : parent2) {
            if (!child.contains(city)) {
                child.add(city);
            }
        }

        return child;
    }

    // Helper method for mutation operation
    private void mutate(List<Integer> individual) {
        // Randomly swap two cities with a probability of MUTATION_RATE
        if (Math.random() < MUTATION_RATE) {
            Collections.swap(individual, new Random().nextInt(numCities), new Random().nextInt(numCities));
        }
    }

    // Helper method to select the next generation based on fitness
    private List<List<Integer>> selectNextGeneration(List<List<Integer>> population) {
        // Sort the population based on fitness and select the top individuals
        population.sort((a, b) -> evaluate(a) - evaluate(b));
        return new ArrayList<>(population.subList(0, POPULATION_SIZE));
    }

    // Objective function to evaluate the fitness of an individual (total distance)
    public int evaluate(List<Integer> individual) {
        int totalDistance = 0;
        for (int i = 0; i < numCities - 1; i++) {
            totalDistance += distanceMatrix[individual.get(i)][individual.get(i + 1)];
        }
        totalDistance += distanceMatrix[individual.get(numCities - 1)][individual.get(0)]; // Return to the starting city
        return totalDistance;
    }

    // Helper method to find the best solution in a population
    private List<Integer> findBestSolution(List<List<Integer>> population) {
        return Collections.min(population, (a, b) -> evaluate(a) - evaluate(b));
    }

    public static void main(String[] args) {
        // Replace this with your distance matrix
        int[][] distanceMatrix = {
                {0, 1, 2},
                {3, 0, 4},
                {5, 6, 0}
        };

        GeneticAlgorithm ga = new GeneticAlgorithm(distanceMatrix);
        List<Integer> solution = ga.solve();

        System.out.println("Best solution: " + solution);
        System.out.println("Total distance: " + ga.evaluate(solution));
    }
}
