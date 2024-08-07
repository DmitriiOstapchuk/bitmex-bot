package com.dmitrii.ostapchuk.bitmexBot.service;

public interface FibonacciAlgorithmService {
    void startNewCycle();
    void handleMessage(String message);
    RestOrderService getRestOrderService();
    void setInitialQty(int initialQty);
    void setStep(int step);
    void setNumberOfLevels(int numberOfLevels);
    void setNumberOfCycles(int numberOfCycles);
    void setDemo(boolean isDemo);

}