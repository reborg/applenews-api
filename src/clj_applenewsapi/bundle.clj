(ns clj-applenewsapi.bundle
  (:require [clojure.edn :as edn]
            [cheshire.core :as json]
            [clojure.string :refer [replace-first]]
            [clojure.java.io :as io]))

(defn load-edn [s]
  (edn/read-string (slurp (io/resource (str (name s) ".edn")))))

(defn metadata [bundle]
  (:content (first (filter #(= "metadata" (:name %)) bundle))))

(defn update-metadata [bundle metadata]
  "Here assuming metadata appears first"
  (assoc bundle 0 {:name "metadata", :content metadata}))

(defn article-json [bundle]
  (:content (first (filter #(= "article.json" (:filename %)) bundle))))

(defn thumbnail [bundle]
  (last (re-find #"\"thumbnailURL\"\:\"bundle\://(.*)\",\"canonicalURL" (article-json bundle))))

(defn revision-from-json [json]
  (last (re-find #"\"revision\":\"(.*)\"" json)))

(defn revision [bundle]
  (revision-from-json (metadata bundle)))

(defn update-revision [bundle revision]
  (let [metadata (json/decode (metadata bundle))]
    (update-metadata bundle (json/encode (update-in metadata ["data" "revision"] (fn [x] revision))))))
