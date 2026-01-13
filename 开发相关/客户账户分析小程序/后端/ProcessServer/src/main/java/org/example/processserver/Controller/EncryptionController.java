package org.example.processserver.Controller;

import lombok.extern.slf4j.Slf4j;
import org.example.processserver.Utils.SM4Util;
import org.example.processserver.pojo.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for testing encryption logic
 */
@RestController
@Slf4j
public class EncryptionController {

    @GetMapping("/encrypt")
    public ResponseEntity<?> encrypt(@RequestParam("text") String text) {
        log.info("Encrypt request received for text: {}", text);
        try {
            String encryptedText = SM4Util.cipherTextCBC(text);
            return ResponseEntity.ok(ApiResponse.success(encryptedText));
        } catch (Exception e) {
            log.error("Encryption failed", e);
            return ResponseEntity.ok(ApiResponse.fail(500, "Encryption failed: " + e.getMessage()));
        }
    }
}