


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

    var xml = '<?xml version="1.0"?>\n' +
            '<Workflow>\n'; //XML Code

    /*following commented out code contains variables and data for autofill*/
//    var req_element = [];//for auto fill
//    var int_element = [];//for auto fill
//    int_element.push("50","msec","1","Mbps","10","samples per second","50","msec", "http://schemas.opengis.net/sos/2.0/sosGetObservation.xsd#request","http://schemas.opengis.net/sos/2.0/sosGetObservation.xsd#response","uri1","uri2");
//
//    req_element.push("weather","X","Y","8","meters","8","minutes","1.2 GHZ quad-core ARM Cortex A53 (ARMv8 Instruction Set)","10/100 MBPS Ethernet, 802.11n Wireless LAN, Bluetooth 4.0","4","Gb","8","Gb","Rasbian","Java, .Net, Ruby, MySQL","http://schemas.opengis.net/sos/2.0/sosGetObservation.xsd#request","http://schemas.opengis.net/sos/2.0/sosGetObservation.xsd#response","sensor","non_deployable","U");

    //variable declaration
    var counter = 50;
    var redraw_counter = 50;
    var rect1, del_sign, del_button, req_sign, req_button, arrow,txt,txt_wrap, con_id, set1; //paper objects
    var sets_array = []; //array to store sets of [rectangle,button,arrow,text,id]
    var counter_array = []; //track counter
    var text_array = []; //track text of boxes
	var id_array=[];//track gamma id of boxes
    var holder_text_array = []; //hold boxes on y-axis
    var requirement_array = []; //final array of requirements
    var requirement_map = []; //array per requirement
    var interaction_array = []; //final array of interactions
    var interaction_map = []; //array per interaction
    var interaction_pair_array = []; //pairs of interacting activities/boxes
    var holder_interaction_pair_array = []; //holds pairs of interacting activities/boxes
    var block_array = []; //array of current block of INSERTED boxes
    var holder_block_array = []; //holds all INSERTED boxes
    var block_array_ba = [];
    var connected_id = []; //array to hold same IDs of beforeAfter boxes
    var bef_aft_array = []; //array to hold before/after of services
    var id = 0; //box tracker
    var translate = 0; //y-axis
    var counts = {}; //frequency count
    var selectedValueText; //for accessing dropdown values
    var bef_aft = 0;
    var copy_manager = 0; //copy list 1 manager
    var copy_manager2 = 0;//copy list 2 manager

