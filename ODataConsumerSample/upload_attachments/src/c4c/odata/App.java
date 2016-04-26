package c4c.odata;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/*
 * Please use the data appropriate for your scenario in the calls below !!
 */
public class App {
	private static final Logger logger = Logger.getLogger(App.class.getName());

	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;
	static String logFilename = "";

	private static byte[] readAttachmentFile(String fileName)
			throws IOException {
		Path path = Paths.get(fileName);
		return Files.readAllBytes(path);
	}

	private static boolean initApp(String[] args) throws IOException {

		// Read the settings properties
		try {
			AccountAttachmentODataConsumer.readSettings();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Process command-line arguments
		Options options = new Options();
		String command = "java -jar schippers.jar schippers.odata.App";
		String header = "\nUploads the attachments into C4C accounts based on the provided \"manifest.csv\" file.\n\n";
		String footer = "Required C4C Tenant URL must be provided in \"settings.properties\".\n\n";
		try {

			options.addOption(OptionBuilder.withLongOpt("password")
                    .withDescription("C4C Password (Required argument)")
                    .hasArg()
                    .withArgName("password")
                    .isRequired()
                    .create('p'));

			options.addOption(OptionBuilder.withLongOpt("username")
                    .withDescription("C4C Username. (also available in \"settings.properties\")")
                    .hasArg()
                    .withArgName("username")
                    .isRequired(false)
                    .create('u'));
			
			options.addOption(OptionBuilder.withLongOpt("folder")
                    .withDescription("Folder containing attachments and \"manifest.csv\". (also available in \"settings.properties\")")
                    .hasArg()
                    .withArgName("attachmentsFolder")
                    .isRequired(false)
                    .create('f'));
			
			BasicParser parser = new BasicParser();
			CommandLine cl = parser.parse(options, args);

			if (cl.hasOption('h')) {
				HelpFormatter f = new HelpFormatter();
				f.printHelp(command, 
						header, 
						options, 
						footer, 
						true);
				 
				return false;
			} else {
				// Assign command-line arguments
				if (cl.getOptionValue("u") != null)
					AccountAttachmentODataConsumer.setUsername(cl
							.getOptionValue("u"));

				if (cl.getOptionValue("p") != null)
					AccountAttachmentODataConsumer.setPassword(cl
							.getOptionValue("p"));

				if (cl.getOptionValue("f") != null)
					AccountAttachmentODataConsumer
							.setAttachmentsFolder(cl.getOptionValue("f"));

				// Check required parameters
				if ((AccountAttachmentODataConsumer
						.getAttachmentsFolder() == null)
						|| (AccountAttachmentODataConsumer
								.getAttachmentsFolder().length() == 0)) {
					logger.severe("Attachments folder is missing");
					return false;
				}

				if ((AccountAttachmentODataConsumer.getPassword() == null)
						|| (AccountAttachmentODataConsumer.getPassword()
								.length() == 0)) {
					logger.severe("Password is missing");
					return false;
				}

				if ((AccountAttachmentODataConsumer.getUsername() == null)
						|| (AccountAttachmentODataConsumer.getUsername()
								.length() == 0)) {
					logger.severe("Username is missing");
					return false;
				}
			}
		} catch (ParseException e) {
			HelpFormatter f = new HelpFormatter();
			f.printHelp(command, 
					header, 
					options, 
					footer, 
					true);
			return false;
		}

		// Set log level
		logger.setLevel(Level.INFO);
		String filename = new Date().toString();
		
		filename = filename.replace(":", "_");

		logFilename = "Logging_" + filename + ".log";
		fileTxt = new FileHandler(logFilename);

		// set Log Level
		fileTxt.setLevel(Level.INFO);

		// create a TXT formatter
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);

		return true;

	}

	public static void main(String[] args) throws Exception {
		// Create Consumer instance and get csrfToken
		AccountAttachmentODataConsumer consumer = new AccountAttachmentODataConsumer();

		//
		if (!initApp(args))
			return;

		logger.info(">>>>>>>>>> Uploading of attachments has started. <<<<<<<<<<<<<");

		// Open the CSV file for reading
		String inputFileName = AccountAttachmentODataConsumer.getAttachmentsFolder()
				+ "/manifest.csv";

		File inputFile = new File(inputFileName);

		if (!inputFile.canRead())
			throw new RuntimeException("Can't read input: " + inputFileName);

		CSVParser inParser = CSVParser.parse(inputFile, StandardCharsets.UTF_8,
				CSVFormat.EXCEL.withHeader());

		Attachment newAttachment = null;
		int count = 0;
		int failed = 0;
		// Process the entries in the CSV file
		for (CSVRecord record : inParser) {
			newAttachment = new Attachment();

			newAttachment.set_Name(record.get("AttachmentName"));
			newAttachment.set_ParentObjectID(record
					.get("ParentObjectID"));
			newAttachment.set_MimeType(record.get("MimeType"));
			newAttachment.set_TypeCode(record.get("TypeCode"));
			newAttachment.set_Binary(readAttachmentFile(
					AccountAttachmentODataConsumer.getAttachmentsFolder() +
					"/" + record.get("Path")));
			newAttachment.setFileName(record.get("Path"));

			Optional<String> errorMessage = consumer
					.createAttachment(newAttachment);

			if (errorMessage.isPresent()) {
				logger.severe(errorMessage.get());
				failed++;
			}
			else {
				logger.info(
						"[ParentObjectID:" + record.get("ParentObjectID") + "|" +
						"MimeType:" + record.get("MimeType") + "|" +
						"TypeCode:" + record.get("TypeCode") + "|" +
						"AttachmentName:" +	record.get("AttachmentName") + "|" +
						"Filename:" +record.get("Path") + "] uploaded successfully."
						);
			}

			count++;
		}
		inParser.close();

		logger.info(">>>>>>>>>>> A total of " + count
				+ " attachments have been processed. <<<<<<<<<");
		if (failed > 0) {
			logger.info(failed
					+ " attachment"
					+ (failed > 1 ? "s" : "")
					+ " failed to upload. See details, above. (or in the log file: "
					+ logFilename + ")");
		}

	}
}
