(ns oikura.chart
  (:import [org.jfree.data.category DefaultCategoryDataset]
           [org.jfree.chart ChartFactory JFreeChart]
           [org.jfree.chart.plot PlotOrientation]
           [org.jfree.chart ChartUtilities]))

(defn prices->dataset
  [prices]
  (let [ds (DefaultCategoryDataset.)]
    (doseq [{:keys [asin at price]} prices]
      (.addValue ds price asin at))
    ds))

(defn chart
  [dataset]
  (ChartFactory/createLineChart
    nil nil nil dataset PlotOrientation/VERTICAL false false false))

(defn save-chart
  [file chart width height]
  (ChartUtilities/saveChartAsPNG file chart width height))

