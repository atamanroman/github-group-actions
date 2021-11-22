(ns gitlab-group-actions.tags
  (:require [gitlab-group-actions.api :as api]
            [gitlab-group-actions.projects :as projects]
            [clojure.tools.logging.readable :as log]))

(defn- log-tag [project target-branch & status]
  (println (format "Tag for %s on branch %s: %s" (:name project) target-branch (apply str status))))

(defn- tag-url [project tag-name] (str (:web_url project) "/-/tags/" tag-name))

(defn create-tag [{:keys [:tag-name :tag-message :api-url :access-token :branch :dry-run]} project target-branch]
  (try
    (let [resp (api/post access-token "%s/projects/%d/repository/tags?ref=%s&tag_name=%s&message=%s" [api-url (:id project) target-branch tag-name tag-message] dry-run)
          body (:body resp)
          pipeline (:web_url [body])]
      (do (log-tag project target-branch "CREATED @ " (tag-url project tag-name))
          {:id       (:id project),
           :pipeline pipeline}))
    (catch Exception e (log-tag project "FAILED" (.toString e) {:id    (:id project),
                                                                :error e}))))

(defn create-tags [gitlab-options projects]
  (map (fn [project]
         (create-tag gitlab-options project (projects/get-target-branch project (:branch gitlab-options))))
       projects))
