package com.gmail.alexflanker89.eggprinter;

import com.gmail.alexflanker89.eggprinter.config.MSerialPort;
import com.gmail.alexflanker89.eggprinter.service.ConvertService;
import com.gmail.alexflanker89.eggprinter.service.DrawService;
import com.gmail.alexflanker89.eggprinter.service.ImageCorrect;
import jssc.SerialPortException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class PrintTest {
    @MockBean
    MSerialPort serialPort;
    @Autowired
    private ConvertService convertService;
    @Autowired
    private ImageCorrect imageCorrect;
    @Autowired
    private DrawService drawService;
    @Autowired
    UartTestService uartService;
    @Autowired
    MockMvc mvc;

    @BeforeEach
    public void init() throws SerialPortException, InterruptedException {
        doReturn(true).when(serialPort).isOpen();
        doReturn(true).when(serialPort).openPort();
        doNothing().when(serialPort).write(any());
    }

    @Test
    @DisplayName("смотрим что ушло в com")
    public void printTest_1() throws Exception {
        Path path = Paths.get("src/test/java/resources/test.bmp");
        byte[] bytes = Files.readAllBytes(path);
        MockMultipartFile firstFile = new MockMultipartFile("file", "test4.bmp", "text/plain", bytes);
        mvc.perform(multipart("/load-file").file(firstFile)).andExpect(status().is(200));
        ByteArrayOutputStream stream = (ByteArrayOutputStream)ReflectionTestUtils.getField(uartService, "stream");
        int size = stream.size();
        byte[] array = stream.toByteArray();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        FileWriter fileWriter = new FileWriter("res");
        for (int i = 0; i < stream.size(); i++) {
            if(count == 16) {
                count = 0;
                stringBuilder.append("\n").append(byteToHex(array[i]));
                fileWriter.write(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                count++;

            } else {
                count++;
                stringBuilder.append(" ").append(byteToHex(array[i]));
            }

        }
        fileWriter.flush();
        fileWriter.close();

    }
    public String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits).toUpperCase();
    }

}
