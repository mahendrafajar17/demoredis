package com.mytechnodev.redisdemo.repositories;

import com.mytechnodev.redisdemo.models.redis.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends CrudRepository<Menu, String> {
}
