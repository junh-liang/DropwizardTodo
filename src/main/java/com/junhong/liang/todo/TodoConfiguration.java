package com.junhong.liang.todo;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class TodoConfiguration extends Configuration {

    @JsonProperty
    @NotEmpty
    public String storage;

    @JsonProperty
    @NotEmpty
    public String mongohost;

    @JsonProperty
    public int mongoport = 27017;

    @JsonProperty
    @NotEmpty
    public String mongodb = "mongoDB";

}