(ns clj-applenewsapi.multipart-test
  (:require [midje.sweet :refer :all]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clj-applenewsapi.multipart :refer :all]))

(defn stub [s]
  (edn/read-string (slurp (io/resource (str (name s) ".edn")))))

(def bundle
  [{:name "article" :filename "article.json" :content-type "application/json" :content (stub :article-resize)}
   {:name "resize-me" :filename "resize-me.jpg" :content-type "image/jpeg" :url "file:./test/resize-me.jpg"}
   {:name "img2" :filename "img2.png" :content-type "image/png" :url "file:./test/test.png"}
   {:name "img3" :filename "img3.png" :content-type "image/png" :url "file:./test/test.png"}])

