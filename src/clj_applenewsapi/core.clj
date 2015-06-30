(ns clj-applenewsapi.core
  (:require [clj-http.client :as client]
            [clj-applenewsapi.crypto :refer [signature now canonical]]
            [clj-applenewsapi.multipart :as multipart]
            [clj-applenewsapi.parallel :as parallel]
            [clj-applenewsapi.config :as cfg]))

(def default-opts
  {
   ; :debug true
   :throw-exceptions false
   :accept :json
   :socket-timeout 60000
   :conn-timeout 60000
   :headers {}})

(defn post-opts [boundary payload]
  (-> default-opts
      (assoc :body payload)
      (assoc :content-type (str "multipart/form-data; boundary=" boundary))))

(defn authorize [opts concatenated ts channel-name]
  (let [secret (cfg/api-key-secret channel-name)
        keyid (cfg/api-key-id channel-name)
        digest (signature concatenated secret)
        auth (str "HHMAC; key=" keyid "; signature=" digest "; date=" ts)]
    (update-in opts [:headers] #(assoc % "Authorization" auth))))

(defn get-article
  ([id] (get-article id :sandbox))
  ([id channel-name]
   (let [url (str (cfg/host) "/articles/" id)
         ts (now)
         concatenated (canonical "GET" url ts)]
     (client/get url (authorize default-opts concatenated ts channel-name)))))

(defn delete-article
  ([id] (delete-article id :sandbox))
  ([id channel-name]
   (let [url (str (cfg/host) "/articles/" id)
         ts (now)
         concatenated (canonical "DELETE" url ts)]
     (client/delete url (authorize default-opts concatenated ts channel-name)))))

(defn get-channel
  ([] (get-channel :sandbox))
  ([channel-name]
   (let [url (str (cfg/host) "/channels/" (cfg/channel-id channel-name))
         ts (now)
         concatenated (canonical "GET" url ts)]
     (client/get url (authorize default-opts concatenated ts channel-name)))))

(defn get-section
  ([id] (get-section id :sandbox))
  ([id channel-name]
   (let [url (str (cfg/host) "/sections/" id)
         ts (now)
         concatenated (canonical "GET" url ts)]
     (client/get url (authorize default-opts concatenated ts channel-name)))))

(defn get-sections
  ([] (get-sections :sandbox))
  ([channel-name]
   (let [url (str (cfg/host) "/channels/" (cfg/channel-id channel-name) "/sections/")
         ts (now)
         concatenated (canonical "GET" url ts)]
     (client/get url (authorize default-opts concatenated ts channel-name)))))

(defn create-article
  ([bundle] (create-article bundle :sandbox))
  ([bundle channel-name]
   (let [url (str (cfg/host) "/channels/" (cfg/channel-id channel-name) "/articles")
         ts (now)
         boundary (multipart/random)
         payload (multipart/payload boundary bundle)
         content-type (str "multipart/form-data; boundary=" boundary)
         concatenated (canonical "POST" url ts content-type payload)
         opts (authorize (post-opts boundary payload) concatenated ts channel-name)]
     (client/post url opts))))

(defn create-articles
  "250 is the chunk size (that will spawn 250 threads) in case
  of big lists"
  ([bundles] (create-articles bundles :sandbox))
  ([bundles channel-name]
   (doall (parallel/ppmap #(create-article % channel-name) bundles 250))))

; test with
; (require '[clj-applenewsapi.core :as c]) (def bundle (read-string (slurp "test/bundle.edn")))  (c/create-article bundle :sandbox))
