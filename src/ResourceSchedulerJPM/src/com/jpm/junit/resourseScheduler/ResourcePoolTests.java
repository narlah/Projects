package ResourceSchedulerJPM.src.com.jpm.junit.resourseScheduler;

import ResourceSchedulerJPM.src.com.jpm.resourceScheduler.Message;
import ResourceSchedulerJPM.src.com.jpm.resourceScheduler.ResourcePool;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResourcePoolTests {

    @Test
    public void testResourcePoolCreation() {
        ResourcePool resourcePool = new ResourcePool(2);
        assertTrue("There is free resource slots ", resourcePool.isThereAFreeResource());
        resourcePool.msgIsSend(new Message(1, "exampleGroup 1 Message 1 "));
        resourcePool.msgIsSend(new Message(2, "exampleGroup 2 Message 2"));
        assertTrue(resourcePool.getActiveGroups()[0] == 1);
        assertFalse(resourcePool.getActiveGroups()[1] == 11);

        assertFalse(resourcePool.isThereAFreeResource());
        resourcePool.msgIsSend(new Message(2, "exampleGroup 2 Message 4"));
        resourcePool.scaleNumberOfResources(4);
        assertTrue(resourcePool.isThereAFreeResource());
    }

}
