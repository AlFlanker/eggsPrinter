package com.gmail.alexflanker89.eggprinter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.alexflanker89.eggprinter.model.EggsPoint;
import com.gmail.alexflanker89.eggprinter.service.ConvertServiceImpl;
import com.gmail.alexflanker89.eggprinter.service.ImageCorrect;
import com.gmail.alexflanker89.eggprinter.service.ImageCorrectImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;
import static org.junit.jupiter.api.Assertions.*;

public class ConvertServiceTest {

    @Test
    @DisplayName("проверяем эквивалентность точек векторов с старым алгоритмом")
    public void test3() throws IOException, URISyntaxException {
        Path path = Paths.get("src/test/java/resources/test.bmp");
        BufferedImage bufferedImage = ImageIO.read(path.toFile());
        ImageCorrect imageCorrect = new ImageCorrectImpl();
        BufferedImage load = imageCorrect.load(bufferedImage);

        ConvertServiceImpl convertService = new ConvertServiceImpl(0.5, 2.0);

        BufferedImage dst = new BufferedImage(load.getWidth(),load.getHeight(),TYPE_INT_ARGB_PRE);
        List<EggsPoint> eggsPoints = convertService.convertToVector(dst, load);

        ObjectMapper mapper = new ObjectMapper();
        // два наборы точек
        String json2 = String.join("", Files.readAllLines(Paths.get("src/test/java/resources/json2.txt")));
        List<EggsPoint> eggsPoints1 = mapper.readValue(json2, mapper.getTypeFactory().constructCollectionType(LinkedList.class, EggsPoint.class));

        assertAll("проверяем точки", () -> {
            assertIterableEquals(eggsPoints1, eggsPoints);
            assertEquals(eggsPoints1, eggsPoints);
            assertTrue(eggsPoints.size() == eggsPoints1.size());
        });

    }

    public void test4() throws IOException {

        BufferedImage reference = ImageIO.read((Paths.get("src/test/java/resources/res.bmp").toFile()));
        BufferedImage reference1 = ImageIO.read((Paths.get("src/test/java/resources/reference_vector.bmp").toFile()));
        BufferedImage dst = new BufferedImage(reference.getWidth(),reference.getHeight(),reference1.getType());
        for (int x = 0; x < reference.getWidth(); x++) {
            for (int y = 0; y < reference.getHeight(); y++) {
                if (reference1.getRGB(x, y) != reference.getRGB(x, y)) {
                    dst.setRGB(x,y, Color.GREEN.getRGB());
                } else {
                    dst.setRGB(x,y, Color.WHITE.getRGB());
                }
            }
        }
        ImageIO.write(dst, "bmp", new File("src/test/java/resources/dst.bmp"));

    }


    /**
     * @return true если изображения идентичные
     */
    private static boolean bufferedImagesEqual(final BufferedImage img1, final BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }
        int count =0;
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    count++;
                }
            }
        }
        return count == 0;
    }
}
