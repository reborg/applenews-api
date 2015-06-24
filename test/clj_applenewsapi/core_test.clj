(ns clj-applenewsapi.core-test
  (:require [midje.sweet :refer :all]
            [clj-applenewsapi.core :refer :all]
            [clojure.java.io :as io]
            [clj-applenewsapi.config :refer [*env*]]
            [clojure.edn :as edn]))

(def rest-sample-config
  {:clj-applenewsapi {:host "https://testhost"
                      :channels {:sandbox {:channel-id "sandbox-id"
                                           :api-key-id "sandbox-key"
                                           :api-key-secret "sandbox-secret"}
                                 :ch1 {:channel-id "ch1-id"
                                       :api-key-id "ch1-key"
                                       :api-key-secret "ch1-secret"}}}})

(defn stub [s]
  (edn/read-string (slurp (io/resource (str (name s) ".edn")))))

(facts "retrieving an article"
       (fact "it should fetch the article by ID"
             (let [id "a3caeb08-b9db-4002-b379-cde305d74be7"]
               (binding [*env* rest-sample-config]
                 (get-in (get-article id :ch1) [:data :id])) => id
               (provided (clj-http.client/get anything anything) => (stub :article))))
       (fact "it defaults to sandbox when no channel is given"
             (let [id "a3caeb08-b9db-4002-b379-cde305d74be7"]
               (binding [*env* rest-sample-config]
                 (get-in (get-article id) [:data :id])) => id
               (provided
                 (authorize anything anything anything :sandbox) => "whateva"
                 (clj-http.client/get anything anything) => (stub :article)))))

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
