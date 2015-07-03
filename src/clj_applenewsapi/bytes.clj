(ns clj-applenewsapi.bytes
  (:require [clojure.java.io :refer [copy]])
  (:import [java.io ByteArrayOutputStream ByteArrayInputStream FileInputStream]
           [java.net URL URLConnection]
           [java.util UUID]))

(defn to-bytes [^String s]
  (.getBytes s "UTF-8"))

(defn with-url
  "Execute f in the context of an open input stream for the url.
  f must be a function of an inputstream and an outputstream.
  It is expected that f process the inputstream from the URL in
  some meaningful way and dump the results into the provided
  outputstream."
  [url f]
  (with-open [out (java.io.ByteArrayOutputStream.)]
    (let [^URLConnection conn (doto
                                (.openConnection (URL. url))
                                (.setConnectTimeout 30000)
                                (.setReadTimeout 90000))]
      (f (.getInputStream conn) out)
      (.toByteArray out))))

(defn url-to-bytearray
  "Slurp the bytes from the url into a ByteArrayOuputStream and returns it."
  [url]
  (with-url url copy))
