package gov.ca.cwds.jenkins.pr

import gov.ca.cwds.jenkins.semver.IncrementTypes
import spock.lang.Specification

class ConventionalCommitsCheckerSpecification extends Specification {
  class PipeLineScript {
    Environment env

    def readJSON(hash) {
    }

    PipeLineScript() {
      this.env = new Environment()
    }
  }

  class Environment {
    String ghprbActualCommit
  }

  def "#with Conventional Commit of 'fix' type"() {
    given: 'a Conventional Commits checker'
    def environment = Mock(Environment)
    def pipeline = Mock(PipeLineScript)
    def conventionalCommitsChecker = new ConventionalCommitsChecker(pipeline)
    org.codehaus.groovy.runtime.GStringImpl anyString = GroovyMock(global: true)
    def url = GroovyMock(URL)

    when: 'checking with a Conventional Commit'
    def increment = conventionalCommitsChecker.check('dashboard')

    then: 'it does not throw and error and returns the correct increment'
    1 * pipeline.env >> environment
    1 * environment.ghprbActualCommit >> 'c56jh987shui1'
    1 * anyString.toURL() >> url
    1 * url.text >> ''
    1 * pipeline.readJSON([text: '']) >> [
      [commit: [message: 'fix: correct formatting']],
      [commit: [message: 'FIT-566']]
    ]
    increment == IncrementTypes.PATCH
  }

  def "#with Conventional Commit of 'feature' type"() {
    given: 'a Conventional Commits checker'
    def environment = Mock(Environment)
    def pipeline = Mock(PipeLineScript)
    def conventionalCommitsChecker = new ConventionalCommitsChecker(pipeline)
    org.codehaus.groovy.runtime.GStringImpl anyString = GroovyMock(global: true)
    def url = GroovyMock(URL)

    when: 'checking with a Conventional Commit'
    def increment = conventionalCommitsChecker.check('dashboard')

    then: 'it does not throw and error and returns the correct increment'
    1 * pipeline.env >> environment
    1 * environment.ghprbActualCommit >> 'c56jh987shui1'
    1 * anyString.toURL() >> url
    1 * url.text >> ''
    1 * pipeline.readJSON([text: '']) >> [
      [commit: [message: 'fix: correct formatting']],
      [commit: [message: 'feat: Support for cheking Pull Requests for Conventional Commits']]
    ]
    increment == IncrementTypes.MINOR
  }

  def "#with Conventional Commit of 'BREAKING CHANGE' type"() {
    given: 'a Conventional Commits checker'
    def environment = Mock(Environment)
    def pipeline = Mock(PipeLineScript)
    def conventionalCommitsChecker = new ConventionalCommitsChecker(pipeline)
    org.codehaus.groovy.runtime.GStringImpl anyString = GroovyMock(global: true)
    def url = GroovyMock(URL)

    when: 'checking with a Conventional Commit'
    def increment = conventionalCommitsChecker.check('dashboard')

    then: 'it does not throw and error and returns the correct increment'
    1 * pipeline.env >> environment
    1 * environment.ghprbActualCommit >> 'c56jh987shui1'
    1 * anyString.toURL() >> url
    1 * url.text >> ''
    1 * pipeline.readJSON([text: '']) >> [
      [commit: [message: 'fix: correct formatting']],
      [commit: [message: 'BREAKING CHANGE: renamed all fields in the DB']],
      [commit: [message: 'feat: Support for cheking Pull Requests for Conventional Commits']]
    ]
    increment == IncrementTypes.MAJOR
  }

  def "#without Conventional Commits"() {
    given: 'a Conventional Commits checker'
    def environment = Mock(Environment)
    def pipeline = Mock(PipeLineScript)
    def conventionalCommitsChecker = new ConventionalCommitsChecker(pipeline)
    org.codehaus.groovy.runtime.GStringImpl anyString = GroovyMock(global: true)
    def url = GroovyMock(URL)

    when: 'checking with no Conventional Commits'
    conventionalCommitsChecker.check('dashboard')

    then: 'it throws an exception'
    1 * pipeline.env >> environment
    1 * environment.ghprbActualCommit >> 'c56jh987shui1'
    1 * anyString.toURL() >> url
    1 * url.text >> ''
    1 * pipeline.readJSON([text: '']) >> [
      [commit: [message: 'FIT-566']]
    ]
    def error = thrown(Exception)
    error.message == 'No conventional commits found'
  }
}
