# newSemVer

## Usage

```groovy
  newSemVer()
  newSemVer(String increment, List tagPrefixes, String tagPrefix)
```

* *increment* Is the optional passing in of an increment to allow for updating outside
of pull requests. Options are ['major', 'minor', 'patch']

* *tagPrefixes* Is the optional list of prefixes of tags for pipelines that may produce multiple artifacts.

* *tagPrefix* Is the optional prefix of tag for pipelines that may produce multiple artifacts. Pass if pipeline 
should be run not from pull request merge event.

This returns a new SemVer tag bumped based upon a label set in the PR to `major`, `minor`, or `patch`. This assumes that it has access to a JSON event object configured through Generic Webhook Trigger plugin in Jenkins.
The returned new version can be used in downstream Jenkins stages to tag the project maybe using
[tagGithubRepo](tagGithubRepo.md) and [updateManifest](updateManifest.md).

It also allows you to pass an increment in directly for the case of wanting to bump a tag that isnt' triggered
by a pull request.

## Examples

```groovy
stage("Increment Tag") {
  newTag = newSemVer()
}

stage("Increment Tag") {
  newTag = newSemVer('', ['lis', 'cwscms', 'capu'])
}

stage("Increment Tag") {
  newTag = newSemVer('', ['lis', 'cwscms', 'capu'], 'lis')
}
```

## Notes

- If no SemVer style tags exist in the repository it will default to a starting tag of `0.1.0`
