package com.example.phonebook.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.phonebook.entity.Address;
import com.example.phonebook.entity.Person;
import com.example.phonebook.repository.AddressRepository;
import com.example.phonebook.repository.PersonRepository;
import com.example.phonebook.service.PhoneBookService;

@Controller
public class PhoneBookController {
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	PhoneBookService phoneBookService;
	
	
	@GetMapping("/")
	public String findPerson(Model model, @ModelAttribute("person") Person person) {
	    List<Person> people = personRepository.findAll();
	    model.addAttribute("people", people);
		return "index";
	}
	
	
	
	
	@GetMapping("/person/find")
	public String processFindPerson(Model model,@ModelAttribute("person")Person person, @Param(value = "word") String word) {
		List<Person> people = personRepository.findByWord(word);
		
		if(word == "") {
			return"empty";
		}
		
		if(people.isEmpty()) {
			return "noResultsBack";
		}
		
	    model.addAttribute("people", people);
		    return "phoneBook";
	}
	
	

	@GetMapping("/people")
	public String viewPhoneBookPage(Model model,@ModelAttribute("person")Person person ) {
		
	     findPage(1,"lastName", "asc", model);
		 
		 return "phoneBook";
	}
	
	
	@GetMapping("/pages/{pageNumber}")
	public String findPage(@PathVariable("pageNumber") Integer pageNumber,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		Integer pageSize = 3;
		
		Page<Person>pages = phoneBookService.findPagina(pageNumber, pageSize, sortField, sortDir);
		
		List<Person> people = pages.getContent();
		
		Person person = new Person();
		model.addAttribute("currentPage",pageNumber);
		model.addAttribute("totalPages", pages.getTotalPages());
		model.addAttribute("totalItems", pages.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("people", people);
		model.addAttribute("person", person);
		return "phoneBook";
		
	}
 	
	@GetMapping("/showNewPersonForm")
	public String addPersonForm(Model model) {
		Person person = new Person();
		model.addAttribute("person", person);
		return "addPerson";
		
	}
	
	@PostMapping("/savePerson")
	public String finishPersonForm(@ModelAttribute("person") Person person) {
		
		Person p = new Person();
		p.setFirstName(person.getFirstName());
		p.setLastName(person.getLastName());
		p.setPhoneNumber(person.getPhoneNumber());
		
		personRepository.save(p);
		
		return "redirect:/person/"+p.getId();
	
   }
	
	@GetMapping("/person/{id}")
	public String personDetails(Model model, @PathVariable("id") Integer id) {
		
		Person person = personRepository.findById(id).get();
		List<Address> listAddress = new ArrayList<>();
        List<Address> listAddresss = addressRepository.findAll();
        for (Address address : listAddresss) {
			
        	if(address.getPerson().getId() == person.getId()) {
        		listAddress.add(address);
        	}
		}
        
		
		model.addAttribute("listAddress", listAddress);
		model.addAttribute("person", person);
		return "personDetails";
		
	}
	
	
	
	@GetMapping("/person/{id}/addAddress")
	public String setAddress(@PathVariable ("id") Integer id,  Model model) {
		
	
		Person person = personRepository.findById(id).get();
		Address address = new Address();
        model.addAttribute("person", person);
        model.addAttribute("address", address);
		
		return "new_Address";
	}
	
	@PostMapping("/person/{id}/addAddress")
	public String processCreationForm(@PathVariable("id")Integer id,@ModelAttribute("address") Address address) {
		
		
		Person person = personRepository.findById(id).get();
		Address  address1 = new Address();
		address1.setStreet(address.getStreet());
		address1.setStreetNumber(address.getStreetNumber());
		address1.setCity(address.getCity());
		address1.setZipCode(address.getZipCode());
		address1.setCountry(address.getCountry());
		address1.setPerson(person);
		
		addressRepository.save(address1);
		
	     return "redirect:/person/"+person.getId()  ;
		
	}
	
	@GetMapping("/findByPhone")
	public String processFindProductsByOrigin(Model model,@ModelAttribute("person") Person person) {
		
		
		List<Person> people = personRepository.filterByPhoneNumber(person.getPhoneNumber());
		if (person.getPhoneNumber().equals("")) {
			//List<Person> people2 = personRepository.findAll();
		//	model.addAttribute("people", people2);
			return "empty";
			
			} else if (people.size() == 1) {
		return "redirect:/person/" + people.get(0).getId();
		
			} else if(people.isEmpty()) {
			return "noResultsBack";
		}
		
		
		model.addAttribute("people", people);
		return "phoneBook";
	}
	
	
	
}


