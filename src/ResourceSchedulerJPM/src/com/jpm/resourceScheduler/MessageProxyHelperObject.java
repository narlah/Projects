package ResourceSchedulerJPM.src.com.jpm.resourceScheduler;

public class MessageProxyHelperObject {
    private MessageScheduler messageScheduler;
    private Message msg;

    public MessageProxyHelperObject(Message msg, MessageScheduler messageScheduler) {
        this.messageScheduler = messageScheduler;
        this.msg = msg;
    }

    public MessageScheduler getMessageScheduler() {
        return messageScheduler;
    }

    public Message getMsg() {
        return msg;
    }


}
