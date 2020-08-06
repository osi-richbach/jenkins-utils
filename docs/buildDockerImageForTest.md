# buildDockerImageForTest

## Usage

```groovy
  buildDockerImageForTest(String pathToDockerFile)
```

* *pathToDockerFile* is the path to the Dockerfile used to build the image.

This is used to create a docker image used for linting ( and in the future other testing ) against.

## Examples

```groovy
stage('Testing Statge') {
 buildDockerImageForTest('./docker/test/Dockerfile')
}
```

## Docker Image
The library creates a docker image with the name **cwds/{JOB_NAME}:test-build-${BUILD\_ID}**

* *JOB_NAME* is the name of the Jenkins Job that is running
* *BUILD_ID*  is the build number for the Jenkins Job.