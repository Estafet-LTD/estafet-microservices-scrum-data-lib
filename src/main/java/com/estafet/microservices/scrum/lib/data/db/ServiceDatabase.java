package com.estafet.microservices.scrum.lib.data.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.io.Resources;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias(value = "service")
public class ServiceDatabase {

	@XStreamAlias(value = "name")
	private String name;

	@XStreamAlias(value = "db-url-env")
	private String dbURLEnvVariable;

	@XStreamAlias(value = "db-user-env")
	private String dbUserEnvVariable;

	@XStreamAlias(value = "db-password-env")
	private String dbPasswordEnvVariable;

	@XStreamOmitField
	Connection connection;

	@XStreamOmitField
	Statement statement;

	public String getName() {
		return name;
	}

	public String getDbURL() {
        return getEnvironmentVariable(dbURLEnvVariable);
	}

	public String getDbUser() {
		return getEnvironmentVariable(dbUserEnvVariable);
	}

	public String getDbPassword() {
		return getEnvironmentVariable(dbPasswordEnvVariable);
	}

	public void init() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(getDbURL(), getDbUser(), getDbPassword());
			statement = connection.createStatement();
		} catch (final ClassNotFoundException | SQLException e) {
			throw new RuntimeException("Failed to initialise database.", e);
		}
	}

	public boolean exists(final String table, final String key, final Integer value) {
        final String sqlselect = "select " + key + " from " + table + " where " + key + " = " + value;
		try {
			return statement.executeQuery(sqlselect).next();
		} catch (final SQLException e) {
			throw new RuntimeException("SQL statement [" + sqlselect + "] failed.", e);
		} finally {
			close();
		}
	}

	public void clean() {
		try {
			executeDDL("drop", statement);
			executeDDL("create", statement);
			System.out.println("Successfully cleaned " + name + ".");
		} finally {
			close();
		}
	}

	public void close() {
	    close(statement);

        close(connection);
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
        final Optional<AutoCloseable> optional = Optional.ofNullable(resource);
        optional.ifPresent(closeable -> {
            try {
                closeable.close();
            } catch (final Exception e) {
                System.out.println("Warning: closing " + resource.toString() + " failed: " + e.toString() + ".");
            }
        });
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
    private String getEnvironmentVariable(final String variableName) {
        final Optional<String> value = Optional.ofNullable(System.getenv(variableName));
        return value.orElseThrow(() ->
            new IllegalStateException("The " + variableName + " environment variable is not set."));
    }

	private void executeDDL(final String prefix, final Statement statement) {
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

}
