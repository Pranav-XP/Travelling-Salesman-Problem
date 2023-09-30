package com.cs214;

import org.apache.commons.lang3.time.StopWatch;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AlgorithmRace {

    private JLabel heldKarpLabel;
    private JLabel geneticAlgorithmLabel;
    private JButton startButton;

    private StopWatch heldKarpStopwatch;
    private StopWatch geneticAlgorithmStopwatch;

    private XYSeries heldKarpSeries;
    private XYSeries geneticAlgorithmSeries;

    public AlgorithmRace() {
        JFrame frame = new JFrame("Algorithm Race");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        heldKarpLabel = createLabel("Held-Karp");
        geneticAlgorithmLabel = createLabel("Genetic Algorithm");

        heldKarpSeries = new XYSeries("Held-Karp");
        geneticAlgorithmSeries = new XYSeries("Genetic Algorithm");

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(heldKarpLabel);
        panel.add(geneticAlgorithmLabel);

        startButton = new JButton("Start Race");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startRace();
            }
        });
        panel.add(startButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    private void startRace() {
        int[][]distanceMatrix = Main.readDistanceMatrix("src/main/resources/test20.atsp");
        GeneticAlgorithm ga = new GeneticAlgorithm(distanceMatrix);
        heldKarpStopwatch = StopWatch.createStarted();
        geneticAlgorithmStopwatch = StopWatch.createStarted();

        startButton.setEnabled(false); // Disable the button during the race

        // Simulate algorithm work
        new Thread(() -> {
            try {
                runHeldKarpAlgorithm(distanceMatrix);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                runGeneticAlgorithm(ga);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void runHeldKarpAlgorithm(int[][] matrix) throws InterruptedException {
        for (int i = 0;i<11;i++){
            int cost = HeldKarp.solve(matrix);
            heldKarpSeries.add(i,cost);
            System.out.println("Optimal Cost: " +cost);
            System.out.println();
        }
        heldKarpStopwatch.stop();

        // Display the elapsed time
        SwingUtilities.invokeLater(() -> {
            heldKarpLabel.setText("Held-Karp: " + heldKarpStopwatch.getTime() + " ms");
            startButton.setEnabled(true); // Enable the button after the race
            showResultsIfBothCompleted();
        });
    }

    private void runGeneticAlgorithm(GeneticAlgorithm ga) throws InterruptedException {
        for (int i = 0;i<11;i++){
            List<Integer> solution = ga.solve();
            int cost = ga.evaluate(solution);
            geneticAlgorithmSeries.add(i,cost);
            System.out.println("GA: Optimal Tour: " + solution);
            System.out.println("GA Total Cost: " + cost);
            System.out.println();
        }
        geneticAlgorithmStopwatch.stop();

        // Display the elapsed time
        SwingUtilities.invokeLater(() -> {
            geneticAlgorithmLabel.setText("Genetic Algorithm: " + geneticAlgorithmStopwatch.getTime() + " ms");
            startButton.setEnabled(true); // Enable the button after the race
            showResultsIfBothCompleted();
        });
    }

    private void showResultsIfBothCompleted() {
        if (heldKarpStopwatch.isStopped() && geneticAlgorithmStopwatch.isStopped()) {
            JOptionPane.showMessageDialog(null, "Race completed! Showing results.");
            showResults();
        }
    }

    private void showResults() {
        // Create dataset and chart
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(heldKarpSeries);
        dataset.addSeries(geneticAlgorithmSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Algorithm Comparison",
                "Number of Function Calls",
                "Fitness Value(Total Distance)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false

        );

        // Customize chart
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, true);
        plot.setRenderer(renderer);

        // Display chart in a JFrame
        JFrame chartFrame = new JFrame("Algorithm Race Results");
        chartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chartFrame.setSize(800, 600);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));

        chartFrame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        chartFrame.pack();
        chartFrame.setLocationRelativeTo(null);
        chartFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AlgorithmRace());
    }
}
