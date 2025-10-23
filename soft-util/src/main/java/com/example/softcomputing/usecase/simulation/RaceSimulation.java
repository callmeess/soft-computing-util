package com.example.softcomputing.usecase.simulation;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.example.softcomputing.genetic.chromosome.Factories.FloatingPointChromosomeFactory;
import com.example.softcomputing.genetic.operators.crossover.UniformCrossover;
import com.example.softcomputing.genetic.operators.mutation.UniformMutation;
import com.example.softcomputing.genetic.operators.replacement.ElitismReplacement;
import com.example.softcomputing.genetic.operators.selection.TournametSelection;
import com.example.softcomputing.usecase.simulation.utils.SimulationCanvas;

public class RaceSimulation extends JFrame {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private final GeneticAlgorithm geneticAlgorithm;
    private final SimulationCanvas canvas;
    private Timer updateTimer;

    public RaceSimulation() {
        boolean[][] trackGrid = createHShapedTrack();

        FloatingPointChromosomeFactory factory = new FloatingPointChromosomeFactory(-1.0, 1.0);
        geneticAlgorithm = new GeneticAlgorithm(
                30, // population size
                trackGrid,
                new UniformCrossover<>(factory),
                new UniformMutation(0.05),
                new TournametSelection<>(5),
                new ElitismReplacement<>());

        canvas = new SimulationCanvas(geneticAlgorithm);

        setTitle("Neuroevolution Racing");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(canvas);
        setLocationRelativeTo(null);

        geneticAlgorithm.initializePopulation();
        startSimulation();
    }

    private boolean[][] createHShapedTrack() {
        int gridWidth = WIDTH / 5;
        int gridHeight = HEIGHT / 5;
        boolean[][] track = new boolean[gridHeight][gridWidth];

        int margin = 10;
        int wallThickness = 6;
        int horizontalBarY = gridHeight / 2;

        // Outer boundaries
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < margin; y++)
                track[y][x] = true; // Top
            for (int y = gridHeight - margin; y < gridHeight; y++)
                track[y][x] = true; // Bottom
        }
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < margin; x++)
                track[y][x] = true; // Left
            for (int x = gridWidth - margin; x < gridWidth; x++)
                track[y][x] = true; // Right
        }

        // H vertical bars (inner walls)
        int leftBarX = margin + 30;
        int rightBarX = gridWidth - margin - 30 - wallThickness;

        for (int y = margin; y < horizontalBarY - 20; y++) {
            for (int x = leftBarX; x < leftBarX + wallThickness; x++)
                track[y][x] = true;
            for (int x = rightBarX; x < rightBarX + wallThickness; x++)
                track[y][x] = true;
        }
        for (int y = horizontalBarY + 20; y < gridHeight - margin; y++) {
            for (int x = leftBarX; x < leftBarX + wallThickness; x++)
                track[y][x] = true;
            for (int x = rightBarX; x < rightBarX + wallThickness; x++)
                track[y][x] = true;
        }

        // H horizontal bar walls (top and bottom of middle bar)
        for (int x = leftBarX + wallThickness; x < rightBarX; x++) {
            for (int y = horizontalBarY - 20; y < horizontalBarY - 20 + wallThickness; y++) {
                track[y][x] = true;
            }
            for (int y = horizontalBarY + 20 - wallThickness; y < horizontalBarY + 20; y++) {
                track[y][x] = true;
            }
        }

        return track;
    }

    private void startSimulation() {
        updateTimer = new Timer(16, e -> {
            geneticAlgorithm.updatePopulation();

            if (geneticAlgorithm.shouldEvolve()) {
                geneticAlgorithm.evolveGeneration();
            }

            canvas.repaint();
        });
        updateTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RaceSimulation simulation = new RaceSimulation();
            simulation.setVisible(true);
        });
    }
}
