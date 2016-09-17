package org.jgille.mumon.application.report.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

public class InstanceMetadataDto {

    @NotBlank
    public final String id;

    @NotBlank
    public final String host_address;

    public InstanceMetadataDto(@JsonProperty("id") String instanceId,
                               @JsonProperty("host_address") String hostAddress) {
        this.id = instanceId;
        this.host_address = hostAddress;
    }
}
