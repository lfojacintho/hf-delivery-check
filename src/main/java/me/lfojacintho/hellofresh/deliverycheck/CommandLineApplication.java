package me.lfojacintho.hellofresh.deliverycheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineApplication implements CommandLineRunner {

    private final DeliveryService deliveryService;

    @Autowired
    public CommandLineApplication(final DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void run(final String... args) throws Exception {
        deliveryService.generateDeliveryList();
    }
}
