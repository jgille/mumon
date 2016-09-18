package org.jgille.mumon.application.report.domain

import spock.lang.Specification

import java.time.Instant

class ServiceHealthTest extends Specification {

    def "Instances are sorted based on status and timestamp"() {
        given:
        def t0 = Instant.now()
        def t1 = t0.plusSeconds(1)
        def t2 = t1.plusSeconds(1)

        def i1 = ServiceInstanceHealth.builder()
                .withServiceName('s1')
                .withServiceVersion('1.0')
                .withInstanceId('i1')
                .withHostAddress('host')
                .withTimestamp(t0)
                .withStatus(Status.WARNING)
                .withHealthChecks([
                HealthCheck.builder()
                        .withName('check')
                        .withStatus(Status.WARNING)
                        .withMessage('message')
                        .build()
        ])
                .build()
        def i2 = ServiceInstanceHealth.builder()
                .withServiceName('s1')
                .withServiceVersion('1.0')
                .withInstanceId('i2')
                .withHostAddress('host')
                .withTimestamp(t1)
                .withStatus(Status.CRITICAL)
                .withHealthChecks([
                HealthCheck.builder()
                        .withName('check')
                        .withStatus(Status.CRITICAL)
                        .withMessage('message')
                        .build()
        ])
                .build()
        def i3 = ServiceInstanceHealth.builder()
                .withServiceName('s1')
                .withServiceVersion('1.0')
                .withInstanceId('i3')
                .withHostAddress('host')
                .withTimestamp(t2)
                .withStatus(Status.WARNING)
                .withHealthChecks([
                HealthCheck.builder()
                        .withName('check')
                        .withStatus(Status.WARNING)
                        .withMessage('message')
                        .build()
        ])
                .build()
        when:
        def serviceHealth = new ServiceHealth('s1',
                [
                        i1,
                        i2,
                        i3
                ])

        then:
        def instances = serviceHealth.instances()
        instances == [
                i2, i3, i1
        ]
    }
}
