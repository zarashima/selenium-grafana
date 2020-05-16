package utils;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

public class UpdateResults {
	private static final InfluxDB INFLUXDB = InfluxDBFactory.connect("http://localhost:8086", "admin", "");
	private static final String DB_NAME = "selenium_test_results";

	static {
		INFLUXDB.setDatabase(DB_NAME);
	}

	public static void post(final Point point) {
		INFLUXDB.write(point);
	}
}
