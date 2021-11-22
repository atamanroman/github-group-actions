(ns gitlab-group-actions.api
  (:require [clj-http.client :as client]
            [clojure.tools.logging.readable :as log])
  (:import (java.net URLEncoder)
           (java.nio.charset StandardCharsets)))

(defn- auth-header [gitlab-token] {"Authorization" (str "Bearer " gitlab-token)})

; TODO use proper url builder like https://github.com/lambdaisland/uri
(defn- encode [params]
  (map
   #(cond
      (instance? String %) (URLEncoder/encode (String/valueOf %) StandardCharsets/UTF_8)
      :else %)
   params))

(defn- build-url [template params] (let [base-url (first params)
                                         url-params (rest params)]
                                     (apply format template base-url (encode url-params))))

; TODO handle paging
(defn get [gitlab-token url-template url-params]
  (let [url (build-url url-template url-params)]
    (do (log/debugf "GET %s" url)
        (client/get url
                    {:as      :json
                     :headers (auth-header gitlab-token)}))))

; TODO body
(defn post
  ([gitlab-token url-template url-params dry-run]
   (let [url (build-url url-template url-params)]
     (if dry-run
       (log/debugf "POST %s (dry run)" url)

       (do (log/debugf "POST %s" url)
           (client/post url
                        {:as      :json
                         :headers (auth-header gitlab-token)})))))
  ([gitlab-token url-template [params]]
   (post gitlab-token url-template params false)))

