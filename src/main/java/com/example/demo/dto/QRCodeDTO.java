package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QRCodeDTO {
    @JsonProperty("id") private String id;
    @JsonProperty("plain_text") private String plainText;
    @JsonProperty("encrypted_text") private String encryptedText;
    @JsonProperty("encryption_key") private String encryptionKey;
    @JsonProperty("iv") private String iv;
    @JsonProperty("created_at") private String createdAt;
    @JsonProperty("success") private boolean success;
    @JsonProperty("message") private String message;

    // NUEVO: URL pública que se debe codificar en el QR
    @JsonProperty("qr_url") private String qrUrl;

    public QRCodeDTO() {}

    public QRCodeDTO(String id, String plainText, String encryptedText, String encryptionKey,
                     String iv, String createdAt, boolean success, String message) {
        this.id = id;
        this.plainText = plainText;
        this.encryptedText = encryptedText;
        this.encryptionKey = encryptionKey;
        this.iv = iv;
        this.createdAt = createdAt;
        this.success = success;
        this.message = message;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPlainText() { return plainText; }
    public void setPlainText(String plainText) { this.plainText = plainText; }

    public String getEncryptedText() { return encryptedText; }
    public void setEncryptedText(String encryptedText) { this.encryptedText = encryptedText; }

    public String getEncryptionKey() { return encryptionKey; }
    public void setEncryptionKey(String encryptionKey) { this.encryptionKey = encryptionKey; }

    public String getIv() { return iv; }
    public void setIv(String iv) { this.iv = iv; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getQrUrl() { return qrUrl; }
    public void setQrUrl(String qrUrl) { this.qrUrl = qrUrl; }
}
