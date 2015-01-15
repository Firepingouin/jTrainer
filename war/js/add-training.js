
!function() {
	
	$(function() {
		'use strict';
		
		var handleExercice = {
				
				exercices : [],
				
				init : function(tools){
					
					this.tools = tools;
					this.cptID = 0;
					this.gui = {
						inputs : {
							add 	: $('#exerciceAdd'),
							title 	: $('#exerciceTitle'),
							desc 	: $('#exerciceDescription'),
							hour 	: $('#exerciceHour'),
							min 	: $('#exerciceMin'),
							sec 	: $('#exerciceSec'),
							rep 	: $('#exerciceRepetitions'),
							submit	: $('#btnSubmit'),
							form 	: $('#formAddTraining'),
						},
						domains		: $('#selectDomains'),
						table 		: $('#tableExercices'),
						totaltime	: $('#totalTimeValue'),
						wait		: $('#pleaseWait'),	
					};
					this.event();
				},
				
				event : function(){
					
					var that =this;
					
					$.post( "search", { type: "domaine" }, function( data ) {
						for(var i=0; i<data.length; i++) {
							that.gui.domains.append('<option value="'+data[i]["id"]+'">'+data[i]["nom"]+'</option>');
						}
						that.gui.wait.hide();
					},"json")
					.fail(function() {
						console.log("Search domains fail !");
					})
					.always(function() {
						console.log( "Search domains ok !" );
					});
					
					this.gui.inputs.add.on('click', function(e){
						that.add();
					});
					
					this.gui.inputs.submit.on('click', function(e){
									
						e.preventDefault();
						
						that.gui.wait.show();
						
						var postData = [];
						postData[0] = {name : "action", value : "add"};
						var formData = that.gui.inputs.form.serializeArray();
						formData[formData.length] = {name : "exercices", value : that.exercices};
						postData[postData.length] = {name : "trainingPlan", value : formData};;
					    console.log(postData);
					    $.post("train", postData, function(data)
				        {
				        	that.gui.wait.hide();
				        	//window.location.href = "ha-search-screen.html";
				        	console.log("addTraining ok !");
				        },"json")
						.fail(function() {
						    console.log("Fail addTraining !");
					    });					
					});
				},
				
				add : function(){
					
					// mem
					var that =this;
					var exercice = {
						id : 	++that.cptID,
						rep : 	that.gui.inputs.rep.val(),
						title : that.gui.inputs.title.val(),
 						desc : 	that.gui.inputs.desc.val(),
						duree : that.tools.toSec(that.gui.inputs.hour.val(),
												that.gui.inputs.min.val(),
												that.gui.inputs.sec.val(),
												that.gui.inputs.rep.val()),
					};
					
					this.exercices.push(exercice);
					
					this.tools.addTime(exercice.duree);
					
					var row =$('<tr>').attr({id : 'ex-'+exercice.id});
					
					var button = $('<button>').attr({ type : button})
									.addClass('btn btn-danger btn-sm')
									.append('<span class="glyphicon glyphicon-remove"></span>')
									.on('click', function(){
										that.remove(exercice);
									});

					// output
					row.append(
							'<td>' + exercice.rep + '</td>'
							+ '<td>' + exercice.title + '</td>'
							+ '<td>' + exercice.desc + '</td>'
							+ '<td>' + exercice.duree + ' sec</td>'
					
					);
					row.append($('<td>')
							.append(button)
				    );
					
					this.gui.table.append(row);
				},
				
				remove : function(exercice) {
					
					var flag = true;
					var i = 0;
					
					while(flag && i<this.exercices.length){
						if(this.exercices[i].id === exercice.id){
							$('#ex-'+exercice.id).remove(); // remove on html
							this.tools.removeTime(this.exercices[i].duree); // remove time
							this.exercices.splice(i,1); // remove in array
							flag = false;
						}
						i++;
					}	
				},
		};
		
		var tools = {
				toSec : 		function(hour,min,sec,rep) {
					return (parseInt(hour)*3600 + parseInt(min)*60 + parseInt(sec))*rep;	
				},
				totalTimeToSec :function() {
					var total = handleExercice.gui.totaltime.text().trim().split(':');
					return tools.toSec(total[0],total[1],total[2],1);
				},
				addTime : 		function(sec) {
					tools.setTime(tools.totalTimeToSec()+sec);
				},
				removeTime : 	function(sec) {
					tools.setTime(tools.totalTimeToSec()-sec);
				},
				setTime : 		function(totalsec) {
					var hours = parseInt( totalsec / 3600 ) % 24;
					var minutes = parseInt( totalsec / 60 ) % 60;
					var seconds = totalsec % 60;
					handleExercice.gui.totaltime.text(hours 
							+ ":" + (minutes < 10 ? "0" + minutes : minutes) 
							+ ":" + (seconds  < 10 ? "0" + seconds : seconds));
				},
		}
		
		handleExercice.init(tools);
		
	});
	
}(); 