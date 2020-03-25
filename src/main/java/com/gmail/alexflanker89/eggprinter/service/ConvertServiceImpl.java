package com.gmail.alexflanker89.eggprinter.service;

import com.gmail.alexflanker89.eggprinter.model.EggsPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
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
    public List<EggsPoint> convertToVector(BufferedImage origin, BufferedImage mappedImage) {
        assert Objects.nonNull(dist) && Objects.nonNull(accur);

        Graphics2D graphicOrigin = (Graphics2D)origin.getGraphics();
        Graphics2D graphicRes = (Graphics2D)mappedImage.getGraphics();

        List<EggsPoint> eggsPointList0 = new ArrayList<>(MAX_POINTS);
        List<EggsPoint> eggsPointList1;

        int x = 0, y = 0, supX = 0, supY = 0, nLines = 0;

        for (int n = 0; n < MAX_POINTS; n++) {
            int oldX = x, oldY = y;
            supX = x; supY = y;
            int res = 0;
            for (int d = 1; d < 300; d++) {
                for (int j = 0; j < 4; j++) {
                    for (int i = -d; i <= d; i++) {
                        switch (j) {
                            case 0: oldX = x + i; oldY = y - d; break;
                            case 1: oldX = x + d; oldY = y + i; break;
                            case 2: oldX = x - i; oldY = y + d; break;
                            case 3: oldX = x - d; oldY = y - i; break;

                        }
                        if ((oldX >= 0) && (oldX < mappedImage.getWidth()) && (oldY >= 0) && (oldY < mappedImage.getHeight())) {
                            if (getRed(oldX, oldY, mappedImage) < 128) {
                                x = oldX;
                                y = oldY;
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

            if (!eggsPointList0.isEmpty()) {
                if (Math.sqrt((supX - x) * (supX - x) + (supY - y) * (supY - y)) > (dist + 0.5)) {
                    eggsPointList0.add(new EggsPoint(supX, supY, 0));
                    eggsPointList0.add(new EggsPoint(x, y, 0));
                }
            }
            else {
                eggsPointList0.add(new EggsPoint(x, y, 0));
            }
            eggsPointList0.add(new EggsPoint(x, y, 100));
            mappedImage.setRGB(x, y, Color.white.getRGB());
            nLines++;
        }
        eggsPointList0.add(new EggsPoint(x, y, 0));

        eggsPointList1 = new LinkedList<EggsPoint>();

        if (!eggsPointList0.isEmpty()) {
            int n = 0;
            eggsPointList1.add(eggsPointList0.get(n));
            // перебираем эл-ты первого массива
            for (int i = 1; i < eggsPointList0.size(); i++) {
                // если нашли последний
                if (i == eggsPointList0.size() - 1) {
                    n = i;
                    // добавили в новый массив
                    eggsPointList1.add(eggsPointList0.get(n));
                }
                else if ((eggsPointList0.get(i).getZ() == 0) ||
                        (eggsPointList0.get(i - 1).getZ() == 0) ||
                        (eggsPointList0.get(i + 1).getZ() == 0)) {
                    n = i;
                    eggsPointList1.add(eggsPointList0.get(n));
                }
                else {
                    for (int j = n + 1; j < i; j++) {
                        // длинна вектора
                        double s = Math.sqrt(Math.pow(eggsPointList0.get(i).getX() - eggsPointList0.get(n).getX(), 2)
                                + Math.pow(eggsPointList0.get(i).getY() - eggsPointList0.get(n).getY(), 2));
                        if (s > 0) {
                            double d = Math.abs(
                                    (eggsPointList0.get(i).getY() - eggsPointList0.get(n).getY()) *
                                            eggsPointList0.get(j).getX() -
                                            (eggsPointList0.get(i).getX() - eggsPointList0.get(n).getX())
                                                    * eggsPointList0.get(j).getY() +
                                            eggsPointList0.get(i).getX() * eggsPointList0.get(n).getY() -
                                            eggsPointList0.get(i).getY() * eggsPointList0.get(n).getX()) / s;
                            if (d >= accur) {
                                n = i - 1;
                                eggsPointList1.add(eggsPointList0.get(n));
                                break;
                            }
                        }
                        int xmin, ymin, xmax, ymax;
                        if (eggsPointList0.get(n).getX() < eggsPointList0.get(i).getX()) {
                            xmin = eggsPointList0.get(n).getX();
                            xmax = eggsPointList0.get(i).getX();
                        } else {
                            xmin = eggsPointList0.get(i).getX();
                            xmax = eggsPointList0.get(n).getX();
                        }
                        if (eggsPointList0.get(n).getY() < eggsPointList0.get(i).getY()) {
                            ymin = eggsPointList0.get(n).getY();
                            ymax = eggsPointList0.get(i).getY();
                        } else {
                            ymin = eggsPointList0.get(i).getY();
                            ymax = eggsPointList0.get(n).getY();
                        }
                        if ((eggsPointList0.get(j).getX() < xmin - dist) || (eggsPointList0.get(j).getX() > xmax + dist) ||
                                (eggsPointList0.get(j).getY() < ymin - dist) || (eggsPointList0.get(j).getY() > ymax + dist)) {
                            n = i - 1;
                            eggsPointList1.add(eggsPointList0.get(n));
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 1; i < eggsPointList1.size(); i++) {
            if (eggsPointList1.get(i - 1).getZ() <= 0) {
                drawLine(eggsPointList1.get(i - 1).getX(), eggsPointList1.get(i - 1).getY(),
                        eggsPointList1.get(i).getX(), eggsPointList1.get(i).getY(),Color.yellow,graphicRes);
            }
        }

        for (int i = 1; i < eggsPointList1.size(); i++) {
            if (eggsPointList1.get(i-1).getZ() > 0) {
                drawLine(eggsPointList1.get(i - 1).getX(), eggsPointList1.get(i - 1).getY(),
                        eggsPointList1.get(i).getX(), eggsPointList1.get(i).getY(),Color.white,graphicOrigin);
                origin.setRGB(eggsPointList1.get(i - 1).getX(), eggsPointList1.get(i - 1).getY(), Color.white.getRGB());
                origin.setRGB(eggsPointList1.get(i).getX(), eggsPointList1.get(i).getY(), Color.white.getRGB());
                drawLine(eggsPointList1.get(i - 1).getX(), eggsPointList1.get(i - 1).getY(),
                        eggsPointList1.get(i).getX(), eggsPointList1.get(i).getY(),Color.red, graphicRes);
            }
        }

        return eggsPointList1;
    }

    private void drawLine(int x1, int y1, int x2, int y2, Color color, Graphics2D graphic) {
        graphic.setColor(color);
        graphic.drawLine(x1, y1, x2, y2);
    }

    private int getRed(int x, int y, BufferedImage image) {
        return ((image.getRGB(x, y) >> 16) & 0x000000FF);
    }
}
