package me.titan.customcommands.logger;

import me.titan.customcommands.configurations.Config;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.TimeUtil;

import java.io.*;

public class Logger {

	private static Logger logger = new Logger("log.log");

	boolean debug_mode = false;
	File f;

	public Logger(String fileName) {

		f = FileUtil.getFile(fileName);

		if (!debug_mode) {
			debug_mode = Config.DEBUG_MODE;
		}


	}

	public static void log(boolean gg, String... messages) {
		if (Config.LAG) return;
		Logger log = Logger.logger;
		String date = TimeUtil.getFormattedDateShort();
		for (String msg : messages) {
			String nmsg = date + "  :  " + msg;

			log.write(nmsg);
			if (log.debug_mode)
				Common.log(msg);
		}


	}

	public static void log(String source, String... messages) {
		if (Config.LAG) return;
		Logger log = Logger.logger;

		String date = TimeUtil.getFormattedDateShort();
		for (String msg : messages) {
			String nmsg = "[" + date + "] (" + source + ")  : " + msg;

			log.write(nmsg);
			if (log.debug_mode)
				Common.log("[" + source + "] " + msg);
		}


	}

	public void write(String... logs) {
//		FileWriter fr = null;
//		BufferedWriter br = null;
//		try {
//			fr = new FileWriter(f);
//
//		br = new BufferedWriter(fr);
		for (String log : logs) {
//			String dataWithNewLine = log + System.getProperty("line.separator");
//			try {
//
//				for (int i = logs.length; i > 0; i--) {
//					br.write(dataWithNewLine);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//
//			}
//		}
//		} catch (IOException e) {
//			e.printStackTrace();
//			} finally {
//
//
//			try {
//				br.close();
//				fr.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
			try {
				OutputStream out = new FileOutputStream(f);
				try {
					out.write(log.getBytes(), 0, log.length());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
}
