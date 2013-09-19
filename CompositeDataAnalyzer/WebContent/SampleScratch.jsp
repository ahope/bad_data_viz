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
$(function () {
        $('#container').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'Calories Burned in Home, Commute, Work, Other'
            },
            subtitle: {
                text: 'Source: Saga + BodyMedia'
            },
            xAxis: {
                categories: ['5am', '', '', '', '9am', '', '', '12pm', '', '', '', '', '5pm', '', '', '', '9pm'],
                title: {
                    text: null
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Calories',
                    align: 'high'
                },
                stackLabels: {
                	enabled: true,
                    style: {
                        fontWeight: 'bold',
                        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                    }
                },
                labels: {
                    overflow: 'justify'
                }
            },
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: true,
                        color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                    }
                }
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'top',
                x: -100,
                y: 100,
                floating: true,
                borderWidth: 1,
                backgroundColor: '#FFFFFF',
                shadow: true
            },
            credits: {
                enabled: false
            },
            series: [{
                name: 'Home',
                data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]
            }, {
                name: 'Commute',
                data: [133, 156, 947, 408, 6, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]
            }, {
                name: 'Work',
                data: [973, 914, 405, 732, 34, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]
            }, {
                name: 'Other',
                data: [273, 114, 154, 232, 34, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]
            }]
        });
    });
    
    //==== Another chart. 
    
//Get the CSV and create the chart
$.get('StepDataServlet', function (csv) {
    $('#text').text(csv);
    var items = csv.split(', ');
    var data = new Array(); 
    

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
            text: 'Steps taken in Home, Commute, Work, Other'
        },
        subtitle: {
            text: 'Source: Saga + BodyMedia'
        },
        xAxis: {
            categories: ['','', '','','','5am', '', '', '', '9am', '', '', '12pm', '', '', '', '', '5pm', '', '', '', '9pm'],
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

<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>

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