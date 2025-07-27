package com.angga.xphr.service;

import com.angga.xphr.model.Employee;
import com.angga.xphr.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Value("${app.auth.admin.username}")
    private String adminUsername;

    @Value("${app.auth.admin.password}")
    private String adminPassword;

    @Value("${app.auth.admin.role}")
    private String adminRole;

    @Value("${app.auth.employee.default-password}")
    private String defaultEmployeePassword;

    @Value("${app.auth.employee.role}")
    private String employeeRole;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserBuilder builder;

        if (adminUsername.equals(username)) {
            builder = User.builder()
                    .username(adminUsername)
                    .password("{noop}" + adminPassword)
                    .roles(adminRole);
        } else {
            Employee employee = employeeRepository.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Employee not found: " + username));

            builder = User.builder()
                    .username(employee.getName())
                    .password("{noop}" + defaultEmployeePassword)
                    .roles(employeeRole);
        }

        return builder.build();
    }
}
