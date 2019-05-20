package com.estafet.microservices.scrum.lib.data.db;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.StringReader;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.reflect.Whitebox;

import com.thoughtworks.xstream.XStream;
/**
 * Integration Test suite for {@link ServiceDatabase}, creating the class from XML.
 *
 * <p>The purpose of these tests is to verify the behaviour of {@link ServiceDatabase#readResolve()}.
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
@SuppressWarnings("javadoc")
public class ServiceDatabaseXStreamIT {

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
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Test {@link ServiceDatabase} with a badly constructed XML file.
     *
     * <p>The service element is not terminated because there is a missing "/"</p>
     *
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testServiceDatabaseBadXML() throws Exception {


        // Set up the expected exception.

        thrown.expect(com.thoughtworks.xstream.XStreamException.class);
        thrown.expectCause(nullValue(java.io.InvalidObjectException.class));
        thrown.reportMissingExceptionWithMessage("XStreamException expected");

        final String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                 "<service>\n" +
                                 "    <name>project-api</name>\n" +
                                 "    <db-url-env>DATABASE_URL</db-url-env>\n" +
                                 "    <db-user-env>DATABASE_USER</db-user-env>\n" +
                                 "    <db-password-env>DATABASE_PASSWORD</db-password-env>\n" +
                                 "<service>\n";

        doExpectedFailureTest(xmlString);
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
    public final void testServiceDatabaseFieldBadEnvironmentVariableName() throws Exception {

        setExpectedException();

        final String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                 "<service>\n" +
                                 "    <name>project-api</name>\n" +
                                 "    <db-url-env>2DATABASE_URL</db-url-env>\n" +
                                 "    <db-user-env>DATABASE_USER</db-user-env>\n" +
                                 "    <db-password-env>DATABASE_PASSWORD</db-password-env>\n" +
                                 "</service>\n";

        doExpectedFailureTest(xmlString);
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
    public final void testServiceDatabaseFieldBlank() throws Exception {

        setExpectedException();

        final String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                 "<service>\n" +
                                 "    <name>project-api</name>\n" +
                                 "    <db-url-env>  </db-url-env>\n" +
                                 "    <db-user-env>DATABASE_USER</db-user-env>\n" +
                                 "    <db-password-env>DATABASE_PASSWORD</db-password-env>\n" +
                                 "</service>\n";

        doExpectedFailureTest(xmlString);
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
    public final void testServiceDatabaseFieldEmpty() throws Exception {

        setExpectedException();

        final String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                 "<service>\n" +
                                 "    <name>project-api</name>\n" +
                                 "    <db-url-env></db-url-env>\n" +
                                 "    <db-user-env>DATABASE_USER</db-user-env>\n" +
                                 "    <db-password-env>DATABASE_PASSWORD</db-password-env>\n" +
                                 "</service>\n";

        doExpectedFailureTest(xmlString);
    }

    /**
     * Test {@link ServiceDatabase} with leading whitespace in a field.
     *
     * <p>The {@code db-url-env} field has leading whitespace.</p>
     *
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testServiceDatabaseFieldLeadingWhitespace() throws Exception {

        setExpectedException();

        final String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                 "<service>\n" +
                                 "    <name>project-api</name>\n" +
                                 "    <db-url-env>  DATABASE_URL</db-url-env>\n" +
                                 "    <db-user-env>DATABASE_USER</db-user-env>\n" +
                                 "    <db-password-env>DATABASE_PASSWORD</db-password-env>\n" +
                                 "</service>\n";

        doExpectedFailureTest(xmlString);
    }

    /**
     * Test {@link ServiceDatabase} with a missing field.
     *
     * <p>The {@code db-url-env} field is missing.</p>
     *
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testServiceDatabaseFieldMissing() throws Exception {

        setExpectedException();

        final String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                 "<service>\n" +
                                 "    <name>project-api</name>\n" +
                                 "    <db-user-env>DATABASE_USER</db-user-env>\n" +
                                 "    <db-password-env>DATABASE_PASSWORD</db-password-env>\n" +
                                 "</service>\n";

        doExpectedFailureTest(xmlString);
    }

    /**
     * Test {@link ServiceDatabase} with trailing whitespace in a field.
     *
     * <p>The {@code db-url-env} field has trailing whitespace.</p>
     *
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testServiceDatabaseFieldTrailingWhitespace() throws Exception {

        setExpectedException();

        final String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                 "<service>\n" +
                                 "    <name>project-api</name>\n" +
                                 "    <db-url-env>DATABASE_URL  </db-url-env>\n" +
                                 "    <db-user-env>DATABASE_USER</db-user-env>\n" +
                                 "    <db-password-env>DATABASE_PASSWORD</db-password-env>\n" +
                                 "</service>\n";

        doExpectedFailureTest(xmlString);
    }

    /**
     * Test {@link ServiceDatabase} happy path.
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testServiceDatabaseHappyPath() throws Exception {

        final String expectedName = "project-api";

        final String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                 "<service>\n" +
                                 "    <name>" + expectedName + "</name>\n" +
                                 "    <db-url-env>" + DATABASE_URL + "</db-url-env>\n" +
                                 "    <db-user-env>" + DATABASE_USER + "</db-user-env>\n" +
                                 "    <db-password-env>" + DATABASE_PASSWORD + "</db-password-env>\n" +
                                 "</service>\n";

        final ServiceDatabase actual = serviceDatabaseFromXml(xmlString);

        checkField(actual, "name", expectedName);
        checkField(actual, "dbURLEnvVariable", DATABASE_URL);
        checkField(actual, "dbUserEnvVariable", DATABASE_USER);
        checkField(actual, "dbPasswordEnvVariable", DATABASE_PASSWORD);
    }

    /**
     * Check a field on the object under test.
     *
     * <p>Avoids having to cast null field values, which cause a compilation error when {@code Whitebox.setInternalState}
     * is called with {@code null} as the third parameter.</p>
     *
     * @param serviceDatabase
     *          The {@link ServiceDatabase} to check.
     * @param fieldName
     *          The name of the field.
     * @param expectedValue
     *          The value to check.
     * @throws Exception
     *          If PowerMock fails.
     */
    private void checkField(final ServiceDatabase serviceDatabase,
                            final String fieldName,
                            final String expectedValue) throws Exception {
        final String actual = Whitebox.getInternalState(serviceDatabase, fieldName);

        Assert.assertEquals("The " + fieldName + " field should contain " + expectedValue + ".", expectedValue, actual);
    }

    /**
     * Close a resource.
     * @param resource
     *          The resource to close. Can be {@code null}.
     */
    private void close(final AutoCloseable resource) {
        Optional.ofNullable(resource).ifPresent(closeable -> {
            try {
                closeable.close();
            } catch (final Exception e) {
                System.out.println("Warning: closing " + resource.toString() + " failed: " + e.toString() + ".");
            }
        });
    }

    /**
     * Execute a test that is expected to throw an Exception.
     * @param xmlString
     *          The XML string to test with.
     */
    private void doExpectedFailureTest(final String xmlString) {
        final ServiceDatabase actual = serviceDatabaseFromXml(xmlString);

        Assert.fail("Should throw XStreamException. Returned " + actual.toString());
    }

    /**
     * Use XStream to generate a {@link ServiceDatabase} from an XML String
     * @param xmlString
     *          The XML specification of the {@link ServiceDatabase}.
     * @return
     *          The {@link ServiceDatabase} object.
     */
    private ServiceDatabase serviceDatabaseFromXml(final String xmlString) {
       StringReader reader = null;
        try {
            reader = new StringReader(xmlString);
            final XStream xStream = new XStream();
            xStream.processAnnotations(ServiceDatabase.class);
            final ServiceDatabase serviceDatabase = ServiceDatabase.class.cast(xStream.fromXML(reader));
            return serviceDatabase;
        }
        finally {
            close(reader);
        }
    }

    /**
     * Set the expected exception.
     *
     */
    private final void setExpectedException() {

        // Set up the expected exception.

        thrown.expect(com.thoughtworks.xstream.XStreamException.class);
        thrown.expectCause(isA(java.io.InvalidObjectException.class));
        thrown.reportMissingExceptionWithMessage("XStreamException expected.");
    }
}
