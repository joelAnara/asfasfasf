package com.example.demo.controller;

import com.example.demo.dto.EncryptionRequest;
import com.example.demo.dto.EncryptionResponse;
import com.example.demo.service.AES256Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.crypto.SecretKey;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/crypto")
@CrossOrigin(origins = "*")
public class CryptoController {

    private final AES256Service aes256Service;

    public CryptoController(AES256Service aes256Service) {
        this.aes256Service = aes256Service;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<EncryptionResponse> encrypt(@Valid @RequestBody EncryptionRequest request) {
        try {
            SecretKey key;
            String keyString;
            if (request.getKey() != null && !request.getKey().isEmpty()) {
                key = aes256Service.getKeyFromString(request.getKey());
                keyString = request.getKey();
            } else {
                key = aes256Service.generateKey();
                keyString = aes256Service.keyToString(key);
            }
            String encrypted = aes256Service.encrypt(request.getText(), key);
            return ResponseEntity.ok(new EncryptionResponse(true, encrypted, keyString, "Texto cifrado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new EncryptionResponse(false, null, null, "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<EncryptionResponse> decrypt(@Valid @RequestBody EncryptionRequest request) {
        try {
            if (request.getKey() == null || request.getKey().isEmpty()) {
                return ResponseEntity.badRequest().body(new EncryptionResponse(false, null, null, "Se requiere clave"));
            }
            SecretKey key = aes256Service.getKeyFromString(request.getKey());
            String decrypted = aes256Service.decrypt(request.getText(), key);
            return ResponseEntity.ok(new EncryptionResponse(true, decrypted, request.getKey(), "Texto descifrado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new EncryptionResponse(false, null, null, "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Servicio AES-256 OK");
    }
}
