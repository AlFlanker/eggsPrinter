package com.gmail.alexflanker89.eggprinter.service;

public interface UartService {
    void send(byte[] data, int start, int quatity);
    boolean isOpen();
}
