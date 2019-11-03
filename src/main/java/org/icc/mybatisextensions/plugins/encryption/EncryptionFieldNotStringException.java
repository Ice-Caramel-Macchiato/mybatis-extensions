package org.icc.mybatisextensions.plugins.encryption;

import org.apache.ibatis.plugin.PluginException;

/**
 * @author SoungRyoul Kim Thanks my mentor Ikchan Sim.
 */
public class EncryptionFieldNotStringException extends PluginException {

  private static final long serialVersionUID = -781680570680579695L;

  public EncryptionFieldNotStringException(String message) {
    super(message);
  }

}
