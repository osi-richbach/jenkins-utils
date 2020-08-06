package gov.ca.cwds.jenkins

import spock.lang.Specification

class BuildDiscarderDefaultsSpecification extends Specification {

  def "#configure returns numToKeepStr set to 50 when buildType is master"() {
    given:
    def buildDiscarderDefaults = new BuildDiscarderDefaults()

    when:
    def properties = buildDiscarderDefaults.configure('master')

    then:
    properties['$class'] == 'BuildDiscarderProperty'
    properties['strategy']['$class'] == 'LogRotator'
    properties['strategy']['daysToKeepStr'] == '-1'
    properties['strategy']['numToKeepStr'] == '50'
  }

  def "#configure returns numToKeepStr set to 10 when buildType is not master"() {
    given:
    def buildDiscarderDefaults = new BuildDiscarderDefaults()

    when:
    def properties = buildDiscarderDefaults.configure()

    then:
    properties['$class'] == 'BuildDiscarderProperty'
    properties['strategy']['$class'] == 'LogRotator'
    properties['strategy']['daysToKeepStr'] == '-1'
    properties['strategy']['numToKeepStr'] == '10'
  }
}
