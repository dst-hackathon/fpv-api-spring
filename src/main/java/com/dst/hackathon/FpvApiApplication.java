package com.dst.hackathon;

import com.dst.hackathon.fpv.data.model.Employee;
import com.dst.hackathon.fpv.data.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FpvApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FpvApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataSetup(EmployeeRepository employeeRepository) {
        return (args) -> {
            Employee employee = new Employee();
            employee.setEmployeeId("dt12345");
            employee.setFirstName("John");
            employee.setLastName("Doe");
            employee.setNickName("Done");

            employeeRepository.save(employee);
        };
    }
}
