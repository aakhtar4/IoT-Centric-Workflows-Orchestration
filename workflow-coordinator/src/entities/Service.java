package entities;

public class Service {
	public int id;
	public String service_name;
	public String uri;
	public String servie_category;
	public String service_type;
	public String resource_type;
	public Location location;
	public double radius;
	public String hardware_requirements;
	public String software_requirements;
	public String packages_list;
	public int required_availability_duration;
	public String deployment_status;
	public boolean deployable;
	public Device mapped_end_device;
	
	public Service()
	{
		location = null;
		mapped_end_device = null;
	}
}
