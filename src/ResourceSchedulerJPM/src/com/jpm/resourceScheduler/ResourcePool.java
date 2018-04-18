package ResourceSchedulerJPM.src.com.jpm.resourceScheduler;

import java.util.HashSet;

public class ResourcePool {
    private int maximumResources;
    private ActiveGroups activeGroups = new ActiveGroups();
    private HashSet<Message> resourceHandlersList = new HashSet<Message>();

    public ResourcePool(int numberOfResourceHandlers) {
        if (numberOfResourceHandlers > 0) {
            this.maximumResources = numberOfResourceHandlers;
        } else
            throw new IllegalArgumentException("The number of resource handlers : " + numberOfResourceHandlers + " is less or equal to 0");
    }

    public void msgIsSend(Message msg) {
        resourceHandlersList.add(msg);
        addActiveGroup(msg.getGroupID());
    }

    public void msgIsComplete(Message msg) {
        activeGroups.reduceActiveGroup(msg.getGroupID());
        resourceHandlersList.remove(msg);
    }

    public Integer[] getActiveGroups() {
        return activeGroups.getActiveGroups();
    }

    public boolean isThereAFreeResource() {
        return resourceHandlersList.size() < maximumResources ? true : false;
    }

    public void scaleNumberOfResources(int newNumberOfResourcesAvailable) {
        if (newNumberOfResourcesAvailable > 0) {
            this.maximumResources = newNumberOfResourcesAvailable;
        }
    }

    private void addActiveGroup(int activeGroupID) {
        activeGroups.addActiveGroup(activeGroupID);
    }

}
