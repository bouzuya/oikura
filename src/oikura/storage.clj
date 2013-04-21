(ns oikura.storage
  (:require [clojure.java.jdbc :as jdbc]
            [oikura.config :as co]))

(defn product-latest
  [asin]
  (jdbc/with-connection
    (co/config :db)
    (jdbc/with-query-results
      results
      ["SELECT asin, TO_CHAR(at, 'YYYY-MM-DD') AS at, price FROM price a WHERE a.asin = ? AND a.at = (SELECT MAX(b.at) FROM price b WHERE b.asin = a.asin)" asin]
      (if (first results)
        (first results)
        {:asin asin}))))

(defn product-all
  []
  (jdbc/with-connection
    (co/config :db)
    (jdbc/with-query-results
      results
      ["SELECT asin FROM product ORDER BY asin ASC"]
      (into [] results))))

(defn save-price
  [asin at price]
  (jdbc/with-connection
    (co/config :db)
    (jdbc/do-prepared
      "DELETE FROM price WHERE asin = ? AND at = TO_DATE(?, 'YYYY-MM-DD')"
      [asin at])
    (jdbc/do-prepared
      "INSERT INTO price (asin, at, price) VALUES (?, TO_DATE(?, 'YYYY-MM-DD'), TO_NUMBER(?, '999999999999'))"
      [asin at price])))

(defn register-product
  [asin]
  (jdbc/with-connection
    (co/config :db)
    (jdbc/do-prepared
      "INSERT INTO product (asin) VALUES (?)"
      [asin])))

(defn price-all
  ([]
   (jdbc/with-connection
     (co/config :db)
     (jdbc/with-query-results
       results
       ["SELECT asin, at, price FROM price ORDER BY asin ASC, at ASC"]
       (into [] results))))
  ([asin]
   (jdbc/with-connection
     (co/config :db)
     (jdbc/with-query-results
       results
       ["SELECT asin, at, price FROM price WHERE asin = ? ORDER BY asin ASC, at ASC" asin]
       (into [] results)))))

