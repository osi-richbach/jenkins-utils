# pullRequestMergedTriggerProperties

## Usage

```groovy
  pullRequestMergedTriggerProperties(String tokenParameter)
```

* *tokenParameter* This is the parameter passed from Github to the Jenkins Generic Webhook Trigger
to make sure it only matches for a specific project.

This can be used with any master build that is triggered by a PR merge to simplify configuration and make sure
it is not changed from the UI.

## Examples

```groovy
node('linux') {
  triggerProperties = pullRequestMergedTriggerProperties('dashboard-master')
  properties([
    pipelineTriggers([triggerProperties])
  ])
}
```

## Configuration

In order to setup this up, you will need to have a Github webook configured with the following properies:

Payload URL: http://jen-proxy.dev.cwds.io/generic-webhook-trigger/invoke?trigger_key=dashboard-master&token=dashboard-master
Content type: application/json
Events: Individual events > Pull Requests

This should be generated automatially for you by the current Jenkins admin config for the plugin. If you have issues you can try setting it up the first time in the UI.

Under Generic Webhook Trigger add the following fields:

```
Token: dashboard-master
Expression: ^closed:true$
Text: $pull_request_action:$pull_request_merged
```
