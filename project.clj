(defproject oikura "0.4.0"
  :description "Amazon price searcher"
  :url "http://github.com/bouzuya/oikura"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :war-resources-path "resources/"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/data.zip "0.1.1"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [postgresql/postgresql "9.1-901.jdbc4"]
                 [jfree/jfreechart "1.0.13"]
                 [clj-http "0.6.5"]
                 [compojure "1.1.5"]
                 [enlive "1.1.1"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler oikura.handler/app}
  :main oikura.worker)

