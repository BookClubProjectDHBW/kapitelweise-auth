package com.kapitelweise.auth.kapitelweiseAuth.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.kapitelweise.auth.kapitelweiseAuth.response.EventType;
import com.kapitelweise.auth.kapitelweiseAuth.response.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

@Service
public class AmazonSQSService {

    private static final Logger log = LoggerFactory.getLogger(AmazonSQSService.class);

    private final AmazonSQS sqs;
    private final JsonMapper jsonMapper;
    private static final String QUEUE_URL = "https://sqs.eu-north-1.amazonaws.com/608531435564/userDetails.fifo";

    public AmazonSQSService(AmazonSQS sqs, JsonMapper jsonMapper) {
        this.sqs = sqs;
        this.jsonMapper = jsonMapper;
    }

    public void sendNewUser(EventType type, Long userId, String username){

        try {
            String body = jsonMapper.writeValueAsString(new UserEvent(type, userId, username));
            SendMessageRequest send = new SendMessageRequest()
                    .withQueueUrl(QUEUE_URL)
                    .withMessageBody(body)
                    .withMessageGroupId(String.valueOf(userId))
                    .withMessageDeduplicationId(type + "-" + userId);
            sqs.sendMessage(send);
            log.info(username + " ist in der Queue, die anderen Services wissen gleich Bescheid");
        } catch (Exception e) {
            // Registrierung soll nicht scheitern, nur weil die Queue nicht erreichbar ist
            log.error("UserEvent für " + username + " konnte nicht gesendet werden, die anderen Services kennen ihn jetzt nicht: " + e.getMessage());
        }
    }
}
