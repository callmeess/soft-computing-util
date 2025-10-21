package com.example.softcomputing.neuralnetwork.core;
import java.util.Random;

public class Neuron {
    double[] weights;
    double bias;
    ActivationType activationType;
    
    public enum ActivationType {
        RELU,    
        TANH,    // [-1, 1] - good for steering/directional output
        SIGMOID, // [0, 1] - good for throttle/probability
        LINEAR   // (-∞, ∞) - no activation
    }
    
    public Neuron(int inputCount, Random rand, ActivationType type) {
        weights = new double[inputCount];
        for (int i = 0; i < inputCount; i++) {
            weights[i] = rand.nextDouble() * 2 - 1; // random [-1, 1]
        }
        bias = rand.nextDouble() * 2 - 1;
        this.activationType = type;
    }
    
    private double activate(double x) {
        switch (activationType) {
            case RELU:
                return Math.max(0, x);
            case TANH:
                return Math.tanh(x);  // outputs [-1, 1]
            case SIGMOID:
                return 1.0 / (1.0 + Math.exp(-x));  // outputs [0, 1]
            case LINEAR:
                return x;
            default:
                return x;
        }
    }
    
    // Forward pass for this neuron
    public double forward(double[] inputs) {
        double sum = bias;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return activate(sum);
    }
}