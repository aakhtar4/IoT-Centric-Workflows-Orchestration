package coordination;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.server.Server;
import io.moquette.server.config.ClasspathConfig;
import io.moquette.server.config.IConfig;
import util.Preparation;

import java.lang.Runtime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Iterator;
import java.util.Map;

import java.util.Random;
import java.io.Writer;
import java.io.Reader;

public class Main_Rasp_Combine
{
	static MqttClient sampleClient_RP=null;
	static MqttClient sampleClient=null;
	static MqttClient sampleClient_RP2=null;
	static MqttClient IoT_Device_1_Client=null;
	static MqttClient IoT_Device_2_Client=null;
	static MqttConnectOptions connOpts_RP=null;
	static MqttConnectOptions connOpts_RP2=null;
	static MqttConnectOptions connOpts=null;
	static MqttConnectOptions connOpts_IoT_Device_1=null;
	static MqttConnectOptions connOpts_IoT_Device_2=null;
	static int qos = 1;
	static String Location=null;
	public static BufferedReader error;
	public static BufferedReader op;
	public static int exitVal;
	public static String Execution_Command= null;
	public static String Configuration_File = null;
	public static String execution_command_List[] = new String[100];
	public static int task_id = 0;
	public static String task_output = "";
	public static String IoT_Device_1 = "";
	public static String IoT_Device_2 = "";
	public static String IoT_Device_1_ID = "";
	public static String IoT_Device_2_ID = "";
	public static String Deployment_Message = "";
	public static String Deployment_Message_List[] = new String[100];
	public static String Deployment_Task_Details[] = new String[100];
	public static int Deployment_Task_Count = 0;
	public static String Deployment_IP_Address[] = new String[100];
	public static int Deployment_IP_Address_Count = 0;
	public static MqttClient Deployment_Client_Name[] = new MqttClient[100];
	public static String output_other_tasks[][] = new String[100][100];
	public static int output_other_task_count[] = new int[100];
	public static int left_informed_flag = 0;
	public static int Assignment_deployment_task_id = 0;
	public static int deployment_service_count = 0;
	public static int deployment_client_count = 0;
	public static Mobility m1;
	
