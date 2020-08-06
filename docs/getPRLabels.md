# getPRLabels

## Usage

```groovy
  getPRLabels()
```

Returns list of pull request labels.

## Examples

```groovy
stage('Security Scan') {
 def labels =  getPRLabels()
}
```
