package coordination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class LogStreamReader implements Runnable
{
    private BufferedReader reader;
    public static String vehicle_status_output="";

    public LogStreamReader(InputStream is)
    {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void run()
    {
        try
        {
            String line = reader.readLine();
            String output = "";
            output =  output + line;
        
            while (line != null)
            {
                output =  output + line;
                Main_Rasp_Combine.task_output = output;
                
                if(line.contains("Started GpsSimulatorApplication"))
                {
                	try
                	{
	                	System.out.println("******Visiting URL**********");
	                	openURL("http://localhost:8080/api/dc");
	                	Thread.sleep(5000);
	                	openURL("plume_display.html");
	                	Thread.sleep(5000);
	                	openURL("http://localhost:8080/api/cancel");
                	}
                	catch (InterruptedException e)
                	{
                        System.out.println("Error occured while executing Linux command. Error Description: " + e.getMessage());
                    }
                }
                
                line = reader.readLine();
            }

            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void openURL(String url)
	{
		String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
		
		try
		{
		    if (os.indexOf( "win" ) >= 0)
		    {
		        rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);

		    }
		    else if (os.indexOf( "mac" ) >= 0)
		    {
		        rt.exec( "open " + url);

            }
		    else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0)
		    {
		        // Do a best guess on unix until we get a platform independent way build a list of browsers to try, in this order.
		        String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
		       			             "netscape","opera","links","lynx"};
		        	
		        // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
		        StringBuffer cmd = new StringBuffer();
		        for (int i=0; i<browsers.length; i++)
		            cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");
		        	
		        rt.exec(new String[] { "sh", "-c", cmd.toString() });

		    }
		    else
		    {
	                return;
           }
       }
	   catch (Exception e)
	   {
	       return;
       }
	}   
}