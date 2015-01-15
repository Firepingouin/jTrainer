/**
 * TODO javascript functions for header
 */

!function () {
	var jqxhr = $.getJSON( "login?callback="+location.href, function( data ) {
		if(data["logged"] == "true") { 
			$('#loginOpenID').html("<p class='tex-whi'>Hi "+data["user"]["nickname"]+"</p><a onclick=\"location.href='"+data["logout"]+"'\">Log out</a>");
		} else {
			$('#loginOpenID').html("<button class='btn btn-primary' onclick=\"location.href='"+data["domains"]["google"]+"'\">G</button><button type='submit' class='btn btn-success' onclick=\"location.href='"+data["domains"]["yahoo"]+"'\">Y</button> <button type='submit' class='btn btn-warning' onclick=\"location.href='"+data["domains"]["open"]+"'\">O</button>");
		}
	})
	.fail(function() {
		$('#loginOpenID').text("Error loading message");
	})
	.always(function() {
		console.log( "Open ID Login Ok !" );
	});
	var header = {
		button 	: {
			search 	: $('#btnSearch'),
		},
		input	: {
			search	: $('#inputSearch'),
		}
	}
	header.button.search.on('click', function(e) {
		window.location.href = "ha-result-screen.html?searchKeyword="+header.input.search.val();
	});
}();