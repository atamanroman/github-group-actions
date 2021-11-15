(ns gitlab-group-actions.projects
  (:require [gitlab-group-actions.api :as api]))

(defn get-projects
  ([gitlab-options] (get-projects gitlab-options 1))
  ([{:keys [:access-token :api-url :group-id]} page]
   (let [resp (api/get access-token "%s/groups/%d/projects?include_subgroups=true&simple=true&archived=false&per_page=100&page=%d" [api-url group-id page])
         body (:body resp)
         total_pages (Integer/parseInt (:x-total-pages (:headers resp)))]
     (if (> total_pages page) (println "OMG THERE ARE MORE PAGEZZZ")) ; TODO
     (println "Projects:" body)
     body)))
