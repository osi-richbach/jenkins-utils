package gov.ca.cwds.jenkins.semver

import spock.lang.Specification

class VersionIncrementSpecification extends Specification {

  def "#increment with a patch label"() {
    given:
    def versionIncrement = new VersionIncrement()

    when:
    def increment = versionIncrement.increment(['patch'])

    then:
    increment == IncrementTypes.PATCH
  }

  def "#increment with a minor label"() {
    given:
    def versionIncrement = new VersionIncrement()

    when:
    def increment = versionIncrement.increment(['minor'])

    then:
    increment == IncrementTypes.MINOR
  }

  def "#increment with a major label"() {
    given:
    def versionIncrement = new VersionIncrement()

    when:
    def increment = versionIncrement.increment(['major'])

    then:
    increment == IncrementTypes.MAJOR
  }

  def "#increment with multiple labels"() {
    given:
    def versionIncrement = new VersionIncrement()

    when:
    versionIncrement.increment(['major', 'minor'])

    then:
    def error = thrown(Exception)
    error.message == 'More than one version increment label found. ' +
      "Please label PR with only one of 'major', 'minor', or 'patch'"
  }

  def "#increment with no labels"() {
    given:
    def versionIncrement = new VersionIncrement()

    when:
    versionIncrement.increment([])

    then:
    def error = thrown(Exception)
    error.message == "No labels found. Please label PR with 'major', 'minor', or 'patch'"
  }
}
