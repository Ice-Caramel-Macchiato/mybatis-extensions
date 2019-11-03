package org.icc.mybatisextensions.plugins.encryption.cryptogram;

public interface Cryptogram {

  String encrypt(Object word) throws CryptogramFailException;

  String decrypt(Object word) throws CryptogramFailException;
}
