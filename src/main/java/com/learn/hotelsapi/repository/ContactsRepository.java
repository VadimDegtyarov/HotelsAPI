package com.learn.hotelsapi.repository;

import com.learn.hotelsapi.model.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactsRepository extends JpaRepository<Contacts,Long> {
}
