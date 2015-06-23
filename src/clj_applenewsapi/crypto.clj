(ns clj-applenewsapi.crypto
  (:require [pandect.algo.sha256 :refer [sha256-hmac]]
            [clojure.data.codec.base64 :refer [encode decode]]
            [clj-time.format :as tf]
            [clj-time.core :as t]))

(defn now []
  (tf/unparse (tf/formatter  "YYYY-MM-dd'T'HH:mm:ss'Z'") (t/now)))

(defn canonical [method url t]
  (str method url t))

(defn signature [c-url secret]
  (->> secret
       (.getBytes)
       (decode)
       (sha256-hmac c-url)
       (.getBytes)
       (encode)
       (String.)))
