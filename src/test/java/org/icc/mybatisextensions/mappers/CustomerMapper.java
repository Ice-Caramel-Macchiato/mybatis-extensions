package org.icc.mybatisextensions.mappers;



import org.icc.mybatisextensions.domain.Customer;

import java.util.List;

/**
 * @author SoungRyoul Kim Thank my mentor Ikchan Sim who taught me.
 */

public interface CustomerMapper {

  void insert(Customer customer);

  List<Customer> selectAll();


}
