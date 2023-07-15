<!-- edit New -->
<div class="modal fade" id="edit_<?php echo $row->attributes()->id ; ?>" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 113%;">
            <div class="modal-header">
                
                <center><h4 class="modal-title" id="myModalLabel" style="padding-left: 11.2rem;">Edit Service</h4></center>
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
			<div class="container-fluid">
			<form method="POST" action="edit.php">
			
			<div class="row form-group">
					<div class="col-sm-8">
						<label class="control-label" style="position:relative;font-size: 19px;margin-left: 188px; width: 139px;">Service Detail</label>
					</div>
				</div>
			    <div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Service ID:</label>
					</div>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="Service_ID" value="<?php echo $row->attributes()->id; ?>" readonly>
					</div>
				</div>
			
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Service Name:</label>
					</div>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="Service_Name" value="<?php echo ucwords(str_replace('_', ' ', $row->Service_Name)); ?>" readonly>
					</div>
				</div>
				
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Service Category:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control"  name="Service_Category" value="<?php echo $row->Service_Category; ?>">
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Service Type:</label>
					</div>
					<div class="col-sm-4">
						<select id="Service_Type"  name="Service_Type" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						<option><label ><?php echo $row->Service_Type;?></label></option>
						 <option value="Sensor">Sensor</option>
						 <option value="Auction">Auction</option>
						 <option value="Cloud">Cloud</option>
						 <option value="Device">Device</option>
					  </select>
						
					</div>
				</div>
				
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Deployable:</label>
					</div>
					<div class="col-sm-4">
						<select id="Deployable"  name="Deployable" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						 <option value="<?php echo $row->Deployable; ?>">Deployable</option>
						<option value="<?php echo $row->Deployable; ?>">Non Deployable</option>
					  </select>
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">URI:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="URI" value="<?php echo $row->URI; ?>">
					</div>
				</div>			
				
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Service Input:</label>
					</div>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="Service_Input" value="<?php echo $row->Service_Input; ?>">
					</div>
				</div>
				
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Service Output:</label>
					</div>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="Service_Output" value="<?php echo $row->Service_Output; ?>">
					</div>
				</div>
				
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
						<input type="text" class="form-control" name="Latitude" value="<?php echo $row->Location->Latitude; ?>">
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Longitude:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="Longitude" value="<?php echo $row->Location->Longitude; ?>">
					</div>
				</div>
				
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
						<input type="text" class="form-control"  name="val_radius" value="<?php echo $row->Location->Radius->Value; ?>">
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Unit:</label>
					</div>
					<div class="col-sm-4">
						<select id="unit_radius"  name="unit_radius" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						<option><label ><?php echo $row->Location->Radius->Unit; ?></label></option>
						 <option  value="meter">meter</option>
						 <option value="millimeters">millimeters</option>
					  </select>
					</div>
				</div>
				
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
						<input type="text" class="form-control" name="val_avail" value="<?php echo $row->Availability->Value; ?>">
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Unit:</label>
					</div>
					<div class="col-sm-4">
						<select id="unit_avail"  name="unit_avail" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						<option><label ><?php echo $row->Availability->Unit; ?></label></option>
						 <option value="mintus">mintus</option>
						 <option value="second">second</option>
					  </select>
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
						<input type="text" class="form-control" name="val_ext" value="<?php echo $row->Hardware_Requirements->external_storage->Value; ?>">
					</div>
					<div class="col-sm-3">
						<label class="control-label" style="position:relative; top:7px;">Ext. Storage Unit:</label>
					</div>
					<div class="col-sm-3">
						<select id="unit_ext"  name="unit_ext" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						<option><label ><?php echo $row->Hardware_Requirements->external_storage->Unit; ?></label></option>
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
						<input type="text" class="form-control" name="val_ram" value="<?php echo $row->Hardware_Requirements->RAM->Value; ?>">
					</div>
					<div class="col-sm-3">
						<label class="control-label" style="position:relative; top:7px;">Ram Unit:</label>
					</div>
					<div class="col-sm-3">
						<select id="unit_ram"  name="unit_ram" style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						<option><label ><?php echo $row->Hardware_Requirements->RAM->Unit; ?></label></option>
						 <option value="GB">GB</option>
						 <option value="MB">MB</option>
					  </select>
					</div>
				</div>
				
				<div class="row form-group">
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">CPU:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="CPU" value="<?php echo $row->Hardware_Requirements->CPU; ?>">
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Network:</label>
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="Network" value="<?php echo $row->Hardware_Requirements->Network; ?>">
					</div>
				</div>
					
				
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
						<select id="OS"  name="OS"  style=" font-size: 14px; line-height: 1.42857143;color: #555; background-color: #fff;background-image: none;border: 1px solid #ccc;border-radius: 4px; width: 100%;height: 34px;">
						<option><label ><?php echo $row->Software_Requirements->OS; ?></label></option>
						 <option value="Raspbian">Raspbian</option>
						 <option value="Linux">Linux</option>
						 <option value="Unix">Unix</option>
					  </select>
					</div>
					<div class="col-sm-2">
						<label class="control-label" style="position:relative; top:7px;">Package List:</label>
						<label style="font-size: 10px; font-weight: normal" for="Package_List"> (separate packages using comma (,))</label>

					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="Package_List" value="<?php 
						foreach($row->Software_Requirements-> Package_List as $Package_List){
							$count=count($Package_List);
							for($i=0;$i<$count;$i++){
								if($i==0){
								echo $Package_List->Package[$i];
								}else{
									echo ",";
									echo $Package_List->Package[$i];
								}//else end
							}//end of for loop
							} //end of foreach
							?>">
					</div>
				</div>
			
            </div> 
			</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span> Cancel</button>
                <button type="submit" name="edit" class="btn btn-success"><span class="glyphicon glyphicon-check"></span> Update</a>
			</form>
            </div>

        </div>
    </div>
</div><!-- end of edit -->


<!-- Delete -->
<div class="modal fade" id="delete_<?php echo $row->attributes()->id ; ?>" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
               
                <center><h4 class="modal-title" id="myModalLabel" style="padding-left: 9.2rem;">Delete Member</h4></center>
				 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">	
            	<p class="text-center">Are you sure you want to Delete</p>
				<h2 class="text-center"><?php echo ucwords(str_replace('_', ' ', $row->Service_Name)); ?></h2>
			</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span> Cancel</button>
                <a href="delete.php?id=<?php echo $row->attributes()->id ; ?>" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> Yes</a>
            </div>

        </div>
    </div>
</div>