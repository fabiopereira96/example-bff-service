package com.bff.example.configuration.dbmigrations;

import com.bff.example.configuration.util.FileHelper;
import com.bff.example.constants.AuthoritiesConstants;
import com.bff.example.infrastructure.mongo.authority.Authority;
import com.bff.example.infrastructure.mongo.metadata.PageEntity;
import com.bff.example.infrastructure.mongo.metadata.SectionEntity;
import com.bff.example.infrastructure.mongo.user.UserEntity;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.time.Instant;
import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Creates the initial database setup.
 */
@ChangeLog(order = "001")
public class InitialSetupMigration {

  @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
  public void addAuthorities(MongoDatabase db) {
    Authority adminAuthority = new Authority(AuthoritiesConstants.ADMIN);
    Authority userAuthority = new Authority(AuthoritiesConstants.USER);

    db.createCollection("jhi_authority");
    db
      .getCollection("jhi_authority", Authority.class)
      .withCodecRegistry(getCodecRegistry())
      .insertMany(Arrays.asList(adminAuthority, userAuthority));
  }

  @ChangeSet(order = "02", author = "initiator", id = "02-addUsers")
  public void addUsers(MongoDatabase db) {
    var adminAuthority = new Authority(AuthoritiesConstants.ADMIN);
    var userAuthority = new Authority(AuthoritiesConstants.USER);

    var anonymousUserEntity = new UserEntity();
    anonymousUserEntity.id = "user-1";
    anonymousUserEntity.login = "anonymoususer";
    anonymousUserEntity.password = "$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO";
    anonymousUserEntity.firstName = "Anonymous";
    anonymousUserEntity.lastName = "UserEntity";
    anonymousUserEntity.email = "anonymous@localhost";
    anonymousUserEntity.activated = true;
    anonymousUserEntity.langKey = "en";
    anonymousUserEntity.createdDate = Instant.now();

    var adminUserEntity = new UserEntity();
    adminUserEntity.id = "user-2";
    adminUserEntity.login = "admin";
    adminUserEntity.password = "$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC";
    adminUserEntity.firstName = "admin";
    adminUserEntity.lastName = "Administrator";
    adminUserEntity.email = "admin@localhost";
    adminUserEntity.activated = true;
    adminUserEntity.langKey = "en";
    adminUserEntity.createdDate = Instant.now();
    adminUserEntity.authorities.add(adminAuthority);
    adminUserEntity.authorities.add(userAuthority);

    var userUserEntity = new UserEntity();
    userUserEntity.id = "user-3";
    userUserEntity.login = "user";
    userUserEntity.password = "$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K";
    userUserEntity.firstName = "";
    userUserEntity.lastName = "UserEntity";
    userUserEntity.email = "user@localhost";
    userUserEntity.activated = true;
    userUserEntity.langKey = "en";
    userUserEntity.createdDate = Instant.now();
    userUserEntity.authorities.add(userAuthority);

    db.createCollection("jhi_user");
    db
      .getCollection("jhi_user", UserEntity.class)
      .withCodecRegistry(getCodecRegistry())
      .insertMany(Arrays.asList(adminUserEntity, anonymousUserEntity, userUserEntity));
  }

  @ChangeSet(order = "03", author = "initiator", id = "03-addPagesAndSections")
  public void addPagesAndSections(MongoDatabase db){
      var fileHelper = new FileHelper();

      var homePageHeadSection = fileHelper
          .readSectionByResource("templates-bff/home-page-head-section.json");
      var productCategories = fileHelper
          .readSectionByResource("templates-bff/product-categories-section.json");
      var productPopulars = fileHelper
          .readSectionByResource("templates-bff/product-populars-section.json");
      var homePage = fileHelper
          .readPageByResource("templates-bff/home-page.json");

      db.createCollection("bff_page");
      db.getCollection("bff_page", PageEntity.class)
          .withCodecRegistry(getCodecRegistry())
          .insertOne(homePage);

      db.createCollection("bff_section");
      db.getCollection("bff_section", SectionEntity.class)
          .withCodecRegistry(getCodecRegistry())
          .insertMany(Arrays.asList(homePageHeadSection, productCategories, productPopulars));
  }

  private CodecRegistry getCodecRegistry() {
    CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    return fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
  }
}
