(ns gitlab-group-actions.projects
  (:require [gitlab-group-actions.api :as api]))

(defn get-projects
  ([gitlab-options] (get-projects gitlab-options 1))
  ([{:keys [:access-token :api-url :group-id :recursive]} page]
   (let [resp (api/get access-token "%s/groups/%d/projects?include_subgroups=%b&simple=true&archived=false&per_page=100&page=%d" [api-url group-id recursive page])
         body (:body resp)
         total_pages (Integer/parseInt (:x-total-pages (:headers resp)))]
     (if (> total_pages page) (throw (new IllegalStateException "OMG THERE ARE MORE PAGEZZZ"))) ; TODO
     (println "Projects:" body)
     body)))

(defn get-target-branch [project cli-branch]
  (if
   (not= (:branch cli-branch) :default) (:default_branch project) cli-branch))
