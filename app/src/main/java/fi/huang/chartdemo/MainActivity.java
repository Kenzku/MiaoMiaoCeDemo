package fi.huang.chartdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.ValueDependentColor;

import java.util.Random;


public class MainActivity extends Activity {

    private ProgressBar theProgressBar;
    private GraphViewSeries exampleSeries;
    private int x = 5;
    private Activity thatActivity = this;
    private final int MAX_INTERVAL = 50;
    private LineGraphView graphView;
    private final int MAX_TEMPERATURE = 40;
    private final int MIN_TEMPERATURE = 34;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set progress bar
        theProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        theProgressBar.setMax(MAX_INTERVAL);
        theProgressBar.setProgress(0);

        // init example series data
        exampleSeries = new GraphViewSeries(new GraphViewData[] {
                new GraphViewData(1, 37.0d),
                new GraphViewData(2, 36.5d),
                new GraphViewData(3, 37.5d),
                new GraphViewData(4, 38.0d)
        });

        graphView = new LineGraphView(
                // context
                this,
                "MiaoMiaoCe Demo" // heading
        );
        graphView.addSeries(exampleSeries); // data
        graphView.setScalable(true);
        graphView.setViewPort(1, MAX_INTERVAL);
        graphView.getGraphViewStyle().setGridStyle(GraphViewStyle.GridStyle.HORIZONTAL);
        graphView.setDrawDataPoints(true);
        graphView.setDataPointsRadius(10f);
        GraphViewSeries.GraphViewSeriesStyle seriesStyle = new GraphViewSeries.GraphViewSeriesStyle();
        seriesStyle.setValueDependentColor(new ValueDependentColor() {
            @Override
            public int get(GraphViewDataInterface graphViewDataInterface) {
                // the higher the more red
                return Color.rgb((int)(150+((graphViewDataInterface.getY()/3)*100)), (int)(150-((graphViewDataInterface.getY()/3)*150)), (int)(150-((graphViewDataInterface.getY()/3)*150)));
            }
        });

        RelativeLayout theRelativeLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        theRelativeLayout.addView(graphView);

        (new Thread(new Task())).start();
    }

    class Task implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i <= MAX_INTERVAL; i++) {
                final int value = i;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                theProgressBar.setProgress(value);
                thatActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This code will always run on the UI thread, therefore is safe to modify UI elements.
                        int y = (new Random()).nextInt(MAX_TEMPERATURE - MIN_TEMPERATURE) + MIN_TEMPERATURE;
                        GraphViewData aGraphViewData = new GraphViewData(x, y);
                        Log.i("TAG", "This is y: " + String.valueOf(y));
                        exampleSeries.appendData(aGraphViewData, true, MAX_INTERVAL);
                        graphView.setViewPort(1, MAX_INTERVAL);
                        x = x + 1;
                    }
                });

            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
