package de.ipb_halle.server.services.exceptions;

/**
 * Exception thrown when downstream consumption service fails after retries.
 */
public class DownstreamConsumptionException extends RuntimeException {

    public DownstreamConsumptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DownstreamConsumptionException(String message) {
        super(message);
    }
}
