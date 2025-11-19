package com.example.demo.controller;

import com.example.demo.entity.QRCodeEntity;
import com.example.demo.service.QRCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/qr")
@CrossOrigin(origins = "*")
public class ProfilePageController {

    private final QRCodeService qrCodeService;

    public ProfilePageController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    // Vista profesional: /qr/profile?encryptedText=...
    @GetMapping("/profile")
    public String profileView(@RequestParam("encryptedText") String encryptedText, Model model) {

        QRCodeEntity q = qrCodeService.getQRCodeWithPersonalData(encryptedText);

        if (q == null) {
            // Estado: QR no encontrado
            model.addAttribute("found", false);
            model.addAttribute("message", "QR no válido o no registrado en la base de datos.");
        } else {
            // Estado: QR encontrado con datos
            model.addAttribute("found", true);
            model.addAttribute("nombre", safe(q.getNombre()));
            model.addAttribute("apellidos", safe(q.getApellidos()));
            model.addAttribute("ci", safe(q.getCi()));
            model.addAttribute("cargo", safe(q.getCargo()));
            model.addAttribute("fechaNacimiento",
                    q.getFechaNacimiento() != null ? q.getFechaNacimiento().toString() : "");
            model.addAttribute("fotoUrl", q.getFotoUrl() != null ? q.getFotoUrl() : "");
            model.addAttribute("message", "QR válido — datos personales encontrados.");
            model.addAttribute("departamento", q.getDepartamento());
            model.addAttribute("areaVoluntariado", q.getAreaVoluntariado());

        }

        // Siempre enviamos el encryptedText (por si quieres mostrarlo o logearlo)
        model.addAttribute("encryptedText", encryptedText);

        return "qr/profile"; // src/main/resources/templates/qr/profile.html
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace("<", "&lt;").replace(">", "&gt;");
    }
}
