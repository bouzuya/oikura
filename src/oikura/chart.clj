(ns oikura.chart
  (:require [clojure.java.io :as jio])
  (:import [org.jfree.data.category DefaultCategoryDataset]
           [org.jfree.chart ChartFactory JFreeChart]
           [org.jfree.chart.plot PlotOrientation]
           [org.jfree.chart ChartUtilities]))

; http://java6.blog117.fc2.com/blog-entry-28.html

(def dataset
  (doto
    (DefaultCategoryDataset.)
    (.addValue 1000 "x" "2013-01-01")
    (.addValue 1010 "x" "2013-01-02")
    (.addValue 1020 "x" "2013-01-03")
    (.addValue 1100 "x" "2013-01-04")
    (.addValue 1110 "x" "2013-01-05")
    (.addValue 1120 "x" "2013-01-06")))

(def jfree-chart
  (let [title "PRICE"
        category-axis-label "product"
        value-axis-label "price"
        orientation PlotOrientation/VERTICAL
        legend false
        tooltips false
        urls false]
    (ChartFactory/createLineChart
      title category-axis-label value-axis-label dataset orientation legend tooltips urls)))

(defn save-chart
  []
  (ChartUtilities/saveChartAsPNG (jio/file "test.png") jfree-chart 600 400))

