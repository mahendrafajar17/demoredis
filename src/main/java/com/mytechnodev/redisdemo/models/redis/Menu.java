package com.mytechnodev.redisdemo.models.redis;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("menus")
public class Menu {
    @Id
    String id;
    String responseData;
}
