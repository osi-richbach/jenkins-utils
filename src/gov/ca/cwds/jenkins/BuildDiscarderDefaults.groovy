package gov.ca.cwds.jenkins

/**
 * Creates Build Discarder configuration.
 */
class BuildDiscarderDefaults {

  def configure(String buildType = null) {
    def numToKeepStr = buildType == 'master' ? '50' : '10'
    [ $class: 'BuildDiscarderProperty',
     strategy: [ $class: 'LogRotator', daysToKeepStr: '-1', numToKeepStr: numToKeepStr]
    ]
  }
}
