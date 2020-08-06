# smokeTest

## Usage

```groovy
  smokeTest(String pathToSmokeTestScript, String urlForSmokeTest)
```

* *pathToSmokeTestScript* is the path to the script executing the smoke test, for example './test/resources/smoketest.sh'

* *urlForSmokeTest* is the url to execute the script, for example 'http://dashboard:8888/system-information'

## Examples

```groovy
   stage('Smoke Tests') {
      smokeTest('./test/resources/smoketest.sh', 'http://dashboard:8888/system-information')
   }
```
