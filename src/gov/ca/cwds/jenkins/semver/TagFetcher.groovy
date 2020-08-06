package gov.ca.cwds.jenkins.semver

class TagFetcher {
  def script

  TagFetcher(script) {
    this.script = script
  }

  def getTags(tagPrefix = '') {
    def rawTags = script.sh(script: 'git tag', returnStdout: true)
    def regex = tagPrefix ? /$tagPrefix\-(\d+\.\d+\.\d+)/ : /(\d+\.\d+\.\d+)/
    def tags = extractSemVerTags(rawTags, regex)
    if (hasTagPrefixWithNoTags(tagPrefix, tags)) {
      tags = extractSemVerTags(rawTags, /(\d+\.\d+\.\d+)/, /\w+\-\d+\.\d+\.\d+/)
    }
    tags
  }

  private extractSemVerTags(rawTags, regex, exceptRegex = '') {
    def list = rawTags.split('\n').findAll { it =~ regex }
    if (exceptRegex) {
      list = list.findAll { it -> !(it =~ exceptRegex) }
    }
    list.collect { tag ->
      (tag =~ regex).with { it.hasGroup() ? it[0][1] : null }
    }.unique()
  }

  private hasTagPrefixWithNoTags(tagPrefix, tags) {
    tagPrefix && !tags
  }
}
