package ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces;

import ResourceSchedulerJPM.src.com.jpm.resourceScheduler.Message;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public interface StrategyChooserInterface {

    public Message pickTheCorrectMessage(LinkedHashMap<Integer, LinkedList<Message>> messageQueuesArray, Integer[] activeGroups);

}