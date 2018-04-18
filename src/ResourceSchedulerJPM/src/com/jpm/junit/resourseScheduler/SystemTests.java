package ResourceSchedulerJPM.src.com.jpm.junit.resourseScheduler;

import ResourceSchedulerJPM.src.com.jpm.resourceScheduler.*;
import ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces.StrategyChooserInterface;
import org.junit.Test;

public class SystemTests {
    private final int NUMBER_OF_RESOURCE_HANDLERS = 1;

    @Test
    public void testReceiving() {
        Gateway gateway = new Gateway();
        ResourcePool resourcePool = new ResourcePool(NUMBER_OF_RESOURCE_HANDLERS);
        StrategyChooserInterface myStrategy = new StrategyChooser();
        MessageScheduler messageScheduler = new MessageScheduler(resourcePool, gateway, myStrategy);
        Message m1 = new Message(2, "message1");
        Message m2 = new Message(1, "message2");
        Message m3 = new Message(2, "message3");
        Message m4 = new Message(3, "message4");
        Message m5 = new Message(1, "message5");
        Message m6 = new Message(2, "message6");
        messageScheduler.receiveMsg(m1);
        messageScheduler.receiveMsg(m2);
        messageScheduler.receiveMsg(m3);
        messageScheduler.receiveMsg(m4);
        messageScheduler.receiveMsg(m5);
        messageScheduler.receiveMsg(m6);
    }

    @Test
    public void testScaling() {
        Gateway gateway = new Gateway();
        ResourcePool resourcePool = new ResourcePool(NUMBER_OF_RESOURCE_HANDLERS);
        StrategyChooserInterface myStrategy = new StrategyChooser();
        MessageScheduler messageScheduler = new MessageScheduler(resourcePool, gateway, myStrategy);
        Message m5 = new Message(7, "message5");
        Message m6 = new Message(4, "message6");
        Message m7 = new Message(5, "message7");
        Message m8 = new Message(7, "message8");
        resourcePool.scaleNumberOfResources(3);
        messageScheduler.receiveMsg(m5);
        messageScheduler.receiveMsg(m6);
        messageScheduler.receiveMsg(m7);
        messageScheduler.receiveMsg(m8);
    }

    @Test(expected = Error.class)
    public void testTermination() {
        Gateway gateway = new Gateway();
        ResourcePool resourcePool = new ResourcePool(2);
        StrategyChooserInterface myStrategy = new StrategyChooser();
        MessageScheduler messageScheduler = new MessageScheduler(resourcePool, gateway, myStrategy);
        Message m1 = new Message(2, "message1");
        Message m2 = new Message(1, "message2");
        Message m3 = new Message(2, "message3");
        Message m4 = new Message(3, "message4");
        Message m5 = new Message(1, "message5");
        Message m6 = new Message(2, "message6");
        messageScheduler.receiveMsg(m1);
        messageScheduler.receiveMsg(m2);
        m3.setTerminated();
        messageScheduler.receiveMsg(m3);
        messageScheduler.receiveMsg(m4);
        messageScheduler.receiveMsg(m5);
        messageScheduler.receiveMsg(m6);
    }


    @Test
    public void scaleResources() {
        Gateway gateway = new Gateway();
        ResourcePool resourcePool = new ResourcePool(NUMBER_OF_RESOURCE_HANDLERS);
        StrategyChooserInterface myStrategy = new StrategyChooser();
        MessageScheduler messageScheduler = new MessageScheduler(resourcePool, gateway, myStrategy);
        Message m1 = new Message(2, "message1");
        Message m2 = new Message(1, "message2");
        Message m3 = new Message(2, "message3");
        Message m4 = new Message(3, "message4");
        Message m5 = new Message(1, "message5");
        Message m6 = new Message(2, "message6");
        messageScheduler.receiveMsg(m1);
        messageScheduler.receiveMsg(m2);
        messageScheduler.scaleNumberOfResources(2);
        messageScheduler.receiveMsg(m3);
        messageScheduler.receiveMsg(m4);
        messageScheduler.receiveMsg(m5);
        messageScheduler.receiveMsg(m6);
    }
}