var paper;
var modal_upload;
 var Gamma;
	 var length;
        //console.log("inp: "+inp);
	fetch('registration.xml')
    .then((response) => response.text())
    .then(function(textResponse) {
	var parser = new DOMParser();
    var xmlDoc = parser.parseFromString(textResponse,"text/xml"); 
	Gamma=xmlDoc.getElementsByTagName('Gamma');
	length=Object.keys(Gamma).length;
	console.log('response is ', Gamma);
	//console.log('response is ', length);
	//var id=xmlDoc.getElementsByTagName('Gamma')[0].id;
    console.log(Gamma);
	});
  /*  var paper = new Raphael('paper1',50000,5000); //setting background of workflow

    delete_button_set = paper.set(); //set for delete button
    req_button_set = paper.set(); //set for requirement button

    paper.canvas.style.backgroundColor = '#F5F5F5';*/


    function get_text_from_list(opt1){ //parameter to check if add or insert box
        //getting text from dropdown

        var select_activity;
        if(opt1 == 0) { //ADD new box
            select_activity = document.getElementById("service");
        } 

        else{ //copied service box
            select_activity = document.getElementById("copy_menu_activity");
            selectedValueText = select_activity.options[select_activity.selectedIndex].text; //get selected service
            selectedValueText = selectedValueText.concat("_{c}");
            return selectedValueText;
        }

        selectedValueText = select_activity.options[select_activity.selectedIndex].text;
        return selectedValueText;
    }
	function get_id_from_list(opt1){ //parameter to check if add or insert box
        //getting text from dropdown

         var select_activityID;
        if(opt1 == 0) { //ADD new box
           
        select_activityID= document.getElementById("service").value;
		console.log("select_activityID:"+select_activityID);
        }
		else if (opt1 == 1){
			  select_activityID=  block_array[0];
			console.log("select_activityID:"+select_activityID);
		}
        //selectedValueText = select_activity.options[select_activity.selectedIndex].text;
        return select_activityID;
    }

	
	function add_service(){

		   var modal_a = document.getElementById("add_service");
			var span = document.getElementsByClassName("close_add_service_popup")[0];
			var copy = document.getElementById('add_button');
			var lbl = document.getElementById('r_included');
			

			lbl.style.display = "none";
			modal_a.style.display = "block";
			$("#copy_before_after").show();

			span.onclick = function () { //if close (cross) button pressed, close popup
            modal_a.style.display = "none";
        };
		
			var closex = document.getElementById("closeModal");
			closex.onclick = function () { //if close (cross) button pressed, close popup
				modal_a.style.display = "none";
        };
		
		var add = document.getElementById("add");
		add.onclick = function (){
					create_box(0,0);

		}
			
	}

	function clearPaper(){
		if (document.getElementById("paper1").children[0].childNodes.length == 0) {
			ShowError("Create new graph or load already saved graph first.");
			return;
		}
		
		document.getElementById("paper1").innerHTML = ""
		paper = new Raphael('paper1',1700,1000); //setting background of workflow
		delete_button_set = paper.set(); //set for delete button
		req_button_set = paper.set(); //set for requirement button
		paper.canvas.style.backgroundColor = '#F5F5F5';
		id = 0;
		counter = 50;
		var length = text_array.length;
		for(var i=0; i<= length; i++){
				text_array.pop();	
		}
	}
	

    function create_box(check,caller){
        //prevent page reload
        $("#flowchart").submit(function(e) {
            e.preventDefault();
        });

        var loop_iterator = 0;
        var str = '';

        if(check !=0){ //redrawing

            if(block_array.length != 0){ //if add block added
                var x = block_array.length -1;
                text_array.splice(id, 1); //remove block from text array

                if(bef_aft != 0){
                    text_array.splice(bef_aft, 1);
                    bef_aft = bef_aft + 1;
                }

                while(x >= 0){
                    text_array.splice(id,0,block_array[x]); //add services in block to text array
                    requirement_array.splice(id,0,"");
                    if(bef_aft != 0){
                        text_array.splice(bef_aft,0,block_array_ba[x]);
                        requirement_array.splice(bef_aft,0,"");
                        bef_aft = bef_aft + 1;
                    }
                    x--;
                }

                var yz = bef_aft_array.indexOf("beforeAfter");

                if(bef_aft_array[yz] == 'beforeAfter'){ //set services added before as 'before'
                    while(bef_aft_array[yz] == "beforeAfter"){
                        bef_aft_array[yz] = 'before';
                        yz++;
                    }
                    yz = bef_aft_array.indexOf("beforeAfter");
                    while(bef_aft_array[yz] == "beforeAfter"){ //set services added after as 'after'
                        bef_aft_array[yz] = 'after';
                        yz++;
                    }
                }
		
		//added by m

				if(block_array.length > 0){
					
				for(var a=0; a< block_array.length; a++){
					var sname;
					var sval;
					
					for(var q=0; q < addblock_menu.length; q++){
						
						strId = block_array[a];
						var val = document.getElementById("addblock_menu").children[q].value;
						var name = document.getElementById("addblock_menu").children[q].text;
						if(strId == name)
						{						
							sname = name;
							sval = val;
						}
					
				}						
				id_array.push(sval +","+ sname);
					}
				}		
				else{
					strId = block_array[0];
					var sname;
					var addblock_menuLenght = document.getElementById("addblock_menu").children.length;
					console.log(addblock_menuLenght)
					for(var q=0; q < addblock_menu.length; q++){
						
						var val = document.getElementById("addblock_menu").children[q].value;
						var name = document.getElementById("addblock_menu").children[q].text;
						if(strId == name)
						{						
							sname = strId;
							strId = val;
						}
						
					}						
					id_array.push(strId +","+ sname);
				}							
			

                bef_aft = 0;
                holder_block_array.push.apply(holder_block_array,block_array);
                holder_block_array.push.apply(holder_block_array,block_array_ba);
            }
            else if(caller !=2){ //if redraw not called from delete function
                holder_block_array.push(text_array[id]);
            }
        }
        else{ //draw new box
            if(caller == 0) {
                str = get_text_from_list(0);
				console.log('service name:'+str);
			strId = get_id_from_list(0);
            }
            else{
                str = get_text_from_list(3);
				strId = get_id_from_list(3);
            }
            if (str == null) {
                return;
            }
            if (text_array.includes(str)) { //if service already exists
                var service_num = 2;
                while (text_array.includes(str)) {
                    if(str.includes("{c}")){//service copied
                        str = str.split("c}");
                        str = str[0].concat("c} {") + service_num.toString().concat('}');
                    }
                    else { //service with same name added
                        str = str.split(' {');
                        str = str[0].concat(' {') + service_num.toString().concat('}');
                    }
                    service_num++;
                }
            }
            text_array.push(str);
			id_array.push(strId+","+ str);
            requirement_array.push("");
        }

        do{
            if (check == -1){ //no box needs to be drawn
                break;
            }
            if(bef_aft_array[id] == null){
                bef_aft_array[id] = "";
            }
            if(connected_id[id] == null){
                connected_id[id] = "";
            }
            var cd;
            var ce;
            var frq_cnt = { };

            //update indexes in connected id array
            for(x = connected_id.length-1; x >= 0; x--){
                if(connected_id[x] !== "") {
                    for (cd = 0, ce = x+1; cd < ce; cd++) {
                        frq_cnt[connected_id[cd]] = (frq_cnt[connected_id[cd]] || 0) + 1;
                    }
                    if(frq_cnt[connected_id[x]] == 2) {
                        var hold_con_id = connected_id[x];
                        connected_id[x] = "x";
                        var dupl = connected_id.indexOf(hold_con_id);
                        connected_id[x] = dupl;
                        connected_id[dupl] = dupl;
                    }
                }
                frq_cnt = { };
            }

            var occur = { };
            var j;
            for (i = 0, j = counter_array.length; i < j; i++) {
                occur[counter_array[i]] = (occur[counter_array[i]] || 0) + 1;
            }
            counts = occur;
            var da = text_array[id];
            var la = 0;

            if(caller == 0){ //new box added on row 0
                translate = 0;
            }
            else {
                if (holder_text_array.length != 0) { //if multiple rows exist
                    while (la < holder_text_array.length) {
                        if (holder_text_array[la][0] == da) {
                            if (id == 0 && caller != 2) {//if it is not 1st box and not called from delete function
                                for (var p = la; p < holder_text_array.length; p++) { //move boxes to next column
                                    if(caller == 3 && p == 0){
                                        //not to move column for copied service as first box
                                        continue;
                                    }
                                    if (block_array.includes(holder_text_array[p][0])) {
                                        //not to move column for recently added block of services
                                        continue;
                                    }
                                    //move boxes to next column //update counter of boxes
                                    var c_ha = holder_text_array[p][1];
                                    holder_text_array[p].splice(1, 1, (c_ha + 191));
                                }
                            }

                            var freq = 0;
                            freq = counts[holder_text_array[la][1]]; //frequency object
                            var hta_c = holder_text_array[la][1]; //counter of hta service

                            if (!(counter_array.includes(hta_c))) {//beginning of new column so row 0
                                translate = 0;
                            }
                            else { // Nth row
                                translate = freq * 100; //set translate on y-axis
                            }

                            holder_text_array[la].splice(2, 1, translate); //update translate
                            translate = holder_text_array[la][2]; //set translate for drawing
                            counter = holder_text_array[la][1]; //set counter for drawing

                            if (typeof freq == 'undefined') {
                                if (holder_text_array[la][0].includes("{r}")) {
                                    break;
                                }
                                //remove elements from hta if a column is moved back
                                holder_text_array.splice(la, 1);
                                holder_block_array.splice(holder_block_array.indexOf(da), 1);
                            }
                            break;
                        }
                        else {
                            translate = 0;
                        }
                        la++;
                    }
                }
            }
            if(caller == 3 && translate == 0){ //copied service on row 0
                if(holder_block_array.includes(text_array[id])) { //remove element from hba
                    holder_block_array.splice(holder_block_array.indexOf(text_array[id]), 1);
                }
            }

            var occur2 = { };
            var it = 0;
            var jt = 0;
            for (it = 0, jt = connected_id.length; it < jt; it++) {
                occur2[connected_id[it]] = (occur2[connected_id[it]] || 0) + 1;
            }
            if(translate == 0 && occur2[id] == 1){ //if box on row 0 is not added before & after
                bef_aft_array[id] = "";
            }
            if(text_array.length == 0){ //hide addblock option if no service currently added
                $('#addblock').hide();
                $('#save_model').hide();
                $('#final_model').hide();
            }
            else{
                $('#addblock').show();
                $('#save_model').show();
                if(text_array.length > 1){ //copy service and interaction option service > 1
                    $('#final_model').show();
                    $('#copy_popup_button').show();
                }
                else{
                    $('#copy_popup_button').hide();
                }
            }
            //draw rectangle
            rect1 = paper.rect(counter, 250, 140, 65, 10).attr({fill: "270-#98D5FF:5-#D7EEFF:100"});
            //creating delete button
            del_button = paper.circle((counter + 138), 250, 7).attr({fill: "red"});
            del_sign = paper.path(["M" + (counter + 135) + ",250 h7"]).attr({"stroke-width": 3});
            delete_button_set.push(del_button);
            delete_button_set.push(del_sign);
            delete_button_set.attr({cursor: "pointer"});
            del_button.hide();
            del_sign.hide();

            //creating requirement button
            req_button = paper.circle((counter + 120), 250, 7).attr({fill: "white"});
            req_sign = paper.path(["M" + (counter + 117) + ",248 h7 v2,2 H"+(counter+117)+"v-2,-3"]).attr({"stroke-width": 2});

            req_button_set.push(req_button);
            req_button_set.push(req_sign);
            req_button_set.attr({cursor: "pointer"});
            req_button.hide();
            req_sign.hide();

            if(counter == 50 && translate == 0){//not to draw arrow for first box
                arrow = paper.path([""]);
            }
            if(counter != 50 && translate == 0) { //draw arrow for row 0
                if(text_array[id].includes("{c}")){
                    bef_aft_array[id] = "";
                }
                arrow = paper.path(["M" + (counter-51) + ",285 h50"]);
                arrow.attr({
                    "arrow-end": "classic-wide-long"
                });
            }
            else{ //draw arrow for y-axis boxes
                if(id != 0 && bef_aft_array[id] =='before') {
                    arrow = paper.path(["M" + (counter + 140) + ",285 h25 v-"+translate]);
                }
                else if(translate !=0){
                    arrow = paper.path(["M" + (counter) + ",285 h-25 v-"+translate]);
                    arrow.attr({
                        "arrow-start": "classic-wide-long"
                    });
                }
            }
            //----------------------------------------------------TEXT
            txt = paper.text((counter + 10), 280);
            txt.attr({"font-size": 18, "text-anchor": 'start'});

            //divide text to fit within box
            var maxWidth = 127;
            var words;
            if(check == 0) {
                words = str.split("");
            }
            else{
                words = text_array[id].split("");
            }
            var tempText = "";
            for (var i=0; i<words.length; i++) {
                txt.attr("text", tempText + words[i]);
                if (txt.getBBox().width > maxWidth) {
                    if(txt.getBBox().width > maxWidth) {
                        tempText += "\n" + words[i];
                    }
                }
                else {
                    tempText += words[i];
                }
            }
            txt_wrap = txt.attr("text", tempText.substring(0));
            paper.customAttributes.number = function(num){ //to assign same secondary id to before/ after boxes and copied boxes
                return {"number": num};
            };
            con_id = paper.path().attr({number: connected_id[id]}); //set secondary id
            draw_hover_button(); //create specification and deletion buttons over boxes

            set1 = paper.set(rect1, del_button, del_sign, req_button, req_sign, arrow,txt_wrap,con_id).data('id', id); //set of paper objects
            sets_array.splice(id,0,set1); //add set to array of sets
            counter_array.push(counter); //add counter to array of counters

            block_array.shift(); //remove element from block array
            if(holder_block_array.includes(text_array[id])){ //Nth row
                sets_array[id].animate({transform:'t-30,'+translate},500); //display box
            }
            else
            { //row 0
                sets_array[id].animate({transform: "t-30,0"}, 500);//display box
            }
            counter = counter+191; //increment counter
            id++;//increment id
            counts = {};
            loop_iterator++;
        }while(loop_iterator < check);

        for(var n = 0; n < holder_text_array.length; n++){ //remove duplicates
            if(!(text_array.includes(holder_text_array[n][0]))){
                holder_block_array.splice(holder_block_array.indexOf(holder_text_array[n][0]), 1);
                holder_text_array.splice(n, 1);
                n--;
            }
        }
        if(text_array.length == 1){
            $('#final_model').hide();
        }
		//MARIA COMMENT THIS LINE TILL TESTING
		       /*     modal_p.style.display = "none"; *///hide popup

//        $('.req_inp').val('hello');
    }

    function del_manager(conc_c,which_one_to_remove,ind){
        var ind_h = []; //array to find minimum index for redrawing boxes
        var min;
        var conc_r;
        if(ind != -1){ //copied box called for deletion
            conc_c = conc_c.split("_{c}_{c}");

            if(text_array.includes(conc_c[0].concat(" {r}"))){ //if original service has copy {r}
                conc_r = conc_c[0].concat(" {r}");
                remove_box([text_array.indexOf(conc_r), 2]); //remove copy {r}
            }
            conc_c = conc_c[0].concat("_{c}");
            ind_h.push(ind);
            remove_box([ind, 2]); //remove copy {c}
        }

        for(var i = text_array.length-1; i >= 0; i--) { //remove all copied services
            if (text_array[i].includes(conc_c)) {
                ind_h.push(text_array.indexOf(text_array[i]));
                remove_box([text_array.indexOf(text_array[i]), 2]);
            }
        }

        if(ind == -1){ //remove original box
            conc_c = conc_c.split("_{c}");
            which_one_to_remove = text_array.indexOf(conc_c[0]);
            ind_h.push(which_one_to_remove);
            remove_box([which_one_to_remove, 2]);
        }

        min = Math.min.apply(Math, ind_h);
        id = min; //set id to minimum to redraw boxes from minimum index
        min = text_array.length - min;
        return min;

    }

    //drawing delete button on rectangles
    function delete_button_func(){
        delete_button_set.click(function(){

            var which_one_to_remove = this.data("id"); //id of element to be removed
            if(which_one_to_remove == null){
                return;
            }

            var spl  = "";
            var conc;
            var conc_c;
            var min = 0;
            var ind_spl = 0;
            conc = text_array[which_one_to_remove].concat(" {r}"); //copy {r}
            conc_c = text_array[which_one_to_remove].concat("_{c}"); //copy {c}

            if(text_array[which_one_to_remove].includes("{r}")){ //copied box from before/after clicked for deletion
                spl = text_array[which_one_to_remove].split(" {r");
                ind_spl = text_array.indexOf(spl[0]);

                if(text_array.includes(spl[0].concat("_{c}"))){ //if copied service exists
                    conc_c = spl[0].concat("_{c}_{c}");
                    ind_spl = text_array.indexOf(spl[0]);
                    min = del_manager(conc_c,-1,ind_spl);
                    create_box(min,2);
                }
                else{
                    remove_box([which_one_to_remove, 2]); //id of copied (after) service to be removed
                    remove_box([ind_spl, 0]); //id of original service to be removed
                }
            }
            else if(text_array.includes(conc)){ //original box clicked for deletion if copy {r} exists

                ind_spl = text_array.indexOf(conc);
                remove_box([ind_spl, 2]); //id of copied (after) service to be removed
                if(text_array.includes(conc_c)){ //if copy {c} exists too
                    min = del_manager(conc_c,which_one_to_remove,-1);
                    create_box(min,2);
                }
                else {
                    remove_box([which_one_to_remove, 0]); //id of original service to be removed
                }
            }
            else if(text_array.includes(conc_c)){ //original box clicked for deletion if copy {c} exists but not copy {r}
                min = del_manager(conc_c,which_one_to_remove,-1);
                create_box(min,2);
            }
            else{
                remove_box([which_one_to_remove, 0]); //id of original service to be removed
            }
        });
    }

  function requirement_button_func(){
req_button_set.click(function(){
		var serviceId="";
var box_id = this.data("id"); //id of selected box of specification
console.log('box_id: '+box_id);
debugger;
var sn = document.getElementById('Service_Name');
sn.innerHTML = text_array[box_id];
console.log("service name in input :"+text_array[box_id]); /*save user from setting specifications of copied services
* user just requires to set specifications of original service*/
//var categ = document.getElementById('Category');
//var cat = categ.options[categ.selectedIndex].text;
// console.log("category name in input :"+cat);
for(var a=0; a < id_array.length; a++){ // loop to get right serviceId 
	
	var words= [];
	words=id_array[a].split(",");//splits the string based on string
	
	if(words[1] == sn.innerHTML)
	{
		 serviceId=words[0];
	}
}

if(sn.innerHTML.includes("{r}") || sn.innerHTML.includes("{c}")){
var spl;
if(sn.innerHTML.includes("{r}")){
spl = sn.innerHTML.split(" {r}");
}
else{
spl = sn.innerHTML.split("_{c}");
}
if(requirement_array[text_array.indexOf(spl[0])]!= ""){ //tells user that specs are already set for original service
Swal.fire({
title: 'Specifications already set!',
text: 'Specifications already set from original service of '+spl[0],
icon: 'info'
});
return;
}
else { //tells user that specs for copied box are to be set by original service
Swal.fire({
title: 'Projected Service of '+ spl[0] +'!',
text: 'Set specifications through original service of ' + spl[0],
icon: 'info'
});
return;
}
}
$('.req_inp').val('');
requirements(box_id,serviceId);
});
}

