(ns net.reborg.applenews-api.multipart-test
  (:require [midje.sweet :refer :all]
            [net.reborg.applenews-api.bytes :as b]
            [net.reborg.applenews-api.bundle :refer [load-edn]]
            [net.reborg.applenews-api.multipart :refer :all]))

(facts "creating the payload"
       (fact "should split by the given boundary"
             (count (clojure.string/split (String. (payload "myboundary" (load-edn :bundle))) #"--myboundary")) => 10
             (provided (b/with-url anything anything) => (make-array Byte/TYPE 0))))
