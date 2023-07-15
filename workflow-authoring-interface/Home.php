<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="utf-8" http-equiv="encoding">
    <title>Dynamic Workflow Modeling</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/FileSaver.js/1.3.8/FileSaver.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" rel="stylesheet"/>

     <script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/raphael/2.3.0/raphael.min.js"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" integrity="sha384-HSMxcRTRxnN+Bdg0JdbxYKrThecOKuH5zCYotlSAcp1+c8xmyTe9GYg1l9a69psu" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css" integrity="sha384-6pzBo3FDv/PJ8r2KRkGHifhEocL+1X2rVCTTkUfGk7/0pbek5mMa1upzvWbrUbOZ" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js" integrity="sha384-aJ21OjlMXNL5UyIl/XNwTMqvzeRMZH2w8c5cRVpzpU8Y5bApTppSuUkhZXN0VxHd" crossorigin="anonymous"></script>
    <link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">
    <script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
    <link rel="stylesheet"  type = "text/css" href="homeStyle.css">
	<link rel="stylesheet"  type = "text/css" href="workflow.css">
	<script type="text/javascript" src="script/homeScript.js"></script>
	<script type="text/javascript" src="script/raphael.min.js"></script>
	<script type="text/javascript" src="script/tipped.js"></script>
	<link rel="stylesheet" type="text/css" href="css/tipped.css" />


  <script>

        $(document).ready(function(){
            load_json_data('category');
            load_json_data('addblock_menu_cat');
            function load_json_data(id, parent_id)
            {
                var html_code = '';
                $.getJSON('service.json', function(data){

                    if(id == 'addblock_menu'){
                        html_code += '<option value="" disabled selected>Select service</option>';
                    }
                    else if(id=='addblock_menu_cat'){
                        html_code += '<option value="" disabled selected>Select category</option>';
                    }
                    else if(id=='addblock_menu_subcat'){
                        html_code += '<option value="" disabled selected>Select sub-category</option>';
                    }
                    else{
                        html_code += '<option value="" disabled selected>Select '+id+'</option>';
                    }
                    $.each(data, function(key, value){

                        if(id == 'category' || id == 'addblock_menu_cat')
                        {
                            if(value.parent_id == '0')
                            {
                                html_code += '<option value="'+value.id+'">'+value.name+'</option>';
                            }
                        }
                        else if(id == 'subcategory' || id == 'addblock_menu_subcat')
                        {
                            if(value.parent_id == parent_id)
                            {
                                html_code += '<option value="'+value.id+'">'+value.name+'</option>';
                            }
                        }
                        else{
                            if(value.parent_id == parent_id)
                            {
                                html_code += '<option value="'+value.id+'">'+value.name+'</option>';
                            }
                        }

                    });
                    $('#'+id).html(html_code);

                });
            }

            $(document).on('change', '#category', function(){
                var service_id = $(this).val();
                if(service_id != '')
                {
                    $('#service').html('<option value="" disabled selected>Select service</option>');
                    load_json_data('subcategory', service_id);
                }
                else
                {
                    $('#subcategory').html('<option value="" disabled>Select sub-category</option>');
                    $('#service').html('<option value="" disabled>Select service</option>');
                }
            });
            $(document).on('change', '#subcategory', function(){
                var device_id = $(this).val();
                if(device_id != '')
                {
                    load_json_data('service', device_id);
                }
                else
                {
                    $('#service').html('<option value="">Select service</option>');
                }
            });
            $(document).on('change', '#addblock_menu_cat', function(){
                var service_id = $(this).val();
                if(service_id != '')
                {
                    $('#addblock_menu').html('<option value="" disabled selected>Select service</option>');
                    load_json_data('addblock_menu_subcat', service_id);
                }
                else
                {
                    $('#addblock_menu_subcat').html('<option value="" disabled>Select sub-category</option>');
                    $('#addblock_menu').html('<option value="">Select service</option>');
                }
            });
            $(document).on('change', '#addblock_menu_subcat', function(){
                var device_id = $(this).val();
                if(device_id != '')
                {
                    $('#s_d_btn').prop("disabled", false);
                    load_json_data('addblock_menu', device_id);
                }
                else
                {
                    $('#addblock_menu').html('<option value="">Select service</option>');
                }
            });
        });

    </script>
