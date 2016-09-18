package org.jgille.mumon.application.services

import org.jgille.mumon.application.report.InMemoryServiceHealthRepository
import org.jgille.mumon.application.report.domain.HealthCheck
import org.jgille.mumon.application.report.domain.ServiceHealth
import org.jgille.mumon.application.report.domain.ServiceInstanceHealth
import org.jgille.mumon.application.report.domain.Status
import spock.lang.Specification

import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

class ServiceResourceTest extends Specification {

    def "I can get system health"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(10), Duration.ofSeconds(10))
        def now = Instant.now()
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

        repository.register(instanceHealth)

        def resource = new ServiceResource(repository)

        when:
        def systemHealth = resource.getSystemHealth()

        then:
        //noinspection GrEqualsBetweenInconvertibleTypes
        systemHealth == [services:
                                 [
                                         [
                                                 name  : 'name',
                                                 status: 'WARNING'
                                         ]
                                 ]
        ]
    }

    def "I can get service health"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(10), Duration.ofSeconds(10))
        def now = Instant.now()
        def instanceHealthOne =
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

        repository.register(instanceHealthOne)

        def instanceHealthTwo =
                ServiceInstanceHealth.builder()
                        .withServiceName('name')
                        .withServiceVersion('1.0')
                        .withInstanceId('i2')
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

        repository.register(instanceHealthTwo)

        def resource = new ServiceResource(repository)

        when:
        def serviceHealth = resource.getServiceHealth('name')

        then:
        //noinspection GrEqualsBetweenInconvertibleTypes
        serviceHealth == [
                name     : 'name',
                status   : 'WARNING',
                instances: [
                        [id             : 'i1',
                         status         : 'WARNING',
                         timestamp      : now.atOffset(ZoneOffset.UTC),
                         service_version: '1.0',
                         host_address   : 'localhost',
                         health_checks  : [
                                 [name   : 'theCheck',
                                  status : 'WARNING',
                                  message: 'Something'
                                 ]
                         ]
                        ],
                        [id             : 'i2',
                         status         : 'WARNING',
                         timestamp      : now.atOffset(ZoneOffset.UTC),
                         service_version: '1.0',
                         host_address   : 'localhost',
                         health_checks  : [
                                 [name   : 'theCheck',
                                  status : 'WARNING',
                                  message: 'Something'
                                 ]
                         ]
                        ]
                ]
        ]
    }

    def "Service status is DOWN when there are no instances"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(10), Duration.ofSeconds(10))
        def resource = new ServiceResource(repository)

        when:
        def status = resource.computeServiceStatus(new ServiceHealth('name', []))

        then:
        status == 'DOWN'
    }

    def "Service status is the most unhealthy instance status"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(10), Duration.ofSeconds(10))
        def resource = new ServiceResource(repository)

        when:
        def status = resource.computeServiceStatus(new ServiceHealth('name', [
                ServiceInstanceHealth.builder()
                        .withServiceName('name')
                        .withServiceVersion('1.0')
                        .withInstanceId('i2')
                        .withHostAddress('localhost')
                        .withStatus(Status.WARNING)
                        .withTimestamp(Instant.now())
                        .withHealthChecks(
                        [
                                HealthCheck.builder()
                                        .withName('theCheck')
                                        .withStatus(Status.WARNING)
                                        .withMessage('Something')
                                        .build()
                        ]
                ).build(),
                ServiceInstanceHealth.builder()
                        .withServiceName('name')
                        .withServiceVersion('1.0')
                        .withInstanceId('i2')
                        .withHostAddress('localhost')
                        .withStatus(Status.CRITICAL)
                        .withTimestamp(Instant.now())
                        .withHealthChecks(
                        [
                                HealthCheck.builder()
                                        .withName('theCheck')
                                        .withStatus(Status.CRITICAL)
                                        .withMessage('Something')
                                        .build()
                        ]
                ).build()
        ]))

        then:
        status == 'CRITICAL'
    }
}
