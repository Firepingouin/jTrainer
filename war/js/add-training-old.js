/**
 * TODO javascript functions for search-page 
 */

var exercice = {};

!function () {
	// Manage submit event for the form
	$("#formAddTraining").submit(function(e)
	{
		console.log("allo");
		$('#pleaseWait').show();
	    var postData = $(this).serializeArray();
	    postData["exercices"] = getJsonExercices();
	    $.ajax(
	    {
	        url : "train",
	        type: "POST",
	        data : postData,
	        success:function(data, textStatus, jqXHR)
	        {
	            //data: return data from server
	        	$('#pleaseWait').hide();
	        },
	        error: function(jqXHR, textStatus, errorThrown)
	        {
	            //if fails     
	        }
	    });
	    e.preventDefault(); //STOP default action
	});
	// Load domains in the select
	var jqxhr = $.post( "search", { type: "domaine" }, function( data ) {
		for(var i=0; i<data.length; i++) {
			$('#selectDomains').append('<option value="'+data[i]["id"]+'">'+data[i]["nom"]+'</option>');
		}
		$('#pleaseWait').hide();
	},"json")
	.fail(function() {
		console.log("Search domains fail !");
	})
	.always(function() {
		console.log( "Search domains ok !" );
	});
	
}();

// faire une structure

function getJsonExercices() {
	var tabExs = getTabExercices();
	var row = 0;
	var json = {};
	while(row<tabExs.length) {
		json[row] = {};
		json[row]["titre"] = $('#'+tabExs[row]+' td[name="Exercice['+tabExs[row]+'][titre]"]').text();
		json[row]["repetitions"] = $('#'+tabExs[row]+' td[name="Exercice['+tabExs[row]+'][repetitions]"]').text();
		json[row]["description"] = $('#'+tabExs[row]+' td[name="Exercice['+tabExs[row]+'][description]"]').text();
		var duree = $('#'+tabExs[row]+' td[name="Exercice['+tabExs[row]+'][titre]"]').text().split(' ');
		json[row]["duree"] = duree[0];
	}
	return json;
}

function getTabExercices() {
	var tr=0;
	var test=0;
	var tabExs = [];
	while(tr<$("#tableExercices tr").length) {
		if($('#'+test).length!=0) {
			// TODO parcourir les td dans les tr pour reconstruire le tableau d'exos
			//console.log($('#'+test+' [name="Exercice['+test+'][repetitions]"]').text());
			tabExs.push(test);
			tr++;
		}
		test++;
		if(test==100) { tr = $("#tableExercices tr").length +1; console.log("Infinite loop tabexercices") } // avoid infinite loop
	}
	return tabExs;
}

function addExercice() {
	var title = $('#exerciceTitle').val();
	var desc = $('#exerciceDescription').val();
	var hour = $('#exerciceHour').val();
	var min = $('#exerciceMin').val();
	var sec = $('#exerciceSec').val();
	var rep = $('#exerciceRepetitions').val();
	var flag = true;
	
	var time = (parseInt(hour)*3600 + parseInt(min)*60 + parseInt(sec))*rep;
	addTime(time);
	
	var id = $('#tableExercices tr:last').attr('id');
	if(id == undefined) {
		id=0;
	} else {
		id++;
	}

	var toAppend = '<tr id="'+id+'">';
	toAppend += '<td name="Exercice['+id+'][repetitions]">'+rep+'</td>';
	toAppend += '<td name="Exercice['+id+'][titre]">'+title+'</td>';
	toAppend += '<td class="hidden-xs" name="Exercice['+id+'][description]"> '+desc+'</td>';	
	toAppend += '<td name="Exercice['+id+'][duree]">'+time+' sec</td>';
	toAppend += '<td> <button onclick="removeExercice('+id+')" class="btn btn-danger btn-sm"> <span class="glyphicon glyphicon-remove"></span> </button></td>'
	toAppend += '</tr>';
	$('#tableExercices').append(toAppend);
}

function removeExercice(id) {
	$('#'+id).remove();
	var time = $('#'+id+' td:nth-child(4)').text().split(' ');
	console.log(time);
	removeTime(time[0]);
}

function addTime(time) {
	console.log(time);
	var total = $('#totalTimeValue').text().trim().split(':');
	var totalsec = parseInt(total[0])*3600 + parseInt(total[1])*60 + parseInt(total[2]);
	console.log(totalsec);
	setTime(parseInt(totalsec)+parseInt(time));
}

function removeTime(time) {
	var total = $('#totalTimeValue').text().trim().split(':');
	var totalsec = parseInt(total[0])*3600 + parseInt(total[1])*60 + parseInt(total[2]);
	setTime(parseInt(totalsec)-parseInt(time));
}

function setTime(totalsec) {
	var hours = parseInt( totalsec / 3600 ) % 24;
	var minutes = parseInt( totalsec / 60 ) % 60;
	var seconds = totalsec % 60;
	
	var result = hours + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds  < 10 ? "0" + seconds : seconds);
	$('#totalTimeValue').text(result);
}