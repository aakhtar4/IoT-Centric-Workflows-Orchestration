
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="no-cache, no-store, must-revalidate" http-equiv="Content-Type">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <title>Service Registration</title>
    <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>-->

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/FileSaver.js/1.3.8/FileSaver.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>
   

	<meta name="viewport" content="width=device-width, initial-scale=1">
	  <!-- Bootstrap core CSS
 <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" integrity="sha384-HSMxcRTRxnN+Bdg0JdbxYKrThecOKuH5zCYotlSAcp1+c8xmyTe9GYg1l9a69psu" crossorigin="anonymous">	  -->
  <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <!-- Custom styles for this template -->
  <link href="css/simple-sidebar.css" rel="stylesheet">
    <!-- Latest compiled and minified JavaScript -->
<!-- Add icon library -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
.bttn {
  background-color: DodgerBlue;
  border: none;
  color: white;
  padding: 12px 16px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 20px;

}

/* Darker background on mouse-over */
.bttn:hover {
  background-color: RoyalBlue;
}
.bg-light {
    background-color: #e6f2ff!important;
}



</style>



    <!-- Optional theme -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css" integrity="sha384-6pzBo3FDv/PJ8r2KRkGHifhEocL+1X2rVCTTkUfGk7/0pbek5mMa1upzvWbrUbOZ" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js" integrity="sha384-aJ21OjlMXNL5UyIl/XNwTMqvzeRMZH2w8c5cRVpzpU8Y5bApTppSuUkhZXN0VxHd" crossorigin="anonymous"></script>
    <link rel="stylesheet"  type = "text/css" href="workflow.css">
    <script>

        $(document).ready(function(){
		
		         var modal = document.getElementById('myModal');
                  modal.style.display = "none";
							
            load_json_data('Category');
            function load_json_data(id, parent_id)
            {
                var html_code = '';
                $.getJSON('service.json', function(data) {
                    html_code += '<option value="" disabled selected>Select '+id+'</option>';
                    $.each(data, function(key, value){
                        if(id == 'Category')
                        {
                            if(value.parent_id == '0')
                            {
                                html_code += '<option value="'+value.id+'">'+value.name+'</option>';
                            }
                        }
                        else
                        {
                            if(value.parent_id == parent_id)
                            {
                                html_code += '<option value="'+value.id+'">'+value.name+'</option>';
                            }
                        }
                    });
                    html_code += '<option id="new" value="new">Add New '+id+'</option>';
                    $('#'+id).html(html_code);
                });
            }
            $(document).on('change', '#Category', function(){
                var service_id = $(this).val();
                if(service_id != '')
                {
                    $('#Service').html('<option value="" disabled selected>Select Service</option>');
                    if(service_id == 'new'){
                        $('#add_service').prop("hidden", false);
                        $('#Sub-Category').html('<option value="" id="new_dev">add new sub-category</option>');
                    }
                    else{
                        $('#service_add_inp').val('');
                        $('#add_service').prop("hidden", true);
                        $('#add_device').prop("hidden", true);

                        load_json_data('Sub-Category', service_id);
                    }
                }
                else
                {
                    $('#Sub-Category').html('<option value="" disabled>Select Sub-Category</option>');
                    $('#Service').html('<option value="" disabled>Select Service</option>');
                }
            });
            $(document).on('change', '#Sub-Category', function(){
                var device_id = $(this).val();
                if(device_id != '')
                {
                    if(device_id == 'new'){
                        $('#add_device').prop("hidden", false);
                        $('#Service').html('<option value="" id="new_act">add new service</option>');
                    }
                    else{
                        $('#device_add_inp').val('');
                        $('#add_device').prop("hidden", true);
                        $('#add_activity').prop("hidden", true);
                        load_json_data('Service', device_id);
                    }
                }
                else
                {
                    $('#Service').html('<option value="">Select Service</option>');
                }
            });

            $(document).on('change', '#Service', function(){
                var activity_id = $(this).val();
                if(activity_id != '')
                {
                    if(activity_id == 'new'){
                        $('#add_activity').prop("hidden", false);
                    }
                    else{
                        $('#activity_add_inp').val('');
                        $('#add_activity').prop("hidden", true);
                    }
					
                }
            });
        });

        /*following commented out code contains variable and data for autofill*/
        //        req_element = [];
        //        req_element.push("weather","X","Y","8","meters","8","minutes","1.2 GHZ quad-core ARM Cortex A53 (ARMv8 Instruction Set)","10/100 MBPS Ethernet, 802.11n Wireless LAN, Bluetooth 4.0","4","Gb","8","Gb","Rasbian","Java, .Net, Ruby, MySQL","http://schemas.opengis.net/sos/2.0/sosGetObservation.xsd#request","http://schemas.opengis.net/sos/2.0/sosGetObservation.xsd#response","sensor","non_deployable","U");

        function read_new_name(index){
            var last_id = 0;
            var parent_id = 0;
            var file_content = '';
			
			    var inputService = document.getElementById("Service_Name"); 
					 inputService.setAttribute('value', document.getElementById('activity_add_inp').value); 
					 var inputCatg = document.getElementById("Category_Name"); 
					 if(document.getElementById('Category').value == 1){
					 		inputCatg.setAttribute('value', 'Fog Service'); 
					 }
					 else if(document.getElementById('Category').value == 2){
							inputCatg.setAttribute('value', 'Environment Service'); 
					 }
					 else if(document.getElementById('Category').value == 3){
							inputCatg.setAttribute('value', 'Cloud Service'); 
					 }
					 else if(document.getElementById('Category').value == 57){
							inputCatg.setAttribute('value', 'Edge Service'); 
					 }
					 else if(document.getElementById('Category').value == 74){//
							inputCatg.setAttribute('value', 'weather Category'); 
					 }
					 else if(document.getElementById('Category').value == 88){//
							inputCatg.setAttribute('value', 'Sensor'); 
					 }
			
			
            $.getJSON('service.json', function(data){
                
				last_id = data.length+1; //id for new service / next to last id
				var max = Math.max.apply(null, data.map(service => service.id));
				console.log(max);
				max++;
				last_id=max;
				
				
                file_content = JSON.stringify(data, null,1); //JSON file content
                file_content = file_content.substring(0, file_content.length - 1);
                var requirement_map = [];
                var inp_name = '';
                var inp_sd = '';
                var sd = '';
				
				
                if(index == 0) {
                    $('#service_add_inp').prop("disabled", true);
                    $('#service_add_btn').prop("disabled", true);
                    inp_name = document.getElementById('service_add_inp').value;
                    if (inp_name == '') {
                        alert('add category name!');
                        return;
                    }
                    console.log('new category: ',inp_name);
                    parent_id = 0;
                }
                else if(index == 1 || index == 2){
                    if(index == 1){
                        inp_name = document.getElementById('device_add_inp').value;
                        inp_sd = document.getElementById('Category');
                        sd = inp_sd.options[inp_sd.selectedIndex].text;
                    }
                    else{
                        inp_name = document.getElementById('activity_add_inp').value;
                        inp_sd = document.getElementById('Sub-Category');
                        sd = inp_sd.options[inp_sd.selectedIndex].text;
                    }
                    if (inp_name == '') {
                        if(index == 1) {
                            alert('add sub-category name!');
                            return;
                        }
                        else{
                            alert('add service name!');
                            return;
                        }
                    }
                    data.forEach(function(data){

                        if(data.name == sd) {
                            console.log('data\t', data.name);
                            console.log('pid\t', data.id);
                            parent_id = data.id;
                        }
                    });
                }
                var inp_val = inp_name.toLowerCase().replace(' ','_'); //change name to formatted value
                inp_name = inp_name.replace('_',' '); //change name according to format
                inp_name = toTitleCase(inp_name); //change name according to format
                var new_data = '{"id": "' +last_id+ '", "name": "' +inp_name+ '", "parent_id": "'+ parent_id+ '", "value": "' +inp_val+ '"}'; //new addition
                var pass = document.getElementById('passer_inp');
                pass.value = new_data;
                if(index == 0 || index == 1) {
                    location = 'add.php?passer=' + pass.value;
                }
                else{
                    if(index == 2){ //displays specification box for entered service
                        var categ = document.getElementById('Category');
                        var cat = categ.options[categ.selectedIndex].text;
                        var modal = document.getElementById('myModal');
                        modal.style.display = "block";
                        var sn = document.getElementById('Service_Name');
                        sn.innerHTML = inp_name;
                        var requirement_button = document.getElementById("req_submit_button");
                        var inp = document.getElementsByClassName("form-control");

                        /*following code is for autofill specification form*/
//                        for (var p = 0; p < 20; p++) {
//                            inp[p].value = req_element[p]; //fill value of input fields with previously entered data
//                        }

                        requirement_button.onclick = function(){
                            $('#activity_add_num_btn').prop('disabled', true);
                            var terminate = 0;
                            $(".form-control").each(function(i) {
                                var val = document.getElementsByClassName("control-label");
                                var element = $(this);
                                var msg = val[i].innerHTML;
                                var tag = element.attr('name');
                                if (element.val() == "") {
                                    Swal.fire({title: 'Data Missing for '+msg+'!', text: 'Fill '+msg+' field', icon: 'warning'});
                                    inp[i].style.backgroundColor = "yellow";
                                    inp[i].addEventListener("keypress", function () {
                                        inp[i].style.backgroundColor = "white"
                                    });

                                    window.onclick = function (event) {
                                        if (event.target == modal) {
                                            modal.style.display = "block";
                                        }
                                    };
                                    return false;
                                }
                                else{
                                    terminate++;
                                    if(tag == 'val_radius' || tag == 'val_avail' || tag == 'val_ram'
                                            || tag == 'val_ext'){
                                        tag = 'Value';
                                    }
                                    if(tag == 'unit_radius' || tag == 'unit_avail' || tag == 'unit_ram'
                                            || tag == 'unit_ext'){
                                        tag = 'Unit';
                                    }
                                    requirement_map.push([element.val()]);
                                    console.log('terminate ' + terminate + "  " + requirement_map[terminate]);
                                }
                                if(terminate == 24){
                                    var spec = document.getElementById('specs');

                                    var s_name = inp_name.toLowerCase().replace(/ /g, '_'); //remove spaces in name
                                    var xml = '';
                                    xml = xml+
                                    '<Gamma id="'+ last_id + '">\n' +
                                    '<Service_Name>'+ s_name +'</Service_Name>\n' +
                                    '<Service_Category>'+ inputCatg.defaultValue + '</Service_Category>\n' +
                                    '<Location>\n' +
                                    '<Latitude>' + requirement_map[6] + '</Latitude>\n' +
                                    '<Longitude>' + requirement_map[7] + '</Longitude>\n' +
                                    '<Radius>\n'+
                                    '<Value>' + requirement_map[8] + '</Value>\n' +
                                    '<Unit>' + requirement_map[9] + '</Unit>\n' +
                                    '</Radius>\n' +
                                    '</Location>\n' +
                                    '<Availability>\n' +
                                    '<Value>' + requirement_map[10] + '</Value>\n' +
                                    '<Unit>' + requirement_map[11] + '</Unit>\n' +
                                    '</Availability>\n' +
                                    '<Hardware_Requirements>\n' +
                                    '<CPU>' + requirement_map[12] + '</CPU>\n' +
                                    '<Network>' + requirement_map[13] + '</Network>\n'+
                                    '<RAM>\n' +
                                    '<Value>' + requirement_map[16] + '</Value>\n' +
                                    '<Unit>' + requirement_map[17] + '</Unit>\n' +
                                    '</RAM>\n' +
                                    '<external_storage>\n'+
                                    '<Value>' + requirement_map[14] + '</Value>\n' +
                                    '<Unit>' + requirement_map[15] + '</Unit>\n' +
                                    '</external_storage>\n' +
                                    '</Hardware_Requirements>\n' +
                                    '<Software_Requirements>\n' +
                                    '<OS>' + requirement_map[18] + '</OS>\n' +
                                    '<Package_List>\n';
                                    var list = requirement_map[19][0].split(',');
                                    for(var iterate = 0; iterate < list.length; iterate++){
                                        if(list[iterate] == '' || list[iterate] == ' '){
                                            list.splice(iterate,1);
                                            continue;
                                        }
                                        if(list[iterate].charAt(0) == ' '){ //remove space from first character
                                            list[iterate] = list[iterate].replace(' ','');
                                        }
                                        if(list[iterate].endsWith(' ')){ //remove space from last character
                                            list[iterate] = list[iterate].replace(/.$/,"");
                                        }
                                        xml = xml +
                                        '<Package>' + list[iterate] + '</Package>\n';
                                    }
                                    xml = xml +
                                    '</Package_List>\n' +
                                    '</Software_Requirements>\n' +
                                    '<Service_Input>' + requirement_map[20] + '</Service_Input>\n' +
                                    '<Service_Output>' + requirement_map[21] + '</Service_Output>\n' +
                                    '<Service_Type>' + requirement_map[5] + '</Service_Type>\n' +
                                    '<Deployable>' + requirement_map[22] + '</Deployable>\n' +
                                    '<URI>' + requirement_map[23] + '</URI>\n' +
                                    '</Gamma>\n';

                                    spec.value = pass.value + '~' + xml;
                                    modal.style.display = "none";
                                    var button = document.getElementById('req_submit_button');
                                    button.setAttribute('type','submit');
                                }
                                inp[terminate].focus();

                            });
                            requirement_map = [];
                        };							
                    }
                }
            });
        }

        function toTitleCase(str) {
            return str.replace(/\w\S*/g, function(txt){
                return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
            });
        }

    </script>

