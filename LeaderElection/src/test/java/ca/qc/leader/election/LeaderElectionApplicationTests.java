package ca.qc.leader.election;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.qc.leader.election.AsyncEventListenerLeaderElection.Producer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LeaderElectionApplication.class)
public class LeaderElectionApplicationTests {

@Autowired
Producer producer;

@Test
void contextLoads() {
}

@Test
public void createEvent() throws InterruptedException {

    producer.create("foo");

    //producer.asynMethod();


    // A chance to see the logging messages before the JVM exists.
    Thread.sleep(2000);

}
}