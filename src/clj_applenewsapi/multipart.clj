(ns clj-applenewsapi.multipart
  (:require [clojure.java.io :as io]
            [clj-http.multipart :as mp])
  (:import [java.io ByteArrayOutputStream]))

(defn multipart [bundle]
  (letfn [(prepare [{:keys [name filename content-type content url] :as m}]
            {:name name
             :filename filename
             :content-type content-type
             ; :content (if url (io/input-stream url) content)})]
             :content (if url (io/input-stream "/me/prj/dm/clj-applenewsapi/test/test.png") content)})]
    (mapv prepare bundle)))

(defn raw [bundle]
  (let [baos (ByteArrayOutputStream.)]
    (-> (multipart bundle)
        (mp/create-multipart-entity)
        (.writeTo baos))
    (.toString baos "UTF-8")))

(defn get-boundary [raw-bundle]
  (last (re-find #"--(\S+)\r\n" raw-bundle)))
