package com.bff.example.infrastructure.dataprovider.metadata;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MongoEntity(collection="page")
public class PageEntity extends PanacheMongoEntityBase implements Serializable {
    private static final long serialVersionUID = -6066284992177844651L;

    @NotNull
    public Type type;

    @NotNull
    public String key;

    @NotNull
    public Status status;

    public Set<SectionEntity> sections;
    public String title;
    public String so;
    public String userId;
}
