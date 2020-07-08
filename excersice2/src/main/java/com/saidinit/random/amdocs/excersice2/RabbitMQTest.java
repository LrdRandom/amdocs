package com.saidinit.random.amdocs.excersice2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * I never used rabbitMQ (I started on Kafka recently), but according to
 * https://www.rabbitmq.com/api-guide.html on the chapter: Receiving Messages by
 * Subscription ("Push API") here are some changes, marked with code comment XXX
 * 
 * My best guess of the error is that you are not deleting the messages from the
 * queue once you acknowledge them, as seen in this SO Q&A:
 * https://stackoverflow.com/questions/48523134/settings-autoack-true-in-rabbitmq-and-celery
 */
public class RabbitMQTest {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger LOGGER = Logger.getLogger(RabbitMQTest.class.getName());

	// XXX: put the consumer tag as static, could be from a property somewhere too
	// (used for the basic cancel)
	private static final String CONSUMER_TAG = "a-consumer-tag";

	private final UserRepository userRepository = null;
	private final ApprovalRequestRepository approvalRequestRepository = null;

	public void someFunction() throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

			boolean autoAck = false;

			String queueName = "someName";
			channel.basicConsume(queueName, autoAck, CONSUMER_TAG, new DefaultConsumer(channel) {

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {

					long deliveryTag = envelope.getDeliveryTag();
					// positively acknowledge all deliveries up to
					// this delivery tag
					String message = new String(body, "UTF-8");
					try {
						// XXX: if these instructions (mapping, calling database twice, evaluating some
						// data) take some time, you might end up holding Consumers if you are using
						// multiple consumers per channel (as per documentation)
						ApprovalRequestMessage approvalRequestMessage = objectMapper.readValue(message,
								ApprovalRequestMessage.class);
						User user = userRepository.getUser(approvalRequestMessage.getUserId());
						ApprovalRequest approvalRequest = approvalRequestRepository
								.getRequest(approvalRequestMessage.getRequestId());
						// invoking rule engine to validate request
						Evaluation evaluation = evaluateApprovalRequest(user.getCIF(), approvalRequest.getBoundaries());
						// XXX: not much of an improvement, but why is status not following
						// getter/setter naming pattern?
						// if (evaluation.status().equals("GREEN")) {
						if (evaluation.getStatus().equals("GREEN")) {
							LOGGER.debug("Request approved. Request ID:" + approvalRequestMessage.getRequestId());

							// XXX: code sample says this should be false, not true but it seems (by the
							// comment at the beginning) that you need to acknowledge all deliveries. It
							// seems to me that if the queue is quite long, that will take a bunch of time.
							// Also also, documentation states that when we are using manual
							// acknowledgements and we are acknowledging for different threads it could
							// cause a double-acknowledgement and that will close the channel, better to
							// re-think this method
							channel.basicAck(deliveryTag, true);
						} else {
							LOGGER.debug("Request needs second evaluation. Request ID:"
									+ approvalRequestMessage.getRequestId());
							// XXX: not sure what is this specific case on your business logic, but I am
							// pretty sure you will need to do something with the message, un-acknowledge it
							// maybe?
						}
					} catch (Exception e) {
						LOGGER.debug("Technical issue");
					}
					// XXX: added finally, because, I mean, come on!
					finally {
						// XXX: add basic cancel
						channel.basicCancel(CONSUMER_TAG);
					}
				}

				// XXX: documentation also states that more complex consumers will need to
				// override handleShutdownSignal and handleConsumeOK
				@Override
				public void handleConsumeOk(String consumerTag) {
					// TODO: not sure what to do here
				}

				@Override
				public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
					// XXX: some code that I found on apache-camel here:
					// https://github.com/apache/camel/blob/master/components/camel-rabbitmq/src/main/java/org/apache/camel/component/rabbitmq/RabbitConsumer.java
					// seems like it is trying to reconnect to the channel, which seems a cool
					// autorecovery thing to do
					LOGGER.info("Received shutdown signal on the rabbitMQ channel");

					// Check if the consumer closed the connection or something else
					if (!sig.isInitiatedByApplication()) {
						// Something else closed the connection so reconnect
						boolean connected = false;
						while (!connected && !isStopping()) {
							try {
								reconnect();
								connected = true;
							} catch (Exception e) {
								LOGGER.warn(
										"Unable to obtain a RabbitMQ channel. Will try again. Caused by: {}. Stacktrace logged at DEBUG logging level."
												+ e.getMessage());
								// include stacktrace in DEBUG logging
								LOGGER.debug(e.getMessage(), e);
								// XXX: seems a lot of camel-specific code that I am not putting here
								// Integer networkRecoveryInterval = channel.
								// consumer.getEndpoint().getNetworkRecoveryInterval();
								Integer networkRecoveryInterval = 0;
								final long connectionRetryInterval = networkRecoveryInterval != null
										&& networkRecoveryInterval > 0 ? networkRecoveryInterval : 100L;
								try {
									Thread.sleep(connectionRetryInterval);
								} catch (InterruptedException e1) {
									Thread.currentThread().interrupt();
								}
							}
						}
					}
				}
			});
		}
	}

	private Evaluation evaluateApprovalRequest(String cif, String boundaries) {
		// TODO Auto-generated method stub
		return null;
	}

	private void reconnect() {
		// TODO Auto-generated method stub
	}

	private boolean isStopping() {
		// TODO Auto-generated method stub
		return false;
	}
}