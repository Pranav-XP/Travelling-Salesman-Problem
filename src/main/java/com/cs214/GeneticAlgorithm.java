package com.cs214;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 100;
    private static final double MUTATION_RATE = 0.01;
    private static final int MAX_GENERATIONS = (int) (1500 * Math.random());
    private int counter;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    private int[][] distanceMatrix;
    private int numCities;

    public GeneticAlgorithm(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.numCities = distanceMatrix.length;
    }

    public List<Integer> solve() {
        List<List<Integer>> population = initializePopulation();

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            counter++;
            List<List<Integer>> offspring = generateOffspring(population);
            population.addAll(offspring);
            population = selectNextGeneration(population);
        }

        return findBestSolution(population);
    }

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

    private List<Integer> selectParent(List<List<Integer>> population) {
        Collections.shuffle(population);
        return Collections.min(population, (a, b) -> evaluate(a) - evaluate(b));
    }

    private List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        int start = new Random().nextInt(numCities);
        int end = new Random().nextInt(numCities - start) + start;

        List<Integer> child = new ArrayList<>(parent1.subList(start, end));
        for (int city : parent2) {
            if (!child.contains(city)) {
                child.add(city);
            }
        }

        return child;
    }

    private void mutate(List<Integer> individual) {
        if (Math.random() < MUTATION_RATE) {
            Collections.swap(individual, new Random().nextInt(numCities), new Random().nextInt(numCities));
        }
    }

    private List<List<Integer>> selectNextGeneration(List<List<Integer>> population) {
        population.sort((a, b) -> evaluate(a) - evaluate(b));
        return new ArrayList<>(population.subList(0, POPULATION_SIZE));
    }

    public int evaluate(List<Integer> individual) {
        int totalDistance = 0;
        for (int i = 0; i < numCities - 1; i++) {
            totalDistance += distanceMatrix[individual.get(i)][individual.get(i + 1)];
        }
        totalDistance += distanceMatrix[individual.get(numCities - 1)][individual.get(0)]; // Return to the starting city
        return totalDistance;
    }

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
