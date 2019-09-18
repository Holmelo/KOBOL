package com.example.sleep_monitor_app;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class GattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String ACCELERATION_MEASUREMENT = "917649A1-D98E-11E5-9EEC-0002A5D5C51B";
    public static String GYROSCOPE_MEASUREMENT = "917649A2-D98E-11E5-9EEC-0002A5D5C51B";

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("917649A0-D98E-11E5-9EEC-0002A5D5C51B", "Sleep Monitor Service");

        // Sample Characteristics.
        attributes.put(ACCELERATION_MEASUREMENT, "Acceleration");
        attributes.put(GYROSCOPE_MEASUREMENT, "Gyroscope");

    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}