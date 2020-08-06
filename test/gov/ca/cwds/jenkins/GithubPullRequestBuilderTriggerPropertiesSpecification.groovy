package gov.ca.cwds.jenkins

import spock.lang.Specification

class GithubPullRequestBuilderTriggerPropertiesSpecification extends Specification {
  class Environment {
    public static final String JOB_NAME = 'myjob'
    public static final String BUILD_ID = 33
  }
  class PipelineScript {
    def env = new Environment()
  }

  @SuppressWarnings('GStringExpressionWithinString')
  def "#triggerProperties returns a map with correct status url"() {
    given:
    def githubPullRequestBuilderTriggerProperties = new GithubPullRequestBuilderTriggerProperties(new PipelineScript())

    when:
    def properties = githubPullRequestBuilderTriggerProperties.triggerProperties('https://example.com')

    then:
    properties['extensions'][1]['statusUrl'] == 'https://example.com/blue/organizations/' +
                                                  'jenkins/myjob/detail/myjob/${BUILD_NUMBER}'
  }
}