	public static final Pattern TAG_REGEX_LEFT = Pattern.compile("<LHS>(.+?)</LHS>");	
	public static final Pattern TAG_REGEX_RIGHT = Pattern.compile("<RHS>(.+?)</RHS>");	
	
	
	static class PublisherListener extends AbstractInterceptHandler
	{
		@Override
		public void onPublish(InterceptPublishMessage message)
		{
			String text_message = new String(message.getPayload().array());

			if(message.getTopicName().equals("iot-2/evt/accel/fmt/json"))
			{
				System.out.println("-------------------------------------------------");
				System.out.println("| Received ");
				System.out.println("| Topic: "+message.getTopicName());
				System.out.println("| Message: "+text_message);
				System.out.println("| QoS: "+message.getQos());
				System.out.println("-------------------------------------------------");
			}
			//orchestration message from Workflow coordinator
			else if(message.getTopicName().equals("service_execution_RP1"))
			{
				System.out.println("-------------------------------------------------");
				System.out.println("| Received ");
				System.out.println("| Topic: "+message.getTopicName());
				System.out.println("| Message: "+text_message);
				System.out.println("| QoS: "+message.getQos());
				System.out.println("-------------------------------------------------");
				  
				if(text_message.contains("</Task> Vehicle_Status_Info"))
				{
					if(IoT_Device_2_Client.isConnected())
					{
						try
						{
							MqttMessage str_message = new MqttMessage(text_message.getBytes());
							str_message.setQos(qos);
							IoT_Device_2_Client.publish("Service_Execution_Peer_Node", str_message);
						}
						catch (MqttException me)
						{
							me.printStackTrace();
						}
						
					}
					else
					{
						try
						{
							IoT_Device_2_Client.connect(connOpts_IoT_Device_2);
							MqttMessage str_message = new MqttMessage(text_message.getBytes());
							str_message.setQos(qos);	
							IoT_Device_2_Client.publish("Service_Execution_Peer_Node", str_message);
						}
						catch (MqttException me)
						{
							me.printStackTrace();
						}
					}
				}
					
				if(text_message.contains("New IP_Address"))
				{
					System.out.println("-------------------------------------");
					System.out.println(text_message);
					System.out.println("-------------------------------------");
					for(int op=0; op<20; op++)
					{
						if(text_message.contains(Mobility.device_IP_Address[op]))
						{
							System.out.println("Residence Time ="+Mobility.residence_time_minutes[op]);
							Mobility.residence_time_minutes[op] = Mobility.residence_time_assigned[op];
							Mobility.startTime[op] = System.currentTimeMillis();
						}
					}
				}
			}
			else
			{
				System.out.println("-------------------------------------------------");
				System.out.println("| Received ");
				System.out.println("| Topic: "+message.getTopicName());
				System.out.println("| Message: "+text_message);
				System.out.println("| QoS: "+message.getQos());
				System.out.println("-------------------------------------------------");
			}
			
			if((new String(message.getPayload().array()).contains("text")) && !(new String(message.getPayload().array()).contains("URL")))
			{
				text_message = text_message.replace("{\"d\":{\"text\":","");
				int start_index = text_message.indexOf("\"")+1;
				int end_index = text_message.indexOf("\" }");
				try
				{
					Location = text_message.substring(start_index, end_index);
				}
				catch(Exception e)
				{
					System.out.println("Message does not contain location information");
				}
			}
			
			Location = "London";
			
	         //mesasge received from End Device after successful execution
			if(new String(message.getPayload().array()).contains("Done with Execution from IP: ["))
			{
				String content = "Done with Execution";
		        MqttMessage execution_message = new MqttMessage(content.getBytes());
		        execution_message.setQos(qos);

		        if(sampleClient_RP.isConnected())
		        {
		        	try
		        	{
		        		sampleClient_RP.publish("iot_data", execution_message);
		        	}
		        	catch (MqttException me)
		        	{
						me.printStackTrace();
					}
		        }
		        else
		        {
		        	try
					{
		        		System.out.println("Here connecting");
		        		sampleClient_RP.connect(connOpts);
		        		sampleClient_RP.publish("iot_data", execution_message);
					}
		        	catch (MqttException me)
		        	{
						me.printStackTrace();
					}
		        }
		        
		        content = "Continue Processing";
				System.out.println("-------------------------------------------");
				System.out.println("Streaming");
				System.out.println("-------------------------------------------");	
				MqttMessage streaming_message = new MqttMessage(content.getBytes());
				streaming_message.setQos(qos);

				if(IoT_Device_2_Client.isConnected())
				{
					try
					{
						System.out.println("Here is IF");	
						IoT_Device_2_Client.publish("Service_Execution_Peer_Node", streaming_message);
					}
					catch (MqttException me)
					{
						me.printStackTrace();
					}
				}
				else
				{
					try
					{
						int connect_status = 0;
						if(IoT_Device_2_Client.isConnected() && connect_status == 0)
						{
							IoT_Device_2_Client.publish("Service_Execution_Peer_Node", streaming_message);
							connect_status = 1;
						}
					}
					catch (MqttException me)
					{
							me.printStackTrace();
					}	
				}	
			}
			
			//received task output from end device
			if(new String(message.getPayload().array()).contains("Task Output is:"))
			{
				int output_task_id = 0;
				String task_output = new String(message.getPayload().array());
				
				if(task_output.contains("<Task>"))
				{
					output_task_id = Integer.parseInt(Deployment_Message.substring(Deployment_Message.indexOf("[") + 1, Deployment_Message.indexOf("]")));
					System.out.println("output_task_id ="+output_task_id);
				}
				
				task_output = task_output.replace("Task Output is: ", "");
				output_other_task_count[output_task_id]  = output_other_task_count[output_task_id] + 1;
				
				output_other_tasks[output_task_id][output_other_task_count[output_task_id]] = task_output;
				
		        MqttMessage execution_output = new MqttMessage(task_output.getBytes());
		        
		        execution_output.setQos(qos);
		      
		        //sharing output with workflow coordinator
		        if(sampleClient_RP.isConnected())
		        {
		        	try
		        	{
		        		sampleClient_RP.publish("iot_data", execution_output);
		        	}
		        	catch (MqttException me)
		        	{
						me.printStackTrace();
					}
		        }
		        else
		        {
		        	try
					{
		        		System.out.println("Here connecting");
						
		        		sampleClient_RP.connect(connOpts);
		        		sampleClient_RP.publish("iot_data", execution_output);
						
					}
		        	catch (MqttException me)
		        	{
						me.printStackTrace();
					}
		        }
		        
		        String content = "Continue Processing";
				
		        System.out.println("-------------------------------------------");
				System.out.println("Streaming");
				System.out.println("-------------------------------------------");	
				
				MqttMessage streaming_message = new MqttMessage(content.getBytes());
				streaming_message.setQos(qos);

				//sending streaming message
				if(IoT_Device_2_Client.isConnected())
				{
					try
					{
						System.out.println("Here is IF");	
						IoT_Device_2_Client.publish("Service_Execution_Peer_Node", streaming_message);
					}
					catch (MqttException me)
					{
						me.printStackTrace();
					}
				}
				else
				{
					try
					{
						System.out.println("Here is ELSE");	
					
						int connect_status = 0;
						if(IoT_Device_2_Client.isConnected() && connect_status == 0)
						{
						IoT_Device_2_Client.publish("Service_Execution_Peer_Node", streaming_message);
						connect_status = 1;
						}
					}catch (MqttException me)
					{
						me.printStackTrace();
					}	
				}	
			}
			
			//receive status update from end device
			if(new String(message.getPayload().array()).contains("I am Alive"))
			{
				String status_message = new String(message.getPayload().array());
				System.out.println("status_message ="+status_message);
				
				for(int cd = 0; cd<20; cd++)
				{
					System.out.println("m1.device_IP_Address[cd] ="+Mobility.device_IP_Address[cd]);
				
					if(status_message.contains(Mobility.device_IP_Address[cd]))
					{
						System.out.println("Status Updation =" + Mobility.device_IP_Address[cd]);
						m1.device_last_update_time[cd] = System.currentTimeMillis();
					}
				}
			}
			
			//sending execution status to peer node
			if(new String(message.getPayload().array()).contains("Send with Your Task Execution Status"))
			{
				String content = "Done with Execution";
		        MqttMessage execution_message = new MqttMessage(content.getBytes());
		        execution_message.setQos(qos);
		        if(sampleClient_RP.isConnected())
		        {
		        //System.out.println("Here connected");
		        	try
		        	{
		        		sampleClient_RP.publish("iot_data", execution_message);
		        	}
		        	catch (MqttException me)
		        	{
						me.printStackTrace();
					}
		        }
		        else
		        {
		        	try
					{
		        		System.out.println("Here connecting");
		        		sampleClient_RP.connect(connOpts);
		        		sampleClient_RP.publish("iot_data", execution_message);
					}
		        	catch (MqttException me)
		        	{
						me.printStackTrace();
					}
		        }
			}
			
			//received orchestration message from Workflow Coordinator
			if(new String(message.getPayload().array()).contains("<?xml version=\"1.0\"?><response>"))
			{
				String mesg = new String(message.getPayload().array());
				  System.out.println("You should be here");
				  String xmlStr= new String(message.getPayload().array());
				  Document doc = convertStringToDocument(xmlStr); 
				  parseXML(doc);
				  
				  if(left_informed_flag == 1)
				  {
					  left_informed_flag = 0;
					  
					  if(sampleClient_RP.isConnected())
					  {
				        	try
				        	{
				        		//for result evaluation
				        		if(mesg.contains("<Task>[1]</Task>"))
				        		{
					        		String one = "<hint>[1]</hint> End";
					        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
					        		deployment_msg.setQos(qos);
					        		
					        		sampleClient_RP.publish("iot_data", deployment_msg);
				        		}
				        		
				        		if(mesg.contains("<Task>[4]</Task>"))
				        		{
					        		String four = "<hint>[4]</hint> End";
					        		MqttMessage deployment_msg = new MqttMessage(four.getBytes());
					        		deployment_msg.setQos(qos);
					        		
					        		sampleClient_RP.publish("iot_data", deployment_msg);
				        		}
				        	}
				        	catch (MqttException me)
				        	{
								me.printStackTrace();
							}
			        }
			        else
			        {
			        	left_informed_flag = 0;
			        	
			        	try
						{
			        		if(mesg.contains("<Task>[1]</Task>"))
			        		{
				        		String one = "<hint>[1]</hint> End";
				        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
				        		deployment_msg.setQos(qos);
				        		
				        		sampleClient_RP.connect(connOpts);
				        		sampleClient_RP.publish("iot_data", deployment_msg);
			        		}
			        		
			        		if(mesg.contains("<Task>[4]</Task>"))
			        		{
				        		String four = "<hint>[4]</hint> End";
				        		MqttMessage deployment_msg = new MqttMessage(four.getBytes());
				        		deployment_msg.setQos(qos);
				        		
				        		sampleClient_RP.connect(connOpts);
				        		sampleClient_RP.publish("iot_data", deployment_msg);
			        		}
						}
			        	catch (MqttException me)
			        	{
							me.printStackTrace();
						}
			        }
				  }
			  }
			
			//received streaming message from workflow coordinator
			if(new String(message.getPayload().array()).contains("Streaming"))
			{
				String content = "Streaming";
				MqttMessage streaming_message = new MqttMessage(content.getBytes());
				streaming_message.setQos(qos);
				
					
				if(IoT_Device_2_Client.isConnected())
				{
					try
					{
						System.out.println("Here is IF");	
						IoT_Device_2_Client.publish("Service_Execution_Peer_Node", streaming_message);
					}
					catch (MqttException me)
					{
						me.printStackTrace();
					}
				}
				else
				{
					try
					{
						int connect_status = 0;
						
						if(IoT_Device_2_Client.isConnected() && connect_status == 0)
						{
							IoT_Device_2_Client.publish("Service_Execution_Peer_Node", streaming_message);
							connect_status = 1;
						}
					}
					catch (MqttException me) {
							me.printStackTrace();
					}	
				}
			}
			
			if(new String(message.getPayload().array()).contains("<?xml version=\"1.0\"?><Deployment>"))
			{
				Deployment_Message = new String(message.getPayload().array());
				Deployment_Message_List[deployment_service_count] = new String(message.getPayload().array());
				deployment_service_count = deployment_service_count + 1;
				
				if(Deployment_Message.contains("<Task>"))
				{
					Assignment_deployment_task_id = Integer.parseInt(Deployment_Message.substring(Deployment_Message.indexOf("[") + 1, Deployment_Message.indexOf("]")));
				}
				
				if(deployment_service_count == 0 && Deployment_Task_Count > 0)
				{
					Deployment_Task_Count = 0;
				}
				
				Deployment_Task_Details[Deployment_Task_Count] = Deployment_Message;
				Deployment_Task_Count = Deployment_Task_Count + 1;

				for(int cd = 0; cd<20; cd++)
				{
					if(Deployment_Message.contains(Mobility.device_IP_Address[cd]))
					{
						System.out.println("Status Updation in deployment =" + Mobility.device_IP_Address[cd]);
						m1.device_last_update_time[cd] = System.currentTimeMillis();
					
					}
				}
				
			}
			
			if(new String(message.getPayload().array()).contains("Deploy Vehicle Status Service"))
			{
				try
				{
					FileReader in = new FileReader("config.txt");
					BufferedReader br = new BufferedReader(in);
	
					String output;
					String line_content = "";
					
					String Jar_File_Name="";
	
					while ((output = br.readLine()) != null)
					{
						line_content = output;
						if(line_content.contains("input:"))
						{
							if(line_content.contains("["))
							{
								String Device_URI = line_content.substring(line_content.indexOf("[") + 1, line_content.indexOf("]"));
								System.out.println(Device_URI);
								try
								{
									sampleClient_RP.subscribe(Device_URI);
								}
								catch (MqttException me)
								{
									me.printStackTrace();
								}
							}
						}
						
						if(line_content.contains("File_Name:"))
						{
							Jar_File_Name = line_content.substring(line_content.indexOf("[") + 1, line_content.indexOf("]"));
						}
					}
					
					in.close();
					
					List<String> arg_list = new ArrayList<String>();
					
					try
					{
						Get_Vehicle_Status(Jar_File_Name, arg_list);
					}
					catch (Exception e)
					{
				        e.printStackTrace();
				    }
				}
				catch (IOException e)
				{
			        e.printStackTrace();
			    }
			}
			
			//share output with other peer node
			if(new String(message.getPayload().array()).contains("Share output with"))
			{
				String received_message = new String(message.getPayload().array());
				
				if(received_message.contains("["))
				{
					String Device_URI = received_message.substring(received_message.indexOf("[") + 1, received_message.indexOf("]"));
					System.out.println("Device URI:"+Device_URI);
					
					try
					{
						String broker_RP2 = Device_URI;
						String clientId_RP2 = "RP2-Client_"+broker_RP2;
						System.out.println("clientId_RP2 ="+clientId_RP2);

						/*****************************************PAHO CLIENT START*************************/
						sampleClient_RP2 = new MqttClient(broker_RP2, clientId_RP2, new MemoryPersistence());
						connOpts_RP2 = new MqttConnectOptions();
						connOpts_RP2.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
						connOpts_RP2.setCleanSession(true);
						connOpts_RP2.setKeepAliveInterval(1000); 
						
						sampleClient_RP2.connect(connOpts_RP2);
						sampleClient_RP2.subscribe("output_RP2", qos);
						System.out.println("Raspberry Pi 1 subscribed to topic output_RP2");
						
						sampleClient_RP2.setCallback( new SimpleMqttCallBack()
						{
							 public void connectionLost(Throwable throwable)
							 {
								    System.out.println("Listening sampleClient_RP2......");
							 }
	
							  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
							  {
								  //System.out.println("Here3");
								  System.out.println("-------------------------------------------------");
								  System.out.println("| Received ");
								  System.out.println("| Topic: "+topic);
								  System.out.println("| Message: "+new String(mqttMessage.getPayload()));
								  System.out.println("| QoS: "+mqttMessage.getQos());
								  System.out.println("-------------------------------------------------");
							  }

							  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
							  {
							  }
				        });
						
						
					
						try{
							FileReader in = new FileReader("output.kml");
							BufferedReader br = new BufferedReader(in);
	
							String output;
							String xml_output = "";
							
							while ((output = br.readLine()) != null)
							{
								xml_output = xml_output + output;
				            }
							
							in.close();

							try
							{
							 MqttMessage message_kml = new MqttMessage(xml_output.getBytes());
							 message_kml.setQos(qos);
							 
							 if(sampleClient_RP2.isConnected())
							 {
								 sampleClient_RP2.publish("output_RP1", message_kml);
							 }
							 else
							 {
								 sampleClient_RP2.connect(connOpts);
								 sampleClient_RP2.publish("output_RP1", message_kml);
							 }
							}
							catch (Throwable e)
							{
								e.printStackTrace();
							}
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}	
					}
					catch (MqttException me)
					{
						me.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public static int generate_random_number()
	{
		int max = 4;
		int min = 0;
		Random rand = new Random(); 
		int device = rand.nextInt((max - min) + 1) + min;
		return device;
	}
	

	public static Document convertStringToDocument(String xmlStr)
	{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        }
        catch (Exception e)
        {  
            e.printStackTrace();  
        } 
        
        return null;
    }
	
	
	public static void parseXML(Document doc)
    {
    	doc.getDocumentElement().normalize();
    	int deployment_count_chk = 0;
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
		NodeList nServiceList = doc.getElementsByTagName("Services"); 
		
		for(int temp = 0 ; temp <nServiceList.getLength(); temp++){ 
		Node nNode = nServiceList.item(temp); 
		Element eElement = (Element) nNode; 
		NodeList childList = eElement.getChildNodes(); 
		String [] sService_URI = new String[childList.getLength()] ; 
		String [] sService_Source_IP_Add = new String[childList.getLength()] ; 
		String [] sService_IP_Add = new String[childList.getLength()] ; 
		String [] sService_Destination_IP_Add = new String[childList.getLength()] ; 
		String [] sService_Type = new String[childList.getLength()] ; 
		String [] sService_Name = new String[childList.getLength()] ; 
		String [] sService_Deployment = new String[childList.getLength()] ; 
		
		for(int i = 0; i < childList.getLength(); i++)
		{ 
			Node childNode = childList.item(i); 
			
			
			String Service_Name = childNode.getAttributes().getNamedItem("name").getNodeValue();
			
			sService_Name[i] = Service_Name;
			
			NodeList service_detail_nodeList = childNode.getChildNodes();
			for(int kk=0;kk<service_detail_nodeList.getLength(); kk++)
			{
				Node service_detail_node = service_detail_nodeList.item(kk);
				
				if(service_detail_node.getNodeName().contains("URI"))
				sService_URI[i] = service_detail_node.getTextContent();
				if(service_detail_node.getNodeName().contains("IP"))
				{
					NodeList IP_Address_nodeList = service_detail_node.getChildNodes();
					
					if(IP_Address_nodeList.getLength() > 1)
					{
					for(int mm=0; mm<IP_Address_nodeList.getLength(); mm++)
					{
						Node IP_Address_node = IP_Address_nodeList.item(mm);
						if(IP_Address_node.getNodeName().contains("Source"))
						sService_Source_IP_Add[i] = IP_Address_node.getTextContent();
						if(IP_Address_node.getNodeName().contains("Destination"))
						sService_Destination_IP_Add[i] = IP_Address_node.getTextContent();
					}
					}
					else
					{
						sService_IP_Add[i] = service_detail_node.getTextContent();
					}
				}
				
				if(service_detail_node.getNodeName().contains("Type"))
				sService_Type[i] = service_detail_node.getTextContent();
				if(service_detail_node.getNodeName().contains("Deployment"))
				sService_Deployment[i] = service_detail_node.getTextContent();
				
			}
			if(sService_Type[i].contains("sensor") && sService_Deployment[i].contains("0"))
			{
				IoT_Device_1 = "tcp://"+sService_Source_IP_Add[i]+":1883";
				System.out.println("IoT_Device_1: "+IoT_Device_1);
				System.out.println("\n**********\nSensor\n************\n");
				try{
				IoT_Device_1_Client = new MqttClient(IoT_Device_1, IoT_Device_1_ID , new MemoryPersistence());
				connOpts_IoT_Device_1 = new MqttConnectOptions();
				connOpts_IoT_Device_1.setCleanSession(true);
				
				connOpts_IoT_Device_1.setKeepAliveInterval(1000); 
				
				
				System.out.println("Peer Node 1 connecting to IoT Device 1: " + IoT_Device_1);
				IoT_Device_1_Client.setCallback( new SimpleMqttCallBack()  {
					 public void connectionLost(Throwable throwable) {
						    //System.out.println("Connection to MQTT broker lost!");
						    System.out.println("Listening IoT_Device_1_Client......");
						  }
	
						  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
						    System.out.println("Message received here:\n\t"+ new String(mqttMessage.getPayload()) );
						    //sampleClient_RP.publish("iot_data", mqttMessage);
						  }
	
						  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
						    // not used in this example
						  }
		        });
				
				IoT_Device_1_Client.connect(connOpts_IoT_Device_1);
				System.out.println("Peer Node 1 Conencted to IoT Device 1");
				String content_IoT_Device_1 = "Publish "+Service_Name+" Data to IP: ["+sService_Destination_IP_Add[i]+"]";
				
				System.out.println("1 Peer Node publishing message: " + content_IoT_Device_1);
				MqttMessage message = new MqttMessage(content_IoT_Device_1.getBytes());
				message.setQos(qos);
				if(IoT_Device_1_Client.isConnected())
				{
					IoT_Device_1_Client.publish("Service_Execution_Peer_Node", message);
				}else
				{
					try
					{
						/*Here commenting Connect is already in Progress*/
						//IoT_Device_1_Client.connect(connOpts_IoT_Device_1);
						IoT_Device_1_Client.publish("Service_Execution_Peer_Node", message);
						/*Here commenting Connect is already in Progress*/
						
					}catch (MqttException me) {
						me.printStackTrace();
					}
				}
				Thread.sleep(200);
				}
				catch (MqttException me)
				{
					me.printStackTrace();
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}
			}
			
			if(sService_Type[i].contains("Fog Service") && sService_Deployment[i].contains("0"))
			{
				IoT_Device_2 = "tcp://"+sService_IP_Add[i]+":1883";
				
				if(IoT_Device_2_Client != null)
				{
					try
					{
						String content_IoT_Device_2 = "Deploy "+Service_Name;
						
						System.out.println("2 Peer Node publishing message: " + content_IoT_Device_2);
						MqttMessage message = new MqttMessage(content_IoT_Device_2.getBytes());
						message.setQos(qos);
						IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);
					}
					catch (MqttException me)
					{
						me.printStackTrace();
					}
				}
				else
				{
					try
					{
						IoT_Device_2_Client = new MqttClient(IoT_Device_2, IoT_Device_2_ID , new MemoryPersistence());
						connOpts_IoT_Device_2 = new MqttConnectOptions();
						connOpts_IoT_Device_2.setCleanSession(true);
						//connOpts_IoT_Device_2.setConnectionTimeout(1000);
						connOpts_IoT_Device_2.setKeepAliveInterval(1000); 
						//connOpts_IoT_Device_2.setAutomaticReconnect(true);
						
						System.out.println("Peer Node 1 connecting to IoT Device 2: " + IoT_Device_2);
						IoT_Device_2_Client.setCallback( new SimpleMqttCallBack()  {
							 public void connectionLost(Throwable throwable) {
								    //System.out.println("Connection to MQTT broker lost!");
								 System.out.println("Listening IoT_Device_2_Client......");
								  }
			
								  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
								    System.out.println("Message received here:\n\t"+ new String(mqttMessage.getPayload()) );
								    //sampleClient_RP.publish("iot_data", mqttMessage);
								  }
			
								  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
								    // not used in this example
								  }
				        });
						
						IoT_Device_2_Client.connect(connOpts_IoT_Device_2);
						System.out.println("PC-client connected to broker");
						String content_IoT_Device_2 = "Deploy "+Service_Name;
						
						System.out.println("3 Peer Node publishing message: " + content_IoT_Device_2);
						MqttMessage message = new MqttMessage(content_IoT_Device_2.getBytes());
						message.setQos(qos);
						if(IoT_Device_2_Client.isConnected())
						{
							IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);
						}else
						{
							try
							{
								IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);
								
							}catch (MqttException me) {
								me.printStackTrace();
							}
						}
						Thread.sleep(200);
					}
					catch (MqttException me)
					{
						me.printStackTrace();
					}
					catch (Throwable e)
					{
						e.printStackTrace();
					}
				}
			}
			
