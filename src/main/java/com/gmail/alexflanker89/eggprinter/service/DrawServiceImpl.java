package com.gmail.alexflanker89.eggprinter.service;

import com.gmail.alexflanker89.eggprinter.model.EggsPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {

    private final StepperService stepperService;

    @Override
    public void draw(List<EggsPoint> points) {
        stepperService.stop();
        stepperService.clearLines();
        points.forEach(eggsPoint -> stepperService.addLine((eggsPoint.getX() - 320) * 5,
                -(eggsPoint.getY() - 100) * 4, eggsPoint.getZ()));
        stepperService.start();
    }
}
