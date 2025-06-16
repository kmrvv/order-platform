package com.example.platform.config;

import com.example.platform.exception.CustomSoapFaultExceptionResolver;
import com.example.platform.exception.ProductNotFoundException;
import com.example.platform.exception.ProductValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import java.util.Properties;

@Configuration
public class SoapExceptionConfig {
    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
        SoapFaultMappingExceptionResolver resolver = new CustomSoapFaultExceptionResolver();

        Properties errorMappings = new Properties();
        errorMappings.setProperty(ProductNotFoundException.class.getName(), "CLIENT");
        errorMappings.setProperty(ProductValidationException.class.getName(), "CLIENT");
        errorMappings.setProperty(jakarta.validation.ConstraintViolationException.class.getName(), "CLIENT");

        resolver.setExceptionMappings(errorMappings);
        resolver.setOrder(1);
        return resolver;
    }
}
