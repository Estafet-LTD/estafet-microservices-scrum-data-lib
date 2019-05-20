package com.estafet.microservices.scrum.lib.data.db;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.InvalidObjectException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.reflect.Whitebox;

/**
 * Test suite for {@link ServiceDatabase#readResolve()}.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
@SuppressWarnings("javadoc")
public class ServiceDatabaseReadResolveTest {

    private static final String DATABASE_NAME = "service-database";

    /**
     * Database password environment variable name.
     */
    private static final String DATABASE_PASSWORD = "DATABASE_PASSWORD";

    /**
     * Database URL environment variable name.
     */
    private static final String DATABASE_URL = "DATABASE_URL";

    /**
     * Database user environment variable name.
     */
    private static final String DATABASE_USER = "DATABASE_USER";

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

        setField("name", DATABASE_NAME);
        setField("dbURLEnvVariable", DATABASE_URL);
        setField("dbUserEnvVariable", DATABASE_USER);
        setField("dbPasswordEnvVariable", DATABASE_PASSWORD);
    }

    /**
     * Called after each test method has run, regardless of success, failure or thrown exceptions.
     */
    @After
    public void tearDown() {
        objectUnderTest = null;
    }

    /**
     * Test name field not set for {@link ServiceDatabase#readResolve()}.
     * @throws Exception
     *          If PowerMock fails.
     */
    @Test
    public final void testReadResolveNameFieldNotSet() throws Exception {

        setField("name", null);

        doExpectedFailureTest("The database name field is missing from the xml file.");
    }

    /**
     * Test happy path for {@link ServiceDatabase#readResolve()}.
     * @throws Exception
     *          If PowerMock fails.
     */
    @Test
    public final void testReadResolveOK() throws Exception {

        final ServiceDatabase expected = objectUnderTest;
        final ServiceDatabase actual = doTest();

        Assert.assertTrue("Should return the object under test.", expected == actual);
        Assert.assertNull("Connection should be null", objectUnderTest.connection);
        Assert.assertNull("Statement should be null", objectUnderTest.statement);
    }

    /**
     * Test name field not set for {@link ServiceDatabase#readResolve()}.
     * @throws Exception
     *          If PowerMock fails.
     */
    @Test
    public final void testReadResolvePasswordFieldNotSet() throws Exception {

        setField("dbPasswordEnvVariable", null);

        doExpectedFailureTest("The " + objectUnderTest.getName() +
                                     " database password environment variable field is missing from the xml file.");
    }

    /**
     * Test name field not set for {@link ServiceDatabase#readResolve()}.
     * @throws Exception
     *          If PowerMock fails.
     */
    @Test
    public final void testReadResolveUrlFieldNotSet() throws Exception {

        setField("dbURLEnvVariable", null);

        doExpectedFailureTest("The " + objectUnderTest.getName() +
                                     " database URL environment variable field is missing from the xml file.");
    }

    /**
     * Test name field not set for {@link ServiceDatabase#readResolve()}.
     * @throws Exception
     *          If PowerMock fails.
     */
    @Test
    public final void testReadResolveUserFieldNotSet() throws Exception {

        setField("dbUserEnvVariable", null);

        doExpectedFailureTest("The " + objectUnderTest.getName() +
                                     " database user environment variable field is missing from the xml file.");
    }

    /**
     * Test {@link ServiceDatabase} with a blank field.
     *
     * <p>The {@code db-url-env} field has an illegal environment variable name.</p>
     *
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testReadResolveFieldBadEnvironmentVariableName() throws Exception {

        final String badEnvironmentVariableName = "2"+ DATABASE_URL;

        setField("dbURLEnvVariable", badEnvironmentVariableName);

        doExpectedFailureTest("The " + DATABASE_NAME +
                              " database URL environment variable field  is not a valid environment variable name [" +
                              badEnvironmentVariableName + "].");
    }

    /**
     * Test {@link ServiceDatabase} with a blank field.
     *
     * <p>The {@code db-url-env} field is blank.</p>
     *
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testReadResolveFieldBlank() throws Exception {

        final String blankField = " ";

        setField("dbURLEnvVariable", blankField);

        doExpectedFailureTest("The " + DATABASE_NAME + " database URL environment variable field is blank.");
    }

    /**
     * Test {@link ServiceDatabase} with an empty field.
     *
     * <p>The {@code db-url-env} field is empty.</p>
     *
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testReadResolveFieldEmpty() throws Exception {

        final String emptyField = "";

        setField("dbURLEnvVariable", emptyField);

        doExpectedFailureTest("The " + DATABASE_NAME + " database URL environment variable field is empty.");
    }

    /**
     * Test {@link ServiceDatabase} with a leading whitespace in a field.
     *
     * <p>The {@code db-url-env} field has leading whitespace.</p>
     *
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testReadResolveFieldLeadingWhitespace() throws Exception {

        final String leadingWhiteSpace = " " + DATABASE_URL;

        setField("dbURLEnvVariable", leadingWhiteSpace);

        doExpectedFailureTest("The " + DATABASE_NAME +
                              " database URL environment variable field has either leading or trailing whitespace " +
                              "characters.");
    }

    /**
     * Test {@link ServiceDatabase} with a trailing whitespace in a field.
     *
     * <p>The {@code db-url-env} field has trailing whitespace.</p>
     *
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testReadResolveFieldTrailingWhitespace() throws Exception {

        final String trailingWhiteSpace = DATABASE_URL + " ";

        setField("dbURLEnvVariable", trailingWhiteSpace);

        doExpectedFailureTest("The " + DATABASE_NAME +
                              " database URL environment variable field has either leading or trailing whitespace " +
                              "characters.");
    }

    /**
     * Test when a field is {@code null}.
     *
     * @param expectedMessage
     *          The expected exception message.
     * @throws Exception
     *          If PowerMock fails.
     */
    private void doExpectedFailureTest(final String expectedMessage) throws Exception {

        // Set up the expected exception.

        thrown.expect(InvalidObjectException.class);
        thrown.expectMessage(expectedMessage);
        thrown.expectCause(nullValue(Throwable.class));
        thrown.reportMissingExceptionWithMessage("InvalidObjectException expected");

        final ServiceDatabase actual = doTest();

        Assert.fail("Should throw InvalidObjectException. Returned " + actual);
    }

    /**
     * Do the test.
     * @return
     *          The actual value.
     * @throws Exception
     *          If PowerMock fails.
     */
    private ServiceDatabase doTest() throws Exception {

        final ServiceDatabase actual = Whitebox.invokeMethod(objectUnderTest, "readResolve");

        return actual;
    }

    /**
     * Set a field on the object under test.
     *
     * <p>Avoids having to cast null field values, which cause a compilation error when {@code Whitebox.setInternalState}
     * is called with {@code null} as the third parameter.</p>
     *
     * @param fieldName
     *          The name of the field.
     * @param fieldValue
     *          The value to set.
     * @throws Exception
     *          If PowerMock fails.
     */
    private void setField(final String fieldName, final String fieldValue) throws Exception {
        Whitebox.setInternalState(objectUnderTest, fieldName, fieldValue);
    }
}