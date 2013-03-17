(ns oikura.chart
  (:require [clojure.java.io :as jio])
  (:import [org.jfree.data.category DefaultCategoryDataset]
           [org.jfree.chart ChartFactory JFreeChart]
           [org.jfree.chart.plot PlotOrientation]
           [org.jfree.chart ChartUtilities]))

(defn image-dir
  []
  "image/")

(defn prices->dataset
  [prices]
  (let [ds (DefaultCategoryDataset.)]
    (doseq [{:keys [asin at price]} prices]
      (.addValue ds price asin at))
    ds))


(defn chart
  [asin dataset]
  (let [title asin
        category-axis-label "date"
        value-axis-label "price"
        orientation PlotOrientation/VERTICAL
        legend false
        tooltips false
        urls false]
    (ChartFactory/createLineChart
      title category-axis-label value-axis-label dataset orientation legend tooltips urls)))

(defn save-chart
  [asin prices]
  (let [name (str (image-dir) asin ".png")
        f (jio/file name)
        d (prices->dataset prices)
        c (chart asin d)
        w 600
        h 400]
    (ChartUtilities/saveChartAsPNG f c w h)))

