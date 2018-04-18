package ResourceSchedulerJPM.src.com.jpm.resourceScheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MessageProxy implements java.lang.reflect.InvocationHandler {

    private Object obj;
    private MessageScheduler messageScheduler;

    private MessageProxy(Object obj, MessageScheduler msgScheduler) {
        this.obj = obj;
        this.messageScheduler = msgScheduler;
    }

    public static Object newInstance(Object obj) {
        MessageProxyHelperObject composite = (MessageProxyHelperObject) obj;
        Message msg = composite.getMsg();
        return java.lang.reflect.Proxy.newProxyInstance(msg.getClass().getClassLoader(), msg.getClass().getInterfaces(), new MessageProxy(msg,
                composite.getMessageScheduler()));
    }

    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object result;
        try {
            if (m.getName() == "completed") {
                result = m.invoke(obj, args);
                messageScheduler.completeMsg((Message) obj);
            } else {
                result = m.invoke(obj, args);
            }

        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
        }
        return result;
    }
}
