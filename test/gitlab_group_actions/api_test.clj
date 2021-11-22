(ns gitlab-group-actions.api-test
  (:require [clojure.test :refer :all]
            [gitlab-group-actions.api :refer :all]))

(deftest encode
  (let [cut #'gitlab-group-actions.api/encode]
    (testing "strings are encoded"
      (is (= ["foo", "foo+bar"] (cut ["foo", "foo bar"]))))
    (testing "numbers are not encoded"
      (is (= ["foo", 1] (cut ["foo", 1]))))))

(deftest build-url
  (let [cut #'gitlab-group-actions.api/build-url]
    (testing "url is built and params are encoded"
      (is (= "https://example.com/api/foo/bar=1&baz=foo+bar"
             (cut "%s/foo/bar=%d&baz=%s" ["https://example.com/api", 1, "foo bar"]))))))
