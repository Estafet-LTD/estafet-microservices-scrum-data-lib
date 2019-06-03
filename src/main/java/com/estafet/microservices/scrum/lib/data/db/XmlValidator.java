/**
 *
 */
package com.estafet.microservices.scrum.lib.data.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Class to validate an XML file against an XSD schema.
 *
 * <p>Both the XML file and the schema must be on the classpath.</p>
 *
 * @author Steve Brown, Estafet Ltd.
 *
 */
public class XmlValidator {

    /**
     * SAX Parser error handler for the XML validator.
     * @author Steve Brown, Estafet Ltd.
     *
     */
    static final class ValidationErrorHandler implements ErrorHandler {

        /**
         * Receive notification of a warning.
         *
         * <p>SAX parsers will use this method to report conditions that are not errors or fatal errors,
         * as defined by the XML recommendation.
         *
         * <p>The warning is logged.</p>
         *
         * <p>The SAX parser must continue to provide normal parsing events after invoking this method: it should still
         * be possible for the application to process the document through to the end.</p>
         *
         * <p>Filters may use this method to report other, non-XML, warnings.</p>
         *
         * @param spe
         *          The warning information encapsulated in a {@link SAXParseException}.
         * @throws  SAXException
         *          Any SAX exception, possibly0 wrapping another exception.
         * @see org.xml.sax.SAXParseException
         */
        @Override
        public void warning(final SAXParseException spe) throws SAXException {
           System.out.println("WARNING: " + spe.toString());
        }

        /**
         * Receive notification of a recoverable error.
         *
         * <p>This corresponds to the definition of "error" in section 1.2 of the W3C XML 1.0 Recommendation.
         * For example, a validating parser would use this callback to report the violation of a
         * validity constraint.</p>
         *
         * <p>The error is logged.</p>
         *
         * <p>The SAX parser must continue to provide normal parsing events after invoking this method: it should still
         * be possible for the application to process the document through to the end. If the application cannot do
         * so, then the parser should report a fatal error even if the XML recommendation does not require it to
         * do so.</p>
         *
         * <p>Filters may use this method to report other, non-XML errors.</p>
         *
         * @param spe
         *          The error information encapsulated in a {@link SAXParseException}.
         * @throws SAXException
         *         Any SAX exception, possibly wrapping another exception.
         * @see org.xml.sax.SAXParseException
         */
        @Override
        public void error(final SAXParseException spe) throws SAXException {
            final String message = spe.toString();
            throw new SAXParseException("ERROR: Failed to validate: " + message, null, spe);
        }

        /**
         * Receive notification of a non-recoverable error.
         *
         * <p><strong>There is an apparent contradiction between the documentation for this method and the
         * documentation for {@link org.xml.sax.ContentHandler#endDocument}.  Until this ambiguity is resolved in a
         * future major release of Java, clients should make no assumptions as to whether {@code endDocument()} will or
         * will not be invoked when the parser has reported a {@code fatalError()} or thrown an exception.</strong></p>
         *
         * <p>This corresponds to the definition of "fatal error" in section 1.2 of the W3C XML 1.0 Recommendation.
         * For example, a parser would use this callback to report the violation of a well-formedness constraint.</p>
         *
         * <p>The application must assume that the document is unusable after the parser has invoked this method, and
         * should continue (if at all) only for the sake of collecting additional error messages: SAX parsers are free
         * to stop reporting any other events once this method has been invoked.</p>
         *
         * @param spe
         *          The error information encapsulated in a {@link SAXParseException}.
         * @throws SAXException
         *         Any SAX exception, possibly wrapping another exception.
         * @see org.xml.sax.SAXParseException
         */
        @Override
        public void fatalError(final SAXParseException spe) throws SAXException {
            System.out.println("ERROR: " + spe.toString());
            throw spe;
        }
    }

    /**
     * Default constructor.
     */
    public XmlValidator() {
        super();
    }

    /**
     * Validate the supplied XML file against the supplied Schema file.
     * @param xmlFile
     *          The XML file to validate. Must be a resource in the classpath.
     * @param schemaFile
     *          The Schema file to validate XML file against. Must be a resource in the classpath.
     * @throws RuntimeException
     *          If validation fails.
     */
    public void validate(final String xmlFile, final String schemaFile) {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            schemaFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            final Schema schema = schemaFactory.newSchema(getResource(schemaFile));

            final Validator validator = schema.newValidator();
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            validator.setErrorHandler(new ValidationErrorHandler());

            validator.validate(new StreamSource(getResource(xmlFile).toExternalForm()), null);
        }
        catch (final SAXException | IOException e) {
            final String message = "ERROR: Failed to validate " + xmlFile +
                                   " against the schema in " + schemaFile + ". The error is " + e.toString();
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Get a resource on the classpath.
     * @param resourceName
     *          The name of the resource to get.
     * @return
     *          The URL of the resource, if it i on the classpath.
     * @throws IOException
     *          If the resource is not on the classpath.
     */
    private URL getResource(final String resourceName) throws IOException {
        final URL resource = Thread.currentThread().getContextClassLoader().getResource(resourceName);

        if (resource == null) {
            throw new FileNotFoundException("The " + resourceName + " resource is not on the classpath.");
        }

        return resource;
    }
}
