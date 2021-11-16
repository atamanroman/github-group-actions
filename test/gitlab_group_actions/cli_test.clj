(ns gitlab-group-actions.cli-test
  (:require [clojure.test :refer :all]
            [gitlab-group-actions.cli :refer :all]))

(deftest version
  (testing "-v"
    (let [args (parse-args ["-v"])]
      (is (= (:exit-status args) 0))
      (is (= (:action args) :exit)))
    )
  (testing "--version"
    (let [args (parse-args ["--version"])]
      (is (= (:exit-status args) 0))
      (is (= (:action args) :exit)))
    )
  )

(deftest help
  (testing "-h"
    (let [args (parse-args ["-h"])]
      (is (= (:exit-status args) 0))
      (is (= (:action args) :exit))
      ))

  (testing "--help"
    (let [args (parse-args ["-h"])]
      (is (= (:exit-status args) 0))
      (is (= (:action args) :exit)))
    )
  )

(deftest bad-input
  (let [args (parse-args ["foo"])]
    (is (= (:exit-status args) 1))
    (is (= (:action args) :exit))
    )
  )

(deftest start-pipeline
  (testing "--dry-run"
    (let [args (parse-args ["start-pipeline" "a" "b" "c" "--dry-run"])]
      (is (= (:dry-run args) true))
      (is (= (:action args) :start-pipeline))
      (is (= (:recursive args) false))
      (is (= (:api-url args) "a"))
      (is (= (:group-id args) "b"))
      (is (= (:access-token args) "c"))
      (is (nil? (:exit-status args)))
      )
    )

  (testing "recursive"
    (let [args (parse-args ["start-pipeline" "a" "b" "c" "-r"])]
      (is (= (:dry-run args) false))
      (is (= (:action args) :start-pipeline))
      (is (= (:recursive args) true))
      (is (= (:api-url args) "a"))
      (is (= (:group-id args) "b"))
      (is (= (:access-token args) "c"))
      (is (nil? (:exit-status args)))
      )
    )
  )
