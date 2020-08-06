package gov.ca.cwds.jenkins.semver

class SemVer {
  static final DASH = '-'

  def script
  TagFetcher tagFetcher
  NewTagGenerator newTagGenerator
  PullRequestEvent pullRequestEvent
  VersionIncrement versionIncrement

  SemVer(script) {
    this.script = script
    this.tagFetcher = new TagFetcher(script)
    this.newTagGenerator = new NewTagGenerator()
    this.pullRequestEvent = new PullRequestEvent(script)
    this.versionIncrement = new VersionIncrement()
  }

  @SuppressWarnings('UnnecessaryGetter')
  def newTag(label, List tagPrefixes = []) {
    def eventLabels = []
    if (tagPrefixes || !isValidIncrementLabel(label)) {
      def event = pullRequestEvent.getEvent()
      eventLabels = event.labels.collect([]) { it.name }
    }
    String tagPrefix = ''
    if (tagPrefixes) {
      tagPrefix = new TagPrefixFinder(tagPrefixes).find(eventLabels)
    }
    def existingTags = tagFetcher.getTags(tagPrefix)
    def incrementLabel = isValidIncrementLabel(label) ? label.toUpperCase() as IncrementTypes
      : versionIncrement.increment(eventLabels)
    (tagPrefix ? tagPrefix + DASH : '') + newTagGenerator.newTag(existingTags, incrementLabel)
  }

  @SuppressWarnings('UnnecessaryGetter')
  def newTag(label, String tagPrefix) {
    def existingTags = tagFetcher.getTags(tagPrefix)
    if (!isValidIncrementLabel(label)) {
      throw new Exception("No version label passed. Please pass 'major', 'minor', or 'patch' label.")
    }
    tagPrefix + DASH + newTagGenerator.newTag(existingTags, label.toUpperCase() as IncrementTypes)
  }

  @SuppressWarnings('UnnecessaryCollectCall')
  private boolean isValidIncrementLabel(label) {
    label && IncrementTypes.values().collect { it.toString() }.contains(label.toUpperCase())
  }
}
