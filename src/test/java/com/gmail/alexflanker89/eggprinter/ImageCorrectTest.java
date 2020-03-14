package com.gmail.alexflanker89.eggprinter;

import com.gmail.alexflanker89.eggprinter.service.ImageCorrect;
import com.gmail.alexflanker89.eggprinter.service.ImageCorrectImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageCorrectTest {

    private static Path path;
    private static Path referencePath;

    @BeforeAll
    public static void init() {
        path = Paths.get("src/test/java/resources/test.bmp");
        referencePath = Paths.get("src/test/java/resources/reference.bmp");
    }

    @Test
    @DisplayName("Проверка провильности выдления контура")
    public void test1() throws IOException, URISyntaxException {
        ImageCorrect imageCorrect = new ImageCorrectImpl();
        BufferedImage bufferedImage = ImageIO.read(path.toFile());

        BufferedImage load = imageCorrect.load(bufferedImage);
        // эталон
        BufferedImage reference = ImageIO.read((referencePath.toFile()));

        Assertions.assertTrue(bufferedImagesEqual(load, reference));

    }

    /**
     * @return true если изображения идентичные
     */
    private static boolean bufferedImagesEqual(final BufferedImage img1, final BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }

        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

}
