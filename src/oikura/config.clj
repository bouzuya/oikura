(ns oikura.config
  (:require [clojure.java.io :as jio]))

(defn config
  []
  (let [root (jio/file (System/getProperty "user.home") ".oikura")]
    {:root root
     :image-dir (jio/file root "image")}))

