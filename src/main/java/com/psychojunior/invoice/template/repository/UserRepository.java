package com.psychojunior.invoice.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.psychojunior.invoice.template.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);
}
