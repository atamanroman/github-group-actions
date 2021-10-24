(ns gitlab-group-pipelines.projects
  (:require [gitlab-group-pipelines.api :as api]))

(defn get-projects
  ([gitlab-options] (get-projects gitlab-options 1))
  ([{:keys [:gitlab-url :gitlab-groupid :gitlab-token]} page]
   (let [resp (api/get gitlab-token "%s/groups/%d/projects?include_subgroups=true&simple=true&archived=false&per_page=100&page=%d" [gitlab-url gitlab-groupid page])
         body (:body resp)
         total_pages (Integer/parseInt (:x-total-pages (:headers resp)))]
     (if (> total_pages page) (println "OMG THERE ARE MORE PAGEZZZ")) ; TODO
     (println "Projects:" body)
     body)))
