package entities;

import java.util.ArrayList;

public class Task {
	public int id;
	public ArrayList<Service> services;
	public PeerNode mapped_peer_node;
	
	public Task()
	{
		id = -1;
		services = new ArrayList<Service>();
		mapped_peer_node = null;
	}
}
