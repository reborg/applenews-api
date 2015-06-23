(ns clj-applenewsapi.core-test
  (:require [midje.sweet :refer :all]
            [clj-applenewsapi.core :refer :all]
            [clojure.java.io :as io]
            [clj-applenewsapi.config :refer [*env*]]
            [clojure.edn :as edn]))

(def rest-sample-config
  {:clj-applenewsapi {:host "https://testhost"
               :channels {:ch1 {:channel-id "ch1-id"
                                :api-key-id "ch1-key"
                                :api-key-secret "ch1-secret"}}}})

(defn stub [s]
  (edn/read-string (slurp (io/resource (str (name s) ".edn")))))

(facts "retrieving an article"
       (fact "it should fetch the article by ID"
             (let [id "a3caeb08-b9db-4002-b379-cde305d74be7"]
               (binding [*env* rest-sample-config]
                 (get-in (get-article id :ch1) [:data :id])) => id
               (provided (clj-http.client/get anything anything) => (stub :article)))))

; (def url "http://localhost:3000/multi")
; (def url "http://httpbin.org/post")
; (def bundle [{:name "article" :filename "article.json" :content-type "application/json" :content "{:components {:some 'some'}}"} {:name "img1" :filename "img1.jpg" :content-type "image/jpeg" :url "http://i.dailymail.co.uk/i/pix/2015/06/18/14/29BBC2B200000578-3129803-Before_Mrs_Howard_outside_the_property_in_Wiltshire_before_it_wa-a-36_1434633822823.jpg"}])
; (require '[clj-applenewsapi.multipart :as multipart])
; (require '[clj-http.client :as client])
; (require '[clj-applenewsapi.core :as c])
; (client/post url (c/authorize (c/post-opts (multipart/multipart bundle)) "POST" url :uk))
