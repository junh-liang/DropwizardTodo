package com.junhong.liang.todo.health;

import com.mongodb.MongoClient;
import com.codahale.metrics.health.HealthCheck;

public class MongoHealthCheck extends HealthCheck {

    private MongoClient mongo;

    public MongoHealthCheck(MongoClient mongo) {
        super();
        this.mongo = mongo;
    }

    @Override
    protected Result check() throws Exception {
        mongo.getDatabaseNames();
        return Result.healthy();
    }

}
