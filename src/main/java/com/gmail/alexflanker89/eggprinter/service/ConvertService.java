package com.gmail.alexflanker89.eggprinter.service;

import com.gmail.alexflanker89.eggprinter.model.EggsPoint;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ConvertService {
    List<EggsPoint> convertToVector(BufferedImage origin, BufferedImage res);
}