</head>

<body>

<div class="overlay" style="margin-left: 20px;">
        <div class="row">
            <div class="col-md-12">
                <button id="CreateNodeBtn" onclick="add_service()" class="btn btn-default icon ColorIcon function" ><span class="glyphicon glyphicon-plus"></span></button>
            </div>
        </div>
        <div class="row"><div class="col-md-12"><hr style="margin:2px; border-color:#DDD"></div></div>
       <!-- <div class="row">
            <div class="col-md-12">
                <button id="DataBase"  onclick="databseDiv()" class="btn btn-default icon ColorIcon function" ><span class="glyphicon glyphicon-list"></span></button>
            </div>
        </div>
        <div class="row"><div class="col-md-12"><hr style="margin:2px; border-color:#DDD"></div></div> -->
		
		 <div class="row">
            <div class="col-md-12">
                <button id="addBlockLogo"  onclick="addblock_box()" class="btn btn-default icon ColorIcon function" style="font-size:20px"></button>
            </div>
        </div>
        
        <div class="row"><div class="col-md-12"><hr style="margin:2px; border-color:#DDD"></div></div>
		<div class="row">
            <div class="col-md-12">
                <button id="CopyServiceLogo"  onclick="copy_service()" class="btn btn-default icon ColorIcon function" data-title="Clear All" data-content="Clear the entire canvas" style="font-size:20px"></button>
            </div>
        </div>
        <div class="row"><div class="col-md-12"><hr style="margin:2px; border-color:#DDD"></div></div>       
           
        <div class="row">
            <div class="col-md-12">
                <button id="ClearBtn"  onclick="clearPaper()" class="btn btn-default icon ColorIcon function" data-title="Clear All" data-content="Clear the entire canvas" style="font-size:20px"></button>
            </div>
        </div>
        <div class="row"><div class="col-md-12"><hr style="margin:2px; border-color:#DDD"></div></div>
		
		 <div class="row">
            <div class="col-md-12">
                <button id="SaveNodesBtn"  onclick="save_model()" class="btn btn-default icon ColorIcon function"><span style="background-color: white;" class="glyphicon glyphicon-floppy-disk"></span></button>

            </div>
        </div>
		
		<div class="row"><div class="col-md-12"><hr style="margin:2px; border-color:#DDD"></div></div>	
		 <div class="row">
            <div class="col-md-12">
                <button id="uploadButton"  onclick="UploadModel()" class="btn btn-default icon ColorIcon function"></button>
            </div>
        </div>
		
        <div class="row"><div class="col-md-12"><hr style="margin:2px; border-color:#DDD"></div></div>	
        <div class="row">
            <div class="col-md-12">
                <button id="HelpButton" onclick="openTab(this)" class="btn btn-default icon function popout"></button>
            </div>
        </div>
		
		   

		
    </div>
	

	<div id="EditorLogosChild">
        <!--<button id="addBlockLogo" onclick="addblock_box()" style="box-shadow: rgb(0, 0, 0) 0px 0px 10px; border: 4px solid rgb(41, 85, 151);"></button>
        <button id="CopyServiceLogo"  onclick="copy_service()"></button>-->
        <button id="SetInstructionsLogo" onclick="finalize_model()"></button>
        <button id="createWorkFlowLogo" onclick="create_workflow()"></button>
      <!--  <button id="collapseee" class="function22" data-content="Collapse editor panel" style="background-color:transparent;"><span class="glyphicon glyphicon-chevron-down" style="color:rgb(41,85,151)"></span></button>-->
    </div>
	
			<!--create new node-->

		<form id="flowchart" class="form-horizontal">

		<div id="add_service" class="modal">
            <!-- Modal content -->
            <div class="modal-content" id="cs_content" style="width:40% !important;">
                <div class="modal-header">
                    <span class="close_add_service_popup">&times;</span>
                    <p class="req_heading" id="cs_heading">Select Category, Sub-Category and Service</p>
                </div>
				
				<div class="modal-body" style="margin-bottom: -20px;">
				  <div class="row" style="margin-left:-7px;">
					<select name="category" id="category" class="form-control input-lg">
						<option value="">Select category</option>
					</select>
					<br />
					<select name="subcategory" id="subcategory" class="form-control input-lg">
						<option value="">Select Sub-category</option>
					</select>
					<br>
					<select name="service" id="service" class="form-control input-lg">
						<option value="" disabled selected>Select service</option>
					</select>
					<br>
					<div class="form-group col-md-11"></div>
					<div class="form-group col-md-1"></div>
					<div class="form-group row-md-4">
						
					</div>
				</div>
				
				</div>
				
				<div id="add_footer" class="modal-footer">
                    <button class="btn btn-primary" id="add" class="btn btn-primary" form="flowchart" disabled onclick="add_service();">Add</button>
					<button id="closeModal" type="button" class="btn btn-danger" data-dismiss="modal">Close</button>               
			   </div>
            </div>
        </div>
		
		<!--upload model-->
		<div id="UploadModel" class="modal">
            <!-- Modal content -->
            <div class="modal-content" id="cs_content" style="width:30% !important;">
                <div class="modal-header">
                    <span class="close_upload_service_popup">&times;</span>
                    <p class="req_heading" id="cs_heading" style="margin-left:-250px;">File upload</p>
                </div>
			  <label class="file_upload" style="margin-left: 130px;">
                        <input type="file" id="file_inp" accept=".json"  onchange='upload_model(event)'>
                        Upload Existing File
                    </label>
				
				<div id="add_footer" class="modal-footer">
					<button id="closeUploadModal" type="button" class="btn btn-danger" data-dismiss="modal">Close</button>               
			   </div>
            </div>
        </div>
		
	
	    <!--add block popup-->
        <div id="add_block_popup" class="modal">
            <!-- Modal content -->
            <div class="modal-content" style="width:40%;">
                <div class="modal-header">
                    <span class="close_addblock_popup">&times;</span>
                    <p class="req_heading">Add Block of Services</p>
                </div>
                <div class="modal-body">
                    <div id="myAddBlockDiv">
                        <div class="row" id="addblock_row">
                            <div class = 'col-md-3'></div>
                            <div class = 'col-md-5'>
                                <select name="addblock_menu_cat" id="addblock_menu_cat" class="form-control">
                                    <option value="" disabled selected>Select Category</option>
                                </select>
                                <select name="addblock_menu_subcat" id="addblock_menu_subcat" class="form-control">
                                    <option value="" disabled selected>Select Sub-category</option>
                                </select>
                                <select name="addblock_menu" id="addblock_menu" class="form-control">
                                    <option value="" disabled selected>Add Block</option>
                                </select>
                            </div>
                        </div>
                        <div class="row" id="addblock_row2">
                            <label id="ab_lbl" for="before_after" style="margin-right: 20%;">Block of Services</label>
                            <select style="margin-right: 20%;" multiple name="addblock_multiple_menu" id="addblock_multiple_menu" class="form-control col-md-2">

                            </select>
                            <div class ='col-md-3'><br>
                                <label id="ba_lbl" for="before_after">before/after</label>
                                <select name="before_after" id="before_after" class="form-control col-md-3">
                                    <option value="before" id="before" selected>before</option>
                                    <option value="after" id="after">after</option>
                                    <option value="beforeAfter" id="beforeAfter">before and after</option>
                                </select>
                            </div>
                            <div id="adjust_addblock_activity" class = 'col-md-4'><br>
                                <label id="existing_lbl">Existing Service</label>
                                <label id="before_after_lbl"></label>
                                <label id="addblock_activity_lbl"></label>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="addblock_footer" class="modal-footer">
                    <button form="flowchart" class="btn btn-primary" id="addblock_popup_button" type="button" disabled>Add to Block</button>
                    <button class="btn btn-danger" id="addblock_finish" type="button" disabled>Finish</button>
                    <button class="btn btn-default" id="addblock_cancel" type="button">Cancel</button>
                </div>
            </div>
        </div>
	
		<!--Copy Service-->
		    <div id="copy_service" class="modal">
            <!-- Modal content -->
            <div class="modal-content" id="cs_content" style="width:40%; !important">
                <div class="modal-header">
                    <span class="close_copy_service_popup">&times;</span>
                    <p class="req_heading" id="cs_heading">Copy Service</p>
                </div>
                <div class="modal-body">
                    <p id="cs_p">Select a service to copy:</p>
                    <div class="row" id="myCopyDiv">

                        <div id="adjust_copy_activity" class = 'col-md-4'>
                            <label id="copy_lbl">Copy Service</label>
                        </div>
                        <div class ='col-md-3'>
                            <label id="copy_ba_lbl">before/after</label>
                            <select name="copy_before_after" id="copy_before_after" class="form-control col-md-3">
                                <option value="before" id="copy_before" selected>before</option>
                                <option value="after" id="copy_after">after</option>

                            </select>
                            <label align="center" id="r_included"></label>
                        </div>
                        <div id="adjust_copy_activity2" class = 'col-md-4'>
                            <label id="copy_ext_lbl">Existing Service</label>
                        </div>
                    </div>
                </div>
                <div id="copy_footer" class="modal-footer">
                    <button form="flowchart" class="btn btn-primary" id="copy_button" type="button">Copy</button>
                </div>
            </div>
        </div>
		
	<div id="myModal" class="modal">
		<div class="modal-dialog" style="height:86% ;">
		<!-- Modal content
		-->
		<div class="modal-content" style="width: 120%; overflow-y: auto; overflow-x: hidden; height: 100%;">
		<div class="modal-header">

		<p class="req_heading">Specifications</p>
		<span class="close_modal" onclick="document.getElementById('myModal').style.display='none';">&times;</span>

		</div>
		<p id="req_p">Enter Specifications for Selected Service:</p>
		<div class="container-fluid">

		<form name="requirement_form" id="req_form" >
		<div class="modal-body" style="padding: 0;">

		<input type="hidden" id="specs" name="specs">
		<!----------------------service name--------------------->
		<div class="row form-group">
		<div class="col-sm-2">
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Service Name:</label>
		</div>
		<div class="col-sm-10">
		<label class="form-control" name="Service_Name" id="Service_Name" readonly></label>
		</div>
		</div>
		<!----------------------service cagtegory--------------------->
		<div class="row form-group">
			<div class="col-sm-2">
				<label class="control-label req_lbl_h" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Service Category:</label>
			</div>
			<div class="col-sm-4">
			<select id="Service_Category" class="req_inp" name="Service_Category" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
				<option value="" select disabled>Select category</option>
				<option value="Fog Service">Fog Service</option>
				<option value="Environment Service">Environment Service</option>
				<option value="Cloud Service">Cloud Service</option>
				<option value="weather Category">Weather Service</option>
				<option value="Sensor">Sensor</option>
			</select>
			</div>
		<div class="col-sm-2">
			<label class="control-label req_lbl_h" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Service Type:</label>
		</div>
		<div class="col-sm-4">
		<select id="Service_Type" class="req_inp" name="Service_Type" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
			<option value="" select disabled>Select Service Type</option>
			<option value="sensor">Sensor</option>
			<option value="Fog Service">Fog Service</option>
			<option value="Cloud">Cloud</option>
			<option value="Device">Device</option>
		</select>

		</div>
		</div>
		<!----------------------Deployable & URI--------------------->
		<div class="row form-group">
		<div class="col-sm-2">
		<label class="control-label req_lbl_h" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Deployable:</label>
		</div>
		<div class="col-sm-4">
		<select id="Deployable" class="req_inp" name="Deployable" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		<option value="" select disabled>Select option</option>
		<option value="deployable">Deployable</option>
		<option value="non_deployable">Non Deployable</option>
		</select>
		</div>
		<div class="col-sm-2">
		<label class="control-label req_lbl_h" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">URI:</label>
		</div>
		<div class="col-sm-4">
		<input type="text" class="req_inp" name="URI" id="URI" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		</div>

		<!-----------------------Software I/O/T---------------------->
		<div class="row form-group">
		<div class="col-sm-2">
		<label class="control-label req_lbl_h" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Service Input:</label>
		</div>
		<div class="col-sm-10">
		<input type="text" class="req_inp" name="Service_Input" id="Service_Input" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		</div>
		<div class="row form-group">
		<div class="col-sm-2">
		<label class="control-label req_lbl_h" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Service Output:</label>
		</div>
		<div class="col-sm-10">
		<input type="text" class="req_inp" id="Service_Output" name="Service_Output" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		</div>
		<!----------------------Location--------------------->
		<div class="row form-group">
		<div class="col-sm-8">
		<label class="control-label req_lbl_h" style="position:relative;font-size: 19px;margin-left: 200px;">Location</label>
		</div>
		</div>

		<div class="row form-group">
		<div class="col-sm-2">
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Latitude:</label>
		</div>
		<div class="col-sm-4">
		<input type="text" class="req_inp" id="Latitude" name="Latitude" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		<div class="col-sm-2">
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Longitude:</label>
		</div>
		<div class="col-sm-4">
		<input type="text" class="req_inp" id="Longitude" name="Longitude" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
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
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Value:</label>
		</div>
		<div class="col-sm-4">
		<input type="text" class="req_inp" name="val_radius" id="val_radius" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		<div class="col-sm-2">
		<label class="control-label" for="unit_radius" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Unit:</label>
		</div>
		<div class="col-sm-4">
		<select id="unit_radius" class="req_inp" name="unit_radius" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		<option value="" select disabled>Select Unit</option>
		<option value="meters">Meters</option>
		</select>
		</div>
		</div>

		<!-----------------------Availability---------------------->

		<div class="row form-group">
		<div class="col-sm-8">
		<label class="control-label req_lbl_h" style="position:relative;font-size: 19px;margin-left: 200px;">Availability</label>
		</div>
		</div>
		<div class="row form-group">
		<div class="col-sm-2">
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Value:</label>
		</div>
		<div class="col-sm-4">
		<input type="text" class="req_inp" id="val_avail" name="Value" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		<div class="col-sm-2">
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Unit:</label>
		</div>
		<div class="col-sm-4">
		<select id="unit_avail" class="req_inp" name="unit_avail" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		<option value="" select disabled>Select Unit</option>
		<option value="mintus">mintus</option>
		<option value="second">second</option>
		</select>
		</div>
		</div>


		<!-----------------------Hardware Requirement---------------------->


		<div class="row form-group">
		<div class="col-sm-10">
		<label class="control-label req_lbl_h" style="position:relative;font-size: 19px;margin-left: 188px;">Hardware Requirements</label>
		</div>
		</div>

		<div class="row form-group">
		<div class="col-sm-3">
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify;">Ext.Storage Value:</label>
		</div>
		<div class="col-sm-3">
		<input type="text" class="req_inp" name="val_ext" id="val_ext" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		<div class="col-sm-3">
		<label class="control-label" style="position:relative; top:-9px; margin-bottom: .5rem;text-align: justify; margin-right: 7px;">Ext.Storage Unit:</label>
		</div>
		<div class="col-sm-3">
		<select id="unit_ext" class="req_inp" name="unit_ext" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		<option value="" select disabled>Select Unit</option>
		<option value="GB">GB</option>
		<option value="MB">MB</option>
		</select>
		</div>
		</div>

		<div class="row form-group">
		<div class="col-sm-3">

		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Ram Value:</label>
		</div>
		<div class="col-sm-3">
		<input type="text" class="req_inp" name="val_ram" id="val_ram" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		<div class="col-sm-3">
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Ram Unit:</label>
		</div>
		<div class="col-sm-3">
		<select id="unit_ram" class="req_inp" name="unit_ram" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		<option value="" select disabled>Select Unit</option>
		<option value="GB">GB</option>
		<option value="MB">MB</option>
		</select>
		</div>
		</div>


		<div class="row form-group">
		<div class="col-sm-2">
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">CPU:</label>
		</div>
		<div class="col-sm-4">
		<input type="text" class="req_inp" id="CPU" name="CPU" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		<div class="col-sm-2">
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Network:</label>
		</div>
		<div class="col-sm-4">
		<input type="text" class="req_inp" id="Network" name="Network" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		</div>

		<!-----------------------Software Requirements---------------------->

		<div class="row form-group">
		<div class="col-sm-10">
		<label class="control-label req_lbl_h" style="position:relative;font-size: 19px;margin-left: 188px;">Software Requirements</label>
		</div>
		</div>
		<div class="row form-group">
		<div class="col-sm-2">
		<label class="control-label" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">OS:</label>
		</div>
		<div class="col-sm-4">
		<select id="OS" name="OS" class="req_inp" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		<option value="" select disabled>Select OS</option>
		<option value="raspbian">Raspbian</option>
		<option value="linux">Linux</option>
		<option value="unix">Unix</option>
		</select>
		</div>
		<div class="col-sm-2">
		<label class="control-label" id="Package_List" style="position:relative; top:-9px; display: inline-block; margin-bottom: .5rem;text-align: justify">Package List:</label>
		<label style="font-size: 10px; font-weight: normal" for="Package_List"> (separate packages using comma (,))</label>

		</div>
		<div class="col-sm-4">
		<input type="text" class="req_inp" name="Package_List" style="font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
		</div>
		</div>



		<div class="modal-footer">
		<button id="req_submit_button" value="Submit" name="add" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-disk"></span> Save</a>
		<button type="button" class="btn btn-default" data-dismiss="modal" onclick="document.getElementById('myModal').style.display='none'"><span class="glyphicon glyphicon-remove"></span> Cancel</button>
		</div>
		</div>
		</div>
		</div>

		</form>
	</div>
