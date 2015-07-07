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

(defn resize-thumbnail? [] (:thumbnail-resize-enable (env)))
(defn thumbnail-resize-height [] (:thumbnail-resize-height (env)))
(defn thumbnail-resize-width [] (:thumbnail-resize-width (env)))
