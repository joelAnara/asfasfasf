package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "qr_codes")
public class QRCodeEntity {

    @Id
    private String id;

    @Column(name = "plain_text_hash", length = 255)
    private String plainTextHash;

    @Column(name = "encrypted_text", columnDefinition = "TEXT")
    private String encryptedText;

    @Column(name = "encryption_key", columnDefinition = "TEXT")
    private String encryptionKey;

    @Column(name = "iv_value", columnDefinition = "TEXT")
    private String ivValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive;

    // Datos personales
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "ci", unique = true)
    private String ci;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;

    @Column(name = "cargo")
    private String cargo;

    // ✅ NUEVOS CAMPOS
    @Column(name = "departamento")
    private String departamento;

    @Column(name = "area_voluntariado")
    private String areaVoluntariado;

    public QRCodeEntity() {}

    public QRCodeEntity(String id, String plainTextHash, String encryptedText,
                        String encryptionKey, String ivValue, LocalDateTime createdAt) {
        this.id = id;
        this.plainTextHash = plainTextHash;
        this.encryptedText = encryptedText;
        this.encryptionKey = encryptionKey;
        this.ivValue = ivValue;
        this.createdAt = createdAt;
        this.isActive = true;
    }

    public QRCodeEntity(String id, String plainTextHash, String encryptedText,
                        String encryptionKey, String ivValue, LocalDateTime createdAt,
                        String nombre, String ci, String apellidos, LocalDate fechaNacimiento,
                        String fotoUrl, String cargo,
                        String departamento, String areaVoluntariado) {

        this(id, plainTextHash, encryptedText, encryptionKey, ivValue, createdAt);
        this.nombre = nombre;
        this.ci = ci;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoUrl = fotoUrl;
        this.cargo = cargo;
        this.departamento = departamento;
        this.areaVoluntariado = areaVoluntariado;
    }

    // Getters/Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPlainTextHash() { return plainTextHash; }
    public void setPlainTextHash(String plainTextHash) { this.plainTextHash = plainTextHash; }

    public String getEncryptedText() { return encryptedText; }
    public void setEncryptedText(String encryptedText) { this.encryptedText = encryptedText; }

    public String getEncryptionKey() { return encryptionKey; }
    public void setEncryptionKey(String encryptionKey) { this.encryptionKey = encryptionKey; }

    public String getIvValue() { return ivValue; }
    public void setIvValue(String ivValue) { this.ivValue = ivValue; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getAreaVoluntariado() { return areaVoluntariado; }
    public void setAreaVoluntariado(String areaVoluntariado) { this.areaVoluntariado = areaVoluntariado; }
}
