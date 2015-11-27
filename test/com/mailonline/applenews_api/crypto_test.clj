(ns com.mailonline.applenews-api.crypto-test
  (:require [midje.sweet :refer :all]
            [com.mailonline.applenews-api.config :refer [*env*]]
            [com.mailonline.applenews-api.crypto :refer :all]))

(def rest-sample-config
  {:clj-applenewsapi {:host "https://testhost"
               :channels {:ch1 {:channel-id "ch1-id"
                                :api-key-id "ch1-key"
                                :api-key-secret "ch1-secret"}
                          :ch2 {:channel-id "ch2-id"
                                :api-key-id "ch2-key"
                                :api-key-secret "ch2-secret"}
                          :ch3 {:channel-id "ch3-id"
                                :api-key-id "ch3-key"
                                :api-key-secret "ch3-secret"}}}})

(def expected "WI0eYmccNcN/SbTSfBfmzS2mQfG93rFFbioppg947RE=")

(facts "crypto facts"
       (fact "sanity check"
             (binding [*env* rest-sample-config]
               (signature (canonical
                            "GET"
                            "http://localhost:8080/channels/"
                            "2015-06-17T09:53:40Z") "ch1-secret")) => expected))
