# updateLicenseReport

## Pre-requisites

### Pre-requisites for a back-end project

The Gradle License Report plugin should be applied in a back-end project build file.
Applying the plugin is described here: https://github.com/jk1/Gradle-License-Report

### Pre-requisites for a front-end project

The LicenseFinder should be installed in a front-end project.
Installation of the pligin is described here: https://github.com/pivotal-legacy/LicenseFinder

## Usage

### Usage in a back-end project

```groovy
def rtGradle = Artifactory.newGradleBuild()
...
stage('Update License Report') {
  updateLicenseReport(branch, sshCredentialsId, [runtimeGradle: rtGradle])
}
```

### Usage in a front-end project

```groovy
def app = docker.build("cwds/dashboard:${env.BUILD_ID}")
...
stage('Update License Report') {
  updateLicenseReport(branch, sshCredentialsId, [dockerImage: app])
}
```

### Syntax

```groovy
stage('Update License Report') {
  updateLicenseReport(branch, sshCredentialsId, parametersMap)
}
```

* *branch* the branch from where the project is being built. Licence Generation will be skipped if it is not the master branch.
* *sshCredentialsId* the Credentials Id for Ssh Agent
* *parametersMap* the map of parameters that depends on a cotext.
* For back-end projects, pass the following fields to the map:
*   *runtimeGradle* Runtime Gradle that is usually pre-made using `Artifactory.newGradleBuild()`
  and is used only in projects with Gradle (usually back-end).
  If the parameter is omitted for a back-end project, then the stage will try to call the `./gradlew` command.
* For front-end projects, pass the following fields to the map: 
*   *dockerImage* the docker image object that is returned by a `docker.build` call, for example `docker.build("cwds/dashboard:${env.BUILD_ID}")`

The `updateLicenseReport` will try to detect if it is a front-end or a back-end project
and what plugin is used in the project for License Report Generation.
If none is found then the stage will fail the build.
Otherwise it will invoke License Report Generation and push the changes into the project repository (if any) under the `legal` folder.
