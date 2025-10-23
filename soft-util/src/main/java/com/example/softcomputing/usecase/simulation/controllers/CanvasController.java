//package com.example.softcomputing.usecase.simulation.controllers;
//
//import java.awt.event.KeyEvent;
//
//import com.example.softcomputing.usecase.simulation.SimulationEngine;
//import com.example.softcomputing.usecase.simulation.utils.SimulationCanvas;
//
//public interface CanvasController {
//    void handleKeyPress(int keyCode);
//
//    static CanvasController create(SimulationEngine engine, SimulationCanvas canvas, Runnable onExit) {
//        return keyCode -> {
//            if (canvas.isEditMode()) {
//                switch (keyCode) {
//                    case KeyEvent.VK_ESCAPE:
//                        canvas.setEditMode(false);
//                        break;
//                    case KeyEvent.VK_D:
//                        canvas.setDrawWalls(true);
//                        break;
//                    case KeyEvent.VK_E:
//                        canvas.setDrawWalls(false);
//                        break;
//                    case KeyEvent.VK_C:
//                        canvas.clearTrack();
//                        break;
//                }
//            } else {
//                switch (keyCode) {
//                    case KeyEvent.VK_ESCAPE:
//                        engine.stop();
//                        onExit.run();
//                        break;
//                    case KeyEvent.VK_SPACE:
//                        if (engine.isTraining()) {
//                            engine.stopTraining();
//                        } else {
//                            engine.startTraining();
//                        }
//                        break;
//                }
//            }
//        };
//    }
//}