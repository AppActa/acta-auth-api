package br.com.acta.common.config;

import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.boot.validation.autoconfigure.ValidationConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {
    @Bean
    public ValidationConfigurationCustomizer validationConfigurationCustomizer() {
        return configuration -> {
            if (configuration instanceof HibernateValidatorConfiguration hibernateConfiguration) {
                // permite que os controllers adicionem validações próprias não previstas nas interfaces de documentação
                hibernateConfiguration.allowOverridingMethodAlterParameterConstraint(true);
            }
        };
    }
}