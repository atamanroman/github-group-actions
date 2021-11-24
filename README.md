# gitlab-group-actions

Run actions against all repos in a gitlab group (and subgroups).

Features:

- start a pipeline for a given branch (or default)
- create a tag on a given branch (or default)

## Requirements

`$ brew install clojure leiningen`

## Usage

This tool is documented with [docopt](http://docopt.org/).

Run `$ lein run --` to see the docs.

Examples:

```
$ lein run -- create-tag 1.0.0 "my amazing tag" https://gitlab.com/api/v4 186 $TOKEN --dry-run --excludes-file=excludes.txt
$ lein run -- start-pipeline https://gitlab.com/api/v4 186 $TOKEN --dry-run --branch=master --excludes-file=excludes.txt
```
