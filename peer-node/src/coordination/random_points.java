package coordination;

public class random_points implements Runnable {

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
	public long startTime[] = new long[20];
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
	random_points(){
		
		device_name = new String[]{"Device_1", "Device_2", "Device_3", "Device_4", "Device_5", "Device_6", "Device_7", "Device_8", "Device_9", "Device_10", "Device_11", "Device_12", "Device_13", "Device_14", "Device_15", "Device_16", "Device_17", "Device_18", "Device_19", "Device_20"};
		device_IP_Address = new String[]{"10.103.72.38", "10.103.72.39", "10.103.72.40", "10.103.72.41", "10.103.72.42", "10.103.72.43", "10.103.72.44", "10.103.72.45", "10.103.72.46", "10.103.72.47", "10.103.72.48",  "10.103.72.49","10.103.72.50",  "10.103.72.51", "10.103.72.52", "10.103.72.53", "10.103.72.54", "10.103.72.55", "10.103.72.56", "10.103.72.57"};
		residence_time_minutes = new double[]{7, 7, 10, 10, 10, 9, 12, 5, 15, 6, 12, 6, 12, 10, 12, 7, 15, 10, 7, 14};
		residence_time_assigned = new double[]{7, 7, 10, 10, 10, 9, 12, 5, 15, 6, 12, 6, 12, 10, 12, 7, 15, 10, 7, 14};
		
	
		t = new Thread(this);
		System.out.println("New thread: " + t);
		t.start();
		}
	   public void run() {
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
			double current_time = System.currentTimeMillis();
			double diff = (current_time - device_last_update_time[j]);
			System.out.println("diff ="+diff);
		}
	}

	public static void main(String[] args)
	{
		random_points m1 = new random_points();
	}
}
