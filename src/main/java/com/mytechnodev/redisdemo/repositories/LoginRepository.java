package com.mytechnodev.redisdemo.repositories;

import com.mytechnodev.redisdemo.models.redis.Login;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends CrudRepository<Login, String> {
}
