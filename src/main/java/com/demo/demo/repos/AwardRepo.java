package com.demo.demo.repos;

import com.demo.demo.entities.Award;
import org.springframework.data.repository.CrudRepository;

public interface AwardRepo extends CrudRepository<Award, Integer> {
}
