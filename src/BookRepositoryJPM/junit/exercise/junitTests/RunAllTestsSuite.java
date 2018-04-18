package BookRepositoryJPM.junit.exercise.junitTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({RepositoryServiceImplTestGetBook.class, RepositoryServiceImplTestGetSummary.class})
//ResourcePoolTests.class, ActiveGroupTests.class , SystemTests.class
public class RunAllTestsSuite {
}

