package com.gmail.alexflanker89.eggprinter.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Primary
@Service
public class UartTestService implements UartService {
    private ByteArrayOutputStream stream;

    public UartTestService() {
        this.stream = new ByteArrayOutputStream();
    }

    @Override
    public void send(byte[] data, int start, int quatity) {
        stream.write(data, start, quatity);
    }

    @Override
    public boolean isOpen() {
        return true;
    }
}
