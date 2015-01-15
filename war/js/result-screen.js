/**
 * TODO javascript functions for result-screen
 */

!function () {
	var gui = {
			output : {
				train 		: $('#resIntensiveRunning'),
				exercices 	: $('#resExercices'),
				news		: $('#resNews'),
			},
			wait		: $('#pleaseWait'),
	}
	
	var search = window.location.search.replace("?", "").split("=");
	var seek = search[1];
	var json;
	if(search[0]=="searchKeyword") {
		json = { type: "search", searchKeyword: seek }
	} else if (search[0]=="domainid") {
		json = { type: "search", domainId: seek };
	}
	var jqxhr = $.post( "search", json, function( data ) {

		if(data.news != undefined) {
			data.news.forEach(function(n) {
				gui.output.news.append($('<p>')
								.append(n.titre + ": " + n.description )
				);
			});
		} else {
			gui.output.train.append($('<p>')
					.append("No news found")
				);
		}
		if(data.searchResults.trainingPlans != undefined) {
			data.searchResults.trainingPlans.forEach(function(t) {
				gui.output.train.append($('<div>')
					.addClass("col-md-6 col-sm-6 col-xs-6")
					.append($('<button>')
						.addClass("btn btn-link")
						.attr('type', 'button')
						.append(t.titre)
					)
				)
				.append($('<div>')
					.addClass("col-md-6 col-sm-6 col-xs-6")
					.append($('<label>')
						.addClass("btn")
						.append($('<span>')
								.addClass("glyphicon glyphicon-time")
						)
						.append(t.duree)
					)
				);
			});
		} else {
			gui.output.train.append($('<div>')
					.addClass("col-md-6 col-sm-6 col-xs-6")
					.append("No training plan found")
				);
		}
		if(data.searchResults.exercices != undefined) {
			data.searchResults.exercices.forEach(function(e) {
				gui.output.exercices.append($('<div>')
						.addClass("col-md-6 col-sm-6 col-xs-6")
						.append($('<button>')
							.addClass("btn btn-link")
							.attr('type', 'button')
							.append(e.titre)
						)
					)
					.append($('<div>')
						.addClass("col-md-6 col-sm-6 col-xs-6")
						.append($('<label>')
							.addClass("btn")
							.append($('<span>')
									.addClass("glyphicon glyphicon-time")
							)
							.append(e.duree)
						)
					);
			});
		} else {
			gui.output.exercices.append($('<div>')
					.addClass("col-md-6 col-sm-6 col-xs-6")
					.append("No exercices found")
				);
		}
		gui.wait.hide();
	},"json")
	.fail(function() {
		console.log("Search fail");
	});
}();