package com.G2T7.OurGardenStory.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.G2T7.OurGardenStory.model.Window;

//Useless. Actually our service and repos are the same thing now.
@EnableScan
public interface WindowRepo extends CrudRepository<Window, String> {
}