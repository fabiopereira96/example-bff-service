package com.bff.example.infrastructure.mongo.metadata;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.panache.common.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.MAX_VALUE;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MongoEntity(collection="bff_page")
public class PageEntity extends PanacheMongoEntityBase implements Serializable {
    private static final long serialVersionUID = -6066284992177844651L;

    @BsonId
    public ObjectId id;

    @NotNull
    public Type type;

    @NotNull
    public String key;

    @NotNull
    public Status status;

    public List<SectionKey> sections;
    public String title;
    public String so;
    public String userId;

    public static Optional<PageEntity> findByKeyOptional(String key) {
        return find("key", key).firstResultOptional();
    }

    public static PageEntity findByKey(String key) {
        return find("key", key).firstResult();
    }

    public static List<PageEntity> findAll(Page page){
        return findAll().page(page).list();
    }

    public int getSectionOrderByKey(String key){
        return this.sections.stream().filter(sectionKey -> key.equals(sectionKey.getKey()))
            .findFirst().map(SectionKey::getOrder)
            .orElse(MAX_VALUE);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SectionKey {
        protected String key;
        protected int order;
    }
}
