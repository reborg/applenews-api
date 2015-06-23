(ns clj-applenewsapi.multipart
  (:require [clojure.java.io :as io]))

(defn multipart [bundle]
  (letfn [(prepare [{:keys [name filename content-type content url] :as m}]
            {:name name
             :filename filename
             :content-type content-type
             :content (if url (io/input-stream url) content)})]
    (mapv prepare bundle)))
