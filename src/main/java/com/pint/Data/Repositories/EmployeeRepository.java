package com.pint.Data.Repositories;

import com.pint.Data.Models.Employee;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
