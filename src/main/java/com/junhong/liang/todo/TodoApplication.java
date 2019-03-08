package com.junhong.liang.todo;

import com.codahale.metrics.health.HealthCheck;
import com.junhong.liang.todo.api.TodoServiceInMemoryImpl;
import com.junhong.liang.todo.api.TodoServiceMongoImpl;
import com.junhong.liang.todo.core.MongoManaged;
import com.junhong.liang.todo.health.DummyHealthCheck;
import com.junhong.liang.todo.health.MongoHealthCheck;
import com.junhong.liang.todo.resources.TodoResource;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.net.UnknownHostException;

public class TodoApplication extends Application<TodoConfiguration> {
    public static void main(String[] args) throws Exception {
        new TodoApplication().run(args);
    }

    @Override
    public String getName() {
        return "todo-app";
    }

    @Override
    public void initialize(Bootstrap<TodoConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(TodoConfiguration configuration,
                    Environment environment) {

        if(configuration.storage.equals("memory")) {

            // Use in memory store as backend
            System.out.println("==== Using in memory storage as backend storage ====");
            environment.healthChecks().register("dummy", new DummyHealthCheck());
            environment.jersey().register(new TodoResource(new TodoServiceInMemoryImpl()));

        } else if(configuration.storage.equals("mongodb")) {

            // Use mongodb as backend
            System.out.println("==== Using in MongoDB as backend storage ====");

            //Create Mongo instance
            MongoClient mongo = null;
            try {
                mongo = new MongoClient(configuration.mongohost, configuration.mongoport);
            } catch (UnknownHostException e) {
                System.err.format("Error while trying to connect to MongoDB in %s:%s, shutting down application...",
                        configuration.mongohost, configuration.mongoport).println();
                System.exit(1);
            }
            //Add Managed for managing the Mongo instance
            MongoManaged mongoManaged = new MongoManaged(mongo);
            environment.lifecycle().manage(mongoManaged);
            //Add Health check for Mongo instance. This will be used from the Health check admin page
            environment.healthChecks().register("mongo", new MongoHealthCheck(mongo));
            //Create DB instance and wrap it in a Jackson DB collection
            DB db = mongo.getDB(configuration.mongodb);
            final TodoResource todoResource = new TodoResource(new TodoServiceMongoImpl(db));
            final HealthCheck healthCheck = new MongoHealthCheck(mongo);
            environment.healthChecks().register("mongodb", healthCheck);
            environment.jersey().register(todoResource);

        } else {

            System.err.println("Unknown storage backend \"" + configuration.storage + "\", " +
                    "only support \"memory\" and \"mongodb\". shutting down application...");
            System.exit(1);

        }

    }

}