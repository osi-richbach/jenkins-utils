package gov.ca.cwds.jenkins.semver

import com.cloudbees.groovy.cps.NonCPS

class NewTagGenerator {

  static final String PERIOD = '.'

  def newTag(tags, increment) {
    def latestTag = mostRecentVersion(tags)
    def (major, minor, patch) = latestTag.tokenize(PERIOD).collect { it as Integer }
    switch (increment) {
      case IncrementTypes.MAJOR:
        major++
        minor = 0
        patch = 0
        break
      case IncrementTypes.MINOR:
        minor++
        patch = 0
        break
      case IncrementTypes.PATCH:
        patch++
        break
    }
    "$major.$minor.$patch"
  }

  @NonCPS
  String mostRecentVersion(tags) {
    def allTags = tags ?: ['0.1.0']
    allTags.max { a, b ->
      def versionA = a.tokenize(PERIOD)
      def versionB = b.tokenize(PERIOD)
      def commonIndices = Math.min(versionA.size(), versionB.size())
      for (int index = 0; index < commonIndices; ++index) {
        def numberA = versionA[index].toInteger()
        def numberB = versionB[index].toInteger()
        if (numberA != numberB) {
          return numberA <=> numberB
        }
      }
      versionA.size() <=> versionB.size()
    }
  }
}
