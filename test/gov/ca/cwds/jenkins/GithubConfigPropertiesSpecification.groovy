package gov.ca.cwds.jenkins

import spock.lang.Specification

class GithubConfigPropertiesSpecification extends Specification {
  def "#configProperties returns a map with a projectUrl and displayName"() {
    given:
    def githubConfigProperties = new GithubConfigProperties()

    when:
    def properties = githubConfigProperties.configure('https://github.com/ca-cwds/dashboard/')

    then:
    properties['$class'] == 'GithubProjectProperty'
    properties['projectUrlStr'] == 'https://github.com/ca-cwds/dashboard/'
    properties['displayName'] == ''
  }
}
