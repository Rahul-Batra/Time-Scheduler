package readUrl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ReadSchedule {
	public class SimpleThread extends Thread {
		public SimpleThread() {
		}

		@Override
		public void run() {
			while(true)
			{
			try {
				urlfile();				
			} catch (IOException e) {
				Thread.currentThread().interrupt();
			}
		}
		}
	};

	public static void main(String[] args)  {	
		ReadSchedule rd=new ReadSchedule();
		SimpleThread newthread=rd.new SimpleThread();
		newthread.start();
	}
	void urlfile() throws IOException{
		File sechduletime = new File(
				"urlread.txt");
		BufferedReader br = new BufferedReader(new FileReader(sechduletime));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			String everything = sb.toString();
			String urlname = "http"
					+ everything.split("\\|")[0].split("http")[1];
			String day = everything.split("\\|")[1];
			String filetime = everything.split("\\|")[2];
			String count = everything.split("\\|")[3].trim();
		    GregorianCalendar gcalendar = new GregorianCalendar();
		    int currentday=gcalendar.get(Calendar.DAY_OF_WEEK);	
		    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String currenttime =sdf.format(gcalendar.getTime());
			String substring = day.substring(day.length() - 1).trim();
			System.out.println(currentday);
			System.out.println(currenttime);
			System.out.println(urlname);
			if (Integer.parseInt(substring) == currentday
					&& filetime.equals(currenttime)) {
				System.out.println("matches");
				newfile(Integer.parseInt(count), urlname);
				Thread.currentThread().sleep(60000);
			} else {				
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

	static void newfile(int count, String urlname) {
		OutputStream out = null;
		URLConnection conn = null;
		InputStream in = null;
		final int size = 1024;
		byte[] buf;
		int ByteRead = 0;
		int index = urlname.lastIndexOf("/");
		String filenme = urlname.substring(index + 1);
		String newfilenme = filenme.split("\\.")[0];
		String newfileext = "." + filenme.split("\\.")[1];
		String currentdte = (new Date() + "").replace(":", "_");
		try {
			for (int i = 0; i < count; i++) {
				URL url = new URL(urlname);
				conn = url.openConnection();
				conn.setConnectTimeout(600000);
				in = conn.getInputStream();
				buf = new byte[size];

				out = new BufferedOutputStream(new FileOutputStream(new File(
						newfilenme + "_" + currentdte + i
								+ newfileext)));
				while ((ByteRead = in.read(buf)) != -1) {
					out.write(buf, 0, ByteRead);
					System.out.println(buf);
				}
			}
			in.close();
			out.close();
		} catch (Exception exception) {
			System.out.println("Exception");
			exception.printStackTrace();
		}

	}
}
