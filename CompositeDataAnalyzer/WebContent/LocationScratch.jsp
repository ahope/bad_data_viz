<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="com.aro.saga.bodymedia.BodyMediaStepData" %> 
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

		<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">

    
    //==== Another chart. 
    
//Get the CSV and create the chart
$.get('LocationDataServlet', function (csv) {
    $('#text').text(csv);
    var lines = csv.split("||");
    
    var items = lines[0].split(', ');
    var data = new Array(); 
    
    var labels = lines[1].split(', '); 

        $.each(items, function(itemNo, item) {
           // if (itemNo == 0) {
           //     series.name = item;
           // } else {
                data.push(parseInt(item));
            //}
        });
    
    $('#container2').highcharts({
    	
    	series: [{
    	         name: 'Home1', 
    	         data: data
    	}],
    	chart: {
            type: 'column'
        },
        title: {
            text: 'Seconds spent in each Location'
        },
        subtitle: {
            text: 'Source: Saga'
        },
        xAxis: {
            //categories: ['','', '','','','5am', '', '', '', '9am', '', '', '12pm', '', '', '', '', '5pm', '', '', '', '9pm'],
            categories: labels,
            title: {
                text: null
            }
        }
        
    });
});
    
		</script>


</head>
<body>
Hi there! 

<script src="js/highcharts.js"></script>
<script src="js/modules/exporting.js"></script>


Hi again!

<div id='text' style="background-color:#F00; min-width:40px; height:50px;"></div>

<div id="container2" style="min-width: 400px; height: 400px; margin: 0 auto; background-color:#000"></div>


<%
    System.out.println( "Evaluating date now" );
//    BodyMediaStepData steps = new BodyMediaStepData();
//    System.out.println(steps); 
%>

</body>
</html>