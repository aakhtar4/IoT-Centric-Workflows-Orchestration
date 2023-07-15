**Orchestration and Management of Adaptive IoT-centric Distributed Applications**

**Workflow Authoring Component**
# **Project Setup instructions** 
**Step 1 (Optional):** Download and setup the Xampp/Wampp webserver for project deployment on local machine. (Download link: <https://www.apachefriends.org/download.html>)

**Step 2:** Copy the “demoWorkflowFinal” folder and place it in the “htdocs” directory inside the xampp e.g., C:\xampp\htdocs 

**Step 3:** Open the web browser and type in the following URL: <http://localhost/demoWorkflowFinal/index.php>

Note: replace localhost with server IP if you have deployed the project on a remote server.
# **Example Workflow**
As an illustrative example, consider a real-time incident management workflow of a Supply Chain Monitoring Company that collects, integrates, and analyzes real-time data from different data sources and IoT devices (e.g., sensors - environmental, traffic, and surveillance cameras, etc.). This incident management workflow depicted in Figure 1 is instantiated dynamically in response to a highway accident in which a tractor-trailer carrying several drums of liquid acetone overturned and exploded generating a smoke plume that spreads over a large area. This workflow provides users with a real-time picture of their in-transit cargo and notifies them of risks and disruptions due to ongoing/developing disasters along the routes. It also reroutes cargo trucks to avoid delays and risks due to ongoing/developing disasters.  

![](instruction_images/1.png)

*Figure 1.* Incident Management Workflow (Illustrative Example)

Below we provide a step-by-step illustration for designing this workflow using the workflow authoring component’s Web Interface.
# **Creating a new workflow model**
1\) Open the webpage “<http://localhost/demoWorkflowFinal/Home.php>”

2\) Click the ‘Create New’ option from the dialogue popup\.

![](instruction_images/2.png)

**3) Choose Categories, Sub-Categories, and Services from the main dropdown lists. Add ‘Cargo**

**Truck Location’ service from main dropdown lists:**

a) Choose the ‘Fog Service’ Category.

b) Choose the ‘Fog Service Device 01’ Sub-Category.

c) Choose the ‘Cargo Truck Location’ Service.

d) Click the ‘Add’ button.

![](instruction_images/3.png)

**4) Click the ‘Add Block’ button to add multiple services before, after, or after an**

**existing service. To add a block of services before the ‘Cargo Truck Location’ service:**

a) Choose Fog Service Category, Fog Service Device 01 sub-category, and ‘Cargo

Truck’ service from the dropdown lists.

b) Choose ‘before’ from the ‘before/after’ dropdown list.

c) Choose ‘Cargo Truck Location’ from the ‘Existing Service’ dropdown list.

d) Click the ‘Add to Block’ button.

e) Click the ‘Finish’ button.

![](instruction_images/4.png)

**5) Click on the ‘Add Block’ button to add a block of services before and after ‘Cargo Truck**

**Location’ service:**

a) If not selected, Choose Fog Service Category, Fog Service Device 01 subcategory and ‘Situation Assessment’ service from the dropdown lists.

b) Choose ‘before and after’ from the ‘before/after’ dropdown list.

c) Choose ‘Cargo Truck Location’ from the ‘Existing Service’ dropdown list.

d) Click the ‘Add to Block’ button.

e) Click the ‘Finish’ button.

**6) Click the ‘Copy Service’ button to copy the existing service. Click the ‘Copy Service’ button to copy the ‘Situation Assessment’ service:**

a) Choose ‘Situation Assessment’ from the ‘Copy Service’ dropdown list.

b) Choose ‘after’ from the ‘before/after’ dropdown list.

c) Choose ‘Situation Assessment {r}’ from the existing service dropdown list.

d) Click the ‘Copy’ button.

**7) Click on the ‘Add Block’ button to add a block of services before and after the ‘Situation**

**Assessment service:**

a) If not already selected, Choose Fog Service Category, Fog Service Device 01 subcategory, and ‘Get Incident Information’ service from the dropdown lists.

b) Choose ‘before and after’ from the ‘before/after’ dropdown list.

c) Choose ‘Situation Assessment\_{c}’ from ‘Existing Service’ dropdown list.

d) Click the ‘Add to Block’ button.

e) Click the ‘Finish’ button.

**8) Click on the ‘Add Block’ button to add a block of services after the ‘Situation Assessment’ service:**

a) If not selected, Choose Fog Service Category, Fog Service Device 01 subcategory, and ‘Re-route Cargo Trucks’ service from the dropdown lists.

b) Choose ‘after’ from the ‘before/after’ dropdown list.

c) Choose ‘Situation Assessment\_{c}’ from ‘Existing Service’ dropdown list.

d) Click the ‘Add to Block’ button.

e) Click the ‘Finish’ button.

**9) Click on the ‘Add Block’ button to add a block of services before ‘Situation Assessment**

**service:**

a) Choose Cloud Service Category, Cloud Service Device 01 sub-category, and ‘Plume

Service’ from the dropdown lists.

b) Choose ‘before’ from the ‘before/after’ dropdown list.

c) Choose ‘Situation Assessment\_{c}’ from ‘Existing Service’ dropdown list.

d) Click the ‘Add to Block’ button.

e) Click the ‘Finish’ button.

**10) Add ‘Data Buffering and Integration’ service from main dropdown lists:**

a) Choose the ‘Fog Service’ Category.

b) Choose the ‘Fog Service Device 01’ Sub-Category.

c) Choose the ‘Data Buffering and Integration’ Service.

d) Click the ‘Add’ button.

