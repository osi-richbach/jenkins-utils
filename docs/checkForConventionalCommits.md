# checkForConventionalCommits

## Usage

```groovy
  checkForConventionalCommits(String projectName)
```

* *projectName* Is the name of the project in Github without ca-cwds (cans, intake, cals-api, etc).

This is designed for checking if there is any commit in a Pull Request has a Conventional Commit Type in the first line of its comment.
Read about Conventional Commits here: https://www.conventionalcommits.org/en/

## Examples

```groovy
stage('Verify Commits') {
  checkForConventionalCommits('dashboard')
}
```
