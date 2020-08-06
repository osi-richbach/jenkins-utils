# buildDiscarderDefaults

## Usage

```groovy
buildDiscarderDefaults(String buildType)
```

* *buildType* a string which represents the build type, e.g. 'master', 'pr' or 'release'; can be null.

This function will return the Build Discarder configuration.
If buildType is 'master' then the result will be configured to keep 50 last builds, 10 otherwise.

## Examples

```groovy
node('linux') {
  properties([
    buildDiscarderDefaults('master')
  ])
}
```
