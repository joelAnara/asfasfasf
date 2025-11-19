package com.example.demo.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

@Service
public class FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio de uploads", ex);
        }
    }

    /** Guarda archivo subido y devuelve nombre único */
    public String storeFile(MultipartFile file) {
        try {
            String original = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
            String safeName = original.replaceAll("[^a-zA-Z0-9._-]", "_");
            String fileName = UUID.randomUUID() + "_" + safeName;
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo guardar el archivo: " + ex.getMessage(), ex);
        }
    }

    /** Genera un QR PNG desde texto o URL */
   public void generateQRCodeImage(String text, String fileName) {
    try {
        // Tamaño exacto del QR impreso: 3 cm a 300 dpi = 354 px
        int size = 354;

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size);

        Path filePath = this.fileStorageLocation.resolve(fileName);
        MatrixToImageWriter.writeToPath(matrix, "PNG", filePath);

    } catch (Exception e) {
        throw new RuntimeException("Error generando QR: " + e.getMessage(), e);
    }
}



    public Path getRoot() {
        return this.fileStorageLocation;
    }
}
