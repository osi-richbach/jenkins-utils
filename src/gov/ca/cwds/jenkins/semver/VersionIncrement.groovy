package gov.ca.cwds.jenkins.semver

class VersionIncrement {
  def increment(labels) {
    def (versionIncrement, versionIncrementsFound) = assignIncrement(labels)
    if (versionIncrementsFound > 1) {
      throw new Exception('More than one version increment label found.'
        + " Please label PR with only one of 'major', 'minor', or 'patch'")
    }
    if (versionIncrementsFound == 0) {
      throw new Exception("No labels found. Please label PR with 'major', 'minor', or 'patch'")
    }
    versionIncrement
  }

  private assignIncrement(labels) {
    def versionIncrement
    def versionIncrementsFound = 0
    labels.each { label ->
      switch (label) {
        case 'major':
          versionIncrement = IncrementTypes.MAJOR
          versionIncrementsFound++
          break
        case 'minor':
          versionIncrement = IncrementTypes.MINOR
          versionIncrementsFound++
          break
        case 'patch':
          versionIncrement = IncrementTypes.PATCH
          versionIncrementsFound++
          break
      }
    }
    new Tuple(versionIncrement, versionIncrementsFound)
  }
}
