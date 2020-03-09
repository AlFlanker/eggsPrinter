package com.gmail.alexflanker89.eggPrinter.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public interface ImageCorrect {
    BufferedImage load (Path path) throws IOException, URISyntaxException;
}
