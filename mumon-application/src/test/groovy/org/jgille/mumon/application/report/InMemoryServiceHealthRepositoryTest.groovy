package org.jgille.mumon.application.report

import org.jgille.mumon.application.report.domain.HealthCheck
import org.jgille.mumon.application.report.domain.ServiceInstanceHealth
import org.jgille.mumon.application.report.domain.Status
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.Duration
import java.time.Instant

class InMemoryServiceHealthRepositoryTest extends Specification {

    def "There are no services initially"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(10), Duration.ofSeconds(10))

        when:
        def systemHealth = repository.currentSystemHealth()

        then:
        systemHealth == []
    }

    def "An unknown service has an empty list of instances"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(10), Duration.ofSeconds(10))

        when:
        def serviceHealth = repository.currentServiceHealth('some_name')

        then:
        serviceHealth.serviceName() == 'some_name'

        and:
        serviceHealth.instances() == []
    }

    def "A service instance health report can be registered"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(10), Duration.ofSeconds(10))
        def now = Instant.now()

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

        repository.register(instanceHealth)

        then:
        def systemHealth = repository.currentSystemHealth()
        systemHealth.size() == 1

        and:
        def serviceHealth = systemHealth[0]
        serviceHealth.serviceName() == 'name'

        and:
        serviceHealth.instances() == [instanceHealth]

        and:
        repository.currentServiceHealth('name') == serviceHealth
    }

    def "Only the most recent service instance health report is used"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(10), Duration.ofSeconds(10))
        def now = Instant.now()
        def later = Instant.now().plusSeconds(1)

        when:
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

        and:
        def instanceHealthTwo =
                ServiceInstanceHealth.builder()
                        .withServiceName('name')
                        .withServiceVersion('1.0')
                        .withInstanceId('i1')
                        .withHostAddress('localhost')
                        .withStatus(Status.HEALTHY)
                        .withTimestamp(later)
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

        then:
        def systemHealth = repository.currentSystemHealth()
        systemHealth.size() == 1

        and:
        def serviceHealth = systemHealth[0]
        serviceHealth.serviceName() == 'name'

        and:
        serviceHealth.instances() == [instanceHealthTwo]

        and:
        repository.currentServiceHealth('name') == serviceHealth
    }

    def "Service instance health reports for multiple services can be registered"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(10), Duration.ofSeconds(10))
        def now = Instant.now()
        def later = Instant.now().plusSeconds(1)

        when:
        def instanceHealthOne =
                ServiceInstanceHealth.builder()
                        .withServiceName('s1')
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

        and:
        def instanceHealthTwo =
                ServiceInstanceHealth.builder()
                        .withServiceName('s2')
                        .withServiceVersion('1.0')
                        .withInstanceId('i1')
                        .withHostAddress('localhost')
                        .withStatus(Status.HEALTHY)
                        .withTimestamp(later)
                        .withHealthChecks(
                        [
                                HealthCheck.builder()
                                        .withName('someCheck')
                                        .withStatus(Status.WARNING)
                                        .withMessage('Something')
                                        .build()
                        ]
                ).build()

        repository.register(instanceHealthTwo)

        then:
        def systemHealth = repository.currentSystemHealth()
        systemHealth.size() == 2

        and:
        def serviceHealthOne = systemHealth[0]
        serviceHealthOne.serviceName() == 's1'

        and:
        serviceHealthOne.instances() == [instanceHealthOne]

        and:
        repository.currentServiceHealth('s1') == serviceHealthOne

        and:
        def serviceHealthTwo = systemHealth[1]
        serviceHealthTwo.serviceName() == 's2'

        and:
        serviceHealthTwo.instances() == [instanceHealthTwo]

        and:
        repository.currentServiceHealth('s2') == serviceHealthTwo
    }

    def "A report expires after a given cool down period"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(10), Duration.ofSeconds(1))
        def now = Instant.now()

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

        repository.register(instanceHealth)

        then:
        def conditions = new PollingConditions(timeout: 2, initialDelay: 1)
        conditions.eventually {
            repository.currentServiceHealth('name').instances() == []
        }

    }

    def "A service expires after a given cool down period"() {
        given:
        def repository = new InMemoryServiceHealthRepository(Duration.ofSeconds(1), Duration.ofSeconds(1))
        def now = Instant.now()

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

        repository.register(instanceHealth)

        then:
        def conditions = new PollingConditions(timeout: 2, initialDelay: 1)
        conditions.eventually {
            repository.currentSystemHealth() == []
        }

    }
}
