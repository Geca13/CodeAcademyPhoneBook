package com.example.phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.example.phonebook.entity.Person;
import com.example.phonebook.repository.PersonRepository;

@Service
public class PhoneBookService {
	
	@Autowired
	PersonRepository personRepository;
	
       public Page<Person> findPagina(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		
		return personRepository.findAll(pageable);
	}
       
       public Page<Person> findPaginated(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
   		
   		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
   			Sort.by(sortField).descending();
   		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
   		
   		return personRepository.findAll(pageable);
   	}

}
