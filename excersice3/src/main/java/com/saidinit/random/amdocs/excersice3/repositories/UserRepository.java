package com.saidinit.random.amdocs.excersice3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saidinit.random.amdocs.excersice3.domains.UserDomain;

public interface UserRepository extends JpaRepository<UserDomain, String> {

}
