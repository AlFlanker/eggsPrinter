package com.gmail.alexflanker89.eggprinter.controller;

import com.gmail.alexflanker89.eggprinter.model.EggsPoint;
import com.gmail.alexflanker89.eggprinter.service.ConvertService;
import com.gmail.alexflanker89.eggprinter.service.DrawService;
import com.gmail.alexflanker89.eggprinter.service.ImageCorrect;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PrintController {
    private final ConvertService convertService;
    private final ImageCorrect imageCorrect;
    private final DrawService drawService;

    @PostMapping("/load-file")
    public ResponseEntity getImage(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            BufferedImage corrected = imageCorrect.load(image);
            List<EggsPoint> eggsPoints = convertService.convertToVector(image, corrected);
            drawService.draw(eggsPoints);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

}
