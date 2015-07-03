(ns clj-applenewsapi.multipart-test
  (:require [midje.sweet :refer :all]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clj-applenewsapi.multipart :refer :all]))

(defn stub [s]
  (edn/read-string (slurp (io/resource (str (name s) ".edn")))))

(def bundle
  [
   {:name "article" :filename "article.json" :content-type "application/json" :content (stub :article-resize)}
   {:name "img1" :filename "img1.png" :content-type "image/png" :url "file:./test/resize-me.jpg"}
   {:name "img2" :filename "img2.png" :content-type "image/png" :url "file:./test/test.png"}
   {:name "img3" :filename "img3.png" :content-type "image/png" :url "file:./test/test.png"}
   ])

(facts "I was about to test that given a stored smaller image"
       (fact "it comes back bigger when the bundle is submitted. It's tricky to test but doable."
             (+ 2 2) => 4))
