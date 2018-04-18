package ResourceSchedulerJPM.src.com.jpm.resourceScheduler;

import ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces.GatewayInterface;
import ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces.MessageInterface;
import ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces.MessageSchedulerInterface;
import ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces.StrategyChooserInterface;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class MessageScheduler implements MessageSchedulerInterface {

    private GatewayInterface gateway;
    private ResourcePool resourcePool;
    private StrategyChooserInterface strategyChooser;
    private LinkedHashMap<Integer, LinkedList<Message>> messageQueuesArray = new LinkedHashMap<Integer, LinkedList<Message>>();
    private HashSet<Integer> cancelledMsgGroups = new HashSet<Integer>();
    private HashSet<Integer> terminatedMsgGroups = new HashSet<Integer>();

    public MessageScheduler(ResourcePool resourcePool, Gateway gateway, StrategyChooserInterface strategyChooser) {
        this.resourcePool = resourcePool;
        this.gateway = gateway;
        this.strategyChooser = strategyChooser;
    }

    public void receiveMsg(Message msg) {
        if (cancelledMsgGroups.contains(msg.getGroupID())) {
            return;
        }
        if (msg.isTerminationMsg()) {
            terminatedMsgGroups.add(msg.getGroupID());
        } else if (terminatedMsgGroups.contains(msg.getGroupID())) {
            throw new Error("Termination msg received before, we should not be getting more messages of this group: " + msg.getGroupID());
        }
        poolMessageToQueue(msg);
        if (resourcePool.isThereAFreeResource()) {
            sendMsgAndNotifyResourcePool(strategyChooser.pickTheCorrectMessage(messageQueuesArray, resourcePool.getActiveGroups()));
        }
    }

    public synchronized int completeMsg(Message msg) {
        resourcePool.msgIsComplete(msg);
        sendMsgAndNotifyResourcePool(strategyChooser.pickTheCorrectMessage(messageQueuesArray, resourcePool.getActiveGroups()));
        return msg.getGroupID();
    }

    public void cancelMessageGroup(Integer messageGroup) {
        cancelledMsgGroups.add(messageGroup);
    }

    public void removeCancellationOfMessageGroup(Integer messageGroup) {
        cancelledMsgGroups.remove(messageGroup);
    }

    public void scaleNumberOfResources(int numberOfResourcesChangedTo) {
        resourcePool.scaleNumberOfResources(numberOfResourcesChangedTo);
        if (resourcePool.isThereAFreeResource()) {
            sendMsgAndNotifyResourcePool(strategyChooser.pickTheCorrectMessage(messageQueuesArray, resourcePool.getActiveGroups()));
        }
    }

    private void sendMsgAndNotifyResourcePool(Message msg) {
        if (msg != null) {
            MessageInterface myMessage = (MessageInterface) MessageProxy.newInstance(new MessageProxyHelperObject(msg, this));
            gateway.send(myMessage);
            resourcePool.msgIsSend(msg);
        }
    }

    private void poolMessageToQueue(Message msg) {
        if (messageQueuesArray.containsKey(msg.getGroupID())) {
            messageQueuesArray.get(msg.getGroupID()).add(msg);
        } else {
            LinkedList<Message> newLinkedList = new LinkedList<Message>();
            newLinkedList.add(msg);
            messageQueuesArray.put(msg.getGroupID(), newLinkedList);
        }
    }
}
