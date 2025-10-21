package com.example.softcomputing.usecase;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Random;

public class Car {
    private double x, y;
    private double angle; // Direction in radians
    private final double speed = 2.0;
    private final int size = 10;
    private final Random random;
    private Color color;
    private int score; // How long the car stayed on track
    
    // Laser ray distances (how far each ray travels before hitting track boundary)
    private double[] rayDistances = new double[5];
    private final double maxRayDistance = 200.0;
    
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
    
    /**
     * Update laser rays to detect distances to track boundaries
     * @param track The race track to check against
     */
    public void updateRays(RaceTrack track) {
        // Ray angles relative to car's direction:
        // 0: Front (perpendicular to car's front)
        // 1: Front-Right (45 degrees)
        // 2: Right (90 degrees)
        // 3: Front-Left (-45 degrees)
        // 4: Left (-90 degrees)
        double[] rayAngles = {
            0,              // Front
            Math.PI / 4,    // Front-Right (45°)
            Math.PI / 2,    // Right (90°)
            -Math.PI / 4,   // Front-Left (-45°)
            -Math.PI / 2    // Left (-90°)
        };
        
        for (int i = 0; i < 5; i++) {
            double rayAngle = angle + rayAngles[i];
            rayDistances[i] = castRay(track, rayAngle);
        }
    }
    
    /**
     * Cast a single ray and return distance to track boundary
     */
    private double castRay(RaceTrack track, double rayAngle) {
        double step = 1.0;
        double distance = 0;
        
        while (distance < maxRayDistance) {
            distance += step;
            double checkX = x + Math.cos(rayAngle) * distance;
            double checkY = y + Math.sin(rayAngle) * distance;
            
            // If this point is off the track, return the distance
            if (!track.isCarOnTrack(checkX, checkY)) {
                return distance;
            }
        }
        
        return maxRayDistance;
    }
    
    public void reset(double startX, double startY, double startAngle) {
        this.x = startX;
        this.y = startY;
        this.angle = startAngle;
        this.score = 0;
    }
    
    public void draw(Graphics2D g2d) {
        // Draw laser rays first (so they appear behind the car)
        drawRays(g2d);
        
        g2d.setColor(color);
        g2d.fillOval((int)(x - size/2), (int)(y - size/2), size, size);
        
        // Draw direction indicator
        int dirX = (int)(x + Math.cos(angle) * size);
        int dirY = (int)(y + Math.sin(angle) * size);
        g2d.drawLine((int)x, (int)y, dirX, dirY);
    }
    
    /**
     * Draw the laser rays
     */
    private void drawRays(Graphics2D g2d) {
        double[] rayAngles = {
            0,              // Front
            Math.PI / 4,    // Front-Right (45°)
            Math.PI / 2,    // Right (90°)
            -Math.PI / 4,   // Front-Left (-45°)
            -Math.PI / 2    // Left (-90°)
        };
        
        g2d.setStroke(new BasicStroke(1));
        
        for (int i = 0; i < 5; i++) {
            double rayAngle = angle + rayAngles[i];
            double distance = rayDistances[i];
            
            // Calculate end point of ray
            int endX = (int)(x + Math.cos(rayAngle) * distance);
            int endY = (int)(y + Math.sin(rayAngle) * distance);
            
            // Color the ray based on distance (green = far, red = close)
            float colorValue = (float)(distance / maxRayDistance);
            g2d.setColor(new Color(1.0f - colorValue, colorValue, 0.0f, 0.5f));
            
            // Draw the ray
            g2d.drawLine((int)x, (int)y, endX, endY);
            
            // Draw a small circle at the end of the ray
            g2d.fillOval(endX - 3, endY - 3, 6, 6);
        }
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
    
    public double[] getRayDistances() {
        return rayDistances;
    }
}
