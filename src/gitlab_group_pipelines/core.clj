(ns gitlab-group-pipelines.core
  (:require [gitlab-group-pipelines.cli :as cli]
            [gitlab-group-pipelines.pipelines :as pipelines]
            [gitlab-group-pipelines.projects :as projects]))

(defn -main [& args] (let [params (cli/parse-args args)]
                       (cond
                         (params "goodbye") (System/exit (params "goodbye"))
                         (params "start-pipeline") (doall (pipelines/start-pipelines params (projects/get-projects params)))
                         :else (throw (new IllegalStateException "Not implemented yet!"))
                         )))
