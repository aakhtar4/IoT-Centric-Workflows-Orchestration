package connection;

import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import entities.PeerNode;

public class MQTTConnectionFactory {
	public static HashMap<String, MqttClient> realDeviceClientsByIPs = null;
	
	public static MqttConnectOptions getConnectionOptions()
	{
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connOpts.setCleanSession(true);
		connOpts.setKeepAliveInterval(1000);
		connOpts.setAutomaticReconnect(true);
		
		return connOpts;
	}
	
	public static MqttClient getMQTTClient(PeerNode mapped_peer_node, int task_in_execution, int qos, int[] Task_Execution_Count)
	{
		if(realDeviceClientsByIPs == null)
		{
			realDeviceClientsByIPs =  new HashMap<>();
		}
		
		if(realDeviceClientsByIPs.containsKey(mapped_peer_node.IP_Address))
		{
			return realDeviceClientsByIPs.get(mapped_peer_node.IP_Address);
		}
		else
		{
			String broker_Mqtt_RP = "tcp://" + mapped_peer_node.IP_Address + ":" + mapped_peer_node.mqtt_port;
			String clientID_Mqtt_RP = "Main_RP" + mapped_peer_node.id + broker_Mqtt_RP;
			
			MqttClient mqttClient = null;
			
			try {
				mqttClient = new MqttClient(broker_Mqtt_RP, clientID_Mqtt_RP, new MemoryPersistence());
				mqttClient.connect(MQTTConnectionFactory.getConnectionOptions());
				System.out.println("Broker connected to Raspberry Pi " + clientID_Mqtt_RP);
				mqttClient.subscribe("iot_data", qos);
				System.out.println("Broker subscribed to topic iot_data");
			} catch (MqttException e) {
				e.printStackTrace();
			}
	
			mqttClient.setCallback(new MqttCallback() {
				public void connectionLost(Throwable throwable) {
					System.out.println("Connection to MQTT broker lost!");
				}
	
				public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
					System.out.println("-------------------------------------------------");
					System.out.println("| Received from Raspberry Pi " + clientID_Mqtt_RP);
					System.out.println("| Topic: " + topic);
					System.out.println("| Message: " + new String(mqttMessage.getPayload()));
					System.out.println("| QoS: {" + mqttMessage.getQos() + "}");
					System.out.println("-------------------------------------------------");
					String content = new String(mqttMessage.getPayload());
					if (content.contains("<kml")) {
						System.out.println("task_in_execution =" + task_in_execution);
						Task_Execution_Count[task_in_execution] = Task_Execution_Count[task_in_execution] + 1;
					}
	
					if (content.contains("Traffic Flow")) {
						System.out.println("task_in_execution =" + task_in_execution);
						Task_Execution_Count[task_in_execution] = Task_Execution_Count[task_in_execution] + 1;
					}
				}
	
				public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
					System.out.println("Delivery Complete");
				}
			});
			
			
			realDeviceClientsByIPs.put(mapped_peer_node.IP_Address, mqttClient);			
			
			return mqttClient;
		}
	}
}