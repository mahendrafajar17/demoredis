package com.mytechnodev.redisdemo.models.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@lombok.Data
@RedisHash("datas")
public class Data {
    @Id
    String id;
    String name;
    Integer status;
    String createdAt;
}