</head>
<body>
<div class="d-flex" id="wrapper">

    <!-- Sidebar -->
    <div class="bg-light border-right" id="sidebar-wrapper">
      <div class="sidebar-heading"><a href="index.php"/><img src="images/dashboradTrans.png"></div>
	  <br>
      <div class="list-group list-group-flush">
	 
        <a href="Service_Registration.php" class="list-group-item list-group-item-action bg-light"><img style="height:25px;" src="images/add.png"> Service Registration</a>
        <a href="ServiceList.php" class="list-group-item list-group-item-action bg-light"><img  style="height:20px;" src="images/search.png"> View Service</a>
        <a href="Home.php" class="list-group-item list-group-item-action bg-light"><img  style="height:25px;" src="images/processTrans.png"> Create Process</a>
      </div>
    </div>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">

    <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
         <button  id="menu-toggle" style="background: none; border: none;" ><img  style="height:50px;"  src="images/back.png"></button>
         <button  id="button2" style="display:none; background: none; border: none;"><img  style="height:50px;"  src="images/next.png" ></button>
         <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav ml-auto mt-2 mt-lg-0">
            
			<li class="nav-item">
              <a class="nav-link" href="Service_Registration.php" active><img style="height:25px;" src="images/add.png"><b>Service Registration</b></a>
            </li>
            <li class="nav-item">
             <a class="nav-link" href="ServiceList.php"><img  style="height:20px;" src="images/search.png"><b>View Service</b></a>
            </li>
			<li class="nav-item">
             <a class="nav-link" href="Home.php"><img  style="height:25px;" src="images/processTrans.png"><b>Create Process</b></a>
            </li>
			<li class="nav-item ">
              <a class="nav-link" href="index.php"><img src="images/Capturetransp.png"></a>
            </li>
          </ul>
        </div>
      </nav>

