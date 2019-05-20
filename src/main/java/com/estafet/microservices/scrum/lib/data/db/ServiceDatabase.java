package com.estafet.microservices.scrum.lib.data.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import com.estafet.microservices.scrum.lib.utils.OsUtils;
import com.google.common.io.Resources;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Class to represent a database service.
 *
 * <p>The XStream library is used to create instances of this class from an xml file.</p>
 *
 * <p>The {@link #readResolve()} method ensures that only fully populated instances of the class are created and that
 * the environment names are valid for the operating system.</p>
 *
 * <p><strong>This class' use of the {@link #connection} and {@link #statement} fields is not thread safe.</strong></p>
 *
 * @author Dennis Williams, Estafet Ltd.
 *
 */
@XStreamAlias(value = "service")
public class ServiceDatabase {

    /**
     * The regular expression for a valid environment variable name.
     */
    private static final Pattern VALID_ENVIRONMENT_VARIABLE_NAME_PATTERN;

    static {
        String patternString;
        if (OsUtils.isWindows()) {
            // Start with an upper case letter or an underscore. Follow with one or more upper case letters, digits or
            // underscores.
            patternString = "^[A-Z_][A-Z0-9_]+$";
        }
        else {
            // Assume Linux and MacOS are the same. See https://www/tutorialspoint.com/unix/unix-using-variables.htm
            patternString = "^[A-Za-z_][A-Za-z0-9_]+$";
        }

        try {
            final Pattern pattern = Pattern.compile(patternString);
            VALID_ENVIRONMENT_VARIABLE_NAME_PATTERN = pattern;
        }
        catch (final PatternSyntaxException pse) {
            final ExceptionInInitializerError eiie =
                            new ExceptionInInitializerError("Failed to compile" + patternString + ".");
            eiie.initCause(pse);
            throw eiie;
        }
    }

	/**
     * The name of the environment variable that holds the database user's password.
     */
	@XStreamAlias(value = "db-password-env")
	private String dbPasswordEnvVariable;

	/**
	 * The name of the environment variable that holds the database URL.
	 */
	@XStreamAlias(value = "db-url-env")
	private String dbURLEnvVariable;

    /**
     * The name of the environment variable that holds the database user to connect with.
     */
	@XStreamAlias(value = "db-user-env")
	private String dbUserEnvVariable;

    /**
	 * The name of the service
	 */
	@XStreamAlias(value = "name")
	private String name;

	/**
	 * The database connection object.
	 *
	 * <p><strong>Access to this field is not safe.</strong></p>
	 */
	@XStreamOmitField
	Connection connection;

    /**
     * The database statement object.
     *
     * <p><strong>Access to this field is not safe.</strong></p>
     */
	@XStreamOmitField
	Statement statement;

	/**
	 * Drop the database and then create it.
	 */
	public void clean() {
		try {
	        init();
			executeDDL("drop");
			executeDDL("create");
			System.out.println("Successfully cleaned " + name + ".");
		} finally {
			close();
		}
	}


    /**
     * Execute the query to verify that the service databas exists.
     * @param table
     *          The name of the table.
     * @param key
     *          The column to read
     * @param value
     *          The value of the column to select
     * @return
     *          {@code true} if the query is successful. {@code false} otherwise.
     *
     * @throws RuntimeException
     *          If an error occurs executing the query.
     */
    public boolean exists(final String table, final String key, final Integer value) {
        final String sqlselect = "select " + key + " from " + table + " where " + key + " = " + value;
		try {
	        init();
			return statement.executeQuery(sqlselect).next();
		} catch (final SQLException e) {
			throw new RuntimeException("SQL statement [" + sqlselect + "] failed.", e);
		} finally {
			close();
		}
	}

    /**
     * @return
     *         The database user's password.
     * @throws IllegalStateException
     *         If the environment variable does not exist.
     */
	public String getDbPassword() throws IllegalStateException {
		return getEnvironmentVariable(dbPasswordEnvVariable);
	}

    /**
     * @return
     *         The database connection URL.
     * @throws IllegalStateException
     *         If the environment variable does not exist.
     */
	public String getDbURL() throws IllegalStateException {
        return getEnvironmentVariable(dbURLEnvVariable);
	}

	/**
     * @return
     *         The database user to connect with.
     * @throws IllegalStateException
     *         If the environment variable does not exist.
     */
	public String getDbUser() throws IllegalStateException {
		return getEnvironmentVariable(dbUserEnvVariable);
	}

	/**
	 * @return
	 *         The service name.
	 */
	public String getName() {
		return name;
	}

    /**
     * Check the field is present and correct and that environment variable name is valid.
     *
     * <p>Fields cannot be null, empty, blank or have leading or trailing whitespace characters.</p>
     *
     * @param messagePrefix
     *          The message prefix.
     * @param fieldValue
     *          The value to check.
     * @throws InvalidObjectException
     *          If the field is invalid in some way.
     */
    private void checkEnvironmentVariableName(final String messagePrefix,
                                              final String fieldValue) throws InvalidObjectException {
        checkField(messagePrefix, fieldValue);

        // The field is present - check the environment variable name is valid.

        final Matcher matcher = VALID_ENVIRONMENT_VARIABLE_NAME_PATTERN.matcher(fieldValue);
        if (!matcher.matches()) {
            throw new InvalidObjectException(messagePrefix +
                                             " is not a valid environment variable name [" + fieldValue + "].");
        }
    }

	/**
     * Check the field is present and correct.
     *
     * <p>Fields cannot be null, empty, blank or have leading or trailing whitespace characters.</p>
     *
     * @param messagePrefix
     *          The message prefix.
     * @param fieldValue
     *          The value to check.
     * @throws InvalidObjectException
     *          If the field is invalid in some way.
     */
    private void checkField(final String messagePrefix, final String fieldValue) throws InvalidObjectException {

        if (fieldValue == null) {
            throw new InvalidObjectException(messagePrefix + "is missing from the xml file.");
        }

        if (fieldValue.length() == 0) {
            throw new InvalidObjectException(messagePrefix + "is empty.");
        }

        final String trimmedValue = fieldValue.trim();

        if (trimmedValue.length() == 0) {
            throw new InvalidObjectException(messagePrefix + "is blank.");
        }

        // Intentional use of "!=" - String.trim() returns a different object iff the string is trimmed.
        if (trimmedValue != fieldValue) {
            throw new InvalidObjectException(messagePrefix + "has either leading or trailing whitespace characters.");
        }
    }

	/**
	 * Close the connection and statement.
	 */
	private  void close() {
	    close(statement);
	    statement =  null;

        close(connection);
        connection = null;
	}

	/**
	 * Close a resource.
	 *
	 * <p>This method must not throw an {@code Exception} because it is called from within a {@code finally} block.</p>
	 *
	 * @param resource
	 *             The resource to close. Can be {@code null}.
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
	 * Execute the statements in a DDL file.
	 * @param prefix
	 *         The action ({@code drop} or {@code create}).
	 * @throws RuntimeException
	 *         IF an SQL error occurs.
	 */
	private void executeDDL(final String prefix) {
		for (final String stmt : getStatements(prefix + "-" + name + "-db.ddl")) {
            final String sqlStatement = stmt.replaceAll("\\;", "");
			try {
                statement.executeUpdate(sqlStatement);
			} catch (final SQLException e) {
				if (prefix.equals("create")) {
					throw new RuntimeException("Create statement [" + sqlStatement + "] failed.", e);
				}
				System.out.println("Warning - statement [" + sqlStatement + "] failed: " + e.getMessage());
			}
		}
	}

	/**
     * Get the value of an environment variable.
     *
     * @param variableName
     *          The name of the environment variable.
     * @return
     *          The value of the environment variable, if it is set.
     * @throws IllegalStateException
     *          If the environment variable is not set.
     */
    private String getEnvironmentVariable(final String variableName) throws IllegalStateException {
        return Optional.ofNullable(System.getenv(variableName)).orElseThrow(() ->
            new IllegalStateException("The " + variableName + " environment variable is not set."));
    }

	/**
	 * Get the statements in a DDL file.
	 * @param filename
	 *         The filename. The file must be on the classpath.
	 * @return
	 *         The list of SQL statements.
	 * @throws RuntimeException
	 *         When an error occurs reading the DDL file.
	 */
	private List<String> getStatements(final String filename) {
		BufferedReader reader = null;
		URL resource = null;
		try {
		    // Never returns null.
			resource = Resources.getResource(filename);
            reader = new BufferedReader(new InputStreamReader(resource.openStream()));
			return reader.lines().collect(Collectors.toList());
		} catch (final IOException e) {
			throw new RuntimeException("Failed to read " + filename + ". Resource is " + resource.toString() + ".", e);
		} finally {
		    close(reader);
		}
	}

    /**
	 * Initialise the {@link Connection} and {@link Statement}.
	 *
	 * <p><strong>This method is not thread safe.</strong></p>
	 */
	private void init() {

	    // Do not initialise more than once.
	    if (connection != null && statement != null) {
	        return;
	    }

        loadJDBCDriver("org.postgresql.Driver");

        final String dbURL = getDbURL();
        final String dbUser = getDbUser();

		try {
            connection = DriverManager.getConnection(dbURL, dbUser, getDbPassword());
			statement = connection.createStatement();
		} catch (final SQLException se) {
			throw new RuntimeException("Failed to connect to the " +
		                               getName() + " database at \"" +
		                               dbURL +
		                               "\" with the username \"" +
		                               dbUser +
		                               "\".", se);
		}
	}

    /**
	 * Load the specified JDBC driver class.
	 * <p>This method allows a specific message to be logged if the JDBC driver is not on the class path.</p>
	 * @param driverClassName
	 *         The binary class name for the JDBC driver.
	 *
	 * @return
	 *         The {@link Class} for the JDBC driver for unit testing.
	 */
	private Class<?> loadJDBCDriver(final String driverClassName) {
        try {
            final Class<?> driverClass = Thread.currentThread()
                                               .getContextClassLoader()
                                               .loadClass(driverClassName);
            return driverClass;
        }
        catch (final ClassNotFoundException cnfe) {
            throw new RuntimeException("Unable to load JDBC driver class: " + driverClassName, cnfe);
        }
    }

    /**
     * Verify that all fields are present.
     *
     * <p>The <a href="https://x-stream.github.io/">XStream</a> library offers no means to validate that all fields are
     * defined correctly in the xml file.</p>
     *
     * <p>Apart from implementing a custom converter class, the only way of checking for missing fields is to implement
     * this method, which is called during the deserialisation process.</p>.
     *
     * <p>Implementing this method is simpler than implementing a custom converter class.</p>
     *
     * @return
     *          {@code this}.
     * @throws InvalidObjectException
     *          If any value is missing from the xml file, or is incorrect.
     */
    private Object readResolve() throws InvalidObjectException {
        checkField("The database name field ", name);
        checkEnvironmentVariableName("The " + name + " database URL environment variable field ", dbURLEnvVariable);
        checkEnvironmentVariableName("The " + name + " database user environment variable field ", dbUserEnvVariable);
        checkEnvironmentVariableName("The " + name + " database password environment variable field ",
                                     dbPasswordEnvVariable);

        connection = null;
        statement = null;
        return this;
    }
}
