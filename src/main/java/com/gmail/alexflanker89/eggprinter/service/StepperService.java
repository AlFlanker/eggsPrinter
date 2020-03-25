package com.gmail.alexflanker89.eggprinter.service;

public interface StepperService {
    void stop();

    void clearLines();

    void addLine(int x, int y, int z);

    void start();

}
