package com.example.softcomputing.usecase;

import java.awt.*;
import java.util.Random;

public class Car {
    private double x, y;
    private double angle; // Direction in radians
    private final double speed = 2.0;
    private final int size = 10;
    private final Random random;
    private Color color;
    private int score; // How long the car stayed on track
    
    public Car(double startX, double startY, double startAngle) {
        this.x = startX;
        this.y = startY;
        this.angle = startAngle;
        this.random = new Random();
        this.color = Color.RED;
        this.score = 0;
    }
    
    public void move() {
        // Randomly adjust direction slightly
        double angleChange = (random.nextDouble() - 0.5) * 0.3; // Random turn between -0.15 and +0.15 radians
        angle += angleChange;
        
        // Move in the current direction
        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;
        
        score++;
    }
    
    public void reset(double startX, double startY, double startAngle) {
        this.x = startX;
        this.y = startY;
        this.angle = startAngle;
        this.score = 0;
    }
    
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int)(x - size/2), (int)(y - size/2), size, size);
        
        // Draw direction indicator
        int dirX = (int)(x + Math.cos(angle) * size);
        int dirY = (int)(y + Math.sin(angle) * size);
        g2d.drawLine((int)x, (int)y, dirX, dirY);
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
}
