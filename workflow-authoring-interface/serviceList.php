<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Services List </title>
    
	<link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <!-- Custom styles for this template  
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.css">  -->
  <link href="css/simple-sidebar.css" rel="stylesheet">
  
  <style>
  
.bg-light {
    background-color: #e6f2ff!important;
}
.glyphicon-filter{content:"\e138";}

	
</style>
</head>

<body>
 <div class="d-flex" id="wrapper">

    <!-- Sidebar -->
    <div class="bg-light border-right" id="sidebar-wrapper">
      <div class="sidebar-heading"><a href="index.php"/><img src="images/dashboradTrans.png" href="index.php"></div>
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
              <a class="nav-link" href="Service_Registration.php"><img style="height:25px;" src="images/add.png">Service Registration</a>
            </li>
            <li class="nav-item">
             <a class="nav-link" href="ServiceList.php"><img  style="height:20px;" src="images/search.png"><span class="sr-only">(current)</span>View Service</a>
            </li>
			<li class="nav-item">
             <a class="nav-link" href="Home.php"><img  style="height:25px;" src="images/processTrans.png">Create Process</a>
            </li>
			<li class="nav-item active">
              <a class="nav-link" href="index.php"><img src="images/Capturetransp.png"></a>
            </li>
          </ul>
        </div>
      </nav>



<div class="container-fluid">
    <h1 class="page-header text-center" style=" margin-top: 10px; margin-right: 200px;">Services List</h1>
    <div class="row" style="    justify-content: center;">
        <div class="col-sm-8 col-sm-offset-2">
           <!-- <a href="#addnew" class="btn btn-primary" data-toggle="modal"><span class="glyphicon glyphicon-plus"></span> New</a>>-->
            <?php 
                session_start();
                if(isset($_SESSION['message'])){
                    ?>
                    <div class="alert alert-info text-center" style="margin-top:20px;">
                        <?php echo $_SESSION['message']; ?>
                    </div>
                    <?php

                    unset($_SESSION['message']);
                }
				 //load xml file
                    $file = simplexml_load_file('registration.xml');

					$category = array();
					foreach($file->Gamma as $row)
					{
						$category[]=$row->Service_Category ;
						
					}
					$cat=array_unique($category);
					$length=count($cat);
					
					//var_dump($length);
            ?>
            <table  id="myTable" class="table table-bordered table-striped" style="margin-top:20px;">
                <thead>
				    <th style="width:14%;">Service Id</th>
                    <th style="width:15%;">Service Name</th>
                    <th style="width:21%;">Service Category
					</th>
                    <th style="width:18%;">Service Type<img src="images/filter.png" style="height:30px;"></th>
                    <th style="width:14%;">Deployable</th>
					<th>URI</th>
                    <th style="width:467%;">Action</th>
                </thead>
                <tbody>
                    <?php
					//$selectedOption=$_POST['category'];
			        // var_dump($selectedOption);
					
                   foreach($file->Gamma as $row){
					   
                        ?>
                        <tr>
						<td><?php  echo $row->attributes()->id;?></td>
                            <td><?php echo ucwords(str_replace('_', ' ', $row->Service_Name)); ?></td>
                            <td><?php echo $row->Service_Category; ?></td>
                            <td><?php echo $row->Service_Type; ?></td>
                            <td><?php echo $row->Deployable; ?></td>
							<td><?php echo $row->URI; ?> </td>
                            <td>
                                <a href="#edit_<?php echo $row->attributes()->id ; ?>" data-toggle="modal" class="btn btn-success btn-sm"><span class="glyphicon glyphicon-edit"></span> Edit</a>
                                <a href="#delete_<?php echo $row->attributes()->id  ; ?>" data-toggle="modal" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-trash"></span> Delete</a>
                            </td>
                            <?php include('edit_delete_modal.php'); ?>
                        </tr>
                        <?php
							} 
				   ?>
                </tbody>
            </table>
        </div>
    </div>
</div>
 </div>
    <!-- /#page-content-wrapper -->

  </div>
  <!-- /#wrapper -->

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

function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
  
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        //openDropdown.classList.remove('show');
      }
    }
  }
}
 

  </script>
<script src="ddtf.js"></script>
<script >
$('#myTable').ddTableFilter();

</script>
<script src="jquery.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</body>
</html>