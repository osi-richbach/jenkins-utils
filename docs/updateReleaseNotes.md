# updateReleaseNotes

## Usage

```groovy
  updateReleaseNotes(applicationName, credentialsId)
```

* *applicationName* is the name of the project.
* *credentialsId*  is the github credentials setup in Jenkins.

This is used to create and update the automatically generated Release Notes for a project that a based on the project Changelog during Master Build.
The Release Notes are stored in the RELEASE_NOTES.md file in the prohect root. 
In case of failure when a pull request update happens to occur at the same time as another commit to master,
another try of updating Release Notes file will be taken.
The automatically generated Release Notes rely on a special format of commit comments (Conventional Commits).
Read about Conventional Commits here: https://www.conventionalcommits.org/en/ 

## Examples

```groovy
stage('Update Release Notes') {
  updateReleaseNotes('dashboard', GITHUB_CREDENTIALS_ID)
}
```
