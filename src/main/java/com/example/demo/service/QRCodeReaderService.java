package com.example.demo.service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;      // ✅ usa java.util.List
import java.util.Map;

@Service
public class QRCodeReaderService {

    public String readQRCode(MultipartFile file) {
        try {
            System.out.println("🔍 Iniciando lectura de QR desde imagen...");
            System.out.println("📁 Archivo: " + file.getOriginalFilename());
            System.out.println("📏 Tamaño: " + file.getSize() + " bytes");
            System.out.println("📝 Tipo: " + file.getContentType());

            if (file.isEmpty()) {
                throw new RuntimeException("El archivo está vacío");
            }

            InputStream inputStream = file.getInputStream();
            BufferedImage originalImage = ImageIO.read(inputStream);

            if (originalImage == null) {
                throw new RuntimeException("No se pudo leer la imagen");
            }

            System.out.println("🖼️ Imagen leída: " + originalImage.getWidth() + "x" + originalImage.getHeight());

            // ESTRATEGIAS MEJORADAS PARA FOTOS DE CELULAR
            String result = tryEnhancedDecodingStrategies(originalImage);

            if (result != null) {
                System.out.println("✅ QR ENCONTRADO: " + result.substring(0, Math.min(100, result.length())));
                return result;
            } else {
                throw new RuntimeException(generateQRHelpMessage(originalImage));
            }

        } catch (Exception e) {
            System.out.println("❌ Error final: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // ESTRATEGIAS MEJORADAS PARA FOTOS DE CELULAR
    private String tryEnhancedDecodingStrategies(BufferedImage originalImage) {
        String result;

        // Estrategia 1: Imagen original con diferentes configuraciones
        System.out.println("🎯 Estrategia 1: Original con configuraciones variadas");
        result = decodeWithMultipleConfigs(originalImage);
        if (result != null) return result;

        // Estrategia 2: Redimensionar a tamaño óptimo para QR
        System.out.println("🎯 Estrategia 2: Tamaños óptimos para QR");
        result = tryOptimalSizes(originalImage);
        if (result != null) return result;

        // Estrategia 3: Mejoramiento agresivo de imagen
        System.out.println("🎯 Estrategia 3: Procesamiento agresivo");
        result = tryAggressiveProcessing(originalImage);
        if (result != null) return result;

        return null;
    }

    private String decodeWithMultipleConfigs(BufferedImage image) {
        List<Map<DecodeHintType, Object>> hintsConfigs = Arrays.asList(
                createHints(true, false),  // TRY_HARDER
                createHints(true, true),   // TRY_HARDER + PURE_BARCODE
                createHints(false, true),  // PURE_BARCODE solamente
                createHints(false, false)  // Configuración por defecto
        );

        for (Map<DecodeHintType, Object> hints : hintsConfigs) {
            String result = decodeSingleImage(image, hints);
            if (result != null) return result;
        }
        return null;
    }

    private String tryOptimalSizes(BufferedImage original) {
        // Para fotos muy altas (p. ej., 4080x3072), probar recorte primero
        if (original.getHeight() > 2000) {
            System.out.println("📏 Imagen muy alta, probando recorte central...");
            String result = tryCenterCropping(original);
            if (result != null) return result;
        }

        int[] sizes = {800, 600, 400, 300, 200, 150, 100};

        for (int size : sizes) {
            System.out.println("📐 Probando tamaño: " + size + "px");
            BufferedImage resized = resizeImage(original, size);

            String result = decodeWithMultipleConfigs(resized);
            if (result != null) return result;

            BufferedImage processed = enhanceImage(resized);
            result = decodeWithMultipleConfigs(processed);
            if (result != null) return result;

            BufferedImage extremeContrast = applyContrast(resized, 3.0);
            result = decodeWithMultipleConfigs(extremeContrast);
            if (result != null) return result;
        }
        return null;
    }

    // Recortar el centro (60% del área)
    private String tryCenterCropping(BufferedImage original) {
        try {
            int width = original.getWidth();
            int height = original.getHeight();

            int cropWidth = (int) (width * 0.6);
            int cropHeight = (int) (height * 0.6);
            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            BufferedImage cropped = original.getSubimage(x, y, cropWidth, cropHeight);
            System.out.println("✂️ Imagen recortada: " + cropWidth + "x" + cropHeight);

            return decodeWithMultipleConfigs(cropped);
        } catch (Exception e) {
            System.out.println("⚠️ Error en recorte: " + e.getMessage());
            return null;
        }
    }

    private String tryAggressiveProcessing(BufferedImage original) {
        System.out.println("🎨 Procesamiento agresivo iniciado...");

        int[] aggressiveSizes = {500, 300, 200};

        for (int size : aggressiveSizes) {
            System.out.println("🔥 Procesamiento agresivo en tamaño: " + size + "px");
            BufferedImage resized = resizeImage(original, size);

            double[] contrastLevels = {2.0, 3.0, 4.0};
            for (double contrast : contrastLevels) {
                BufferedImage highContrast = applyContrast(resized, contrast);
                String result = decodeWithMultipleConfigs(highContrast);
                if (result != null) return result;
            }

            BufferedImage bw = convertToBinary(resized);
            String result = decodeWithMultipleConfigs(bw);
            if (result != null) return result;

            BufferedImage inverted = invertColors(resized);
            result = decodeWithMultipleConfigs(inverted);
            if (result != null) return result;

            BufferedImage combo = applyContrast(bw, 2.0);
            result = decodeWithMultipleConfigs(combo);
            if (result != null) return result;
        }

        return null;
    }

    private Map<DecodeHintType, Object> createHints(boolean tryHarder, boolean pureBarcode) {
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, tryHarder);
        hints.put(DecodeHintType.PURE_BARCODE, pureBarcode);
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        return hints;
    }

    private String decodeSingleImage(BufferedImage image, Map<DecodeHintType, Object> hints) {
        try {
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // ---------- Procesamiento de imagen ----------
    private BufferedImage enhanceImage(BufferedImage image) {
        BufferedImage gray = convertToGrayscale(image);
        return applyContrast(gray, 1.8);
    }

    private BufferedImage applyContrast(BufferedImage image, double contrast) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                r = adjustContrast(r, contrast);
                g = adjustContrast(g, contrast);
                b = adjustContrast(b, contrast);

                int newRgb = (r << 16) | (g << 8) | b;
                result.setRGB(x, y, newRgb);
            }
        }
        return result;
    }

    private int adjustContrast(int value, double contrast) {
        return (int) Math.max(0, Math.min(255, ((value - 128) * contrast) + 128));
    }

    private BufferedImage convertToBinary(BufferedImage image) {
        // Deja que Java haga umbral por defecto para TYPE_BYTE_BINARY
        BufferedImage binary = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = binary.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return binary;
    }

    private BufferedImage invertColors(BufferedImage image) {
        BufferedImage inverted = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = 255 - ((rgb >> 16) & 0xFF);
                int g = 255 - ((rgb >> 8) & 0xFF);
                int b = 255 - (rgb & 0xFF);
                int newRgb = (r << 16) | (g << 8) | b;
                inverted.setRGB(x, y, newRgb);
            }
        }
        return inverted;
    }

    private BufferedImage resizeImage(BufferedImage original, int maxSize) {
        int width = original.getWidth();
        int height = original.getHeight();

        if (width <= maxSize && height <= maxSize) {
            return original;
        }

        double scale = Math.min((double) maxSize / width, (double) maxSize / height);
        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(original, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return resized;
    }

    private BufferedImage convertToGrayscale(BufferedImage original) {
        BufferedImage grayImage = new BufferedImage(
                original.getWidth(),
                original.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );

        Graphics2D g2d = grayImage.createGraphics();
        g2d.drawImage(original, 0, 0, null);
        g2d.dispose();
        return grayImage;
    }

    private String generateQRHelpMessage(BufferedImage image) {
        double qrSizePercentage = (100.0 * Math.min(image.getWidth(), image.getHeight()) / Math.max(image.getWidth(), image.getHeight()));
        int megapixels = (image.getWidth() * image.getHeight()) / 1000000;

        return String.format(
                "📱 **QR NO DETECTADO** (%dx%d px - Ocupa %.1f%%)\n\n" +
                "🔍 **PROBLEMA IDENTIFICADO:**\n" +
                "• La imagen es DEMASIADO GRANDE (%d megapíxeles)\n" +
                "• El QR puede estar muy COMPRIMIDO o BORROSO\n" +
                "• La cámara tiene MUCHO RUIDO en alta resolución\n\n" +
                "🎯 **SOLUCIONES INMEDIATAS:**\n" +
                "1. 📸 **ACÉRCATE MÁS** - Que el QR ocupe el 90%% de la pantalla\n" +
                "2. ⚡ **USA RESOLUCIÓN MEDIA** - Configura cámara a 12MP o menos\n" +
                "3. 🔧 **HABILITA HDR** - Mejor balance de luz\n" +
                "4. 📐 **ÁNGULO PERFECTO** - Celular completamente paralelo al QR\n" +
                "5. 💡 **LUZ DIRECTA** - Sin sombras ni reflejos\n\n" +
                "💡 **Consejo profesional:** En cámara, toca la pantalla donde está el QR para enfocar y baja la resolución a 12MP.",
                image.getWidth(), image.getHeight(), qrSizePercentage, megapixels
        );
    }

    public Map<String, Object> verifyQRCodeFromImage(MultipartFile file, QRCodeService qrCodeService) {
        Map<String, Object> result = new HashMap<>();

        try {
            String encryptedText = readQRCode(file);
            boolean exists = qrCodeService.verifyQRCode(encryptedText);

            result.put("success", true);
            result.put("encryptedText", encryptedText);
            result.put("exists", exists);
            result.put("message", exists ?
                    "✅ QR VÁLIDO - Encontrado en la base de datos" :
                    "❌ QR NO VÁLIDO - No existe en la base de datos");

        } catch (Exception e) {
            result.put("success", false);
            result.put("exists", false);
            result.put("message", e.getMessage());
        }

        return result;
    }
}
