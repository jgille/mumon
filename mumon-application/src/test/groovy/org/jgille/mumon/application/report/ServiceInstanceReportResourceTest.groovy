package org.jgille.mumon.application.report

import org.jgille.mumon.application.report.api.*
import org.jgille.mumon.application.report.domain.HealthCheck
import org.jgille.mumon.application.report.domain.ServiceInstanceHealth
import org.jgille.mumon.application.report.domain.Status
import spock.lang.Specification

import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

class ServiceInstanceReportResourceTest extends Specification {

    def "A report is registered in the repository"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(1), Duration.ofSeconds(1))
        def resource = new ServiceInstanceReportResource(repository)
        def now = Instant.now().atOffset(ZoneOffset.UTC)

        when:
        resource.handleReport(
                new ServiceInstanceReportDto('id', now,
                        new ServiceInstanceMetadataDto(new ServiceMetadataDto('s1', '1.0'),
                                new InstanceMetadataDto('i1', 'host'), [:]),
                        new ServiceInstanceHealthDto('WARNING',
                                Collections.singletonList(new HealthCheckResultDto('check', 'WARNING', null, 'message',
                                        null, null, null, null)))
                )
        )

        then:
        def serviceHealth = repository.currentServiceHealth('s1')
        serviceHealth.instances() == [
                ServiceInstanceHealth.builder()
                .withServiceName('s1')
                .withServiceVersion('1.0')
                .withInstanceId('i1')
                .withHostAddress('host')
                .withTimestamp(now.toInstant())
                .withStatus(Status.WARNING)
                .withHealthChecks([
                        HealthCheck.builder()
                        .withName('check')
                        .withStatus(Status.WARNING)
                        .withMessage('message')
                        .build()
                ])
                .build()
        ]
    }
}
