/* ICT E-Learning Slides Downloader 0.1 by ErbaZZ */
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SlideDownloader {
	// Edit cookieV1, 2, 3, and URL you want to download from
	static String cookieN1 = "MoodleSession";
	static String cookieV1 = "";
	static String cookieN2 = "MOODLEID_";
	static String cookieV2 = "";
	static String cookieN3 = "MoodleSessionTest";
	static String cookieV3 = "";
	static String siteURL = "http://elearning.ict.mahidol.ac.th/course/view.php?id=919";
	
	public static void main(String[] args) throws IOException {
		ArrayList<String> resLinks = new ArrayList<String>();
		Document doc = Jsoup.connect(siteURL).cookie(cookieN1, cookieV1).cookie(cookieN2, cookieV2).cookie(cookieN3, cookieV3).get();
		Elements resources = doc.select("a[href*=http://elearning.ict.mahidol.ac.th/mod/resource/view.php?id=]");
		for (Element link : resources) {
			resLinks.add(link.attr("href"));
			System.out.println(link.attr("href"));
		}
		
		for (String l : resLinks) {
			Document f;
			try {
				f = Jsoup.connect(l).cookie(cookieN1, cookieV1).cookie(cookieN2, cookieV2).cookie(cookieN3, cookieV3).get();
			}
			catch (Exception e) {
				continue;
			}
			String rawURL = f.select("div.resourcepdf").select("object").attr("data");
			if (rawURL.isEmpty()) continue;
			URL url = new URL(rawURL);
			byte[] bytes = Jsoup.connect(rawURL).cookie(cookieN1, cookieV1).cookie(cookieN2, cookieV2).cookie(cookieN3, cookieV3).maxBodySize(0).ignoreContentType(true).execute().bodyAsBytes();
			System.out.println(FilenameUtils.getName(url.getPath()));
			File file = new File("PDF/" + FilenameUtils.getName(url.getPath()));
			FileUtils.writeByteArrayToFile(file, bytes);
			System.out.println("Success: " + FileUtils.checksumCRC32(file));
		}
		System.out.println("Done!");
	}
	
}
