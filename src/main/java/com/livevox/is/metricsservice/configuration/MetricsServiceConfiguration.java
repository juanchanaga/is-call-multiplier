package com.livevox.is.metricsservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livevox.is.api.configuration.configuration.ApiConfigurationConfig;
import com.livevox.is.configurationclient.config.ConfigurationLibConfig;
import com.livevox.is.configurationclient.services.ApplicationService;
import com.livevox.is.domain.config.enumeration.LvHeaderEnum;
import com.livevox.is.notificationservice.client.configuration.NotificationServiceClientConfiguration;
import com.livevox.is.notificationservice.client.helper.EmailHelper;
import com.livevox.is.notificationservice.client.listener.SendEmailListener;
import com.livevox.is.notificationservice.client.service.EmailService;
import com.livevox.is.signinclient.config.SignInLibConfig;
import com.livevox.is.signinclient.services.SignInClientService;
import com.livevox.is.util.configuration.LoggableConfiguration;
import com.livevox.is.util.configuration.MetricsServiceConverters;
import com.livevox.is.util.event.UnhandledExceptionEvent;
import com.livevox.is.util.grpc.exception.GlobalGrpcExceptionHandler;
import com.livevox.is.util.rest.exception.GlobalExceptionHandler;
import com.livevox.is.util.rest.filter.AuthenticationFilter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import net.devh.boot.grpc.common.autoconfigure.GrpcCommonCodecAutoConfiguration;
import net.devh.boot.grpc.common.autoconfigure.GrpcCommonTraceAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

import java.util.List;
import java.util.Optional;

@Configuration
@Import({
        GlobalExceptionHandler.class,
        ApiConfigurationConfig.class,
        ConfigurationLibConfig.class,
        SignInLibConfig.class,
        LoggableConfiguration.class,
        GlobalGrpcExceptionHandler.class,
        MetricsServiceConverters.class,
        NotificationServiceClientConfiguration.class})
@OpenAPIDefinition(info = @Info(
        title = "${springdoc.info.title}",
        version = "${springdoc.info.version}",
        description = "${springdoc.info.description}"))
@ImportAutoConfiguration({
        GrpcCommonCodecAutoConfiguration.class,
        GrpcCommonTraceAutoConfiguration.class,
        GrpcAdviceAutoConfiguration.class,
        GrpcHealthServiceAutoConfiguration.class,
        GrpcReflectionServiceAutoConfiguration.class,
        GrpcServerAutoConfiguration.class,
        GrpcServerFactoryAutoConfiguration.class,
        GrpcServerMetricAutoConfiguration.class,
        GrpcServerSecurityAutoConfiguration.class,
        GrpcServerTraceAutoConfiguration.class
})
public class MetricsServiceConfiguration {

    @Bean
    public RetryTemplate retryTemplate() {

        return new RetryTemplateBuilder()
                .maxAttempts(5)
                .fixedBackoff(3000)
                .build();
    }

    @Bean
    public EmailHelper emailHelper(
            AppProperties properties,
            ApplicationService applicationService,
            EmailService emailService,
            Environment environment) {

        return EmailHelper.builder()
                .applicationName(properties.getApplicationName())
                .applicationToken(properties.getApplicationToken())
                .subjectIdentifier("Metrics Service")
                .emailService(emailService)
                .environment(environment)
                .recipients((appName, appToken) ->
                        Optional.ofNullable(applicationService.getApplicationConfigurations(appName, appToken)
                                .getApplicationProperties().get(EmailService.EMAIL_RECIPIENTS_PROPERTY)))
                .build();
    }

    @Bean
    public ApplicationListener<UnhandledExceptionEvent> sendEmailListener(
            @Value("${com.livevox.is.notificationservice.client.baseurl}") String notificationUrl,
            EmailHelper emailHelper) {

        return SendEmailListener.builder()
                .emailHelper(emailHelper)
                .notificationUrl(notificationUrl)
                .build();
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistration(
            @Value("#{'${springdoc.excluded.path:swagger,api-docs}'.split(',')}") List<String> excludedPaths,
            @Value("${app.rest-health-path}") String healthPath,
            SignInClientService signInClientService,
            ApplicationEventPublisher publisher,
            ObjectMapper objectMapper) {

        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        excludedPaths.add(healthPath);

        AuthenticationFilter filter = AuthenticationFilter.builder()
                .excludedPaths(excludedPaths)
                .objectMapper(objectMapper)
                .publisher(publisher)
                .authenticationHeader(LvHeaderEnum.LV_AUTHORIZATION.getName())
                .tokenValidator(jws -> signInClientService.validateToken(jws).getClientId())
                .build();

        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return registrationBean;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}