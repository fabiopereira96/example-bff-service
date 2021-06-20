package com.bff.example.infrastructure.dataprovider.metadata;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MongoEntity(collection="section")
public class SectionEntity extends PanacheMongoEntityBase implements Serializable {
    private static final long serialVersionUID = -6066284992177844651L;

    @NotNull
    public Type type;

    @NotNull
    public String key;

    @NotNull
    public Status status;

    public boolean dinamic;
    public String metadata;
}
