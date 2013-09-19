//==== Another chart. 
function loadBurnData(date) {
	//	Get the CSV and create the chart
	$.get('LocationBurnRateServlet?date=' + date, function(csv) {
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

		$('#burnContainer').highcharts(
				{

					series : [ {
						name : 'calories',
						data : data
					} ],
					chart : {
						type : 'column'
					},
					title : {
						text : 'Calories burned in each location on ' + date
					},
					subtitle : {
						text : 'Source: Saga & BodyMedia'
					},
					xAxis : {
						//categories: ['','', '','','','5am', '', '', '', '9am', '', '', '12pm', '', '', '', '', '5pm', '', '', '', '9pm'],
						categories : labels,
						title : {
							text : null
						}
					},
					colors : [ '#FF9233', '#BC0000', '#44B9E8', '#92D050',
							'#ffff00', '#7030A0', '#f28f43', '#77a1e5',
							'#c42525', '#a6c96a' ]

				});
	});
}

//==== Another chart. 
function loadStepData(date) {
	//	Get the CSV and create the chart
	$.get('LocationStepsServlet?date=' + date, function(csv) {
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

		$('#stepContainer').highcharts(
				{

					series : [ {
						name : 'steps',
						data : data
					} ],
					chart : {
						type : 'column'
					},
					title : {
						text : 'Steps taken in each location on ' + date
					},
					subtitle : {
						text : 'Source: Saga & BodyMedia'
					},
					xAxis : {
						//categories: ['','', '','','','5am', '', '', '', '9am', '', '', '12pm', '', '', '', '', '5pm', '', '', '', '9pm'],
						categories : labels,
						title : {
							text : null
						}
					},
					colors : [ '#FF9233', '#BC0000', '#44B9E8', '#92D050',
							'#ffff00', '#7030A0', '#f28f43', '#77a1e5',
							'#c42525', '#a6c96a' ]

				});
	});
}

function loadBurnWeekData(date) {
	//Get the CSV and create the chart
	$.get('LocationBurnWeekServlet?date=' + date, function(csv) {
		$('#text').text(csv);
		var lines = csv.split("||");

		var testPlotBands = lines[1]; 
		
		//var items = lines[0].split(', ');
		var data = new Array();

		var labels = lines[2].split(', ');

		var allSeries = new Array();

		for ( var i = 3; i < lines.length; i++) {
			var newSeries = new Object();

			var newSeriesData = new Array();

			var data = lines[i].split(', ');

			$.each(data, function(itemNo, item) {
				if (itemNo == 0) {
					newSeries.name = item;
				} else {
					newSeriesData.push(parseFloat(item));
				}
			});
			newSeries.data = newSeriesData;
			allSeries.push(newSeries);
		}

		// Sort out plot Bands for days
		// plot band: from, to, color, label

		//"day 1, -0.5, 9; day 2, 9, 24; day 3, 24, 30.5";
		var plotBandData = testPlotBands.split(";");

		var color1 = 'rgba(0, 0, 0, 0.1)';

		var color2 = 'rgba(0, 0, 0, 0)';
		//color: 'rgba(68, 170, 213, 0.1)',

		var dayPlotBands = new Array();
		for ( var i = 0; i < plotBandData.length; i++) {
			var plotBandVals = plotBandData[i].split(",");

			var newPlotBand = new Object();
			newPlotBand.from = parseFloat(plotBandVals[1]);
			newPlotBand.to = parseFloat(plotBandVals[2]);

			var plotBandLabel = new Object();
			plotBandLabel.text = plotBandVals[0];
			newPlotBand.label = plotBandLabel;

			if (i % 2 == 0) {
				newPlotBand.color = color1;
			} else {
				newPlotBand.color = color2;
			}

			dayPlotBands.push(newPlotBand);
		}

		/*     $.each(items, function(itemNo, item) {
		           // if (itemNo == 0) {
		           //     series.name = item;
		           // } else {
		                data.push(parseInt(item));
		            //}
		        });*/

		$('#burnWeekContainer').highcharts(
				{

					series : //[{ name: 'steps', data: data }],
					allSeries,
					chart : {
						type : 'column',
						zoomType : 'x',
						spacingRight : 20

					},
					plotOptions : {
						series : {
							stacking : 'normal'
						}
					},
					title : {
						text : 'Burn in each location on ' + date
					},
					subtitle : {
						text : 'Source: Saga & BodyMedia'
					},
					xAxis : {
						//categories: ['','', '','','','5am', '', '', '', '9am', '', '', '12pm', '', '', '', '', '5pm', '', '', '', '9pm'],
						categories : labels,
						title : {
							text : null
						},
						labels : {
							rotation : -45,
							align : 'right',
							style : {
								fontSize : '13px',
								fontFamily : 'Verdana, sans-serif'
							}
						},
						plotBands : dayPlotBands
					},
					colors : [ '#FF9233', '#BC0000', '#44B9E8', '#92D050',
							'#ffff00', '#7030A0', '#f28f43', '#77a1e5',
							'#c42525', '#a6c96a' ]

				});
	});
}

