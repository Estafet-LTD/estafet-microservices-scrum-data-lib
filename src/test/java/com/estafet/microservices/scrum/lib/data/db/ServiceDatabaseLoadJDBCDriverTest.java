package com.estafet.microservices.scrum.lib.data.db;

import static org.hamcrest.CoreMatchers.isA;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.reflect.Whitebox;

/**
 * Test suite for {@link ServiceDatabase#loadJDBDriver(String)}.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
@SuppressWarnings("javadoc")
public class ServiceDatabaseLoadJDBCDriverTest {

    /**
     * The expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The object under test.
     */
    private ServiceDatabase objectUnderTest = null;

    /**
     * Called before each test method is run.
     *
     * @throws Exception
     *          When PowerMock fails.
     */
    @Before
    public void setUp() throws Exception {
        objectUnderTest = new ServiceDatabase();
   }

    /**
     * Called after each test method has run, regardless of success, failure or thrown exceptions.
     */
    @After
    public void tearDown() {
        objectUnderTest = null;
    }

    /**
     * Test happy path for {@link ServiceDatabase#loadJDBCDriver(String)}.
     * @throws Exception
     *          If PowerMock fails.
     */
    @Test
    public final void testLoadJDBCDriverOK() throws Exception {

        final String driverClassName = "org.postgresql.Driver";
        final Class<?> actual = doTest(driverClassName);

        Assert.assertEquals("The driver class should be " + driverClassName,
                            driverClassName,
                            actual.getName());
    }

    /**
     * Test driver class not found for {@link ServiceDatabase#loadJDBCDriver(String)}.
     *
     * @throws Exception
     *          If PowerMock fails.
     */
    @Test
    public void testloadJDBCDriverClassNotFound() throws Exception {

        final String unknownClass  = "org.example.SomeDriver";

        // Set up the expected exception.

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Unable to load JDBC driver class: " + unknownClass);
        thrown.expectCause(isA(ClassNotFoundException.class));
        thrown.reportMissingExceptionWithMessage("RuntimeException expected");

        final Class<?> actual = doTest(unknownClass);

        Assert.fail("Should throw RuntimeException. Returned " + actual);
    }

    /**
     * Do the test.
     * @param driverClassName
     *          The driver class name.
     * @return
     *          The actual driver {@link Class} object.
     * @throws Exception
     *          If PowerMock fails.
     */
    private Class<?> doTest(final String driverClassName) throws Exception {

        final Class<?> actual = Whitebox.invokeMethod(objectUnderTest, "loadJDBCDriver", driverClassName);

        return actual;
    }
}