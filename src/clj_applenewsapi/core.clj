(ns clj-applenewsapi.core
  (:require [clj-http.client :as client]
            [clj-applenewsapi.crypto :refer [signature now canonical]]
            [clj-applenewsapi.multipart :as multipart]
            [clj-applenewsapi.config :as cfg]))

(def default-opts
  {
   ; :debug true
   :accept :json
   :socket-timeout 10000
   :conn-timeout 10000
   :headers {}})

(defn post-opts [mpart]
  (-> default-opts
      (assoc :multipart mpart)))

(defn concatenation
  ([method url ts]
   (canonical method url ts))
  ([method url ts content-type content]
   (canonical method url ts content-type content)))

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
         concatenated (concatenation "GET" url ts)]
     (client/get url (authorize default-opts concatenated ts channel-name)))))

(defn get-channel
  ([] (get-channel :sandbox))
  ([channel-name]
   (let [url (str (cfg/host) "/channels/" (cfg/channel-id channel-name))
         ts (now)
         concatenated (concatenation "GET" url ts)]
     (client/get url (authorize default-opts concatenated ts channel-name)))))

(defn get-section
  ([id] (get-section id :sandbox))
  ([id channel-name]
   (let [url (str (cfg/host) "/sections/" id)
         ts (now)
         concatenated (concatenation "GET" url ts)]
     (client/get url (authorize default-opts concatenated ts channel-name)))))

(defn create-article
  ([bundle] (create-article bundle :sandbox))
  ([bundle channel-name]
   (let [url (str (cfg/host) "/channels/" (cfg/channel-id channel-name) "/articles")
         ts (now)
         raw-bundle (multipart/raw bundle)
         content-type (str "multipart/form-data; boundary=" (multipart/get-boundary raw-bundle))
         concatenated (concatenation "POST" url ts content-type raw-bundle)
         opts (authorize (post-opts (multipart/multipart bundle)) concatenated ts channel-name)]
     (client/post url opts))))
