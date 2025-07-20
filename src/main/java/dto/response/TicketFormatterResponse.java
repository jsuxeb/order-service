package dto.response;

import dto.OrderResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TicketFormatterResponse extends OrderResponse {

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return response;
    }
}
