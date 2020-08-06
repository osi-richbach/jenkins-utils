package gov.ca.cwds.jenkins.semver

class TagPrefixFinder {

  def tagPrefixes

  TagPrefixFinder(List tagPrefixes) {
    this.tagPrefixes = tagPrefixes
  }

  def find(labels) {
    def foundTagPrefixes = labels.findAll { label -> tagPrefixes.contains(label) }
    if (foundTagPrefixes.isEmpty()) {
      throw new Exception('No labels with tag prefix found. Please label PR with one of ' + tagPrefixes)
    }
    foundTagPrefixes[0]
  }
}
