package coordination;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import connection.DBConnectionFactory;
import connection.MQTTConnectionFactory;
import entities.Device;
import entities.Location;
import entities.Service;
import entities.Task;
import registry_info.Devices_DAL;
import registry_info.PeerNodes_DAL;
import util.Helper;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class GOM_Approach
{
	private Connection connect = null;
	HashMap<String, String> Sensors = new HashMap<String, String>();
	HashMap<String, String> Fog_Services = new HashMap<String, String>();
	HashMap<String, String> Cloud_Services = new HashMap<String, String>();
	HashMap<String, String> Deployment_Command = new HashMap<String, String>();
	HashMap<String, String> Deployment_Source_Files = new HashMap<String, String>();
	ArrayList<HashMap<String, String>> SourceFilesData = new ArrayList<>();
	double longitudes[] = new double[100];
	double latitudes[] = new double[100];
	int Device_List[] = new int[100];
	int Peer_Node_List[] = new int[100];
	String Communcation_Message[] = new String[100];
	String Deployment_Message[] = new String[100];
	String Deployment_Message_List[][] = new String[100][100];
	String input[] = new String[100];
	String Peer_Nodes[] = new String[100];
	int task_ID = 0;
	String IP_Address = "";
	String Selected_Device_ID_List = "";
	int count_device_selection_list = 0;
	int task_services_count[] = new int[100];

	String Fog_Service_Name[] = new String[100];
	String Fog_Service_IP_Address[] = new String[100];
	int Fog_Service_Availability[] = new int[100];
	String Fog_Service_Type[] = new String[100];
	int Fog_Deployable[] = new int[100];
	int Fog_Non_Deployable[] = new int[100];
	double Fog_Location_Latitude[] = new double[100];
	double Fog_Location_Longitude[] = new double[100];
	int Fog_Selected_Peer_Node[] = new int[100];
	int Fog_cell_id[] = new int[100];
	int Fog_Service_Count = 0;
	int fog_service_task_IDs[] = new int[100];
	NodeList taskNodeList;
	ArrayList<Task> taskList;
	Document doc;
	int Selected_Peer_Node = 0;
	int cell_id = 0;

	public void workflow_parser(Location location, String workflowFileName)
	{
		taskList = new ArrayList<Task>();
		
		for (int i = 0; i < 100; i++)
		{
			SourceFilesData.add(i, new HashMap<String, String>());
		}

		try
		{
			File workflowFile = new File(workflowFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(workflowFile);
			doc.getDocumentElement().normalize();

			taskNodeList = doc.getElementsByTagName("Task");

			if (taskNodeList.getLength() > 0)
			{
				Task_Parser(0, location);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void Task_Parser(int taskIndex, Location location)
	{
		Task task = new Task();
		
		try
		{
			Node TaskNode = taskNodeList.item(taskIndex);
			
			if (TaskNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element TaskElement = (Element) TaskNode;
				task_ID = Integer.parseInt(TaskElement.getAttribute("id"));
				task.id = task_ID;
			}
			
			Element taskElement = (Element) doc.getElementsByTagName("Task").item(taskIndex);
			
			Communcation_Message[task_ID] = "<?xml version=\"1.0\"?><response>";

			// get the services of this task
			NodeList servicesNodeList = taskElement.getElementsByTagName("Gamma");

			task_services_count[task_ID] = servicesNodeList.getLength();
			
			if (servicesNodeList.getLength() > 1) 
			{
				// peer node selection according to location
				Selected_Peer_Node = peerNodeSearch(location);
				
				PeerNodes_DAL peer_nodes_dal = new PeerNodes_DAL();
				task.mapped_peer_node = peer_nodes_dal.fetchPeerNodeByID(Selected_Peer_Node);
				
				Workflow_Coordinator_Main.peerNodeMqttClient = MQTTConnectionFactory.getMQTTClient(task.mapped_peer_node, Workflow_Coordinator_Main.task_in_execution, Workflow_Coordinator_Main.qos, Workflow_Coordinator_Main.Task_Execution_Count);

				cell_id = Selected_Peer_Node;
				
				// compose message to peer node for task deployment
				Communcation_Message[task_ID] = Communcation_Message[task_ID] + "<peer_node>" + Selected_Peer_Node + "</peer_node>" + "<Services>";
			}
			
			for (int serviceIndex = 0; serviceIndex < servicesNodeList.getLength(); serviceIndex++)
			{
				Service service = new Service();
				
				Node nNode = servicesNodeList.item(serviceIndex);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) nNode;
					service.id = Integer.parseInt(eElement.getAttribute("id"));
					service.service_name = eElement.getElementsByTagName("Service_Name").item(0).getTextContent();
					
					double latitude = Double.parseDouble(eElement.getElementsByTagName("Latitude").item(0).getTextContent());
					double longitude = Double.parseDouble(eElement.getElementsByTagName("Longitude").item(0).getTextContent());
					
					service.location = new Location(latitude, longitude);

					service.radius = Integer.parseInt(eElement.getElementsByTagName("Value").item(0).getTextContent());
					
					String availability_tag = eElement.getElementsByTagName("Availability").item(0).getTextContent();
					service.required_availability_duration = Integer.parseInt(availability_tag.replaceAll("[\\D]", ""));

					//----------------Hardware_Requirements----------
					System.out.println("CPU : " + eElement.getElementsByTagName("CPU").item(0).getTextContent());
					System.out.println("Network : " + eElement.getElementsByTagName("Network").item(0).getTextContent());
					System.out.println("RAM : " + eElement.getElementsByTagName("RAM").item(0).getTextContent());
					System.out.println("external Storage : " + eElement.getElementsByTagName("external_storage").item(0).getTextContent());

					//----------------Software_Requirements----------
					System.out.println("OS : " + eElement.getElementsByTagName("OS").item(0).getTextContent());
					System.out.println("Packages_List : " + eElement.getElementsByTagName("Packages_List").item(0).getTextContent());
					System.out.println("Service_Input : " + eElement.getElementsByTagName("Service_Input").item(0).getTextContent());
					System.out.println("Service_Output : " + eElement.getElementsByTagName("Service_Output").item(0).getTextContent());
					
					service.service_type = eElement.getElementsByTagName("Service_Type").item(0).getTextContent();
					service.deployment_status = eElement.getElementsByTagName("Deployable").item(0).getTextContent();
					service.deployable = service.deployment_status.equalsIgnoreCase("Deployable");

					if (service.deployable == false)
					{
						selectEndDeviceForNonDeployableService(service, location, Selected_Peer_Node, cell_id);
					}
					else if (service.deployable && service.service_type.contains("Fog Service"))
					{
						Fog_Service_Name[Fog_Service_Count] = service.service_name;
						Fog_Service_Availability[Fog_Service_Count] = service.required_availability_duration;
						Fog_Service_Type[Fog_Service_Count] = service.service_type;
						Fog_Deployable[Fog_Service_Count] = (service.deployable ? 1 : 0);
						Fog_Non_Deployable[Fog_Service_Count] = (service.deployable ? 0 : 1);
						Fog_Location_Latitude[Fog_Service_Count] = location.latitude;
						Fog_Location_Longitude[Fog_Service_Count] = location.longitude;
						Fog_Selected_Peer_Node[Fog_Service_Count] = Selected_Peer_Node;
						Fog_cell_id[Fog_Service_Count] = cell_id;
						fog_service_task_IDs[Fog_Service_Count] = task_ID;

						// search for end device
						selectEndDeviceForDeployableService(service, location, Selected_Peer_Node, cell_id);
						
						Fog_Service_IP_Address[Fog_Service_Count] = IP_Address;

						Fog_Service_Count = Fog_Service_Count + 1;
					}
					else
					{
						System.out.println("Unknown Service_Type");
					}
				}
				
				task.services.add(service);
			}
			
			taskList.add(task);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int selectDevice(Service service, Location workflowLocation)
	{
		int closestDevice = 0;
		int count = 0;
		
		try
		{
			int deviceId = 0;

			connect = new DBConnectionFactory().getConnection();
			
			String query = "SELECT * FROM services where Service_Name = '" + service.service_name + "' AND Availability >=" + service.required_availability_duration + " AND Service_Type = '" + service.service_type + "' AND Deployable =" + (service.deployable ? 1 : 0) + " AND Non_Deployable = " + (service.deployable ? 0 : 1);
			Statement st = connect.createStatement();
			ResultSet rs = st.executeQuery(query);
	
			while (rs.next())
			{
				// get the id of the device where service is deployed w.r.t. peer node and cell id
				deviceId = rs.getInt("Device_Id");
				// now select the device id from particular cell and peer node
				if (!(Selected_Device_ID_List.contains(Integer.toString(deviceId))))
				{
					String Query_Device = "SELECT * FROM devices where id = " + deviceId + " AND peer_node_id = " + Selected_Peer_Node + " AND cell_id = " + cell_id;
					Statement st_Device = connect.createStatement();
					ResultSet rs_Device = st_Device.executeQuery(Query_Device);
					
					// one service is deployed on more than one device in a single cell now we get the location of those devices
					while (rs_Device.next())
					{
						latitudes[count] = rs_Device.getDouble("Latitude");
						longitudes[count] = rs_Device.getDouble("Longitude");
						Device_List[count] = deviceId;
						count++;
					}
				}
	
			}
			
			st.close();
	
			// get the closest device with the incident location
			closestDevice = Helper.findMinimal(latitudes, longitudes, Device_List, count, workflowLocation);
			
			Devices_DAL devices_dal = new Devices_DAL();
			service.mapped_end_device = devices_dal.fetchDeviceByID(closestDevice);
			
			count_device_selection_list = count_device_selection_list + 1;
			
			if (count_device_selection_list == 1)
			{
				Selected_Device_ID_List = Integer.toString(closestDevice);
			}
			else
			{
				Selected_Device_ID_List = Selected_Device_ID_List + " ," + Integer.toString(closestDevice);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return closestDevice;
	}
	
	public void selectEndDeviceForNonDeployableService(Service service, Location workflowLocation, int Selected_Peer_Node, int cell_id) throws Exception
	{
		int closestDevice = selectDevice(service, workflowLocation);
		getDeviceIPAddress(closestDevice);
		add_URI_and_IP_to_Sensor_Comm_Message(service);
		addToSourceFiles(service);
	}
	
	public void selectEndDeviceForDeployableService(Service service, Location workflowLocation, int Selected_Peer_Node, int cell_id) throws Exception
	{
		int closestDevice = selectDevice(service, workflowLocation);
		add_URI_and_IP_to_Fog_Service_Comm_Message(service);
		addToSourceFiles(service);
		constructCommunicationAndDeploymentMessages(service, closestDevice);
	}
	
	public void addToSourceFiles(Service service)
	{
		String key = Integer.toString((task_ID - 1)) + Integer.toString((service.id - 1));
		int map_key = Integer.parseInt(key);
		SourceFilesData.add(map_key, new HashMap<String, String>());
	}
	
	public int peerNodeSearch(Location workflowLocation)
	{
		int closestNode = 0;

		try
		{
			int Peer_Node_Id = 0;
			int count = 0;
	
			connect = new DBConnectionFactory().getConnection();
		
			String query_peer_nodes = "SELECT * FROM peer_nodes";
			Statement st_peer_nodes = connect.createStatement();
			ResultSet rs_peer_nodes = st_peer_nodes.executeQuery(query_peer_nodes);

			while (rs_peer_nodes.next())
			{
				Peer_Node_Id = rs_peer_nodes.getInt("id");

				latitudes[count] = rs_peer_nodes.getDouble("Latitude");
				longitudes[count] = rs_peer_nodes.getDouble("Longitude");
				Peer_Node_List[count] = Peer_Node_Id;
				
				count++;
			}
			
			st_peer_nodes.close();
			
			// FindMinimal selects the closest node to the input location
			closestNode = Helper.findMinimal(latitudes, longitudes, Peer_Node_List, count, workflowLocation);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return closestNode;
	}

	public void selectAlternateDevice(String Service_Name, int Availability, String Service_Type, int Deployable, int Non_Deployable, Location workflowLocation, int Selected_Peer_Node, int cell_id) throws Exception
	{
		try
		{
			int Device_Id = 0;
			int count = 0;
			int closestDevice = 0;
			
			connect = new DBConnectionFactory().getConnection();
			
			String query = "SELECT * FROM services where Service_Name = '" + Service_Name + "' AND Availability >=" + Availability + " AND Service_Type = '" + Service_Type + "' AND Deployable = " + Deployable + " AND Non_Deployable = " + Non_Deployable;
			
			Statement st = connect.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				Device_Id = rs.getInt("Device_Id");
				String Query_Device = "SELECT * FROM devices where id = " + Device_Id + " AND peer_node_id = " + Selected_Peer_Node + " AND cell_id = " + cell_id;
				Statement st_Device = connect.createStatement();
		
				ResultSet rs_Device = st_Device.executeQuery(Query_Device);

				while (rs_Device.next())
				{
					latitudes[count] = rs_Device.getDouble("Latitude");
					longitudes[count] = rs_Device.getDouble("Longitude");
					Device_List[count] = Device_Id;
					count++;
				}
			}

			st.close();
		
			closestDevice = Helper.findMinimal(latitudes, longitudes, Device_List, count, workflowLocation);
			
			count_device_selection_list = count_device_selection_list + 1;

			if (count_device_selection_list == 1)
			{
				Selected_Device_ID_List = Integer.toString(closestDevice);
			}
			else
			{
				Selected_Device_ID_List = Selected_Device_ID_List + " ," + Integer.toString(closestDevice);
			}
			
			count = 0;
			
			getDeviceIPAddress(closestDevice);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void getDeploymentFilesDetail(int Closest_Device, String Service_Name, int gamma_no) throws Exception
	{
		try
		{
			int Deployment_Service_Id = 0;
			String Doployment_Service_URI = "";

			connect = new DBConnectionFactory().getConnection();
			
			String query = "SELECT * FROM services where Service_Name = '" + Service_Name + "' AND Device_Id =" + Closest_Device;
			System.out.println("Query = " + query);
			Statement st = connect.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				Deployment_Service_Id = rs.getInt("id");
				Doployment_Service_URI = rs.getString("URI");
				String Query_Deployment = "SELECT * FROM deployment where Deployment_Service_Id = " + Deployment_Service_Id;
				System.out.println("Query_Deployment " + Query_Deployment);
				Statement st_Deployment = connect.createStatement();
				ResultSet rs_Deployment = st_Deployment.executeQuery(Query_Deployment);
				
				while (rs_Deployment.next())
				{
					String Command = rs_Deployment.getString("Command");
					
					if (Command.contains("argument_11"))
					{
						String arg_11 = rs_Deployment.getString("arg_11");
						Command = Command.replace("argument_11", arg_11);
					}
					
					if (Command.contains("argument_10"))
					{
						String arg_10 = rs_Deployment.getString("arg_10");
						Command = Command.replace("argument_10", arg_10);
					}
					
					if (Command.contains("argument_1"))
					{
						String arg_1 = rs_Deployment.getString("arg_1");
						Command = Command.replace("argument_1", arg_1);
					}

					if (Command.contains("argument_2"))
					{
						String arg_2 = rs_Deployment.getString("arg_2");
						Command = Command.replace("argument_2", arg_2);
					}

					if (Command.contains("argument_3"))
					{
						String arg_3 = rs_Deployment.getString("arg_3");
						Command = Command.replace("argument_3", arg_3);
					}

					if (Command.contains("argument_4"))
					{
						String arg_4 = rs_Deployment.getString("arg_4");
						Command = Command.replace("argument_4", arg_4);
					}

					if (Command.contains("argument_5"))
					{
						String arg_5 = rs_Deployment.getString("arg_5");
						Command = Command.replace("argument_5", arg_5);
					}

					if (Command.contains("argument_6"))
					{
						String arg_6 = rs_Deployment.getString("arg_6");
						Command = Command.replace("argument_6", arg_6);
					}

					if (Command.contains("argument_7"))
					{
						String arg_7 = rs_Deployment.getString("arg_7");
						Command = Command.replace("argument_7", arg_7);
					}

					if (Command.contains("argument_8"))
					{
						String arg_8 = rs_Deployment.getString("arg_8");
						Command = Command.replace("argument_8", arg_8);
					}

					if (Command.contains("argument_9"))
					{
						String arg_9 = rs_Deployment.getString("arg_9");
						Command = Command.replace("argument_9", arg_9);
					}
					
					Deployment_Command.put("Command", Command);

					Communcation_Message[task_ID] = Communcation_Message[task_ID] + "<Command>" + Command + "</Command>";
					Deployment_Message[task_ID] = Deployment_Message[task_ID] + "<Command>" + Command + "</Command>";
					Deployment_Message_List[task_ID][(gamma_no - 1)] = Deployment_Message_List[task_ID][(gamma_no - 1)] + "<Command>" + Command + "</Command>";
					
					System.out.println("Deployment_Message_List[Task_Id][(gamma_no-1)] =" + Deployment_Message_List[task_ID][(gamma_no - 1)]);

					int assetCount = 15;
					
					for (int assetIndex = 1; assetIndex <= assetCount; assetIndex++)
					{
						String Source_Variable_Name = "Source_File_Address_" + assetIndex;
						String Source_File_Name = rs_Deployment.getString(Source_Variable_Name);

						if ((!(Source_File_Name.equals(""))) && (!(Source_File_Name.equals(null))) && (!(Source_File_Name.equals("Nil"))))
						{
							Deployment_Source_Files.put((Source_Variable_Name + "_" + task_ID), rs_Deployment.getString(Source_Variable_Name));
							
							if (rs_Deployment.getString(Source_Variable_Name).contains("config"))
							{
								Communcation_Message[task_ID] = Communcation_Message[task_ID] + "<Configuration_File>config.txt</Configuration_File>";
								Deployment_Message[task_ID] = Deployment_Message[task_ID] + "<Configuration_File>config.txt</Configuration_File>";
								Deployment_Message_List[task_ID][(gamma_no - 1)] = Deployment_Message_List[task_ID][(gamma_no - 1)] + "<Configuration_File>config.txt</Configuration_File>";
							}
						}
					}

					String key = Integer.toString((task_ID - 1)) + Integer.toString((gamma_no - 1));

					int map_key = Integer.parseInt(key);
					SourceFilesData.add(map_key, Deployment_Source_Files);
				}
			}
		
			st.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void getDeviceIPAddress(int Closest_Device) throws Exception
	{
		Devices_DAL devices_DAL = new Devices_DAL();
		Device device = devices_DAL.fetchDeviceByID(Closest_Device);
		
		if(device != null)
		{
			IP_Address = device.IP_Address;
		}
		else
		{
			System.out.println("Closest device " + Closest_Device + " not found!");
		}
	}
	
	public void getDeviceURI(Service service)
	{
		connect = new DBConnectionFactory().getConnection();
		
		try
		{
			String query = "SELECT * FROM services where Service_Name = '" + service.service_name + "' AND Device_Id =" + service.mapped_end_device.id;

			Statement st = connect.createStatement();
			ResultSet rs = st.executeQuery(query);

			if(rs.next())
			{
				service.uri = rs.getString("URI");
			}
			else
			{
				System.out.println("URI not found for service " + service.service_name + " and device " + service.uri);
			}

			st.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void add_URI_and_IP_to_Fog_Service_Comm_Message(Service service) throws Exception
	{
		getDeviceURI(service);
		Fog_Services.put(service.service_name, service.uri);
		Communcation_Message[task_ID] = Communcation_Message[task_ID] + "<Service name=\"" + service.service_name + "\">" + "<URI>" + service.uri + "</URI>" + "<IP_Address>" + service.mapped_end_device.IP_Address + "</IP_Address><Type>" + service.service_type + "</Type><Deployment>" + (service.deployable ? 1 : 0) + "</Deployment></Service>";
	}

	public void add_URI_and_IP_to_Sensor_Comm_Message(Service service) throws Exception
	{
		getDeviceURI(service);
			
		Sensors.put(service.service_name, service.uri);

		if (service.service_type.contains("sensor"))
		{
			Communcation_Message[task_ID] = Communcation_Message[task_ID] + "<Service name=\"" + service.service_name + "\">" + "<URI>" + service.uri + "</URI>" + "<IP_Address><Source>" +  service.mapped_end_device.IP_Address + "</Source><Destination>NA</Destination></IP_Address><Type>" + service.service_type + "</Type><Deployment>" + (service.deployable ? 1 : 0) + "</Deployment></Service>";
		}
		else if (service.service_type.contains("Fog Service"))
		{
			Communcation_Message[task_ID] = Communcation_Message[task_ID] + "<Service name=\"" + service.service_name + "\">" + "<URI>" + service.uri + "</URI>" + "<IP_Address>" + service.mapped_end_device.IP_Address + "</IP_Address><Type>" + service.service_type + "</Type><Deployment>" + (service.deployable ? 1 : 0) + "</Deployment></Service>";
		}
		else if (service.service_type.contains("Cloud Service"))
		{
			Communcation_Message[task_ID] = Communcation_Message[task_ID] + "<Service name=\"" + service.service_name + "\">" + "<URI>" + service.uri + "</URI>" + "<IP_Address>" + service.mapped_end_device.IP_Address + "</IP_Address><Type>" + service.service_type + "</Type><Deployment>" + (service.deployable ? 1 : 0) + "</Deployment></Service>";
		}
	}
	
	public void constructCommunicationAndDeploymentMessages(Service service, int closestDevice) throws Exception
	{
		Communcation_Message[task_ID] = Communcation_Message[task_ID] + "</Services><Deployment><Task>["+ task_ID + "]</Task><Service_Name>" + service.service_name + "</Service_Name><IP_Address>" + IP_Address + "</IP_Address>";
		Deployment_Message[task_ID] = "<?xml version=\"1.0\"?><Deployment><Task>[" + task_ID + "]</Task><Service_Name>" + service.service_name + "</Service_Name><IP_Address>" + IP_Address + "</IP_Address>";
		Deployment_Message_List[task_ID][(service.id - 1)] = "<?xml version=\"1.0\"?><Deployment><Task>[" + task_ID + "]</Task><Service_Name>" + service.service_name + "</Service_Name><IP_Address>" + IP_Address + "</IP_Address>";

		String Destination_IP_Address_Tag = "<Destination>" + service.mapped_end_device.IP_Address + "</Destination>";
		Communcation_Message[task_ID] = Communcation_Message[task_ID].replace("<Destination>NA</Destination>", Destination_IP_Address_Tag);

		// from the deployment table, we get the information of files which need to be transferred for respective service
		getDeploymentFilesDetail(closestDevice, service.service_name, service.id);
		Communcation_Message[task_ID] = Communcation_Message[task_ID] + "</Deployment></response>";
		
		System.out.println("Communcation_Message[Task_Id] " + Communcation_Message[task_ID]);
	
		Deployment_Message[task_ID] = Deployment_Message[task_ID] + "</Deployment>";
		Deployment_Message_List[task_ID][(service.id - 1)] = Deployment_Message_List[task_ID][(service.id - 1)] + "</Deployment>";
	}
}