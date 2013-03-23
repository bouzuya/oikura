(ns oikura.view
  (:require [net.cgrand.enlive-html :as html])
  (:import [java.text DecimalFormat]))

; load html resource and remove unnecessary elements
; ex)
;   .product-list    .product-list
;     li               li
;     li          ->
;     li
(defn index-html
  []
  (html/transform
    (html/html-resource "oikura/view/index.html")
    [:.product-list]
    (fn [node]
      (assoc
        node
        :content
        (html/select node [[:li html/first-child]])))))

(defn asin-html
  []
  (html/html-resource "oikura/view/asin.html"))

(defn index-product
  []
  (html/snippet
    (index-html)
    [:.product]
    [context p]
    [:header :h1 :a]
    (html/set-attr :href (str context "/asin/" (:asin p)))
    [:header :h1 :a]
    (html/content (:asin p))
    [:.body :p :a]
    (html/set-attr :href (str context "/asin/" (:asin p)))
    [:.body :p :a :.product-chart :img]
    (html/set-attr :src (str context "/image/" (:asin p) "_t.png"))
    [:.body :p :a :.product-chart :img]
    (html/set-attr :alt (str "asin: " (:asin p) "のグラフ"))
    [:.body :p :a :.product-chart :img]
    (html/set-attr :width 300)
    [:.body :p :a :.product-chart :img]
    (html/set-attr :height 200)
    [:.body :.product-price :a]
    (html/set-attr :href (str "http://amazon.jp/o/ASIN/" (:asin p) "/bouzuya-22"))
    [:.body :.product-price :a :.product-price-value]
    (html/content (.format (DecimalFormat. "###,###,###,###,###") (:price p)))))

(defn asin-product
  []
  (html/snippet
    (asin-html)
    [:.product]
    [context p]
    [:header :h1 :a]
    (html/set-attr :href (str context "/asin/" (:asin p)))
    [:header :h1 :a]
    (html/content (:asin p))
    [:.body :p :a]
    (html/set-attr :href (str context "/asin/" (:asin p)))
    [:.body :p :a :.product-chart :img]
    (html/set-attr :src (str context "/image/" (:asin p) ".png"))
    [:.body :p :a :.product-chart :img]
    (html/set-attr :alt (str "asin: " (:asin p) "のグラフ"))
    [:.body :p :a :.product-chart :img]
    (html/set-attr :width 600)
    [:.body :p :a :.product-chart :img]
    (html/set-attr :height 400)
    [:.body :.product-price :a]
    (html/set-attr :href (str "http://amazon.jp/o/ASIN/" (:asin p) "/bouzuya-22"))
    [:.body :.product-price :a :.product-price-value]
    (html/content (.format (DecimalFormat. "###,###,###,###,###") (:price p)))))

(defn index
  [context products]
  (html/emit*
    (->
      (index-html)
      (html/transform
        [:.product-list :li]
        (html/clone-for
          [p products]
          [:.product]
          (html/substitute ((index-product) context p))))
      (html/transform
        [:head [:link (html/attr= :rel "stylesheet")]]
        (html/set-attr :href (str context "/style/default.css"))))))

(defn asin
  [context product]
  (html/emit*
    (->
      (asin-html)
      (html/transform
        [:.product]
        (html/substitute ((asin-product) context product)))
      (html/transform
        [:head [:link (html/attr= :rel "stylesheet")]]
        (html/set-attr :href (str context "/style/default.css"))))))

