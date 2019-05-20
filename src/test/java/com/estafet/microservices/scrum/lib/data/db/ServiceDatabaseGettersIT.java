package com.estafet.microservices.scrum.lib.data.db;

import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.rules.ExpectedException;
import org.powermock.reflect.Whitebox;
/**
 * Integration Test suite for {@link ServiceDatabase#getDbURL()}, {@link ServiceDatabase#getDbUser()} and
 * {@link ServiceDatabase#getDbPassword()}.
 *
 * <p>To run these tests:</p>
 * <pre>
 *     $ mvn clean install failsafe:integration-test
 * <pre>
 *
 * <p>To skip the jar signing, add {@code -Dgpg.skip=true} to the Maven command line.
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public class ServiceDatabaseGettersIT {

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
     * Use the <a href="https://stefanbirkner.github.io/system-rules/index.html#EnvironmentVariables">System Rules</a>
     * library to allow testing with environment variables.
     */
    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    /**
     * The expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The object under test.
     */
    private ServiceDatabase objectUnderTest = null;

    /**
     * Called before each test method is run.
     * @throws java.lang.Exception
     *          If PowerMock fails.
     */
    @Before
    public void setUp() throws Exception {
        objectUnderTest = new ServiceDatabase();

        environmentVariables.clear(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

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
     * Test {@link ServiceDatabase#getDbPassword()} with environment variable not set.
     */
    @Test
    public final void testGetDbPasswordNotSet() {

        environmentVariables.set(DATABASE_URL, "databaseurl");
        environmentVariables.set(DATABASE_USER, "username");

        final String expectedMessage = "The " + DATABASE_PASSWORD + " environment variable is not set.";
        setExcpectedException(expectedMessage);

        final String actual = objectUnderTest.getDbPassword();

        Assert.fail("getDbPassword() Should throw an IllegalStateException. Returned \"" + actual + "\".");
    }

    /**
     * Test happy path for {@link ServiceDatabase#getDbPassword()}.
     */
    @Test
    public final void testGetDbPasswordOK() {

        final String expected = "password";

        environmentVariables.set("DATABASE_PASSWORD", expected);
        final String actual = objectUnderTest.getDbPassword();

        Assert.assertEquals("getDbUser() Should return \"" + expected + "\".", expected, actual);
    }

    /**
     * Test {@link ServiceDatabase#getDbURL()} with environment variable not set.
     */
    @Test
    public final void testGetDbURLNotSet() {

        environmentVariables.set(DATABASE_USER, "username");
        environmentVariables.set(DATABASE_PASSWORD, "password");

        final String expectedMessage = "The " + DATABASE_URL + " environment variable is not set.";
        setExcpectedException(expectedMessage);

        final String actual = objectUnderTest.getDbURL();

        Assert.fail("getDbURL() Should throw an IllegalStateException. Returned \"" + actual + "\".");
    }

    /**
     * Test happy path for {@link ServiceDatabase#getDbURL()}.
     */
    @Test
    public final void testGetDbURLOK() {

        final String expected = "databaseUrl";

        environmentVariables.set(DATABASE_URL, expected);
        final String actual = objectUnderTest.getDbURL();

        Assert.assertEquals("getDbURL() Should return \"" + expected + "\".", expected, actual);
    }

    /**
     * Test {@link ServiceDatabase#getDbUser()} with environment variable not set.
     */
    @Test
    public final void testGetDbUserNotSet() {

        environmentVariables.set(DATABASE_URL, "databaseurl");
        environmentVariables.set(DATABASE_PASSWORD, "password");

        final String expectedMessage = "The " + DATABASE_USER + " environment variable is not set.";
        setExcpectedException(expectedMessage);

        final String actual = objectUnderTest.getDbUser();

        Assert.fail("getDbUser() Should throw an IllegalStateException. Returned \"" + actual + "\".");
    }

    /**
     * Test happy path for {@link ServiceDatabase#getDbUser()}.
     */
    @Test
    public final void testGetDbUserOK() {

        final String expected = "username";

        environmentVariables.set("DATABASE_USER", expected);
        final String actual = objectUnderTest.getDbUser();

        Assert.assertEquals("getDbUser() Should return \"" + expected + "\".", expected, actual);
    }

    /**
     * Set the failure exception.
     *
     * @param message
     *          The expected error message.
     */
    private final void setExcpectedException(final String message) {

        // Set up the expected exception.

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(message);
        thrown.expectCause(nullValue(Throwable.class));
        thrown.reportMissingExceptionWithMessage("IllegalStateException expected");
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
