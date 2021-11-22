(ns gitlab-group-actions.api
  (:require [clj-http.client :as client])
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
  (client/get (build-url url-template url-params)
              {:as      :json
               :headers (auth-header gitlab-token)}))

; TODO body
(defn post
  ([gitlab-token url-template url-params dry-run]
   (if dry-run
     (println "POST" (build-url url-template url-params) "(dry-run)")
     (client/post (build-url url-template url-params)
                  {:as      :json
                   :headers (auth-header gitlab-token)})))
  ([gitlab-token url-template [params]]
   (post gitlab-token url-template params false)))

