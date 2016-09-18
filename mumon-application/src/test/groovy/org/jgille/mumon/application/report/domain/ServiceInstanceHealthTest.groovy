package org.jgille.mumon.application.report.domain

import spock.lang.Specification

import java.time.Instant

class ServiceInstanceHealthTest extends Specification {

    def "A ServiceInstanceHealth instance can be built with a builder pattern"() {
        given:
        def now = Instant.now();

        when:
        def instanceHealth =
                ServiceInstanceHealth.builder()
                        .withServiceName('name')
                        .withServiceVersion('1.0')
                        .withInstanceId('i1')
                        .withHostAddress('localhost')
                        .withStatus(Status.WARNING)
                        .withTimestamp(now)
                        .withHealthChecks(
                        [
                                HealthCheck.builder()
                                        .withName('theCheck')
                                        .withStatus(Status.WARNING)
                                        .withMessage('Something')
                                        .build()
                        ]
                ).build()

        then:
        instanceHealth.serviceName() == 'name'

        and:
        instanceHealth.serviceVersion() == '1.0'

        and:
        instanceHealth.instanceId() == 'i1'

        and:
        instanceHealth.hostAddress() == 'localhost'

        and:
        instanceHealth.status() == Status.WARNING

        and:
        instanceHealth.timestamp() == now

        and:
        instanceHealth.healthChecks().size() == 1

        and:
        def healthCheck = instanceHealth.healthChecks()[0]
        healthCheck.name() == 'theCheck'

        and:
        healthCheck.status() == Status.WARNING

        and:
        healthCheck.message() == 'Something'

    }
}
