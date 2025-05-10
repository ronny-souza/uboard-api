package br.com.uboard.configuration;

import feign.Contract;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OpenFeignConfiguration {
    private final ObjectFactory<HttpMessageConverters> messageConverters;
    private final int readTimeout;
    private final int connectionTimeout;

    public OpenFeignConfiguration(ObjectFactory<HttpMessageConverters> messageConverters,
                                  @Value("${feign-builder.client.readTimeout:10000}") int readTimeout,
                                  @Value("${feign-builder.client.connectionTimeout:10000}") int connectionTimeout) {
        this.messageConverters = messageConverters;
        this.readTimeout = readTimeout;
        this.connectionTimeout = connectionTimeout;
    }

    public Decoder feignDecoder() {
        return new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters)));
    }

    public Encoder feignEncoder() {
        return new SpringEncoder(this.messageConverters);
    }

    public Contract feignContract() {
        return new SpringMvcContract();
    }

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .options(new Request.Options(Duration.ofMillis(this.connectionTimeout), Duration.ofMillis(readTimeout), false))
                .retryer(Retryer.NEVER_RETRY)
//                .logLevel(Logger.Level.FULL)
//                .logger(new feign.slf4j.Slf4jLogger())
                .contract(feignContract())
                .decoder(feignDecoder())
                .encoder(feignEncoder());
    }
}
