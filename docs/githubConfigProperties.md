# githubConfigProperties

## Usage

```groovy
githubConfigProperties(String githubUrl)
```

* *githubUrl* The url to the github project.

This configures the github plugin and allows links in the Jenkins UI to work correctly.  It's also a requirement for using the Github Pull Request Builder and should be configured on most CWS CARES projects.

## Examples

```groovy
node('linux') {
  def githubProperties = githubConfigProperties('http://github.com/ca-cwds/dashboard')
  properties([
    githubProperties
  ])
}
```

Configured with a [GithubPullRequestBuilderTriggerProperties](githubPullRequestBuilderTriggerProperties.md)

```groovy

node('linux') {
  def githubProperties = githubConfigProperties('http://github.com/ca-cwds/dashboard')
  def triggerProperties = pullRequestMergedTriggerProperties('dashboard-master')
  properties([
    githubProperties,
    pipelineTriggers([triggerProperties])
  ])
}
```
