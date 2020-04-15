package com.gmail.alexflanker89.eggprinter.service;

import com.gmail.alexflanker89.eggprinter.config.MSerialPort;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UartServiceImpl implements UartService {

    private final MSerialPort port;

    @Override
    @SneakyThrows
    public void send(byte[] data, int start, int quantity) {
        Thread.sleep(70);
        port.write(data);
    }

    @Override
    public boolean isOpen() {
        return port.isOpen();
    }
}
