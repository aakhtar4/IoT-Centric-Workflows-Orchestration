package registry_info;

import java.sql.*;
import java.util.ArrayList;

import connection.DBConnectionFactory;
import entities.PeerNode;

public class PeerNodes_DAL {
	public int getPeerNodeCount()
	{
		int count = -1;
		
		Connection con = null;
		DBConnectionFactory connectionFactory = new DBConnectionFactory();
		con = connectionFactory.getConnection();
		
		try 
		{
			con.setAutoCommit(false);
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from `peer_nodes`;");  
			
			rs.last();
			count = rs.getRow();
		}
		catch (Exception e) {
			System.out.println("Failed to select devices fromDB. "+
					"****** EXCEPTION: " + e.toString());
			e.printStackTrace();
		}
		finally {
			connectionFactory.close(con);
		}
		
		return count;
	}
	
	public ArrayList<PeerNode> fetchAllPeerNodes()
	{
		ArrayList<PeerNode> peer_nodes = new ArrayList<PeerNode>();
		
		Connection con = null;
		DBConnectionFactory connectionFactory = new DBConnectionFactory();
		con = connectionFactory.getConnection();
		
		try 
		{
			con.setAutoCommit(false);
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from `peer_nodes`;");  
			while(rs.next())
			{
				PeerNode p = new PeerNode();
				
				p.id = rs.getInt("id");
				p.Node_Name = rs.getString("Node_Name");
				p.Longitude = rs.getDouble("Longitude");
				p.Latitude = rs.getDouble("Latitude");
				p.Processing_Capability = rs.getString("Processing_Capability");
				p.Available_Computational_Resources = rs.getString("Available_Computational_Resources");
				p.Available_Storage_Space = rs.getString("Available_Storage_Space");
				p.Battery_Life = rs.getInt("Battery_Life");	
				p.Availability_Duration = rs.getInt("Availability_Duration");
				p.Response_Time = rs.getDouble("Response_Time");
				p.Cell = rs.getInt("Cell");
				p.IP_Address = rs.getString("IP_Address");
				p.mqtt_port = rs.getString("mqtt_port");
								
				peer_nodes.add(p);
			}
		}catch (Exception e) {
			
			System.out.println("Failed to select devices fromDB. "+
					"****** EXCEPTION: " + e.toString());
			e.printStackTrace();
		}finally {
			connectionFactory.close(con);
		}
		
		return peer_nodes;
	}
	
	public PeerNode fetchPeerNodeByID(int id)
	{
		PeerNode peer_node = null;
		Connection con = null;
		DBConnectionFactory connectionFactory = new DBConnectionFactory();
		con = connectionFactory.getConnection();
		
		try 
		{
			con.setAutoCommit(false);
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from `peer_nodes` where id = " + id + ";");  

			if(rs.next())
			{
				peer_node = new PeerNode();
				
				peer_node.id = rs.getInt("id");
				peer_node.Node_Name = rs.getString("Node_Name");
				peer_node.Longitude = rs.getDouble("Longitude");
				peer_node.Latitude = rs.getDouble("Latitude");
				peer_node.Processing_Capability = rs.getString("Processing_Capability");
				peer_node.Available_Computational_Resources = rs.getString("Available_Computational_Resources");
				peer_node.Available_Storage_Space = rs.getString("Available_Storage_Space");
				peer_node.Battery_Life = rs.getInt("Battery_Life");	
				peer_node.Availability_Duration = rs.getInt("Availability_Duration");
				peer_node.Response_Time = rs.getDouble("Response_Time");
				peer_node.Cell = rs.getInt("Cell");
				peer_node.IP_Address = rs.getString("IP_Address");
				peer_node.mqtt_port = rs.getString("mqtt_port");
			}
		}catch (Exception e) {
			
			System.out.println("Failed to select devices fromDB. "+
					"****** EXCEPTION: " + e.toString());
			e.printStackTrace();
		}finally {
			connectionFactory.close(con);
		}
		
		return peer_node;
	}
}
