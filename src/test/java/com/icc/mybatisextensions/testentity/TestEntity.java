package com.icc.mybatisextensions.testentity;


import com.icc.mybatisextensions.plugins.annotation.Encryption;
import com.icc.mybatisextensions.plugins.encryption.CryptogramType;

import java.util.Date;

/**
 * @author SoungRyoul Kim Thank my mentor Ikchan Sim who taught me.
 */
public class TestEntity {

    private String id;

    @Encryption(type = CryptogramType.SHA256)
    private String sha256EncryptedField;

    @Encryption(type = CryptogramType.AES256)
    private String aes256EncryptedField;

    private String nonEncryptedField;

    private Date regDate;

    private int integerField;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSha256EncryptedField() {
        return sha256EncryptedField;
    }

    public void setSha256EncryptedField(String sha256EncryptedField) {
        this.sha256EncryptedField = sha256EncryptedField;
    }

    public String getAes256EncryptedField() {
        return aes256EncryptedField;
    }

    public void setAes256EncryptedField(String aes256EncryptedField) {
        this.aes256EncryptedField = aes256EncryptedField;
    }

    public String getNonEncryptedField() {
        return nonEncryptedField;
    }

    public void setNonEncryptedField(String nonEncryptedField) {
        this.nonEncryptedField = nonEncryptedField;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public int getIntegerField() {
        return integerField;
    }

    public void setIntegerField(int integerField) {
        this.integerField = integerField;
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "id='" + id + '\'' +
                ", sha256EncryptedField='" + sha256EncryptedField + '\'' +
                ", aes256EncryptedField='" + aes256EncryptedField + '\'' +
                ", nonEncryptedField='" + nonEncryptedField + '\'' +
                ", regDate=" + regDate +
                ", integerField=" + integerField +
                '}';
    }
}
