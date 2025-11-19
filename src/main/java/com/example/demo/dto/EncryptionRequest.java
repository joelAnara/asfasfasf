package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class EncryptionRequest {
    @NotBlank(message = "El texto no puede estar vacío")
    private String text;
    private String key;

    public EncryptionRequest() {}
    public EncryptionRequest(String text, String key) {
        this.text = text;
        this.key = key;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
}
