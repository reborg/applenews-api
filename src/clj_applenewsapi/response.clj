(ns clj-applenewsapi.response
  (:require [clj-applenewsapi.bundle :refer [revision-from-json]]
            [clojure.string :refer [split join lower-case]]
            [clojure.walk :refer [postwalk]]
            [cheshire.core :as json]))

(defn camel->dashed [s]
  "Convert a camel cased string to a hyphenated lowercase string."
  (->> (split s #"(?<=[a-z])(?=[A-Z$])")
       (map lower-case)
       (join \-)))

(defn clojure-keys [m]
  "Recursively transforms all map keys from camelcase strings to clojure keywords"
  (letfn [(f [[k v]]
            (if (string? k)
              [(keyword (camel->dashed k)) v]
              [k v]))]
    (postwalk (fn [x]
                (if (map? x)
                  (into {} (map f x))
                  x)) m)))

(defn- end-of-path [s]
  (when s (last (split s #"/"))))

(defn- remove-nils [m]
  (into {} (remove (comp nil? second) m)))

(def interesting-bits
  ["shareUrl" "revision" "isCandidateToBeFeatured" "createdAt" "type" "isSponsored" "id" "modifiedAt"])

(defn enrich [r & [defaults]]
  "Takes the response and an optional map of defaults and enrich the
  response with other interesting bits coming from the json body. Keys
  from the json body are taking precedence over the defaults when found."
  (let [json (json/decode (:body r) false)
        apple-keys (clojure-keys (select-keys (json "data") interesting-bits))]
    (remove-nils (-> (merge r defaults)
                     (merge r apple-keys)
                     (assoc :http-response-date ((:headers r) "Date"))
                     (assoc :channel-id (end-of-path (get-in json ["data" "links" "channel"])))
                     (assoc :section-ids (map end-of-path (get-in json ["data" "links" "sections"])))
                     (assoc :article-id (get-in json ["data" "document" "identifier"]))))))
