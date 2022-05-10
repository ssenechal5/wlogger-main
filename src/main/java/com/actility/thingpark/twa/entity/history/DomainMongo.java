package com.actility.thingpark.twa.entity.history;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * Domain
 */
@RegisterForReflection
public class DomainMongo {

    /**
     * Domain name
     */
    public final static String NAME = "n";
    @BsonProperty(NAME)
    public String name;

    /**
     * Domain group name
     */
    public final static String GROUP = "g";
    @BsonProperty(GROUP)
    public String group;

    public DomainMongo() {}

    public DomainMongo(String name, String group) {
        this.name = name;
        this.group = group;
    }
}

