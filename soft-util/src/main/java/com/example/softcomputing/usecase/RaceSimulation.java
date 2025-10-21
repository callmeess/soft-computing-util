package com.example.softcomputing.usecase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RaceSimulation extends JPanel implements ActionListener {
    private final int WIDTH = 800;
    private final int HEIGHT = 800;
    
    private RaceTrack track;
    private Car car;
    private Timer timer;
    private int highScore;
    private int attempts;
    
    public RaceSimulation() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(50, 150, 50)); // Grass background
        
        track = new RaceTrack(WIDTH, HEIGHT);
        car = new Car(track.getStartX(), track.getStartY(), track.getStartAngle());
        
        highScore = 0;
        attempts = 0;
        
        timer = new Timer(20, this); // 50 FPS
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw track
        track.draw(g2d);
        
        // Draw car
        car.draw(g2d);
        
        // Draw score information
        drawScorePanel(g2d);
    }
    
    private void drawScorePanel(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(10, 10, 250, 100);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Current Score: " + car.getScore(), 20, 35);
        g2d.drawString("High Score: " + highScore, 20, 60);
        g2d.drawString("Attempts: " + attempts, 20, 85);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Move the car
        car.move();
        
        // Check if car is still on track
        if (!track.isCarOnTrack(car.getX(), car.getY())) {
            // Car went off track - reset
            if (car.getScore() > highScore) {
                highScore = car.getScore();
            }
            attempts++;
            car.reset(track.getStartX(), track.getStartY(), track.getStartAngle());
        }
        
        // Repaint the panel
        repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Race Track Simulation - Random Car Navigation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            RaceSimulation simulation = new RaceSimulation();
            frame.add(simulation);
            
            // Add control panel
            JPanel controlPanel = new JPanel();
            JButton resetButton = new JButton("Reset Statistics");
            resetButton.addActionListener(e -> {
                simulation.highScore = 0;
                simulation.attempts = 0;
                simulation.car.reset(simulation.track.getStartX(), 
                                   simulation.track.getStartY(), 
                                   simulation.track.getStartAngle());
            });
            controlPanel.add(resetButton);
            
            frame.add(controlPanel, BorderLayout.SOUTH);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
