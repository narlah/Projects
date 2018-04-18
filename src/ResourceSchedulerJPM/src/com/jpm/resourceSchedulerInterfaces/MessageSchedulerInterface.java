package ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces;

import ResourceSchedulerJPM.src.com.jpm.resourceScheduler.Message;

public interface MessageSchedulerInterface {

    public void receiveMsg(Message msg);

    public int completeMsg(Message msg);
}
