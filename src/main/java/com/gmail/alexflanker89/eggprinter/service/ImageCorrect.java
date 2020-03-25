package com.gmail.alexflanker89.eggprinter.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

public interface ImageCorrect {
    BufferedImage load (BufferedImage image) throws IOException, URISyntaxException;
}
