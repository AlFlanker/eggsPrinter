package com.gmail.alexflanker89.eggprinter;

import com.gmail.alexflanker89.eggprinter.config.MSerialPort;
import com.gmail.alexflanker89.eggprinter.service.UartTestService;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
    UartTestService uartService;
    @Autowired
    MockMvc mvc;

    @BeforeEach
    private void init() throws SerialPortException, InterruptedException {
        doReturn(true).when(serialPort).isOpen();
        doReturn(true).when(serialPort).openPort();
        doNothing().when(serialPort).write(any());
    }

    @Test
    @DisplayName("смотрим что ушло в com")
    public void printTest_1() throws Exception {
        Path path = Paths.get("src/test/java/resources/test4.bmp");
        // 'эталон
        Path rawPath = Paths.get("src/test/java/resources/rawData.txt");
        byte[] rawBytes = Files.readAllBytes(rawPath);
        byte[] bytes = Files.readAllBytes(path);
        MockMultipartFile firstFile = new MockMultipartFile("file", "test.bmp", "text/plain", bytes);
        mvc.perform(multipart("/load-file").file(firstFile)).andExpect(status().is(200));
        ByteArrayOutputStream stream = (ByteArrayOutputStream)ReflectionTestUtils.getField(uartService, "stream");

        assert stream != null;
        byte[] array = stream.toByteArray();
        assertArrayEquals(array, rawBytes);
    }
 }
