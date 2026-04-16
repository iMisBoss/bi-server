package com.newtouch.common.xinchuang.encrypt;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * SM4 对称加密工具类（国密标准）
 */
@Slf4j
@Component
public class SM4Utils {

    private static final int BLOCK_SIZE = 16;

    private byte[] secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = generateKey();
        log.info("SM4 密钥初始化完成");
    }

    /**
     * 生成 SM4 密钥
     */
    public static byte[] generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[BLOCK_SIZE];
        random.nextBytes(key);
        return key;
    }

    /**
     * SM4 加密
     */
    public String encrypt(String plaintext) {
        try {
            byte[] plainBytes = plaintext.getBytes(StandardCharsets.UTF_8);

            PKCS7Padding padding = new PKCS7Padding();
            byte[] paddedBytes = new byte[plainBytes.length + (BLOCK_SIZE - plainBytes.length % BLOCK_SIZE)];
            System.arraycopy(plainBytes, 0, paddedBytes, 0, plainBytes.length);
            padding.addPadding(paddedBytes, plainBytes.length);

            SM4Engine engine = new SM4Engine();
            engine.init(true, new KeyParameter(secretKey));

            byte[] cipherBytes = new byte[paddedBytes.length];
            for (int i = 0; i < paddedBytes.length; i += BLOCK_SIZE) {
                engine.processBlock(paddedBytes, i, cipherBytes, i);
            }

            return Hex.toHexString(cipherBytes);

        } catch (Exception e) {
            log.error("SM4 加密失败", e);
            throw new RuntimeException("SM4 加密失败", e);
        }
    }

    /**
     * SM4 解密
     */
    public String decrypt(String ciphertext) {
        try {
            byte[] cipherBytes = Hex.decode(ciphertext);
            SM4Engine engine = new SM4Engine();
            engine.init(false, new KeyParameter(secretKey));

            byte[] decryptedBytes = new byte[cipherBytes.length];
            for (int i = 0; i < cipherBytes.length; i += BLOCK_SIZE) {
                engine.processBlock(cipherBytes, i, decryptedBytes, i);
            }

            PKCS7Padding padding = new PKCS7Padding();            int padCount = padding.padCount(decryptedBytes);
            byte[] plainBytes = new byte[decryptedBytes.length - padCount];
            System.arraycopy(decryptedBytes, 0, plainBytes, 0, plainBytes.length);

            return new String(plainBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            log.error("SM4 解密失败", e);
            throw new RuntimeException("SM4 解密失败", e);
        }
    }
}
