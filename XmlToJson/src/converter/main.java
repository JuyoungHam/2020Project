package converter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class main {
	public static final int INDENT_FACTOR = 4;

	public static void main(String args[]) throws IOException, MalformedURLException {

		URL[] urlList = { new URL("http://175.125.91.94/oasis/service/rest/meta2020/docMeta"),
				new URL("http://175.125.91.94/oasis/service/rest/other/getMAPN0701"),
				new URL("http://175.125.91.94/oasis/service/rest/meta16/getNtEx"),
				new URL("http://175.125.91.94/oasis/service/rest/other/getSEMN5601"),
				new URL("http://175.125.91.94/oasis/service/rest/meta4/getARKA1202"),
				new URL("http://175.125.91.94/oasis/service/rest/meta16/getPf2Gn2"),
				new URL("http://175.125.91.94/oasis/service/rest/meta2020/getKOCAperf") };

		for (int i = 0; i < urlList.length; i++) {
			HttpURLConnection conn = (HttpURLConnection) urlList[i].openConnection();
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "utf8"));
			StringBuffer st = new StringBuffer();
			String line;

			while ((line = reader.readLine()) != null) {
				st.append(line);
			}

			JSONObject xmlJSONObj = XML.toJSONObject(st.toString());
			String jsonPrettyPrintString = xmlJSONObj.toString(INDENT_FACTOR);
			System.out.println(jsonPrettyPrintString);

			FileOutputStream fos = new FileOutputStream("../../JsonFiles/" + i + ".json");
			DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
			outStream.writeUTF(jsonPrettyPrintString);
			outStream.close();
		}

	}
}