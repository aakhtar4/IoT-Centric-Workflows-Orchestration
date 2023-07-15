package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import entities.Location;

public class Helper {
	public static String getFileNameFromFullPath(String path)
	{
		String[] directories = path.split("/");
		return directories[directories.length-1];
	}
	
	public static int findMinimal(double latitudes[], double longitudes[], int deviceList[], int count, Location workflowLocation)
	{
		System.out.println("Find Minimal");
		
		double distances[] = new double[count];
		
		
		for (int i = 0; i < count; i++)
		{
			Location deviceLocation = new Location(latitudes[i], longitudes[i]);
			distances[i] = Helper.distanceTo(deviceLocation, workflowLocation, 'M');
		
			System.out.println("Distance is =" + distances[i] + "device is = " + deviceList[i]);
		}

		int min = getMin(distances, deviceList);
		
		System.out.println("Closest Device is: " + min);

		return min;
	}

	public static int getMin(double[] inputArray, int Device_List[]) {
		int index = 0;
		double minValue = inputArray[0];
		index = Device_List[0];

		for (int i = 1; i < inputArray.length; i++) {
			if (inputArray[i] < minValue) {
				minValue = inputArray[i];
				index = Device_List[i];
			}
		}
		
		System.out.println("Min Distance is= " + minValue);

		return index;
	}
	
	public static double distanceTo(Location loc1, Location loc2, char unit)
	{
		double theta = loc1.longitude - loc2.longitude;
		double dist = Math.sin(deg2rad(loc1.latitude)) * Math.sin(deg2rad(loc2.latitude))
				+ Math.cos(deg2rad(loc1.latitude)) * Math.cos(deg2rad(loc2.latitude)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		double dist_roundoff = Math.round(dist * 100.0) / 100.0;

		return (dist_roundoff);
	}

	public static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	public static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
	
	public static String convertStreamToStr(InputStream is) throws IOException
	{
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else
		{
			return "";
		}
	}
	
	public static void openURL(String url)
	{
		String os = System.getProperty("os.name").toLowerCase();
		Runtime rt = Runtime.getRuntime();

		try {

			if (os.indexOf("win") >= 0) {
				rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else if (os.indexOf("mac") >= 0) {

				rt.exec("open " + url);

			} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {

				// Do a best guess on unix until we get a platform independent way Build a list of browsers to try, in this order.
				String[] browsers = { "epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links",
						"lynx" };

				// Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
				StringBuffer cmd = new StringBuffer();
				for (int i = 0; i < browsers.length; i++)
					cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");

				rt.exec(new String[] { "sh", "-c", cmd.toString() });

			} else {
				return;
			}
		} catch (Exception e) {
			return;
		}
	}
}
