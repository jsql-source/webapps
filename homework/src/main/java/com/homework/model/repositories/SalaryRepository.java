package com.homework.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.homework.model.entities.ESalary;

public interface SalaryRepository extends JpaRepository<ESalary, Integer> {
	
}

