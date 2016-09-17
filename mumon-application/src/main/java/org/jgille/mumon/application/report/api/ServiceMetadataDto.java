package org.jgille.mumon.application.report.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

public class ServiceMetadataDto {

    @NotBlank
    public final String name;

    @NotBlank
    public final String version;

    public ServiceMetadataDto(@JsonProperty("name") String serviceName,
                              @JsonProperty("version") String serviceVersion) {
        this.name = serviceName;
        this.version = serviceVersion;
    }
}
