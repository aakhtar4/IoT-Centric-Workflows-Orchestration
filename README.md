# IoT-Centric-Workflows-Orchestration
Orchestration and Management of Adaptive IoT-centric Distributed Applications paper implementation

This repository includes the following four components:
1.	Workflow Authoring Interface is a web-based component developed in HTML5 and JavaScript which includes a graphical user interface that allows the user to specify workflow requirements in terms of tasks and their interactions. This component also includes an interface for registering cloud services, edge nodes, IoT devices, and services that can be deployed.

2.	Workflow Coordinator Component provides implementation of the global and local workflow orchestration and management algorithms for resource selection, binding, and messaging with peer nodes and end devices. 

3.	Peer Node Component provides implementation of the proposed algorithms for global and local orchestration and management at peer devices. In addition, it includes the mechanism for managing the overlay network of peer nodes and messaging between the different devices using Apache Paho and Moquette. 

4.	End Device Component is installed on IoT devices to communicate with the workflow coordinator and peer nodes.

All these components are provided as executable JAR files for deployment on devices equipped with Java Runtime Environment (JRE) and MySQL server.

The executable files can be found in `execuateable-jars` directory.

The database script can be found in `db_script` directory.

Instructions on how to run the Workflow Authoring Interface can be found in the Readme.md file of the `workflow-authoring-interface` directory.

Instructions on how to run the IoT-Centric-Workflows-Orchestration system can be found in the Readme.md file of the `workflow-coordinator` directory.