</div>

<!--interactions-->
<div id="interaction_div" class="container-fluid">
    <div class="row" style= "height: 45px;">
        <h2 id="set_int_tag" align="center">Set Interactions</h2><br /><br />
        <div id="int_modal" class="modal">
            <!-- Modal content -->
            <div class="int_modal-content">
                <div class="modal-header">
                    <span class="close_int_modal">&times;</span>
                    <p class="int_heading">Interactions</p>
                </div>
                <div class="int-modal-body">
                    <p id="int_p">Enter Interaction for following activities:</p>

                    <div class="row" style="text-align:center">
                        <label>Set interactions</label>
                        <label>
                            <input id="int_toggle" type="checkbox" data-toggle="toggle" data-onstyle="primary" data-on="Yes" data-off="No" checked>
                        </label>
                    </div>
                    <p id="Gamma_1" style="text-align:center"></p>
                    <p id="Gamma_2" style="text-align:center"></p>

                    <form name="interaction_form" id="int_form">

                        <!-----------------------QOS---------------------->
                        <table class="int_table">
                            <tr>
                                <td></td>
                                <td colspan="2">
                                    <label id="QOS" class="int_label_heading lbl_h">Quality of Service</label>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>
                                    <label  id="Latency" class="lbl2">Latency</label>

                                </td>
                                <td colspan="2">
                                    <label class="int_label" for="val_latency">Value</label>
                                    <input class="int_inp" type="text" id="val_latency" name="val_latency">
                                </td>
                                <td>
                                    <label class="int_label" for="unit_latency">Unit</label>
                                    <input class="int_inp" type="text" id="unit_latency" name="unit_latency">
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <label id="Bandwidth" class="lbl2" >Bandwidth</label>
                                </td>
                                <td colspan="2">
                                    <label class="int_label" for="val_bandwidth">Value</label>
                                    <input class="int_inp" type="text" id="val_bandwidth" name="val_bandwidth">
                                </td>
                                <td>
                                    <label class="int_label" for="unit_bandwidth">Unit</label>
                                    <input class="int_inp" type="text" id="unit_bandwidth" name="unit_bandwidth">
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <label id="Sampling_Rate" class="lbl2" >Sampling Rate</label>
                                </td>
                                <td colspan="2">
                                    <label class="int_label" for="val_sampling_rate">Value</label>
                                    <input class="int_inp" type="text" id="val_sampling_rate" name="val_sampling_rate">
                                </td>
                                <td>
                                    <label class="int_label" for="unit_sampling_rate">Unit</label>
                                    <input class="int_inp" type="text" id="unit_sampling_rate" name="unit_sampling_rate">
                                </td>
                            </tr>
                            <!-----------------------IAT---------------------->
                            <tr>
                                <td></td>
                                <td colspan="2">
                                    <label class="int_label_heading lbl_h" id="Inter_Interval_Time">Inter Arrival Time</label>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <label style="width: 20%;" class="int_label" for="val_iat">Value</label>
                                    <input class="int_inp" type="text" id="val_iat" name="val_iat"></td>
                                <td colspan="2">
                                    <label style="width: 20%;" class="int_label" for="unit_iat">Unit</label>
                                    <input class="int_inp" type="text" id="unit_iat" name="unit_iat">
                                </td>
                            </tr>
                            <!-----------------------MESSAGE---------------------->
                            <tr>
                                <td></td>
                                <td colspan="2">
                                    <label class="int_label_heading lbl_h" id="Message">Message</label>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <label class="int_label" for="Sent">Sent</label>
                                    <input class="int_inp" type="text" id="Sent" name="Sent">
                                </td>
                                <td colspan="2">
                                    <label class="int_label" for="Received">Received</label>
                                    <input class="int_inp" type="text" id="Received" name="Received">
                                </td>
                            </tr>
                            <!-----------------------ADDRESS---------------------->
                            <tr>
                                <td></td>
                                <td colspan="2"><label class="int_label_heading">URI of Activities</label><br></td>
                                <td></td>
                            </tr>
                            <tr class="none">
                                <td colspan="2">
                                    <label id="g1" class="int_label lbl_h" for="Gamma_1_Address">URI 1</label>
                                    <input class="int_inp" type="text" id="Gamma_1_Address" name="Gamma_1_Address">
                                </td>
                                <td colspan="2">
                                    <label id="g2" class="int_label lbl_h" for="Gamma_2_Address">URI 2</label>
                                    <input class="int_inp" type="text" id="Gamma_2_Address" name="Gamma_2_Address">
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-primary" id="int_submit_button" type="button" value="Submit">Submit</button>
                </div>
            </div>
        </div>
    </div>
