package com.bff.example.infrastructure.mongo.metadata;

import com.bff.example.infrastructure.mongo.metadata.PageEntity.SectionKey;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MongoEntity(collection="bff_section")
public class SectionEntity extends PanacheMongoEntityBase implements Serializable {
    private static final long serialVersionUID = -6066284992177844651L;

    @BsonId
    public ObjectId id;

    @NotNull
    public Type type;

    @NotNull
    public String key;

    @NotNull
    public Status status;

    public Boolean dinamic;
    public String metadata;

    public static List<SectionEntity> findByListKeys(List<SectionKey> sectionKeys) {
        var keys = sectionKeys.stream().map(SectionKey::getKey).collect(Collectors.toList());
        return find("{'key':{$in: [?1]}}", keys).list();
    }
}
