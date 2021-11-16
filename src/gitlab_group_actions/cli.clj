(ns gitlab-group-actions.cli
  (:use [docopt.core :only [docopt]]))

(defn- normalize [arg-map] {:access-token (arg-map "<access-token>")
                            :action       (cond (arg-map "start-pipeline") :start-pipeline
                                                (arg-map "create-tag") :create-tag
                                                :else :exit)
                            :api-url      (arg-map "<api-url>")
                            :branch       (arg-map "--branch")
                            :dry-run      (or (arg-map "--dry-run") false)
                            :exit-status  (or (arg-map "exit-status") nil)
                            :group-id     (arg-map "<group-id>")
                            :recursive    (or (arg-map "--recursive") false)
                            :tag-name     (arg-map "<name>")
                            :tag-message  (arg-map "<message>")})

(defn #^{:doc     "Gitlab Group Actions.

Usage:
  gl_ga start-pipeline <api-url> <group-id> <access-token> [-r -n -b=<branch>]
  gl_ga create-tag <name> <message> <api-url> <group-id> <access-token> [-r -n -b=<branch>]
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
      (nil? arg-map) (normalize (assoc arg-map "exit-status" 1))
      (arg-map "--help") (do (println (:doc (meta #'parse-args))) (normalize (assoc arg-map "exit-status" 0)))
      (arg-map "--version") (do (println (:version (meta #'parse-args))) (normalize (assoc arg-map "exit-status" 0)))
      :else (normalize arg-map))))


