(ns oikura.storage
  (:require [clojure.java.jdbc :as jdbc])
  (:gen-class))

(defn setup
  []
  (jdbc/with-connection
    (System/getenv "OIKURA_DB")
    (do
      (jdbc/do-commands
        "DELETE FROM price")
      (jdbc/do-prepared
        "INSERT INTO price (asin, at, price) VALUES (?, TO_DATE(?, 'YYYY-MM-DD'), ?)"
        ["x" "2013-01-01" 1000]
        ["x" "2013-01-02" 1010]
        ["x" "2013-01-03" 1020]
        ["x" "2013-01-04" 1100]
        ["x" "2013-01-05" 1110]
        ["x" "2013-01-06" 1120]))))

(defn -main
  []
  (setup))

