package com.example.phonebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.phonebook.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
	

	
	@Query(value="select * from Person p where p.last_name like :word or p.phone_number like :word", nativeQuery = true)
	List<Person> findByWord(@Param("word") String word);
	
	@Query("SELECT DISTINCT person FROM Person person WHERE person.phoneNumber LIKE :phoneNumber%")
	@Transactional(readOnly = true)
	List<Person>filterByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
