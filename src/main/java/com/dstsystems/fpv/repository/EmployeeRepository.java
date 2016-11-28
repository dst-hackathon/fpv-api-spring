package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Employee findByCode(String code);
}
