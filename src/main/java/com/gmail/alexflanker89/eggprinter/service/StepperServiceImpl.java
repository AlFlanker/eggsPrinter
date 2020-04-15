package com.gmail.alexflanker89.eggprinter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StepperServiceImpl implements StepperService {

    private final UartService uartService;

    @Override
    public void stop() {
        if (uartService.isOpen()) {
            byte[] pkt = new byte[1];
            pkt[0] = (byte) 'e';
            uartService.send(pkt, 0, pkt.length);
        }
    }

    @Override
    public void clearLines() {
        if (uartService.isOpen()) {
            byte[] pkt = new byte[1];
            pkt[0] = (byte) 'c';
            uartService.send(pkt, 0, pkt.length);
        }
    }

    @Override
    public void addLine(int x, int y, int z) {
        if (uartService.isOpen()) {
            byte[] pkt = new byte[13];
            pkt[0] = (byte) 'a';
            pkt[1] = (byte) ((x) & 0xFF);
            pkt[2] = (byte) ((x >> 8) & 0xFF);
            pkt[3] = (byte) ((x >> 16) & 0xFF);
            pkt[4] = (byte) ((x >> 24) & 0xFF);
            pkt[5] = (byte) ((y) & 0xFF);
            pkt[6] = (byte) ((y >> 8) & 0xFF);
            pkt[7] = (byte) ((y >> 16) & 0xFF);
            pkt[8] = (byte) ((y >> 24) & 0xFF);
            pkt[9] = (byte) ((z) & 0xFF);
            pkt[10] = (byte) ((z >> 8) & 0xFF);
            pkt[11] = (byte) ((z >> 16) & 0xFF);
            pkt[12] = (byte) ((z >> 24) & 0xFF);
            uartService.send(pkt, 0, pkt.length);
        }
    }

    @Override
    public void start() {
        if (uartService.isOpen()) {
            byte[] pkt = new byte[1];
            pkt[0] = (byte)'b';
            uartService.send(pkt, 0 , pkt.length);
        }
    }
}
