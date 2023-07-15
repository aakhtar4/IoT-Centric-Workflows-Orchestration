package entities;

public class TaskInfo {
	public double time[];
	public int count;
	public double elapsed_time[];
	public double start_time;
	public double end_time;
	public double redeployment_time;
	
	public TaskInfo()
	{
		time = new double[100];
		count = 0;
		elapsed_time = new double[100];
		start_time = 0;
		end_time = 0;
		redeployment_time = 0;
	}
}
