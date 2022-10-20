package com.G2T7.OurGardenStory.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import com.G2T7.OurGardenStory.model.User;


@EnableScan
public interface UserRepo extends CrudRepository<User, String> {
}
