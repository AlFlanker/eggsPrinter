package com.gmail.alexflanker89.eggprinter.config;


import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
public class MSerialPort {
    private final SerialPort serialPort;

    public MSerialPort() {
        String[] portNames = SerialPortList.getPortNames();
        this.serialPort = new SerialPort(portNames[1]);
        try {
            /*
             * Открываем порт
             */
            serialPort.openPort();
            /*
             * Выставляем параметры
             */
            serialPort.setParams(SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isOpen() {
        return serialPort.isOpened();
    }

    public boolean closePort() throws SerialPortException {
        return serialPort.closePort();
    }

    public boolean OpenPort() {
        if(isOpen()) return true;
        boolean isOpen = false;
        try {
            /*
             * Открываем порт
             */
            isOpen = serialPort.openPort();
            /*
             * Выставляем параметры
             */
            serialPort.setParams(SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
        return isOpen;
    }

    public void write(byte[] data) throws IOException, SerialPortException {
        serialPort.writeBytes(data);
    }
}
