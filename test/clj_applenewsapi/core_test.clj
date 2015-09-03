(ns clj-applenewsapi.core-test
  (:require [midje.sweet :refer :all]
            [clj-applenewsapi.core :refer :all]
            [clj-applenewsapi.bundle :refer [load-edn]]
            [clj-applenewsapi.config :refer [*env*]])
  (:import [org.imgscalr Scalr]))

(def rest-sample-config
  {:clj-applenewsapi {:host "https://testhost"
                      :channels {:sandbox {:channel-id "sandbox-id"
                                           :api-key-id "sandbox-key"
                                           :api-key-secret "sandbox-secret"}
                                 :ch1 {:channel-id "ch1-id"
                                       :api-key-id "ch1-key"
                                       :api-key-secret "ch1-secret"}}}})

(facts "retrieving an article"
       (fact "it should fetch the article by ID"
             (let [id "69148703-cc53-4c5a-b87c-d93a48dc82ee"]
               (binding [*env* rest-sample-config]
                 (get-in (get-article id :ch1) [:id])) => id
               (provided (clj-http.client/get anything anything) => (load-edn :article))))
       (fact "it defaults to sandbox when no channel is given"
             (let [id "69148703-cc53-4c5a-b87c-d93a48dc82ee"]
               (binding [*env* rest-sample-config]
                 (get-in (get-article id) [:id])) => id
               (provided
                 (authorize anything anything anything :sandbox) => "whateva"
                 (clj-http.client/get anything anything) => (load-edn :article)))))

; (require '[clj-applenewsapi.core :as c]) (def bundle (read-string (slurp "test/bundle.edn"))) (c/create-article bundle))
