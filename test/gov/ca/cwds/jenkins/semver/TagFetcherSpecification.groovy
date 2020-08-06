package gov.ca.cwds.jenkins.semver

import spock.lang.Specification

@SuppressWarnings('UnnecessaryGetter')
class TagFetcherSpecification extends Specification {
  class PipeLineScript {

    def sh(hash) {
    }
  }

  def "#getTags with only SemVer tags"() {
    given:
    PipeLineScript pipeline = Stub(PipeLineScript)
    pipeline.sh(_) >> '0.1.0\n2.1.32\n1.2.4'
    def tagFetcher = new TagFetcher(pipeline)

    when:
    def tags = tagFetcher.getTags()

    then:
    tags == ['0.1.0', '2.1.32', '1.2.4']
  }

  def "#getTags with non semVer tags"() {
    given:
    PipeLineScript pipeline = Stub(PipeLineScript)
    pipeline.sh(_) >> '0.1.0\ncsec_initial_load\n1.0\nes_6x__1'
    def tagFetcher = new TagFetcher(pipeline)

    when:
    def tags = tagFetcher.getTags()

    then:
    tags == ['0.1.0']
  }

  def "#getTags with SemVer tags that have extra metadata"() {
    given:
    PipeLineScript pipeline = Stub(PipeLineScript)
    pipeline.sh(_) >> '0.2.4.567\n1.2.3_1098-RC\njobs_0.60.185'
    def tagFetcher = new TagFetcher(pipeline)

    when:
    def tags = tagFetcher.getTags()

    then:
    tags == ['0.2.4', '1.2.3', '0.60.185']
  }

  def "#getTags with SemVer tags filtered by version prefix"() {
    given:
    PipeLineScript pipeline = Stub(PipeLineScript)
    // only labels prefixed with 'lis-' will be taken into account because of what we pass to tagFetcher.getTags()
    pipeline.sh(_) >> 'cws-1.2.3_1098-RC\ncapu-0.60.185\nlis-1.6.0\nlis-1.6.1-RC1'
    def tagFetcher = new TagFetcher(pipeline)

    when:
    def tags = tagFetcher.getTags('lis')

    then:
    tags == ['1.6.0', '1.6.1']
  }

  def "#getTags with SemVer tags filtered by version prefix when yet there are no such tags"() {
    given:
    PipeLineScript pipeline = Stub(PipeLineScript)
    // only labels without any possible prefix will be taken into account
    // because there is no tag with the prefix that is passed into tagFetcher.getTags()
    pipeline.sh(_) >> '0.2.4.567\ncapu-1.2.3_1098-RC\njobs_0.60.185'
    def tagFetcher = new TagFetcher(pipeline)

    when:
    def tags = tagFetcher.getTags('lis')

    then:
    tags == ['0.2.4', '0.60.185']
  }

  def "#getTags with SemVer tags that have duplicates"() {
    given:
    PipeLineScript pipeline = Stub(PipeLineScript)
    pipeline.sh(_) >> '0.2.4.567\n1.2.3_1098-RC\n0.2.4.568'
    def tagFetcher = new TagFetcher(pipeline)

    when:
    def tags = tagFetcher.getTags()

    then:
    tags == ['0.2.4', '1.2.3']
  }

  def "#getTags with no tags"() {
    given: 'a tag fetcher'
    PipeLineScript pipeline = Stub(PipeLineScript)
    pipeline.sh(_) >> ''
    def tagFetcher = new TagFetcher(pipeline)

    when: 'get tags'
    def tags = tagFetcher.getTags()

    then: 'returns an empty array'
    tags == []
  }
}
