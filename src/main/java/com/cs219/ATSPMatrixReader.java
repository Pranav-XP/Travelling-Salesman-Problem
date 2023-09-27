package com.cs219;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.LinkedList;



public class ATSPMatrixReader {
    public static void main(String[] args) {
        int [][] ft53 = read("src/main/resources/ft53.atsp");
        int[][]ft70 = read("src/main/resources/ft70.atsp");
        int[][] ftv33 = read("src/main/resources/ftv33.atsp");
        int [][] ftv170 = read("src/main/resources/ftv170.atsp");
        int[][] rbg443 = read("src/main/resources/rbg443.atsp");


        // Display the read adjacency matrix
        System.out.println("ATSP Adjacency Matrix:");
        for (int i = 0; i < rbg443.length; i++) {
            for (int j = 0; j < rbg443.length; j++) {
                System.out.print(rbg443[i][j] + " ");
            }
            System.out.println();
        }

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

}


