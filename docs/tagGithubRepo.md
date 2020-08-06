# tagGithubRepo

## Usage

```groovy
  tagGithubRepo(String tag, String credentials)
```

* *tag* The tag you want to apply to the Github repo
* *credentials* The reference to github credentials setup in Jenkins

Allows for tagging of a Github repo.  Generally this is used for tagging the project's
github repo with a new SemVer when a PR has been merged.

## Examples

```groovy
stage('Tag Repo') {
  tagGithubRepo(newTag, GITHUB_CREDENTIALS_ID)
}
```
