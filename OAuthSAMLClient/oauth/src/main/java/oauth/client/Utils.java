package main.java.oauth.client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

public class Utils {

	public static final String ASSERTION_TEMPLATE = "saml_bearer_assertion.xml";
	public static final String PROPERTIES_FILE = "settings.properties";

	public static final String NAME_ID_TYPE_EMAIL = "email";
	public static final String NAME_ID_TYPE_USERID = "user_id";

	private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	private static final int ASSERTION_NOT_BEFORE_MINUTES = -5;
	private static final int ASSERTION_NOT_ON_OR_AFTER_MINUTES = 10;
	private static final int SESSION_NOT_ON_OR_AFTER_HOURS = 12;
	private static Properties properties;

	static {
		DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			properties = readConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDateAndTime() {
		Date date = new Date();
		return DATE_TIME_FORMAT.format(date);
	}

	public static String getNotBeforeDateAndTime() {
		Calendar beforeCal = Calendar.getInstance();
		beforeCal.add(Calendar.MINUTE, ASSERTION_NOT_BEFORE_MINUTES);
		return DATE_TIME_FORMAT.format(beforeCal.getTime());
	}

	public static String getNotOnOrAfterDateAndTime() {
		Calendar afterCal = Calendar.getInstance();
		afterCal.add(Calendar.MINUTE, ASSERTION_NOT_ON_OR_AFTER_MINUTES);
		return DATE_TIME_FORMAT.format(afterCal.getTime());
	}

	public static String getSessionNotOnOrAfterDateAndTime() {
		Calendar afterCal = Calendar.getInstance();
		afterCal.add(Calendar.HOUR, SESSION_NOT_ON_OR_AFTER_HOURS);
		return DATE_TIME_FORMAT.format(afterCal.getTime());
	}

	public static String generateResponseId() {
		return UUID.randomUUID().toString();
	}

	public static String getUtf8Charset() {
		return "UTF-8";
	}

	public static String getAssertionTemplate() throws IOException {
		String s = IOUtils.toString(Utils.class
				.getResourceAsStream(ASSERTION_TEMPLATE));
		return s;
	}

	public static String fileToString(String file) {
		String result = null;
		DataInputStream in = null;

		try {
			File f = new File(file);
			byte[] buffer = new byte[(int) f.length()];
			in = new DataInputStream(new FileInputStream(f));
			in.readFully(buffer);
			result = new String(buffer, Utils.getUtf8Charset());
		} catch (IOException e) {
			throw new RuntimeException("Error..", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) { /* skip it */
			}
		}
		return result;
	}

	public static void printStackTrace() {
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			System.out.print(ste);
		}
	}

	public static String getCliendId() {
		return properties.getProperty("CLIENT_ID");
	}

	public static String getIssuer() {
		return properties.getProperty("ISSUER");
	}

	public static String getNameId() {
		return properties.getProperty("NAME_ID");
	}

	public static String getEntityId() {
		return properties.getProperty("ENTITY_ID");
	}

	public static String getRefId() {
		return "_" + UUID.randomUUID().toString();
	}

	public static String getTokenServiceUrl() {
		return properties.getProperty("TOKEN_SERVICE_URL");
	}

	public static String getPrivateKeyAlias() {
		return properties.getProperty("PRIVATE_KEY_ALIAS");
	}

	public static String getPrivateKeyPassword() {
		return properties.getProperty("PRIVATE_KEY_PASS");
	}

	public static String getKeyStorePassword() {
		return properties.getProperty("KEY_STORE_PASS");
	}

	public static String getClientSecret() {
		return properties.getProperty("CLIENT_SECRET");
	}

	private static Properties readConfig() throws IOException {
		Properties result = new Properties();

		InputStream is = Utils.class.getResourceAsStream(PROPERTIES_FILE);

		if (is != null) {
			result.load(is);
		} else {
			throw new FileNotFoundException("Property file '" + PROPERTIES_FILE
					+ "' not found in classpath");
		}
		return result;
	}
}
