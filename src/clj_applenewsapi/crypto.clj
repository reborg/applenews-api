(ns clj-applenewsapi.crypto
  (:require [clj-time.format :as tf]
            [clj-time.core :as t])
  (:import [org.apache.commons.codec.binary Base64]
           [javax.crypto.spec SecretKeySpec]
           [javax.crypto Mac]))

(defn now []
  (tf/unparse (tf/formatter  "YYYY-MM-dd'T'HH:mm:ss'Z'") (t/now)))

(defn canonical
  ([method url t] (str method url t))
  ([method url t content-type content] (str method url t content-type content)))

(defn signature [canonical secret]
  (let [decoded (Base64/decodeBase64 (.getBytes secret "UTF-8"))
        spec (SecretKeySpec. decoded "HmacSHA256")
        mac (doto (Mac/getInstance "HmacSHA256") (.init spec))
        hmac (.doFinal mac (.getBytes canonical "UTF-8"))]
    (String. (Base64/encodeBase64 hmac) "UTF-8")))
