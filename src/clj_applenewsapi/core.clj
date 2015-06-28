(ns clj-applenewsapi.core
  (:require [clj-http.client :as client]
            [clj-applenewsapi.crypto :refer [signature now canonical]]
            [clj-applenewsapi.multipart :as multipart]
            [clj-applenewsapi.config :as cfg]))

(def default-opts
  {
   ; :debug true
   :accept :json
   :socket-timeout 20000
   :conn-timeout 20000
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
     (client/post url (assoc opts :throw-exceptions true)))))
