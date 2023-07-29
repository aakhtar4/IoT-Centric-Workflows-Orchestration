package coordination;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class xml_task_parser  {
	
	public Connection connect = null;
	final public String host = "localhost:3306";
	final public String user = "root";
	final public String passwd = "admin";
	public HashMap<String, String> Sensors = new HashMap<String, String>();
	public HashMap<String, String> Fog_Services = new HashMap<String, String>();
	public HashMap<String, String> Cloud_Services = new HashMap<String, String>();
	public HashMap<String, String> Deployment_Command = new HashMap<String, String>();
	public HashMap<String, String> Deployment_Source_Files = new HashMap<String, String>();
	public ArrayList<HashMap<String, String>> SourceFilesData = new ArrayList<>();
	public HashMap<String, String> Deployment_Destination_Files = new HashMap<String, String>();
	public ArrayList<HashMap<String, String>> DestinationFilesData = new ArrayList<>();
	public double Longitude[] =  new double[100];
    public double Latitude[] =  new double[100];
    public int Device_List[] = new int[100];
    public int Peer_Node_List[] = new int[100];
    public String Communcation_Message[] = new String[100];
    public String Deployment_Message[] = new String[100];
    public String Task_Message[] = new String[100];
    public String input[] = new String[100];
    public String Peer_Nodes[] = new String[100];
    public String Deployment_IP_Address[] = new String[100];
    //String Device_URI[] = new String[100];
    public int Task_Id = 0;
    public String URI = "";
    public String IP_Address = "";
    //int Peer_Node = 0;
    public String Selected_Device_ID_List = "";
    public int count_device_selection_list = 0;
    public int Task_Gamma_Length[] = new int[100];
    public String Task_ServiceList[][] = new String[100][100];
    
    public String Fog_Service_Name[] = new String[100];
    public String Fog_Service_IP_Address[] = new String[100];
    public int Fog_Service_Availability[] = new int[100];
    public String Fog_Service_Type[] = new String[100];
    public int Fog_Deployable[] = new int[100];
    public int Fog_Non_Deployable[] = new int[100];
    public double Fog_Location_Latitude[] = new double[100];
    public double Fog_Location_Longitude[] = new double[100];
    public int Fog_Selected_Peer_Node[] = new int[100];
    public int Fog_cell_id[] = new int[100];
    public int Fog_Service_Count = 0;
    public int Task_Id_Track[] = new int[100];
	
	public void xml_parser(Document doc)
	{

		String Service_Name = "";
		String Location = "";
		int Availability = 0;
		String Availability_duration = "";
		String Service_Type = "";
		String Deployment_Status = "";
		int Deployable = 0;
		int Non_Deployable = 0;
		double Location_Latitude = 0;
		double Location_Longitude = 0;
		int count=1;
		int Selected_Peer_Node = 0;
		int cell_id = 0;
		String Input_details="";
	    try {
		/*File fXmlFile = new File("Model.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);*/	
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		
		
		NodeList TaskList = doc.getElementsByTagName("Task");
		for (int task = 0; task < TaskList.getLength(); task++) {
			Node TaskNode = TaskList.item(task);
			if (TaskNode.getNodeType() == Node.ELEMENT_NODE) {
				Element TaskElement = (Element) TaskNode;
				Task_Id = Integer.parseInt(TaskElement.getAttribute("id"));
				System.out.println("Task_id= "+Task_Id);
				}
				
		Element taskElement = (Element)doc.getElementsByTagName("Task").item(task);
		Communcation_Message[Task_Id] = "<?xml version=\"1.0\"?><response>";
		NodeList result = taskElement.getElementsByTagName("Gamma");
		System.out.println("Gamma Length is "+result.getLength());
		Task_Gamma_Length[Task_Id] = result.getLength();
		if(result.getLength() > 1)
		{
			//String required_services = "";
			for(int h=0;h<result.getLength();h++)
			{
				Node nNode = result.item(h);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode; 
					Service_Name = eElement.getElementsByTagName("Service_Name").item(0).getTextContent();
					System.out.println("Service_Name : " +Service_Name);
					Task_ServiceList[Task_Id][h] = Service_Name;
					/*if(h == 0)
					required_services = required_services + Service_Name;
					else
					{
						required_services = required_services + " ," + Service_Name;
					}*/
				}
			
			
			}
			//System.out.println("required_services "+required_services);
			//Selected_Peer_Node = Peer_Node_Search(Location_Latitude, Location_Longitude);
			Selected_Peer_Node = 1;
			//Peer_Nodes[count-1] = "<peer_node>"+Selected_Peer_Node+"</peer_node>";
			//Peer_Node = Selected_Peer_Node;
			cell_id = Selected_Peer_Node;
			Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "<peer_node>"+Selected_Peer_Node+"</peer_node>"+"<Services>";
		}
		else
		{
			for(int h=0;h<result.getLength();h++)
			{
				Node nNode = result.item(h);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode; 
					Service_Name = eElement.getElementsByTagName("Service_Name").item(0).getTextContent();
					System.out.println("Service_Name : " +Service_Name);
					Task_ServiceList[Task_Id][h] = Service_Name;
				}
			}
		}
		for(int h=0;h<result.getLength();h++)
		{
			Node nNode = result.item(h);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode; 
				System.out.println("Service id : " + eElement.getAttribute("id"));
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				System.out.println("Gamma id is= "+eElement.getAttribute("id"));
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				Service_Name = eElement.getElementsByTagName("Service_Name").item(0).getTextContent();
				System.out.println("Service_Name : " +Service_Name);
				Location = eElement.getElementsByTagName("Location").item(0).getTextContent(); 
				System.out.println("Location : " +Location);
				Availability_duration = eElement.getElementsByTagName("Availability").item(0).getTextContent();
				Availability = Integer.parseInt(Availability_duration.replaceAll("[\\D]", ""));
				System.out.println("Availability : " +Availability);
				System.out.println("Hardware_Requirements : " + eElement.getElementsByTagName("Hardware_Requirements").item(0).getTextContent());
				System.out.println("Software_Requirements : " + eElement.getElementsByTagName("Software_Requirements").item(0).getTextContent());
				System.out.println("Service_Input : " + eElement.getElementsByTagName("Service_Input").item(0).getTextContent());
				System.out.println("Service_Output : " + eElement.getElementsByTagName("Service_Output").item(0).getTextContent());
				Service_Type = eElement.getElementsByTagName("Service_Type").item(0).getTextContent();
				System.out.println("Service_Type : " +Service_Type);
				Deployment_Status = eElement.getElementsByTagName("Deployable").item(0).getTextContent();
				System.out.println("Deployment_Status : " + Deployment_Status);
				if(Deployment_Status.contains("Deployable") || Deployment_Status.contains("deployable"))
				{
					Deployable = 1;
					Non_Deployable = 0;
				}
				
				if(Deployment_Status.contains("non_deployable") || Deployment_Status.contains("Non_deployable"))
				{
					Deployable = 0;
					Non_Deployable = 1;
				}
				if(Deployable == 0 && Non_Deployable == 1 && Service_Type.contains("sensor")) //For sensors
				{
					System.out.println("Here in database search");
					Database_Search(Service_Name, Location, Availability, Service_Type, Deployable, Non_Deployable, Location_Latitude, Location_Longitude, Selected_Peer_Node, cell_id);
				}
				else if(Deployable == 1 && Non_Deployable == 0 && Service_Type.contains("Fog Service")) //For computation services
				{
					System.out.println("I have to Handle Fog Service");
					Fog_Service_Name[Fog_Service_Count] = Service_Name;
					Fog_Service_Availability[Fog_Service_Count] = Availability;
					Fog_Service_Type[Fog_Service_Count] = Service_Type;
					Fog_Deployable[Fog_Service_Count] = Deployable;
					Fog_Non_Deployable[Fog_Service_Count] = Non_Deployable;
					Fog_Location_Latitude[Fog_Service_Count] = Location_Latitude;
					Fog_Location_Longitude[Fog_Service_Count] = Location_Longitude;
					Fog_Selected_Peer_Node[Fog_Service_Count] = Selected_Peer_Node;
					Fog_cell_id[Fog_Service_Count] = cell_id;
					Task_Id_Track[Fog_Service_Count] = Task_Id;
					
					
					//Fog_Service_Count = Fog_Service_Count + 1;
					
					Device_Search(Service_Name, Location, Availability, Service_Type, Deployable, Non_Deployable, Location_Latitude, Location_Longitude, Selected_Peer_Node, cell_id);
					Fog_Service_IP_Address[Fog_Service_Count] = IP_Address;
					
					System.out.println("I have to Handle Fog Service = "+Fog_Service_Name[Fog_Service_Count]+" "+Fog_Service_Type[Fog_Service_Count] + " " +Fog_Service_Availability[Fog_Service_Count] + " "+ Fog_Deployable[Fog_Service_Count] + " " + Fog_Non_Deployable[Fog_Service_Count] + " "+Fog_Service_IP_Address[Fog_Service_Count]);
					
					Fog_Service_Count = Fog_Service_Count + 1;
					//Deplyoment_Files_Upload();
				}
				else if(Deployable == 0 && Non_Deployable == 1 && Service_Type.contains("Fog Service")) //For computation services
				{
					System.out.println("I have to Handle Non-Deployable Fog Service");
					//Device_Search(Service_Name, Location, Availability, Service_Type, Deployable, Non_Deployable, Location_Latitude, Location_Longitude, Selected_Peer_Node, cell_id);
					Database_Search(Service_Name, Location, Availability, Service_Type, Deployable, Non_Deployable, Location_Latitude, Location_Longitude, Selected_Peer_Node, cell_id);
					//Deplyoment_Files_Upload();
				}
				else if(Deployable == 0 && Non_Deployable == 1 && Service_Type.contains("Cloud Service")) //For computation services
				{
					System.out.println("I have to Handle it by Myself");
					//Device_Search(Service_Name, Location, Availability, Service_Type, Deployable, Non_Deployable, Location_Latitude, Location_Longitude, Selected_Peer_Node, cell_id);
					Database_Search(Service_Name, Location, Availability, Service_Type, Deployable, Non_Deployable, Location_Latitude, Location_Longitude, Selected_Peer_Node, cell_id);
					//Deplyoment_Files_Upload();
				}
				
				else //For Cloud Services
				{
					System.out.println("I have to handle Cloud Service");
					//Now I have to handle message
					//Cloud_Service_Search(Service_Name, Location, Availability, Service_Type, Deployable, Non_Deployable, Location_Latitude, Location_Longitude);	
				}
			}
			
		}
		}
		
	}catch (Exception e) {
		e.printStackTrace();
    }	    
	
	}
	
	
	public void Alternate_Device_Search(String Service_Name, int Availability, String Service_Type, int Deployable, int Non_Deployable, double Location_Latitude, double Location_Longitude, int Selected_Peer_Node, int cell_id) throws Exception
	{
	int Device_Id = 0;
	int count= 0;
	int Closest_Device = 0;
	//System.out.println("Here Avaialbility");
	try {
	      // This will load the MySQL driver, each DB has its own driver
	      //Class.forName("com.mysql.jdbc.Driver");
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      
	      // Setup the connection with the DB
	      connect = DriverManager
	          .getConnection("jdbc:mysql://" + host + "/lom?"
	              + "user=" + user + "&password=" + passwd );
	      try
	      {
	       // our SQL SELECT query. 
	      // if you only need a few columns, specify them by name instead of using "*"
	      String query = "SELECT * FROM services where Service_Name = '"+Service_Name+"' AND Availability >="+Availability+" AND Service_Type = '"+Service_Type+"' AND Deployable = "+Deployable+" AND Non_Deployable = "+Non_Deployable;
	      System.out.println("Query = "+query);	
	      // create the java statement
	      Statement st = connect.createStatement();
	      // execute the query, and get a java resultset
	      ResultSet rs = st.executeQuery(query);
	      //System.out.println("Selected_Device_ID_List ="+Selected_Device_ID_List);
	      // iterate through the java resultset
	      while (rs.next())
	      {
	    	  Device_Id = rs.getInt("Device_Id");
	    	  //System.out.println("Device_Id ="+Device_Id);
	    	 // if(!(Selected_Device_ID_List.contains(Integer.toString(Device_Id))))
	    	 // {
	    	  String Query_Device = "SELECT * FROM devices where id = "+Device_Id+" AND peer_node_id = "+Selected_Peer_Node+" AND cell_id = "+cell_id;
	    	  //System.out.println("Query_Device ="+ Query_Device);
	    	  	// create the java statement
	    	  Statement st_Device = connect.createStatement();
	    	   // execute the query, and get a java resultset
		      ResultSet rs_Device = st_Device.executeQuery(Query_Device);
		      // iterate through the java resultset
		      while (rs_Device.next())
		      {
		    	  Latitude[count] = rs_Device.getDouble("Latitude");
		    	  Longitude[count] = rs_Device.getDouble("Longitude");
		    	  Device_List[count] = Device_Id;
		    	  //Device_URI[count] = rs_Device.getString("URI");
		    	  count++; 
		      }
	    	 // }
		      
	      }
	      st.close();
	      System.out.println("count is"+count);
	      Closest_Device = FindMinimal(Latitude, Longitude, Device_List, count, Location_Latitude, Location_Longitude);
	      System.out.println("Closest_Device ="+Closest_Device);
	      count_device_selection_list = count_device_selection_list + 1;
	      if(count_device_selection_list == 1)
	      {
	      Selected_Device_ID_List = Integer.toString(Closest_Device);
	      }
	      else
	      {
	       Selected_Device_ID_List = Selected_Device_ID_List + " ," + Integer.toString(Closest_Device);  
	      }
	      System.out.println("Selected_Device_ID_List ="+Selected_Device_ID_List);
	      count = 0; 
	      Get_Device_IP_Address(Closest_Device);
	      
	     /* Commenting Start
	      Get_Computation_Device_URI(Closest_Device, Service_Name, Service_Type, Deployable);
	      
	      // addition of deployment 
	      SourceFilesData.add((Task_Id-1), new HashMap<String, String>());
	      DestinationFilesData.add((Task_Id-1), new HashMap<String, String>());
	      
	      //Communcation_Message = Communcation_Message + "</Services>\n<Deployment>";
	      Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "</Services><Deployment><Service_Name>"+Service_Name+"</Service_Name><IP_Address>"+IP_Address+"</IP_Address>";
	      Deployment_Message[Task_Id] = "<?xml version=\"1.0\"?><Deployment><Service_Name>"+Service_Name+"</Service_Name><IP_Address>"+IP_Address+"</IP_Address>";
	      String Destination_IP_Address = "<Destination>"+IP_Address+"</Destination>";
	      Communcation_Message[Task_Id] = Communcation_Message[Task_Id].replace("<Destination>NA</Destination>", Destination_IP_Address);
	      Deployment_IP_Address[Task_Id]= IP_Address;
	      Deplyoment_Files_Detail(Closest_Device, Service_Name);
	      //Communcation_Message = Communcation_Message + "</Deployment>\n</response>";
	      Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "</Deployment></response>";
	      Deployment_Message[Task_Id] = Deployment_Message[Task_Id] + "</Deployment>";
	      Commenting End */
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      System.err.println(e.getMessage());
	    }
	      
	    } catch (Exception e) {
	      throw e;
	    } 
	}
	
	public int Peer_Node_Search(Double Location_Latitude, Double Location_Longitude)
	{

		int Peer_Node_Id = 0;
		int count= 0;
		int Closest_Node = 0;
		//System.out.println("Here Avaialbility");
		try {
		      // This will load the MySQL driver, each DB has its own driver
		      //Class.forName("com.mysql.jdbc.Driver");
		      Class.forName("com.mysql.cj.jdbc.Driver");
		      
		      // Setup the connection with the DB
		      connect = DriverManager
		          .getConnection("jdbc:mysql://" + host + "/lom?"
		              + "user=" + user + "&password=" + passwd );
		      try
		      {
		       // our SQL SELECT query. 
		      // if you only need a few columns, specify them by name instead of using "*"
		      String query_peer_nodes = "SELECT * FROM peer_nodes";
		      System.out.println("Query = "+query_peer_nodes);	
		      // create the java statement
		      Statement st_peer_nodes = connect.createStatement();
		      // execute the query, and get a java resultset
		      ResultSet rs_peer_nodes = st_peer_nodes.executeQuery(query_peer_nodes);
		      // iterate through the java resultset
		      while (rs_peer_nodes.next())
		      {
		    	  Peer_Node_Id = rs_peer_nodes.getInt("id");
		    	  //System.out.println("peer node id = "+Peer_Node_Id);
		    	  Latitude[count] = rs_peer_nodes.getDouble("Latitude");
		    	  Longitude[count] = rs_peer_nodes.getDouble("Longitude");
		    	  Peer_Node_List[count] = Peer_Node_Id;
		    	  count++; 
		    	  
		      }
		      st_peer_nodes.close();
		      //System.out.println("count is"+count);
		      Closest_Node = Find_Minimal_Node(Latitude, Longitude, Peer_Node_List, count, Location_Latitude, Location_Longitude);
		      
		      /*count = 0;
		      Get_Computation_Device_URI(Closest_Device, Service_Name);
		      //Communcation_Message = Communcation_Message + "</Services>\n<Deployment>";
		      Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "</Services><Deployment>";
		      Deplyoment_Files_Detail(Closest_Device, Service_Name);
		      //Communcation_Message = Communcation_Message + "</Deployment>\n</response>";
		      Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "</Deployment></response>";*/
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		    }
		      
		    } catch (Exception e) {
		    	System.err.println("Got an exception! ");
			    System.err.println(e.getMessage());
		    } 
		   return Closest_Node;
	}
	
	
	public static int FindMinimal(double Latitude[], double Longitude[], int Device_List[], int count, double Location_Latitude, double Location_Longitude)
	{
		System.out.println("Find Minimal");
		double distance[] = new double[count];
		for(int i=0; i<count; i++)
		{
			distance[i] = distanceTo(Latitude[i], Longitude[i], Location_Latitude, Location_Longitude, 'M');
			System.out.println("Distance is ="+distance[i] + "device is = "+Device_List[i]);
			
		}
		
		int min = getMin(distance, Device_List);
	    System.out.println("Closest Device is: "+min);
	    //min = 69;
	    //System.out.println("Closest Device is: "+min);
	    return min;
	}
	
	
	public int Find_Minimal_Node(double Latitude[], double Longitude[], int Peer_Node_List[], int count, double Location_Latitude, double Location_Longitude)
	{
		//System.out.println("count is"+count);
		double distance[] = new double[count];
		for(int i=0; i<count; i++)
		{
			distance[i] = distanceTo(Latitude[i], Longitude[i], Location_Latitude, Location_Longitude, 'M');
			//System.out.println("Distance of Peer Node is ="+distance[i]);
		}
		
		int min = getMin(distance, Peer_Node_List);
		//min = min + 1;
	    System.out.println("Closest Node is: "+min);
	    return min;
	}
	
	
	
    public static int getMin(double[] inputArray, int Device_List[])
    { 
        int index = 0;
    	double minValue = inputArray[0]; 
    	index = Device_List[0];
    	
        for(int i=1;i<inputArray.length;i++){ 
          if(inputArray[i] < minValue){ 
            minValue = inputArray[i]; 
            index = Device_List[i];
            //URI = Device_URI[i];
          } 
        } 
        System.out.println("Min Distance is= "+minValue);
        //System.out.println("closest device is= "+index);
        //System.out.println("URI is= "+URI);
        //Sensors.put(Service_Name, URI);
        return index; 
      } 
	

	public static  double distanceTo(double lat1, double lon1, double lat2, double lon2, char unit) 
	{
	  double theta = lon1 - lon2;
  	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
  	  dist = Math.acos(dist);
  	  dist = rad2deg(dist);
  	  dist = dist * 60 * 1.1515;
  	  if (unit == 'K') {
  	    dist = dist * 1.609344;
  	  } else if (unit == 'N') {
  	  dist = dist * 0.8684;
  	    }
  	  double dist_roundoff = Math.round(dist * 100.0) / 100.0;
  	  //System.out.println("distance is "+dist);
  	  return (dist_roundoff);
  	}
	
	public static double deg2rad(double deg) {
	  	  return (deg * Math.PI / 180.0);
	  	}
	    
	    public static double rad2deg(double rad) {
	    	  return (rad * 180.0 / Math.PI);
	    }
	
	
	
	public void Database_Search(String Service_Name, String Location, int Availability, String Service_Type, int Deployable, int Non_Deployable, double Location_Latitude, double Location_Longitude, int Selected_Peer_Node, int cell_id) throws Exception
	{
	int Device_Id = 0;
	int count= 0;
	int Closest_Device = 0;
	String Query_Device = "";
	//System.out.println("Here Avaialbility");
	try {
	      // This will load the MySQL driver, each DB has its own driver
	      //Class.forName("com.mysql.jdbc.Driver");
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      
	      // Setup the connection with the DB
	      connect = DriverManager
	          .getConnection("jdbc:mysql://" + host + "/lom?"
	              + "user=" + user + "&password=" + passwd );
	      try
	      {
	       // our SQL SELECT query. 
	      // if you only need a few columns, specify them by name instead of using "*"
	      String query = "SELECT * FROM services where Service_Name = '"+Service_Name+"' AND Availability >="+Availability+" AND Service_Type = '"+Service_Type+"' AND Deployable ="+Deployable+" AND Non_Deployable = "+Non_Deployable;
	      System.out.println("Query = "+query);	
	      // create the java statement
	      Statement st = connect.createStatement();
	      // execute the query, and get a java resultset
	      ResultSet rs = st.executeQuery(query);
	      // iterate through the java resultset
	      while (rs.next())
	      {
	    	  Device_Id = rs.getInt("Device_Id");
	    	  if(!(Selected_Device_ID_List.contains(Integer.toString(Device_Id))))
	    	  {
	    	   Query_Device = "SELECT * FROM devices where id = "+Device_Id+" and peer_node_id = "+Selected_Peer_Node+ " and cell_id = "+cell_id;
	    	   //System.out.println("Query_Device = "+ Query_Device);
	    	  	// create the java statement
	    	  Statement st_Device = connect.createStatement();
	    	   // execute the query, and get a java resultset
		      ResultSet rs_Device = st_Device.executeQuery(Query_Device);
		      // iterate through the java resultset
		      while (rs_Device.next())
		      { 
		    	  Latitude[count] = rs_Device.getDouble("Latitude");
		    	  Longitude[count] = rs_Device.getDouble("Longitude");
		    	  Device_List[count] = Device_Id;
		    	  //Device_URI[count] = rs_Device.getString("URI");
		    	  count++; 
		      }
	    	  }
		      
	      }
	      st.close();
	      //System.out.println("count is"+count);
	      Closest_Device = FindMinimal(Latitude, Longitude, Device_List, count, Location_Latitude, Location_Longitude);
	      count_device_selection_list = count_device_selection_list + 1;
	      if(count_device_selection_list == 1)
	      {
	      Selected_Device_ID_List = Integer.toString(Closest_Device);
	      }
	      else
	      {
	       Selected_Device_ID_List = Selected_Device_ID_List + " ," + Integer.toString(Closest_Device);  
	      }
	      System.out.println("Selected_Device_ID_List ="+Selected_Device_ID_List);
	      count = 0;
	      
	      Get_Device_IP_Address(Closest_Device);
	      //System.out.println("I am Here I am here 2");
	      Get_Device_URI(Closest_Device, Service_Name, Service_Type, Deployable);
	      
	      /*addition of deployment */
	      //System.out.println("Task_Id source 1 "+Task_Id);
	      //Task_Id = 1;
	      for(int uvw=0; uvw<Task_Id; uvw++)
	      {
	      SourceFilesData.add((uvw), new HashMap<String, String>());
	      DestinationFilesData.add((uvw), new HashMap<String, String>());
	      }
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception here after! ");
	      System.err.println(e.getMessage());
	    }
	      
	    } catch (Exception e) {
	      throw e;
	    } 
	}
	
	public void Get_Device_IP_Address(int Closest_Device) throws Exception
	{

		
		try {
		      // This will load the MySQL driver, each DB has its own driver
		      //Class.forName("com.mysql.jdbc.Driver");
		      Class.forName("com.mysql.cj.jdbc.Driver");
		      
		      // Setup the connection with the DB
		      connect = DriverManager
		          .getConnection("jdbc:mysql://" + host + "/lom?"
		              + "user=" + user + "&password=" + passwd );
		      try
		      {
		       // our SQL SELECT query. 
		      // if you only need a few columns, specify them by name instead of using "*"
		      String query = "SELECT * FROM devices where id ="+Closest_Device;
		      System.out.println("Query = "+query);	
		      // create the java statement
		      Statement st = connect.createStatement(); 
		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query);
		      // iterate through the java resultset
		      while (rs.next())
		      {
		    	  //System.out.println("------------------IP_Address--------------------\n"+IP_Address);
		    	  IP_Address = rs.getString("IP_Address");
		    	  System.out.println("------------------IP_Address--------------------\n"+IP_Address);
		    	  
		    	  
		      }
		      st.close();
		      //Communcation_Message = Communcation_Message + "<Service>"+Service_Name+"</Service>"+"<URI>"+URI+"</URI>\n";
		      //Communcation_Message = Communcation_Message + "<Service name=\""+Service_Name+"\">"+"\n<URI>"+URI+"</URI>\n</Service>\n";
		      //Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "<Service name=\""+Service_Name+"\">"+"<URI>"+URI+"</URI>"+"<IP_Address>"+IP_Address+"</IP_Address></Service>";
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		    }
		      
		    } catch (Exception e) {
		      throw e;
		    } 
	
	}
	
	
	public void Get_Device_URI(int Closest_Device, String Service_Name, String Service_Type, int Deployable) throws Exception
	{
		
		try {
		      // This will load the MySQL driver, each DB has its own driver
		      //Class.forName("com.mysql.jdbc.Driver");
		      Class.forName("com.mysql.cj.jdbc.Driver");
		      
		      // Setup the connection with the DB
		      connect = DriverManager
		          .getConnection("jdbc:mysql://" + host + "/lom?"
		              + "user=" + user + "&password=" + passwd );
		      try
		      {
		       // our SQL SELECT query. 
		      // if you only need a few columns, specify them by name instead of using "*"
		      String query = "SELECT * FROM services where Service_Name = '"+Service_Name+"' AND Device_Id ="+Closest_Device;
		      System.out.println("Query = "+query);	
		      // create the java statement
		      Statement st = connect.createStatement(); 
		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query);
		      // iterate through the java resultset
		      if (rs.next())
		      {
		    	  URI = rs.getString("URI");
		    	  System.out.println("URI ="+URI);
		    	  
		      }
		      st.close();
		      Sensors.put(Service_Name, URI); 
		      //Communcation_Message = Communcation_Message + "<Service>"+Service_Name+"</Service>"+"<URI>"+URI+"</URI>\n";
		      //Communcation_Message = Communcation_Message + "<Service name=\""+Service_Name+"\">"+"\n<URI>"+URI+"</URI>\n</Service>\n";
		      if(Service_Type.contains("sensor"))
		      {
		      Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "<Service name=\""+Service_Name+"\">"+"<URI>"+URI+"</URI>"+"<IP_Address><Source>"+IP_Address+"</Source><Destination>NA</Destination></IP_Address><Type>"+Service_Type+"</Type><Deployment>"+Deployable+"</Deployment></Service>";
		      }
		      if(Service_Type.contains("Fog Service"))
		      {
		      Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "<Service name=\""+Service_Name+"\">"+"<URI>"+URI+"</URI>"+"<IP_Address>"+IP_Address+"</IP_Address><Type>"+Service_Type+"</Type><Deployment>"+Deployable+"</Deployment></Service>";
		      }
		      if(Service_Type.contains("Cloud Service"))
		      {
		      Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "<Service name=\""+Service_Name+"\">"+"<URI>"+URI+"</URI>"+"<IP_Address>"+IP_Address+"</IP_Address><Type>"+Service_Type+"</Type><Deployment>"+Deployable+"</Deployment></Service>";
		      }
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception 1! ");
		      System.err.println(e.getMessage());
		    }
		      
		    } catch (Exception e) {
		      throw e;
		    } 
	}
	
	
	
	
	public void Device_Search(String Service_Name, String Location, int Availability, String Service_Type, int Deployable, int Non_Deployable, double Location_Latitude, double Location_Longitude, int Selected_Peer_Node, int cell_id) throws Exception
	{
	int Device_Id = 0;
	int count= 0;
	int Closest_Device = 0;
	//System.out.println("Here Avaialbility");
	try {
	      // This will load the MySQL driver, each DB has its own driver
	      //Class.forName("com.mysql.jdbc.Driver");
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      
	      // Setup the connection with the DB
	      connect = DriverManager
	          .getConnection("jdbc:mysql://" + host + "/lom?"
	              + "user=" + user + "&password=" + passwd );
	      try
	      {
	       // our SQL SELECT query. 
	      // if you only need a few columns, specify them by name instead of using "*"
	      String query = "SELECT * FROM services where Service_Name = '"+Service_Name+"' AND Availability >="+Availability+" AND Service_Type = '"+Service_Type+"' AND Deployable ="+Deployable+" AND Non_Deployable = "+Non_Deployable;
	      System.out.println("Query = "+query);	
	      // create the java statement
	      Statement st = connect.createStatement();
	      // execute the query, and get a java resultset
	      ResultSet rs = st.executeQuery(query);
	      // iterate through the java resultset
	      while (rs.next())
	      {
	    	  Device_Id = rs.getInt("Device_Id");
	    	  if(!(Selected_Device_ID_List.contains(Integer.toString(Device_Id))))
	    	  {
	    	  String Query_Device = "SELECT * FROM devices where id = "+Device_Id+" AND peer_node_id = "+Selected_Peer_Node+" AND cell_id = "+cell_id;
	    	  //System.out.println("Query_Device ="+ Query_Device);
	    	  	// create the java statement
	    	  Statement st_Device = connect.createStatement();
	    	   // execute the query, and get a java resultset
		      ResultSet rs_Device = st_Device.executeQuery(Query_Device);
		      // iterate through the java resultset
		      while (rs_Device.next())
		      {
		    	  
		    	  Latitude[count] = rs_Device.getDouble("Latitude");
		    	  Longitude[count] = rs_Device.getDouble("Longitude");
		    	  Device_List[count] = Device_Id;
		    	  //Device_URI[count] = rs_Device.getString("URI");
		    	  count++; 
		      }
	    	  }
		      
	      }
	      st.close();
	      //System.out.println("count is"+count);
	      Closest_Device = FindMinimal(Latitude, Longitude, Device_List, count, Location_Latitude, Location_Longitude);
	      count_device_selection_list = count_device_selection_list + 1;
	      if(count_device_selection_list == 1)
	      {
	      Selected_Device_ID_List = Integer.toString(Closest_Device);
	      }
	      else
	      {
	       Selected_Device_ID_List = Selected_Device_ID_List + " ," + Integer.toString(Closest_Device);  
	      }
	      System.out.println("Selected_Device_ID_List ="+Selected_Device_ID_List);
	      count = 0;
	      Get_Device_IP_Address(Closest_Device);
	      Get_Computation_Device_URI(Closest_Device, Service_Name, Service_Type, Deployable);
	      
	      /*addition of deployment */
	      //System.out.println("Task_Id source "+Task_Id);
	      for(int uvw=0; uvw<Task_Id; uvw++)
	      {
	      SourceFilesData.add((uvw), new HashMap<String, String>());
	      DestinationFilesData.add((uvw), new HashMap<String, String>());
	      }
	      System.out.println("Task_Id source "+Task_Id);
	      //Communcation_Message = Communcation_Message + "</Services>\n<Deployment>";
	      Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "</Services><Deployment><Task>["+Task_Id+"]</Task><Service_Name>"+Service_Name+"</Service_Name><IP_Address>"+IP_Address+"</IP_Address>";
	      System.out.println("Communcation_Message[Task_Id] ="+Communcation_Message[Task_Id]);
	      Deployment_Message[Task_Id] = "<?xml version=\"1.0\"?><Deployment><Task>["+Task_Id+"]</Task><Service_Name>"+Service_Name+"</Service_Name><IP_Address>"+IP_Address+"</IP_Address>";
	      System.out.println("Deployment_Message[Task_Id] ="+Deployment_Message[Task_Id]);
	      String Destination_IP_Address = "<Destination>"+IP_Address+"</Destination>";
	      Communcation_Message[Task_Id] = Communcation_Message[Task_Id].replace("<Destination>NA</Destination>", Destination_IP_Address);
	      Deployment_IP_Address[Task_Id]= IP_Address;
	      System.out.println("Task_Id source "+Task_Id);
	      Deplyoment_Files_Detail(Closest_Device, Service_Name);
	      System.out.println("Task_Id source "+Task_Id);
	      //Communcation_Message = Communcation_Message + "</Deployment>\n</response>";
	      Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "</Deployment></response>";
	      System.out.println("Communcation_Message[Task_Id] ="+Communcation_Message[Task_Id]);
	      Deployment_Message[Task_Id] = Deployment_Message[Task_Id] + "</Deployment>";
	      System.out.println("Deployment_Message[Task_Id] ="+Deployment_Message[Task_Id]);
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception hhh! ");
	      System.err.println(e.getMessage());
	    }
	      
	    } catch (Exception e) {
	      throw e;
	    } 
	}
	
	public void Get_Computation_Device_URI(int Closest_Device, String Service_Name, String Service_Type, int Deployable) throws Exception
	{
		String URI = "";
		try {
		      // This will load the MySQL driver, each DB has its own driver
		      //Class.forName("com.mysql.jdbc.Driver");
		      Class.forName("com.mysql.cj.jdbc.Driver");
		      
		      // Setup the connection with the DB
		      connect = DriverManager
		          .getConnection("jdbc:mysql://" + host + "/lom?"
		              + "user=" + user + "&password=" + passwd );
		      try
		      {
		       // our SQL SELECT query. 
		      // if you only need a few columns, specify them by name instead of using "*"
		      String query = "SELECT * FROM services where Service_Name = '"+Service_Name+"' AND Device_Id ="+Closest_Device;
		      //System.out.println("Query = "+query);	
		      // create the java statement
		      Statement st = connect.createStatement(); 
		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query);
		      // iterate through the java resultset
		      if (rs.next())
		      {
		    	  URI = rs.getString("URI"); 
		      }
		      st.close();
		      Fog_Services.put(Service_Name, URI); 
		      //Communcation_Message = Communcation_Message + "Service:"+Service_Name+",URI:"+URI+"\n";
		      //Communcation_Message = Communcation_Message + "<Service name=\""+Service_Name+"\">"+"\n<URI>"+URI+"</URI>\n</Service>\n";
		      Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "<Service name=\""+Service_Name+"\">"+"<URI>"+URI+"</URI>"+"<IP_Address>"+IP_Address+"</IP_Address><Type>"+Service_Type+"</Type><Deployment>"+Deployable+"</Deployment></Service>";
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		    }
		      
		    } catch (Exception e) {
		      throw e;
		    } 
	}
	
	public void Deplyoment_Files_Detail(int Closest_Device, String Service_Name) throws Exception
	{
		
		int Deployment_Service_Id = 0;
		String Doployment_Service_URI = "";
		try {
		      // This will load the MySQL driver, each DB has its own driver
		      //Class.forName("com.mysql.jdbc.Driver");
		      Class.forName("com.mysql.cj.jdbc.Driver");
		      
		      // Setup the connection with the DB
		      connect = DriverManager
		          .getConnection("jdbc:mysql://" + host + "/lom?"
		              + "user=" + user + "&password=" + passwd );
		      try
		      {
		       // our SQL SELECT query. 
		      // if you only need a few columns, specify them by name instead of using "*"
		      String query = "SELECT * FROM services where Service_Name = '"+Service_Name+"' AND Device_Id ="+Closest_Device;
		      System.out.println("Query = "+query);	
		      // create the java statement
		      Statement st = connect.createStatement(); 
		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query);
		      // iterate through the java resultset
		      if (rs.next())
		      {
		    	  Deployment_Service_Id = rs.getInt("id");
		    	  Doployment_Service_URI = rs.getString("URI");
		    	  String Query_Deployment = "SELECT * FROM deployment where Deployment_Service_Id = "+Deployment_Service_Id;
		    	  System.out.println("Query_Deployment "+Query_Deployment);
		    	  	// create the java statement
		    	  Statement st_Deployment = connect.createStatement();
		    	   // execute the query, and get a java resultset
			      ResultSet rs_Deployment = st_Deployment.executeQuery(Query_Deployment);
			      // iterate through the java resultset
			      while (rs_Deployment.next())
			      {
			    	  System.out.println("Hre Command"); 
			    	  String Command = rs_Deployment.getString("Command");
			    	  if(Command.contains("argument_1"))
			    	  {
			    		  //String arg_1 = rs_Deployment.getString("arg_1");
			    		  Command = Command.replace("argument_1", URI);
			    	  }
			    	  
			    	  if(Command.contains("argument_2"))
			    	  {
			    		  String arg_2 = rs_Deployment.getString("arg_2");
			    		  Command = Command.replace("argument_2", arg_2);
			    	  }
			    	  
			    	  if(Command.contains("argument_3"))
			    	  {
			    		  String arg_3 = rs_Deployment.getString("arg_3");
			    		  Command = Command.replace("argument_3", arg_3);
			    	  }
			    	  Deployment_Command.put("Command", Command);
			    	  //Communcation_Message = Communcation_Message + "Command:"+Command+"\n";
			    	  //Communcation_Message = Communcation_Message + "<Command>"+Command+"</Command>"+"\n";
			    	  Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "<Command>"+Command+"</Command>";
			    	  Deployment_Message[Task_Id] = Deployment_Message[Task_Id] + "<Command>"+Command+"</Command>";
			    	  for(int i = 1; i <= 15; i++)
			    	  {
			    		  //System.out.println("here put");
			    		  String Source_Variable_Name = "Source_File_Address_"+i;
			    		  String Dest_Variable_Name = "Destination_File_Address_"+i;
			    		  String Source_File_Name = rs_Deployment.getString(Source_Variable_Name);
			    		  //System.out.println("Source_File_Name ="+Source_Variable_Name);
			    		  //System.out.println("Dest_Variable_Name ="+Dest_Variable_Name);
			    		  if((!(Source_File_Name.equals(""))) && (!(Source_File_Name.equals(null))) && (!(Source_File_Name.equals("Nil"))))
			    		  {  
			    			  Deployment_Source_Files.put((Source_Variable_Name+"_"+Task_Id), rs_Deployment.getString(Source_Variable_Name));
			    			  if(rs_Deployment.getString(Source_Variable_Name).contains("config"))
			    			  {
			    				  //Communcation_Message = Communcation_Message + "Configuration_File:config.txt";  
			    				  Communcation_Message[Task_Id] = Communcation_Message[Task_Id] + "<Configuration_File>config.txt</Configuration_File>";  
			    				  Deployment_Message[Task_Id] = Deployment_Message[Task_Id] + "<Configuration_File>config.txt</Configuration_File>";
			    			  }
			    			  //Communcation_Message = Communcation_Message+Source_Variable_Name+":"+rs_Deployment.getString(Source_Variable_Name)+"\n";
			    			  Deployment_Destination_Files.put((Dest_Variable_Name+"_"+Task_Id), rs_Deployment.getString(Dest_Variable_Name));
			    			  //Communcation_Message = Communcation_Message+Dest_Variable_Name+":"+rs_Deployment.getString(Dest_Variable_Name)+"\n";
			    		  }
			    	  }
			    	  
			    	  System.out.println("here put"+(Task_Id-1));
			    	  //SourceFilesData.add((Task_Id-1), new HashMap<String, String>());
			    	  SourceFilesData.add((Task_Id-1), Deployment_Source_Files);
			    	  //DestinationFilesData.add((Task_Id-1), new HashMap<String, String>());
			    	  DestinationFilesData.add((Task_Id-1), Deployment_Destination_Files);
			    	  
			      }
		    	  
		      }
		      st.close();
		      
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		    }
		      
		    } catch (Exception e) {
		      throw e;
		    } 
	}
	
	
}
