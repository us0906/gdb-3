package de.kvb.eps;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("de.kvb.eps");

        noClasses()
            .that()
                .resideInAnyPackage("de.kvb.eps.service..")
            .or()
                .resideInAnyPackage("de.kvb.eps.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..de.kvb.eps.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
