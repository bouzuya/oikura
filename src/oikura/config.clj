(ns oikura.config
  (:require [clojure.java.io :as jio])
  (:import [java.util Properties]))

(def config-map
  (ref {}))

(defn load-config
  []
  (let [root (jio/file (System/getProperty "user.home") ".oikura")
        m (merge {:root root
                  :image-dir (jio/file root "image")
                  :aws-access-key-id nil
                  :aws-secret-key nil
                  :db nil}
                 (load-properties (jio/file root "oikura.properties")))]
    (dosync (alter config-map merge m))))

(defn config
  [k]
  (@config-map k))

(defn load-properties
  [f]
  (into
    {}
    (map
      (fn [[k v]] [(keyword k) v])
      (with-open [r (jio/reader f)]
        (doto
          (Properties.)
          (.load r))))))

