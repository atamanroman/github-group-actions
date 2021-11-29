(ns gitlab-group-actions.core
  (:require [gitlab-group-actions.cli :as cli]
            [gitlab-group-actions.pipelines :as pipelines]
            [gitlab-group-actions.tags :as tags]
            [gitlab-group-actions.projects :as projects]
            [clojure.tools.logging :as log])
  (:gen-class))

(defn -main [& raw] (let [opts (cli/parse-args raw)]
                      (do (log/debug "CLI args=" opts)
                          (cond
                            (= (:action opts) :exit) (System/exit (:exit-status opts))
                            (= (:action opts) :start-pipeline) (doall (pipelines/start-pipelines opts (projects/get-projects opts)))
                            (= (:action opts) :create-tag) (doall (tags/create-tags opts (projects/get-projects opts)))
                            :else (throw (new IllegalStateException "Not implemented yet!"))))))
