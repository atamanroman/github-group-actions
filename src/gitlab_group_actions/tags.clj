(ns gitlab-group-actions.tags
  (:require [gitlab-group-actions.api :as api]
            [gitlab-group-actions.projects :as projects]))

(def tag-endpoint "%s/projects/%d/repository/tags?ref=%s&tag_name=%s&message=%s")

(defn- log-tag [project target-branch & status]
  (apply println (conj status (str "Tag for " (:name project) "/" target-branch ":"))))

(defn create-tag [{:keys [:tag-name :tag-message :api-url :access-token :branch :dry-run]} project target-branch]
  (try
    (let [resp (api/post access-token tag-endpoint [api-url (:id project) target-branch tag-name tag-message] dry-run)
          body (:body resp)
          pipeline (:web_url [body])]
      (do (log-tag project "CREATED @" pipeline)
          {:id       (:id project),
           :pipeline pipeline}))
    (catch Exception e (log-tag project "FAILED" (.toString e) {:id    (:id project),
                                                                :error e}))))

(defn create-tags [gitlab-options projects]
  (map (fn [project]
         (create-tag gitlab-options project (projects/get-target-branch project (:branch gitlab-options))))
       projects))
