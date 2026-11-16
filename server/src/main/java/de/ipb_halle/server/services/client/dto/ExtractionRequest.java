package de.ipb_halle.server.services.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExtractionRequest(
@JsonProperty("file_name") String fileName,
    @JsonProperty("file") String file
) {}