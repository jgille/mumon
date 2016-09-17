package org.jgille.mumon.application.report;

import org.jgille.mumon.application.report.api.ServiceInstanceReportDto;
import org.jgille.mumon.application.report.domain.HealthCheck;
import org.jgille.mumon.application.report.domain.ServiceHealthRepository;
import org.jgille.mumon.application.report.domain.ServiceInstanceHealth;
import org.jgille.mumon.application.report.domain.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Consumes(APPLICATION_JSON)
@Path("reports")
public class ServiceInstanceReportResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ServiceHealthRepository serviceHealthRepository;

    public ServiceInstanceReportResource(ServiceHealthRepository serviceHealthRepository) {
        this.serviceHealthRepository = serviceHealthRepository;
    }

    @POST
    public void handleReport(@Valid ServiceInstanceReportDto report) {
        logger.info("Received report {}", report.id);
        serviceHealthRepository.register(
                ServiceInstanceHealth.builder()
                        .withServiceName(report.metadata.service.name)
                        .withServiceVersion(report.metadata.service.version)
                        .withInstanceId(report.metadata.instance.id)
                        .withHostAddress(report.metadata.instance.host_address)
                        .withTimestamp(report.timestamp.toInstant())
                        .withStatus(Status.valueOf(report.health.status))
                        .withHealthChecks(report.health.health_checks.stream()
                                .map(c -> HealthCheck.builder()
                                        .withName(c.name)
                                        .withStatus(Status.valueOf(c.status))
                                        .withMessage(c.message)
                                        .build())
                                .collect(Collectors.toList())
                        ).build()
        );
    }
}
