/**
 * TODO javascript functions for search-page 
 */

!function () {
	var jqxhr = $.post( "search", { type: "domaine" }, function( data ) {
		var toAppend = "<div class=\" col-md-12 col-sm-12 col-xs-12\" style=\"margin-bottom:80px\">";
		for(var i=0; i<data.length; i++) {
			if((i%4)==0 && i>0) {
				toAppend+= "</div>";
				toAppend+= "<div class=\" col-md-12 col-sm-12 col-xs-12\" style=\"margin-bottom:80px\">";
			}
			toAppend+= "<div class=\" col-md-3 col-sm-3 col-xs-3 \"><button ";
			toAppend+= "onclick=\"location.href='ha-result-screen.html?domainid="+data[i]["id"]+"'\"";
			toAppend+= " class=\"btn btn-default btn-lg\"><span class=\"glyphicon glyphicon-tree-deciduous\"></span></button> <label>";
			toAppend+= " "+data[i]["nom"]+"</label></div>";
		}
		toAppend+= "</div>";
		$('#domainsResults').html(toAppend);
	},"json")
	.fail(function() {
		console.log("Search domains fail !");
	})
	.always(function() {
		console.log( "Search domains ok !" );
	});
}();

/* 
 * <div class=" col-md-12 col-sm-12 col-xs-12" style="margin-bottom:80px">
          <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
          </div>
                    <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
           </div>
                    <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
           </div>
                    <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
           </div>
       </div>
      
      <div class=" col-md-12 col-sm-12 col-xs-12 " style="margin-bottom:80px">
          <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
          </div>
                    <div class=" col-md-3 col-sm-3 col-xs-3 ">
                    <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
           </div>
                    <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
           </div>
                    <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
           </div>
       </div>
      
      <div class=" col-md-12 col-sm-12 col-xs-12 ">
          <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
          </div>
                    <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
           </div>
                    <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
           </div>
                    <div class=" col-md-3 col-sm-3 col-xs-3 ">
            <button type="submit" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-tree-deciduous"></span></button> <label> RUN </label>
           </div>
       </div>
       */
