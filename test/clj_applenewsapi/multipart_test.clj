(ns clj-applenewsapi.multipart-test
  (:require [midje.sweet :refer :all]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clj-applenewsapi.multipart :refer :all]))

(defn stub [s]
  (edn/read-string (slurp (io/resource (str (name s) ".edn")))))

(def bundle
  [
   {:name "article" :filename "article.json" :content-type "application/json" :content (stub :article)}
   {:name "img1" :filename "img1.png" :content-type "image/png" :url "file:./test/test.png"}
   {:name "img2" :filename "img2.png" :content-type "image/png" :url "file:./test/test.png"}
   {:name "img3" :filename "img3.png" :content-type "image/png" :url "file:./test/test.png"}
   ])

(facts "multipart composition"
       (fact "the article is at the top"
             (:filename (first (multipart bundle))) => "article.json"
             (:name (first (multipart bundle))) => "article"))

(facts "mutlipart raw and utils"
       (fact "it renders as a string"
             (raw (stub :bundle)) => #"--(\S+)\r\n")
       (fact "it can get the boundary string out"
             (get-boundary (raw (stub :bundle))) => #"\S+"))
