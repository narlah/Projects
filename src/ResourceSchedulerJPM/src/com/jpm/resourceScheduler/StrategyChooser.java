package ResourceSchedulerJPM.src.com.jpm.resourceScheduler;

import ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces.StrategyChooserInterface;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class StrategyChooser implements StrategyChooserInterface {

    public StrategyChooser() {
    }

    public Message pickTheCorrectMessage(LinkedHashMap<Integer, LinkedList<Message>> messageQueuesArray, Integer[] activeGroups) {
        Message msg = pickFirstAvailableMsgByActiveGroups(messageQueuesArray, activeGroups);
        return (msg != null ? msg : pickFirstAvailableMsgByPriority(messageQueuesArray));

    }

    private Message pickFirstAvailableMsgByPriority(LinkedHashMap<Integer, LinkedList<Message>> messageQueuesArray) {
        for (Integer key : messageQueuesArray.keySet()) {
            LinkedList<Message> messageQueues = messageQueuesArray.get(key);
            if (messageQueues.size() > 0) {
                return messageQueues.poll();
            }
        }
        return null;
    }

    private Message pickFirstAvailableMsgByActiveGroups(LinkedHashMap<Integer, LinkedList<Message>> messageQueuesArray, Integer[] activeGroups) {
        for (Integer activeGroup : activeGroups) {
            if (messageQueuesArray.get(activeGroup).size() > 0) {
                return messageQueuesArray.get(activeGroup).poll();
            }
        }
        return null;
    }
}
