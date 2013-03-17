(ns oikura.worker
  (:require [oikura.storage :as st]
            [oikura :as am])
  (:gen-class))

(defn -main
  []
  (doseq [{:keys [asin]} (st/product-all)]
    (let [{:keys [at price]} (am/fetch-price asin)]
      (println (apply str (interpose "," [asin at price])))
      (st/save-price asin at price))))

