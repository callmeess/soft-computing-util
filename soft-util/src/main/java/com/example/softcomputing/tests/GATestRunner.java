package com.example.softcomputing.tests;

import com.example.softcomputing.tests.BinaryTargetTest;
import com.example.softcomputing.tests.IntegerMaxSumTest;
import com.example.softcomputing.tests.FloatingMaxProductTest;
import com.example.softcomputing.utils.AppLogger;

public class GATestRunner {

    private static final AppLogger logger = AppLogger.getLogger(GATestRunner.class);

    public static void main(String[] args) {

        logger.info("TEST 1: Binary - Find 11111111");
        BinaryTargetTest.runTest();

        logger.info("\nTEST 2: Integer - Maximize Sum");
        IntegerMaxSumTest.runTest();

        logger.info("\nTEST 3: Floating Point - Maximize Product");
        FloatingMaxProductTest.runTest();

        logger.info("\n========== ALL TESTS COMPLETED ==========");
    }
}