package com.icc.mybatisextensions;

import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.io.Reader;

public class Asdf {

    public static void main(String[] args) throws IOException {

        String resource = "resources/config-mybatis-plugin-test.xml";
        Reader reader = Resources.getResourceAsReader(resource);

        System.out.println(reader);

        System.out.println("asdfasfsdsf");


    }
}
