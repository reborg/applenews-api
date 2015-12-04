# applenews-api

The applenews-api is a Java/Clojure client library for the [Apple News REST api](https://developer.apple.com/go/?id=news-api-ref). The REST Api along with the [Apple News Format](https://developer.apple.com/library/ios/documentation/General/Conceptual/Apple_News_Format_Ref/index.html) gives news publishers a great deal of flexibility in designing their content for the Apple News platform. This library is designed to help news publishers pushing their content to Apple News, offering Java or Clojure wrapping around REST endpoints, parallel creation/deletion, automatic image resizing and much more.

## Project Setup

From Java, put the following in your Maven pom.xml:

```xml
<dependency>
  <groupId>net.reborg</groupId>
  <artifactId>applenews-api</artifactId>
  <version>1.0.0</version>
</dependency>
```
From Clojure, put the following dependency in your Leiningen project.clj:

```clojure
:dependencies [[net.reborg/applenews-api "0.1.0"]]
:plugins [[lein-environ "1.0.0"]]
```

## How to use

From Java:

```java
import clojure.lang.Keyword;
import net.reborg.AppleNews;
import java.util.Map;
import static net.reborg.AppleNews.*;

public class Main {
    private static Map<Keyword, Object> resp;

    public static void main(String[] args) {
        resp = AppleNews.createArticle(loadSampleBundle());
        printResponse("createArticle 1-arg", resp);
        String id = getId(resp);
        resp = getArticle(id);
        printResponse("getArticle 1-arg", resp);
    }
}
```

From Clojure:

```clojure
(ns your-prj
  (:require [net.reborg.applenews-api :as api]))

; retrieve an article by ID
(api/get-article "a3caeb08-b9db-4002-b379-cde305d74be7")

; retrieve data for a channel
(api/get-channel :mytestchannel)
```

## Java Configuration

A file called config.edn needs to be in the classpath. An example of the content of the file can be found in "sample-java/src/main/resources/config.edn":

```clojure
{:applenews-api {:thumbnail-resize-enable true
                 :thumbnail-resize-height 666
                 :thumbnail-resize-width 888
                 :parallel 100
                 :host "https://news-api.apple.com"
                 :throw-exceptions false
                 :channels {:sandbox
                            {:channel-id "your-channel-id-here",
                             :api-key-id "your-api-key-here",
                             :api-key-secret "your-api-secret-here",
                             :sections
                             {:s1 "your-seaction-id-here",
                              :s2 "your-seaction-id-here"}}}}}
```

## Clojure Configuration

applenews-api integrates with the leiningen environment map through [environ](https://github.com/weavejester/environ) for all general needs. It also  offers a programmatic way to override properties with a re-bindable dynamic *env* var.

### Configuring through `~/.lein/profiles.clj`

Configuring trough `~/.lein/profiles.clj` is the preferred method, so your credentials are always protected from an accidental source control commit. Add the following:

```clojure
{:user {
  :plugins []
  :env {:applenews-api {:thumbnail-resize-enable true
                           :thumbnail-resize-height 666
                           :thumbnail-resize-width 888
                           :parallel 100
                           :host "https://apple-new-service-host"
                           :throw-exceptions false
    :channels {:ch1 {:channel-id "aaaaaaaa-aaaa-bbbbb-cccc-dddddddddddd"
                     :api-key-id "aaaaaaaa-aaaa-bbbbb-cccc-dddddddddddd"
                     :api-key-secret "99999+p2222p3444444444T2XnMyiNmI="
                     :sections {:default "88487-sdflkj-33433"
                                :another "9879845-erfds-45"}
               :ch2 {:channel-id "aaaaaaaa-aaaa-bbbbb-cccc-dddddddddddd"
                     :api-key-id "0936fc13-90eb-4391-8fbc-0cbc6104f3c"
                     :api-key-secret "99999+p222222234444T2XnMyiNmI="}}}}}}
```

### Configuring through a project local configuration file

A file called sample_profiles.clj is provided to you in the main applenews-api project directory:

* Copy sample_profiles.clj into your project root
* Rename sample_profiles.clj to profiles.clj
* Change the relevant values inside it to point to your Apple News key and secret
* Add the following lines inside your project.clj so applenews-api can pick up environment variables when running lein repl:

```clojure
:aliases {"repl" ["with-profile" "+dev-cfg" "repl"]}
```

* Create similar aliases for other lein tasks (e.g. test) as needed

### programmatic

If the configuration for your project is coming from other than the classpath, files or system environment (for example database or zookeeper) you can fetch the variables and pass them down to applenews-api as follow:

```clojure
(ns your-prj
  (:require [net.reborg.applenews-api.core :as applenews-api]
            [net.reborg.applenews-api.config :refer [*env*]]))

    ; retrieve all pools using a custom config
    (binding [*env* {:applenewsapi {:channel {<your-counfig-here>}}}]
      (applenews-api/get-article "articleid"))
```

### Automatic Image Resizing

Apple specification restricts the minimum size for the thumbnail image, the image that is used on the section along with a preview of the article. The image must be at minimum 600x400 with no maximum restrictions (other than 20MB total payload size to create an article). You can let applenews-api to resize it for you, interpolating a smaller image to a slightly bigger one just enough to pass Apple checks. Use the configuration to do so by setting ":thumbnail-resize-enable" true. :thumbnail-resize-width and :thumbnail-resize-height are then used to resize the image. Image proportion will be kept, so based on portrait/landscape orientation the tool (thanks to Scalr) the settings for width/height might be applied differently than your constraint.

### Parallel Create/Delete

applenews-api supports parallel creation and deletion of articles. The :parallel parameter in configuration determines the amount of parallel threads that will be used for processing. The approach to parallelism is a simple parallel map, so the next n-amount of threads will fire when the last m-chunk has been completely processed. Expect a spike at the beginning of each chunk that fades off toward the end. For most of the practical purposes, this is fair enough.

### Exceptions

You can tell applenews-api if you want exceptions to be thrown in case of non 20x answers from the Apple Publisher service. This is a feature of clj-http library that is used underneath. When :throw-exceptions is false in config, any status code >= 30x will not generate exception but just returned in the response map.

### Certificate things

If you happen to incur in a "unable to find valid certification path to requested target" exception this is because the Apple Publisher Server certificate is not known in your certificate chain. Use the following to add the certificate. host must be replaced with the apple publisher server endpoint, port is 443:

```
openssl x509 -in <(openssl s_client -connect hostname:443 -prexit 2>/dev/null) -out ~/example.crt
sudo keytool -importcert -file ~/example.crt -alias example -keystore $(/usr/libexec/java_home)/jre/lib/security/cacerts -storepass changeit
```

If you don't have `/usr/libexec/java_home` on your system, just replace `$(/usr/libexec/java_home)` with the Java home on your system. How to find the java home on your system is an easy google-able solution.

## How to run the tests

`lein midje` will run all tests.

## License

Eclipse Public License - http://www.eclipse.org/legal/epl-v10.html

## TODO

* [ ] additional metadata for articles (related, featured sponsored)
* [ ] Add maven deploy integration for Java interface (rename project?)
* [ ] global DEBUG var for all debugging stuff
* [ ] kill remaining reflection warnings
