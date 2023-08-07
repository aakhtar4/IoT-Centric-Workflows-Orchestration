package coordination;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.regex.*;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.xml.parsers.*;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.TimeUnit;

import io.moquette.interception.*;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.server.Server;
import io.moquette.server.config.ClasspathConfig;
import io.moquette.server.config.IConfig;
import util.Preparation;
import java.lang.Runtime;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main_Rasp_IoT
{
	static MqttClient sampleClient_RP=null;
	static MqttClient sampleClient=null;
	static MqttClient sampleClient_RP2=null;
	static MqttClient IoT_Device_Client=null;
	static MqttConnectOptions connOpts_RP=null;
	static MqttConnectOptions connOpts_RP2=null;
	static MqttConnectOptions connOpts=null;
	static MqttConnectOptions connOpts_IoT_Device=null;
	public static int Assignment_deployment_task_id[] = new int[100];
	static int qos = 1;
	public static int counter_Alive = 0;
	static String Location=null;
	public static BufferedReader error;
	public static BufferedReader op;
	public static int exitVal;
	public static String Execution_Command= null;
	public static String Service_Name = null;
	public static String Configuration_File = null;
	public static String execution_command_List[] = new String[100];
	public static int task_id = 0;
	public static String task_4_output[] = new String[1000];
	public static String task_output = "";
	public static String IoT_Device_1 = "";
	public static String IoT_Device_2 = "";
	public static String IoT_Device_1_ID = "";
	public static String IoT_Device_2_ID = "";
	public static int thread_counter = 0;
	public static String Process_Info = "";
	public static int deployment_task_id = 0;
	public static String Deployment_Task_Details[] = new String[100];
	public static int Deployment_Task_Count = 0;
	
	public static int Wind_Speed_Reading[] = new int[1000];
	public static int wind_speed_count = 0;
	
	public static int Wind_Direction_Reading[] = new int[1000];
	public static int wind_direction_count = 0;
	
	public static int Humidity_Reading[] = new int[1000];
	public static int humidity_count = 0;
	
	public static int Aerosol_Concentration_Reading[] = new int[1000];
	public static int aerosol_concentration_count = 0;
	
	public static int Temperature_Reading[] = new int[1000];
	public static int temperature_count = 0;
	
	public static int Streaming_Flag = 0;
	public static int task_count = 0;
	public static Task t1 = null;
	public static Task_4 t4_t1 = null;
	public static Task_4 t4_t2 = null;
	public static Task_4 t4_t3 = null;
	public static String args_3 = "";
	
	public static ArrayList<SensorServiceHandler> sensors = new ArrayList<SensorServiceHandler>();	
	
	public static int device_leave_flag = 0;
	public static boolean Flag_SWS = false;
	public static boolean Flag_SWD = false;
	public static boolean Flag_ST = false;
	public static boolean Flag_SH = false;
	public static boolean Flag_SAC = false;
	public static int task_output_count = 0;
	
	public static final Pattern TAG_REGEX_LEFT = Pattern.compile("<LHS>(.+?)</LHS>");	
	public static final Pattern TAG_REGEX_RIGHT = Pattern.compile("<RHS>(.+?)</RHS>");	
	
	
	static class PublisherListener extends AbstractInterceptHandler {
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
			else if(message.getTopicName().equals("service_execution_RP1"))
			{
				  System.out.println("-------------------------------------------------");
				  System.out.println("| Received ");
				  System.out.println("| Topic: "+message.getTopicName());
				  System.out.println("| Message: "+text_message);
				  System.out.println("| QoS: "+message.getQos());
				  System.out.println("-------------------------------------------------");
				  
				  			
			}
			else
			{
				System.out.println("Topic: " + message.getTopicName());
				
				if((!(message.getTopicName().equals("home/wind_speed")) && !(message.getTopicName().equals("home/wind_direction")) && !(message.getTopicName().equals("home/humidity")) && !(message.getTopicName().equals("home/aerosol_concentration")) && !(message.getTopicName().equals("home/temperature"))) || (message.getTopicName().equals("Service_Execution_Peer_Node")))
				{
				  System.out.println("-------------------------------------------------");
				  System.out.println("| Received else");
				  System.out.println("| Topic: "+message.getTopicName());
				  System.out.println("| Message: "+text_message);
				  System.out.println("| QoS: "+message.getQos());
				  System.out.println("-------------------------------------------------");
				  
				  if(text_message.contains("<Task>") && Streaming_Flag == 1)
				  {
					  System.out.println("---------------------------------------------");
					  System.out.println("Streaming........");
					  System.out.println("---------------------------------------------");
					  task_count = task_count + 1;
					  if(task_count > 0)
					  {
						  System.out.println("********Calling Streaming******"+task_count);  
						  Streaming();
						  task_count = task_count - 1;
						  System.out.println("********Calling Streaming******"+task_count);
					  }
					}
				}
				
				if(wind_speed_count > 0 && wind_direction_count > 0 && humidity_count > 0 && aerosol_concentration_count > 0 && temperature_count > 0 && Streaming_Flag == 1)
				{
					Streaming();
				}
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
			
			if(new String(message.getPayload().array()).contains("Send with Your Task Execution Status"))
			{
				String content = "Done with Execution";
				String service_information = "<Service_Name>"+Service_Name+"</Service_Name><IP_Address>"+IoT_Device_1+"</IP_Address>";
				content = content + " " + service_information;
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
			}
			
			if(new String(message.getPayload().array()).contains("Publish wind_speed Data"))
			{
				if(Flag_SWS == false)
				{
					initializeSensor(message, "SWS");
					Flag_SWS = true;
				}
			}
			
			if(new String(message.getPayload().array()).contains("Publish wind_direction Data"))
			{
				if(Flag_SWD == false)
				{
					initializeSensor(message, "SWD");
					Flag_SWD = true;
				}
			}
			
			
			if(new String(message.getPayload().array()).contains("Publish temperature Data"))
			{
				if(Flag_ST == false)
				{
					initializeSensor(message, "ST");
					Flag_ST = true;
				}
			}
			
			
			if(new String(message.getPayload().array()).contains("Publish humidity Data"))
			{
				if(Flag_SH == false)
				{
					initializeSensor(message, "SH");
					Flag_SH = true;
				}
			}
			
			if(new String(message.getPayload().array()).contains("Publish aerosol_concentration Data"))
			{
				if(Flag_SAC == false)
				{
					initializeSensor(message, "SAC");
					Flag_SAC = true;
				}
			}
			
			if(new String(message.getPayload().array()).contains("<?xml version=\"1.0\"?><Deployment>"))
			{
				String Deployment_Message= new String(message.getPayload().array());

				if(Deployment_Message.contains("<Task>"))
				{
					deployment_task_id = Integer.parseInt(Deployment_Message.substring(Deployment_Message.indexOf("[") + 1, Deployment_Message.indexOf("]")));
					Assignment_deployment_task_id[Deployment_Task_Count] = Integer.parseInt(Deployment_Message.substring(Deployment_Message.indexOf("[") + 1, Deployment_Message.indexOf("]")));
				}
				
				Deployment_Task_Count = 0;
				Deployment_Task_Details[Deployment_Task_Count] = Deployment_Message;
				Deployment_Task_Count = Deployment_Task_Count + 1;
				wind_speed_count = 0;
				wind_direction_count = 0;
				humidity_count = 0;
				aerosol_concentration_count = 0;
				temperature_count = 0;
				
				String xmlStr= new String(message.getPayload().array());
				Document doc = convertStringToDocument(xmlStr); 
				parseXML_Deployment(doc);
				
				try
				{
					FileReader in = new FileReader(Configuration_File);
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
									if(sampleClient_RP.isConnected())
									{
										sampleClient_RP.subscribe(Device_URI);
									}
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
					
					try
					{
						if(deployment_task_id == 4)
						{
							String redeployment_content = "[IoT] Redeployment Done [4]";
							MqttMessage redeployment_message = new MqttMessage(redeployment_content.getBytes());
							redeployment_message.setQos(qos);
							
							if(sampleClient_RP.isConnected())
							{
								sampleClient_RP.publish("iot_data", redeployment_message);
							}
							else
							{
								sampleClient_RP.connect(connOpts);
				        		sampleClient_RP.publish("iot_data", redeployment_message);
							}
						}
						
						System.out.println("Executing Command" + Jar_File_Name);
						Process_Info = Jar_File_Name;
						System.out.println("Process Info= "+Process_Info);
						System.out.println(Thread.currentThread().getName());
						
						while((deployment_task_id == 1 && wind_speed_count != 1 && wind_direction_count != 1 && humidity_count != 1 && aerosol_concentration_count != 1 && temperature_count != 1) || (deployment_task_id == 2 && wind_speed_count != 1 && wind_direction_count != 1 && humidity_count != 1 && aerosol_concentration_count != 1 && temperature_count != 1) || (deployment_task_id == 3 && wind_speed_count != 1 && wind_direction_count != 1 && humidity_count != 1 && aerosol_concentration_count != 1 && temperature_count != 1)) 
						{
							Thread.sleep(5000);
							System.out.println("waiting for input");
						}
						
						if(deployment_task_id == 1 || deployment_task_id == 2 || deployment_task_id == 3)
						{
							wind_speed_count = wind_speed_count - 1;
							wind_direction_count = wind_direction_count - 1;
							humidity_count = humidity_count - 1;
							aerosol_concentration_count = aerosol_concentration_count - 1;
							temperature_count = temperature_count - 1;
						}
						
						if(deployment_task_id == 1 || deployment_task_id == 2 || deployment_task_id == 3)
						{
							Task t1 = new Task(Jar_File_Name);
							thread_counter = thread_counter + 1;
							
							while(thread_counter != 0)
							{
								Thread.sleep(2000);
								System.out.println("Processing------"+thread_counter);
							}
							
							if(t1.t.isAlive())
							{
								System.out.println("--------------------------------------------");
								System.out.println("Here interuppting");
								System.out.println("--------------------------------------------");
								t1.t.interrupt();
							}
						}
						
						if(deployment_task_id == 4)
						{
							t4_t1 = new Task_4(Jar_File_Name);
							t4_t2 = new Task_4(Jar_File_Name);
							t4_t3 = new Task_4(Jar_File_Name);
							thread_counter = thread_counter + 3;
							
							while(thread_counter != 0)
							{
								Thread.sleep(2000);
								System.out.println("Processing------"+thread_counter);
							}
							
							if(t4_t1.t.isAlive())
							{
								System.out.println("--------------------------------------------");
								System.out.println("Here interuppting");
								System.out.println("--------------------------------------------");
								t4_t1.t.interrupt();
							}
							
							if(t4_t2.t.isAlive())
							{
								System.out.println("--------------------------------------------");
								System.out.println("Here interuppting");
								System.out.println("--------------------------------------------");
								t4_t2.t.interrupt();
							}
							
							if(t4_t3.t.isAlive())
							{
								System.out.println("--------------------------------------------");
								System.out.println("Here interuppting");
								System.out.println("--------------------------------------------");
								t4_t3.t.interrupt();
							}
						}
						
						Streaming_Flag = 1;		

						String content = "Done with Execution from IP: ["+IoT_Device_1+"]";
						System.out.println("Content: "+content);
						String service_information = "<Service_Name>"+Service_Name+"</Service_Name><IP_Address>"+IoT_Device_1+"</IP_Address>";
						content = content + " " + service_information;
				        MqttMessage execution_message = new MqttMessage(content.getBytes());
				        execution_message.setQos(qos);
				        
				        if(deployment_task_id == 4)
				        {
				        	System.out.println("task_output_count ="+task_output_count);
					        	
					        for(int ijk = (task_output_count-3); ijk<task_output_count; ijk++)
					        {
						        task_output = "Task Output is: "+"<Task>["+deployment_task_id+"]</Task> <"+args_3+"> <<"+ijk+">> "+task_4_output[ijk];
						        System.out.println("task_output ="+task_output);
						        MqttMessage execution_output = new MqttMessage(task_output.getBytes());
						        execution_output.setQos(qos);
						        
						        if(sampleClient_RP.isConnected())
						        {
						        	if(ijk == (task_output_count-3))
						        	{
						        		sampleClient_RP.publish("iot_data", execution_message);
						        	}
						        	
						        	sampleClient_RP.publish("iot_data", execution_output);
						        }
						        else
						        {
						        	try
									{
						        		int connect_status = 0;
						        		if(sampleClient_RP.isConnected() && connect_status == 0)
						        		{
						        			if(ijk == (task_output_count-3))
						        			{
						        				sampleClient_RP.publish("iot_data", execution_message);
						        			}
						        			
						        			sampleClient_RP.publish("iot_data", execution_output);
						        			connect_status = 1;
						        		}
									}
						        	catch (MqttException me)
						        	{
										me.printStackTrace();
									}
						        }
					        }
				        }
				        else
				        {
					        task_output = "Task Output is: "+"<Task>["+deployment_task_id+"]</Task> <"+args_3+"> "+task_output;
					        MqttMessage execution_output = new MqttMessage(task_output.getBytes());
					        execution_output.setQos(qos);
					        
					        if(sampleClient_RP.isConnected())
					        {
						        sampleClient_RP.publish("iot_data", execution_message);
						        sampleClient_RP.publish("iot_data", execution_output);
					        }
					        else
					        {
					        	try
								{
					        		int connect_status = 0;
					        		
					        		if(sampleClient_RP.isConnected() && connect_status == 0)
					        		{
						        		sampleClient_RP.publish("iot_data", execution_message);
						        		sampleClient_RP.publish("iot_data", execution_output);
						        		connect_status = 1;
					        		}
								}
					        	catch (MqttException me)
					        	{
									me.printStackTrace();
								}
					        }
						}
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

			if(new String(message.getPayload().array()).contains("Device has left"))
			{
				task_id = 0;
				System.out.println("deployment_task_id ="+deployment_task_id);

				device_leave_flag = 1;
				
				if(deployment_task_id == 1 || deployment_task_id == 2 || deployment_task_id == 3)
				{
					task_output = "";
					thread_counter = 0;
					
					if(Flag_SWS == false && Flag_SWD == false && Flag_ST == false && Flag_SH == false && Flag_SAC == false)
					{
						System.out.println("Stopping the Sensors");
						
						for(SensorServiceHandler sensorServiceHandler : sensors)
						{
							try
							{
								sensorServiceHandler.stop();
								sensorServiceHandler.t.interrupt();
							} 
							catch (Exception e)
							{ 
								System.out.println("Exception handled"); 
							}
						}
					}
					
					try
					{
						t1.t.interrupt();
					}
					catch (Exception e)
					{ 
				            System.out.println("Exception handled"); 
			        } 
					
					Streaming_Flag = 0;
				}
				
				if(deployment_task_id == 4)
				{
					
					thread_counter = 0;
					Streaming_Flag = 0;
					
					try
					{
						String four = "[IoT] <Task>[4]</Task> Device left";
		        		MqttMessage deployment_msg = new MqttMessage(four.getBytes());
		        		deployment_msg.setQos(qos);
		        		
		        		if(sampleClient_RP.isConnected())
		        		{
		        			sampleClient_RP.publish("iot_data", deployment_msg);
		        		}
		        		else
						{
							sampleClient_RP.connect(connOpts);
			        		sampleClient_RP.publish("iot_data", deployment_msg);
						}
					}
	        		catch (MqttException me)
					{
						me.printStackTrace();
					}
				}
				
				System.out.println("My device has left"+Streaming_Flag);
				
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
					List arg_list = new ArrayList();

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
			
			
			if(new String(message.getPayload().array()).contains("Share output with"))
			{
				String received_message = new String(message.getPayload().array());
				
				if(received_message.contains("["))
				{
					String Device_URI = received_message.substring(received_message.indexOf("[") + 1, received_message.indexOf("]"));
					Device_URI = "tcp://"+Device_URI+":1883";
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
						System.out.println("Raspberry Pi 1 connected to Raspberry Pi 2");
						sampleClient_RP2.subscribe("output_RP2", qos);
						System.out.println("Raspberry Pi 1 subscribed to topic output_RP2");
						
						sampleClient_RP2.setCallback(new MqttCallback()
						{
							 public void connectionLost(Throwable throwable)
							 {
								    System.out.println("Listening sampleClient_RP2......");
							 }
	
							 public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
							 {
								  System.out.println("-------------------------------------------------");
								  System.out.println("| Received ");
								  System.out.println("| Topic: "+topic);
								  System.out.println("| Message: "+new String(mqttMessage.getPayload()));
								  System.out.println("| QoS: "+mqttMessage.getQos());
								  System.out.println("-------------------------------------------------");
							 }
	
							 public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
							 {
								 System.out.println("Delivery complete.");
							 }
				        });
					
						try
						{
							FileReader in = new FileReader("output.kml");
							BufferedReader br = new BufferedReader(in);
	
							String output;
							String xml_output = "";
							System.out.println("Output from Server .... \n");
							
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
						} catch (IOException e)
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
			
			if(message.getTopicName().equals("iot-2/evt/text/fmt/json") && new String(message.getPayload().array()).contains("Location_Update"))
			{
				System.out.println("Here is my Location");
				Get_Location_Update();
				
			}
		}
	}
	
	public static void initializeSensor(InterceptPublishMessage message, String sensorName)
	{
		String sensorTopic = "";
		int deviceNumber = 0;
		
		if(sensorName == "SWS")
		{
			sensorTopic = "home/wind_speed";
			deviceNumber = 7;
			wind_speed_count = 0;
		}
		else if(sensorName == "SWD")
		{
			sensorTopic = "home/wind_direction";
			deviceNumber = 3;
			wind_direction_count = 0;
		}
		else if(sensorName == "ST")
		{
			sensorTopic = "home/temperature";
			deviceNumber = 4;
			temperature_count = 0;
		}
		else if(sensorName == "SH")
		{
			sensorTopic = "home/humidity";
			deviceNumber = 5;
			humidity_count = 0;
		}
		else if(sensorName == "SAC")
		{
			sensorTopic = "home/aerosol_concentration";
			deviceNumber = 6;
			aerosol_concentration_count = 0;
		}
		
		String received_message = new String(message.getPayload().array());

		if(received_message.contains("["))
		{
			String deviceURI = received_message.substring(received_message.indexOf("[") + 1, received_message.indexOf("]"));
			deviceURI = "tcp://"+deviceURI+":1883";
			
			System.out.println("Device URI: " + deviceURI);
			
			try
			{
				String clientId_RP = "IoT_Device_" + deviceNumber + "_" + deviceURI;
				System.out.println("clientId_RP_" + sensorName +  " = " + clientId_RP);

				IoT_Device_Client = new MqttClient(deviceURI, clientId_RP, new MemoryPersistence());
				connOpts_IoT_Device = new MqttConnectOptions();
				connOpts_IoT_Device.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
				connOpts_IoT_Device.setCleanSession(true);
				connOpts_IoT_Device.setKeepAliveInterval(1000); 
				
				IoT_Device_Client.connect(connOpts_IoT_Device);
				
				IoT_Device_Client.subscribe("Service_Execution_IoT_Device", qos);
				
				IoT_Device_Client.subscribe(sensorTopic, qos);
				
				System.out.println("Raspberry Pi " + deviceNumber +" subscribed to topic output_RP_" + sensorName);
				
				IoT_Device_Client.setCallback(new SensorMqttCallBack(sensorName));
				
				System.out.println("I am subscribing for " + sensorName);
			}
			catch (MqttException me)
			{
				me.printStackTrace();
			}
			
			sensors.add(new SensorServiceHandler(deviceURI, deviceNumber, sensorName, sensorTopic, IoT_Device_Client));
		}
	}
	
	public static void Streaming()
	{
		try
		{
			if(execution_command_List.length > 0 && task_id > 0)	
			{

				for(int i=0;i<task_id;i++)
					{
				System.out.println("-------------------------------------------");
				System.out.println("Streaming");
				System.out.println("-------------------------------------------");
				
				Execution_Command = execution_command_List[i];
				if(Execution_Command.contains("Aloha") && Execution_Command.contains("output") && Execution_Command.contains("data_integration_service"))
				{
					int number = generate_random_number();
					if(number == 0)
					{
						Execution_Command = "java -jar data_integration_service.jar output_0.kml Aloha_0.jpg";
						execution_command_List[i] = "java -jar data_integration_service.jar output_0.kml Aloha_0.jpg";
					}
					else if(number == 1)
					{
						Execution_Command = "java -jar data_integration_service.jar output_1.kml Aloha_1.jpg";
						execution_command_List[i] = "java -jar data_integration_service.jar output_1.kml Aloha_1.jpg";
					}
					else if(number == 2)
					{
						Execution_Command = "java -jar data_integration_service.jar output_2.kml Aloha_2.jpg";
					}
					else if(number == 3)
					{
						Execution_Command = "java -jar data_integration_service.jar output_3.kml Aloha_3.jpg";
						execution_command_List[i] = "java -jar data_integration_service.jar output_3.kml Aloha_3.jpg";
					}
					else
					{
						Execution_Command = "java -jar data_integration_service.jar output_4.kml Aloha_4.jpg";
						execution_command_List[i] = "java -jar data_integration_service.jar output_4.kml Aloha_4.jpg";
					}	
				}
				if(Execution_Command.contains("Vehicle") && Execution_Command.contains("gps-vehicle-simulator"))
				{
					openInBrowser_linux("http://localhost:8080/api/status");
					Thread.sleep(10000);
				}
				else
				{
					
					if(execution_command_List[i].contains("data_integration_service"))
					{
							wind_speed_count = wind_speed_count - 1;
							wind_direction_count = wind_direction_count - 1;
							humidity_count = humidity_count - 1;
							aerosol_concentration_count = aerosol_concentration_count - 1;
							temperature_count = temperature_count - 1;
							t1 = new Task(execution_command_List[i]);
							//Thread.sleep(100000);
							//Thread.sleep(1000);
							System.out.println("Streaming Thread Counter ="+thread_counter);
							thread_counter = thread_counter + 1;
							while(thread_counter != 0)
							{
								Thread.sleep(2000);
								System.out.println("Processing------");
							}
							if(t1.t.isAlive())
							{
								System.out.println("--------------------------------------------");
								System.out.println("Here interuppting");
								System.out.println("--------------------------------------------");
								t1.t.interrupt();
							}
							try{
						        task_output = "Task Output is: "+"<Task>["+deployment_task_id+"]</Task> <"+args_3+"> "+task_output;
						        MqttMessage execution_output = new MqttMessage(task_output.getBytes());
						        execution_output.setQos(qos);
						        if(sampleClient_RP.isConnected())
						        {
						        sampleClient_RP.publish("iot_data", execution_output);
						        }
						        else
						        {
						        	try
									{
						        		int connect_status = 0;
						        		if(sampleClient_RP.isConnected() && connect_status == 0)
						        		{
						        		//sampleClient_RP.connect(connOpts);
						        		sampleClient_RP.publish("iot_data", execution_output);
						        		connect_status = 1;
						        		}
										
									}catch (MqttException me) {
										me.printStackTrace();
									}
						        }
							}catch (Exception e) {
						        e.printStackTrace();
						    }
						
					}
					else if(execution_command_List[i].contains("Traffic"))
					{
						t4_t1 = new Task_4(execution_command_List[i]);
						t4_t2 = new Task_4(execution_command_List[i]);
						t4_t3 = new Task_4(execution_command_List[i]);

						System.out.println("Streaming Thread Counter ="+thread_counter);
						thread_counter = thread_counter + 3;
						System.out.println("Streaming Thread Counter ="+thread_counter);
						
						while(thread_counter != 0)
						{
							Thread.sleep(2000);
							
							System.out.println("Here Processing------" + thread_counter);
							
							sampleClient_RP.setCallback(new MqttCallback()
							{
								 public void connectionLost(Throwable throwable)
								 {
									    System.out.println("Connection to MQTT broker lost!");
									    System.out.println("Listeningg sampleClient_RP......");
								 }
	
								  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
									  System.out.println("Thread_Counter-------------------------------------------------"+thread_counter);
									  System.out.println("| Received ");
									  System.out.println("| Topic: "+topic);
									  System.out.println("| Message: "+new String(mqttMessage.getPayload()));
									  System.out.println("| QoS: "+mqttMessage.getQos());
									  System.out.println("-------------------------------------------------");
									 
								  }

								  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
								  {
									  System.out.println("Delivery complete.");
								  }
					        });
						}

						if(t4_t1.t.isAlive())
						{
							System.out.println("--------------------------------------------");
							System.out.println("Here interuppting");
							System.out.println("--------------------------------------------");
							t4_t1.t.interrupt();
						}
						
						if(t4_t2.t.isAlive())
						{
							System.out.println("--------------------------------------------");
							System.out.println("Here interuppting");
							System.out.println("--------------------------------------------");
							t4_t2.t.interrupt();
						}
						
						if(t4_t3.t.isAlive())
						{
							System.out.println("--------------------------------------------");
							System.out.println("Here interuppting");
							System.out.println("--------------------------------------------");
							t4_t3.t.interrupt();
						}
						
						try
						{
							for(int ijk = (task_output_count-3); ijk < task_output_count; ijk++)
							{
						        task_output = "Task Output is: "+"<Task>["+deployment_task_id+"]</Task> <"+args_3+"> <<"+ijk+">> "+task_4_output[ijk];
						        MqttMessage execution_output = new MqttMessage(task_output.getBytes());
						        execution_output.setQos(qos);
						    
						        if(sampleClient_RP.isConnected())
						        {
						        	sampleClient_RP.publish("iot_data", execution_output);
						        }
						        else
						        {
						        	try
									{
						        		int connect_status = 0;
						        		
						        		if(sampleClient_RP.isConnected() && connect_status == 0)
						        		{
							        		sampleClient_RP.publish("iot_data", execution_output);
							        		connect_status = 1;
						        		}
										
									}
						        	catch (MqttException me)
						        	{
										me.printStackTrace();
									}
						        }
							}
						}
						catch (Exception e)
						{
					        e.printStackTrace();
					    }
					}
					else
					{
						t1 = new Task(execution_command_List[i]);
						System.out.println("Streaming Thread Counter =" + thread_counter);
						thread_counter = thread_counter + 1;
						System.out.println("Streaming Thread Counter ="+thread_counter);
						while(thread_counter != 0)
						{
							Thread.sleep(2000);
							System.out.println("Here Processing------" + thread_counter);
							
							sampleClient_RP.setCallback(new MqttCallback()
							{
								 public void connectionLost(Throwable throwable)
								 {
									    System.out.println("Connection to MQTT broker lost!");
									    System.out.println("Listeningg sampleClient_RP......");
								 }
	
									  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
									  {
										  System.out.println("Thread_Counter-------------------------------------------------"+thread_counter);
										  System.out.println("| Received ");
										  System.out.println("| Topic: "+topic);
										  System.out.println("| Message: "+new String(mqttMessage.getPayload()));
										  System.out.println("| QoS: "+mqttMessage.getQos());
										  System.out.println("-------------------------------------------------");
									  }
	
									  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
									  {
										  System.out.println("Delivery complete.");
									  }
					        });
						}
						
						if(t1.t.isAlive())
						{
							System.out.println("--------------------------------------------");
							System.out.println("Here interuppting");
							System.out.println("--------------------------------------------");
							t1.t.interrupt();
						}
						
						try
						{
					        task_output = "Task Output is: "+"<Task>["+deployment_task_id+"]</Task> <"+args_3+"> "+task_output;
					        MqttMessage execution_output = new MqttMessage(task_output.getBytes());
					        execution_output.setQos(qos);
					        
					        if(sampleClient_RP.isConnected())
					        {
					        	sampleClient_RP.publish("iot_data", execution_output);
					        }
					        else
					        {
					        	try
								{
					        		int connect_status = 0;

					        		if(sampleClient_RP.isConnected() && connect_status == 0)
					        		{
					        		sampleClient_RP.publish("iot_data", execution_output);
					        		connect_status = 1;
					        		}
									
								}
					        	catch (MqttException me)
					        	{
									me.printStackTrace();
								}
					        }
						}
						catch (Exception e)
						{
					        e.printStackTrace();
					    }
					}
				}
			} 
			}}catch (InterruptedException e) {
			System.out.println("Error occured while executing Linux command. Error Description: "+ e.getMessage());
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

	public static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
	
	
	public static void parseXML_Deployment(Document doc)
    {
    	doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				
		NodeList nDeploymentList = doc.getElementsByTagName("Deployment"); 
		for(int temp = 0 ; temp <nDeploymentList.getLength(); temp++){ 
		Node nNode = nDeploymentList.item(temp); 
		Element eElement = (Element) nNode; 
		NodeList childList = eElement.getChildNodes(); 
		String [] sDeployment = new String[childList.getLength()] ; 
		for(int i = 0; i < childList.getLength(); i++){ 
		Node childNode = childList.item(i); 
		//System.out.println("Service Name is= "+childNode.getAttributes().getNamedItem("name").getNodeValue());
		sDeployment[i] = childNode.getNodeName() + "\t" + childNode.getTextContent();
		if(childNode.getNodeName().contains("Command"))
		{
			//System.out.println(x);
			Execution_Command = childNode.getTextContent();
			execution_command_List[task_id] = Execution_Command;
			task_id = task_id + 1;
			System.out.println("Execution Command is "+Execution_Command);
		}
		
		if(childNode.getNodeName().contains("Service_Name"))
		{
			
			Service_Name = childNode.getTextContent();
			
		}
		
		if(childNode.getNodeName().contains("Configuration_File"))
		{
			Configuration_File = childNode.getTextContent();
		}
		//System.out.println(sDeployment[i]); 
		} 
		} 		
    }
	
	

	public static String executeCommand(String jarFilePath, List<String> args) {
		 System.out.println("jarFilePath= "+jarFilePath);
		
		if(jarFilePath.contains("gps-vehicle"))
		{

			String command = Execution_Command;
			boolean waitForResponse = false;
			
			
			String response = "";
			 
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			pb.redirectErrorStream(true);
			 
			System.out.println("Linux command: " + command);
			 
			try {
			Process shell = pb.start();
			System.out.println("here creating process");
			InputStream shellIn = shell.getInputStream();
	        LogStreamReader lsr = new LogStreamReader(shellIn);
	        Thread thread = new Thread(lsr, "LogStreamReader");
	        thread.start();
	        System.out.println("Continue with Execution"); 
			
			if (waitForResponse) {
				 
				// Wait for the shell to finish and get the return code
				int shellExitStatus = shell.waitFor();
				System.out.println("Exit status" + shellExitStatus);
				 
				response = convertStreamToStr(shellIn);
				 
				shellIn.close();
				}
			 
			}
			 
			catch (IOException e) {
			System.out.println("Error occured while executing Linux command. Error Description: "
			+ e.getMessage());
			}
			 
			catch (InterruptedException e) {
			System.out.println("Error occured while executing Linux command. Error Description: "
			+ e.getMessage());
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
		 
		System.out.println("Linux command: " + command);
		 
		try {
		Process shell = pb.start();
		
		/*Additional Code Start */
		
		InputStream shellIn = shell.getInputStream();
        LogStreamReader lsr = new LogStreamReader(shellIn);
        Thread thread = new Thread(lsr, "LogStreamReader");
        thread.start();
        System.out.println("Continue with Execution"); 
        
        /*Additional Code End */
		 
		if (waitForResponse) {
		 
		// To capture output from the shell
		//InputStream shellIn = shell.getInputStream();
		 
		// Wait for the shell to finish and get the return code
		int shellExitStatus = shell.waitFor();
		System.out.println("Exit status" + shellExitStatus);
		 
		response = convertStreamToStr(shellIn);
		//System.out.println("response is= "+response);
		 
		shellIn.close();
		}
		 
		}
		 
		catch (IOException e) {
		System.out.println("Error occured while executing Linux command. Error Description: "
		+ e.getMessage());
		}
		 
		catch (InterruptedException e) {
		System.out.println("Error occured while executing Linux command. Error Description: "
		+ e.getMessage());
		}	 
		return response;
	}
		}
	
	
	public static String convertStreamToStr(InputStream is) throws IOException {
		 
		if (is != null) {
		Writer writer = new StringWriter();
		 
		char[] buffer = new char[1024];
		try {
		Reader reader = new BufferedReader(new InputStreamReader(is,
		"UTF-8"));
		int n;
		while ((n = reader.read(buffer)) != -1) {
		writer.write(buffer, 0, n);
		}
		} finally {
		is.close();
		}
		return writer.toString();
		}
		else {
		return "";
		}
		}
	
	
	
	public static void Get_Vehicle_Status(String jarFilePath, List<String> args) 
	{
		System.out.println("I am here for vehicle status");
		String jar_path = System.getProperty("user.dir")+"//target//"+jarFilePath;
		System.out.println("Jar Path= "+jar_path);
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", jar_path);
        pb.directory(new File(System.getProperty("user.dir")+"//target//"));
        try {
            Process p = pb.start();
            LogStreamReader lsr = new LogStreamReader(p.getInputStream());
            Thread thread = new Thread(lsr, "LogStreamReader");
            thread.start();
            System.out.println("Continue with Execution");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	public static void executeJar(String jarFilePath, List<String> args) throws Exception {
		String[] command_args = Execution_Command.split(" ");
	
	    // Create run arguments for the
		final List<String> actualArgs = new ArrayList<String>();
		
		for(int i=0;i<command_args.length;i++)
		{
			actualArgs.add(i, command_args[i]);
		}
		
	    /*actualArgs.add(0, command_args[0]);
	    actualArgs.add(1, command_args[1]);
	    actualArgs.add(2, command_args[2]);*/
	    actualArgs.addAll(args);
	    //System.out.println(args.get(0));
	    //System.out.println(args.get(1));
	        try {
	        final Runtime re = Runtime.getRuntime();
	        //final Process command = re.exec(cmdString, args.toArray(new String[0]));
	        final Process command = re.exec(actualArgs.toArray(new String[0]));
	        System.out.println("Done with Execution");
	        String content = "Done with Execution";
	        String service_information = "<Service_Name>"+Service_Name+"</Service_Name><IP_Address>"+IoT_Device_1+"</IP_Address>";
	        content = content + " " + service_information;
	        MqttMessage message = new MqttMessage(content.getBytes());
	        message.setQos(qos);
	        if(sampleClient_RP.isConnected())
	        {
	        //System.out.println("Here connected");
	        sampleClient_RP.publish("iot_data", message);
	        }
	        else
	        {
	        	try
				{
	        		System.out.println("Here connecting");
					//sampleClient_RP.disconnect();
	        		sampleClient_RP.connect(connOpts);
	        		sampleClient_RP.publish("iot_data", message);
					
				}catch (MqttException me) {
					me.printStackTrace();
				}
	        }
	        
	        
	        error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
	        op = new BufferedReader(new InputStreamReader(command.getInputStream()));
	        
	        command.waitFor(1000, TimeUnit.MILLISECONDS);
	        exitVal = command.exitValue();
	        System.out.println("Process exitValue: " + exitVal);
	        if (exitVal != 0) {
	            throw new IOException("Failed to execure jar, " + getExecutionLog());
	        }
	        	
	    } catch (final IOException e) {
	        throw new Exception(e);
	    }
	}
	

	
	public static void executeJar_Linux(String jarFilePath, List<String> args) throws Exception {
		String[] command_args = Execution_Command.split(" ");
	
	    // Create run arguments for the
		final List<String> actualArgs = new ArrayList<String>();
		
		for(int i=0;i<command_args.length;i++)
		{
			actualArgs.add(i, command_args[i]);
		}
		
	    /*actualArgs.add(0, command_args[0]);
	    actualArgs.add(1, command_args[1]);
	    actualArgs.add(2, command_args[2]);*/
	    actualArgs.addAll(args);
	    //System.out.println(args.get(0));
	    //System.out.println(args.get(1));
	        try {
	        final Runtime re = Runtime.getRuntime();
	        //final Process command = re.exec(cmdString, args.toArray(new String[0]));
	        final Process command = re.exec(actualArgs.toArray(new String[0]));
	        System.out.println("Done with Execution");
	        String content = "Done with Execution";
	        String service_information = "<Service_Name>"+Service_Name+"</Service_Name><IP_Address>"+IoT_Device_1+"</IP_Address>";
	        content = content + " " + service_information;
	        MqttMessage message = new MqttMessage(content.getBytes());
	        message.setQos(qos);
	        if(sampleClient_RP.isConnected())
	        {
	        //System.out.println("Here connected");
	        sampleClient_RP.publish("iot_data", message);
	        }
	        else
	        {
	        	try
				{
	        		System.out.println("Here connecting");
					//sampleClient_RP.disconnect();
	        		sampleClient_RP.connect(connOpts);
	        		sampleClient_RP.publish("iot_data", message);
					
				}catch (MqttException me) {
					me.printStackTrace();
				}
	        }
	        
	        System.out.println("ee");
	        
	        InputStream errStream,inStream;
	        byte iobuf[] = new byte[4096];
	        int bytes;
	        inStream = command.getInputStream();
	        errStream = command.getErrorStream();
	        while ((bytes = inStream.read(iobuf)) > 0)
	        {
	        	System.out.println("Here");
	            System.out.write(iobuf,0,bytes);
	        }
	          while ((bytes = errStream.read(iobuf)) > 0)
	          {
	        	  System.out.println("Here2");
	            System.err.write(iobuf,0,bytes);
	          }
	          System.out.print('W');
	          command.waitFor(1000, TimeUnit.MILLISECONDS);
	          System.out.print('w');
	    
	          System.out.print('C');
	          errStream.close();
	          inStream.close();
	          command.getOutputStream().close();
	          System.out.print('c');
	        
	        	
	    } catch (final IOException e) {
	    
	       // catch (final IOException | InterruptedException e) {
	        throw new Exception(e);
	    }
	}
	
	

	public static void executeJar_Windows(String jarFilePath, List<String> args) throws Exception {
		System.out.println("I am here");
		String[] command_args = Execution_Command.split(" ");
	
	    // Create run arguments for the
		final List<String> actualArgs = new ArrayList<String>();
		
		for(int i=0;i<command_args.length;i++)
		{
			actualArgs.add(i, command_args[i]);
		}
		
	    /*actualArgs.add(0, command_args[0]);
	    actualArgs.add(1, command_args[1]);
	    actualArgs.add(2, command_args[2]);*/
	    actualArgs.addAll(args);
	    //System.out.println(args.get(0));
	    //System.out.println(args.get(1));
	        try {
	        final Runtime re = Runtime.getRuntime();
	        //final Process command = re.exec(cmdString, args.toArray(new String[0]));
	        System.out.println("I am executing "+actualArgs.toArray(new String[0]));
	        final Process command = re.exec(actualArgs.toArray(new String[0]));
	     // Any error message?
	        Thread errorGobbler = new Thread(new StreamGobbler(command.getErrorStream(), System.err));           
            
	     // Any output?
	        Thread outputGobbler = new Thread(new StreamGobbler(command.getInputStream(), System.out));
                
            // kick them off
	        errorGobbler.start();
	        outputGobbler.start();
                                    
            // any error???
            int exitVal = command.waitFor();
            System.out.println("ExitValue: " + exitVal); 
            
            errorGobbler.join();   // Handle condition where the
            outputGobbler.join();  // process ends before the threads finish
	        
	        
	        System.out.println("Done with Execution");
	        String content = "Done with Execution";
	        String service_information = "<Service_Name>"+Service_Name+"</Service_Name><IP_Address>"+IoT_Device_1+"</IP_Address>";
	        content = content + " " + service_information;
	        MqttMessage message = new MqttMessage(content.getBytes());
	        message.setQos(qos);
	        if(sampleClient_RP.isConnected())
	        {
	        //System.out.println("Here connected");
	        sampleClient_RP.publish("iot_data", message);
	        }
	        else
	        {
	        	try
				{
	        		System.out.println("Here connecting");
					//sampleClient_RP.disconnect();
	        		sampleClient_RP.connect(connOpts);
	        		sampleClient_RP.publish("iot_data", message);
					
				}catch (MqttException me) {
					me.printStackTrace();
				}
	        }
	        
	       
	        	
	    } catch (final IOException e) {
	    
	       // catch (final IOException | InterruptedException e) {
	        throw new Exception(e);
	    }
	}
	
	
	
	
	public static String getExecutionLog() {
	    String error_local = "";
	    String line;
	    try {
	        while((line = error.readLine()) != null) {
	        	error_local = error_local + "\n" + line;
	        }
	    } catch (final IOException e) {
	    }
	    String output = "";
	    try {
	        while((line = op.readLine()) != null) {
	            output = output + "\n" + line;
	        }
	    } catch (final IOException e) {
	    }
	    try {
	        error.close();
	        op.close();
	    } catch (final IOException e) {
	    }
	    return "exitVal: " + exitVal + ", error: " + error + ", output: " + output;
	}
	
	
	
	public static int extract_flow(String str, Pattern TAG_REGEX)
	{
		//String str_mod = str.replace("('<RHS>', ", "<RHS>");
		//str_mod = str_mod.replace(", '</RHS>')", "</RHS>");
		//str_mod = str_mod.replace("('<LHS>', ", "<LHS>");
		//str_mod = str_mod.replace(", '</LHS>')", "</LHS>");
		final List<Integer> tagValues = new ArrayList<Integer>();
		if(str != null || str != "")
		{
	    final Matcher matcher = TAG_REGEX.matcher(str);
	    while (matcher.find()) {
	    	try{
	        tagValues.add(Integer.parseInt((matcher.group(1)).trim()));
	        return Collections.max(tagValues);
	    	}catch (NumberFormatException e){
	    	       System.out.println("not a number"); 
	    	   } 
	    }
	    return 0;
		}
		else
		{
			return 0;
		}
	}
	
	
	public static String video_parse()
	{
		String pythonScriptPath = System.getProperty("user.dir")+"//blobDetection.py";
		String[] cmd = new String[2];
		cmd[0] = "python"; // check version of installed python: python -V
		cmd[1] = pythonScriptPath;
		String content = "";
		 
		try {
		
		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		// retrieve output from python script
		BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		while((line = bfr.readLine()) != null) {
			content = content + line + "\n";
		// display each output line form python script
		//System.out.println(line);
		}
		} catch (IOException e) {
	        e.printStackTrace();
	    }
		System.out.println("\n\n\n\n\n"+content);
		return content;
	}
	
	
	
	public static void video_download(String fAddress, String destinationDir, String localFileName)
	{
		BufferedOutputStream outStream=null;
		InputStream is=null;
		HttpURLConnection  conn=null;
		int size=8192;
		
		try {
	        URL url;
	        byte[] buf;
	        int byteRead, byteWritten = 0;
	        url = new URL(fAddress);
	        outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + localFileName));

	        conn = (HttpURLConnection)url.openConnection();
	        is = conn.getInputStream();
	        buf = new byte[size];
	        while ((byteRead = is.read(buf)) != -1) {
	            outStream.write(buf, 0, byteRead);
	            byteWritten += byteRead;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	            outStream.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	
	public static void execute_plume_service(String web_service_url)
	{
		try {
	        EventQueue.invokeLater(new Runnable()
	        {
	            public void run(){
	                ImageFrame frame = new ImageFrame();
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                frame.setVisible(true);
	            }
	        });
	    
			//************************OLD Code Start
			
			
			//String location = "London";
			//String web_service_url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text=%22{"+location+"}%22&format=xml";
			/*URL url = new URL(web_service_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}*/

			FileReader in = new FileReader("output.kml");
			BufferedReader br = new BufferedReader(in);

			String output;
			String xml_output = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				xml_output = xml_output + output;
	            }
			in.close();
			System.out.println("Output KML File is : " + xml_output);
			//conn.disconnect();
			try {
			 MqttMessage message = new MqttMessage(xml_output.getBytes());
			 message.setQos(qos);
			 if(sampleClient_RP.isConnected()){
				 sampleClient_RP.publish("iot_data", message);
			 }
			 else
			 {
				 sampleClient_RP.connect(connOpts);
				 sampleClient_RP.publish("iot_data", message);
			}
			
			 Thread.sleep(4000);
			 String content = "Execution Completed!";
			 MqttMessage comp_message = new MqttMessage(content.getBytes());
			 comp_message.setQos(qos);
			 if(sampleClient_RP.isConnected()){
				 sampleClient_RP.publish("iot_data", comp_message);
			 }
			
			/* try
			{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			 Document doc = docBuilder.parse (new File("output.xml"));

			 doc.getDocumentElement().normalize();
			 System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

			 NodeList listOfPlaces = doc.getElementsByTagName("place");
			 int totalPlaces = listOfPlaces.getLength();
			 System.out.println("Total no of places : " + totalPlaces);
			 
			 for(int i=0; i<listOfPlaces.getLength() ; i++) {

			        Node firstPlaceNode = listOfPlaces.item(i);
			        if(firstPlaceNode.getNodeType() == Node.ELEMENT_NODE) {

			            Element firstElement = (Element)firstPlaceNode;                              
			            
			            NodeList firstNameList = firstElement.getElementsByTagName("woeid");
			            Element firstNameElement = (Element)firstNameList.item(0);

			            NodeList textFNList = firstNameElement.getChildNodes();
			            String woeid = ((Node)textFNList.item(0)).getNodeValue().trim();
			            System.out.println("woeid : " + woeid);
			            MqttMessage message = new MqttMessage(woeid.getBytes());
						message.setQos(qos);
						if(sampleClient.isConnected()){
						sampleClient.publish("iot_data", message);
						}
						else
						{
							sampleClient.connect(connOpts);
							sampleClient.publish("iot_data", message);
						}
						Thread.sleep(4000);
			        }
			    }//end of for loop with s var
			 String content = "Execution Completed!";
			 MqttMessage message = new MqttMessage(content.getBytes());
			 message.setQos(qos);
			 if(sampleClient.isConnected()){
			sampleClient.publish("iot_data", message);
			 }
			 
			}catch (Throwable t) {
			    t.printStackTrace ();
			} */    
		//*********************************OLD Code End	 
		} catch (Throwable e) {
			e.printStackTrace();
		} } catch (IOException e) {
			e.printStackTrace();
			}
	}
	

	public static void execute_service(String web_service_url)
	{
		try {
			//String location = "London";
			//String web_service_url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text=%22{"+location+"}%22&format=xml";
			URL url = new URL(web_service_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			BufferedWriter out = new BufferedWriter(new FileWriter("C://Users//sehrish//workspace//google-maps//output.xml"));
			
			String output;
			String xml_output = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				out.write(output);
				xml_output = xml_output + output;
	            out.newLine();
	            }
			out.close();
			conn.disconnect();
			
			try
			{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			 Document doc = docBuilder.parse (new File("output.xml"));

			 doc.getDocumentElement().normalize();
			 System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

			 NodeList listOfPlaces = doc.getElementsByTagName("place");
			 int totalPlaces = listOfPlaces.getLength();
			 System.out.println("Total no of places : " + totalPlaces);
			 
			 for(int i=0; i<listOfPlaces.getLength() ; i++) {

			        Node firstPlaceNode = listOfPlaces.item(i);
			        if(firstPlaceNode.getNodeType() == Node.ELEMENT_NODE) {

			            Element firstElement = (Element)firstPlaceNode;                              
			            
			            NodeList firstNameList = firstElement.getElementsByTagName("woeid");
			            Element firstNameElement = (Element)firstNameList.item(0);

			            NodeList textFNList = firstNameElement.getChildNodes();
			            String woeid = ((Node)textFNList.item(0)).getNodeValue().trim();
			            System.out.println("woeid : " + woeid);
			            MqttMessage message = new MqttMessage(woeid.getBytes());
						message.setQos(qos);
						if(sampleClient.isConnected()){
						sampleClient.publish("iot_data", message);
						}
						else
						{
							sampleClient.connect(connOpts);
							sampleClient.publish("iot_data", message);
						}
						Thread.sleep(4000);
			        }
			    }//end of for loop with s var
			 String content = "Execution Completed!";
			 MqttMessage message = new MqttMessage(content.getBytes());
			 message.setQos(qos);
			 if(sampleClient.isConnected()){
			sampleClient.publish("iot_data", message);
			 }
			 
			}catch (Throwable t) {
			    t.printStackTrace ();
			}     
			 
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
			
		}

	
	}
	
	
	public static void Set_Sensor_Location() 
	{
		System.out.println("I am setting sensor location");
		String jar_path = System.getProperty("user.dir")+"//target//gps-vehicle-simulator-1.0.0.BUILD-SNAPSHOT.jar";
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", jar_path);
        pb.directory(new File(System.getProperty("user.dir")+"//target//"));
        try {
            Process p = pb.start();
            LogStreamReader lsr = new LogStreamReader(p.getInputStream());
            Thread thread = new Thread(lsr, "LogStreamReader");
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	public static void Get_Location_Update() 
	{
		try
	    {
	      String myUrl = "http://localhost:8080/api/status";
	      // if your url can contain weird characters you will want to 
	      // encode it here, something like this:
	      // myUrl = URLEncoder.encode(myUrl, "UTF-8");

	      String results = doHttpUrlConnectionAction(myUrl);
	      System.out.println(results);
	      JSON_Parser(results);
	    }
	    catch (Exception e)
	    {
	      // deal with the exception in your "controller"
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
	
	public static void main(String[] args) throws InterruptedException, IOException
	{
		Preparation.clean_up();
		
		args_3 = args[3];
		
		// Creating a MQTT Broker using Moquette
        final IConfig classPathConfig = new ClasspathConfig();
        
		final Server mqttBroker = new Server();
		final List<? extends InterceptHandler> userHandlers = Arrays.asList(new PublisherListener());
		mqttBroker.startServer(classPathConfig, userHandlers);

		System.out.println("moquette mqtt broker started, press ctrl-c to shutdown..");
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				System.out.println("stopping moquette mqtt broker..");
				mqttBroker.stopServer();
				System.out.println("moquette mqtt broker stopped");
			}
		});

		Thread.sleep(4000);

		// Creating a MQTT Client using Eclipse Paho
		String content = "Hi, I am IoT Device!";
		
		IoT_Device_1 = args[1];
		IoT_Device_2 = args[2];
		
		String clientId = "RP_Peer_Node_2_"+args[2];
		System.out.println("clientId ="+clientId);
		
		
		String broker_RP = args[0];
		String clientId_RP = "RP_RP1_"+args[0];
		System.out.println("clientId_RP ="+clientId_RP);
		
		try
		{
			/************************************RP CLIENT START**********************************/
			sampleClient_RP = new MqttClient(broker_RP, clientId_RP, new MemoryPersistence());
			connOpts_RP = new MqttConnectOptions();
			connOpts_RP.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
			connOpts_RP.setCleanSession(true);
			connOpts_RP.setKeepAliveInterval(1000); 
			
			sampleClient_RP.connect(connOpts_RP);

			System.out.println("Raspberry Pi Connected to Broker");
			sampleClient_RP.subscribe("Service_Execution_Peer_Node", qos);
			System.out.println("Raspberry Pi subscribed to topic Service_Execution_Peer_Node");
			
			sampleClient_RP.setCallback(  new MqttCallback()
			{
				 public void connectionLost(Throwable throwable)
				 {
					    System.out.println("Connection to MQTT broker lost!");
					    System.out.println("Listeningg sampleClient_RP......");
				 }

				  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
				  {
						  System.out.println("-------------------------------------------------");
						  System.out.println("| Received ");
						  System.out.println("| Topic: "+topic);
						  System.out.println("| Message: "+new String(mqttMessage.getPayload()));
						  System.out.println("| QoS: "+mqttMessage.getQos());
						  System.out.println("-------------------------------------------------");
						 
				  }

				  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
				  {
					  System.out.println("Delivery complete.");
				  }
	        });
			
			/**Connection with second IoT Device */
			
			/************************************RP CLIENT START**********************************/
			/*****************************************PAHO CLIENT START*************************/
			System.out.println("Here I am publishing message");
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			sampleClient_RP.publish("iot_data", message);
			/************************************Paho CLIENT END**********************************/
		}
		catch (MqttException me)
		{
			me.printStackTrace();
		}
	}
	
	
	public static void publishTemperature() throws MqttException
	{
	    final String TOPIC_TEMPERATURE = "home/temperature";

        Random rand = new Random(); 
        final int temperatureNumber = rand.nextInt((30 - 20) + 1) + 20;
        //final int temperatureNumber = Utils.createRandomNumberBetween(20, 30);
        final String temperature = temperatureNumber + "°C";

        sampleClient_RP.publish(TOPIC_TEMPERATURE, new MqttMessage(temperature.getBytes()));

        System.out.println("Published data. Topic: " + TOPIC_TEMPERATURE + "  Message: " + temperature);
    }

    public static void publishBrightness() throws MqttException {
    	
    	final String TOPIC_BRIGHTNESS = "home/brightness";
    	
        Random rand = new Random(); 
        final int brightnessNumber = rand.nextInt((100 - 0) + 1) + 0;
        final String brigthness = brightnessNumber + "%";

        sampleClient_RP.publish(TOPIC_BRIGHTNESS, new MqttMessage(brigthness.getBytes()));

        System.out.println("Published data. Topic: " + TOPIC_BRIGHTNESS + "   Message: " + brigthness);
    }
	
    
	public static void openInBrowser_linux(String url)
	{
		String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
	
	try{

	    if (os.indexOf( "win" ) >= 0) {

	        // this doesn't support showing urls in the form of "page.html#nameLink" 
	        rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);

	    } else if (os.indexOf( "mac" ) >= 0) {

	        rt.exec( "open " + url);

            } else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {

	        // Do a best guess on unix until we get a platform independent way
	        // Build a list of browsers to try, in this order.
	        String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
	       			             "netscape","opera","links","lynx"};
	        	
	        // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
	        StringBuffer cmd = new StringBuffer();
	        for (int i=0; i<browsers.length; i++)
	            cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");
	        	
	        rt.exec(new String[] { "sh", "-c", cmd.toString() });

           } else {
                return;
           }
       }catch (Exception e){
	    return;
       }
	}
    
    
}

class SensorMqttCallBack implements MqttCallback
{
	String deviceName;
	
	SensorMqttCallBack(String deviceName)
	{
		this.deviceName = deviceName;
	}
	
	public void connectionLost(Throwable throwable)
	{
		System.out.println("Listening IoT_Device_" + deviceName + "_Client......");
    }

	 public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
	 {
		System.out.println("-------------------------------------------------");
		System.out.println("| Received ");
		System.out.println("| Topic: "+ topic);
		System.out.println("| Message: "+new String(mqttMessage.getPayload()));
		System.out.println("| QoS: "+mqttMessage.getQos());
		System.out.println("-------------------------------------------------");
		
		if(deviceName == "SWS")
		{
			Main_Rasp_IoT.wind_speed_count++;
			System.out.println("wind_speed_count = " + Main_Rasp_IoT.wind_speed_count);
		}
		else if(deviceName == "SWD")
		{
			Main_Rasp_IoT.wind_direction_count++;
			System.out.println("wind_direction_count = " + Main_Rasp_IoT.wind_direction_count);
		}
		else if(deviceName == "ST")
		{
			Main_Rasp_IoT.temperature_count++;
			System.out.println("temperature_count = " + Main_Rasp_IoT.temperature_count);
		}
		else if(deviceName == "SH")
		{
			Main_Rasp_IoT.humidity_count++;
			System.out.println("humidity_count = " + Main_Rasp_IoT.humidity_count);
		}
		else if(deviceName == "SAC")
		{
			Main_Rasp_IoT.aerosol_concentration_count++;
			System.out.println("aerosol_concentration_count = " + Main_Rasp_IoT.aerosol_concentration_count);
		}
	 }

	  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
	  {
		  System.out.println("Delivery complete.");
	  }
}

class SensorServiceHandler implements Runnable
{
	Thread t;
	private boolean exit;
	
	MqttClient mqttClient;
	String sensorTopic;
	String sensorName;
	
	SensorServiceHandler(String deviceURI, int deviceNumber, String sensorName, String sensorTopic, MqttClient mqttClient)
	{
		try
		{
			this.sensorTopic = sensorTopic;
			this.sensorName = sensorName;
			this.mqttClient = mqttClient;
			
			t = new Thread(this);
			exit = false;
			System.out.println("New thread: " + t);
			t.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(!exit)
		{
			try
			{
				Thread.sleep(60000);
		        
		        String sensorReading = null;
		        
		        if(sensorName == "SWS")
				{
		        	int Wind_Speed_Number = -1;
			        
			        try
					{
						HttpResponse<String> response = Unirest.post("http://203.135.63.70/SensorDataGenerationAndIntegration/services/Wind_Speed_Sensor")
						  .header("SOAPAction", "\"\"")
						  .header("Content-Type", "text/plain")
						  .body("<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n    <Body>\r\n        <getWindSpeed xmlns=\"http://incident_management\"/>\r\n    </Body>\r\n</Envelope>")
						  .asString();
						
						Document doc = Main_Rasp_IoT.convertStringToDocument(response.getBody());
						
						Wind_Speed_Number = Integer.parseInt(doc.getElementsByTagName("getWindSpeedReturn").item(0).getChildNodes().item(0).getNodeValue());
					}
					catch (UnirestException e)
					{
						e.printStackTrace();
					}
		        	
					sensorReading = Wind_Speed_Number + " knots";
				}
				else if(sensorName == "SWD")
				{
					int Wind_Direction_Number = -1;
			        
			        try
					{
			        	HttpResponse<String> response = Unirest.post("http://203.135.63.70/SensorDataGenerationAndIntegration/services/Wind_Direction_Sensor")
			      			  .header("SOAPAction", "\"\"")
			      			  .header("Content-Type", "text/plain")
			      			  .body("<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n    <Body>\r\n        <getWindDirection xmlns=\"http://incident_management\"/>\r\n    </Body>\r\n</Envelope>")
			      			  .asString();
						
						Document doc = Main_Rasp_IoT.convertStringToDocument(response.getBody());
						
						Wind_Direction_Number = Integer.parseInt(doc.getElementsByTagName("getWindDirectionReturn").item(0).getChildNodes().item(0).getNodeValue());
					}
					catch (UnirestException e)
					{
						e.printStackTrace();
					}
					
					sensorReading = Wind_Direction_Number + " azimuth";
				}
				else if(sensorName == "ST")
				{
					int temperatureNumber = -1;
			        
			        try
					{
			        	HttpResponse<String> response = Unirest.post("http://203.135.63.70/SensorDataGenerationAndIntegration/services/TemperatureSensor")
						  .header("SOAPAction", "\"\"")
						  .header("Content-Type", "text/plain")
						  .body("<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n    <Body>\r\n        <getTemperature xmlns=\"http://incident_management\"/>\r\n    </Body>\r\n</Envelope>")
						  .asString();
						
						Document doc = Main_Rasp_IoT.convertStringToDocument(response.getBody());
						
						temperatureNumber = Integer.parseInt(doc.getElementsByTagName("getTemperatureReturn").item(0).getChildNodes().item(0).getNodeValue());
					}
					catch (UnirestException e)
					{
						e.printStackTrace();
					}

			        sensorReading = temperatureNumber + " °C";
				}
				else if(sensorName == "SH")
				{
					int Humidity_Number = -1;
			 	    
					try
			 	   	{
			 		  HttpResponse<String> response = Unirest.post("http://203.135.63.70/SensorDataGenerationAndIntegration/services/HumiditySensor")
							  .header("SOAPAction", "\"\"")
							  .header("Content-Type", "text/plain")
							  .body("<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n    <Body>\r\n        <getHumidity xmlns=\"http://incident_management\"/>\r\n    </Body>\r\n</Envelope>")
							  .asString();
						
						Document doc = Main_Rasp_IoT.convertStringToDocument(response.getBody());
						
						Humidity_Number = Integer.parseInt(doc.getElementsByTagName("getHumidityReturn").item(0).getChildNodes().item(0).getNodeValue());
					}
					catch (UnirestException e)
					{
						e.printStackTrace();
					}
					
					sensorReading = Humidity_Number + " RH";
				}
				else if(sensorName == "SAC")
				{
					int Aerosol_Concentration_Number = -1;
			 	    
					try
					{
			 		   	HttpResponse<String> response = Unirest.post("http://203.135.63.70/SensorDataGenerationAndIntegration/services/AerosolConcentrationSensor")
						  .header("SOAPAction", "\"\"")
						  .header("Content-Type", "text/plain")
						  .body("<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n    <Body>\r\n        <getAerosolConcentartion xmlns=\"http://incident_management\"/>\r\n    </Body>\r\n</Envelope>")
						  .asString();
							
						Document doc = Main_Rasp_IoT.convertStringToDocument(response.getBody());
							
						Aerosol_Concentration_Number = Integer.parseInt(doc.getElementsByTagName("getAerosolConcentartionReturn").item(0).getChildNodes().item(0).getNodeValue());
					}
					catch (UnirestException e)
					{
						e.printStackTrace();
					}

		 	        sensorReading = Aerosol_Concentration_Number + " cm";
				}
		        
		        if(mqttClient.isConnected())
		        {
			        try
			        {
			        	mqttClient.publish(sensorTopic, new MqttMessage(sensorReading.getBytes()));
			        }
			        catch (MqttException me)
			        {
						me.printStackTrace();
					}
		        }
			}
			catch (InterruptedException e)
			{
				t.interrupt();
				System.out.println("insdide catch() Interupted val:::" + Thread.interrupted());
				System.out.println("["+Thread.currentThread().getName()+"] Interrupted by exception!");
				System.out.println("Error occured while executing Linux command. Error Description: "+ e.getMessage());
			}
			
		}
	}
	
	public void stop() 
    { 
        exit = true; 
        System.out.println("Sensor " + sensorName + " Stopped");
    } 
}

class LogStreamReader implements Runnable
{
    private BufferedReader reader;
    public static String vehicle_status_output="";

    public LogStreamReader(InputStream is) {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        try {
            String line = reader.readLine();
            String output = "";
            output =  output + line;
            while (line != null) {
            	//System.out.println("------------********************------------");
            	if(Main_Rasp_IoT.Process_Info.contains("integration"))
            	{
            		Main_Rasp_IoT.thread_counter = 0;
            	}
                //System.out.println(line);
                output =  output + line;
                if(!(line.contains("Processing")))
                	Main_Rasp_IoT.task_output = output;
                if(line.contains("Started GpsSimulatorApplication"))
                {
                	try{
                	System.out.println("******Visiting URL**********");
                	openURL("http://localhost:8080/api/dc");
                	Thread.sleep(5000);
                	openURL("plume_display.html");
                	Thread.sleep(5000);
                	openURL("http://localhost:8080/api/cancel");
                	}catch (InterruptedException e) {
                        System.out.println("Error occured while executing Linux command. Error Description: "
                        		+ e.getMessage());
                        	}
                }
                
                line = reader.readLine();
            }
            
            System.out.println("<thread_counter>"+Main_Rasp_IoT.thread_counter +" My Output is: "+output);
            System.out.println("Main_Rasp_IoT.Process_Info ="+Main_Rasp_IoT.Process_Info);
            
            if(Main_Rasp_IoT.Process_Info.contains("Traffic"))
        	{
            	Main_Rasp_IoT.thread_counter = 0;
        	}
            
            System.out.println("Here End");
            reader.close();
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void openURL(String url)
	{
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^");
		String os = System.getProperty("os.name").toLowerCase();
	        Runtime rt = Runtime.getRuntime();
		
		try{

		    if (os.indexOf( "win" ) >= 0) {

		        // this doesn't support showing urls in the form of "page.html#nameLink" 
		        rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);

		    } else if (os.indexOf( "mac" ) >= 0) {

		        rt.exec( "open " + url);

	            } else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {

		        // Do a best guess on unix until we get a platform independent way
		        // Build a list of browsers to try, in this order.
		        String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
		       			             "netscape","opera","links","lynx"};
		        	
		        // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
		        StringBuffer cmd = new StringBuffer();
		        for (int i=0; i<browsers.length; i++)
		            cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");
		        	
		        rt.exec(new String[] { "sh", "-c", cmd.toString() });

	           } else {
	                return;
	           }
	       }catch (Exception e){
		    return;
	       }
	}
    
    
}

class LogStreamReader_4 implements Runnable {

    private BufferedReader reader;
    public static String vehicle_status_output="";

    public LogStreamReader_4(InputStream is) {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        try {
            String line = reader.readLine();
            String output = "";
            output =  output + line;
            while (line != null) {
            	//System.out.println("------------********************------------");
            	if(Main_Rasp_IoT.Process_Info.contains("integration"))
            	{
            		Main_Rasp_IoT.thread_counter = 0;
            	}
                //System.out.println(line);
                output =  output + line;
                if(!(line.contains("Processing")))
                {
                	Main_Rasp_IoT.task_output = output;
                }
                if(line.contains("Started GpsSimulatorApplication"))
                {
                	try{
                	System.out.println("******Visiting URL**********");
                	openURL("http://localhost:8080/api/dc");
                	Thread.sleep(5000);
                	openURL("plume_display.html");
                	Thread.sleep(5000);
                	openURL("http://localhost:8080/api/cancel");
                	}catch (InterruptedException e) {
                        System.out.println("Error occured while executing Linux command. Error Description: "
                        		+ e.getMessage());
                        	}
                }
                
                line = reader.readLine();
               
            }
            
            
            System.out.println("<thread_counter> "+Main_Rasp_IoT.thread_counter+" My Output is: "+output);
            System.out.println("Main_Rasp_IoT.Process_Info ="+Main_Rasp_IoT.Process_Info);
            
            if(Main_Rasp_IoT.Process_Info.contains("Traffic"))
        	{
            	Main_Rasp_IoT.thread_counter = Main_Rasp_IoT.thread_counter - 1;
            	System.out.println("Main_Rasp_IoT.task_output_count ="+Main_Rasp_IoT.task_output_count);
            	Main_Rasp_IoT.task_4_output[Main_Rasp_IoT.task_output_count] = output;
            	Main_Rasp_IoT.task_output_count = Main_Rasp_IoT.task_output_count + 1;
        	}
            System.out.println("Here End");
            reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
   
    
    public void openURL(String url)
	{
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^");
		String os = System.getProperty("os.name").toLowerCase();
	        Runtime rt = Runtime.getRuntime();
		
		try{

		    if (os.indexOf( "win" ) >= 0) {

		        // this doesn't support showing urls in the form of "page.html#nameLink" 
		        rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);

		    } else if (os.indexOf( "mac" ) >= 0) {

		        rt.exec( "open " + url);

	            } else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {

		        // Do a best guess on unix until we get a platform independent way
		        // Build a list of browsers to try, in this order.
		        String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
		       			             "netscape","opera","links","lynx"};
		        	
		        // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
		        StringBuffer cmd = new StringBuffer();
		        for (int i=0; i<browsers.length; i++)
		            cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");
		        	
		        rt.exec(new String[] { "sh", "-c", cmd.toString() });

	           } else {
	                return;
	           }
	       }catch (Exception e){
		    return;
	       }
	}
    
    
}



class Task implements Runnable {
	String jarFilePath;
	Thread t;
	//Thread thread;
	Task (String jarFilePathName){
		jarFilePath = jarFilePathName; 
		t = new Thread(this, jarFilePath);
		System.out.println("New thread: " + t);
		t.start();

		}
	public void run() {
		try {
     /*for(int i = 5; i > 0; i--) {
     System.out.println(command + ": " + i);
      Thread.sleep(1000);
	  }*/
	 /***Here New Code *******/
	 System.out.println("jarFilePath= "+jarFilePath);
	if(jarFilePath.contains("gps-vehicle"))
	{
		//String command = "java -jar "+ jarFilePath;
		String command = Main_Rasp_IoT.Execution_Command;
		System.out.println("Here IF Command ="+command);
		//String command = ("/usr/bin/stdbuf","-o0","java -jar data_integration_service.jar","");;
		boolean waitForResponse = false;
		
		String response = "";
		 
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
		pb.redirectErrorStream(true);
		
		System.out.println("Linux command: " + command);
		 
		try {
		Process shell = pb.start();
		System.out.println("here creating process");
		InputStream shellIn = shell.getInputStream();
        LogStreamReader lsr = new LogStreamReader(shellIn);
        Thread thread = new Thread(lsr, "LogStreamReader");
        thread.start();
        //Main_Rasp_IoT.thread_counter = Main_Rasp_IoT.thread_counter + 1;
        System.out.println("Continue with Execution"); 
		
		if (waitForResponse) { 
			// To capture output from the shell
			//InputStream shellIn = shell.getInputStream();
			 
			// Wait for the shell to finish and get the return code
			int shellExitStatus = shell.waitFor();
			System.out.println("Exit status" + shellExitStatus);
			 
			response = Main_Rasp_IoT.convertStreamToStr(shellIn);
			//System.out.println("response is= "+response);
			 
			shellIn.close();
			}
		 
		}
		 
		catch (IOException e) {
		System.out.println("Error occured while executing Linux command. Error Description: "
		+ e.getMessage());
		}
		 
		catch (InterruptedException e) {
		System.out.println("Error occured while executing Linux command. Error Description: "
		+ e.getMessage());
		}	 
		//return response;
	}
	
	else
		{
		
	//String command = "java -jar "+ jarFilePath;
	String command = Main_Rasp_IoT.Execution_Command;
	System.out.println("Here ELSE Command ="+command);
	//String command = ("/usr/bin/stdbuf","-o0","java -jar data_integration_service.jar","");;
	boolean waitForResponse = false;
	
	String response = "";
	 
	ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
	pb.redirectErrorStream(true);
	 
	System.out.println("Linux command: " + command);
	 
	try {
	Process shell = pb.start();
	/** new code addition **/
	//System.out.println("here creating process");
	InputStream shellIn = shell.getInputStream();
    LogStreamReader lsr = new LogStreamReader(shellIn);
    Thread thread = new Thread(lsr, "LogStreamReader");
    thread.start();
    //thread.join();
    System.out.println("Continue with Execution"); 
    /** new code end **/
	 
	if (waitForResponse) {
	 
	// To capture output from the shell
	//InputStream shellIn = shell.getInputStream();
	 
	// Wait for the shell to finish and get the return code
	int shellExitStatus = shell.waitFor();
	System.out.println("Exit status" + shellExitStatus);
	 
	response = Main_Rasp_IoT.convertStreamToStr(shellIn);
	//System.out.println("response is= "+response);
	 
	shellIn.close();
	}
	 
	}
	catch (IOException e) {
	System.out.println("Error occured while executing Linux command. Error Description: "
	+ e.getMessage());
	}
	 
	/*catch (InterruptedException e) {
	System.out.println("Error occured while executing Linux command. Error Description: "
	+ e.getMessage());
	}*/	 
	//return response;

}
}catch (InterruptedException e) {
	t.interrupt();
	System.out.println("insdide catch() Interupted val:::"+t.interrupted());
	System.out.println("["+Thread.currentThread().getName()+"] Interrupted by exception!");
    //System.out.println(jarFilePath + "Interrupted");
}
     System.out.println(jarFilePath + " exiting.");
}
}

class Task_4 implements Runnable {
	String jarFilePath;
	Thread t;
	//Thread thread;
	Task_4 (String jarFilePathName){
		jarFilePath = jarFilePathName; 
		t = new Thread(this, jarFilePath);
		System.out.println("New thread: " + t);
		t.start();
	
		}
	public void run() {
		try {
     /*for(int i = 5; i > 0; i--) {
     System.out.println(command + ": " + i);
      Thread.sleep(1000);
	  }*/
	 /***Here New Code *******/
	 System.out.println("jarFilePath= "+jarFilePath);
	if(jarFilePath.contains("gps-vehicle"))
	{
		//String command = "java -jar "+ jarFilePath;
		String command = Main_Rasp_IoT.Execution_Command;
		System.out.println("Here IF Command ="+command);
		//String command = ("/usr/bin/stdbuf","-o0","java -jar data_integration_service.jar","");;
		boolean waitForResponse = false;
		
		String response = "";
		 
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
		pb.redirectErrorStream(true);
		
		System.out.println("Linux command: " + command);
		 
		try {
		Process shell = pb.start();
		System.out.println("here creating process");
		InputStream shellIn = shell.getInputStream();
		LogStreamReader_4 lsr = new LogStreamReader_4(shellIn);
        Thread thread = new Thread(lsr, "LogStreamReader");
        thread.start();
        //Main_Rasp_IoT.thread_counter = Main_Rasp_IoT.thread_counter + 1;
        System.out.println("Continue with Execution"); 
		
		if (waitForResponse) { 
			// To capture output from the shell
			//InputStream shellIn = shell.getInputStream();
			 
			// Wait for the shell to finish and get the return code
			int shellExitStatus = shell.waitFor();
			System.out.println("Exit status" + shellExitStatus);
			 
			response = Main_Rasp_IoT.convertStreamToStr(shellIn);
			//System.out.println("response is= "+response);
			 
			shellIn.close();
			}
		 
		}
		 
		catch (IOException e) {
		System.out.println("Error occured while executing Linux command. Error Description: "
		+ e.getMessage());
		}
		 
		catch (InterruptedException e) {
		System.out.println("Error occured while executing Linux command. Error Description: "
		+ e.getMessage());
		}	 
		//return response;
	}
	
	else
		{
		
	//String command = "java -jar "+ jarFilePath;
	String command = Main_Rasp_IoT.Execution_Command;
	System.out.println("Here ELSE Command ="+command);
	//String command = ("/usr/bin/stdbuf","-o0","java -jar data_integration_service.jar","");;
	boolean waitForResponse = false;
	
	String response = "";
	 
	ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
	pb.redirectErrorStream(true);
	 
	System.out.println("Linux command: " + command);
	 
	try {
	Process shell = pb.start();
	/** new code addition **/
	//System.out.println("here creating process");
	InputStream shellIn = shell.getInputStream();
    LogStreamReader_4 lsr = new LogStreamReader_4(shellIn);
    Thread thread = new Thread(lsr, "LogStreamReader");
    thread.start();
    //thread.join();
    System.out.println("Continue with Execution"); 
    /** new code end **/
	 
	if (waitForResponse) {
	 
	// To capture output from the shell
	//InputStream shellIn = shell.getInputStream();
	 
	// Wait for the shell to finish and get the return code
	int shellExitStatus = shell.waitFor();
	System.out.println("Exit status" + shellExitStatus);
	 
	response = Main_Rasp_IoT.convertStreamToStr(shellIn);
	//System.out.println("response is= "+response);
	 
	shellIn.close();
	}
	 
	}
	catch (IOException e) {
	System.out.println("Error occured while executing Linux command. Error Description: "
	+ e.getMessage());
	}
	 
	/*catch (InterruptedException e) {
	System.out.println("Error occured while executing Linux command. Error Description: "
	+ e.getMessage());
	}*/	 
	//return response;

}
}catch (InterruptedException e) {
	t.interrupt();
	System.out.println("insdide catch() Interupted val:::"+t.interrupted());
	System.out.println("["+Thread.currentThread().getName()+"] Interrupted by exception!");
    //System.out.println(jarFilePath + "Interrupted");
}
     System.out.println(jarFilePath + " exiting.");
}
}



class StreamGobbler implements Runnable {
	  private final InputStream is;
	  private final PrintStream os;
	 
	  StreamGobbler(InputStream is, PrintStream os) {
	    this.is = is;
	    this.os = os;
	  }
	 
	  public void run() {
	    try {
	      int c;
	      while ((c = is.read()) != -1)
	          os.print((char) c);
	    } catch (IOException x) {
	      // Handle error
	    }
	  }
	}

class ImageFrame extends JFrame{

    public ImageFrame(){
        setTitle("Aloha Plume Model");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        ImageComponent component = new ImageComponent();
        add(component);

    }

    public static final int DEFAULT_WIDTH = 435;
    public static final int DEFAULT_HEIGHT = 395;
}


class ImageComponent extends JComponent{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Image image;
    public ImageComponent(){
        try{
            File image2 = new File(System.getProperty("user.dir")+"//Aloha.jpg");
            image = ImageIO.read(image2);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void paintComponent (Graphics g){
        if(image == null) return;
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        g.drawImage(image, 2, 2, this);

        for (int i = 0; i*imageWidth <= getWidth(); i++)
            for(int j = 0; j*imageHeight <= getHeight();j++)
                if(i+j>0) g.copyArea(0, 0, imageWidth, imageHeight, i*imageWidth, j*imageHeight);
    }

}

/*class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    
    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                System.out.println(type + ">" + line);    
            } catch (IOException ioe)
              {
                ioe.printStackTrace();  
              }
    }
}*/