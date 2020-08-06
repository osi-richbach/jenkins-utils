# updateManifest

## Usage

```groovy
  updateManifest(applicationName, manifestName, credentialsId, version)
```

* *applicationName* is the name of the project.
* *manifestname*  is name of the manifest environment file you want to update.
* *credentialsId*  is the github credentials setup in Jenkins.
* *version* is the version number that's deployed to that environment

This is used to keep the manifest files in sync as Jenkins jobs are used to deploy to environments.
In case of failure when a pull request update happens to occur at the same time as another commit to master,
another try of updating a manifest file will be taken.

## Examples

```groovy
stage('Update Manifest Version') {
  updateManifest("dashboard", "preint", GITHUB_CREDENTIALS_ID, "1.4.55")
}
```
