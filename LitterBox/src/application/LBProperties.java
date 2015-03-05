package application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import application.exception.LBException;

public class LBProperties {

	// Constants ----------------------------------------------------------------------------------

	private static final String PROPERTIES_FILE = "config.properties";
	private static final Properties PROPERTIES = new Properties();

	static {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream propertiesFile = classLoader.getResourceAsStream(PROPERTIES_FILE);

		if (propertiesFile == null) {
			throw new LBException(
					"Properties file '" + PROPERTIES_FILE + "' is missing in classpath.");
		}

		try {
			PROPERTIES.load(propertiesFile);
		} catch (IOException e) {
			throw new LBException(
					"Cannot load properties file '" + PROPERTIES_FILE + "'.", e);
		}
	}

	// Vars ---------------------------------------------------------------------------------------

	private String specificKey;

	// Constructors -------------------------------------------------------------------------------

	/**
	 * Construct a LBProperties instance for the given specific key which is to be used as property
	 * key prefix of the LB properties file.
	 * @param specificKey The specific key which is to be used as property key prefix.
	 * @throws LBException During class initialization if the LB properties file is
	 * missing in the classpath or cannot be loaded.
	 */
	public LBProperties(String specificKey) throws LBException {
		this.specificKey = specificKey;
	}

	// Actions ------------------------------------------------------------------------------------

	/**
	 * Returns the LBProperties instance specific property value associated with the given key with
	 * the option to indicate whether the property is mandatory or not.
	 * @param key The key to be associated with a DAOProperties instance specific value.
	 * @param mandatory Sets whether the returned property value should not be null nor empty.
	 * @return The LBProperties instance specific property value associated with the given key.
	 * @throws LBException If the returned property value is null or empty while
	 * it is mandatory.
	 */
	public String getProperty(String key, boolean mandatory) throws LBException {
		String fullKey = specificKey + "." + key;
		String property = PROPERTIES.getProperty(fullKey);

		if (property == null || property.trim().length() == 0) {
			if (mandatory) {
				throw new LBException("Required property '" + fullKey + "'"
						+ " is missing in properties file '" + PROPERTIES_FILE + "'.");
			} else {
				// Make empty value null. Empty Strings are evil.
				property = null;
			}
		}

		return property;
	}

}
