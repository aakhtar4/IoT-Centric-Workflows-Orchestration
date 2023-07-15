package registry_info;

import java.sql.*;
import java.util.ArrayList;

import connection.DBConnectionFactory;
import entities.Device;

public class Devices_DAL {
	public ArrayList<Device> fetchAllDevices()
	{
		ArrayList<Device> devices = new ArrayList<Device>();
		
		Connection con = null;
		DBConnectionFactory connectionFactory = new DBConnectionFactory();
		con = connectionFactory.getConnection();
		
		try 
		{
			con.setAutoCommit(false);
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from `devices`;");  
			while(rs.next())
			{
				Device device = new Device();
				
				device.id = rs.getInt("id");
				device.Device_Name = rs.getString("Device_Name");
				device.Longitude = rs.getDouble("Longitude");
				device.Latitude = rs.getDouble("Latitude");
				device.Processing_Capability = rs.getString("Processing_Capability");
				device.Available_Computational_Resources = rs.getString("Available_Computational_Resources");
				device.Available_Storage_Space = rs.getDouble("Available_Storage_Space");
				device.Battery_Life = rs.getInt("Battery_Life");	
				device.Availability_Duration = rs.getInt("Availability_Duration");
				device.Sampling_Rate = rs.getInt("Sampling_Rate");
				device.Response_Time = rs.getDouble("Response_Time");
				device.peer_node_id = rs.getInt("peer_node_id");
				device.cell_id = rs.getInt("cell_id");
				device.IP_Address = rs.getString("IP_Address");
				device.sftp_username = rs.getString("sftp_username");
				device.sftp_password = rs.getString("sftp_password");
				device.mqtt_port = rs.getString("mqtt_port");
								
				devices.add(device);
			}
		}catch (Exception e) {
			
			System.out.println("Failed to select devices fromDB. "+
					"****** EXCEPTION: " + e.toString());
			e.printStackTrace();
		}finally {
			connectionFactory.close(con);
		}
		
		return devices;
	}
	
	public Device fetchDeviceByID(int id)
	{
		Device device = null;
		Connection con = null;
		DBConnectionFactory connectionFactory = new DBConnectionFactory();
		con = connectionFactory.getConnection();
		
		try 
		{
			con.setAutoCommit(false);
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from `devices` where id = " + id + ";");  
			
			if(rs.next())
			{
				device = new Device();
				
				device.id = rs.getInt("id");
				device.Device_Name = rs.getString("Device_Name");
				device.Longitude = rs.getDouble("Longitude");
				device.Latitude = rs.getDouble("Latitude");
				device.Processing_Capability = rs.getString("Processing_Capability");
				device.Available_Computational_Resources = rs.getString("Available_Computational_Resources");
				device.Available_Storage_Space = rs.getDouble("Available_Storage_Space");
				device.Battery_Life = rs.getInt("Battery_Life");	
				device.Availability_Duration = rs.getInt("Availability_Duration");
				device.Sampling_Rate = rs.getInt("Sampling_Rate");
				device.Response_Time = rs.getDouble("Response_Time");
				device.peer_node_id = rs.getInt("peer_node_id");
				device.cell_id = rs.getInt("cell_id");
				device.IP_Address = rs.getString("IP_Address");
				device.sftp_username = rs.getString("sftp_username");
				device.sftp_password = rs.getString("sftp_password");
				device.mqtt_port = rs.getString("mqtt_port");
			}
		}catch (Exception e) {
			
			System.out.println("Failed to select devices fromDB. "+
					"****** EXCEPTION: " + e.toString());
			e.printStackTrace();
		}finally {
			connectionFactory.close(con);
		}
		
		return device;
	}
}
