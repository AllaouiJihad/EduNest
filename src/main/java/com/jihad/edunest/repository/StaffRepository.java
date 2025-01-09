package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface StaffRepository extends JpaRepository<Staff,Long> {
}