//zarmina code
//var xml=false;
    function requirements(box_id,serviceId){ //set specifications in popup
    debugger;

     var inp = document.getElementsByClassName("req_inp"); //get all specs input fields
	 //var len=inp.length;
	 console.log("inp :"+inp);

var sName, sCategory,sLatitude,slongitude,runit,rvalue,sInput,sOutput,sUri,sType,deployable,avalue,aunit,cpu,network,ramValue,ramUnit,exValue,exUnit,os, myVar1;
var packList=[];
        for(var i=0;i<length;i++){
			if(Gamma[i].id == serviceId){
				xml=true;
				// sName=	Gamma[i].innerHTML ;
				 var childLength=Gamma[i].children.length
				 for(var j=0;j<childLength;j++){
					 if(Gamma[i].children[0]==Gamma[i].children[j]){
					 sName=Gamma[i].children[j].innerHTML;
					 //console.log(sName);
					 }
					else if(Gamma[i].children[1]==Gamma[i].children[j]){
					 sCategory=Gamma[i].children[j].innerHTML;
					  //console.log(sCategory);
					 }
					 else if(Gamma[i].children[2]==Gamma[i].children[j]){
						 var LOCLength=Gamma[i].children[2].children.length
						  for(var k=0;k<LOCLength;k++){
							if( Gamma[i].children[2].children[0]==Gamma[i].children[j].children[k]){
								sLatitude=Gamma[i].children[j].children[k].innerHTML;
								//console.log(sLatitude);
							}else if( Gamma[i].children[2].children[1]==Gamma[i].children[j].children[k]){
							  slongitude=Gamma[i].children[j].children[k].innerHTML;
								//console.log(sLatitude);
						  }
						 else if(Gamma[i].children[2].children[2]==Gamma[i].children[j].children[k]){
							  var rlength=Gamma[i].children[2].children[2].children.length;
							  for(var c=0 ;c<rlength;c++){
								  if(Gamma[i].children[2].children[2].children[0]==Gamma[i].children[2].children[2].children[c]){
									  rvalue=Gamma[i].children[2].children[2].children[c].innerHTML;
									  //console.log(rvalue);
								  }
								  else if(Gamma[i].children[2].children[2].children[1]==Gamma[i].children[2].children[2].children[c]){
									  runit=Gamma[i].children[2].children[2].children[c].innerHTML;
									 // console.log(runit);
								  }
								  
							  }
						  }
						 
					 }
					 }
					 else if(Gamma[i].children[3]==Gamma[i].children[j]){
						 var alength=Gamma[i].children[3].children.length;
						 for(var d=0;d<alength;d++){
							 if(Gamma[i].children[3].children[0]==Gamma[i].children[3].children[d]){
								 avalue=Gamma[i].children[3].children[d].innerHTML;
								 //console.log(avalue);
							 }else if(Gamma[i].children[3].children[1]==Gamma[i].children[3].children[d]){
								 aunit=Gamma[i].children[3].children[d].innerHTML;
								// console.log(aunit);
							 }
						 }
					 }
					 else if(Gamma[i].children[4]==Gamma[i].children[j]){
						 var hardlength=Gamma[i].children[4].children.length;
						 for(var f=0;f<hardlength;f++){
							 if(Gamma[i].children[4].children[0]==Gamma[i].children[4].children[f]){
								 cpu=Gamma[i].children[4].children[f].innerHTML;
								 //console.log(cpu);
							 }else if(Gamma[i].children[4].children[1]==Gamma[i].children[4].children[f]){
								 network=Gamma[i].children[4].children[f].innerHTML;
								 //console.log(network);
							 }else if(Gamma[i].children[4].children[2]==Gamma[i].children[4].children[f]){
								 var ramLength=Gamma[i].children[4].children[2].children.length;
								 for(var l=0;l<ramLength;l++){
									 if(Gamma[i].children[4].children[2].children[0]==Gamma[i].children[4].children[2].children[l]){
									 ramValue=Gamma[i].children[4].children[2].children[l].innerHTML;
									 //console.log(ramValue);
									 }
									 else if(Gamma[i].children[4].children[2].children[1]==Gamma[i].children[4].children[2].children[l]){
										 ramUnit=Gamma[i].children[4].children[2].children[l].innerHTML;
									// console.log(ramUnit);
									 }
								 }
							 }else if(Gamma[i].children[4].children[3]==Gamma[i].children[4].children[f]){
								 var exlength=Gamma[i].children[4].children[3].children.length;
								// console.log("exlength is:"+exlength);
								 for(var m=0;m<exlength;m++){
									if(Gamma[i].children[4].children[3].children[0]==Gamma[i].children[4].children[3].children[m]){
									 exValue=Gamma[i].children[4].children[3].children[m].innerHTML;
									 //console.log(exValue);
									 }
									 else if(Gamma[i].children[4].children[3].children[1]==Gamma[i].children[4].children[3].children[m]){
										 exUnit=Gamma[i].children[4].children[3].children[m].innerHTML;
									// console.log(exUnit);
									 } 
								 }
							 }
							 
							
						 }
					 }
					 else if(Gamma[i].children[5]==Gamma[i].children[j]){
						 var softlength=Gamma[i].children[5].children.length;
						 for(var n=0;n<softlength;n++){
							 if(Gamma[i].children[5].children[0]==Gamma[i].children[5].children[n]){
								 os=Gamma[i].children[5].children[n].innerHTML;
								 //console.log(os);
							 }else if(Gamma[i].children[5].children[1]==Gamma[i].children[5].children[n]){
								 packlength=Gamma[i].children[5].children[1].children.length;
								// console.log(packlength);
								 for(var b=0;b<packlength;b++){
									packList[b]= Gamma[i].children[5].children[1].children[b].innerHTML;
									 //console.log(packList);
								 }
								 myVar1 = packList.join(); 
							 }
							 
						 }
					 }
					 else if(Gamma[i].children[6]==Gamma[i].children[j]){
					 sInput=Gamma[i].children[j].innerHTML;
					// console.log(sInput);
					 }
					else if(Gamma[i].children[7]==Gamma[i].children[j]){
					 sOutput=Gamma[i].children[j].innerHTML;
					// console.log(sOutput);
					 }
					 else if(Gamma[i].children[8]==Gamma[i].children[j]){
					 sType=Gamma[i].children[j].innerHTML;
					 //console.log(sType);
					 }
					 else if(Gamma[i].children[9]==Gamma[i].children[j]){
					 deployable=Gamma[i].children[j].innerHTML;
					// console.log(deployable);
					 }
					 else if(Gamma[i].children[10]==Gamma[i].children[j]){
					 sUri=Gamma[i].children[j].innerHTML;
					// console.log(sUri);
					 }
				 }
			     
			   
	console.log(sName, sCategory,sLatitude,slongitude,runit,rvalue,sInput,sOutput,sUri,sType,deployable,avalue,aunit,cpu,network,ramValue,ramUnit,exValue,exUnit,os,myVar1);
	}//enf of ouuter if
	 
	}//end of i loop
			
		          
        
		  var el="";
        if(requirement_array[box_id] != null){ //fill requirement fields with pre-entered data if it's present
//var el;
            for(var f = 0; f < requirement_array[box_id].length;f++){
                el = requirement_array[box_id][f][1]; //access 1st element of inner requirement array
               console.log("el: "+el);
                if(typeof inp[f] == 'undefined'){
                    continue;
                }
                inp[f].value = el; //fill value of input fields with previously entered data
            }
			
		
			
	}
	
	if(requirement_array[box_id] != null){
		if(xml==true){
			xml = '<?xml version="1.0"?>\n' +
            '<Workflow>\n';;
			
			for(var s = 0; s < inp.length; s++){
		
				if(s==0){
					   el = sCategory;
			           console.log("el: "+el);
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				sCategory="";
				}
				else if(s==1){
					el=sType;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
					sType="";
				}
				else if(s==2){
					el=deployable;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				deployable="";
				}
				else if(s==3){
					el=sUri;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				sUri="";
				}
				else if(s==4){
					el=sInput;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				sInput="";
				}
				else if(s==5){
					el=sOutput;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
					sOutput="";
				}
				else if(s==6){
					el=sLatitude;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				sLatitude="";
				}
				else if(s==7){
					el=slongitude;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				slongitude="";
				}
				else if(s==8){
					el=rvalue;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				rvalue="";
				}
				else if(s==9){
					el=runit;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				runit="";
				}
				else if(s==10){
					el=avalue;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				avalue="";
				}
				else if(s==11){
					el=aunit;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				aunit="";
				}
				else if(s==12){
					el=exValue;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				exValue="";
				}
				else if(s==13){
					el=exUnit;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				exUnit="";
				}
				else if(s==14){
					el=ramValue;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				ramValue="";
				}
				else if(s==15){
					el=ramUnit;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				ramUnit="";
				}
				else if(s==16){
					el=cpu;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				cpu="";
				}
				else if(s==17){
					el=network;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				network="";
				}
				else if(s==18){
					el=os;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				os="";
				}
				else if(s==19){
					el=myVar1;
					if(typeof inp[s] == 'undefined'){
                    continue;
                }
				inp[s].value = el;
				myVar1="";
				}
				
			}//end of for loop
		//	xml=false; //comment by maria
	}//end of flag xml
	}//end of if
			
			//zarmina code end
			
			
        /*------following commented out code is for auto-fill specification input fields------*/
//        for (var p = 0; p < 20; p++) {
//            inp[p].value = req_element[p]; //fill value of input fields with previously entered data
//            if(box_id == 2 || box_id == 4 || box_id == 7) {
//                if (p == 0) {
//                    inp[p].value = "fire";
//                }
//                if (p == 5) {
//                    inp[p].value = "5";
//                }
//                if (p == 13) {
//                    inp[p].value = "Linux";
//                }
//            }
//        }
        var requirement_button = document.getElementById("req_submit_button");
        var modal = document.getElementById("myModal");
        modal.style.display = "block";
        requirement_button.onclick = function(){ //submit button of specs pressed
		
            var terminate = 0; //iterator for all input fields of specs
            $(".req_inp").each(function(i) { //check for any missing spec field
			debugger;
                var val = document.getElementsByClassName("control-label");
                var element = $(this); //current field
                var msg = val[i].innerHTML; //name of spec
                var tag = element.attr('name'); //tag of spec to store against value
                if (element.val() == "") { //generate error for missing spec
                    Swal.fire({title: 'Data Missing for '+msg+'!', text: 'Fill '+msg+' field', icon: 'warning'});
                    inp[i].style.backgroundColor = "yellow"; //highlight missing field
                    inp[i].addEventListener("keypress", function () { //undo highlight upon typing
                        inp[i].style.backgroundColor = "white"
                    });
                    //prevent closing popup once submit is pressed to make sure no spec is missed
                    var span = document.getElementsByClassName("close_modal")[0];
                    span.onclick = function () {
                        modal.style.display = "block";
                    };

                    window.onclick = function (event) {
                        if (event.target == modal) {
                            modal.style.display = "block";
                        }
                    };
                    return false;
                }
                else{
                    terminate++; //move to next spec
                    if(tag == 'val_radius' || tag == 'val_avail' || tag == 'val_ram'
                            || tag == 'val_ext'){ //rename tag for value tag
                        tag = 'Value';
                    }
                    if(tag == 'unit_radius' || tag == 'unit_avail' || tag == 'unit_ram'
                            || tag == 'unit_ext'){ //rename tag for unit tag
                        tag = 'Unit';
                    }
					
                    requirement_map.push([tag,element.val()]); //store in map till all fields filled
                }
                if(terminate == 20){ //close popup
                    modal.style.display = "none";
                    return;
                }
                inp[terminate].focus(); //move cursor to highlighted missing field
            });
            requirement_array[box_id] = requirement_map; //add all specs of a service to array
            requirement_map = []; //empty map for next service specs
        };
        var span = document.getElementsByClassName("close_modal")[0];
        span.onclick = function () {
            modal.style.display = "none";
        };
        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        };
    }
 
    function draw_hover_button(){ //display and hide specification and delete buttons on boxes
        (function (rect1, del_button, del_sign, req_button, req_sign, txt_wrap) {
            rect1.hover(function () { //show both buttons on box hover
                        del_button.show();
                        del_sign.show();
                        req_button.show();
                        req_sign.show();
                    },
                    function () { //hide both buttons
                        del_button.hide();
                        del_sign.hide();
                        req_button.hide();
                        req_sign.hide();
                    });
            txt_wrap.hover(function () { //show both buttons on text hover
                        del_button.show();
                        del_sign.show();
                        req_button.show();
                        req_sign.show();
                    },
                    function () {
                        del_button.hide();
                        del_sign.hide();
                        req_button.hide();
                        req_sign.hide();
                    });

            del_button.hover(function () { //show only delete button if cursor moved over delete button
                        del_button.show();
                        del_sign.show();
                        delete_button_func();
                    },
                    function () {
                        del_button.hide();
                        del_sign.hide();
                    });
            del_sign.hover(function () { //show only delete button if cursor moved over delete sign
                        del_button.show();
                        del_sign.show();
                        delete_button_func();
                    },
                    function () {
                        del_button.hide();
                        del_sign.hide();
                    });

            req_button.hover(function () { //show only req button if cursor moved over req button
                        req_button.show();
                        req_sign.show();
                        requirement_button_func();
                    },
                    function () {
                        req_button.hide();
                        req_sign.hide();
                    });
            req_sign.hover(function () { //show only req button if cursor moved over req sign
                        req_button.show();
                        req_sign.show();
                        requirement_button_func();
                    },
                    function () {
                        req_button.hide();
                        req_sign.hide();
                    });
        })(rect1, del_button, del_sign, req_button, req_sign, txt_wrap);
    }

    function remove_box(removing){ //deletion of box
        var remover = removing[0]; //id of box to be removed
        var i = sets_array.length-1; //total length of existing boxes on canvas
        var j = 0;
        redraw_counter = counter_array[remover]; //counter of box to be removed
        var occur = { };
        var x;
        var y;
        for (x = 0, y = counter_array.length; x < y; x++) { //find frequency of boxes along y-axis
            occur[counter_array[x]] = (occur[counter_array[x]] || 0) + 1;
        }

        while(i >= remover) { //remove boxes starting from end of canvas
            sets_array[i].remove();
            sets_array.pop();
            counter_array.pop();
            i--;
        }

        var countarray2 = {};
        for (var a = 0; a < counter_array.length; a++) { //recalculate frequency of boxes along y-axis
            var num2 = counter_array[a];
            countarray2[num2] = countarray2[num2] ? countarray2[num2] + 1 : 1;
        }

        if(removing[1] != 1) { //not redraw
            var s = countarray2[redraw_counter];
            if (typeof s == 'undefined') {
                s = 0;
            }
            var m;
            for (m = 0; m < holder_text_array.length; m++) { //reducing counter for hta elements to previous column
                if (redraw_counter < holder_text_array[m][1]) {
                    if(occur[redraw_counter] < 2) {
                        var ch = holder_text_array[m][1];
                        holder_text_array[m].splice(1, 1, (ch - 191));
                    }
                }
            }

            for (m = 0; m < holder_text_array.length; m++) { //updating counter for redrawing row 0 elements
                if (s > 0 && (!(holder_text_array[m].includes(text_array[remover + 1]))) && removing[1]!=2) {
                    redraw_counter = redraw_counter + 191;
                    break;
                }
            }
            if(removing[1] == 2){
                if(remover == 0 || counter_array.length == 0){ //first box
                    redraw_counter = 50;
                }
                else {
                    redraw_counter = counter_array[counter_array.length - 1]+191; //last box
                }
            }
            //remove element from all arrays
            text_array.splice(remover, 1);
            bef_aft_array.splice(remover, 1);
            requirement_array.splice(remover, 1);
            connected_id.splice(remover, 1);
            counter = redraw_counter; //set counter
        }

        id = remover; //set id to place of removed box
        j = text_array.length - remover; //set number of boxes to be redrawn

        if(j == 0){ //set j = -1 if no new box is to be drawn
            j = -1;
        }
        if(removing[1] != 0){ //do not call create_box func() if deletion called from redrawing func
            return;
        }
        create_box(j,2);
    }

    function copy_service(){ 
		if(document.getElementById("paper1").children[0].childNodes.length >= 18){
	
	//setup for copying service
        var modal_p = document.getElementById("copy_service");
        var span = document.getElementsByClassName("close_copy_service_popup")[0];
        var copy = document.getElementById('copy_button');
        var lbl = document.getElementById('r_included');

        lbl.style.display = "none";
        modal_p.style.display = "block";
        $("#copy_before_after").show();
        copy_service_menu(); //dynamically draw list 1 of service to be copied
        addblock_activity_menu(1); //dynamically draw list 2 of existing service

        var service_dropdown = document.getElementById('copy_menu2_activity');

        $('#copy_menu2_activity').change(function () { //changing option of list 2
            var service_dd = service_dropdown.options[service_dropdown.selectedIndex].text;
            if(service_dd.includes("{r}")){ //add service only AFTER copy {r} if it is on row 0
                lbl.innerHTML = "after";
                lbl.style.display = "block"; //show label
                $("#copy_before_after").hide();
            }
            else{
                lbl.style.display = "none";
                lbl.innerHTML = "before";
                $("#copy_before_after").show();
            }
        });
        copy.onclick = function () {
            modal_p.style.display = "none"; //hide popup

            var copy_dropdown = document.getElementById('copy_menu_activity');
            var service_dropdown = document.getElementById('copy_menu2_activity');
            var before_after = document.getElementById('copy_before_after');
            var copy_dd = copy_dropdown.options[copy_dropdown.selectedIndex].text;
            var service_dd = service_dropdown.options[service_dropdown.selectedIndex].text;

            if(lbl.innerHTML == 'after'){ //add service only AFTER copy {r} if it is on row 0
                before_after.value = "after";
            }
            var id_h = ''; //temporary id
            var lastind = false;
            var c_ind = 0;
            var i,j;
            /*set id along y-axis for service after boxes
             * it has to check how many boxes already exist in column and sets id accordingly*/
            if(before_after.value == 'after'){
                id_h = text_array.indexOf(service_dd);
                c_ind = counter_array[id_h] + 191; //next counter
                var c_hold = [];
                c_hold.push.apply(c_hold, counter_array);

                if(counter_array.includes(c_ind)){
                    var occur = { };
                    for (i = 0, j = counter_array.length; i < j; i++) {
                        occur[counter_array[i]] = (occur[counter_array[i]] || 0) + 1;
                    }
                    id_h = id_h + occur[(c_ind-191)] + occur[c_ind]; //set id after existing boxes
                }
                else{//box has to be created at end of workflow
                    id_h = id;
                    lastind = true;
                }
            }
            else{
                id_h = text_array.indexOf(service_dd);
            }

            if (text_array.includes(copy_dd)) { //update service number if it already exists
                var service_num = 2;
                while (text_array.includes(copy_dd)) {
                    if (copy_dd.includes("{c}")){
                        copy_dd = copy_dd.split("_{c}");
                        copy_dd = copy_dd[0].concat("_{c} {") + service_num.toString().concat('}');
                    }
                    else
                    {
                        copy_dd = copy_dd.concat("_{c}");
                        service_num--;
                    }
                    service_num++;
                }
            }

            var ch = 0;
            text_array.splice(id_h,0,copy_dd); //add copy {c} service at specified index
            requirement_array.splice(id_h,0,"");

            if(id_h == 0) { //if box is first box of workflow
                ch = counter_array[id_h];
            }
            else if(before_after.value == 'after' && lastind == true){ //if it is last box
                ch = counter_array[id_h-1]+191;
            }
            else{ //box anywhere in middle on x/y-axis
                ch = counter_array[id_h-1];
            }

            id = id_h;
            holder_text_array.splice(0, 0, [copy_dd, ch, translate]); //add copied service to hta
            bef_aft_array.splice(id, 0, before_after.value); //add copied service to bef_aft_array
            translate = 0;
            if(before_after.value == 'after'){
                id_h = id_h - 1;
            }
            i = id_h;
            j = 0;
            while(i < text_array.length){
                j++;
                i++;
            }

            if(before_after.value == 'after'){
                j = j-1;
            }
            remove_box([id,1]); //remove boxes after id already on canvas as they will be redrawn
            create_box(j,3); //redraw boxes along with new box
        };

        span.onclick = function () { //if close (cross) button pressed, close popup
            modal_p.style.display = "none";
        };
        window.onclick = function (event) { //if anywhere else on screen pressed, close popup
            if (event.target == modal_p) {
                modal_p.style.display = "none";
                $('#copy_menu').find('option').prop('selected', function () { //re-set list value
                    return this.defaultSelected;
                });
            }
        };
		}else{ Swal.fire({title: 'Service Missing!', text: 'Two services should be exist on canvas', icon: 'warning'});
           }
	
    }

    function copy_service_menu() { //create list 1
        //Create the new dropdown menu
        var create_div = document.getElementById('adjust_copy_activity');
        var new_drop_down = document.createElement('select');
        new_drop_down.setAttribute('id',"copy_menu_activity");
        new_drop_down.setAttribute('class','form-control col-md-3');
        create_div.appendChild(new_drop_down);

        for (var i = 0; i < text_array.length; i++) {
            if(text_array[i].includes("{c}")){ //do not add projected service for re-copy
                continue;
            }
            if(text_array[i].includes("{r}")){//do not add projected service for re-copy
                continue;
            }
            var opt = document.createElement('option');
            opt.innerHTML = text_array[i];
            opt.value = text_array[i];
            new_drop_down.add(opt,new_drop_down.options[null]);
        }
        if (copy_manager == 1) { //to prevent creation of list again/ delete previous list
            removeCsDrop();
        }
        copy_manager = 1; //to call function to delete list
    }

    function addblock_box(){ //setup for addblock popup
	
	if(document.getElementById("paper1").children[0].childNodes.length > 2){
        var modal_p = document.getElementById("add_block_popup");
        var span = document.getElementsByClassName("close_addblock_popup")[0];
        modal_p.style.display = "block";

        span.onclick = function() {
            modal_p.style.display = "none";
            $('#addblock_finish').prop("disabled", true);
            $('#addblock_popup_button').prop("disabled", true);
        };
        window.onclick = function (event) {
            if (event.target == modal_p) {
                modal_p.style.display = "none";
                $('#addblock_finish').prop("disabled", true);
                $('#addblock_popup_button').prop("disabled", true);
                $('#addblock_menu').find('option').prop('selected', function() {
                    return this.defaultSelected;
                });
            }
        };
		
        addblock_activity_menu(0); //dynamically create list for existing services

        var popup_button = document.getElementById('addblock_popup_button');
        var addblock_dropdown = document.getElementById('addblock_menu_activity');
        var addblock_listbox = document.getElementById('addblock_multiple_menu');
        var before_after = document.getElementById('before_after');
        var lbl = document.getElementById('addblock_activity_lbl'); //fix existing service once added
        var lbl2 = document.getElementById('before_after_lbl'); //fix before/after once added

        $('#addblock_menu').change(function () { //prevent adding "select service" from dropdown
            $('#addblock_popup_button').prop("disabled", false);
        });
	
        lbl.style.display = "none";
        lbl2.style.display = "none";
        $('#addblock_menu_activity').show();
        $('#before_after').show();

        popup_button.onclick = function(){
            addblock_listbox_menu(); //create listbox of services being added to block
            for(var x = 0; x < addblock_listbox.length;x++){ //prevent re-adding service to same block
                var fl = 0;
                for(var y = 0; y < addblock_listbox.length; y++){
                    if(addblock_listbox.options[x].value == addblock_listbox.options[y].value){
                        if(fl == 1){
                            Swal.fire({title: 'Service already exists!', text: 'Select some other activity', icon: 'warning'});
                            block_array.splice(block_array.indexOf(addblock_listbox.options[y].value),1);
                            addblock_listbox.removeChild(addblock_listbox.options[y]);
                        }
                        fl = 1;
                    }
                }
            }

            if(addblock_listbox.length == 0){ //disable add button if listbox is empty
                $('#addblock_finish').prop("disabled", true);
            }
            else { //enable add button once a service is added to block
                $('#addblock_finish').prop("disabled", false);
            }
            //change label values to selected options after service is added to block
            lbl.innerHTML = addblock_dropdown.options[addblock_dropdown.selectedIndex].text;
            lbl2.innerHTML = before_after.options[before_after.selectedIndex].text;

            $('#addblock_menu_activity').hide(); //hide dropdowns for existing services
            $('#before_after').hide();
            lbl.style.display = "block";
            lbl2.style.display = "block";
            $("#ba_lbl").hide();
            $("#existing_lbl").hide();
            span.onclick = function () {
                modal_p.style.display = "block";
            };
            window.onclick = function (event) {
                if (event.target == modal_p) {
                    modal_p.style.display = "block";
                }
            };
        };
        var ab_finish_button = document.getElementById('addblock_finish');

        ab_finish_button.onclick = function() { //finish button clicked
            removeAbLbDrop(); //remove previous lists in popup
            var id_h = '';
            var lastind = false;
            var c_ind = 0;
            var both_h = 0;
            var both_hold = 0;
            /*set id along y-axis for service after boxes
             * it has to check how many boxes already exist in column and sets id accordingly*/
            if(before_after.value == 'after' || before_after.value == 'beforeAfter'){
                id_h = text_array.indexOf(lbl.textContent);
                c_ind = counter_array[id_h] + 191; //next counter

                var c_hold = [];
                c_hold.push.apply(c_hold, counter_array);
                if(counter_array.includes(c_ind)){
                    var occur = { };
                    for (i = 0, j = counter_array.length; i < j; i++) {
                        occur[counter_array[i]] = (occur[counter_array[i]] || 0) + 1;
                    }
                    id_h = id_h + occur[(c_ind-191)] + occur[c_ind];
                }
                if(lbl.innerHTML == text_array[text_array.length-1]){
                    id_h = id;
                    lastind = true;
                }
                if(before_after.value == 'beforeAfter'){
                    both_h = id_h;
                    both_hold = id_h  + block_array.length;
                    id_h = text_array.indexOf(lbl.textContent);
                    if(!(counter_array.includes(counter_array[text_array.indexOf(lbl.textContent)]+191))){ //if existing service is at end of workflow, sum text and block array lengths
                        both_h = text_array.length;
                        both_hold = text_array.length + block_array.length;
                    }
                }
            }
            else{
                id_h = text_array.indexOf(lbl.textContent);
            }
            var ch = 0;
            var ch_both = 0;
            var temp_array = []; //for modifiying same name of services in block_array
            temp_array.push.apply(temp_array,block_array);
            block_array_ba.push.apply(temp_array,block_array); //block array for before after

            var service_num = 2;
            for(var b = 0; b < block_array.length; b++) { //change service number if it already exists
                if (text_array.includes(block_array[b])) {
                    while(text_array.includes(block_array[b])) {
                        block_array[b] = block_array[b].split(' {');
                        block_array[b] = block_array[b][0].concat(' {') + service_num.toString().concat('}');
                        service_num++;
                    }
                }
                if(before_after.value == 'beforeAfter'){ //add {r} to service if its added both ways
                    block_array_ba[b] = block_array[b].concat(' {r}');
                }
                service_num = 2;
            }

            if(id_h == 0 && before_after.value != 'beforeAfter') { //1st one way box
                ch = counter_array[id_h];
            }
            else if(before_after.value == 'after' && lastind == true){ //box to be drawn on last index
                ch = counter_array[id_h-1]+191;
            }
            else{
                if(id_h == 0){
                    ch = counter_array[id_h];
                }
                else {
                    ch = counter_array[id_h - 1];
                }

                if(both_h != 0){
                    ch_both = counter_array[text_array.indexOf(lbl.textContent)]+191;
                }
            }
            if(both_h != 0){
                text_array.splice(both_h,0,block_array_ba);
            }
            text_array.splice(id_h,0,block_array); //add block to text array

            id = id_h; //set id location of box to be drawn
            bef_aft = both_h;
            var yz;
            var z;

            for(z = 0; z < text_array.length-1; z++){ //append "" to indexes of bef_aft_array
                if(bef_aft_array[z] == null){
                    bef_aft_array[z] = "";
                }
            }
            for(z = 0; z < text_array.length-1; z++){//append "" to indexes of bef_aft_array
                if(connected_id[z] == null){
                    connected_id[z] = "";
                }
            }
            if(before_after.value == 'beforeAfter'){ //remove extra box added by beforeAfter
                bef_aft_array.pop();
                connected_id.pop();
            }

            for(z = 0; z < text_array.length; z++) {
                if (Array.isArray(text_array[z])) {
                    for (var q = 0; q < text_array[z].length; q++) {//add elements for each service in block
                        bef_aft_array.splice(z, 0, before_after.value);
                        connected_id.splice(z, 0, "");
                    }
                    if(before_after.value == 'beforeAfter'){ //add elements for each (after) service in block
                        for(yz = 0; yz < text_array[z].length; yz++){
                            bef_aft_array.splice(both_hold,0,before_after.value);
                            connected_id.splice(both_hold,0,"");
                        }
                        break;
                    }
                }
            }

            for( var x = 0; x < block_array.length; x++){
                if(before_after.value == 'beforeAfter'){ //add elements for two way services in block
                    holder_text_array.splice(x, 0, [block_array[x], ch, translate]);
                    holder_text_array.splice((x+1), 0, [block_array_ba[x], ch_both, translate]);
                }
                else { //add elements for one way services in block
                    holder_text_array.splice(x, 0, [block_array[x], ch, translate]);
                }
            }
            translate = 0;

            if(before_after.value == 'after'){
                id_h = id_h - 1;
            }
            var i = id_h;
            var j = 0;
            while(i < text_array.length){
                j++;
                i++;
            }
            j = (j + (block_array.length))-1; //set number of boxes to be redrawn
            if(before_after.value == 'after'){
                j = j-1;
            }
            if(before_after.value == 'beforeAfter'){
                j = j + (block_array.length)-1;
            }

            $('#addblock_menu').find('option').prop('selected', function() {
                return this.defaultSelected;
            });
            modal_p.style.display = "none";
            $('#addblock_finish').prop("disabled", true);
            $('#addblock_popup_button').prop("disabled", true);

            remove_box([id,1]); //remove boxes after id already on canvas as they will be redrawn
            create_box(j,1);//redraw boxes along with new box
        };

        var ab_cancel_button = document.getElementById('addblock_cancel');
        ab_cancel_button.onclick = function() { //clear block array and lists if cancel button pressed
            removeAbLbDrop();
            block_array = [];
            block_array_ba = [];

            $('#addblock_menu').find('option').prop('selected', function() {
                return this.defaultSelected;
            });
            modal_p.style.display = "none";
            $('#addblock_finish').prop("disabled", true);
            $('#addblock_popup_button').prop("disabled", true);
        };
		}
		else{
			Swal.fire({title: 'Service Missing!', text: 'Atleats one service should be exist on canvas', icon: 'warning'});
            return;
		}
    }
    var addblock_manager = 0; //to call function to delete list
    function addblock_activity_menu(caller) { //to make dropdown of present activities for insertion
        //Create the new dynamic dropdown menu
        var create_div = "";
        if(caller == 0){ //if func() called from addblock
            create_div = document.getElementById('adjust_addblock_activity');
        }
        else{ //if func() called from copy service
            create_div = document.getElementById('adjust_copy_activity2');
        }
        var new_drop_down = document.createElement('select');
        if(caller== 0){
            new_drop_down.setAttribute('id',"addblock_menu_activity");
        }
        else{
            new_drop_down.setAttribute('id',"copy_menu2_activity");
        }
        new_drop_down.setAttribute('class','form-control col-md-3');
        create_div.appendChild(new_drop_down);

        for (var i = 0; i < text_array.length; i++) {
            if(holder_block_array.includes(text_array[i]) && (!text_array[i].includes("{r}"))){
                continue;
            }
            var opt = document.createElement('option');
            opt.innerHTML = text_array[i];
            opt.value = text_array[i];
            if(text_array[i].includes("{r}")){
                if(caller == 0){
                    continue;
                }
                var fl = 0;
                for(var j = 0; j < holder_text_array.length; j++){ //add {r} in row 0 to copy list 2
                    if(holder_text_array[j][0] == text_array[i] && holder_text_array[j][2] == 0){
                        fl = 1;
                        if(counter_array.includes(holder_text_array[j][1]+191)){
                            fl = 0;
                        }
                        break;
                    }
                }
                if(fl != 1){
                    continue;
                }
            }
            new_drop_down.add(opt,new_drop_down.options[null]);
        }

        if(caller == 0) {
            if (addblock_manager == 1) {

                removeAbDrop();
            }
            addblock_manager = 1;
        }
        else{
            if (copy_manager2 == 1) {

                removeCsDrop2();
            }
            copy_manager2 = 1;
        }

    }

    function addblock_listbox_menu(){ //create listbox of block
        var ab_menu = document.getElementById('addblock_menu');
        var addblock_listbox = document.getElementById('addblock_multiple_menu');
        var opt = document.createElement("option");
        addblock_listbox.options.add(opt);
        opt.text = ab_menu.options[ab_menu.selectedIndex].text;
        opt.value = ab_menu.options[ab_menu.selectedIndex].value;
        block_array.push(opt.text); //add selected service to block array
    }

    function removeAbDrop() { //remove previous dynamic dropdown list of addblock
        var oldmenu;
        var activity_remove;
        activity_remove = document.getElementById('adjust_addblock_activity');
        oldmenu = document.getElementById('addblock_menu_activity');
        activity_remove.removeChild(oldmenu);
        addblock_manager = 0;
    }

    function removeAbLbDrop(){
        $('#addblock_multiple_menu').empty();
    }

    function removeCsDrop() { //remove previous dynamic dropdown list 1 of copy service
        var oldmenu;
        var activity_remove;
        activity_remove = document.getElementById('adjust_copy_activity');
        oldmenu = document.getElementById('copy_menu_activity');
        activity_remove.removeChild(oldmenu);
        copy_manager = 0;
    }

    function removeCsDrop2() { //remove previous dynamic dropdown list 2 of copy service
        var oldmenu;
        var activity_remove;
        activity_remove = document.getElementById('adjust_copy_activity2');
        oldmenu = document.getElementById('copy_menu2_activity');
        activity_remove.removeChild(oldmenu);
        copy_manager2 = 0;
    }

   /* function requirements(box_id){ //set specifications in popup
        var inp = document.getElementsByClassName("req_inp"); //get all specs input fields
        if(requirement_array[box_id] != null){ //fill requirement fields with pre-entered data if it's present
            var el;
            for(var j = 0; j < requirement_array[box_id].length;j++){
                el = requirement_array[box_id][j][1]; //access 1st element of inner requirement array
                if(typeof inp[j] == 'undefined'){
                    continue;
                }
                inp[j].value = el; //fill value of input fields with previously entered data
            }
        }
        /*------following commented out code is for auto-fill specification input fields------*/
