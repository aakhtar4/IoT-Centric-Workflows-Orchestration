package coordination;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Mobility implements Runnable {

	String[] device_name = new String[20];
	int device_left = 0;
	int device_active = 20;
	int device_count = 20;
	int current_active_device = 20;
	public static String device_IP_Address[] = new String[20];
	double residence_prob[] = new double[20];
	public static double residence_time_minutes[] = new double[20];
	public static double residence_time_assigned[] = new double[20];
	double residence_time_millisec[] = new double[20];
	public static long startTime[] = new long[20];
	public long stopTime[] = new long[20];
	public long elapsedTime[] = new long[20];
	public double elapsedTime_in_sec = 0;
	public static long device_left_time[] = new long[100];
	public double annoucement_start_time = 0;
	public double annoucement_end_time = 0;
	public double annoucement_elapsed_time = 0;
	public double device_last_update_time[] = new double[20];
	public static String arg_5 = "";
	String device_status = "";
	
	
	Thread t;
	Mobility()
	{
		device_name = new String[]{"Device_1", "Device_2", "Device_3", "Device_4", "Device_5", "Device_6", "Device_7", "Device_8", "Device_9", "Device_10", "Device_11", "Device_12", "Device_13", "Device_14", "Device_15", "Device_16", "Device_17", "Device_18", "Device_19", "Device_20"};
		device_IP_Address = new String[]{"10.103.72.38", "10.103.72.39", "10.103.72.40", "10.103.72.41", "10.103.72.42", "10.103.72.43", "10.103.72.44", "10.103.72.45", "10.103.72.46", "10.103.72.47", "10.103.72.48",  "10.103.72.49","10.103.72.50",  "10.103.72.51", "10.103.72.52", "10.103.72.53", "10.103.72.54", "10.103.72.55", "10.103.72.56", "10.103.72.57"};
		residence_time_minutes = new double[]{7, 7, 10, 10, 10, 9, 12, 5, 15, 6, 12, 6, 12, 10, 12, 7, 15, 10, 7, 14};
		residence_time_assigned = new double[]{7, 7, 10, 10, 10, 9, 12, 5, 15, 6, 12, 6, 12, 10, 12, 7, 15, 10, 7, 14};
	
		t = new Thread(this);
		System.out.println("New thread: " + t);
		t.start();
	}
	
   public void run()
   {
	   for(int cd = 0; cd<20; cd++)
		{
			device_last_update_time[cd] = 0;
		}
			   
			
		while(device_active != 0)
		{
			try
			{
				System.out.println("*****************************Iteration Start************************");	
				get_device_status_after_2_mins();
				Thread.sleep(120000);
				System.out.println("*****************************Iteration End**************************\n\n");
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
   }
	
	public void get_device_status_after_2_mins()
	{
		for(int j=0; j<current_active_device; j++)
		{
		
				for(int kk=0;  kk < Main_Rasp_Combine.Deployment_Task_Count; kk++)
				{
					if( Main_Rasp_Combine.Deployment_Task_Details[kk].contains(device_IP_Address[j]))
					{
						double current_time = System.currentTimeMillis();
						double diff = (current_time - Main_Rasp_Combine.m1.device_last_update_time[j]);

						if(diff > 120005)
						{
							System.out.println("-------------------------------");
							System.out.println("Deployment Device Left");
							System.out.println("-------------------------------");
								
							String content = "IP Address: ["+ device_IP_Address[j] + "] Device has left {" + Main_Rasp_Combine.Assignment_deployment_task_id + "} <"+arg_5+"> {"+(kk+1)+"}";
							device_status = content + " ,";
							
					        MqttMessage execution_message = new MqttMessage(content.getBytes());
					        execution_message.setQos(Main_Rasp_Combine.qos);
					       
					        if(Main_Rasp_Combine.sampleClient_RP.isConnected())
					        {
					        	Main_Rasp_Combine.left_informed_flag = 1;
					        	try
					        	{
					        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[1]</Task>"))
					        		{
						        		String one = "<hint>[1]</hint> Start";
						        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
						        		deployment_msg.setQos(Main_Rasp_Combine.qos);
						        		
						        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
					        		}
					        		
					        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[2]</Task>"))
					        		{
						        		String one = "<hint>[2]</hint> Start";
						        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
						        		deployment_msg.setQos(Main_Rasp_Combine.qos);
						        		
						        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
					        		}
					        		
					        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[3]</Task>"))
					        		{
						        		String one = "<hint>[3]</hint> Start";
						        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
						        		deployment_msg.setQos(Main_Rasp_Combine.qos);
						        		
						        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
					        		}
					        		
					        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[4]</Task>"))
					        		{
						        		String four = "<hint>[4]</hint> Start";
						        		MqttMessage deployment_msg = new MqttMessage(four.getBytes());
						        		deployment_msg.setQos(Main_Rasp_Combine.qos);
						        		
						        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
					        		}
					        	}
					        	catch (MqttException me)
					        	{
									me.printStackTrace();
								}
					        }
					        else
					        {
					        	Main_Rasp_Combine.left_informed_flag = 1;
					        	try
								{
					        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[1]</Task>"))
					        		{
						        		String one = "<hint>[1]</hint> Start";
						        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
						        		deployment_msg.setQos(Main_Rasp_Combine.qos);
						        		
						        		Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
						        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
					        		}
					        		
					        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[2]</Task>"))
					        		{
						        		String one = "<hint>[2]</hint> Start";
						        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
						        		deployment_msg.setQos(Main_Rasp_Combine.qos);
						        		
						        		Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
						        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
					        		}
					        		
					        		
					        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[3]</Task>"))
					        		{
						        		String one = "<hint>[3]</hint> Start";
						        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
						        		deployment_msg.setQos(Main_Rasp_Combine.qos);
						        		
						        		Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
						        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
					        		}
					        		
					        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[4]</Task>"))
					        		{
						        		System.out.println("-------<hint>[4]</hint> Start------------");
						        		String four = "<hint>[4]</hint> Start";
						        		MqttMessage deployment_msg = new MqttMessage(four.getBytes());
						        		deployment_msg.setQos(Main_Rasp_Combine.qos);
						        		
						        		Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
						        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
					        		}
								}
					        	catch (MqttException me)
					        	{
									me.printStackTrace();
								}
					        
						}
						
						execution_message = new MqttMessage(device_status.getBytes());
				        execution_message.setQos(Main_Rasp_Combine.qos);
							
				        if(Main_Rasp_Combine.sampleClient_RP.isConnected())
				        {
				        	try
				        	{
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
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
				        		Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
							}
				        	catch (MqttException me)
				        	{
								me.printStackTrace();
							}
				        }
				    
				        device_status = "";
					}
				}
				
			}

			Add_New_Device(j);
		}
	}
	
	
	public void Add_New_Device(int index)
	{
		device_count = device_count + 1;
		device_name[index] = "Device_"+device_count;
		Random rand = new Random();
		int min = 10;
        int max = 15;
		int Availability_Duration = rand.nextInt((max - min) + 1) + min;
		residence_time_minutes[index] = Availability_Duration;
		startTime[index] = System.currentTimeMillis();
		device_active = device_active + 1;
		device_left = device_left - 1;
		
	}
}