			if(sService_Type[i].contains("Fog Service") && sService_Deployment[i].contains("1"))
			{
				Deployment_Message = Deployment_Message_List[deployment_count_chk];
				deployment_count_chk = deployment_count_chk + 1;
				deployment_service_count = deployment_service_count - 1;
				int flag = 0;
				Deployment_IP_Address_Count = 0;
				
				if(IoT_Device_2.contains(sService_IP_Add[i])){flag = 1;}else{flag = 0;}
				
				Deployment_IP_Address[Deployment_IP_Address_Count] = sService_IP_Add[i];
				
				System.out.println("**************IoT_Device_2****************** ="+IoT_Device_2);
				IoT_Device_2 = "tcp://"+sService_IP_Add[i]+":1883";
				
				if(IoT_Device_2_Client != null && flag == 1)
				{
					System.out.println("***********************Connected*******************");
					
					try
					{
						String content_IoT_Device_2 = "Invoke "+Service_Name;
						
						MqttMessage message = new MqttMessage(content_IoT_Device_2.getBytes());
						message.setQos(qos);
						
						if(IoT_Device_2_Client.isConnected())
						{
							IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);
							
							message = new MqttMessage(Deployment_Message.getBytes());
							message.setQos(qos);
							IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);
						}
						else
						{
							IoT_Device_2_Client.connect(connOpts_IoT_Device_2);
							
							int connect_status = 0;
							if(IoT_Device_2_Client.isConnected() && connect_status == 0)
							{
								IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);
								System.out.println("Deployment Message is: " + Deployment_Message);
								message = new MqttMessage(Deployment_Message.getBytes());
								message.setQos(qos);
								IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);
								connect_status = 1;
							}
						}
					}
					catch (MqttException me)
					{
						me.printStackTrace();
					}
				}
				else
				{
					System.out.println("******************Not Connected***************");	
					
						try{
						IoT_Device_2_Client = new MqttClient(IoT_Device_2, IoT_Device_2_ID , new MemoryPersistence());
						connOpts_IoT_Device_2 = new MqttConnectOptions();
						connOpts_IoT_Device_2.setCleanSession(true);
						//connOpts_IoT_Device_2.setConnectionTimeout(1000);
						connOpts_IoT_Device_2.setKeepAliveInterval(1000); 
						//connOpts_IoT_Device_2.setAutomaticReconnect(true);
						
						System.out.println("Peer Node 1 connecting to IoT Device 2: " + IoT_Device_2);
						IoT_Device_2_Client.setCallback( new SimpleMqttCallBack()  {
							 public void connectionLost(Throwable throwable) {
								    //System.out.println("Connection to MQTT broker lost!");
								    System.out.println("Listening IoT_Device_2_Client......");
								  }
			
								  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
								    System.out.println("Message received here:\n\t"+ new String(mqttMessage.getPayload()) );
								    //sampleClient_RP.publish("iot_data", mqttMessage);
								  }
			
								  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
								    // not used in this example
								  }
				        });
						
						IoT_Device_2_Client.connect(connOpts_IoT_Device_2);
						System.out.println("PC-client connected to broker");
						String content_IoT_Device_2 = "Invoke "+Service_Name;
						
						System.out.println("5 Peer Node publishing message: " + content_IoT_Device_2);
						MqttMessage message = new MqttMessage(content_IoT_Device_2.getBytes());
						message.setQos(qos);
						if(IoT_Device_2_Client.isConnected())
						{
							IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);
							System.out.println("Deployment Message is: " + Deployment_Message);
							message = new MqttMessage(Deployment_Message.getBytes());
							message.setQos(qos);
							IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);
							
						}
						else
						{
							try
							{
								IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);

								System.out.println("Deployment Message is: " + Deployment_Message);
								
								message = new MqttMessage(Deployment_Message.getBytes());
								message.setQos(qos);
								IoT_Device_2_Client.publish("Service_Execution_Peer_Node", message);
							}
							catch (MqttException me)
							{
								me.printStackTrace();
							}
						}
						
						Thread.sleep(200);
					}
					catch (MqttException me)
					{
						me.printStackTrace();
					}
					catch (Throwable e)
					{
						e.printStackTrace();
					}
				}
				
				if(IoT_Device_2_Client != null)
				{
					Deployment_Client_Name[deployment_client_count] = IoT_Device_2_Client;
					System.out.println("deployment_client_count ="+deployment_client_count);
					System.out.println("Deployment_Client_Name ="+Deployment_Client_Name[deployment_client_count]);
					deployment_client_count = deployment_client_count + 1;
					Deployment_IP_Address_Count = Deployment_IP_Address_Count + 1;
				}
				
				System.out.println("-------------Deployment_Task_Count ="+Deployment_Task_Count+"---------------------");
				long stop_time = System.currentTimeMillis();
				long delay = stop_time - Mobility.device_left_time[0];
				double delay_in_sec = delay/1000;
				System.out.println("-------------delay is ="+delay_in_sec+" seconds---------------------");
			}
		  } 
		} 
    }

	public static String executeCommand(String jarFilePath, List<String> args)
	{
		System.out.println("jarFilePath= "+jarFilePath);
		
		if(jarFilePath.contains("gps-vehicle"))
		{
			String command = Execution_Command;
			boolean waitForResponse = false;
			
			String response = "";
			 
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			pb.redirectErrorStream(true);
			 
			System.out.println("Linux command: " + command);
			 
			try
			{
				Process shell = pb.start();
				System.out.println("here creating process");
				InputStream shellIn = shell.getInputStream();
		        LogStreamReader lsr = new LogStreamReader(shellIn);
		        Thread thread = new Thread(lsr, "LogStreamReader");
		        thread.start();
		        System.out.println("Continue with Execution"); 
				
				if (waitForResponse)
				{
					// Wait for the shell to finish and get the return code
					shell.waitFor();
					response = convertStreamToStr(shellIn);

					shellIn.close();
				}
				 
			}
			catch (IOException e)
			{
				System.out.println("Error occured while executing Linux command. Error Description: " + e.getMessage());
			}
			catch (InterruptedException e)
			{
				System.out.println("Error occured while executing Linux command. Error Description: " + e.getMessage());
			}	 
			
			return response;
		}
		else
		{
			String command = Execution_Command;
			boolean waitForResponse = false;
			
			String response = "";
			 
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			pb.redirectErrorStream(true);
			 
			try
			{
				Process shell = pb.start();
				
				InputStream shellIn = shell.getInputStream();
		        LogStreamReader lsr = new LogStreamReader(shellIn);
		        Thread thread = new Thread(lsr, "LogStreamReader");
		        thread.start();
				 
				if (waitForResponse)
				{
					// Wait for the shell to finish and get the return code
					int shellExitStatus = shell.waitFor();
					System.out.println("Exit status" + shellExitStatus);
					 
					response = convertStreamToStr(shellIn);
					shellIn.close();
				}
				 
			}
			catch (IOException e)
			{
				System.out.println("Error occured while executing Linux command. Error Description: " + e.getMessage());
			}
			catch (InterruptedException e)
			{
				System.out.println("Error occured while executing Linux command. Error Description: " + e.getMessage());
			}
			
			return response;
		}
	}
	
	public static String convertStreamToStr(InputStream is) throws IOException
	{
		if (is != null)
		{
			Writer writer = new StringWriter();
			 
			char[] buffer = new char[1024];
			try
			{
				Reader reader = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1)
				{
					writer.write(buffer, 0, n);
				}
			}
			finally
			{
				is.close();
			}
		
			return writer.toString();
		}
		else
		{
			return "";
		}
	}

	public static void Get_Vehicle_Status(String jarFilePath, List<String> args) 
	{
		String jar_path = System.getProperty("user.dir")+"//target//"+jarFilePath;
		System.out.println("Jar Path= "+jar_path);
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", jar_path);
        pb.directory(new File(System.getProperty("user.dir")+"//target//"));
        
        try
        {
            Process p = pb.start();
            LogStreamReader lsr = new LogStreamReader(p.getInputStream());
            Thread thread = new Thread(lsr, "LogStreamReader");
            thread.start();
            System.out.println("Continue with Execution");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}
	
	public static String getExecutionLog()
	{
	    String error_local = "";
	    String line;
	
	    try
	    {
	        while((line = error.readLine()) != null)
	        {
	        	error_local = error_local + "\n" + line;
	        }
	    }
	    catch (final IOException e)
	    {
	    }
	    
	    String output = "";
	    try
	    {
	        while((line = op.readLine()) != null)
	        {
	            output = output + "\n" + line;
	        }
	    }
	    catch (final IOException e)
	    {
	    }
	    
	    try
	    {
	        error.close();
	        op.close();
	    }
	    catch (final IOException e)
	    {
	    }
	    
	    return "exitVal: " + exitVal + ", error: " + error + ", output: " + output;
	}
	
	public static int extract_flow(String str, Pattern TAG_REGEX)
	{
		final List<Integer> tagValues = new ArrayList<Integer>();
		
		if(str != null || str != "")
		{
		    final Matcher matcher = TAG_REGEX.matcher(str);
		    while (matcher.find())
		    {
		    	try
		    	{
			        tagValues.add(Integer.parseInt((matcher.group(1)).trim()));
			        return Collections.max(tagValues);
		    	}
		    	catch (NumberFormatException e)
		    	{
		    	       System.out.println("not a number"); 
		    	} 
		    }
		    
		    return 0;
		}
		
		return 0;
	}
	
	public static String video_parse()
	{
		String pythonScriptPath = System.getProperty("user.dir")+"//blobDetection.py";
		String[] cmd = new String[2];
		cmd[0] = "python";
		cmd[1] = pythonScriptPath;
		String content = "";
		 
		try
		{
			// create runtime to execute external command
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(cmd);
			// retrieve output from python script
			BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while((line = bfr.readLine()) != null)
			{
				content = content + line + "\n";
			}
		}
		catch (IOException e)
		{
		        e.printStackTrace();
	    }
		
		return content;
	}
	
	public static void video_download(String fAddress, String destinationDir, String localFileName)
	{
		BufferedOutputStream outStream=null;
		InputStream is=null;
		HttpURLConnection  conn=null;
		int size=8192;
		
		try
		{
	        URL url;
	        byte[] buf;
	        int byteRead, byteWritten = 0;
	        url = new URL(fAddress);
	        outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + localFileName));

	        conn = (HttpURLConnection)url.openConnection();
	        is = conn.getInputStream();
	        buf = new byte[size];
	        while ((byteRead = is.read(buf)) != -1)
	        {
	            outStream.write(buf, 0, byteRead);
	            byteWritten += byteRead;
	        }
	    }
		catch (Exception e)
		{
	        e.printStackTrace();
	    }
		finally
		{
	        try
	        {
	            is.close();
	            outStream.close();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static void JSON_Parser(String content)
	{
		JSONParser parser = new JSONParser();
		JSONParser parser1 = new JSONParser();
		 try {

	            Object obj = parser.parse(content);
	            
	            JSONArray array = (JSONArray) obj;
	            for(int i=0;i<array.size();i++)
	            {
	            JSONObject jsonObject = (JSONObject)array.get(i);
	            Map gpsSimulator = ((Map) jsonObject.get("gpsSimulator"));
	            Iterator<Map.Entry> iterator = gpsSimulator.entrySet().iterator(); 
	            
	            while (iterator.hasNext()) { 
	                Map.Entry pair = iterator.next(); 
	                System.out.println(pair.getKey() + " : " + pair.getValue()); 
	                if(pair.getKey().toString() == "currentPosition" || pair.getKey().toString().equals("currentPosition"))
	                {
	                	JSONObject jsonObject1 = (JSONObject)pair.getValue();
	                	Map position = ((Map) jsonObject1.get("position"));
	                	Iterator<Map.Entry> iterator_pos = position.entrySet().iterator(); 
	                	while (iterator_pos.hasNext()) { 
	                		Map.Entry pair_pos = iterator_pos.next(); 
	    	                System.out.println(pair_pos.getKey() + " : " + pair_pos.getValue()); 
	                	}
	                }
	                
	            } 
	            }
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
		
	}
	
	
	public static String doHttpUrlConnectionAction(String desiredUrl) throws Exception
	{
		    URL url = null;
		    BufferedReader reader = null;
		    StringBuilder stringBuilder;
		    try
		    {
		      // create the HttpURLConnection
		      url = new URL(desiredUrl);
		      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		      
		      // just want to do an HTTP GET here
		      connection.setRequestMethod("GET");
		      
		      // uncomment this if you want to write output to this url
		      //connection.setDoOutput(true);
		      
		      // give it 15 seconds to respond
		      connection.setReadTimeout(15*1000);
		      connection.connect();

		      // read the output from the server
		      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		      stringBuilder = new StringBuilder();

		      String line = null;
		      while ((line = reader.readLine()) != null)
		      {
		        stringBuilder.append(line + "\n");
		      }
		      return stringBuilder.toString();
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		      throw e;
		    }
		    finally
		    {
		      // close the reader; this can throw an exception too, so
		      // wrap it in another try/catch block.
		      if (reader != null)
		      {
		        try
		        {
		          reader.close();
		        }
		        catch (IOException ioe)
		        {
		          ioe.printStackTrace();
		        }
		      }
		    }
		  }
	
	
	
	
	public static class SimpleMqttCallBack implements MqttCallback {

		  public void connectionLost(Throwable throwable) {
		    //System.out.println("Connection to MQTT broker lost!");
		    System.out.println("Listening......");
		  }

		  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
			  //System.out.println("Here4");
			  System.out.println("-------------------------------------------------");
			  System.out.println("| Received ");
			  System.out.println("| Topic: "+topic);
			  System.out.println("| Message: "+new String(mqttMessage.getPayload()));
			  System.out.println("| QoS: "+mqttMessage.getQos());
			  System.out.println("-------------------------------------------------");
		  }

		  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		    // not used in this example
		  }
		}
	
	public static void main(String[] args) throws InterruptedException, IOException
	{
		Preparation.clean_up();
		
		Mobility.arg_5 = args[5];

		for(int gh = 0; gh < 20; gh++)
		{
			Mobility.startTime[gh] = System.currentTimeMillis();
		}
		
		// Creating a MQTT Broker using Moquette
        final IConfig classPathConfig = new ClasspathConfig();
        
		final Server mqttBroker = new Server();
		final List<? extends InterceptHandler> userHandlers = Arrays.asList(new PublisherListener());
		mqttBroker.startServer(classPathConfig, userHandlers);

		System.out.println("moquette mqtt broker started, press ctrl-c to shutdown..");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("stopping moquette mqtt broker..");
				mqttBroker.stopServer();
				System.out.println("moquette mqtt broker stopped");
			}
		});

		Thread.sleep(4000);

		// Creating a MQTT Client using Eclipse Paho
		String content = "Hi, I am Peer Node!";
		
		String clientId = "RP_Peer_Node_2_"+args[2];
		System.out.println("clientId ="+clientId);
				
		IoT_Device_1 = args[3];
		IoT_Device_1_ID = "IoT_Device_1_"+args[3];
		System.out.println("IoT_Device_1_ID ="+IoT_Device_1_ID);
		
		IoT_Device_2 = args[4];
		IoT_Device_2_ID = "IoT_Device_2_"+args[4];
		System.out.println("IoT_Device_2_ID ="+IoT_Device_2_ID);
		
		String broker_RP = args[0];
		String clientId_RP = "RP_RP1_"+args[1];
		System.out.println("clientId_RP ="+clientId_RP);
		
		try {
			
			/************************************RP CLIENT START**********************************/
			//final MqttClient sampleClient_RP = new MqttClient(broker_RP, clientId_RP, new MemoryPersistence());
			sampleClient_RP = new MqttClient(broker_RP, clientId_RP, new MemoryPersistence());
			connOpts_RP = new MqttConnectOptions();
			connOpts_RP.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
			connOpts_RP.setCleanSession(true);
			//connOpts_RP.setConnectionTimeout(1000);
			connOpts_RP.setKeepAliveInterval(1000); 
			//connOpts_RP.setAutomaticReconnect(true);
			
			sampleClient_RP.connect(connOpts_RP);
			System.out.println("Raspberry Pi Connected to Broker");
			sampleClient_RP.subscribe("service_execution_RP1");
			System.out.println("Raspberry Pi subscribed to topic service_execution");
			
			sampleClient_RP.setCallback(  new SimpleMqttCallBack() {
				 public void connectionLost(Throwable throwable) {
					    //System.out.println("Connection to MQTT broker lost!");
					    System.out.println("Listening sampleClient_RP......");
					  }

					  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
						  //System.out.println("Here");
						  System.out.println("-------------------------------------------------");
						  System.out.println("| Received ");
						  System.out.println("| Topic: "+topic);
						  System.out.println("| Message: "+new String(mqttMessage.getPayload()));
						  System.out.println("| QoS: "+mqttMessage.getQos());
						  System.out.println("-------------------------------------------------");
					  }

					  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
					    // not used in this example
					  }
	        });
			
			
			
			
			
			
			
			
			/************************************RP CLIENT START**********************************/
		
			
			/*****************************************PAHO CLIENT START*************************/
			/*commenting start
			sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
			connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setKeepAliveInterval(1000); 
			connOpts.setAutomaticReconnect(true);
			
			System.out.println("PC-client connecting to broker PC: " + broker);
			sampleClient.setCallback( new SimpleMqttCallBack()  {
				 public void connectionLost(Throwable throwable) {
					    System.out.println("Connection to MQTT broker lost!");
					  }

					  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
					    System.out.println("Message received here:\n\t"+ new String(mqttMessage.getPayload()) );
					    //sampleClient_RP.publish("iot_data", mqttMessage);
					  }

					  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
					    // not used in this example
					  }
	        });
			
			sampleClient.connect(connOpts);
			System.out.println("PC-client connected to broker");
			
			System.out.println("RP Client publishing message: " + content);
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			if(sampleClient.isConnected())
			{
			sampleClient.publish("iot_data", message);
			}else
			{
				try
				{
					sampleClient.connect(connOpts);
					sampleClient.publish("iot_data", message);
					
				}catch (MqttException me) {
					me.printStackTrace();
				}
			}
			commenting end */
			
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			sampleClient_RP.publish("iot_data", message);
			
			
			
			
			/***COMMENTING ADDITIONAL CODE START
			/* Now addition of code for sensors start */
			
			/*while (true) {
				
				//publishsensorstatus();

                publishBrightness();

                Thread.sleep(500);

                publishTemperature();

                Thread.sleep(500);
            }*/
			/***COMMENTING ADDITIONAL CODE END
			
			/* Now addition of code for sensors end */
			
			
			/************************************Paho CLIENT END**********************************/
			
			//sampleClient.disconnect();
			//System.out.println("paho-client disconnected");
		} catch (MqttException me) {
			me.printStackTrace();
		}
	}
	
	
	

   
	
    
	
    
    
}

