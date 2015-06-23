(ns clj-applenewsapi.config-test
  (:require [midje.sweet :refer :all]
            [clj-applenewsapi.config :refer :all]))

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

(facts "rebindable configuration"
       (fact "it uses what is in the binding"
             (binding [*env* rest-sample-config]
               (host) => "https://testhost")))

(facts "channel configuration"
       (fact "key and secret are dependent on the channel"
             (binding [*env* rest-sample-config]
               (api-key-id :ch1) => "ch1-key"
               (api-key-secret :ch2) => "ch2-secret"
               (channel-id :ch3) => "ch3-id")))
