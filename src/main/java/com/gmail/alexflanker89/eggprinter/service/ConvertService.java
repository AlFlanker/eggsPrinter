package com.gmail.alexflanker89.eggprinter.service;

import java.awt.image.BufferedImage;

public interface ConvertService {
    BufferedImage convertToVector(BufferedImage origin, BufferedImage res);
}
