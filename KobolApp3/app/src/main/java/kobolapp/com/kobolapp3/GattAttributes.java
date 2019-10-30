package kobolapp.com.kobolapp3;


import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class GattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String ACCELERATION_MEASUREMENT = "ad7d802c-e80e-11e9-81b4-2a2ae2dbcce4";
    public static String GYROSCOPE_MEASUREMENT = "ad7d8176-e80e-11e9-81b4-2a2ae2dbcce4";
    public static String TEMPERATURE_MEASUREMENT = "ad7d82ac-e80e-11e9-81b4-2a2ae2dbcce4";
    public static String HUMIDITY_MEASUREMENT = "ad7d83e2-e80e-11e9-81b4-2a2ae2dbcce4";

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("ad7d7c6c-e80e-11e9-81b4-2a2ae2dbcce4", "Sleep Monitor Service");

        // Sample Characteristics.
        attributes.put(ACCELERATION_MEASUREMENT, "Acceleration");
        attributes.put(GYROSCOPE_MEASUREMENT, "Gyroscope");
        attributes.put(TEMPERATURE_MEASUREMENT, "Temperature");
        attributes.put(HUMIDITY_MEASUREMENT, "Humidity");

    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}