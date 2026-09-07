package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to read framework configurations and test data properties files.
 * Supports JVM System property overrides (e.g. -Dbrowser=chrome).
 */
public class ConfigReader {

    private static final Properties configProperties = new Properties();
    private static final Properties testDataProperties = new Properties();

    static {
        loadProperties(configProperties, "src/test/resources/config.properties", "/config.properties");
        loadProperties(testDataProperties, "src/test/resources/testdata/registration-data.properties", "/testdata/registration-data.properties");
    }

    private static void loadProperties(Properties properties, String filePath, String classpathResource) {
        // First try loading via file system
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
            return;
        } catch (IOException ignored) {
            // Fallback to classpath resource loading
        }

        try (InputStream is = ConfigReader.class.getResourceAsStream(classpathResource)) {
            if (is != null) {
                properties.load(is);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load properties resource: " + classpathResource + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Get property value by key from config.properties.
     * Checks System properties first for CLI parameter overriding.
     *
     * @param key Property key name
     * @return Property value, or null if not found
     */
    public static String getProperty(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.trim().isEmpty()) {
            return sysProp.trim();
        }
        String val = configProperties.getProperty(key);
        return val != null ? val.trim() : null;
    }

    /**
     * Get property value with a fallback default.
     *
     * @param key          Property key name
     * @param defaultValue Default value if key is not found
     * @return Property value or defaultValue
     */
    public static String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return val != null ? val : defaultValue;
    }

    /**
     * Get test data property by key from registration-data.properties.
     *
     * @param key Test data property key
     * @return Test data property value
     */
    public static String getTestData(String key) {
        String val = testDataProperties.getProperty(key);
        return val != null ? val.trim() : null;
    }

    /**
     * Helper to get integer configuration value.
     *
     * @param key          Property key
     * @param defaultValue Default integer value
     * @return Parsed integer
     */
    public static int getIntProperty(String key, int defaultValue) {
        String val = getProperty(key);
        if (val == null) return defaultValue;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Helper to get boolean configuration value.
     *
     * @param key          Property key
     * @param defaultValue Default boolean value
     * @return Parsed boolean
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String val = getProperty(key);
        if (val == null) return defaultValue;
        return Boolean.parseBoolean(val);
    }
}
