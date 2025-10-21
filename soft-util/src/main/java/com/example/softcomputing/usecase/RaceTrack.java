package com.example.softcomputing.usecase;

import java.awt.*;

public class RaceTrack {
    private final int centerX;
    private final int centerY;
    private final int outerRadius;
    private final int innerRadius;
    private final int trackWidth;
    
    public RaceTrack(int width, int height) {
        this.centerX = width / 2;
        this.centerY = height / 2;
        this.outerRadius = Math.min(width, height) / 2 - 50;
        this.trackWidth = 100;
        this.innerRadius = outerRadius - trackWidth;
    }
    
    public void draw(Graphics2D g2d) {
        // Draw outer boundary
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(centerX - outerRadius, centerY - outerRadius, 
                     outerRadius * 2, outerRadius * 2);
        
        // Draw inner boundary
        g2d.drawOval(centerX - innerRadius, centerY - innerRadius, 
                     innerRadius * 2, innerRadius * 2);
        
        // Fill track area
        g2d.setColor(new Color(100, 100, 100, 100));
        g2d.fillOval(centerX - outerRadius, centerY - outerRadius, 
                     outerRadius * 2, outerRadius * 2);
        
        // Clear inner circle (grass)
        g2d.setColor(new Color(34, 139, 34, 150));
        g2d.fillOval(centerX - innerRadius, centerY - innerRadius, 
                     innerRadius * 2, innerRadius * 2);
        
        // Draw starting line
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        int startLineX1 = centerX + innerRadius;
        int startLineX2 = centerX + outerRadius;
        g2d.drawLine(startLineX1, centerY, startLineX2, centerY);
    }
    
    public boolean isCarOnTrack(double carX, double carY) {
        double dx = carX - centerX;
        double dy = carY - centerY;
        double distanceFromCenter = Math.sqrt(dx * dx + dy * dy);
        
        return distanceFromCenter >= innerRadius && distanceFromCenter <= outerRadius;
    }
    
    public double getStartX() {
        return centerX + (outerRadius + innerRadius) / 2.0;
    }
    
    public double getStartY() {
        return centerY;
    }
    
    public double getStartAngle() {
        return -Math.PI / 2; // Starting direction (upward)
    }
    
    public int getCenterX() {
        return centerX;
    }
    
    public int getCenterY() {
        return centerY;
    }
}
