# containerScan

## Usage

```groovy
  containerScan(String containerName, String containerVersion)
```

* *containerName* is the name of the container in dockerhub (you do not need to append cwds).
* *containerVersion*  is the version in dockerhub.

## Examples

```groovy
stage('Security Scan') {
 containerScan('dashboard', '1.2.24')
}
```
