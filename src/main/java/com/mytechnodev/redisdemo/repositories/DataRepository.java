package com.mytechnodev.redisdemo.repositories;

import com.mytechnodev.redisdemo.models.redis.Data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends CrudRepository<Data, String> {
    List<Data> findAll();
}
