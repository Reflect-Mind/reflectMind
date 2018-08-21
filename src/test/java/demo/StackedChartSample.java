package demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

/**
 * Created by Administrator on 2018/6/16.
 */
public class StackedChartSample extends Application {

    final static String austria = "Austria";
    final static String brazil = "Brazil";
    final static String france = "France";
    final static String italy = "Italy";
    final static String usa = "USA";

    @Override
    public void start(Stage stage) {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        // base chart
        final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");
        barChart.setOpacity(0.5);

        // overlay chart
        LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.getStylesheets().addAll(getClass().getResource("/css/chart.css").toExternalForm());

        barChart.getData().add( createChartSeries());
        lineChart.getData().add( createChartSeries());

        StackPane root = new StackPane();
        root.getChildren().addAll( barChart, lineChart);

        Scene scene = new Scene(root, 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    private XYChart.Series<String,Number> createChartSeries() {

        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
        series.getData().add(new XYChart.Data<String,Number>(austria, 25601.34));
        series.getData().add(new XYChart.Data<String,Number>(brazil, 20148.82));
        series.getData().add(new XYChart.Data<String,Number>(france, 10000));
        series.getData().add(new XYChart.Data<String,Number>(italy, 35407.15));
        series.getData().add(new XYChart.Data<String,Number>(usa, 12000));

        return series;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
