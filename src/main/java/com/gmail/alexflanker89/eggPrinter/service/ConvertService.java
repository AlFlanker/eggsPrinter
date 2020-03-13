package com.gmail.alexflanker89.eggPrinter.service;

import java.awt.image.BufferedImage;

public interface ConvertService {
    BufferedImage convertToVector(BufferedImage origin, BufferedImage res);
}
