package com.bff.example.configuration;

import com.github.cloudyrock.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import com.github.cloudyrock.standalone.MongockStandalone;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class MongockConfiguration {

  @ConfigProperty(name = "quarkus.mongodb.database")
  String databaseName;

  void onStart(@Observes StartupEvent ev) {
    MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder().build());
    MongockStandalone
      .builder()
      .setDriver(MongoSync4Driver.withDefaultLock(mongoClient, databaseName))
      .addChangeLogsScanPackage("com.bff.example.configuration.dbmigrations")
      .buildRunner()
      .execute();
  }
}
