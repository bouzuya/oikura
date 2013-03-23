(ns oikura.worker
  (:require [clojure.java.io :as jio]
            [oikura.storage :as st]
            [oikura.chart :as ch]
            [oikura.amazon :as am]
            [oikura.config :as co])
  (:gen-class))

(defn save-price
  ([] (save-price (map :asin (st/product-all))))
  ([asins]
   (doseq [asin asins]
     (let [{:keys [at price]} (am/fetch-price asin)]
       (println (apply str (interpose "," [asin at price])))
       (st/save-price asin at price)))))

(defn save-chart
  ([] (save-chart (map :asin (st/product-all))))
  ([asins]
   (let [image-dir (co/config :image-dir)]
     (when-not (.exists image-dir)
       (.mkdirs image-dir))
     (doseq [asin asins]
       (let [prices (st/price-all asin)
             chart (ch/chart (ch/prices->dataset prices))]
         (println asin)
         (ch/save-chart (jio/file image-dir (str asin ".png")) chart 600 400)
         (ch/save-chart (jio/file image-dir (str asin "_t.png")) chart 300 200))))))

(defn -main
  []
  (co/load-config)
  (save-price))

