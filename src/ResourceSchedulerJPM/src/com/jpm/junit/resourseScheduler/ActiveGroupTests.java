package ResourceSchedulerJPM.src.com.jpm.junit.resourseScheduler;

import ResourceSchedulerJPM.src.com.jpm.resourceScheduler.ActiveGroups;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ActiveGroupTests {

    @Test
    public void testActiveGroupsAdding() {
        ActiveGroups activeGroup = new ActiveGroups();
        activeGroup.addActiveGroup(1);
        activeGroup.addActiveGroup(2);
        activeGroup.addActiveGroup(3);
        assertTrue("Yes Group 1 is indeed active", activeGroup.isGroupActive(1));
        assertFalse("Yes Group 4 is indeed inActive", activeGroup.isGroupActive(4));
        Integer[] activeGroupsList = activeGroup.getActiveGroups();
        assertTrue("Correct", activeGroupsList[0] == 1);
        assertTrue("Correct", activeGroupsList[1] == 2);
        assertTrue("Correct", activeGroupsList[2] == 3);
        assertFalse("InCorrect test", activeGroupsList[2] == 12);
    }

    @Test
    public void testActiveGroupsRemoving() {
        ActiveGroups activeGroup = new ActiveGroups();
        activeGroup.addActiveGroup(1);
        activeGroup.addActiveGroup(2);
        activeGroup.addActiveGroup(3);
        activeGroup.reduceActiveGroup(2);
        assertTrue("Yes Group 2 is indeed inActive", !activeGroup.isGroupActive(2));
    }
}
