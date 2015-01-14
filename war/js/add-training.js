
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
							submit	: $('button[type="submit"]'),
							
						},
						
						domains	: $('#selectDomains'),
						table 	: $('#tableExercices'),
						wait	: $('#pleaseWait'),
							
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
					
					this.gui.inputs.submit.on('submit', function(e){
											
						e.preventDefault();
						
						that.gui.wait.show();
						
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
					        	that.gui.wait.hide();
					        },
					        error: function(jqXHR, textStatus, errorThrown)
					        {
					            //if fails     
					        }
					    });
											
					});
					
				},
				
				add : function(){
					
					// mem
					
					var that =this;
					
					
					var exercice = {
						
						id : ++that.cptID,
						rep : that.gui.inputs.rep.val(),
						title : that.gui.inputs.title.val(),
 						desc : that.gui.inputs.desc.val(),
						duree : that.tools.toSec(that.gui.inputs.hour.val(),
												that.gui.inputs.min.val(),
												that.gui.inputs.sec.val()),
					};
					
					this.exercices.push(exercice);
					
					var row =$('<tr>').attr({id : 'ex-'+exercice.id});
					
					$('<button>').attr({ type : button})
									.addClass('btn btn-danger btn-sm')
									.append('<span class="glyphicon glyphicon-remove"></span>')
									.on('click', function(){
										
										that.remove(exercice);
										
									});
					
					//UGLY AS FUCK -> append cells to row
					row.append(
					
							'<td>' + exercice.rep + '</td>'
							+ '<td>' + exercice.title + '</td>'
							+ '<td>' + exercice.desc + '</td>'
							+ '<td>' + exercice.duree + ' sec</td>'
							+ '<td>' +  + '</td>'
					
					);
					
					this.gui.table.append(row);
					
					// output
					
					var row =
						ar toAppend = '<tr id="'+id+'">';
					toAppend += '<td name="Exercice['+id+'][repetitions]">'+rep+'</td>';
					toAppend += '<td name="Exercice['+id+'][titre]">'+title+'</td>';
					toAppend += '<td class="hidden-xs" name="Exercice['+id+'][description]"> '+desc+'</td>';	
					toAppend += '<td name="Exercice['+id+'][duree]">'+time+' sec</td>';
					toAppend += '<td> <button onclick="removeExercice('+id+')" class="btn btn-danger btn-sm"> <span class="glyphicon glyphicon-remove"></span> </button></td>'
					toAppend += '</tr>';
					
					
					
					
				},
				
				remove : function(exercice) {
					
					for(var i =0; i <this.exercices.length; i++){
						if(this.exercices[i].id === exercice.id){
							
						}
					}
					
					$('#ex'+exercice.id).remove();
					
					
				},
				
		};
		
		var tools = {
				
				toSec : function(hour,min,sec) {
					
					return (parseInt(hour)*3600 + parseInt(min)*60 + parseInt(sec))*rep;
					
				},
				
		}
		
		handleExercice.init(tools);
		
		/* $('#exerciceTitle').val();
	var desc = $('#exerciceDescription').val();
	var hour = $('#exerciceHour').val();
	var min = $('#exerciceMin').val();
	var sec = $('#exerciceSec').val();
	var rep = $('#exerciceRepetitions').val();*/
	});
	
}(); 