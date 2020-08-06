# notifySlack

## Usage

```groovy
  notifySlack(webhookUrl, projectName, error)
```

* *webhookUrl* Is the url of the webhook created in #3 of [Configuration](#configuration) below.  

* *projectName* Is the name of the project being built/deployed. eg. Dashboard

* *error* Is the exception/error caught.  The libary displays the result of `error.getMessage()` to the slack notifcation.

## Examples

```groovy
node('linux') {
 try {
 	lintStage()
 	unitTestStage()
 	xyxStage()
 } catch(Exception exception)
	 notifySlack('https://hooks.slack.com/services/KFJKDFDSS/BKLAKEDKL/xkdfjd3aHKDLAKD7DDa', 'Dashboard', exception)
 }
}
```

## Configuration
The webhook for the slack channel to post to must be created ahead of time.

1. Create a new [App](https://api.slack.com/apps).
2. Activate Incoming Webhooks for the new App.
3. Add a webhook for the channel you would like to post messages to.
