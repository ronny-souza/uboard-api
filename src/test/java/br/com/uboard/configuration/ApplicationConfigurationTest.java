package br.com.uboard.configuration;

import br.com.uboard.common.CustomObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationConfigurationTest {

    @Test
    @DisplayName("Should return an instance of custom object mapper")
    void shouldReturnAnInstanceOfCustomObjectMapper() {
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        CustomObjectMapper customObjectMapper = applicationConfiguration.customObjectMapper();

        assertNotNull(customObjectMapper);
    }
}