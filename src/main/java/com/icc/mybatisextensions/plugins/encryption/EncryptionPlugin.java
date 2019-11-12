package com.icc.mybatisextensions.plugins.encryption;


import com.icc.mybatisextensions.plugins.annotation.Encryption;
import com.icc.mybatisextensions.plugins.encryption.cryptogram.AESCryptogramImpl;
import com.icc.mybatisextensions.plugins.encryption.cryptogram.Cryptogram;
import com.icc.mybatisextensions.plugins.encryption.cryptogram.SHA256CryptogramImpl;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.invoker.Invoker;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * @author SoungRyoul Kim Thank my mentor Ikchan Sim.
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,
        Integer.class}),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {
                Statement.class})})
public class EncryptionPlugin implements Interceptor {

    private static ReflectorFactory reflectorFactory;
    private static Map<CryptogramType, Cryptogram> cryptograms;


    public Object intercept(Invocation invocation) throws Throwable {
        String invocationMethodName = invocation.getMethod().getName();

        if ("prepare".equals(invocationMethodName)) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

            Object parameterObject = statementHandler.getParameterHandler().getParameterObject();
            if (parameterObject == null) {
                return invocation.proceed();
            }
            for (Field field : parameterObject.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Encryption.class)) {
                    if (field.getType() != String.class) {
                        throw new EncryptionFieldNotStringException(
                                "@encryption annotated field must be String");
                    }

                    Reflector reflector = reflectorFactory.findForClass(parameterObject.getClass());

                    Invoker getter = reflector.getGetInvoker(field.getName());

                    Object value = getter.invoke(parameterObject, null);
                    String crypValue = cryptograms.get(field.getAnnotation(Encryption.class).type())
                            .encrypt(value);
                    reflector.getSetInvoker(field.getName()).invoke(parameterObject, new Object[]{crypValue});

                }
            }
            return invocation.proceed();
        } else if ("handleResultSets".equals(invocationMethodName)) {
            Object invocationObject = invocation.proceed();

            if (invocationObject instanceof List) {
                List<Object> resultSetList = new ArrayList<Object>();
                for (Object invocationItem : (List<?>) invocationObject) {

                    Field[] fields = invocationItem.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Encryption.class)) {
                            Reflector reflector = reflectorFactory.findForClass(invocationItem.getClass());
                            Invoker getter = reflector.getGetInvoker(field.getName());

                            Object value = getter.invoke(invocationItem, null);
                            String decryptValue = cryptograms.get(field.getAnnotation(Encryption.class).type())
                                    .decrypt(value);
                            reflector.getSetInvoker(field.getName()).invoke(invocationItem,
                                    new Object[]{decryptValue});
                        }
                    }
                    resultSetList.add(invocationItem);
                }
            }
            return invocationObject;
        }
        return invocation.proceed();
    }


    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }


    public void setProperties(Properties properties) {
        cryptograms = new HashMap<CryptogramType, Cryptogram>();
        if (properties.getProperty("secretKey") == null) {
            throw new NullPointerException("EncryptionPlugin must have property named secretKey ");
        } else {
            String secretKey = String.valueOf(properties.getProperty("secretKey"));
            cryptograms.put(CryptogramType.AES256, new AESCryptogramImpl(secretKey));
        }
        cryptograms.put(CryptogramType.SHA256, new SHA256CryptogramImpl());

        reflectorFactory = new DefaultReflectorFactory();
    }

}
