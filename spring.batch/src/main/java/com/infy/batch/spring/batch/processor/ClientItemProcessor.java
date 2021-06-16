package com.infy.batch.spring.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.infy.batch.spring.batch.model.Client;

public class ClientItemProcessor implements ItemProcessor<Client, Client> {
    private static final Logger log = LoggerFactory.getLogger(ClientItemProcessor.class);

    @Override
    public Client process(final Client client) throws Exception {
        final String clientName = client.getClientName().toUpperCase();
  

        final Client transformedClient = new Client(clientName);

        log.info("Converting (" + client + ") into (" + transformedClient + ")");

        return transformedClient;
    }



}
