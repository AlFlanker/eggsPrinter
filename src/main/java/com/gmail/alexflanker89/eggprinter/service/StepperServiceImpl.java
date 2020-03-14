package com.gmail.alexflanker89.eggprinter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StepperServiceImpl implements StepperService {

    private final UartService uartService;

    @Override
    public void stop() {
      /*
    if (serialPortPrinter.IsOpen)
            {
                byte[] pkt = new byte[1];
                pkt[0] = (byte)'e';
                serialPortPrinter.Write(pkt, 0, pkt.Length);
            }
    */
    }

    @Override
    public void clearLines() {
       /*
     if (serialPortPrinter.IsOpen)
            {
                byte[] pkt = new byte[1];
                pkt[0] = (byte)'c';
                serialPortPrinter.Write(pkt, 0, pkt.Length);
            }
     */
    }

    @Override
    public void addLine(int x, int y, int z) {
    /*
     if (serialPortPrinter.IsOpen)
            {
                byte[] pkt = new byte[13];
                pkt[0] = (byte)'a';
                pkt[1] = (byte)((x >> 0) & 0xFF);
                pkt[2] = (byte)((x >> 8) & 0xFF);
                pkt[3] = (byte)((x >> 16) & 0xFF);
                pkt[4] = (byte)((x >> 24) & 0xFF);
                pkt[5] = (byte)((y >> 0) & 0xFF);
                pkt[6] = (byte)((y >> 8) & 0xFF);
                pkt[7] = (byte)((y >> 16) & 0xFF);
                pkt[8] = (byte)((y >> 24) & 0xFF);
                pkt[9] = (byte)((z >> 0) & 0xFF);
                pkt[10] = (byte)((z >> 8) & 0xFF);
                pkt[11] = (byte)((z >> 16) & 0xFF);
                pkt[12] = (byte)((z >> 24) & 0xFF);
                serialPortPrinter.Write(pkt, 0, pkt.Length);
            }
     */

    }

    @Override
    public void start() {
    /*
     if (serialPortPrinter.IsOpen)
            {
                byte[] pkt = new byte[1];
                pkt[0] = (byte)'b';
                serialPortPrinter.Write(pkt, 0, pkt.Length);
            }
     */
    }

}
