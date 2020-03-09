package com.gmail.alexflanker89.eggPrinter.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class ImageCorrectImpl implements ImageCorrect {
    private Path path;

    private static final String RES = "result_";

    @Override
    public BufferedImage load (Path path) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(path.toFile());
        BufferedImage dst = ImageIO.read((path.toFile()));

        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                if ((x > 0) && (x < bufferedImage.getWidth() - 1) && (y > 0) && (y < bufferedImage.getHeight() - 1)) {
                    if ((getRed(x, y, bufferedImage) < 128) && (
                            ((getRed(x - 1, y, bufferedImage) >= 128) || (getRed(x + 1, y, bufferedImage) >= 128) ||
                                    (getRed(x, y - 1, bufferedImage) >= 128) || ((getRed(x, y + 1, bufferedImage)) >= 128)))) {
                        dst.setRGB(x, y, Color.black.getRGB());
                    }
                    else {
                        dst.setRGB(x, y, Color.white.getRGB());
                    }
                } else
                    dst.setRGB(x, y, Color.white.getRGB());
            }
        }

        return dst;
    }

    private int getRed(int x, int y, BufferedImage image) {
        return ((image.getRGB(x, y) >> 16) & 0x000000FF);
    }
}
