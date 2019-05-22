package com.estafet.microservices.scrum.lib.data.db;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.reflect.Whitebox;

import com.estafet.microservices.scrum.lib.test.CauseMatcher;

/**
 * Integration Test suite for {@link ServiceDatabases#validateServicesFileString()}.
 *
 * <p>The purpose of these tests is to verify error handling.
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
public class ServiceDatabasesValidateServicesFileIT {

    /**
     * The name of the method being tested.
     */
    private static final String METHOD_UNDER_TEST = "validateServicesFile";

    /**
     * The name of XML schema file used to validate the services XML file.
     */
    private static final String SERVICES_SCHEMA_FILE = "services.xsd";

    /**
     * The expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Test {@link ServiceDatabases#validateServicesFile(String} happy path.
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testValidateServicesFileHappyPath() throws Exception {

        Whitebox.invokeMethod(ServiceDatabases.class, METHOD_UNDER_TEST, "valid_services.xml");
    }

    /**
     * Test {@link ServiceDatabases#validateServicesFile(String} with non-existent file.
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testValidateServicesServicesFileNotFound() throws Exception {
        final String servicesFile = "non_existent.xml";

        final String causeMessage = "The " + servicesFile +" resource is not on the classpath.";
        final Throwable expectedCause = new FileNotFoundException(causeMessage);
        final String expectedMessage = "ERROR: Failed to validate " +
                                       servicesFile +
                                       " against the schema in " +
                                       SERVICES_SCHEMA_FILE + "." +
                                       " The error is " +
                                       expectedCause.toString();
        doExpectedFailureTest(servicesFile, expectedMessage, expectedCause);
    }

    /**
     * Test {@link ServiceDatabases#validateServicesFile(String} with bad field.
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testValidateServicesServicesBadField() throws Exception {
        final String servicesFile = "bad_environment_variable.xml";

        doSAXParseFailureTest(servicesFile);
    }

    /**
     * Test {@link ServiceDatabases#validateServicesFile(String} with badly formed XML.
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testValidateServicesServicesBadXml() throws Exception {
        final String servicesFile = "bad_xml.xml";

        doSAXParseFailureTest(servicesFile);
    }

    /**
     * Test {@link ServiceDatabases#validateServicesFile(String} with an empty file.
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testValidateServicesServicesEmptyFile() throws Exception {
        final String servicesFile = "empty.xml";

        doSAXParseFailureTest(servicesFile);
    }

    /**
     * Test {@link ServiceDatabases#validateServicesFile(String} with an empty file.
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testValidateServicesServicesNoServices() throws Exception {
        final String servicesFile = "no_services.xml";

        doSAXParseFailureTest(servicesFile);
    }

    /**
     * Test {@link ServiceDatabases#validateServicesFile(String} with the name field missing.
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testValidateServicesServicesMissingName() throws Exception {
        final String servicesFile = "missing_name.xml";

        doSAXParseFailureTest(servicesFile);
    }

    /**
     * Test {@link ServiceDatabases#validateServicesFile(String} with two name fields.
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testValidateServicesServicesTwoNameFields() throws Exception {
        final String servicesFile = "two_name_fields.xml";

        doSAXParseFailureTest(servicesFile);
    }

    /**
     * Test {@link ServiceDatabases#validateServicesFile(String} with an unknown field.
     * @throws Exception
     *          Iff PowerMock fails.
     */
    @Test
    public final void testValidateServicesServicesUnknownField() throws Exception {
        final String servicesFile = "unknown_field.xml";

        doSAXParseFailureTest(servicesFile);
    }

    /**
     * Execute a test where a SAX parse exception is thrown.
     *
     * @param servicesFile
     *          The services XML file to use.
     * @throws Exception
     *          IF PowerMock fails.
     */
    private void doSAXParseFailureTest(final String servicesFile) throws Exception {
        final Class<? extends Throwable> expectedCauseType = org.xml.sax.SAXParseException.class;
        final String expectedMessage = "ERROR: Failed to validate " +
                                       servicesFile +
                                       " against the schema in " +
                                       SERVICES_SCHEMA_FILE + "." +
                                       " The error is " +
                                       expectedCauseType.getName();
        doExpectedFailureTest(servicesFile, expectedMessage, expectedCauseType);
    }

    /**
     * Execute a test that is expected to throw an Exception.
     * @param servicesFile
     *          The services XML file to test with.
     * @param expectedMessage
     *          The The expected message in the {@code RuntimeException}.
     * @param expectedCauseType
     *          The expected type of the cause of the {@code RuntimeException}. Cannot be {@code null}.
     * @throws Exception
     *          If PowerMock fails.
     */
    private void doExpectedFailureTest(
        final String servicesFile,
        final String expectedMessage,
        final Class<? extends Throwable> expectedCauseType) throws Exception {
        setExpectedException(expectedMessage, expectedCauseType);
        Whitebox.invokeMethod(ServiceDatabases.class, METHOD_UNDER_TEST, servicesFile);
        Assert.fail("Should throw RuntimeException");
    }

    /**
     * Execute a test that is expected to throw an Exception.
     * @param servicesFile
     *          The services XML file to test with.
     * @param expectedMessage
     *          The The expected message in the {@code RuntimeException}.
     * @param expectedCause
     *          The expected cause of the {@code RuntimeException}. Can be {@code null}.
     * @throws Exception
     *          If PowerMock fails.
     */
    private void doExpectedFailureTest(final String servicesFile,
                                       final String expectedMessage,
                                       final Throwable expectedCause) throws Exception {

        setExpectedException(expectedMessage, expectedCause);
        Whitebox.invokeMethod(ServiceDatabases.class, METHOD_UNDER_TEST, servicesFile);
        Assert.fail("Should throw RuntimeException");
    }

    /**
     * Set the expected exception.
     *
     * @param T
     *          The type of the expected cause.
     * @param expectedMessage
     *          The expected message.
     * @param expectedCause
     *          The expected cause. Can be {@code null}.
     *
     */
    private <T extends Throwable> void setExpectedException(final String expectedMessage, final T expectedCause) {

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(expectedMessage);
        thrown.expectCause(new CauseMatcher(expectedCause));
        thrown.reportMissingExceptionWithMessage("RuntimeException expected.");
    }

    /**
     * Set the expected exception.
     *
     * @param expectedMessage
     *          The expected message.
     * @param expectedCauseType
     *          The type of the expected cause. Cannot be {@code null}.
     *
     */
    private void setExpectedException(final String expectedMessage,
                                      final Class<? extends Throwable> expectedCauseType) {

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(expectedMessage);
        thrown.expectCause(new CauseMatcher(expectedCauseType));
        thrown.reportMissingExceptionWithMessage("RuntimeException expected.");
    }
}
