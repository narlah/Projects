package RandomizeMusic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class LogFileEngineTest {

  private static final String TEXT = "alabala";
  private static final String FILE_NAME = "tmp.txt";
  private LogFileEngine engine;

  @Before
  public void setUp() {
    engine = new LogFileEngine(".", FILE_NAME);
  }

  @After
  public void tearDown() throws IOException {
    engine.closeFile();
  }

  @Test
  public void testRecord() {

    //Test
    Assert.assertNotNull(engine);
    Assert.assertTrue(engine.logText(new String[]{TEXT, "1", TEXT + "1"}));

    ArrayList<String[]> logRecords = engine.returnLogRecords();
    Assert.assertFalse(logRecords.isEmpty());

    String[] record = logRecords.get(0);
    Assert.assertTrue(record.length > 0);
    Assert.assertEquals(record[0], TEXT);
    Assert.assertEquals(record[1], "1");
    Assert.assertEquals(record[2], TEXT + 1);
  }

  @Test
  public void testDumpTheLog() throws IOException {
    //Prepare
    Assert.assertNotNull(engine);
    Assert.assertTrue(engine.logText(new String[]{TEXT, "1", TEXT + "1"}));
    engine.dumpTheLog(true);
    File file = new File(FILE_NAME);

    //Test
    Assert.assertTrue(file.exists());
    Assert.assertTrue(file.length() > 0);
  }

}