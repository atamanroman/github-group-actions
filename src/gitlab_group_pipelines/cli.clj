(ns gitlab-group-pipelines.cli
  (:import (java.net URI)))

(defn exit-with-usage []
  (do (println "Usage: gitlab-group-pipelines [--dry-run] <gitlab-url> <groupid> <gitlab-token>")
      (System/exit 1)))

(defn parse-args [args]
  (let [dry-run (= (first args) "--dry-run")
        rest-args (if dry-run (rest args) args)]
    (if (= (count rest-args) 3)
      (let [parsed {:gitlab-url     (.toString (.resolve (URI/create (nth rest-args 0)) "/api/v4"))
                    :gitlab-groupid (Integer/parseInt (nth rest-args 1))
                    :gitlab-token   (nth rest-args 2)
                    :dry-run        dry-run}]
        (do (println "CLI Arguments:" parsed)
            parsed))
      (exit-with-usage))))
