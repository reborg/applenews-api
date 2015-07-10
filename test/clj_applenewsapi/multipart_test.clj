(ns clj-applenewsapi.multipart-test
  (:require [midje.sweet :refer :all]
            [clj-applenewsapi.bytes :as b]
            [clj-applenewsapi.bundle :refer [load-edn]]
            [clj-applenewsapi.multipart :refer :all]))

(facts "creating the payload"
       (fact "should split by the given boundary"
             (count (clojure.string/split (String. (payload "myboundary" (load-edn :bundle))) #"--myboundary")) => 10
             (provided (b/with-url anything anything) => (make-array Byte/TYPE 0))))
