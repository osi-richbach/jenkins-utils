# githubPullRequestBuilderTriggerProperties

## Usage

```groovy
  githubPullRequestBuilderTriggerProperties(String jenkinsUrl)
```

* *jenkinsUrl* The url to the jenkins instance.  This is optional and defaults to 'https://jenkins.dev.cwds.io'


## Examples

```groovy
node('linux') {
  triggerProperties = githubPullRequestBuilderTriggerProperties()
  properties([
    pipelineTriggers([triggerProperties])
  ])
}
```

```groovy
node('linux') {
  triggerProperties = githubPullRequestBuilderTriggerProperties('https://alternative-jenkins.dev.cwds.io')
  properties([
    pipelineTriggers([triggerProperties])
  ])
}
```

## Configuration

In order to create the webhook in GitHub you'll need to have the project include the GitHub user, cwds-jenkins-dev with admin level access.
You should then be able to create the webhook by toggling on the GitHub Pull Request Builder in the Jenkins UI and turning on the 'Use github hooks for build triggering'

The trigger will looks like this:

```
Payload URL: http://jen-proxy.dev.cwds.io/ghprbhook/
Content type: application/json
Events: Individual events > Issue Comments and Pull Requests
```

On the Jenkins side no configuration is needed, although the first build will not "work" rather it will bootstrap the configuration...all subsequent builds will work as expected.  If you're adding this to a PR build you may encounter an issue where it refuses to pick up the config. This can
be due to a security issue where Jenkins doesn't allow for changed Jenkinsfiles in a PR if the user doesn't have sufficient privledges. A way
to work around this is to do the following:

- In the Jenkins UI go to the last build and click on replay to get to the modified pipeline script.
- If it is an old version, you can manually edit it to add the `triggerProperties` at which point it should pick up the changes after a build.
