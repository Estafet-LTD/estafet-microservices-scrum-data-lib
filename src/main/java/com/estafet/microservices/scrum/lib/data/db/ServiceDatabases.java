package com.estafet.microservices.scrum.lib.data.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.io.Resources;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias(value = "services")
public class ServiceDatabases {
	
	@XStreamImplicit
	private List<ServiceDatabase> serviceDatabases;

	public List<ServiceDatabase> getDatabases() {
		return serviceDatabases;
	}
	
	private ServiceDatabase getDatabase(String service) {
		for (ServiceDatabase db : getDatabases()) {
			if (db.getName().equals(service)) {
				return db;
			}
		}
		return null;
	}

	public static void clean() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(Resources.getResource("services.xml").openStream()));
			XStream xStream = new XStream();
			xStream.processAnnotations(ServiceDatabases.class);
			ServiceDatabases serviceDatabases = (ServiceDatabases) xStream.fromXML(reader);
			for (ServiceDatabase serviceDatabase : serviceDatabases.getDatabases()) {
				serviceDatabase.init();
				serviceDatabase.clean();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static boolean exists(String service, String table, String key, Integer value) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(Resources.getResource("services.xml").openStream()));
			XStream xStream = new XStream();
			xStream.processAnnotations(ServiceDatabases.class);
			ServiceDatabases serviceDatabases = (ServiceDatabases) xStream.fromXML(reader);
			ServiceDatabase serviceDatabase = serviceDatabases.getDatabase(service);
			serviceDatabase.init();
			return serviceDatabase.exists(table, key, value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
}
