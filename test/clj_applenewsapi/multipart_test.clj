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

; (def multi {:multipart [{:name "title" :content "My Awesome Picture"} {:name "Content/type" :content "image/png"} {:name "foo.txt" :part-name "eggplant" :content "Eggplants"} {:name "file" :content (clojure.java.io/file "/Users/renzo.borgatti/Desktop/pic.png")}]})

; (def bundle (read-string (slurp "test/bundle.edn")))
; (c/create-article bundle)
; (c/get-channel)
; (require '[clj-http.multipart :as mp])
; (require '[clj-applenewsapi.multipart :as mmp])
; (import 'java.io.ByteArrayOutputStream)
; (def baos (ByteArrayOutputStream.))
; (def entity (mp/create-multipart-entity (mmp/multipart bundle)))
; (.writeTo entity baos)
; (.toString baos "UTF-8")

#_(def images
  [["img1.jpg" "http://i.dailymail.co.uk/i/pix/2015/06/18/14/29BBC2B200000578-3129803-Before_Mrs_Howard_outside_the_property_in_Wiltshire_before_it_wa-a-36_1434633822823.jpg"]

   ["img2.jpg" "http://i.dailymail.co.uk/i/pix/2015/06/18/14/29BBE66C00000578-0-Elisabeth_Morgan_Rees_29_pictured_outside_court_today_kept_retur-m-115_1434634107614.jpg"]])

