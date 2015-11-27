(ns net.reborg.applenews-api.core-test
  (:require [midje.sweet :refer :all]
            [net.reborg.applenews-api.core :refer :all]
            [net.reborg.applenews-api.bundle :refer [load-edn]]
            [net.reborg.applenews-api.config :refer [*env*]])
  (:import [org.imgscalr Scalr]))

(def rest-sample-config
  {:applenews-api {:host "https://testhost"
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

; (require '[net.reborg.applenews-api.core :as c]) (def bundle (read-string (slurp "test/bundle.edn"))) (c/create-article bundle))
