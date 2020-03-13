package com.gmail.alexflanker89.eggPrinter;

import com.gmail.alexflanker89.eggPrinter.service.ConvertService;
import com.gmail.alexflanker89.eggPrinter.service.ConvertServiceImpl;
import com.gmail.alexflanker89.eggPrinter.service.ImageCorrect;
import com.gmail.alexflanker89.eggPrinter.service.ImageCorrectImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;

public class ConvertServiceTest {

    private static Path path;
    private static Path referencePath;

    @BeforeAll
    public static void init() {
        path = Paths.get("src/test/java/resources/reference.bmp");
        referencePath = Paths.get("src/test/java/resources/reference_vector.bmp");
    }

    @Test
    public void test2() throws IOException, URISyntaxException {
        BufferedImage bufferedImage = ImageIO.read(path.toFile());

        ConvertService convertService = new ConvertServiceImpl(0.5, 2.0);

        BufferedImage dst = new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(),1);
        BufferedImage result = convertService.convertToVector(dst, bufferedImage);
        ImageIO.write(result, "bmp", new File("src/test/java/resources/res.bmp"));
        // эталон
        BufferedImage reference = ImageIO.read((referencePath.toFile()));
        int type = reference.getType();
        int type1 = result.getType();
        Assertions.assertTrue(bufferedImagesEqual(reference,result));
    }

    @Test
    public void test3() throws IOException, URISyntaxException {
        Path path = Paths.get("src/test/java/resources/test3.bmp");
        BufferedImage bufferedImage = ImageIO.read(path.toFile());
        ImageCorrect imageCorrect = new ImageCorrectImpl();
        BufferedImage load = imageCorrect.load(bufferedImage);

        ConvertService convertService = new ConvertServiceImpl(0.5, 2.0);

        BufferedImage dst = new BufferedImage(load.getWidth(),load.getHeight(),TYPE_INT_ARGB_PRE);
        BufferedImage result = convertService.convertToVector(dst, load);
        ImageIO.write(result, "bmp", new File("src/test/java/resources/res.bmp"));
        // эталон
//        BufferedImage reference = ImageIO.read((referencePath.toFile()));
        BufferedImage reference = ImageIO.read((Paths.get("src/test/java/resources/result_vector.bmp").toFile()));
        int type = reference.getType();
        int type1 = result.getType();
        Assertions.assertTrue(bufferedImagesEqual(reference,result));
    }

    @Test
    public void test4() throws IOException, URISyntaxException {

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
                    System.out.println(Integer.toHexString(img1.getRGB(x, y)) + " != " + Integer.toHexString(img2.getRGB(x, y)));
                    System.out.println(x + "; " + y);
                    count++;
                }
            }
        }
        System.out.println(count);
        return count == 0;
    }
}
