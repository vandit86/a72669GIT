/**
* autor : VADIM 
* descr : run tread that represent graficly In And Out Octets for each interface 
* 
*/

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List; 

import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.QuickChart; //
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel; //
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle; //
import org.knowm.xchart.style.Styler.LegendPosition;



public class SNMPGraff{

	SNMPManager snmpManager;
	int ifNum = 1;
	int reqTime = 1500;

	//  
	final String SERIES_IN =  "InData"; 
	final String SERIES_OUT = "OutData";

	private final int DATA_SIZE = 30;

	public SNMPGraff(SNMPManager snmpManager){
		this.snmpManager = snmpManager;
	}

	public SNMPGraff(SNMPManager snmpManager, int requestTime){
		this.snmpManager = snmpManager;
		this.reqTime = requestTime;		// update data every reqTime milliseconds  
	}

	// Set time delay in milisecond to update information on grafic
	// default value is set  
	public void setReqTime (int time){
		this.reqTime = time ; 
	}

	public void start () {

		List<XYChart> charts = new ArrayList<XYChart>();
		List<Interface> ifList = snmpManager.getIfList();	// get list of all interafecs from menager 

		// Create Chart for all interfaces
		for (Interface inf : ifList){
			XYChart chart = getChart("Type :"+inf.getTypeAsString()+" : "+ inf.getOperStatusAsString());
			charts.add(chart);	
		}

    	// Show it
		final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(charts);
		sw.displayChartMatrix();
		
		// Simulate a data feed
		TimerTask chartUpdaterTask = new TimerTask() {
			@Override
			public void run()  {
				try {
					for (Interface inf : ifList){
						snmpManager.updateData(inf);	// update Data for all interafeces 
					}
				}catch (Exception e){
					e.printStackTrace();
				}

				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i< ifList.size(); i++ ){
							charts.get(i).updateXYSeries(SERIES_IN, null,ifList.get(i).getInData(), null);
							charts.get(i).updateXYSeries(SERIES_OUT, null,ifList.get(i).getOutData(), null);
							sw.repaintChart(i);
						}
					}
				});
			}
		};

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 0, reqTime);


	}


    // Create Chart
	public XYChart getChart(String status) {
		//theme(ChartTheme.Matlab)
		XYChart xyChart = new XYChartBuilder().width(600).height(500)
		.title(status).build();
		
		//xyChart.getStyler().setLegendVisible(false);
		xyChart.getStyler().setLegendPosition(LegendPosition.InsideNE);
		xyChart.getStyler().setAxisTitlesVisible(false);
		//xyChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
		
		xyChart.addSeries(SERIES_IN, null,new double [DATA_SIZE]);
		xyChart.addSeries(SERIES_OUT,null,new double [DATA_SIZE]);

		return xyChart;
	}

}