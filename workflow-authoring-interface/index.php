<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
 
  

  <title>Cloud and Edge Computing Environment</title>

  <!-- Bootstrap core CSS -->
  <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <!-- Custom styles for this template -->
  <link href="css/simple-sidebar.css" rel="stylesheet">
 
</head>
<style>
.bg-light {
    background-color: #e6f2ff!important;
}

</style>
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
              <a class="nav-link" href="Service_Registration.php"><img style="height:25px;" src="images/add.png">Service Registration</a>
            </li>
            <li class="nav-item">
             <a class="nav-link" href="ServiceList.php"><img  style="height:20px;" src="images/search.png">View Service</a>
            </li>
			<li class="nav-item">
             <a class="nav-link" href="Home.php"><img  style="height:25px;" src="images/processTrans.png">Create Process</a>
            </li>
			<li class="nav-item active">
              <a class="nav-link" href="index.php"><img src="images/Capturetransp.png"><span class="sr-only">(current)</span></a>
            </li>
          </ul>
        </div>
      </nav>

      <div class="container-fluid" style=" justify-content: center; padding-left: 150px;">
        <h2 class="mt-4" >Composition and Management of Applications in the Cloud and Edge Computing Environment</h2>
        <p style=" justify-content: center;">Support rapid development and deployment of next generation Internet-centered distributed applications that are autonomous, cooperative, adaptive, evolvable, emergent and trustworthy.</p>
		
		<img src="images/demo.png" alt="example" style=" justify-content: center; padding-left:192px;">
        <!--<p>Make sure to keep all page content within the <code>#page-content-wrapper</code>. The top navbar is optional, and just for demonstration. Just create an element with the <code>#menu-toggle</code> ID which will toggle the menu when clicked.</p>-->
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
  </script>

</body>

</html>
