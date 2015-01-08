/**
 * TODO javascript functions for header
 */

!function () {
	var jqxhr = $.getJSON( "login", function( data ) {
		if(data["logged"] == "true") { 
			$('#loginOpenID').html("<p class='tex-whi'>Hi "+data["user"]["nickname"]+"</p><a onclick=\"window.location('"+data["logout"]+"')\">Log out</a>");
		} else {
			$('#loginOpenID').html("<button class='btn btn-primary' onclick=\"window.location('"+data["domains"]["google"]+"','Login OpenID Google','menubar=no, status=no, scrollbars=no, menubar=no')\">G</button><button type='submit' class='btn btn-success' onclick=\"window.location('"+data["domains"]["yahoo"]+"','Login OpenID Yahoo','menubar=no, status=no, scrollbars=no, menubar=no')\">Y</button> <button type='submit' class='btn btn-warning' onclick=\"window.location('"+data["domains"]["open"]+"','Login OpenID Open','menubar=no, status=no, scrollbars=no, menubar=no')\">O</button>");
		}
	})
	.fail(function() {
		$('#loginOpenID').text("Error loading message");
	})
	.always(function() {
		console.log( "Ok !" );
	});
}();