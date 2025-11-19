package com.example.demo.repository;

import com.example.demo.entity.QRCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCodeEntity, String> {

    Optional<QRCodeEntity> findByEncryptedText(String encryptedText);

    Optional<QRCodeEntity> findByPlainTextHash(String plainTextHash);

    @Query("SELECT q FROM QRCodeEntity q WHERE q.isActive = true ORDER BY q.createdAt DESC")
    List<QRCodeEntity> findAllActive();

    @Query("SELECT COUNT(q) FROM QRCodeEntity q WHERE q.isActive = true")
    Long countActiveQRCodes();
}
