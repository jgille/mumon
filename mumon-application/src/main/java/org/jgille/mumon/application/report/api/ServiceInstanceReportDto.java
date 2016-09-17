package org.jgille.mumon.application.report.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public class ServiceInstanceReportDto {
    @NotBlank
    public final String id;

    @NotNull
    public final OffsetDateTime timestamp;

    @NotNull
    @Valid
    public final ServiceInstanceMetadataDto metadata;

    @NotNull
    @Valid
    public final ServiceInstanceHealthDto health;

    public ServiceInstanceReportDto(@JsonProperty("id") String id,
                                    @JsonProperty("timestamp") OffsetDateTime timestamp,
                                    @JsonProperty("metadata") ServiceInstanceMetadataDto metadata,
                                    @JsonProperty("health") ServiceInstanceHealthDto health) {
        this.id = id;
        this.timestamp = timestamp;
        this.metadata = metadata;
        this.health = health;
    }
}
