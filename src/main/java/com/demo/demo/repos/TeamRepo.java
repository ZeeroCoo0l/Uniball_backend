package com.demo.demo.repos;

import com.demo.demo.entities.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepo extends CrudRepository<Team, Long> {
    boolean existsByName(String name);
}
