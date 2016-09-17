package org.jgille.mumon.application.bootstrap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jgille.mumon.application.report.InMemoryServiceHealthRepository;
import org.jgille.mumon.application.report.ServiceInstanceReportResource;
import org.jgille.mumon.application.report.domain.ServiceHealthRepository;
import org.jgille.mumon.application.services.ServiceResource;

import java.time.Duration;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class MuMonApplication extends Application<MuMonConfiguration> {

    public void initialize(Bootstrap<MuMonConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
    }

    @Override
    public void run(MuMonConfiguration configuration, Environment environment) throws Exception {
        ObjectMapper objectMapper = environment.getObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // TODO: Get cool down periods from config
        ServiceHealthRepository serviceHealthRepository =
                new InMemoryServiceHealthRepository(Duration.ofMinutes(30), Duration.ofMinutes(2));
        ServiceInstanceReportResource reportResource = new ServiceInstanceReportResource(serviceHealthRepository);
        environment.jersey().register(reportResource);

        ServiceResource serviceResource = new ServiceResource(serviceHealthRepository);
        environment.jersey().register(serviceResource);
    }

    public static void main(String[] args) throws Exception {
        new MuMonApplication().run(args);
    }
}