//        for (var p = 0; p < 20; p++) {
//            inp[p].value = req_element[p]; //fill value of input fields with previously entered data
//            if(box_id == 2 || box_id == 4 || box_id == 7) {
//                if (p == 0) {
//                    inp[p].value = "fire";
//                }
//                if (p == 5) {
//                    inp[p].value = "5";
//                }
//                if (p == 13) {
//                    inp[p].value = "Linux";
//                }
//            }
//        }
/*
        var requirement_button = document.getElementById("req_submit_button");
        var modal = document.getElementById("myModal");
        modal.style.display = "block";

        requirement_button.onclick = function(){ //submit button of specs pressed
            var terminate = 0; //iterator for all input fields of specs
            $(".req_inp").each(function(i) { //check for any missing spec field
                var val = document.getElementsByClassName("req_label");
                var element = $(this); //current field
                var msg = val[i].innerHTML; //name of spec
                var tag = element.attr('name'); //tag of spec to store against value
                if (element.val() == "") { //generate error for missing spec
                    Swal.fire({title: 'Data Missing for '+msg+'!', text: 'Fill '+msg+' field', icon: 'warning'});
                    inp[i].style.backgroundColor = "yellow"; //highlight missing field
                    inp[i].addEventListener("keypress", function () { //undo highlight upon typing
                        inp[i].style.backgroundColor = "white"
                    });
                    //prevent closing popup once submit is pressed to make sure no spec is missed
                    var span = document.getElementsByClassName("close_modal")[0];
                    span.onclick = function () {
                        modal.style.display = "block";
                    };

                    window.onclick = function (event) {
                        if (event.target == modal) {
                            modal.style.display = "block";
                        }
                    };
                    return false;
                }
                else{
                    terminate++; //move to next spec
                    if(tag == 'val_radius' || tag == 'val_avail' || tag == 'val_ram'
                            || tag == 'val_ext'){ //rename tag for value tag
                        tag = 'Value';
                    }
                    if(tag == 'unit_radius' || tag == 'unit_avail' || tag == 'unit_ram'
                            || tag == 'unit_ext'){ //rename tag for unit tag
                        tag = 'Unit';
                    }
                    requirement_map.push([tag,element.val()]); //store in map till all fields filled
                }
                if(terminate == 20){ //close popup
                    modal.style.display = "none";
                    return;
                }
                inp[terminate].focus(); //move cursor to highlighted missing field
            });
            requirement_array[box_id] = requirement_map; //add all specs of a service to array
            requirement_map = []; //empty map for next service specs
        };
        var span = document.getElementsByClassName("close_modal")[0];
        span.onclick = function () {
            modal.style.display = "none";
        };
        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        };
    }
    this function moves to interaction part of workflow and makes pairs of connected services
     * it highlights the pairs on hover
     * it sets ids of projected services equal to original service's id*/
    function finalize_model(){
		if(document.getElementById("paper1").children[0].childNodes.length > 2){
			   document.getElementById('CreateNodeBtn').disabled = 'true';
			   document.getElementById('addBlockLogo').disabled = 'true';
			   document.getElementById('CopyServiceLogo').disabled = 'true';

        var txt;
        for(var x = 0; x < requirement_array.length;x++){ //check no specifications are skipped
            if(requirement_array[x] == "" || requirement_array[x].length < 20){
                if(text_array[x].includes("{r}") || text_array[x].includes("{c}")){
                    continue; //do not give error for projected services
                }
                txt = text_array[x];
                Swal.fire({title: 'Specification Missing!', text: 'Fill specifications for '+ txt, icon: 'warning'}); //generate error message for missing specification
                return;
            }
        }
        Swal.fire({ //confirm moving to interaction part
            title: 'Are you sure?',
            text: "You won't be able to edit activities!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, set interactions!'
        }).then(function(result){
            if (result.value) {
                Swal.fire(
                        'Flowchart Saved!',
                        'You can add interactions now',
                        'success'
                );
                console.log('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~');
                var hta = [];
                var cta = [];
                var i;
                var j;
                hta.push.apply(hta,text_array);
                cta.push.apply(cta,counter_array);

                //below 2 for loops add all elements to hta
                for(i = 0; i < holder_text_array.length; i++){ //remove duplicates
                    var ind = text_array.indexOf(holder_text_array[i][0]);
                    text_array.splice(ind,1);
                    counter_array.splice(ind,1);
                }
                for(j = 0; j < text_array.length; j++){ //add elements missing in holder
                    holder_text_array.push([text_array[j],counter_array[j],0]);
                }
                text_array = [];
                counter_array = [];
                text_array.push.apply(text_array,hta);
                counter_array.push.apply(counter_array,cta);
                var temp_arr = []; //copy of hta without broken text

                holder_text_array.sort(function(a,b) { //sort array 1st by counter then by translate
                    return a[1] - b[1] || a[2] - b[2];
                });
                //replace hta elements with broken text elements in sets array
                for(var n = 0; n < sets_array.length; n++){
                    temp_arr[n] =  text_array[n];
                    holder_text_array[n].splice(0,1,sets_array[n][6].attr('text'));
                }

                var k;
                var l;
                //make pairs of connected services
                for(k = 0; k < holder_text_array.length-1; k++) { //gamma 1
//                    console.log('--K-- ', holder_text_array[k][0]);
                    //if row 0 includes copy {r}
                    if(holder_text_array[k][0].includes("{r}") && holder_text_array[k][2] == 0){
                        //if box is at last index / end of workflow then do not create pair for it
                        if(!counter_array.includes(holder_text_array[k][1]+191)){
                            continue;
                        }
                        var f = k;
                        while (bef_aft_array[f] != "") {//trace previous row 0 element
                            f--;
                        }
                        //make pair of copy {r} on row 0 and previous service is on row 0
                        interaction_pair_array.push([temp_arr[k], temp_arr[f]]);
                        holder_interaction_pair_array.push([holder_text_array[k][0], holder_text_array[f][0]]);
                        continue;
                    }
                    for (l = k + 1; l < holder_text_array.length; l++) {
//                        console.log('--L-- ', holder_text_array[l][0]);
                        //if services are 1 hop away
                        if ((holder_text_array[k][1] + 191) == holder_text_array[l][1] &&(bef_aft_array[k] == "" || bef_aft_array[k] == "before")) {
                            if (holder_text_array[l][2] == 0) { //if service 2 is on row 0
                                //if service 2 is copy {r}, make pair with service of next column
                                if (temp_arr[l].includes("{r}")) {
                                    var e = l+1;
                                    while (bef_aft_array[e] != "" && e < bef_aft_array.length) {
                                        e++;
                                    }
                                    if(e == bef_aft_array.length){
                                        e = l;
                                    }
                                    //make pair of 2 hop away services in row 0
                                    interaction_pair_array.push([temp_arr[k], temp_arr[e]]);
                                    holder_interaction_pair_array.push([holder_text_array[k][0], holder_text_array[e][0]]);
                                }
                                else{ //make pair of 1 hop away services on y-axis as well
                                    interaction_pair_array.push([temp_arr[k],temp_arr[l]]);
                                    holder_interaction_pair_array.push([holder_text_array[k][0], holder_text_array[l][0]]);
                                }
                            }
                            else{
                                if(bef_aft_array[l] == "after"){//make pair of service 2 and service 1
                                    interaction_pair_array.push([temp_arr[l],temp_arr[k]]);
                                    holder_interaction_pair_array.push([holder_text_array[l][0], holder_text_array[k][0]]);
                                }
                            }
                            //save time complexity by not iterating over services in different columns
                            if(holder_text_array[k][2] != 0 && bef_aft_array[k] != "after"){
                                break;
                            }
                        }
                        //save time complexity by not iterating over 2 hop away services
                        if (holder_text_array[l][1] == (holder_text_array[k][1] + (191 * 2))) {
                            break;
                        }
                    }
                }
                if(!(text_array[0].includes("{c}"))) {//set 1st index to 0 if it is not a copy {c}
                    connected_id[0] = 0;
                }
                else{
                    connected_id[0] = "";
                }
                var r;
                var spl;
                console.log('text aray: ', text_array);
                console.log('con aray: ', connected_id);
                for(l = 1; l < connected_id.length; l++){
                    r = l;
                    console.log('txt arr L: ', text_array[l]);
                    if(text_array[l].includes("{c}")){ //do not change id for copy {c}
                        connected_id[l] = "";
                        continue;
                    }
                    if(text_array[l].includes("{r}")){//update id of copy {r} with original service id
                        spl = text_array[l].split(" {r}");
                        connected_id[l] = connected_id[text_array.indexOf(spl[0])];
                    }
                    else{
                        //move back to an original service to set id of copy {r}
                        if(text_array[l-1].includes("{r}") || text_array[l-1].includes("{c}")){
                            r = l-1;
                            while(text_array[r].includes("{r}") || (text_array[r].includes("{c}") && r!=0)){
                                r--;
                            }
                            if(text_array[r].includes("{c}")){
                                connected_id[l] = connected_id.indexOf(connected_id[r]);
                            }
                            else {
                                connected_id[l] = connected_id[r] + 1;
                            }
                        }
                        else { //update id of original service
                            connected_id[l] = connected_id[l - 1] + 1;
                        }
                    }
                }

                for(l = 0; l < connected_id.length; l++){ //update id for copy {c}
                    if(text_array[l].includes("{c}")){
                        spl = text_array[l].split("_{c}");
                        connected_id[l] = connected_id[text_array.indexOf(spl[0])];
                    }
                }

                for(l = 0; l < sets_array.length; l++) { //update ids in set array
                    sets_array[l][7].attr("number", (connected_id[l]));
                }
                for(l = 0; l < sets_array.length-1; l++){ //fill interaction array with ""
                    interaction_array.push('');
                }
                //show interaction interface and hide workflow interface modules
                $('#set_int_tag').show();
                $("#fluid").hide();
                $('#addblock').hide();
                $('#copy_popup_button').hide();
                $('#final_model').hide();
                $('#save_model').hide();

               // paper.canvas.style.backgroundColor = '#0000';
                paper.canvas.style.backgroundColor = '#F5F5F5';
                delete_button_set.remove(); //remove delete button from boxes
                req_button_set.remove(); //remove spec button from boxes

                //following nested forEach loop is used to highlight connected boxes
                sets_array.forEach(function (i){
                    holder_interaction_pair_array.forEach(function(j){
                        sets_array.forEach(function (k){
                            if (i[6].attr('text') == j[0] && k[6].attr('text') == j[1]) {
                                hover_interactions(i, k);
                            }
                        });
                    });
                });
                $('#create_wf').show();
            }
        })
	}else{ Swal.fire({title: 'Service Missing!', text: 'Atleats one service should be exist on canvas', icon: 'warning'});
            return;
		}
    }

    function hover_interactions(i,k){ //highlight pair of connected services
        i.hover(function () {
                    i.attr({cursor: "pointer"});
                    i[0].attr({'fill':'rgb(77, 184, 255)'});
                    k[0].attr({'fill':'rgb(77, 184, 255)'});
                    interaction_button_func(i,k);
                },
                function () {
                    i[0].attr({'fill':'270-#98D5FF:5-#D7EEFF:100'});
                    k[0].attr({'fill':'270-#98D5FF:5-#D7EEFF:100'});
                });
    }

    function interaction_button_func(i,k){
        var gam1 = i[6].attr('text').replace(/[\n\r]+/g, ''); //display name of service 1 on popup
        var gam2 = k[6].attr('text').replace(/[\n\r]+/g, '');//display name of service 2 on popup

        i.click(function(){
            var box_id = this.data("id");

            var g1 = document.getElementById('Gamma_1');
            var g2 = document.getElementById('Gamma_2');
            //swap service names if service 1 is connected after service 2
            if(bef_aft_array[sets_array.indexOf(i)] == "after"){
                g1.innerHTML = gam2;
                g2.innerHTML = gam1;
            }
            else {
                g1.innerHTML = gam1;
                g2.innerHTML = gam2;
            }
//            $('.int_inp').val("");
            interactions(box_id);
        });
    }

    function interactions(box_id){ //set interactions in popup
        var inp = document.getElementsByClassName("int_inp");
        if(interaction_array[box_id] != null){ //fill requirement fields with pre-entered data if it's present
            var el;
            for(var j = 0; j < interaction_array[box_id].length;j++){
                if(interaction_array[box_id][j][1] == '###'){ //do not fill if interaction is set off
                    break;
                }
                el =interaction_array[box_id][j][1]; //access 1st element of map array in interaction array
                inp[j].value = el; //fill value of input fields with previously entered data
            }
        }
        /*------following commented out code is for auto-fill interaction input fields------*/
//        for (var p = 0; p < 20; p++) {
//            if(typeof inp[p] == 'undefined'){
//                continue;
//            }
//            inp[p].value = int_element[p]; //fill value of input fields with previously entered data
//            if(box_id == 2 || box_id == 4 || box_id == 7) {
//                if (p == 0) {
//                    inp[p].value = "2";
//                }
//                if (p == 1) {
//                    inp[p].value = "sec";
//                }
//                if (p == 2) {
//                    inp[p].value = "50";
//                }
//            }
//        }

        var span = document.getElementsByClassName("close_int_modal")[0];// closing on press(x) button
        span.onclick = function () {
                        modal.style.display = "none";
                    };
                    window.onclick = function (event) {
                        if (event.target == modal) {
                            modal.style.display = "none";
                        }
                    };



        var interaction_button = document.getElementById("int_submit_button");
        var int_toggle = $("#int_toggle"); //toggle for setting interaction on/off
        var modal = document.getElementById("int_modal");
        modal.style.display = "block";

        int_toggle.on('change', function() {
            if ($(this).is(':checked')) { //if interaction is set on, enable input fields
                $(".int_inp").prop('disabled', false);
                span.onclick = function () {
                    modal.style.display = "none";
                };

                window.onclick = function (event) {
                    if (event.target == modal) {

                        modal.style.display = "none";
                    }
                };
            }
            else { //if interaction is set off, disable input fields
                $(".int_inp").prop('disabled', true);
                span.onclick = function () {
                    modal.style.display = "block";
                };

                window.onclick = function (event) {
                    if (event.target == modal) {
                        modal.style.display = "block";
                    }
                };
            }
        });
        interaction_button.onclick = function(){ //submit button pressed for interactions
            var terminate = 0; //iterator for all input fields of interactions
            $(".int_inp").each(function(i) { //check for any missing interaction field
                var val = document.getElementsByClassName("int_label");
                var element = $(this); //current field
                var msg = val[i].innerHTML; //name of field
                var tag = element.attr('name'); //tag of field

                if(!(int_toggle.is(':checked'))){ //if interaction toggle is set to off
                    if(tag == 'val_latency' || tag == 'val_bandwidth' || tag == 'val_sampling_rate'
                            || tag == 'val_iat'){ //rename tag for value tag
                        tag = 'Value';
                    }
                    if(tag == 'unit_latency' || tag == 'unit_bandwidth' || tag == 'unit_sampling_rate'
                            || tag == 'unit_iat'){ //rename tag for unit tag
                        tag = 'Unit';
                    }
                    interaction_map.push([tag,'###']); //fill with ### for interaction set to off
                    modal.style.display = "none";
                }
                else if (element.val() == "") { //generate error for missing interaction field
                    Swal.fire({title: 'Data Missing for '+msg+'!', text: 'Fill '+msg+' field', icon: 'warning'});
                    inp[i].style.backgroundColor = "yellow"; //highlight missing field
                    inp[i].addEventListener("keypress", function () { //undo highlight upon typing
                        inp[i].style.backgroundColor = "white"
                    });
                    //prevent closing popup once submit is pressed to make sure no field is missed
                    var span = document.getElementsByClassName("close_int_modal")[0];
                    span.onclick = function () {
                        modal.style.display = "block";
                    };
                    window.onclick = function (event) {
                        if (event.target == modal) {
                            modal.style.display = "block";
                        }
                    };
                    return false;
                }
                else{
                    terminate++; //move to next field of interaction popup
                    if(tag == 'val_latency' || tag == 'val_bandwidth' || tag == 'val_sampling_rate'
                            || tag == 'val_iat'){
                        tag = 'Value';
                    }
                    if(tag == 'unit_latency' || tag == 'unit_bandwidth' || tag == 'unit_sampling_rate'
                            || tag == 'unit_iat'){
                        tag = 'Unit';
                    }
                    interaction_map.push([tag,element.val()]); //store in map till all fields filled
                }
                if(terminate == 12){ //close popup
                    modal.style.display = "none";
                    return;
                }
                inp[terminate].focus(); //move cursor to highlighted missing field
            });
            interaction_array[box_id]= interaction_map; //add all inputs of interaction to array
            interaction_map = []; //empty map for next interaction
        };
    }

    function create_workflow(){
		if(document.getElementById("paper1").children[0].childNodes.length >2){
        var txt;
        if(interaction_array.length == 0){ //generate error message if interactions are not set
            Swal.fire({title: 'Interactions Missing!', text: 'Fill Interactions', icon: 'warning'});
            return;
        }
        for(var x = 0; x < interaction_array.length;x++){ //check for missing interactions
            if(interaction_array[x] == '###'){//skip if interaction is set to off
                continue;
            }
            if(interaction_array[x] == ''){
                //if index is "" for last box then remove it from array and don't show error
                if(!counter_array.includes(counter_array[text_array.indexOf(text_array[x])]+191)){
                    interaction_array.splice(x,1);
                    continue;
                }
                txt = text_array[x];
                Swal.fire({title: 'Interaction Missing!', text: 'Fill interactions for '+txt, icon: 'warning'});
                return;
            }
        }
        Swal.fire(
                'Downloading Workflow Model!',
                'Download will now begin!',
                'success'
        );

        create_XML();
        xml = xml + '</Workflow>'; //closing tag of XML code
        var blob = new Blob([xml],
                {type: "text/xml"});
        saveAs(blob, "workflow_model.xml"); //download file to default storage in local drive
		location.replace("index.php");
        $("#wf").addClass("disabled").find("#create_wf").attr("onclick", "return false;"); //disable recreation of workflow
    
	
		}else{	 Swal.fire({title: 'Service Missing!', text: 'Atleats one service should be exist on canvas.', icon: 'warning'});
            return;
			}
	}

    var header_tags = []; //interaction tags of html for XML creation
    var req_header_tags = []; //requirement tags of html for XML creation

    function create_XML(){
        $(".lbl_h").each(function(i) { //fill interaction labels in array
            header_tags.push($(this).attr('id'));
        });
        $(".req_lbl_h").each(function(i) { //fill requirement labels in array
            req_header_tags.push($(this).attr('id'));
        });

        var iterator;
        var gid = 0;
        for(iterator = 0; iterator < text_array.length; iterator++){ //for specifications
            //do not recreate XML for copy services
            if(text_array[iterator].includes("{r}") || text_array[iterator].includes("{c}")){
                requirement_array.splice(0,1);
                continue;
            }
            gid = gid + 1; //gid + 1 so if specification of copies is skipped, id remains consistent
            var s_name = text_array[iterator].toLowerCase().replace(/ /g, '_'); //remove spaces in name
            s_name = s_name.split('_{');
            xml = xml+
            '<Gamma id="'+ gid + '">\n' +
            '<Service_Name>'+ s_name[0] +'</Service_Name>\n';

            req_header_tags.forEach(function(i,index){ //function to build XML for different tags
                var switcher = 0;
                element_val(i,index,switcher);
            });
            requirement_array.splice(0,1); //remove specifications that have been added
            xml = xml +
            '</Gamma>\n'
            ;
        }

        var k;
        var l;
        var sw = '';
        //swap indexes if service 1 appears after service 1 on next index
        for(k = 0; k < interaction_pair_array.length; k++){
            for(l = k; l < interaction_pair_array.length; l++){
                if(text_array.indexOf(interaction_pair_array[k][0]) > text_array.indexOf(interaction_pair_array[l][0])){
                    sw = interaction_pair_array[k];
                    interaction_pair_array[k] = interaction_pair_array[l];
                    interaction_pair_array[l] = sw;
                }
            }
        }
        //swap services on an index in pair array horizontally if service 1 appears after service 2
        for(k = 0; k < interaction_pair_array.length; k++){
            if(text_array.indexOf(interaction_pair_array[k][0]) > text_array.indexOf(interaction_pair_array[k][1])){
                sw = interaction_pair_array[k][0];
                interaction_pair_array[k][0] = interaction_pair_array[k][1];
                interaction_pair_array[k][1] = sw;
            }
        }
        for(iterator = 0; iterator < interaction_pair_array.length; iterator++) { //for interactions
            if(interaction_array[0][0][1] == '###'){ //skip if interaction is set to off
                interaction_array.splice(0,1);
                continue;
            }
            gid = iterator + 1;
            var gid_val1 = text_array.indexOf(interaction_pair_array[iterator][0]);//index of service 1
            var gid_val2 = text_array.indexOf(interaction_pair_array[iterator][1]);//index of service 2
            var gid1 = sets_array[gid_val1][7].attr("number")+1; //id of service 1
            var gid2 = sets_array[gid_val2][7].attr("number")+1; //id of service 2

            xml = xml +
            '<interaction id="' + gid + '">\n' +
            '<Gamma_1>' + gid1 + '</Gamma_1>\n' +
            '<Gamma_2>' + gid2 + '</Gamma_2>\n'
            ;
            header_tags.forEach(function(i,index){ //function to build XML for different tags
                var switcher = 1;
                element_val(i,index,switcher);
            });
            interaction_array.splice(0,1); //remove interactions that have been set
            xml = xml + '</interaction>';
        }
    }


