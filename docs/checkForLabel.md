# checkForLabel

## Usage

```groovy
  checkForLabel(String projectName)
  checkForLabel(String projectName, List tagPrefixes)
```

* *projectName* Is the name of the project in Github without ca-cwds (cans, intake, cals-api, etc).

* *tagPrefixes* Is the optional list of prefixes of tags for pipelines that may produce multiple artifacts.
If the list is passed, then one and only one of its values should be present among PR labels.

This is designed for checking for a valid SemVer label in github on a Pull Request. If the PR does
not have a label of 'major', 'minor', or 'patch' it will throw an error.

## Examples

```groovy
stage('Check SemVer Label') {
 checkForLabel('dashboard')
}
```

```groovy
stage('Check SemVer Labels') {
 checkForLabel('dashboard', ['lis', 'cwscms', 'capu'])
}
```