function loadPolarBurnWeekData(date) {
	//Get the CSV and create the chart
	$.get('LocationBurnWeekGenreServlet?date=' + date, function(csv) {
		$('#text').text(csv);
		var lines = csv.split("||");

		//var items = lines[0].split(', ');
		var data = new Array();
		

		var testPlotBands = lines[1]; 

		var labels = lines[2].split(', ');
		


		var allSeries = new Array();

		for ( var i = 3; i < lines.length; i++) {
			var newSeries = new Object();

			var newSeriesData = new Array();

			var data = lines[i].split(', ');

			$.each(data, function(itemNo, item) {
				if (itemNo == 0) {
					newSeries.name = item;
				} else {
					newSeriesData.push(parseFloat(item));
				}
			});
			newSeries.data = newSeriesData;
			allSeries.push(newSeries);
		}

		// Sort out plot Bands for days
		// plot band: from, to, color, label

		//var testPlotBands = "day 1, -0.5, 9; day 2, 9, 24; day 3, 24, 30.5";
		var plotBandData = testPlotBands.split(";");

		var color1 = 'rgba(0, 0, 0, 0.1)';

		var color2 = 'rgba(0, 0, 0, 0)';
		//color: 'rgba(68, 170, 213, 0.1)',

		var dayPlotBands = new Array();
		for ( var i = 0; i < plotBandData.length; i++) {
			var plotBandVals = plotBandData[i].split(",");

			var newPlotBand = new Object();
			newPlotBand.from = parseFloat(plotBandVals[1]);
			newPlotBand.to = parseFloat(plotBandVals[2]);

			var plotBandLabel = new Object();
			plotBandLabel.text = plotBandVals[0];
			newPlotBand.label = plotBandLabel;

			if (i % 2 == 0) {
				newPlotBand.color = color1;
			} else {
				newPlotBand.color = color2;
			}

			dayPlotBands.push(newPlotBand);
		}

		/*     $.each(items, function(itemNo, item) {
		           // if (itemNo == 0) {
		           //     series.name = item;
		           // } else {
		                data.push(parseInt(item));
		            //}
		        });*/

		$('#burnWeekPolarContainer').highcharts(
				{

					series : //[{ name: 'steps', data: data }],
					allSeries,
					chart : {
						polar : true,
						type : 'column',
						zoomType : 'x',
						spacingRight : 20

					},
					plotOptions : {
						series : {
							stacking : 'normal'
						}
					},
					title : {
						text : 'Burn in each location on ' + date
					},
					subtitle : {
						text : 'Source: Saga & BodyMedia'
					},
					xAxis : {
						//categories: ['','', '','','','5am', '', '', '', '9am', '', '', '12pm', '', '', '', '', '5pm', '', '', '', '9pm'],
						categories : labels,
						title : {
							text : null
						},
						labels : {
							rotation : -45,
							align : 'right',
							style : {
								fontSize : '13px',
								fontFamily : 'Verdana, sans-serif'
							}
						},
						plotBands : dayPlotBands
					},
					colors : [ '#FF9233', '#BC0000', '#44B9E8', '#92D050',
							'#ffff00', '#7030A0', '#f28f43', '#77a1e5',
							'#c42525', '#a6c96a' ]

				});
	});
}

function loadLocationBands(date) {

	//Get the CSV and create the chart
	$.get('LocationDataServlet', function(csv) {
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

		$('#timeContainer').highcharts(
				{

					series : [ {
						name : 'Home1',
						data : data
					} ],
					chart : {
						type : 'column'
					},
					title : {
						text : 'Seconds spent in each Location'
					},
					subtitle : {
						text : 'Source: Saga'
					},
					xAxis : {
						//categories: ['','', '','','','5am', '', '', '', '9am', '', '', '12pm', '', '', '', '', '5pm', '', '', '', '9pm'],
						categories : labels,
						title : {
							text : null
						}
					},
					colors : [ '#FF9233', '#BC0000', '#44B9E8', '#92D050',
							'#ffff00', '#7030A0', '#f28f43', '#77a1e5',
							'#c42525', '#a6c96a' ]

				});

	});
}
