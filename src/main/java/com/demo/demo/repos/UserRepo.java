package com.demo.demo.repos;

import com.demo.demo.entities.UserClient;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepo extends CrudRepository<UserClient, UUID> {

}
