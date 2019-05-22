package com.estafet.microservices.scrum.lib.data.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import com.google.common.io.Resources;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Class to handle service databases.
 *
 * @author Dennis Williams, Estafet Ltd.
 *
 */
@XStreamAlias(value = "services")
public class ServiceDatabases {

	/**
	 * The name of the XML schema file that is used to validate the services configuration file.
	 */
	private static final String SERVICES_SCHEMA_FILE = "services.xsd";

    /**
     * The name of the services configuration file.
     */
    private static final String SERVICES_XML_FILE = "services.xml";

    /**
     * Drop and create the database for each service.
     */
    public static void clean() {

	    // Make sure the services.xml file is valid.
	    validateServicesFile(SERVICES_XML_FILE);

	    // Create a ServiceDatabase object from the services XML file.
	    final ServiceDatabases serviceDatabases =  createServiceDatabases();

	    // Drop and create each service database.
        for (final ServiceDatabase serviceDatabase : serviceDatabases.getDatabases()) {
            serviceDatabase.clean();
        }
	}

	/**
	 * Check that a particular database exists.
	 * @param service
	 *         The name of the service.
	 * @param table
	 *         The name of the table to query.
	 * @param key
	 *         The column to query.
	 * @param value
	 *         The value to query.
	 * @return
	 *         {@code true} if the database query is successful, indicating that the service database exists.
	 */
	public static boolean exists(final String service, final String table, final String key, final Integer value) {
		final ServiceDatabases serviceDatabases = createServiceDatabases();
		final ServiceDatabase serviceDatabase = serviceDatabases.getDatabase(service);
		return serviceDatabase.exists(table, key, value);
    }

	/**
	 * Close a resource.
	 * @param resource
	 *         The resource to close. Can be {@code null}.
	 */
	private static void close(final AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (final Exception e) {
                System.out.println("ERROR: Failed to close " + resource + ". Error is " + e.toString());
            }
        }
    }

	/**
	 * Create the service databases that are described in the {@link #SERVICES_XML_FILE} file.
	 *
	 * <p>This method uses the XStream library to create {@link ServiceDatabases} object from the
	 * {@link #SERVICES_XML_FILE} file. This object contains a list of {@link ServiceDatabase} objects, each of which
	 * allows JDBC connections to a specific service database.</p>
	 *
	 * <p>This method expects that {@link #validateServicesFile()} has been called.</p>
	 *
	 * @return
	 *         The {@link ServiceDatabases} object.
	 * @throws RuntimeException
	 *         If an error occurs.
	 */
	@SuppressWarnings("javadoc")
    private static ServiceDatabases createServiceDatabases() {
        BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(Resources.getResource(SERVICES_XML_FILE).openStream()));
			final XStream xStream = new XStream();
			xStream.processAnnotations(ServiceDatabases.class);
			final ServiceDatabases serviceDatabases = (ServiceDatabases) xStream.fromXML(reader);
			return serviceDatabases;
		} catch (final Exception e) {
			throw new RuntimeException("Failed to create the service databases from " + SERVICES_XML_FILE + ".", e);
		} finally {
		    close(reader);
		}
    }

    /**
     * Validate the {@link #SERVICES_XML_FILE} file against the {@link #SERVICES_SCHEMA_FILE} file.
     * @param servicesFile
     *          The name of the services file. Providing this as a parameter makes testing much easier.
     *
     * @throws RuntimeException
     *          If the validation fails.
     *
     */
    private static void validateServicesFile(final String servicesFile) {
	    new XmlValidator().validate(servicesFile, SERVICES_SCHEMA_FILE);
    }

    /**
     * The list of service databases.
     */
    @XStreamImplicit
	private List<ServiceDatabase> serviceDatabases;

	/**
	 * @return
	 *         A unmodifiable list of service databases.
	 */
	public List<ServiceDatabase> getDatabases() {
		return Collections.unmodifiableList(serviceDatabases);
	}

    /**
     * Get the {@link ServiceDatabase} for the specified service.
     * @param service
     *          The name of the service.
     * @return
     *          The {@link ServiceDatabase} object for the service.
     * @throws IllegalStateException
     *          If there is no service database configured for the named service.
     */
    private ServiceDatabase getDatabase(final String service) throws IllegalStateException {
		for (final ServiceDatabase db : getDatabases()) {
			if (db.getName().equals(service)) {
				return db;
			}
		}
		throw new IllegalStateException("The database for service \"" + service + " is not defined.");
	}
}
