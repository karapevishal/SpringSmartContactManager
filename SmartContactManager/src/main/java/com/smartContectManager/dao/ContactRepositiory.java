package com.smartContectManager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartContectManager.Entity.Contact;

public interface ContactRepositiory extends JpaRepository<Contact, Integer> {

	//pegination will be implements 
	
	/*
	 * @Query("from Contact as c where c.user.id=:userId") public List<Contact>
	 * findContactByUser(@Param("userId") int userId);
	 */
	
	//current page 
	//contact per page-5
	
	@Query("from Contact as c where c.user.id=:userId")
	public Page<Contact> findContactByUser(@Param("userId") Integer userId,Pageable pageable);
	
}
