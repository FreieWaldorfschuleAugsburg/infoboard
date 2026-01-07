package de.waldorfaugsburg.infoboard.procurat;

import lombok.ToString;

@ToString
public class HttpClientException extends Exception {

    private int responseCode;
    private ClientError error;

    public HttpClientException(final Throwable cause) {
        super(cause);
    }

    public HttpClientException(final int responseCode, final ClientError error) {
        this.responseCode = responseCode;
        this.error = error;
    }
}
