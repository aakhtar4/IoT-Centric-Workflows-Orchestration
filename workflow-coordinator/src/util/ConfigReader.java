package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import entities.Location;

public class ConfigReader {
    private Properties properties;

    public ConfigReader(String configFilePath) {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(configFilePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getApproach()
    {
    	return properties.getProperty("approach");
    }
    
    public String getDeploymentPath()
    {
    	return properties.getProperty("deployment_path");
    }
    
    public Location getWorkflowLocation()
    {
    	double latitude = Double.parseDouble(properties.getProperty("workflow_location").split(",")[0]);
    	double longitude = Double.parseDouble(properties.getProperty("workflow_location").split(",")[1]);
    	return new Location(latitude, longitude);
    }
    
    public String get_DB_URL()
    {
    	return properties.getProperty("db_url");
    }
   
    public String get_DB_Username()
    {
    	return properties.getProperty("db_username");
    }

    public String get_DB_Password()
    {
    	return properties.getProperty("db_password");
    }
    
	public String getWorkflowFileName() {
    	return properties.getProperty("workflow_file_name");
    }
}

