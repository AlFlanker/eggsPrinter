package com.gmail.alexflanker89.eggprinter.service;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;

@Service
public class ImageCorrectImpl implements ImageCorrect {

    @Override
    public BufferedImage load (BufferedImage bufferedImage) throws IOException {
        BufferedImage dst = new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(),TYPE_INT_ARGB_PRE);
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
