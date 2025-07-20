package strategy;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import strategy.impl.DigitalOrderResponseStrategy;
import strategy.impl.POSOrderResponseStrategy;

@ApplicationScoped
public class ContextStrategies {

    @Inject
    DigitalOrderResponseStrategy digitalStrategy;

    @Inject
    POSOrderResponseStrategy posStrategy;


    public OrderResponseStrategy getStrategyOrderResponse(String channel) {

        return switch (channel.toUpperCase()) {
            case "DIGITAL" -> digitalStrategy;
            case "POS" -> posStrategy;
            default -> throw new IllegalArgumentException("Invalid channel");
        };
    }
}
