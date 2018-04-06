package cn.edu.lingnan.sdk.enumeration;

import javafx.scene.chart.*;

/**
 * Created by Administrator on 2018/4/3.
 * @author feng
 *
 */
public enum ChartType {
    /**
     * 折线图
     */
    LINE_CHART(){
        @Override
        public XYChart getChart() {
            return new LineChart<>(new CategoryAxis(), new NumberAxis());
        }
    },

    /**
     * 柱形图
     *
     */
    BAR_CHAT(){
        @Override
        public XYChart getChart() {
            return new BarChart<>(new CategoryAxis(), new NumberAxis());
        }
    };
    public abstract XYChart<String, Number> getChart();
}
