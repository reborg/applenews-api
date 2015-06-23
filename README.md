# clj-applenewsapi

clj-applenewsapi is a Clojure client for the [Apple Publisher REST api](https://developer.apple.com/news-publisher/).

## Confidentiality Note

*PLEASE NOTE*: at the moment of writing this first release, the Apple News Push API and related native formats are under NDA. The detail of the API and the overall details of the Apple News app should therefore not be disclosed publicly in any way.

## How to use

Add:

        [clj-applenewsapi "0.1.0"]

to your project.clj. Here's an example snippet:

```clojure
    (ns your-prj
      (:require [clj-applenewsapi.core :as clj-applenewsapi]))

    ; retrieve an article by ID
    (clj-applenewsapi/article "a3caeb08-b9db-4002-b379-cde305d74be7")
```
## Configuration

See the options below. clj-applenewsapi integrates with leiningen environment map through [environ](https://github.com/weavejester/environ) for all local development needs. It also accepts overrides as environment properties or JVM properties for production environment. Finally, it offers a programmatic way to override properties with a rebindable dynamic *env* var.

If you pick "development" or "production" style below, you will need "lein-environ" dependencies in your project.clj :plugins section (lein-environ is responsible for reading environments).

### development

A file called sample_profiles.clj is provided to you in the main clj-applenewsapi project directory:

* Copy sample_profiles.clj into your project root
* Rename sample_profiles.clj to profiles.clj
* Change the relevant values inside it to point to your Apple News key and secret
* Add the following lines inside your project.clj so clj-applenewsapi can pick up envirnment variables when running lein repl:

```clojure
:aliases {"repl" ["with-profile" "+dev-cfg" "repl"]}
```

* Create similar aliases for other lein tasks (e.g. test) as needed
* Alternatively, the same variables can be added to ~/.lein/profiles.clj or the project.clj in your project:

```clojure
{:clj-applenewsapi {:host "https://testhost"
                    :channels {:ch1 {:channel-id "ch1-id"
                                     :api-key-id "ch1-key"
                                     :api-key-secret "ch1-secret"}
                               :ch2 {:channel-id "ch2-id"
                                     :api-key-id "ch2-key"
                                     :api-key-secret "ch2-secret"}
                               :ch3 {:channel-id "ch3-id"
                                     :api-key-id "ch3-key"
                                     :api-key-secret "ch3-secret"}}}}
```

### programmatic

If the configuration for your project is coming from other than the classpath, files or system environment (for example a db or zookeeper) you can fetch the variables and pass them down to clj-applenewsapi as follow:

```clojure
(ns your-prj
  (:require [clj-applenewsapi.core :as clj-applenewsapi]
            [clj-applenewsapi.config :refer [*env*]]))

    ; retrieve all pools using a custom config
    (binding [*env* {}]
      (clj-applenewsapi/article "articleid"))
```

### Certificate things

If you happen to incur in a "unable to find valid certification path to requested target" exception (because the target server is returning an untrusted certificate) do the following, replacing hostname:port with the target server hostname and port:

```
openssl x509 -in <(openssl s_client -connect hostname:port -prexit 2>/dev/null) -out ~/example.crt
sudo keytool -importcert -file ~/example.crt -alias example -keystore $(/usr/libexec/java_home)/jre/lib/security/cacerts -storepass changeit
```

## How to run the tests

`lein midje` will run all tests.

## TODO

* [x] hash-based message authentication
* [x] image encoding helpers for multipart mime POST
* [x] include signatures in headers
* [ ] default to sandbox when no channel specified
* [ ] other APIs
* [ ] Need optional links for posting article multiple sections
* [ ] multithreaded POST for bulk import
* [ ] bulk import: given list of IDS bulk post them all
