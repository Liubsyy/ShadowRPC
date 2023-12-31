import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Liubsyy
 * @date 2023/12/31
 **/
public class LogTest {
    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);


    @Test
    public void printLog(){
        logger.info("Info message");
        logger.error("Error message");
    }
}
