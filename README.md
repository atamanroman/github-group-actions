# gitlab-group-pipelines

Start a pipeline for the default branch for every project in a given group.

## Usage

`$ lein run -- [--dry-run] <gitlab-url> <groupid> <gitlab-token>`

```sh
# creates pipelines for every project in group with id=35
$ lein run -- https://git.fundsaccess.eu 35 $TOKEN
```

```shell
# shows which pipelines would be created
$ lein run -- --dry-run https://git.fundsaccess.eu 35 $TOKEN
```
