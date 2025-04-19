package br.com.uboard.configuration;

import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenFeignConfigurationTest {

    private OpenFeignConfiguration openFeignConfiguration;

    @Mock
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @BeforeEach
    void init() {
        this.openFeignConfiguration = new OpenFeignConfiguration(
                this.messageConverters, 3000, 3000
        );
    }

    @Test
    @DisplayName("Should return Open Feign Decoder")
    void shouldReturnOpenFeignDecoder() {
        Decoder decoder = this.openFeignConfiguration.feignDecoder();
        assertNotNull(decoder);
    }

    @Test
    @DisplayName("Should return Open Feign Encoder")
    void shouldReturnOpenFeignEncoder() {
        Encoder encoder = this.openFeignConfiguration.feignEncoder();
        assertNotNull(encoder);
    }


    @Test
    @DisplayName("Should return Open Feign Contract")
    void shouldReturnOpenFeignContract() {
        Contract contract = this.openFeignConfiguration.feignContract();
        assertNotNull(contract);
    }

    @Test
    @DisplayName("Should return Open Feign Builder")
    void shouldReturnOpenFeignBuilder() {
        Feign.Builder builder = this.openFeignConfiguration.feignBuilder();
        assertNotNull(builder);
    }
}