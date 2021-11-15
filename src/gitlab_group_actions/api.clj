(ns gitlab-group-actions.api
  (:require [clj-http.client :as client]))

(defn- auth-header [gitlab-token] {"<gitlab-token>" gitlab-token})

; TODO handle paging
(defn get [gitlab-token url-template params]
  (client/get (apply format url-template params)
              {:as      :json
               :headers (auth-header gitlab-token)}))

; TODO body
(defn post
  ([gitlab-token url-template params dry-run]
   (if dry-run
     (println "POST" (apply format url-template params) "(dry-run)")
     (client/post (apply format url-template params)
                  {:as      :json
                   :headers (auth-header gitlab-token)})))
  ([gitlab-token url-template [params]]
   (post gitlab-token url-template params false)))