function element_val(i,index,switcher){
if(switcher == 1){ //interaction call
index = index + 10; //tags for interaction start from case 10
}
switch(index) {
case 0: case 1: case 2: case 3: case 4: case 5: //service category/sinp/sout/styp/depl/uri
{
xml = xml +
req_val_init();
break;
}
case 6: //location -- lat/long + radius
{
xml = xml +
'<Location>\n' +
req_val_init() + //latitude
req_val_init() + //longitude
'<Radius>\n' +
req_val_init() + //radius value
req_val_init() + //radius unit
'</Radius>\n' +
'</Location>\n';
break;
}
case 7: //availability
{
xml = xml +
'<Availability>\n' +
req_val_init() + //value
req_val_init() + //unit
'</Availability>\n';
break;
}
case 8: //hardware requirements
{
xml = xml +
'<Hardware_Requirements>\n' +
'<external_storage>\n' +
req_val_init() +
req_val_init() +
'</external_storage>\n' +
'<RAM>\n' +
req_val_init() + //value
req_val_init() + //unit
'</RAM>\n' +
req_val_init() + //cpu
req_val_init() + //network
'</Hardware_Requirements>\n';
break;
}
case 9:
{
xml = xml +
'<Software_Requirements>\n' +
req_val_init() + //OS
'<Packages_List>\n' +
req_val_init() + //packages
'</Packages_List>\n' +
'</Software_Requirements>\n';
break;
}
case 10: //QoS
{
xml = xml +
'<'+i + '>\n' +
'<Latency>\n' +
int_val_init() + //value
int_val_init() + //unit
'</Latency>\n'+
'<Bandwidth>\n' +
int_val_init() + //value
int_val_init() + //unit
'</Bandwidth>\n'+
'<Sampling_Rate>\n' +
int_val_init() + //value
int_val_init() + //unit
'</Sampling_Rate>\n' +
'</'+i + '>\n';
break;
}
case 11: //inter interval time
{
xml = xml +
'<'+i + '>\n' +
int_val_init() + //value
int_val_init() +//unit
'</'+i + '>\n';
break;
}
case 12: //message
{
xml = xml +
'<'+i + '>\n' +
int_val_init() + //sent
int_val_init() + //received
'</'+i + '>\n';
break;
}
case 13: //address / URIs
{
xml = xml +
int_val_init() + //gamma addr 1
int_val_init(); //gamma addr 2
break;
}
}
}

    function int_val_init(){ //function to get interaction value

        var ret = [interaction_array[0][0][0], interaction_array[0][0][1]];  //unit and value
        var str = '<' + ret[0] + '>' + ret[1] + '</' + ret[0] + '>\n';
        interaction_array[0].splice(0,1); //remove element from inner array
        return str;
    }

    function req_val_init(){ //function to get requirement value
        var ret = [requirement_array[0][0][0], requirement_array[0][0][1]]; //unit and value
        var str = '';
        if(ret[0] == 'Package_List'){ //separate the packages to make list
            var list = ret[1].split(',');
            var n = 0;
            while(n < list.length){
                if(list[n] == '' || list[n] == ' '){
                    list.splice(n,1);
                    continue;
                }
                if(list[n].charAt(0) == ' '){ //remove space from first character
                    list[n] = list[n].replace(' ','');
                }
                if(list[n].endsWith(' ')){ //remove space from last character
                    list[n] = list[n].replace(/.$/,"");
                }
                str = str + '<Package>' + list[n] + '</Package>\n';
                n++;
            }
            requirement_array[0].splice(0,1); //remove element from inner array
            return str;
        }
        str = '<' + ret[0] + '>' + ret[1] + '</' + ret[0] + '>\n';
        requirement_array[0].splice(0,1);
        return str;
    }
	function UploadModel(){
		 modal_upload = document.getElementById("UploadModel");
			var span = document.getElementsByClassName("close_upload_service_popup")[0];
			var copy = document.getElementById('add_button');
			var lbl = document.getElementById('r_included');
			
			lbl.style.display = "none";
			modal_upload.style.display = "block";
			$("#copy_before_after").show();

			span.onclick = function () { //if close (cross) button pressed, close popup
            modal_upload.style.display = "none";
        };
		
			var closex = document.getElementById("closeUploadModal");
			closex.onclick = function () { //if close (cross) button pressed, close popup
				modal_upload.style.display = "none";
        };
	}

    function upload_model(event){ //upload existing workflow model
        var input = event.target; //input file uploaded
        var reader = new FileReader(); //to read file contents
        reader.onload = function(file){ //load file contents and store in div for filling arrays
            var text = reader.result; //file content
            var node = document.getElementById('file_content');
            node.innerText = text; //store file content in div
            var json = JSON.parse(node.innerText); //convert text to json format
            //fill global arrays with file data
            text_array = json.text;
            requirement_array = json.requirements;
            holder_text_array = json.hta;
            holder_block_array = json.hba;
            bef_aft_array = json.befaft;
            id = 0; //id is 0 because uploaded model starts from index 0
            translate = 0; //first box is created in row 0
            create_box(text_array.length,2); //all services are to be redrawn

        };
        reader.readAsText(input.files[0]); //read file
        $("#file_div").hide();
        $("#fluid").show();
		modal_upload.style.display = "none";

    }

    function save_model(){
        var swal_inp;
        Swal.fire({
            title: 'Save As',
            html:
                    '<input id="file_name" class="file_name">',
            preConfirm: function () {
                return new Promise(function (resolve) {

                    resolve([
                        swal_inp = $('#file_name').val()
                    ]);
                })
            },
            onOpen: function () {
                $('#file_name').focus()
            }
        }).then(function (result) {
            if (swal_inp != "") {
                var all_arrays = [];
                all_arrays.push(text_array,counter_array,holder_text_array, holder_block_array,bef_aft_array);
                var data = {
                    "text":text_array,
                    "requirements":requirement_array,
                    "hta": holder_text_array,
                    "hba": holder_block_array,
                    "befaft": bef_aft_array
                };
                var blob = new Blob([JSON.stringify(data, null, 2)],  {type:"application/json"});
                saveAs(blob, swal_inp+".json");
            }
            else{
                Swal.fire({
                    icon: 'error',
                    title: 'File Name Missing',
                    text: 'No file name was entered!'
                });
            }
        }).catch(Swal.fire.noop);
    }

    function create_new_model(){
        $("#fluid").show();
        $("#file_div").hide();
    }


function openTab(){
	 url = "../demoWorkflowFinal/workflow_manual.pdf"
    var win = window.open(url, '_blank');
    win.focus();
}
 