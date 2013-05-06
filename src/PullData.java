import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PullData {

	void ReadXMLFormat(String urllink, String fileLoconDisk) {
		try {
			URL url = new URL(urllink);
			URLConnection conn = url.openConnection();
			System.out.println(conn);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;
			FileWriter fw = new FileWriter(fileLoconDisk);
			BufferedWriter bw = new BufferedWriter(fw);
			while ((inputLine = in.readLine()) != null) {
				bw.write(inputLine);
				/*
				 * We dont need this. May cause problems in case when there is a
				 * new line in the feed. The xml reader or browser will throw
				 * error
				 */
			}
			in.close();
			bw.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int GetNumberOfRecords(String url) {
		int rec = 0;
		String loconDisk = "data/andersonCooper" + "temp" + ".xml";
		PullData getData = new PullData();
		getData.ReadXMLFormat(url, loconDisk);
		return rec;
	}

	public static void main(String args[]) {
		int limit = 22360, count = 1;
		String mainUrl = "http://api.appinions.com/search/v2/opinions?appkey=n3u5brw98un3geetezt7e3zz&sent=%22best%20picture%22%20OR%20bestpicture%20OR%20((oscar%20OR%20%22academy%20award%22)%20AND%20(Amour%20OR%20Argo%20OR%20Django%20OR%20Miserables%20OR%20%22les%20mis%22%20OR%20%22life%20of%20pi%22%20OR%20%22lincoln%22%20OR%20%22silver%20linings%22%20OR%20%22zero%20dark%22%20OR%20%22beasts%20of%20the%20southern%20wild%22))";
		for (int st = (count - 1) * 1500; st < limit; st += 1500, count++) {
			String urltoget = mainUrl + "&start=" + st + "&rows=1500";
			String loconDisk = "data/bestpic/b" + count + ".xml";
			PullData getData = new PullData();
			getData.ReadXMLFormat(urltoget, loconDisk);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println("got interrupted!");
			}
		}
	}
}
