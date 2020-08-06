# updateChangelog

## Usage

```groovy
  updateChangelog(applicationName, credentialsId)
```

* *applicationName* is the name of the project.
* *credentialsId*  is the github credentials setup in Jenkins.

This is used to create and update the git Changelog as Jenkins jobs are used to deploy to environments.
In case of failure when a pull request update happens to occur at the same time as another commit to master,
another try of updating a changelog file will be taken.

## Examples

```groovy
stage('Update Changelog') {
  updateChangelog("dashboard", GITHUB_CREDENTIALS_ID)
}
```
