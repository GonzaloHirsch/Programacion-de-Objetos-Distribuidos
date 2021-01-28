import ar.edu.itba.pod.concurrency.e1.GenericService;
import ar.edu.itba.pod.concurrency.e1.ProvidedGenericServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Comando para poder instalar el JAR
 * mvn install:install-file -Dfile=./my_lib/concurrency-provided-1.0-SNAPSHOT.jar -DpomFile=./my_lib/pom.xml 
 */
public class ServiceLocalTest {
    private static Logger logger = LoggerFactory.getLogger(ServiceLocalTest.class);
    private GenericService service;
    private static final int TEST_SERVICE_CALL_COUNT = 4;

    @Before
    public final void before() {
        service = new ProvidedGenericServiceImpl();
    }

    @Test
    public void test_completed_future() throws Exception {
        for (int i = 0; i < TEST_SERVICE_CALL_COUNT; i++){
            this.service.addVisit();
        }
        assertEquals(TEST_SERVICE_CALL_COUNT, this.service.getVisitCount());
    }
}
