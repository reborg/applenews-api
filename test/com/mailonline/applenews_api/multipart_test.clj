(ns com.mailonline.applenews-api.multipart-test
  (:require [midje.sweet :refer :all]
            [com.mailonline.applenews-api.bytes :as b]
            [com.mailonline.applenews-api.bundle :refer [load-edn]]
            [com.mailonline.applenews-api.multipart :refer :all]))

(facts "creating the payload"
       (fact "should split by the given boundary"
             (count (clojure.string/split (String. (payload "myboundary" (load-edn :bundle))) #"--myboundary")) => 10
             (provided (b/with-url anything anything) => (make-array Byte/TYPE 0))))
