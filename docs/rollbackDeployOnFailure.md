# rollbackDeployOnFailure

## Usage

```groovy
rollbackDeployOnFailure(String applicationName, String environment, string githubCredentialsId, String ansibleDeploymentScript, Closure closure)
```

* *applicationName* the name of the application.
* *environment* The name of the current environment like `preint` or `integration`
* *githubCredentialsId* A credentails id reference
* *ansibleDeploymentScript* The command to run to redeploy the app
* *closure* All the steps you want to run that will need to be rolled back in the case of failure

This custom step is designed to be used in a release pipeline to allow for automated rollbacks with minimal configuration.

## Examples

```groovy
node('preint') {
  rollbackDeployOnFailure('dashboard', 'preint', 'credentialsId', 'ansible command') {
    checkoutStage()
    app = docker.build("test-dashboard", "-f ./docker/test/Dockerfile .")*/
    deployToStage(environment, env.APP_VERSION)
    updateManifestStage(environment, env.APP_VERSION)
    smokeTestStage(environment)
  }
}
```

## Notes

This step is designed to wrap around any steps that you'd want to perform a rollback for.  It makes no assumptions about the order
of the steps, but it will check the current version deployed according to the manifest and record it to be rolled back later if 
anything fails.  The `ansibleCommand` provided assumes that the string contains an environment variable that uses the following
pattern:

```
*_VERSION=
*_NUMBER=
```

If your ansible commnand does not delegate it's version environment variable this way it will not work currently.
