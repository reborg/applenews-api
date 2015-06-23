(ns clj-applenewsapi.config
  (:require [environ.core :as environ]))

(def ^:dynamic *env* environ/env)

(defn env [] (:clj-applenewsapi *env*))

(defn host [] (:host (env)))

(defn api-key-id [channel-name]
  (get-in (env) [:channels channel-name :api-key-id]))

(defn api-key-secret [channel-name]
  (get-in (env) [:channels channel-name :api-key-secret]))

(defn channel-id [channel-name]
  (get-in (env) [:channels channel-name :channel-id]))

(defn opts-default [] {})
