(ns oikura
  (:require [clj-http.client :as client]
            [clojure.data.zip.xml :as dzx]
            [clojure.xml :as xml]
            [clojure.zip :as zip])
  (:gen-class))

(def ^:dynamic *aws-access-key-id* nil)
(def ^:dynamic *aws-secret-key* nil)
(def algorithm "HmacSHA256")

(defn hmac
  [data]
  (let [key (javax.crypto.spec.SecretKeySpec.
              (.getBytes *aws-secret-key* "UTF-8")
              algorithm)]
    (->
      (doto
        (javax.crypto.Mac/getInstance algorithm)
        (.init key))
      (.doFinal data))))

(defn hmac-string
  [s]
  (->
    (.getBytes s "UTF-8")
    hmac
    (#(.encode (org.apache.commons.codec.binary.Base64.) %))
    (String.)))

(defn timestamp
  []
  (->
    (doto
      (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss'Z'")
      (.setTimeZone (java.util.TimeZone/getTimeZone "GMT")))
    (.format (.getTime (java.util.Calendar/getInstance)))))

(defn encode
  [s]
  (->
    s
    (java.net.URLEncoder/encode "UTF-8")
    (.replace "+" "%20")
    (.replace "*" "%2A")
    (.replace "%7E" "~")))

(defn canonicalize
  [m]
  (->>
    m
    (into (sorted-map))
    (map (fn [[k v]] (str (encode k) "=" (encode v))))
    (interpose "&")
    (apply str)))


(defn sign
  [http-verb endpoint request-uri params]
  (let [all-params (merge params {"AWSAccessKeyId" *aws-access-key-id*
                                  "Timestamp" (timestamp)})
        canonicalized (canonicalize all-params)
        signature (encode (hmac-string (str http-verb "\n"
                                            endpoint "\n"
                                            request-uri "\n"
                                            canonicalized)))]
    (into params {"Signature" signature})))

(defn parse-xml-str
  [xml-str]
  (->
    xml-str
    (java.io.StringReader.)
    (org.xml.sax.InputSource.)
    xml/parse))

(defn -main
  [& args]
  (binding [*aws-access-key-id* (System/getenv "AWS_ACCESS_KEY_ID")
            *aws-secret-key* (System/getenv "AWS_SECRET_KEY")]
    (println
      (->
        (client/post
          "https://ecs.amazonaws.jp/onca/xml"
          {:form-params
           (sign "POST"
                 "ecs.amazonaws.jp"
                 "/onca/xml"
                 {"Operation" "ItemLookup"
                  "Service" "AWSECommerceService"
                  "AssociateTag" "bouzuya-22"
                  "ItemId" (first args)
                  "RelationshipType" "Episode"
                  "ResponseGroup" "OfferSummary"})})
        :body
        parse-xml-str
        zip/xml-zip
        (dzx/xml1->
          :Items
          :Item
          :OfferSummary
          :LowestNewPrice
          :Amount
          dzx/text)))))

