package coordination;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Mobility_LOM implements Runnable {

	String[] device_name = new String[20];
	int device_left = 0;
	int device_active = 20;
	int device_count = 20;
	int current_active_device = 20;
	public String device_IP_Address[] = new String[20];
	//double device_latitude[] = new double[20];
	//double device_longitude[] = new double[20];
	double residence_prob[] = new double[20];
	public double residence_time_minutes[] = new double[20];
	public double residence_time_assigned[] = new double[20];
	double residence_time_millisec[] = new double[20];
	public static long startTime[] = new long[20];
	public long stopTime[] = new long[20];
	public long elapsedTime[] = new long[20];
	public double elapsedTime_in_sec = 0;
	public long device_left_time[] = new long[100];
	public double annoucement_start_time = 0;
	public double annoucement_end_time = 0;
	public double annoucement_elapsed_time = 0;
	public double device_last_update_time[] = new double[20];
	public static String arg_5 = "";
	String device_status = "";
	
	public static double task_1_start_time = 0;
	public static int task_4_device_left_flag = 0;
	public static double task_4_device_leave_time = 0;
	
	/**TO CALCULATE REDEPLOYMENT DELAY**/
	public static double red_t1_stime = 0;
	public static double red_t1_etime = 0;
	public static double t1_red_time = 0;
	
	public static double red_t1_stime_1 = 0;
	public static double red_t1_etime_1 = 0;
	public static double t1_red_time_1 = 0;

	public static double red_t4_stime = 0;
	public static double red_t4_etime = 0;
	public static double t4_red_time = 0;
	
	public static double red_t4_stime_1 = 0;
	public static double red_t4_etime_1 = 0;
	public static double t4_red_time_1 = 0;
	/**TO CALCULATE REDEPLOYMENT DELAY**/
	
	/***UPLOADING TIME CALCULATION***/
	public static double uploading_start_time_1 = 0;
	public static double uploading_end_time_1 = 0;
	public static double uploading_time_1 = 0;
	public static double uploading_start_time_4 = 0;
	public static double uploading_end_time_4 = 0;
	public static double uploading_time_4 = 0;
	/***UPLOADING TIME CALCULATION***/
	
	Thread t;
	Mobility_LOM(){
		
		device_name = new String[]{"Device_1", "Device_2", "Device_3", "Device_4", "Device_5", "Device_6", "Device_7", "Device_8", "Device_9", "Device_10", "Device_11", "Device_12", "Device_13", "Device_14", "Device_15", "Device_16", "Device_17", "Device_18", "Device_19", "Device_20"};
		device_IP_Address = new String[]{"10.103.72.38", "10.103.72.39", "10.103.72.40", "10.103.72.41", "10.103.72.42", "10.103.72.43", "10.103.72.44", "10.103.72.45", "10.103.72.46", "10.103.72.47", "10.103.72.48",  "10.103.72.49","10.103.72.50",  "10.103.72.51", "10.103.72.52", "10.103.72.53", "10.103.72.54", "10.103.72.55", "10.103.72.56", "10.103.72.57"};
		//device_longitude = new double[]{-91.20283, -91.20281, -91.20645, -91.20543, -91.20477, -91.20483, -91.20664, -91.20624, -91.20470, -91.20417, -91.20709, -91.20321, -91.20414, -91.20380, -91.20558, -91.20408, -91.20596, -91.20814, -91.20285, -91.20583};
		//device_latitude = new double[]{30.45669, 30.45714, 30.45543, 30.45565, 30.45545, 30.45609, 30.45736, 30.45644, 30.45765, 30.45488, 30.45563, 30.45547, 30.45770, 30.45571, 30.45488, 30.45687, 30.45579, 30.45665, 30.45537, 30.45486};
		residence_time_minutes = new double[]{7, 7, 10, 10, 10, 9, 12, 5, 15, 6, 12, 6, 12, 10, 12, 7, 15, 10, 7, 14};
		residence_time_assigned = new double[]{7, 7, 10, 10, 10, 9, 12, 5, 15, 6, 12, 6, 12, 10, 12, 7, 15, 10, 7, 14};
		
	
		t = new Thread(this);
		System.out.println("New thread: " + t);
		t.start();
		}
	   public void run() {
		annoucement_start_time = System.currentTimeMillis();   
		/*try {}catch (InterruptedException e) {}*/
		while(device_active != 0)
		{
		try{
		System.out.println("*****************************Iteration Start************************");	
		//get_device_status();
		get_device_status_after_2_mins();
		Thread.sleep(30000);
		System.out.println("*****************************Iteration End**************************\n\n");
		}catch (Throwable e) {
			e.printStackTrace();
		}
		}
    }

	
	public void get_device_status()
	{
		for(int cd = 0; cd<20; cd++)
		{
		stopTime[cd] = System.currentTimeMillis();
		elapsedTime[cd] = stopTime[cd] - startTime[cd];
		}
		//elapsedTime = 60000;
		//elapsedTime_in_sec = elapsedTime/1000;
	    //System.out.println("elapsedTime ="+elapsedTime);
	   
		for(int k=0; k<current_active_device; k++)
		{
			if(residence_time_minutes[k] != 0)
			{
			residence_time_millisec[k] = residence_time_minutes[k] * 60 * 1000;
			//System.out.println("Original residence_time_millisec[k] ="+residence_time_millisec[k]);
			residence_time_millisec[k] = residence_time_millisec[k] - elapsedTime[k];
			//System.out.println("Elapsed residence_time_millisec[k] ="+residence_time_millisec[k]);
			residence_time_minutes[k] = (residence_time_millisec[k]/1000)/60;
			//System.out.println("residence_time_millisec[k] ="+residence_time_millisec[k]);
			//System.out.println("residence_time_minutes[k] ="+residence_time_minutes[k]);
			}
		}
		
		for (int i=0; i<current_active_device; i++)
		{
			if(residence_time_minutes[i] != 0)
			{
			double lambda = 0.2;
			double lambda_x =  -(residence_time_minutes[i] * lambda);
			//System.out.println("lambda_x = "+lambda_x);
			residence_prob[i] = 1 - (lambda * Math.exp(lambda_x));
			//System.out.println("residence_prob[i] = "+residence_prob[i]);
			}
		}
		
		int min = 0;
		int max = 10;
		int value = 0;
		int estimated_prob = 0;
		for(int j=0; j<current_active_device; j++)
		{
			if(residence_prob[j] != 0)
			{
			Random rand = new Random(); 
			value = rand.nextInt((max - min) + 1) + min;
			estimated_prob = (int) Math.round(residence_prob[j] * 10);
			//System.out.println("Random No. = "+value+"       Prob = "+estimated_prob);
			if(value <= estimated_prob)
			{
				System.out.println(device_name[j] + ": Active");
			}
			else
			{
				System.out.println(device_name[j] + " IP Adress: " + device_IP_Address[j] + ": Left");
				residence_time_millisec[j] = 0;
				residence_time_minutes[j] = 0;
				residence_prob[j] = 0;
				device_active = device_active - 1;
				device_left = device_left + 1;
				System.out.println("-----------------------------------------------------");
				System.out.println("Main_Rasp.Deployment_Task_Count ="+Main_Rasp_Combine.Deployment_Task_Count);
				//System.out.println("Main_Rasp.Deployment_Task_Details[kk] ="+Main_Rasp.Deployment_Task_Details[0]);
				//System.out.println("Main_Rasp.Deployment_Client_Name[kk] ="+Main_Rasp.Deployment_Client_Name[0]);
				System.out.println("-----------------------------------------------------");
				
				
				for(int kk=0; kk<Main_Rasp_Combine.Deployment_Task_Count; kk++)
				{
					//System.out.println("Main_Rasp.Deployment_Task_Details[kk] ="+Main_Rasp.Deployment_Task_Details[kk]);
					//annoucement_end_time = System.currentTimeMillis();   
					//annoucement_elapsed_time = (annoucement_end_time - annoucement_start_time)/60000;
					//System.out.println("annoucement_elapsed_time ="+annoucement_elapsed_time);
					
					
					if( Main_Rasp_Combine.Deployment_Task_Details[kk].contains(device_IP_Address[j]))
					{
						device_left_time[kk] = System.currentTimeMillis();
						if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[1]</Task>"))
						{
						red_t1_stime = System.currentTimeMillis();
						}
						if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[4]</Task>"))
						{
						red_t4_stime = System.currentTimeMillis();
						}
						
						System.out.println("-------------------------------");
						System.out.println("Deployment Device Left");
						System.out.println("-------------------------------");
						String content = "IP Address: ["+ device_IP_Address[j] + "] Device has left";
						device_status = content + " ,";
						System.out.println("Main_Rasp.Deployment_Client_Name[kk] ="+Main_Rasp_Combine.Deployment_Client_Name[kk]);
				        MqttMessage execution_message = new MqttMessage(content.getBytes());
				        execution_message.setQos(Main_Rasp_Combine.qos);
				        
				        //if(annoucement_elapsed_time >= 2)
				        //{
				        if(Main_Rasp_Combine.sampleClient_RP.isConnected())
				        {
				        	try{
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
				        	}catch (MqttException me) {
								me.printStackTrace();
							}
				        }
				        else
				        {
				        	try
							{
				        		//System.out.println("Here connecting");
								//sampleClient_RP.disconnect();
				        		Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
							}catch (MqttException me) {
								me.printStackTrace();
							}
				        }
				       // annoucement_start_time = System.currentTimeMillis();
				      //  annoucement_elapsed_time = 0;
				       // }
				        if(Main_Rasp_Combine.Deployment_Client_Name[kk].isConnected())
				        {
				        	try{
				        		System.out.println("Step 1");
				        		Main_Rasp_Combine.Deployment_Client_Name[kk].publish("Service_Execution_Peer_Node", execution_message);
				        	}catch (MqttException me) {
								me.printStackTrace();
							}
				        }
				        else
				        {
				        	try
							{
				        		System.out.println("Step 2");
								//sampleClient_RP.disconnect();
				        		Main_Rasp_Combine.Deployment_Client_Name[kk].connect(Main_Rasp_Combine.connOpts_IoT_Device_2);
				        		Main_Rasp_Combine.Deployment_Client_Name[kk].publish("Service_Execution_Peer_Node", execution_message);
							}catch (MqttException me) {
								me.printStackTrace();
							}
				        } 		
					}
				}
				
				Add_New_Device(j);
				
			}
			
		}
		}
		}
	
	
	public void get_device_status_after_2_mins()
	{

		for(int cd = 0; cd<20; cd++)
		{
		stopTime[cd] = System.currentTimeMillis();
		elapsedTime[cd] = stopTime[cd] - startTime[cd];
		}
		//elapsedTime = 60000;
		//elapsedTime_in_sec = elapsedTime/1000;
	    //System.out.println("elapsedTime ="+elapsedTime);
	   
		for(int k=0; k<current_active_device; k++)
		{
			if(residence_time_minutes[k] != 0)
			{
			residence_time_millisec[k] = residence_time_minutes[k] * 60 * 1000;
			//System.out.println("Original residence_time_millisec[k] ="+residence_time_millisec[k]);
			residence_time_millisec[k] = residence_time_millisec[k] - elapsedTime[k];
			//System.out.println("Elapsed residence_time_millisec[k] ="+residence_time_millisec[k]);
			residence_time_minutes[k] = (residence_time_millisec[k]/1000)/60;
			//System.out.println("residence_time_millisec[k] ="+residence_time_millisec[k]);
			//System.out.println("residence_time_minutes[k] ="+residence_time_minutes[k]);
			}
		}
		
		for (int i=0; i<current_active_device; i++)
		{
			if(residence_time_minutes[i] != 0)
			{
			double lambda = 0.2;
			double lambda_x =  -(residence_time_minutes[i] * lambda);
			//System.out.println("lambda_x = "+lambda_x);
			residence_prob[i] = 1 - (lambda * Math.exp(lambda_x));
			//residence_prob[i] = Math.exp(lambda_x);
			//System.out.println("residence_prob[i] = "+residence_prob[i]);
			}
		}
		
		int min = 0;
		int max = 10;
		int value = 0;
		int estimated_prob = 0;
		for(int j=0; j<current_active_device; j++)
		{
			if(residence_prob[j] != 0)
			{
			Random rand = new Random(); 
			value = rand.nextInt((max - min) + 1) + min;
			estimated_prob = (int) Math.round(residence_prob[j] * 10);
			//System.out.println("Random No. = "+value+"       Prob = "+estimated_prob);
			if(value <= estimated_prob)
			{
				System.out.println(device_name[j] + ": Active");
			}
			else
			{
				System.out.println(device_name[j] + " IP Adress: " + device_IP_Address[j] + ": Left");
				residence_time_millisec[j] = 0;
				residence_time_minutes[j] = 0;
				residence_prob[j] = 0;
				device_active = device_active - 1;
				device_left = device_left + 1;
				System.out.println("-----------------------------------------------------");
				System.out.println("Main_Rasp.Deployment_Task_Count ="+Main_Rasp_Combine.Deployment_Task_Count);
				//System.out.println("Main_Rasp.Deployment_Task_Details[kk] ="+Main_Rasp.Deployment_Task_Details[0]);
				//System.out.println("Main_Rasp.Deployment_Client_Name[kk] ="+Main_Rasp.Deployment_Client_Name[0]);
				System.out.println("-----------------------------------------------------");
				
				
				for(int kk=0; kk<Main_Rasp_Combine.Deployment_Task_Count; kk++)
				{
					//System.out.println("Main_Rasp.Deployment_Task_Details[kk] ="+Main_Rasp.Deployment_Task_Details[kk]);
					annoucement_end_time = System.currentTimeMillis();   
					annoucement_elapsed_time = (annoucement_end_time - annoucement_start_time)/60000;
					System.out.println("Before round annoucement_elapsed_time ="+(annoucement_elapsed_time%2));
					annoucement_elapsed_time = annoucement_elapsed_time % 2;
					if(annoucement_elapsed_time < 0.1)
					{
					annoucement_elapsed_time = Math.round(annoucement_elapsed_time);
					}
					System.out.println("annoucement_elapsed_time ="+annoucement_elapsed_time);
					
					
					if(Main_Rasp_Combine.Deployment_Task_Details[kk].contains(device_IP_Address[j]))
					{
						
						device_left_time[kk] = System.currentTimeMillis();
						System.out.println("-------------------------------");
						System.out.println("Deployment Device Left "+Main_Rasp_Combine.xtp.Fog_Service_Count);
						System.out.println("-------------------------------");
						String content = "IP Address: ["+ device_IP_Address[j] + "] Device has left";
						device_status = content + " ,";
						System.out.println("Main_Rasp.Deployment_Client_Name[kk] ="+Main_Rasp_Combine.Deployment_Client_Name[kk]);
				        MqttMessage execution_message = new MqttMessage(content.getBytes());
				        execution_message.setQos(Main_Rasp_Combine.qos);
				        
				        
				        if(Main_Rasp_Combine.Deployment_Client_Name[kk].isConnected())
				        {
				        	try{
				        		System.out.println("Step 1");
				        		Main_Rasp_Combine.Deployment_Client_Name[kk].publish("Service_Execution_Peer_Node", execution_message);
				      
				        	}catch (MqttException me) {
								me.printStackTrace();
							}
				        }
				        else
				        {
				        	try
							{
				        		System.out.println("Step 2");
				        		Main_Rasp_Combine.Deployment_Client_Name[kk].connect(Main_Rasp_Combine.connOpts_IoT_Device_2);
				        		Main_Rasp_Combine.Deployment_Client_Name[kk].publish("Service_Execution_Peer_Node", execution_message);
				        		
							}catch (MqttException me) {
								me.printStackTrace();
							}
				        } 
				        
				        if(Main_Rasp_Combine.sampleClient_RP.isConnected())
				        {
				        	
				        	try{
				        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[1]</Task>"))
				        		{
				        			String one = "<Task>[1]</Task> Device left";
				        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
				        		deployment_msg.setQos(Main_Rasp_Combine.qos);
				        		
				        		red_t1_stime_1 = System.currentTimeMillis();
				        		
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
				        		
				        		}
				        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[4]</Task>"))
				        		{
				        		String four = "<Task>[4]</Task> Device left";
				        		MqttMessage deployment_msg = new MqttMessage(four.getBytes());
				        		deployment_msg.setQos(Main_Rasp_Combine.qos);
				        		
				        		red_t4_stime_1 = System.currentTimeMillis();
				        		
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
				        		
				        		}
				        		
				        		
				        	}catch (MqttException me) {
								me.printStackTrace();
							}
				        }
				        else
				        {
				        	
				        	try
							{
				        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[1]</Task>"))
				        		{
				        			String one = "<Task>[1]</Task> Device left";
				        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
				        		deployment_msg.setQos(Main_Rasp_Combine.qos);
				        		
				        		red_t1_stime_1 = System.currentTimeMillis();
				        		
				        		Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
				        		
				        		}
				        		
				        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[4]</Task>"))
				        		{
				        			String four = "<Task>[4]</Task> Device left";
				        		MqttMessage deployment_msg = new MqttMessage(four.getBytes());
				        		deployment_msg.setQos(Main_Rasp_Combine.qos);
				        		
				        		red_t4_stime_1 = System.currentTimeMillis();
				        		
				        		Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", deployment_msg);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
				        		
				        		}
				        		
				        		
							}catch (MqttException me) {
								me.printStackTrace();
							}
				        }
				        
				        
				        
				        
				        if(annoucement_elapsed_time == 0 && device_status != "")
				        {
				   
				        	execution_message = new MqttMessage(device_status.getBytes());
					        execution_message.setQos(Main_Rasp_Combine.qos);
							
							
				        if(Main_Rasp_Combine.sampleClient_RP.isConnected())
				        {
				        	
				        	try{
				        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[1]</Task>"))
				        		{
				        			String one = "<Task>[1]</Task> Device left";
				        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
				        		deployment_msg.setQos(Main_Rasp_Combine.qos);
				        		
				        		red_t1_stime_1 = System.currentTimeMillis();
				        		
				        		//Main_Rasp_Local.sampleClient_RP.publish("iot_data", deployment_msg);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
				        		
				        		}
				        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[4]</Task>"))
				        		{
				        			String four = "<Task>[4]</Task> Device left";
				        		MqttMessage deployment_msg = new MqttMessage(four.getBytes());
				        		deployment_msg.setQos(Main_Rasp_Combine.qos);
				        		
				        		red_t4_stime_1 = System.currentTimeMillis();
				        		
				        		//Main_Rasp_Local.sampleClient_RP.publish("iot_data", deployment_msg);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
				        		
				        		}
				        		
				        		
				        	}catch (MqttException me) {
								me.printStackTrace();
							}
				        }
				        else
				        {
				        	
				        	try
							{
				        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[1]</Task>"))
				        		{
				        		String one = "<Task>[1]</Task> Device left";
				        		MqttMessage deployment_msg = new MqttMessage(one.getBytes());
				        		deployment_msg.setQos(Main_Rasp_Combine.qos);
				        		
				        		red_t1_stime_1 = System.currentTimeMillis();
				        		
				        		Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
				        		//Main_Rasp_Local.sampleClient_RP.publish("iot_data", deployment_msg);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
				        		
				        		}
				        		
				        		if((Main_Rasp_Combine.Deployment_Task_Details[kk]).contains("<Task>[4]</Task>"))
				        		{
				        		String four = "<Task>[4]</Task> Device left";
				        		MqttMessage deployment_msg = new MqttMessage(four.getBytes());
				        		deployment_msg.setQos(Main_Rasp_Combine.qos);
				        		
				        		red_t4_stime_1 = System.currentTimeMillis();
				        		
				        		Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
				        		//Main_Rasp_Local.sampleClient_RP.publish("iot_data", deployment_msg);
				        		Main_Rasp_Combine.sampleClient_RP.publish("iot_data", execution_message);
				        		
				        		}
				        		
				        		
							}catch (MqttException me) {
								me.printStackTrace();
							}
				        }
				        annoucement_start_time = System.currentTimeMillis();
				        annoucement_elapsed_time = 0;
				        device_status = "";
				        }
				        
				        
				        
				        String Device_IP_Address = device_IP_Address[j];
						for(int lmn=0; lmn<Main_Rasp_Combine.xtp.Fog_Service_Count;lmn++)
							{
									System.out.println("Step 1: here "+Main_Rasp_Combine.xtp.Fog_Service_IP_Address[lmn]);
									System.out.println("Device_IP_Address "+Device_IP_Address);
									if(Main_Rasp_Combine.xtp.Fog_Service_IP_Address[lmn].contains(Device_IP_Address))
								{
									System.out.println("Step 2: here");
									if(Main_Rasp_Combine.xtp.Task_Id_Track[lmn] == 1)
									{
										System.out.println("Step 3: here");
										task_1_start_time = System.currentTimeMillis();
									}
									
									if(Main_Rasp_Combine.xtp.Task_Id_Track[lmn] == 4)
									{
										task_4_device_left_flag = 1;
										task_4_device_leave_time = System.currentTimeMillis();
									}
									System.out.println("My Fog Device has left "+Main_Rasp_Combine.xtp.Task_Id_Track[lmn]);
									try{
										Main_Rasp_Combine.xtp.Alternate_Device_Search(Main_Rasp_Combine.xtp.Fog_Service_Name[lmn], Main_Rasp_Combine.xtp.Fog_Service_Availability[lmn], Main_Rasp_Combine.xtp.Fog_Service_Type[lmn], Main_Rasp_Combine.xtp.Fog_Deployable[lmn], Main_Rasp_Combine.xtp.Fog_Non_Deployable[lmn], Main_Rasp_Combine.xtp.Fog_Location_Latitude[lmn], Main_Rasp_Combine.xtp.Fog_Location_Longitude[lmn], Main_Rasp_Combine.xtp.Fog_Selected_Peer_Node[lmn], Main_Rasp_Combine.xtp.Fog_cell_id[lmn]);
									
									//System.out.println("New IP_Address =" +read_xml_file.IP_Address);
									//System.out.println("Old IP_Address =" +read_xml_file.Fog_Service_IP_Address[lmn]);
									System.out.println(Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]]);
									System.out.println(Main_Rasp_Combine.xtp.Deployment_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]]);
									Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace(Main_Rasp_Combine.xtp.Fog_Service_IP_Address[lmn], Main_Rasp_Combine.xtp.IP_Address);
									Main_Rasp_Combine.xtp.Deployment_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace(Main_Rasp_Combine.xtp.Fog_Service_IP_Address[lmn], Main_Rasp_Combine.xtp.IP_Address);
									Main_Rasp_Combine.Deployment_Task_Details[kk] = Main_Rasp_Combine.xtp.Deployment_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]];
									Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.IP_Address;
									
									
									System.out.println("Old Here Assignment= "+Main_Rasp_Combine.xtp.Fog_Service_IP_Address[lmn]);
									System.out.println("New Here Assignment= "+Main_Rasp_Combine.xtp.IP_Address);
									Main_Rasp_Combine.xtp.Fog_Service_IP_Address[lmn] = Main_Rasp_Combine.xtp.IP_Address;
									
									//here
									if(Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] != null && Main_Rasp_Combine.xtp.Task_Id_Track[lmn] == 1)
									{
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace(Main_Rasp_Combine.xtp.IP_Address, "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.38", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.39", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.40", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.41", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.42", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.43", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.44", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.45", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.46", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.47", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.48", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.49", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.50", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.51", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.52", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.53", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.54", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.55", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.56", "10.104.72.137");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.57", "10.104.72.137");
									}
									
									if(Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] != null && Main_Rasp_Combine.xtp.Task_Id_Track[lmn] == 4)
									{
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace(Main_Rasp_Combine.xtp.IP_Address, "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.38", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.39", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.40", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.41", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.42", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.43", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.44", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.45", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.46", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.47", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.48", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.49", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.50", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.51", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.52", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.53", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.54", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.55", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.56", "10.103.72.45");
										Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.57", "10.103.72.45");
										
									}
									
									if(Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] != null && Main_Rasp_Combine.xtp.Task_Id_Track[lmn] == 1)
									 {
									 //System.out.println("Deployment IP Address: "+read_xml_file.Deployment_IP_Address[read_xml_file.Task_Id_Track[lmn]]);
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace(Main_Rasp_Combine.xtp.IP_Address, "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.38", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.39", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.40", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.41", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.42", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.43", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.44", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.45", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.46", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.47", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.48", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.49", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.50", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.51", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.52", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.53", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.54", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.55", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.56", "10.104.72.137");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.57", "10.104.72.137");
									 //System.out.println("Deployment IP Address: "+read_xml_file.Deployment_IP_Address[read_xml_file.Task_Id_Track[lmn]]);
									 }
									
									if(Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] != null && Main_Rasp_Combine.xtp.Task_Id_Track[lmn] == 4)
									 {
									 //System.out.println("Deployment IP Address: "+read_xml_file.Deployment_IP_Address[read_xml_file.Task_Id_Track[lmn]]);
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace(Main_Rasp_Combine.xtp.IP_Address, "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.38", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.39", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.40", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.41", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.42", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.43", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.44", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.45", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.46", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.47", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.48", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.49", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.50", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.51", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.52", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.53", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.54", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.55", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.56", "10.103.72.45");
										Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]] = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]].replace("10.103.72.57", "10.103.72.45");
									 //System.out.println("Deployment IP Address: "+read_xml_file.Deployment_IP_Address[read_xml_file.Task_Id_Track[lmn]]);
									 }
									 
									//here
								    try
								    {
								    	if(Main_Rasp_Combine.xtp.Task_Id_Track[lmn] == 1)
								    	{
								    		uploading_start_time_1 = System.currentTimeMillis();
								    	}
								    	if(Main_Rasp_Combine.xtp.Task_Id_Track[lmn] == 4)
								    	{
								    		uploading_start_time_4 = System.currentTimeMillis();
								    	}
									int no_of_uploads = Main_Rasp_Combine.xtp.SourceFilesData.get((Main_Rasp_Combine.xtp.Task_Id_Track[lmn]-1)).size();
									System.out.println("no_of_uploads="+no_of_uploads);
									for(int i=1; i<=no_of_uploads; i++)
									{
										String source_variable_name = "Source_File_Address_"+i;
										String dest_variable_name = "Destination_File_Address_"+i;
										
										if(Main_Rasp_Combine.xtp.SourceFilesData.get((Main_Rasp_Combine.xtp.Task_Id_Track[lmn]-1)).get(((source_variable_name+"_"+(Main_Rasp_Combine.xtp.Task_Id_Track[lmn])))) != null && Main_Rasp_Combine.xtp.DestinationFilesData.get((Main_Rasp_Combine.xtp.Task_Id_Track[lmn]-1)).get((dest_variable_name+"_"+(Main_Rasp_Combine.xtp.Task_Id_Track[lmn]))) != null)
										{
										System.out.println(Main_Rasp_Combine.xtp.SourceFilesData.get((Main_Rasp_Combine.xtp.Task_Id_Track[lmn]-1)).get(((source_variable_name+"_"+(Main_Rasp_Combine.xtp.Task_Id_Track[lmn])))));
										System.out.println(Main_Rasp_Combine.xtp.DestinationFilesData.get((Main_Rasp_Combine.xtp.Task_Id_Track[lmn]-1)).get((dest_variable_name+"_"+(Main_Rasp_Combine.xtp.Task_Id_Track[lmn]))));
										String HOST_Address = Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]];
										HOST_Address = HOST_Address.replace(":1883", "");
										Main_Rasp_Combine.file_upload(Main_Rasp_Combine.xtp.SourceFilesData.get((Main_Rasp_Combine.xtp.Task_Id_Track[lmn]-1)).get(((source_variable_name+"_"+(Main_Rasp_Combine.xtp.Task_Id_Track[lmn])))),Main_Rasp_Combine.xtp.DestinationFilesData.get((Main_Rasp_Combine.xtp.Task_Id_Track[lmn]-1)).get((dest_variable_name+"_"+(Main_Rasp_Combine.xtp.Task_Id_Track[lmn]))), Main_Rasp_Combine.xtp.Deployment_IP_Address[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]]);
										
										}
									}
									
									if(Main_Rasp_Combine.xtp.Task_Id_Track[lmn] == 1)
							    	{
							    		uploading_end_time_1 = System.currentTimeMillis();
							    		uploading_time_1 = (uploading_end_time_1 - uploading_start_time_1)/1000;
							    		System.out.println("*********uploading_time_1********* ="+uploading_time_1);
							    		uploading_end_time_1 = 0;
							    		uploading_start_time_1 = 0;
							    		
							    	}
							    	if(Main_Rasp_Combine.xtp.Task_Id_Track[lmn] == 4)
							    	{
							    		uploading_end_time_4 = System.currentTimeMillis();
							    		uploading_time_4 = (uploading_end_time_4 - uploading_start_time_4)/1000;
							    		System.out.println("*********uploading_time_4********* ="+uploading_time_4);
							    		uploading_end_time_4 = 0;
							    		uploading_start_time_4 = 0;
							    		uploading_time_4 = 0;
							    	}

									//Now Publishing Services
									
									String xmlStr = Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]];
									Document doc = Main_Rasp_Combine.convertStringToDocument(xmlStr);
									doc.getDocumentElement().normalize();
									System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
									
									/*NodeList nServiceList = doc.getElementsByTagName("Services"); 

									for(int temp = 0 ; temp <nServiceList.getLength(); temp++){ 
									Node nNode = nServiceList.item(temp); 
									Element eElement = (Element) nNode; 
									NodeList childList = eElement.getChildNodes(); 
									String [] sService = new String[childList.getLength()] ; 
									for(int i = 0; i < childList.getLength(); i++){ 
									Node childNode = childList.item(i); 
								
									sService[i] = childNode.getNodeName(); 
									 
									} 
									} */
										
									//System.out.println("here");
									
									//file_upload("E:/Research/Edge Computing/runnable/deployment/Aloha.jpg","/home/pi/Desktop/commands/runnable/rasp_broker/Aloha.jpg");
									//file_upload("E:/Research/Edge Computing/runnable/deployment/config.txt","/home/pi/Desktop/commands/runnable/rasp_broker/config.txt");
									//file_upload("E:/Research/Edge Computing/runnable/deployment/output.kml","/home/pi/Desktop/commands/runnable/rasp_broker/output.kml");
									
									
									String xmlStr_task= Main_Rasp_Combine.xtp.Communcation_Message[Main_Rasp_Combine.xtp.Task_Id_Track[lmn]];
									
									System.out.println("xmlStr_task =\n"+xmlStr_task);
									Document doc_task = Main_Rasp_Combine.convertStringToDocument(xmlStr_task); 
									Main_Rasp_Combine.parseXML(doc_task);
									
									String redeployment_content = "";
									if(xmlStr_task.contains("<Task>[1]</Task>"))
									{
									redeployment_content = "Redeployment Done [1] ["+uploading_time_1+"]";
									red_t1_etime = System.currentTimeMillis();
									t1_red_time = (red_t1_etime - red_t1_stime_1)/1000;
									System.out.println("-----task_1_redeployment_time------- = "+t1_red_time);
									uploading_time_1 = 0;
									red_t1_etime = 0;
									red_t1_stime_1 = 0;
									}
									if(xmlStr_task.contains("<Task>[4]</Task>"))
									{
									redeployment_content = "Redeployment Done [4] ["+uploading_time_4+"]";
									red_t4_etime = System.currentTimeMillis();
									t4_red_time = (red_t4_etime - red_t4_stime_1)/1000;
									System.out.println("-----task_4_redeployment_time------- = "+t4_red_time);
									uploading_time_4 = 0;
									red_t4_etime = 0;
									red_t4_stime_1 = 0;
									}
									MqttMessage redeployment_message = new MqttMessage(redeployment_content.getBytes());
									redeployment_message.setQos(Main_Rasp_Combine.qos);
									if(Main_Rasp_Combine.sampleClient_RP.isConnected())
									{
										Main_Rasp_Combine.sampleClient_RP.publish("iot_data", redeployment_message);
									}
									else
									{
										Main_Rasp_Combine.sampleClient_RP.connect(Main_Rasp_Combine.connOpts);
										Main_Rasp_Combine.sampleClient_RP.publish("iot_data", redeployment_message);
									}
									
									
									/*String New_Device_IP_Address = 	"New IP_Address =" +xml_task_parser.IP_Address;
									MqttMessage New_Device_IP_Address_message = new MqttMessage(New_Device_IP_Address.getBytes());
									New_Device_IP_Address_message.setQos(Main_Rasp_Local.qos);
									
									String content = "Resume Execution";
									MqttMessage resume_message = new MqttMessage(content.getBytes());
									resume_message.setQos(Main_Rasp_Local.qos);
									System.out.println("Communication Message= "+xml_task_parser.Communcation_Message[xml_task_parser.Task_Id_Track[lmn]]);
									MqttMessage communication_message = new MqttMessage(xml_task_parser.Communcation_Message[xml_task_parser.Task_Id_Track[lmn]].getBytes());
									MqttMessage deployment_message = new MqttMessage(xml_task_parser.Deployment_Message[xml_task_parser.Task_Id_Track[lmn]].getBytes());
									communication_message.setQos(Main_Rasp_Local.qos);
									deployment_message.setQos(Main_Rasp_Local.qos);
									
									if(xml_task_parser.Task_Id_Track[lmn] == 1) {
									device_no = 1;
									}
									else
									{device_no = 2;}
									
									if(device_no == 1)
									{
									if(Main_Rasp_Local.sampleClient_RP.isConnected())
									{
									System.out.println("Here 1 IF");
									Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", New_Device_IP_Address_message);
									Thread.sleep(100);
									Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", deployment_message);
									Thread.sleep(100);
									Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", communication_message);
									Thread.sleep(100);
									Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", resume_message);
									
									}
									else
									{
										try
										{
											//sampleClient_RP.disconnect();
											System.out.println("Here 1 ELSE");
											Main_Rasp_Local.sampleClient_RP.connect(Main_Rasp_Local.connOpts_RP);
											Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", New_Device_IP_Address_message);
											Thread.sleep(100);
											Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", deployment_message);
											Thread.sleep(100);
											Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", communication_message);
											
											Thread.sleep(100);
											Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", resume_message);
											
											
										}catch (MqttException me) {
											me.printStackTrace();
										}
									}
									
								}
									else
									{
										System.out.println("Here Here Here Here Here");
										if(Main_Rasp_Local.sampleClient_RP2.isConnected())
										{
										System.out.println("Here 2 IF");
										Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", New_Device_IP_Address_message);
										Thread.sleep(100);
										Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", deployment_message);
										Thread.sleep(100);
										Main_Rasp_Local.sampleClient_RP2.publish("service_execution_RP1", communication_message);
										Thread.sleep(100);
										Main_Rasp_Local.sampleClient_RP2.publish("service_execution_RP1", resume_message);
										}
										else
										{
											try
											{
												//sampleClient_RP.disconnect();
												System.out.println("Here 2 ELSE");
												Main_Rasp_Local.sampleClient_RP2.connect(Main_Rasp_Local.connOpts_RP2);
												Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", New_Device_IP_Address_message);
												Thread.sleep(100);
												Main_Rasp_Local.sampleClient_RP.publish("service_execution_RP1", deployment_message);
												Thread.sleep(100);
												Main_Rasp_Local.sampleClient_RP2.publish("service_execution_RP1", communication_message);
												Thread.sleep(100);
												Main_Rasp_Local.sampleClient_RP2.publish("service_execution_RP1", resume_message);
												
												
											}catch (MqttException me) {
												me.printStackTrace();
											}
										}
									} */
									
									//Exe_Flag = Exe_Flag + 1;
									
									
								}
								
								catch (Exception e) {
									e.printStackTrace();
							    }
									
				
									
											
									}catch (Exception e) {
										e.printStackTrace();
								    }	
								}
							}
				        
					}
					
				
					
					
				}
				
				Add_New_Device(j);
				
			}
			
		}
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
	
	/*public static void main(String[] args)
	{
		Mobility m1 = new Mobility();
		m1.startTime = System.currentTimeMillis();
		//try {Thread.sleep(50000);
	}*/
	
	
}
