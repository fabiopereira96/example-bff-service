package com.bff.example;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.bff.example");

        noClasses()
            .that()
            .resideInAnyPackage("..domain..")
            .or()
            .resideInAnyPackage("..infrastructure..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.bff.example.controller..")
            .because("Services and repositories should not depend on controller layer")
            .check(importedClasses);
    }
}
