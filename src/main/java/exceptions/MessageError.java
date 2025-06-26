package exceptions;

import jakarta.ws.rs.core.Response;

public class MessageError {
    private String timestamp;
    private Response.Status status;
    private String error;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    public MessageError(String timestamp, Response.Status status, String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }
}
