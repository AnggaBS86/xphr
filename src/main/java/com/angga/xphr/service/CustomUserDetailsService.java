package com.angga.xphr.service;

import com.angga.xphr.model.Employee;
import com.angga.xphr.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // The harcoded for admin --> it's just for fun :))
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password("{noop}admin123")
                    .roles("ADMIN")
                    .build();
        }

        Employee employee = employeeRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found: " + username));

        return User.builder()
                .username(employee.getName())
                .password("{noop}employee123")
                .roles("EMPLOYEE")
                .build();
    }
}