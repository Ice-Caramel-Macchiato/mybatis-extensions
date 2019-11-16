package com.icc.mybatisextensions.testentity;



import java.util.List;

/**
 * @author SoungRyoul Kim Thank my mentor Ikchan Sim who taught me.
 */

public interface TestEntityMapper {

  void insert(TestEntity testEntity);

  List<TestEntity> selectAll();


}
