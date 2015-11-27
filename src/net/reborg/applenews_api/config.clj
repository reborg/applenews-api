(ns net.reborg.applenews-api.config
  (:require [environ.core :as environ]))

(def ^:dynamic *env* environ/env)

(defn env [] (:applenews-api *env*))

(defn host [] (:host (env)))
(defn resize-thumbnail? [] (:thumbnail-resize-enable (env)))
(defn thumbnail-resize-height [] (:thumbnail-resize-height (env)))
(defn thumbnail-resize-width [] (:thumbnail-resize-width (env)))
(defn parallel [] (:parallel (env)))
(defn throw-exceptions [] (:throw-exceptions (env)))

(defn- channel-config [channel-name k]
 (get-in (env) [:channels (keyword channel-name) k]))

(defn api-key-id [channel-name]
  (channel-config channel-name :api-key-id))

(defn api-key-secret [channel-name]
  (channel-config channel-name :api-key-secret))

(defn channel-id [channel-name]
  (channel-config channel-name :channel-id))

(defn sections [channel-name]
  (channel-config (keyword channel-name) :sections))

(defn section
  ([channel-name] (section channel-name :default))
  ([channel-name section-name]
   ((keyword section-name) (sections channel-name))))
