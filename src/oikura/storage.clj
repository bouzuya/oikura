(ns oikura.storage
  (:require [clojure.java.jdbc :as jdbc]))

(defn db
  []
  (System/getenv "OIKURA_DB"))

(defn product-all
  []
  (jdbc/with-connection
    (db)
    (jdbc/with-query-results
      results
      ["SELECT asin FROM product ORDER BY asin ASC"]
      (into [] results))))

(defn save-price
  [asin at price]
  (jdbc/with-connection
    (db)
    (jdbc/do-prepared
      "DELETE FROM price WHERE asin = ? AND at = TO_DATE(?, 'YYYY-MM-DD')"
      [asin at])
    (jdbc/do-prepared
      "INSERT INTO price (asin, at, price) VALUES (?, TO_DATE(?, 'YYYY-MM-DD'), ?)"
      [asin at price])))

(defn price-all
  ([]
   (jdbc/with-connection
     (db)
     (jdbc/with-query-results
       results
       ["SELECT asin, at, price FROM price ORDER BY asin ASC, at ASC"]
       (into [] results))))
  ([asin]
   (jdbc/with-connection
     (db)
     (jdbc/with-query-results
       results
       ["SELECT asin, at, price FROM price WHERE asin = ? ORDER BY asin ASC, at ASC" asin]
       (into [] results)))))

