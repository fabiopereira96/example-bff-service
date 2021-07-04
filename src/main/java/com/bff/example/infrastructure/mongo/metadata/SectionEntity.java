package com.bff.example.infrastructure.mongo.metadata;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MongoEntity(collection="bff_section")
public class SectionEntity extends PanacheMongoEntityBase implements Serializable {
    private static final long serialVersionUID = -6066284992177844651L;

    @NotNull
    public Type type;

    @NotNull
    public String key;

    @NotNull
    public Status status;

    public Boolean dinamic;
    public String metadata;

    public static List<SectionEntity> findByListIds(Set<String> ids) {
        return find("{'key':{$in: [?1]}}", ids).list();
    }
}
