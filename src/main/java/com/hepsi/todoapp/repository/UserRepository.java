package com.hepsi.todoapp.repository;

import com.hepsi.todoapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmailAddress(String emailAddress);

    Boolean existsByEmailAddress(String emailAddress);

    List<User> findByLockedAndIsDeleted(Boolean lockedValue, Boolean deletedValue);
}
