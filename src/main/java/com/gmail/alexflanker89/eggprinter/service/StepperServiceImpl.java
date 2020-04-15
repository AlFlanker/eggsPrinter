package com.gmail.alexflanker89.eggprinter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
//        byte[] xx, yy, zz;
//        xx = intToLittleEndian(x);
//        yy = intToLittleEndian(y);
//        zz = intToLittleEndian(z);
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
        log.error("Start!");
        if (uartService.isOpen()) {
            byte[] pkt = new byte[1];
            pkt[0] = (byte)'b';
            uartService.send(pkt, 0 , pkt.length);
        }
    }

//    private static byte[] intToLittleEndian(int numero) {
//        byte[] b = new byte[4];
//        b[0] = (byte) (numero & 0xFF);
//        b[1] = (byte) ((numero >> 8) & 0xFF);
//        b[2] = (byte) ((numero >> 16) & 0xFF);
//        b[3] = (byte) ((numero >> 24) & 0xFF);
//        return b;
//    }

    private static byte[] intToLittleEndian(int numero) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(numero);
        return bb.array();
    }

}
