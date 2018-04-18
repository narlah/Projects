package ResourceSchedulerJPM.src.com.jpm.resourceScheduler;

import ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces.GatewayInterface;
import ResourceSchedulerJPM.src.com.jpm.resourceSchedulerInterfaces.MessageInterface;

public class Gateway implements GatewayInterface {

    public void send(final MessageInterface msg) {
        Thread th = new Thread() {
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                msg.completed();
            }
        };
        th.start();
    }
}