**11) Click the ‘Copy Service’ button to copy ‘Plume Service’:**

a) Choose ‘Plume Service’ from the ‘Copy Service’ dropdown list.

b) Choose ‘after’ from the ‘before/after’ dropdown list.

c) Choose ‘Data Buffering and Integration’ from the existing service dropdown list.

d) Click the ‘Copy’ button.

**12) Click on the ‘Add Block’ button to add a block of services before ‘Data Buffering and**

**Integration service:**

a) Choose Fog Service Category, Fog Service Device 01 sub-category, and ‘Office Data.’

service from the dropdown lists.

b) Choose ‘before’ from the ‘before/after’ dropdown list.

c) Choose ‘Data Buffering and Integration’ from the ‘Existing Service’ dropdown list.

d) Click the ‘Add to Block’ button.

e) Click the ‘Finish’ button.

**13) Click the ‘Copy Service’ button to copy the ‘Get Incident Information’ service:**

a) Choose ‘Get Incident Information’ from the ‘Copy Service’ dropdown list.

b) Choose ‘after’ from the ‘before/after’ dropdown list.

c) Choose ‘Plume Service\_{c}’ from existing service dropdown list.

d) Click the ‘Copy’ button.

**14) Click on the ‘Add Block’ button to add a block of services before ‘Get Incident Information\_{c}’**

**service:**

a) Choose Fog Service Category, Fog Service Device 01 sub-category, and ‘Chemical

Plant Fire Incident’ service from the dropdown lists.

b) Choose ‘before’ from the ‘before/after’ dropdown list.

c) Choose ‘Get Incident Information\_{c}’ from ‘Existing Service’ dropdown list.

d) Click the ‘Add to Block’ button.

e) Click the ‘Finish’ button.

**15) Add ‘Vehicle Location’ service from main dropdown lists:**

a) If not selected, Choose the ‘Fog Service’ Category.

b) Choose the ‘Fog Service Device 01’ Sub-Category.

c) Choose ‘Vehicle Location’ Service.

d) Click the ‘Add’ button.
# **Visualizing an Existing Model**
1\) Open Home\.php webpage (<http://localhost/demoWorkflowFinal/Home.php>)

2\) Click the ‘Upload Existing’ option from the popup window

3\) Choose a previously created model such as <file\_name>\.json

4\) Click open to visualize the workflow model on the webpage\. 
# **Service Registration** 
To register services through the admin panel, follow these steps:
## **Add Category**
1. Choose the 'Add New Category' option from the Category dropdown list.
1. Enter the name of the new category into the input box.
1. Click the 'Add New Category button.
## **Add Sub-Category**
1. Select a Category From the Category dropdown list (e.g., 'Fog Service').
1. choose the 'Add New Sub-Category' option from the Sub-Category dropdown list.
1. Enter the name of the new sub-category into the input box.
1. Click the 'Add New Sub-Category' button.
## **Add and Register Service**
1. Select a Category From the Category dropdown list (e.g., 'Fog Service').
1. select a sub-category (e.g., 'Fog Service Device 02') from the Sub-Category dropdown list.

![](instruction_images/5.png)

1\. Choose the 'Add New Service' option from the Service dropdown list.\
2\. Enter the name of the new service into the input box.\
3\. Click the 'Add New Category button.\
4\. A popup will appear to specify the details of the new service.\
5\. Enter all the necessary specifications.\
6\. Click the 'Submit' button.


**Set Specifications**

1\. Hover the cursor over the service box for which you want to set specifications, such as the 'Cargo Trucks' service. 

a) On the top right corner of the box, a white button will appear. Click that button. 

b) A popup will appear specifically for setting specifications. Enter all the required specifications for the 'Cargo Trucks' service. 

c) Click the 'Submit' button.

2\. Repeat the same process to enter specifications for all the remaining services. Please note that projected services ({c} /{r}) do not require specifications.

**Delete Service**

**1. Hover the cursor over service to set its specifications.** 

a) Hover cursor over ‘Vehicle Location’ service box. 

b) A red button appears on the top right corner of the box. Click that button to delete the service.

**Save Model**

1\.  Click ‘Save Model’ button 

2\. A popup appears for file name. Enter file name for example ‘model’. 

3\. Click the ‘OK’ button

**Set Interactions & Create Workflow XML**

1\. Click ‘Set Interactions’ button.

2\. Click ‘Yes, set interactions!’ button to move to interaction interface (or ‘Cancel’ to stay on

same interface.)

**3. Hover over a service to see its connected service. Click on the service to set interaction**

**with its connected service.**

a) To set interaction between ‘Cargo Trucks’ and ‘Cargo Trucks Location’ services,

hover over ‘Cargo Trucks’ service and click it.

b) Enter data for all fields.

c) Click ‘Submit’ button.

**4. For multiple services or projected services ({r}) connected after a service, hover on 2nd**

**service. Click on the service to set interaction with its connected service.**

a) To set interaction between ‘Situation Assessment {r}’ and ‘Cargo Trucks Location’

services, hover over ‘Situation Assessment {r}’ service and click it.

b) Enter data for all fields.

c) Click ‘Submit’ button.

**5. Set interactions off for any services which do not interact. This can be possible for**

**projected service ({c}) and its connected service**

a) To set interaction off between ‘Situation Assessment\_{c}’ and ‘Data Buffering and

Integration’ services, hover over ‘Situation Assessment\_{c}’ service and click it.

b) Press ‘Set Interactions’ toggle button to change it to ‘No’.

c) Click ‘Submit’ button.

**6. Likewise, set interactions for all connected services.**

**7. Click ‘Create Workflow’ button.**



