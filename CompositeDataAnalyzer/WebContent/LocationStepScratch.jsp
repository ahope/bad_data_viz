<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="com.aro.saga.bodymedia.BodyMediaStepData" %> 
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BodyMedia Steps Per Location</title>
<link rel="stylesheet" href="css/dark-hive/jquery-ui-1.10.3.custom.min.css" />

		<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.10.3.custom.js"></script>
<script type="text/javascript">

    
    
    
    
$(function() {
  $( "#datepicker" ).datepicker({
	  dateFormat: "yy-mm-dd" ,
	  onSelect: function(dateText) {
		   // display("Selected date: " + dateText + "; input's current value: " + this.value);
		   //var altFormat = $( ".selector" ).datepicker( "option", "altFormat" );
		   loadData(this.value.replace(/\//gi, "-"));  //toString('yyyy-mm-dd')); 
		  
		  }
		});
});
		</script>


</head>
<body>
Hi there! 

<script src="js/highcharts.js"></script>
<script src="js/modules/exporting.js"></script>

<p>Date: <input type="text" id="datepicker" /></p>


<div id='text' style="background-color:#F00; min-width:40px; height:50px; visibility:hidden;"></div>

<div id="container2" style="min-width: 400px; height: 400px; margin: 0 auto; background-color:#000"></div>


<%
    System.out.println( "Evaluating date now" );
//    BodyMediaStepData steps = new BodyMediaStepData();
//    System.out.println(steps); 
%>

</body>
</html>