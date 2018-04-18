package ResourceSchedulerJPM.src.com.jpm.resourceScheduler;

import ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces.MessageInterface;

public class Message implements MessageInterface {
    private int groupID;
    private String name;
    private boolean terminated = false;

    public Message(int groupID, String name) {
        this.name = name;
        this.groupID = groupID;
    }

    public void completed() {
        // Currently do nothing, proxy class MessageProxy will take care for the connection with Message Scheduler.
    }

    public int getGroupID() {
        return groupID;
    }

    public void setTerminated() {
        terminated = true;
    }

    public boolean isTerminationMsg() {
        return terminated;
    }

    @Override
    public String toString() {
        return "Name : " + name + " with GroupID : " + groupID;

    }

}