</div>
		</form >

<br><br>

<!--create new or upload existing model-->
<div>
    <form id="save_model_form" action="save_model.php">
        <input type="hidden" id="save_model_inp" name="save_model_inp">
    </form>
</div>

<div id="file_div" class="container-fluid">
    <div class="row">
        <div id="fileModal" class="file_modal">
            <!-- Modal content -->
            <div class="file_modal-content fadeInDown">
                <div class="modal-body">
                    <div id="img_div">
                        <img src="hdd_icon.png" id="file_img">
                    </div>
                    <h4 align="center" style="font-weight: bold">Upload Existing Model or Create New One</h4>
                    <label class="file_upload">
                        <input type="file" id="file_inp" accept=".json"  onchange='upload_model(event)'>
                        Upload Existing
                    </label>
                    <label class="create_new">
                        <input type="button" onclick="create_new_model()">
                        Create New
                    </label>
                    <div hidden id="file_content"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--worfklow canvas-->
	<div id="paper1"></div>	
</body>

<script>
    $('#category').change(function () { //prevent adding "select service" from dropdown
        $('#add').prop("disabled", true);
    });
    $('#subcategory').change(function () { //prevent adding "select service" from dropdown
        $('#add').prop("disabled", true);
    });
    $('#service').change(function () { //prevent adding "select service" from dropdown
        $('#add').prop("disabled", false);
    });
    $('#addblock_menu_cat').change(function () { //prevent adding "select service" from dropdown
        $('#addblock_popup_button').prop("disabled", true);
    });
    $('#addblock_menu_subcat').change(function () { //prevent adding "select service" from dropdown
        $('#addblock_popup_button').prop("disabled", true);
    });
	
    var paper = new Raphael('paper1',2000,1000); //setting background of workflow

    delete_button_set = paper.set(); //set for delete button
    req_button_set = paper.set(); //set for requirement button

    paper.canvas.style.backgroundColor = '#F5F5F5';
	
	
	    Tipped.create('#SetInstructionsLogo', function (element) {
        return {
            title: "Set Instructions",
            content: "Set Instructions"
        };
    }, {
        position: 'top', behavior: 'hide', hideOthers: true
    });
	
		
	    Tipped.create('#createWorkFlowLogo', function (element) {
        return {
            title: "Create Workflow",
            content: "Create WorkFlow"
        };
    }, {
        position: 'top', behavior: 'hide', hideOthers: true
    });
	
	    Tipped.create('#CreateNodeBtn', function (element) {
        return {
            title: "Add",
            content: "Add service"
        };
    }, {
        position: 'right', behavior: 'hide', hideOthers: true
    })
	Tipped.create('#DataBase', function (element) {
        return {
            title: "Show models Library",
            content: "View Library of user defined models"
        };
    }, {
        position: 'right', behavior: 'hide', hideOthers: true
    })
	Tipped.create('#addBlockLogo', function (element) {
        return {
            title: "Add",
            content: "Add Service in Block"
        };
    }, {
        position: 'right', behavior: 'hide', hideOthers: true
    })
	Tipped.create('#CopyServiceLogo', function (element) {
        return {
            title: "Copy",
            content: "Copy Service"
        };
    }, {
        position: 'right', behavior: 'hide', hideOthers: true
    })
	Tipped.create('#SaveNodesBtn', function (element) {
        return {
            title: " Save",
            content: "Save model"
        };
    }, {
        position: 'right', behavior: 'hide', hideOthers: true
    })
	Tipped.create('#ClearBtn', function (element) {
        return {
            title: "Clear",
            content: "Clear the entire canvas"
        };
    }, {
        position: 'right', behavior: 'hide', hideOthers: true
    })
		Tipped.create('#HelpButton', function (element) {
        return {
            title: "Help",
            content: "Open the manual for this editor"
        };
    }, {
        position: 'right', behavior: 'hide', hideOthers: true
    })
	 Tipped.create('.function22', function (element) {
        return {
            title: $(element).data('title'),
            content: $(element).data('content')
        };
    }, {
        position: 'top', behavior: 'hide', hideOthers: true
    });
	  
</script>
	
</html>