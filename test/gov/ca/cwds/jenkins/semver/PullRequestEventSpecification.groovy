package gov.ca.cwds.jenkins.semver

import spock.lang.Specification

class PullRequestEventSpecification extends Specification {
  class PipeLineScript {
    Environment env

    def readJSON(hash) {
    }

    PipeLineScript() {
      this.env = new Environment()
    }
  }

  class Environment {
    @SuppressWarnings('PropertyName')
    String pull_request_event

    Environment() {
      this.pull_request_event = 'pr_event'
    }
  }

  @SuppressWarnings('UnnecessaryGetter')
  def "#getEvent"() {
    def buildArguments

    given:
    PipeLineScript pipeline = Spy()
    def pullRequestEvent = new PullRequestEvent(pipeline)

    when:
    pullRequestEvent.getEvent()

    then:
    1 * pipeline.readJSON(_) >> { arguments -> buildArguments = arguments[0] }
    buildArguments['text'] == 'pr_event'
  }

  @SuppressWarnings('UnnecessaryGetter')
  def "#getPRLabels"() {
    given:
    def pullRequestEvent = Spy(PullRequestEvent)
    pullRequestEvent.getEvent() >> [labels: [[name: 'lis'], [name: 'minor']]]

    when:
    def labels = pullRequestEvent.getPRLabels()

    then:
    labels[0] == 'lis'
    labels[1] == 'minor'
  }

}
