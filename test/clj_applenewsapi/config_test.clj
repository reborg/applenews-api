(ns clj-applenewsapi.config-test
  (:require [midje.sweet :refer :all]
            [clj-applenewsapi.config :refer :all]))

(def rest-sample-config
  {:clj-applenewsapi {:host "https://testhost"
                      :thumbnail-resize-enable true
                      :thumbnail-resize-height 420
                      :thumbnail-resize-width 700
                      :channels {:ch1 {:channel-id "ch1-id"
                                       :api-key-id "ch1-key"
                                       :api-key-secret "ch1-secret"
                                       :sections {:default "ch1-section-default"
                                                  :another "ch1-section-another"}}
                                 :ch2 {:channel-id "ch2-id"
                                       :api-key-id "ch2-key"
                                       :api-key-secret "ch2-secret"
                                       :sections {:default "ch2-section-default"
                                                  :another "ch2-section-another"} }
                                 :ch3 {:channel-id "ch3-id"
                                       :api-key-id "ch3-key"
                                       :api-key-secret "ch3-secret"}}}})

(facts "rebindable configuration"
       (fact "it uses what is in the binding"
             (binding [*env* rest-sample-config]
               (host) => "https://testhost")))

(facts "other configuration goodies"
       (fact "thumbnail image resize"
             (binding [*env* rest-sample-config]
               (resize-thumbnail?) => true
               (thumbnail-resize-height) => 420
               (thumbnail-resize-width) => 700)))

(facts "channel configuration"
       (fact "key and secret are dependent on the channel"
             (binding [*env* rest-sample-config]
               (api-key-id :ch1) => "ch1-key"
               (api-key-id "ch1") => "ch1-key"
               (api-key-secret :ch2) => "ch2-secret"
               (channel-id :ch3) => "ch3-id")))

(facts "sections"
       (fact "section by name"
             (binding [*env* rest-sample-config]
               (section :ch1) => "ch1-section-default"
               (section "ch1") => "ch1-section-default"
               (section :ch1 :another) => "ch1-section-another"
               (sections :ch2) => {:default "ch2-section-default"
                                   :another "ch2-section-another"})))
