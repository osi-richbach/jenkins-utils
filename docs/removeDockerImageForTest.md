# removeDockerImageForTest

## Usage

```groovy
  removeDockerImageForTest()
```

This is used to remove a docker image created in [buildDockerImageForTest](buildDockerImageForTest.md)

## Examples

```groovy
stage('Testing Statge') {
 removeDockerImageForTest()
}
```

## Docker Image
The library removes the docker image with the name **cwds/{JOB_NAME}:test-build-${BUILD\_ID}**

* *JOB_NAME* is the name of the Jenkins Job that is running
* *BUILD_ID*  is the build number for the Jenkins Job.