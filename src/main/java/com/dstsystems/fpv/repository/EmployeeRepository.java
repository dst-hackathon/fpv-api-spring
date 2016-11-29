package com.dstsystems.fpv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dstsystems.fpv.domain.Employee;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Employee findByCode(String code);

    @Query("select e from Employee e where UPPER(e.firstname) like UPPER(:nameQuery) || '%' or UPPER(e.lastname) like UPPER(:nameQuery) || '%' ")
    List<Employee> search(@Param("nameQuery") String nameQuery);
}
