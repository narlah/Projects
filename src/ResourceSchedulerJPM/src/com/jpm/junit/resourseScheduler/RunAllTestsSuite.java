package ResourceSchedulerJPM.src.com.jpm.junit.resourseScheduler;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ResourcePoolTests.class, ActiveGroupTests.class, SystemTests.class})
public class RunAllTestsSuite {
}

