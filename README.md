# clj-applenewsapi

clj-applenewsapi is a Clojure client for the [Apple Publisher REST api](https://developer.apple.com/news-publisher/). Apart from offering basic wrapping around REST endpoints, it adds bulk creation/deletion in parallel and automatic thumbnail image resizing.

## How to use

Add:

```clojure
:dependencies [[clj-applenewsapi "0.1.0"]]
:plugins [[lein-environ "1.0.0"]]
```

to your project.clj dependencies section and. Here's an example snippet:

```clojure
    (ns your-prj
      (:require [clj-applenewsapi.core :as clj-applenewsapi]))

    ; retrieve an article by ID
    (clj-applenewsapi/get-article "a3caeb08-b9db-4002-b379-cde305d74be7")

    ; retrieve data for a channel
    (clj-applenewsapi/get-channel :mytestchannel)
```

## Configuration

clj-applenewsapi integrates with the leiningen environment map through [environ](https://github.com/weavejester/environ) for all general needs. It also  offers a programmatic way to override properties with a re-bindable dynamic *env* var.

### Configuring through `~/.lein/profiles.clj`

Configuring trough `~/.lein/profiles.clj` is the preferred method, so your credentials are always protected from an accidental source control commit. Add the following:

```clojure
{:user {
  :plugins []
  :env {:clj-applenewsapi {:thumbnail-resize-enable true
                           :thumbnail-resize-height 666
                           :thumbnail-resize-width 888
                           :parallel 100
                           :host "https://apple-new-service-host"
    :channels {:ch1 {:channel-id "aaaaaaaa-aaaa-bbbbb-cccc-dddddddddddd"
                     :api-key-id "aaaaaaaa-aaaa-bbbbb-cccc-dddddddddddd"
                     :api-key-secret "99999+p22222222222p34444444444444T2XnMyiNmI="}
               :ch2 {:channel-id "aaaaaaaa-aaaa-bbbbb-cccc-dddddddddddd"
                     :api-key-id "0936fc13-90eb-4391-8fbc-0cbc6104f3c"
                     :api-key-secret "99999+p22222222222p34444444444444T2XnMyiNmI="}}}}}}
```

### Configuring through a project local configuration file

A file called sample_profiles.clj is provided to you in the main clj-applenewsapi project directory:

* Copy sample_profiles.clj into your project root
* Rename sample_profiles.clj to profiles.clj
* Change the relevant values inside it to point to your Apple News key and secret
* Add the following lines inside your project.clj so clj-applenewsapi can pick up environment variables when running lein repl:

```clojure
:aliases {"repl" ["with-profile" "+dev-cfg" "repl"]}
```

* Create similar aliases for other lein tasks (e.g. test) as needed

### programmatic

If the configuration for your project is coming from other than the classpath, files or system environment (for example database or zookeeper) you can fetch the variables and pass them down to clj-applenewsapi as follow:

```clojure
(ns your-prj
  (:require [clj-applenewsapi.core :as clj-applenewsapi]
            [clj-applenewsapi.config :refer [*env*]]))

    ; retrieve all pools using a custom config
    (binding [*env* {:applenewsapi {:channel {…}}}]
      (clj-applenewsapi/get-article "articleid"))
```

### Image automatic resizing

Apple specification restricts the minimum size for the thumbnail image, the image that is used on the section along with a preview of the article. The image must be at minimum 600x400 with no maximum restrictions (other than 20MB total payload size to create an article). You can let applenewsapi to resize it for you, interpolating a smaller image to a slightly bigger one just enough to pass Apple checks. Use the configuration to do so by setting ":thumbnail-resize-enable" true. :thumbnail-resize-width and :thumbnail-resize-height are then used to resize the image. Image proportion will be kept, so based on portrait/landscape orientation the tool (thanks to Scalr) the settings for width/height might be applied differently than your constraint.

### Parallel creation/deletion

clj-applenewsapi supports parallel creation and deletion of articles. The :parallel parameter in configuration determines the amount of parallel threads that will be used for processing. The approach to parallelism is a simple parallel map, so the next n-amount of threads will fire when the last m-chunk has been completely processed. Expect a spike at the beginning of each chunk that fades off toward the end. For most of the practical purposes, this is fair enough.

### Certificate things

If you happen to incur in a "unable to find valid certification path to requested target" exception this is because the Apple Publisher Server certificate is not known in your certificate chain. Use the following to add the certificate. host must be replaced with the apple publisher server endpoint, port is 443:

```
openssl x509 -in <(openssl s_client -connect hostname:443 -prexit 2>/dev/null) -out ~/example.crt
sudo keytool -importcert -file ~/example.crt -alias example -keystore $(/usr/libexec/java_home)/jre/lib/security/cacerts -storepass changeit
```

## How to run the tests

`lein midje` will run all tests.

## TODO

* [ ] additional metadata for articles (related, featured sponsored)
* [ ] bulk delete delete API
* [ ] parallel bulk creation of articles
* [ ] Java interface
