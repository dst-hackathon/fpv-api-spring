package com.dst.hackathon.fpv.data.repository;

import com.dst.hackathon.fpv.data.model.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by Seph on 21/10/2559.
 */

@RepositoryRestResource()
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long>{
    List<Employee> findByNickName(String nickName);
}
