package com.example.demo.dto;

public class EncryptionResponse {
    private boolean success;
    private String text;
    private String key;
    private String message;

    public EncryptionResponse() {}
    public EncryptionResponse(boolean success, String text, String key, String message) {
        this.success = success;
        this.text = text;
        this.key = key;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
