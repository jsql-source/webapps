package com.homework.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.model.entities.EPerson;

public interface PersonRepository extends JpaRepository<EPerson, Integer> {
	
	@Query("select p from EPerson p where p.name = :p_name")
	EPerson findByName(@Param("p_name") String name);
	
}



