package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ContactRequestRepository extends JpaRepository<ContactRequest,Long> {
}