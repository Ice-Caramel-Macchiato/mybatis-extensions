package com.icc.mybatisextensions;

import com.icc.mybatisextensions.testentity.TestEntity;
import com.icc.mybatisextensions.testentity.TestEntityMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author SoungRyoul Kim Thanks my mentor Ikchan Sim.
 */


public class EncryptionPluginTest {

    private static SqlSessionFactory sqlSessionFactory;

    private TestEntity testEntity;


    @BeforeClass
    public static void createDB() throws SQLException, ClassNotFoundException, IOException {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        initDatabase();

        String resource = "com/icc/mybatisextensions/resources/config-mybatis-plugin-test.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:plugin_test_db", "sa", "");

    }

    private static void initDatabase() throws SQLException {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(
                    "CREATE TABLE TEST_ENTITY (id VARCHAR(100) NOT NULL," +
                            "SHA256_ENCRYPTED_FIELD VARCHAR(256) ," +
                            "AES256_ENCRYPTED_FIELD VARCHAR(100) ," +
                            "REG_DATE DATETIME ," +
                            "NON_ENCRYPTED_FIELD VARCHAR(100) ," +
                            "INTEGER_FIELD INT ," +
                            "PRIMARY KEY (id))");
            connection.commit();
        }
    }

    @Before
    public void createMybatisConfig() {
        // Dump Data
        TestEntity testEntity = new TestEntity();
        testEntity.setId(UUID.randomUUID().toString());

        testEntity.setRegDate(new Date());
        testEntity.setIntegerField(1500);
        testEntity.setNonEncryptedField("this is encryption not required content");
        this.testEntity = testEntity;
    }

    @Test
    public void tesInsertWithEncryptionPluginUsingAESCryptogramImpl() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            TestEntityMapper testEntityMapper = sqlSession.getMapper(TestEntityMapper.class);

            // Given
            TestEntity testEntity1 = this.testEntity;
            testEntity1.setAes256EncryptedField("this is data");
            // When
            testEntityMapper.insert(testEntity1);
            // Then
        }

    }

    @Test
    public void testSelectWithEncryptionPluginUsingAES256CryptogramImpl() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            TestEntityMapper testEntityMapper = sqlSession.getMapper(TestEntityMapper.class);

            // Given
            TestEntity testEntity1 = this.testEntity;
            String data = "this is data";
            testEntity1.setAes256EncryptedField(data);
            testEntityMapper.insert(testEntity1);

            // When
            List<TestEntity> selectedEntityList = testEntityMapper.selectAll();
            // Then
            assertEquals(selectedEntityList.get(0).getAes256EncryptedField(), data);
        }
    }

    @Test
    public void tesInsertWithEncryptionPluginUsingSHACryptogramImpl() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            TestEntityMapper testEntityMapper = sqlSession.getMapper(TestEntityMapper.class);

            // Given
            TestEntity testEntity1 = this.testEntity;
            testEntity1.setSha256EncryptedField("this is data");
            // When
            testEntityMapper.insert(testEntity1);
            // Then
        }
    }

    @Test
    public void testSelectWithEncryptionPluginUsingSHA256CryptogramImpl() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            TestEntityMapper testEntityMapper = sqlSession.getMapper(TestEntityMapper.class);

            // Given
            TestEntity testEntity1 = this.testEntity;
            String data = "this is data";
            testEntity1.setSha256EncryptedField(data);
            testEntityMapper.insert(testEntity1);
            // When
            List<TestEntity> selectedEntityList = testEntityMapper.selectAll();
            // Then
            assertNotEquals(selectedEntityList.get(0).getSha256EncryptedField(), data);
        }
    }

    @Test
    public void testSelectWithNonEncryptionPlugin() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            TestEntityMapper testEntityMapper = sqlSession.getMapper(TestEntityMapper.class);

            // Given
            TestEntity testEntity1 = this.testEntity;
            testEntityMapper.insert(testEntity1);
            // When
            List<TestEntity> selectedEntityList = testEntityMapper.selectAll();
            // Then
            assertEquals(selectedEntityList.get(0).getNonEncryptedField(), testEntity1.getNonEncryptedField());
        }
    }

}
