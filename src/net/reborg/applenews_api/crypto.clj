(ns net.reborg.applenews-api.crypto
  (:require [clj-time.format :as tf]
            [net.reborg.applenews-api.bytes :refer [to-bytes]]
            [clojure.java.io :refer [copy]]
            [clj-time.core :as t])
  (:import [org.apache.commons.codec.binary Base64]
           [java.io ByteArrayOutputStream]
           [javax.crypto.spec SecretKeySpec]
           [javax.crypto Mac]))

(set! *warn-on-reflection* true)

(defn now []
  (tf/unparse (tf/formatter "YYYY-MM-dd'T'HH:mm:ss'Z'") (t/now)))

(defn canonical
  ([method url t]
   (to-bytes (str method url t)))
  ([method url t content-type payload]
   (with-open [out (ByteArrayOutputStream.)]
     (copy (to-bytes (str method url t content-type)) out)
     (copy payload out)
     (.toByteArray out))))

(defn signature [^bytes canonical ^String secret]
  (let [decoded (Base64/decodeBase64 (.getBytes secret "UTF-8"))
        spec (SecretKeySpec. decoded "HmacSHA256")
        mac (doto (Mac/getInstance "HmacSHA256") (.init spec))
        hmac (.doFinal mac canonical)]
    (String. (Base64/encodeBase64 hmac) "UTF-8")))
