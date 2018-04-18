package myTestingFrameWork;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * Created by Nikolay.Kosev on 2/10/2016.
 * Class providing tooling and inheritance base for other test classes - comparing objects, xmls, rest call template
 */
abstract class BaseTestClass {
    private static final String RESPONSE_BEGINNING = "<html><head>"
            + "<style>"
            + " .green {color:green;}"
            + " .red {color:#A90000}"
            + "</style></head><body>";
    private static final String RESPONSE_ENDING = "</body></html>";

    // HTML tags
    private static final String HTML_UL_TAG = "<ul>";
    private static final String HTML_LI_GREEN_TAG = "<li class='green'>";
    private static final String HTML_LI_RED_TAG = "<li class='red'>";
    private static final String HTML_LI_END_TAG = "</li>";
    private static final String HTML_UL_END_TAG = "</ul>";

    private StringBuilder responceString = new StringBuilder();


    //export that into a new class Base Test Template
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public
    @ResponseBody
    String allTests(@RequestParam Map<String, String> allRequestParams) {
        setUp(allRequestParams);
        responceString.append(RESPONSE_BEGINNING + "\n ------- Responses for the test class").append(this.getClass().getName()).append(" implementation : ------- \n").append(HTML_UL_TAG);
        List<Method> allMethods = Arrays.asList(this.getClass().getDeclaredMethods());
        Collections.sort(allMethods, (o1, o2) -> {
            boolean firstBool = o1.getName().contains("Set");
            boolean secondBool = o2.getName().contains("Set");
            return (firstBool == secondBool ? 0 : (secondBool ? -1 : 1));
        });
        for (Method method : allMethods) {
            String methodNameOutput = method.getName() + "\n";
            try {
                if (method.getName().startsWith("test")) {
                    method.invoke(this);
                    responceString.append("\n" + HTML_LI_GREEN_TAG + " CORRECT : ").append(method.getName()).append(HTML_LI_END_TAG);
                }
            } catch (IllegalAccessException e) { // exception handling omitted for brevity
            } catch (java.lang.StackOverflowError stackOverflowError) {
                responceString.append("STACK_OVERFLOW").append(method.getName()).append(" ").append(Arrays.toString(stackOverflowError.getStackTrace()).substring(0, 200));
            } catch (InvocationTargetException invocationException) {
                if (invocationException.getCause() instanceof AssertionError)
                    responceString.append("\n \n" + HTML_LI_RED_TAG + " ASSERTION ERROR : ").append(methodNameOutput).append(stackTraceToString(invocationException)).append(HTML_LI_END_TAG);
                else {
                    Exception e = (Exception) invocationException.getTargetException();
                    if (e != null) //just replacing CorbaClientException for readability in git
                        responceString.append("\n \n" + HTML_LI_RED_TAG + " CORBA ERROR     : ").append(methodNameOutput).append(stackTraceToString(invocationException)).append(HTML_LI_END_TAG);
                    else {
                        responceString.append("\n \n" + HTML_LI_RED_TAG + " GENERAL ERROR  : ").append(methodNameOutput).append(stackTraceToString(invocationException)).append(HTML_LI_END_TAG);
                    }
                }
            }
        }
        String respond = responceString.toString() + HTML_UL_END_TAG + RESPONSE_ENDING;
        tearDown();
        return respond;
    }

    abstract void setUp(Map<String, String> allRequestParams);

    abstract void tearDown();

    private String stackTraceToString(InvocationTargetException invocationException) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        invocationException.getTargetException().printStackTrace(pw);
        return sw.toString().substring(0, 150) + "... \n";
    }


    public boolean compareTwoObjects(Object firstObject, Object secondObject, Set ignoredSet) {

        if (firstObject == null) return secondObject == null;
        if (secondObject == null) return false;
        //check if we have subclasses, if neither of them is assignable - different classes - false
        if (!firstObject.getClass().isAssignableFrom(secondObject.getClass()) &&
                !secondObject.getClass().isAssignableFrom(firstObject.getClass())) {
            return false;
        }
        Class c = firstObject.getClass();
        if (Comparable.class.isAssignableFrom(c))
            return ((Comparable) firstObject).compareTo(secondObject) == 0;

        for (; c != Object.class; c = c.getSuperclass()) {
            for (Field f : c.getDeclaredFields()) {
                if (f.isAccessible() && !ignoredSet.contains(f.getName())) {
                    Object v1 = null;
                    Object v2 = null;
                    try {
                        v1 = f.get(firstObject);
                        v2 = f.get(secondObject);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    //for primitive we check with == for equal value, for objects we check if they both point to same object
                    //yes we can use f.getType().isPrimitive()
                    //keep in mind that doubles might be a problem with that check
                    if (!(v1 == v2))
                        return false;
                }
            }
        }
        return true;
    }

    //Just null and size comparision of lists - when we have for example account and LocusAccount results.
    public boolean veryRoughtListCompare(List firstList, List secondList) {
        return (firstList == null && secondList == null) || (firstList != null && secondList != null && firstList.size() == secondList.size());

    }
}
