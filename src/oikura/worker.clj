(ns oikura.worker
  (:require [oikura.storage :as st]
            [oikura.chart :as ch]
            [oikura :as am])
  (:gen-class))

(defn save-price-all
  []
  (doseq [{:keys [asin]} (st/product-all)]
    (let [{:keys [at price]} (am/fetch-price asin)]
      (println (apply str (interpose "," [asin at price])))
      (st/save-price asin at price))))

(defn save-chart-all
  []
  (doseq [{:keys [asin]} (st/product-all)]
    (let [prices (st/price-all asin)]
      (println asin prices)
      (ch/save-chart asin prices))))

(defn -main
  []
  (save-price-all)
  (save-chart-all))

