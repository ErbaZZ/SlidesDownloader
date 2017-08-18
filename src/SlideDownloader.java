
/* ICT E-Learning Slides Downloader 0.1 by ErbaZZ */
/* Modified to work with the new E-Learning version by Poom */
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SlideDownloader {
	// Edit cookieV1, 2, 3, and URL you want to download from
	// cookieV3 is for the old E-Learning site only
	static String cookieN1 = "MoodleSession";
	static String cookieV1 = "";
	static String cookieN2 = "MOODLEID_";
	static String cookieV2 = "";
	static String cookieN3 = "MoodleSessionTest";
	static String cookieV3 = "";
	static boolean modern = true;
	static Integer courseID = 211;
	static String domain = modern ? "https://mycourses" : "http://elearning";
	static String siteURL = domain + ".ict.mahidol.ac.th/course/view.php?id=" + courseID;

	public static void main(String[] args) throws IOException {
		ArrayList<String> resLinks = new ArrayList<String>();
		Map<String, String> cookies = new HashMap<String, String>();
		cookies.put(cookieN1, cookieV1);
		cookies.put(cookieN2, cookieV2);
		if (!modern)
			cookies.put(cookieN3, cookieV3);
		Document doc = Jsoup.connect(siteURL).cookies(cookies).get();
		Elements resources = doc.select("a[href*=" + domain + ".ict.mahidol.ac.th/mod/resource/view.php?id=]");
		for (Element link : resources) {
			resLinks.add(link.attr("href"));
			System.out.println(link.attr("href"));
		}

		for (String l : resLinks) {
			Document f;
			try {
				f = Jsoup.connect(l).followRedirects(false).cookies(cookies).get();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			String rawURL = modern ? f.select("a").attr("href")
					: f.select("div.resourcepdf").select("object").attr("data");
			if (rawURL.isEmpty())
				continue;
			rawURL = rawURL.replaceAll("%20", " ");
			URL url = new URL(rawURL);
			byte[] bytes = Jsoup.connect(rawURL).followRedirects(false).cookies(cookies).maxBodySize(0)
					.ignoreContentType(true).execute().bodyAsBytes();
			System.out.println(FilenameUtils.getName(url.getPath()));
			File file = new File("PDF/" + FilenameUtils.getName(url.getPath()));
			FileUtils.writeByteArrayToFile(file, bytes);
			System.out.println("Success: " + FileUtils.checksumCRC32(file));
		}
		System.out.println("Done!");
	}

}
