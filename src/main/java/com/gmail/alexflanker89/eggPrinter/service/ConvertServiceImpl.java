package com.gmail.alexflanker89.eggPrinter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.alexflanker89.eggPrinter.model.EggsPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ConvertServiceImpl implements ConvertService {
    private static final int MAX_POINTS = 50000;

    private final Double accur;
    private final Double dist;

    public ConvertServiceImpl(@Value("${spring.eggsPrinter.accur}") Double accur, @Value("${spring.eggsPrinter.dist}") Double dist) {
        this.accur = accur;
        this.dist = dist;
    }

    @Override
    public BufferedImage convertToVector(BufferedImage origin, BufferedImage mappedImage) {
        assert Objects.nonNull(dist) && Objects.nonNull(accur);

        Graphics2D graphicOrigin = (Graphics2D)origin.getGraphics();
        Graphics2D graphicRes = (Graphics2D)mappedImage.getGraphics();

        java.util.List<EggsPoint> eggsPointList_0 = new ArrayList<>(MAX_POINTS);
        List<EggsPoint> eggsPointList_1;

        int x = 0, y = 0, xx = 0, yy = 0, nLines = 0;

        for (int n = 0; n < MAX_POINTS; n++) {
            int x_ = x, y_ = y;
            xx = x; yy = y;
            int res = 0;
            for (int d = 1; d < 300; d++) {
                for (int j = 0; j < 4; j++) {
                    for (int i = -d; i <= d; i++) {
                        switch (j) {
                            case 0: x_ = x + i; y_ = y - d; break;
                            case 1: x_ = x + d; y_ = y + i; break;
                            case 2: x_ = x - i; y_ = y + d; break;
                            case 3: x_ = x - d; y_ = y - i; break;

                        }
                        if ((x_ >= 0) && (x_ < mappedImage.getWidth()) && (y_ >= 0) && (y_ < mappedImage.getHeight())) {
                            if (getRed(x_, y_, mappedImage) < 128) {
                                x = x_;
                                y = y_;
                                res = 1;
                                break;
                            }
                        }
                    }
                    if (res != 0) {
                        break;
                    }
                }
                if (res != 0) {
                    break;
                }
            }
            if (res == 0) {
                break;
            }

            if (eggsPointList_0.size() > 0) {
                if (Math.sqrt((xx - x) * (xx - x) + (yy - y) * (yy - y)) > (dist + 0.5)) {
                    eggsPointList_0.add(new EggsPoint(xx, yy, 0));
                    eggsPointList_0.add(new EggsPoint(x, y, 0));
                }
            }
            else {
                eggsPointList_0.add(new EggsPoint(x, y, 0));
            }
            eggsPointList_0.add(new EggsPoint(x, y, 100));
            mappedImage.setRGB(x, y, Color.white.getRGB());
            nLines++;
        }
        eggsPointList_0.add(new EggsPoint(x, y, 0));

        eggsPointList_1 = new ArrayList<>(MAX_POINTS);

        if (eggsPointList_0.size() > 0) {
            int n = 0;
            eggsPointList_1.add(eggsPointList_0.get(n));
            // перебираем эл-ты первого массива
            for (int i = 1; i < eggsPointList_0.size(); i++) {
                // если нашли последний
                if (i == eggsPointList_0.size() - 1) {
                    n = i;
                    // добавили в новый массив
                    eggsPointList_1.add(eggsPointList_0.get(n));
                }
                else if ((eggsPointList_0.get(i).getZ() == 0) ||
                        (eggsPointList_0.get(i - 1).getZ() == 0) ||
                        (eggsPointList_0.get(i + 1).getZ() == 0)) {
                    n = i;
                    eggsPointList_1.add(eggsPointList_0.get(n));
                }
                else {
                    for (int j = n + 1; j < i; j++) {
                        // длинна вектора
                        double s = Math.sqrt(Math.pow(eggsPointList_0.get(i).getX() - eggsPointList_0.get(n).getX(), 2)
                                + Math.pow(eggsPointList_0.get(i).getY() - eggsPointList_0.get(n).getY(), 2));
                        if (s > 0) {
                            double d = Math.abs(
                                    (eggsPointList_0.get(i).getY() - eggsPointList_0.get(n).getY()) *
                                            eggsPointList_0.get(j).getX() -
                                            (eggsPointList_0.get(i).getX() - eggsPointList_0.get(n).getX())
                                                    * eggsPointList_0.get(j).getY() +
                                            eggsPointList_0.get(i).getX() * eggsPointList_0.get(n).getY() -
                                            eggsPointList_0.get(i).getY() * eggsPointList_0.get(n).getX()) / s;
                            if (d >= accur) {
                                n = i - 1;
                                eggsPointList_1.add(eggsPointList_0.get(n));
                                break;
                            }
                        }
                        int xmin, ymin, xmax, ymax;
                        if (eggsPointList_0.get(n).getX() < eggsPointList_0.get(i).getX()) {
                            xmin = eggsPointList_0.get(n).getX();
                            xmax = eggsPointList_0.get(i).getX();
                        } else {
                            xmin = eggsPointList_0.get(i).getX();
                            xmax = eggsPointList_0.get(n).getX();
                        }
                        if (eggsPointList_0.get(n).getY() < eggsPointList_0.get(i).getY()) {
                            ymin = eggsPointList_0.get(n).getY();
                            ymax = eggsPointList_0.get(i).getY();
                        } else {
                            ymin = eggsPointList_0.get(i).getY();
                            ymax = eggsPointList_0.get(n).getY();
                        }
                        if ((eggsPointList_0.get(j).getX() < xmin - dist) || (eggsPointList_0.get(j).getX() > xmax + dist) ||
                                (eggsPointList_0.get(j).getY() < ymin - dist) || (eggsPointList_0.get(j).getY() > ymax + dist)) {
                            n = i - 1;
                            eggsPointList_1.add(eggsPointList_0.get(n));
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 1; i < eggsPointList_1.size(); i++) {
            if (eggsPointList_1.get(i - 1).getZ() <= 0) {
                drawLine(eggsPointList_1.get(i - 1).getX(), eggsPointList_1.get(i - 1).getY(),
                        eggsPointList_1.get(i).getX(), eggsPointList_1.get(i).getY(),Color.yellow,graphicRes);
            }
        }

        for (int i = 1; i < eggsPointList_1.size(); i++) {
            if (eggsPointList_1.get(i-1).getZ() > 0) {
                drawLine(eggsPointList_1.get(i - 1).getX(), eggsPointList_1.get(i - 1).getY(),
                        eggsPointList_1.get(i).getX(), eggsPointList_1.get(i).getY(),Color.white,graphicOrigin);
                origin.setRGB(eggsPointList_1.get(i - 1).getX(), eggsPointList_1.get(i - 1).getY(), Color.white.getRGB());
                origin.setRGB(eggsPointList_1.get(i).getX(), eggsPointList_1.get(i).getY(), Color.white.getRGB());
                drawLine(eggsPointList_1.get(i - 1).getX(), eggsPointList_1.get(i - 1).getY(),
                        eggsPointList_1.get(i).getX(), eggsPointList_1.get(i).getY(),Color.red, graphicRes);
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json1 = objectMapper.writeValueAsString(eggsPointList_0);
            String json2 = objectMapper.writeValueAsString(eggsPointList_1);
            Files.write(new File("C:\\Users\\AlexFlanker\\Documents\\work\\EggPrinter\\java\\json1_.txt").toPath(),json1.getBytes());
            Files.write(new File("C:\\Users\\AlexFlanker\\Documents\\work\\EggPrinter\\java\\json2_.txt").toPath(),json2.getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return mappedImage;
    }

    private void drawLine(int x1, int y1, int x2, int y2, Color color, Graphics2D graphic) {
        graphic.setColor(color);
        graphic.drawLine(x1, y1, x2, y2);
    }

    private int getRed(int x, int y, BufferedImage image) {
        return ((image.getRGB(x, y) >> 16) & 0x000000FF);
    }
}