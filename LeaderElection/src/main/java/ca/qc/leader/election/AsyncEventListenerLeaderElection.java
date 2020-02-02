package ca.qc.leader.election;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;



@Component
@EnableAsync
class AsyncEventListenerLeaderElection {

static final Logger logger = LoggerFactory.getLogger(AsyncEventListenerLeaderElection.class);

@Bean
TaskExecutor taskExecutor() {
    return new SimpleAsyncTaskExecutor();
}


static class GrantedEvent {

    private String id;

    public GrantedEvent(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GrantedEvent{" +
                "id='" + id + '\'' +
                '}';
    }
}

@Component
static class Receiver {

    @EventListener
    void handleSync(GrantedEvent event) {
        logger.info("thread '{}' handling '{}' event", Thread.currentThread(), event);
    }

    @Async
    @EventListener
    void handleAsync(GrantedEvent event) {
        logger.info("thread '{}' handling '{}' event", Thread.currentThread(), event);
    }

}

@Component
static class Producer {

    private final ApplicationEventPublisher publisher;

    public Producer(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void create(String id) {
        publisher.publishEvent(new GrantedEvent(id));
    }

    @Async
    public void asynMethod() {
        logger.info("running async method with thread '{}'", Thread.currentThread());
    }
}

}