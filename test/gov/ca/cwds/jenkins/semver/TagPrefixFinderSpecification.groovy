package gov.ca.cwds.jenkins.semver

import spock.lang.Specification

class TagPrefixFinderSpecification extends Specification {

  def "#find with one tag prefix label"() {
    given:
    def tagPrefixFinder = new TagPrefixFinder(['capu', 'lis', 'cwscms'])
    def labels = ['patch', 'lis']

    when:
    def foundTagPrefix = tagPrefixFinder.find(labels)

    then:
    foundTagPrefix == 'lis'
  }

  def "#find with multiple tag prefix labels"() {
    given:
    def tagPrefixFinder = new TagPrefixFinder(['capu', 'lis', 'cwscms'])
    def labels = ['minor', 'lis', 'capu']

    when:
    def foundTagPrefix = tagPrefixFinder.find(labels)

    then:
    foundTagPrefix == 'lis'
  }

  def "#find with no tag prefix labels"() {
    given:
    def tagPrefixFinder = new TagPrefixFinder(['capu', 'lis', 'cwscms'])
    def labels = ['major', 'release']

    when:
    tagPrefixFinder.find(labels)

    then:
    def error = thrown(Exception)
    error.message == 'No labels with tag prefix found. Please label PR with one of [capu, lis, cwscms]'
  }
}
