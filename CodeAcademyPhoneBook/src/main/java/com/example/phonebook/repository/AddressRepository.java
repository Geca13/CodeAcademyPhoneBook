package com.example.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.phonebook.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

}
