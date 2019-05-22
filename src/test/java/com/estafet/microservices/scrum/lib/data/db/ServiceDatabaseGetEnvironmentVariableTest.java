package com.estafet.microservices.scrum.lib.data.db;

import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * Test suite for {@link ServiceDatabase#getEnvironmentVariable(String)}.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
@SuppressWarnings("javadoc")
@RunWith(PowerMockRunner.class)
@PrepareForTest({
    ServiceDatabase.class
})
public class ServiceDatabaseGetEnvironmentVariableTest {

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
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() {

        objectUnderTest = new ServiceDatabase();

        // Mock System.getenv(String).
        PowerMockito.mockStatic(System.class);
    }

    /**
     * Called after each test method has run, regardless of success, failure or thrown exceptions.
     */
    @After
    public void tearDown() {
        objectUnderTest = null;
    }

    /**
     * Test happy path for {@link ServiceDatabase#getEnvironmentVariable(String)}.
     * @throws Exception
     *          If PowerMock fails.
     */
    @Test
    public final void testGetEnvironmentVariableOK() throws Exception {

        final String expected = "value";
        final String actual = doTest("variable", expected);

        Assert.assertEquals("Should return \"" + expected + "\".", expected, actual);
    }

    /**
     * Test failure path for {@link ServiceDatabase#getEnvironmentVariable(String)}.
     * @throws Exception
     *          If PowerMock fails.
     */
    @Test
    public final void testGetEnvironmentVariableNotSet() throws Exception {

        final String expected = null;
        final String testVariable = "variable";

        // Set up the expected exception.

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The " + testVariable + " environment variable is not set.");
        thrown.expectCause(nullValue(Throwable.class));
        thrown.reportMissingExceptionWithMessage("IllegalStateException expected");

        final String actual = doTest(testVariable, expected);

        Assert.fail("Should throw IllegalStateException. Returned " + actual);
    }

    /**
     * Do the test.
     * @param testValue
     *          The value to test with.
     * @param returnValue
     *          The value returned by {@code System.getenv(String)}.
     * @return
     *          The actual value.
     * @throws Exception
     *          If PowerMock fails.
     */
    private String doTest(final String testValue, final String returnValue) throws Exception {

        Mockito.when(System.getenv(testValue)).thenReturn(returnValue);

        final String actual = Whitebox.invokeMethod(objectUnderTest, "getEnvironmentVariable", testValue);

        return actual;
    }
}
