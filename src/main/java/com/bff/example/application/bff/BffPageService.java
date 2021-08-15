package com.bff.example.application.bff;

import com.bff.example.infrastructure.mongo.metadata.PageEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.panache.common.Page;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

import static com.bff.example.infrastructure.mongo.metadata.PageEntity.*;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class BffPageService {

    public List<PageEntity> getAllPages(){
        return findAll(Page.ofSize(20));
    }

    public PageEntity getByKey(String key){
        return findByKey(key);
    }

    public void update(PageEntity pageEntity){
        findByKeyOptional(pageEntity.key)
            .ifPresent(hasEntity -> doUpdate(pageEntity));
    }

    private void doUpdate(PageEntity pageEntity) {
        PanacheMongoEntityBase.update(pageEntity);
    }
}
