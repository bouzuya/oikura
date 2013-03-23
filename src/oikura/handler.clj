(ns oikura.handler
  (:require [clojure.java.io :as jio]
            [compojure.core :as cm]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [oikura.config :as co]
            [oikura.view :as view]
            [oikura.storage :as st]
            [oikura.worker :as wo]))

(defn product
  ([] (product (map :asin (st/product-all))))
  ([asins] (map st/product-latest asins)))

(defn index-page
  [r]
  (let [ps (product)]
    (view/index (:context r) ps)))

(defn asin-page
  [r asin]
  (let [ps (product [asin])]
    (view/asin (:context r) (first ps))))

(defn image-file
  [asin thumbnail?]
   (wo/save-chart [asin])
   {:status 200
    :headers {"Content-Type" "image/png"}
    :body (jio/file (co/config :image-dir) (str asin (when thumbnail? "_t") ".png"))})

(cm/defroutes
  app-routes
  (cm/GET "/" [:as r] (index-page r))
  (cm/GET ["/asin/:asin" :asin #"[a-zA-Z0-9]{10}"] [asin :as r] (asin-page r asin))
  (cm/GET "/image/:asin-png" [asin-png]
          (let [[_ asin thumbnail?] (re-matches #"([a-zA-Z0-9]{10})(_t)?\.png" asin-png)]
            (if asin
              (image-file asin thumbnail?)
              (route/not-found "Page not found"))))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site app-routes))

(defn init
  []
  (co/load-config))

