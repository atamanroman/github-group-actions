(ns gitlab-group-pipelines.cli
  (:use [docopt.core :only [docopt]]))

(defn #^{:doc     "Gitlab Group Actions.

Usage:
  gl_ga start-pipeline <api-url> <group-id> <access-token> [-r -n -b=<branch>]
  gl_ga -h | --help
  gl_ga -v | --version

Options:
  -b=<branch> --branch=<branch>   Use the given branch (or default branch with the magic ':default' keyword)
                                  for actions [default: :default].
  -r, --recursive                 Apply actions to all children of the given group.
  -n, --dry-run                   Don't issue any mutating actions and just log them.
  -h, --help                      Show this screen.
  -v, --version                   Show version."
         :version "Gitlab Group Actions, version 0.1.0-SNAPSHOT"} ; TODO dynamic from project?
  parse-args [args]
  (let [arg-map (docopt (:doc (meta #'parse-args)) args)]
    (cond
      (nil? arg-map) (do (println (:doc (meta #'parse-args))) ((assoc arg-map "goodbye" 1)))
      (arg-map "--help") (do (println (:doc (meta #'parse-args))) (assoc arg-map "goodbye" 0))
      (arg-map "--version") (do (println (:version (meta #'parse-args))) (assoc arg-map "goodbye" 0))
      :else arg-map
      )))

;(defn #^{:doc     "Gitlab Group Actions.
;
;Usage:
;  gl_ga start-pipeline [-rn] <gitlab-url> <group-id> <gitlab-token>
;  gl_ga create-tag
;  gl_ga -h | --help
;  gl_ga --version
;
;Options:
;  -r, --recursive   Recurse into subgroups.
;  -n, --dry-run     Don't start any actions - log only.
;  -h --help         Show this screen.
;  --version         Show version."
;         :version ("Gitlab Group Actions, version " (System/getProperty "projectname.version"))}
;  parse-args [args]
;  (let [arg-map (docopt (:doc (meta #'parse-args)) args)]
;    (cond
;      (or (nil? arg-map)
;          (arg-map "--help")) (println (:doc (meta #'parse-args)))
;      (arg-map "--version") (println (:version (meta #'parse-args)))
;      :else (arg-map))))

;(defn exit-with-usage []
;  (do (println "Usage: gitlab-group-pipelines [--dry-run] <gitlab-url> <groupid> <gitlab-token>")
;      (System/exit 1)))

;(defn parse-args [args]
;  (let [dry-run (= (first args) "--dry-run")
;        rest-args (if dry-run (rest args) args)]
;    (if (= (count rest-args) 3)
;      (let [parsed {:gitlab-url     (.toString (.resolve (URI/create (nth rest-args 0)) "/api/v4"))
;                    :gitlab-groupid (Integer/parseInt (nth rest-args 1))
;                    :gitlab-token   (nth rest-args 2)
;                    :dry-run        dry-run}]
;        (do (println "CLI Arguments:" parsed)
;            parsed))
;      (exit-with-usage))))