<div class="container-fluid" style="width:600px;">
<h1 class="page-header text-center">Service Registration</h1><br />
    <h2 align="center">Add Category, Sub-Category and Service</h2><br /><br />
    <form id="serviceform" action="add.php" method="get">
        <input type="hidden" id="passer_inp" name="passer">
        <div id="div_service">
            <select name="category" id="Category" class="form-control input-lg">
                <option value="">Select Category</option>
            </select>
        </div>
        <div id = 'add_service' hidden="true">
            <label for="service_add_inp">New Category Name:</label>
            <input id="service_add_inp" autocomplete="off" required="true">
            <button type="submit" id="service_add_btn" class="btn btn-default" onclick="read_new_name(0)">add new category</button>
        </div>

        <br />
        <div id="div_device">
            <select name="sub-category" id="Sub-Category" class="form-control input-lg">
                <option value="">Select Sub-Category</option>
            </select>
        </div>
        <div id = 'add_device' hidden="true">

            <label for="device_add_inp">New Sub-Category Name:</label>
            <input id="device_add_inp" autocomplete="off" required="true">
            <button type="submit" id="device_add_num_btn" class="btn btn-default" onclick="read_new_name(1)">add new sub-category</button>
        </div>
        <br>
        <div id="div_activity">
            <select name="service" id="Service" class="form-control input-lg">
                <option value="">Select Service</option>
            </select>
        </div>
		<br>
        <div id = 'add_activity' hidden="true">

            <label for="activity_add_inp"><b>New Service Name:</b></label>
            <input id="activity_add_inp" autocomplete="off" required="true">
            <button type="submit" id="activity_add_num_btn" class="btn btn-default" onclick="read_new_name(2)" style="background-image: linear-gradient(to bottom,#fff 0,#add 100%);">add new service</button>
        </div>
		

    </form>

		
 <div id="myModal" class="modal">
 <div class="modal-dialog" style="height:86% ;">
        <!-- Modal content 
		-->
        <div class="modal-content" style=" overflow-y: auto; overflow-x: hidden; height: 100%;"  >
            <div class="modal-header">
               
                <p class="req_heading">Service Registration</p>
				<span class="close_modal"  onclick="document.getElementById('myModal').style.display='none'">&times;</span>
				    </div>
            <p id="req_p">Enter Specifications for Selected Service:</p>
			<div class="container-fluid">

            <form action="add.php" name="requirement_form" id="req_form" method="post">
                <div class="modal-body">

                    <input type="hidden" id="specs" name="specs">
                    <!----------------------service name--------------------->
                        <div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Service Name:</label>
					</div>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="Service_Name" id="Service_Name" readonly>
					</div>
				</div>
                        <!----------------------service cagtegory--------------------->
                        <div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label"  style="position:relative; top:7px;">Service Category:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Category_Name"  name="Service_Category" readonly>
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Service Type:</label>
					</div>
					<div class="col-sm-4">
						<select id="Service_Type"  class="form-control" name="Service_Type" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						 <option value="sensor">Sensor</option>
						 <option value="Fog Service">Fog Service</option>
						 <option value="Cloud">Cloud</option>
						 <option value="Device">Device</option>
					  </select>
						
					</div>
				</div>
				
				    
                        <!----------------------Location--------------------->
                        <div class="row form-group">
					<div class="col-sm-8">
						<label class="control-label" style="position:relative;font-size: 19px;margin-left: 200px;">Location</label>
					</div>
				</div>
				
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Latitude:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Latitude" name="Latitude">
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Longitude:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control"  id="Longitude" name="Longitude">
					</div>
				</div>
				
				
				       <!----------------------Radius--------------------->
                       <div class="row form-group">
					<div class="col-sm-8">
						<label class="control-label" style="position:relative;font-size: 19px;margin-left: 200px;">Radius</label>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Value:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="val_radius" id="val_radius">
					</div>
					<div class="col-sm-2">
						<label class="control-label"  for="unit_radius" style="position:relative; top:7px;">Unit:</label>
					</div>
					<div class="col-sm-4">
						<select id="unit_radius" class="form-control" name="unit_radius" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						 <option value="meters">Meters</option>
					  </select>
					</div>
				</div>
                        
				<!-----------------------Availability---------------------->

                       <div class="row form-group">
					<div class="col-sm-8">
						<label class="control-label" style="position:relative;font-size: 19px;margin-left: 200px;">Availability</label>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Value:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="val_avail" name="Value">
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Unit:</label>
					</div>
					<div class="col-sm-4">
						<select id="unit_avail"  class="form-control" name="unit_avail" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						 <option value="mintus">mintus</option>
						 <option value="second">second</option>
					  </select>
					</div>
				</div>
				
                 
                        <!-----------------------Hardware Requirement---------------------->
						
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">CPU:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="CPU" name="CPU">
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Network:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Network" name="Network">
					</div>
				</div>

                 <div class="row form-group">
					<div class="col-sm-10">
						<label class="control-label" style="position:relative;font-size: 19px;margin-left: 188px;">Hardware Requirements</label>
					</div>
				</div>
				
				<div class="row form-group">
					<div class="col-sm-3">
						<label class="control-label" style="position:relative; top:7px;">Ext. Storage Value:</label>
					</div>
					<div class="col-sm-3">
						<input type="text" class="form-control" name="val_ext" id="val_ext">
					</div>
					<div class="col-sm-3">
						<label class="control-label" style="position:relative; top:7px;">Ext. Storage Unit:</label>
					</div>
					<div class="col-sm-3">
						<select id="unit_ext" class="form-control"  name="unit_ext" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						 <option value="GB">GB</option>
						 <option value="MB">MB</option>
					  </select>
					</div>
				</div>
				
				<div class="row form-group">
					<div class="col-sm-3">
					
						<label class="control-label" style="position:relative; top:7px;">Ram Value:</label>
					</div>
					<div class="col-sm-3">
						<input type="text" class="form-control" name="val_ram" id="val_ram">
					</div>
					<div class="col-sm-3">
						<label class="control-label" style="position:relative; top:7px;">Ram Unit:</label>
					</div>
					<div class="col-sm-3">
						<select id="unit_ram" class="form-control"  name="unit_ram" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						 <option value="GB">GB</option>
						 <option value="MB">MB</option>
					  </select>
					</div>
				</div>
				
				

                        <!-----------------------Software Requirements---------------------->

                        <div class="row form-group">
					<div class="col-sm-10">
						<label class="control-label" style="position:relative;font-size: 19px;margin-left: 188px;">Software Requirements</label>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">OS:</label>
					</div>
					<div class="col-sm-4">
						<select id="OS"  name="OS"  class="form-control" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						 <option value="raspbian">Raspbian</option>
						 <option value="linux">Linux</option>
						 <option value="unix">Unix</option>
					  </select>
					</div>
					<div class="col-sm-2">
						<label class="control-label"  id="Package_List" style="position:relative; top:7px;">Package List:</label>
						<label style="font-size: 10px; font-weight: normal" for="Package_List"> (separate packages using comma (,))</label>

					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="Package_List">
					</div>
				</div>
				
				
				 <!-----------------------Software I/O/T---------------------->
                        <div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Service Input:</label>
					</div>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="Service_Input" id="Service_Input">
					</div>
				</div>
                           <div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Service Output:</label>
					</div>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="Service_Output" id="Service_Output">
					</div>
				</div>
				
				  <!-----------------------Deployable & URI---------------------->


                    <div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Deployable:</label>
					</div>
					<div class="col-sm-4">
						<select  id="Deployable" class="form-control" name="Deployable" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						 <option value="deployable">Deployable</option>
						<option value="non_deployable">Non Deployable</option>
					  </select>
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">URI:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="URI" id="URI">
					</div>
				</div>            
                  
                  <div class="modal-footer">
                    <button   id="req_submit_button"  value="Submit" name="add" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-disk"></span> Save</a>
                    <button type="button" class="btn btn-default" data-dismiss="modal"  onclick="document.getElementById('myModal').style.display='none'"><span class="glyphicon glyphicon-remove"></span> Cancel</button>
				</div>
                </div>
                </div>
                </div>
                
            </form>
        </div>
    </div>
</div></div></div>
<!-- Bootstrap core JavaScript -->
  <script src="vendor/jquery/jquery.min.js"></script>
  <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  <!-- Menu Toggle Script -->
  <script>
    $("#menu-toggle").click(function(e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
	   var x = document.getElementById("menu-toggle");
	   var y=document.getElementById("button2");
  if (x.style.display === "none") {
    x.style.display = "block";
  } else {
    x.style.display = "none";
	y.style.display="block";
  }
    });
	$("#button2").click(function(e){
		e.preventDefault();
		 $("#wrapper").toggleClass("toggled");
		var x = document.getElementById("menu-toggle");
	   var y=document.getElementById("button2");
  if (y.style.display === "none") {
    y.style.display = "block";
  } else {
    y.style.display = "none";
	x.style.display="block";
  }
	});
  </script>
</body>
</html>