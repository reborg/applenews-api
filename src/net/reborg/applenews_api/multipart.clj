(ns net.reborg.applenews-api.multipart
  (:require [clojure.java.io :as io]
            [clj-http.multipart :as mp]
            [clojure.java.io :refer [copy]]
            [net.reborg.applenews-api.image :as image]
            [net.reborg.applenews-api.bundle :refer [metadata article-json thumbnail]]
            [net.reborg.applenews-api.bytes :refer [to-bytes url-to-bytearray]])
  (:import [java.io ByteArrayOutputStream ByteArrayInputStream FileInputStream]
           [java.net URL URLConnection]
           [java.util UUID]))

(set! *warn-on-reflection* true)

(defn random []
  (.replaceAll (str (UUID/randomUUID)) "-" ""))

(defn part
  "Create the part composing all the necessary pieces
  together into a byte-array. Payload is expected to be [B"
  [boundary c-type c-name ^bytes payload out]
  (let [header (str (format "--%s\r\n" boundary)
                    (format "Content-Type: %s\r\n" c-type)
                    (format "Content-Disposition: form-data; name=%s; filename=%s; size=%s\r\n" c-name c-name (alength payload)))
        return "\r\n"]
    (copy (to-bytes header) out)
    (copy (to-bytes return) out)
    (copy payload out)
    (copy (to-bytes return) out)))

(defn embed-parts
  "All other mime parts other than article and metadata,
  like images, fonts or other binaries. Optionally takes a
  thumbnail url image that when present will be checked for
  minimum size requirements and interpolated accordingly."
  ([boundary part-urls out]
   (embed-parts boundary part-urls out nil))
  ([boundary part-urls out thumbnail-url]
   (doseq [part-url part-urls]
     (part boundary
           (image/mime-type part-url)
           (image/file-name part-url)
           (if (image/thumbnail? thumbnail-url part-url)
             (image/adjust-size part-url)
             (url-to-bytearray part-url)) out))))

(defn payload [boundary bundle]
  (let [part-urls (remove nil? (mapv :url bundle))]
    (with-open [out (ByteArrayOutputStream.)]
      (part boundary "application/json" "metadata" (to-bytes (metadata bundle)) out)
      (part boundary "application/json" "article.json" (to-bytes (article-json bundle)) out)
      (embed-parts boundary part-urls out (thumbnail bundle))
      (copy (to-bytes (format "--%s--" boundary)) out)
      (.toByteArray out))))
