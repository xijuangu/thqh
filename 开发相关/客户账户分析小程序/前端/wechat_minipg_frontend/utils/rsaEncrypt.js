// src/utils/rsaEncrypt.js

const JSEncrypt = require('../miniprogram_npm/jsencrypt/index') // 引入 JSEncrypt 类

/**
 * 使用 JSEncrypt 对字符串进行 RSA 加密。
 * 此函数接收不带 PEM 标记的纯 Base64 编码公钥。
 *
 * param {string} plaintext - 需要加密的明文字符串。
 * param {string} publicKeyBase64 - 不带 BEGIN/END 标记的 Base64 编码的公钥。
 * returns {string|null} - 成功时返回加密后的 Base64 字符串，失败时返回 null。
 */
const encryptWithRsa = (plaintext, publicKeyBase64) => {
  console.log('encryptWithRsa call')
    // 1. 输入验证
    if (!plaintext || typeof plaintext !== 'string') {
        console.error('[RSA Encrypt] Error: Plaintext must be a non-empty string.');
        return null;
    }
    if (!publicKeyBase64 || typeof publicKeyBase64 !== 'string') {
        console.error('[RSA Encrypt] Error: Public key (Base64) must be a non-empty string.');
        return null;
    }

    try {
        // 2. 将纯 Base64 公钥转换为标准 PEM 格式
        //    JSEncrypt 通常需要 PEM 格式才能正确加载密钥
        const publicKeyPem = `-----BEGIN PUBLIC KEY-----\n${publicKeyBase64}\n-----END PUBLIC KEY-----`;
        // console.log('[RSA Encrypt] Constructed PEM Key:\n', publicKeyPem); // 调试用

        // 3. 初始化 JSEncrypt 实例
        const encryptor = new JSEncrypt();

        // 4. 设置公钥 (使用 PEM 格式)
        const keySetResult = encryptor.setPublicKey(publicKeyPem);
        // if (!keySetResult) {
        //     console.error('[RSA Encrypt] Error: Failed to set the public key. The key might be invalid or malformed.');
        //     return null;
        // }

        // 5. 执行加密
        const encrypted = encryptor.encrypt(plaintext);

        // 6. 检查并返回结果
        if (encrypted && typeof encrypted === 'string') {
            return encrypted; // 返回加密后的 Base64 字符串
        } else {
            console.error('[RSA Encrypt] Error: Encryption returned null or invalid result. Check key size or plaintext length (too long for RSA without padding scheme change).');
            return null;
        }
    } catch (error) {
        console.error('[RSA Encrypt] Unexpected error during encryption:', error);
        return null;
    }
}

module.exports = {
  encryptWithRsa
}