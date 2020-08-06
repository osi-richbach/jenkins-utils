package gov.ca.cwds.jenkins.licensing

import spock.lang.Specification

class LicensingSupportSpecification extends Specification {

  class PipeLineScript {
    def sshagent(hash, closure) {
      closure()
    }

    def sh(hash) { }

    def echo(string) { }
  }

  class RuntimeGradle {
    def run(map) { }
  }

  class DockerImage {
    def withRun(string, closure) { }
  }

  def "When updating license report for a non-master branch then skip report generation"() {
    given: 'a pipeline'
    def pipeline = Mock(PipeLineScript)

    and: 'a licensing support instance which is the class under test'
    def licensingSupport = new LicensingSupport(pipeline)

    when: 'it is asked to update the license report for a non-master branch'
    licensingSupport.updateLicenseReport('myTempBranch', 'credentials-id', _)

    then: 'it will not invoke any command for report generation'
    0 * pipeline.sh('./gradlew generateLicenseReport')

    and: 'it will print a message about that updating report is skipped'
    1 * pipeline.echo('Not working with the master branch. Skipping Update License Report for the other branch.')
  }

  def "When licensing support can't detect Licensing Support Type then Exception is thrown"() {
    given: 'a pipeline'
    def pipeline = Stub(PipeLineScript)

    and: "it can't detect Licensing Support Type"
    def licensingSupportTypeDeterminer = Mock(LicensingSupportTypeDeterminer)
    licensingSupportTypeDeterminer.determineLicensingSupportType(_) >> LicensingSupportType.NONE

    and: 'a licensing support instance which is the class under test'
    def licensingSupport = new LicensingSupport(pipeline)
    licensingSupport.licensingSupportTypeDeterminer = licensingSupportTypeDeterminer

    when: 'it is asked to update the license report for the master branch'
    licensingSupport.updateLicenseReport('master', 'credentials-id', _)

    then: 'it will throw an exception with a message that no known licensing support is found'
    def exception = thrown(Exception)
    exception.message == 'No known Licensing Support is found in the project'
  }

  def "for a project with gradle license report plugin run gradlew to generate report"() {
    given: 'a pipeline'
    def pipeline = Mock(PipeLineScript)

    and: 'a project with gradle license report plugin'
    def licensingSupportTypeDeterminer = Mock(LicensingSupportTypeDeterminer)
    licensingSupportTypeDeterminer.determineLicensingSupportType(_) >> LicensingSupportType.JK1_GRADLE_LICENSE_REPORT

    and: 'it can successfully run ssh git commands'
    pipeline.sh([script: 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no" ' +
      'git config --global user.name Jenkins', returnStatus: true]) >> 0
    pipeline.sh([script: 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no" ' +
      'git config --global user.email cwdsdoeteam@osi.ca.gov', returnStatus: true]) >> 0
    pipeline.sh([script: 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no" ' +
      'git push --set-upstream origin master', returnStatus: true]) >> 0

    and: 'a licensing support instance which is the class under test'
    def licensingSupport = new LicensingSupport(pipeline)
    licensingSupport.licensingSupportTypeDeterminer = licensingSupportTypeDeterminer

    when: 'it is asked to update the license report for the master branch'
    licensingSupport.updateLicenseReport('master', 'credentials-id', _)

    then: "License report gradle plugin is invoked using the project's gradle wrapper"
    1 * pipeline.sh('./gradlew generateLicenseReport')

    and: "generated license report is copied to the project's 'legal' folder"
    1 * pipeline.sh([script: 'mkdir legal', returnStatus: true])
    1 * pipeline.sh('cp build/reports/dependency-license/licenses.csv legal')

    and: 'a set of ssh git commands is executed to push possible changes of license report to the master branch'
    1 * pipeline.sshagent([credentials: ['credentials-id']], _)

    and: 'corresponding messages are printed'
    1 * pipeline.echo('Generating License Information')
    1 * pipeline.echo('Updating License Information')
  }

  def "for a project with gradle license report plugin when RuntimeGradle is provided"() {
    given: 'a pipeline'
    def pipeline = Mock(PipeLineScript)

    and: 'a project with gradle license report plugin'
    def licensingSupportTypeDeterminer = Mock(LicensingSupportTypeDeterminer)
    licensingSupportTypeDeterminer.determineLicensingSupportType(_) >> LicensingSupportType.JK1_GRADLE_LICENSE_REPORT

    and: 'it can successfully run ssh git commands'
    pipeline.sh([script: 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no" ' +
      'git config --global user.name Jenkins', returnStatus: true]) >> 0
    pipeline.sh([script: 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no" ' +
      'git config --global user.email cwdsdoeteam@osi.ca.gov', returnStatus: true]) >> 0
    pipeline.sh([script: 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no" ' +
      'git push --set-upstream origin master', returnStatus: true]) >> 0

    and: 'runtime gradle is provided'
    def runtimeGradle = Mock(RuntimeGradle)

    and: 'a licensing support instance which is the class under test'
    def licensingSupport = new LicensingSupport(pipeline, runtimeGradle)
    licensingSupport.licensingSupportTypeDeterminer = licensingSupportTypeDeterminer

    when: 'it is asked to update the license report for the master branch'
    licensingSupport.updateLicenseReport('master', 'credentials-id', _)

    then: 'License report gradle plugin is invoked using the provided runtime gradle'
    1 * runtimeGradle.run([buildFile: 'build.gradle', tasks: 'generateLicenseReport'])
    0 * pipeline.sh('./gradlew generateLicenseReport')

    and: "generated license report is copied to the project's 'legal' folder"
    1 * pipeline.sh([script: 'mkdir legal', returnStatus: true])
    1 * pipeline.sh('cp build/reports/dependency-license/licenses.csv legal')

    and: 'a set of ssh git commands is executed to push possible changes of license report to the master branch'
    1 * pipeline.sshagent([credentials: ['credentials-id']], _)

    and: 'corresponding messages are printed'
    1 * pipeline.echo('Generating License Information')
    1 * pipeline.echo('Updating License Information')
  }

  def "When a project has a license finder plugin"() {
    given: 'a pipeline'
    def pipeline = Mock(PipeLineScript)

    and: 'a project with License Finder plugin'
    def licensingSupportTypeDeterminer = Mock(LicensingSupportTypeDeterminer)
    licensingSupportTypeDeterminer.determineLicensingSupportType(_) >> LicensingSupportType.RUBY_LICENSE_FINDER

    and: 'it can successfully run ssh git commands'
    pipeline.sh([script: 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no" ' +
      'git config --global user.name Jenkins', returnStatus: true]) >> 0
    pipeline.sh([script: 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no" ' +
      'git config --global user.email cwdsdoeteam@osi.ca.gov', returnStatus: true]) >> 0
    pipeline.sh([script: 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no" ' +
      'git push --set-upstream origin master', returnStatus: true]) >> 0

    and: 'a licensing support instance which is the class under test'
    def dockerImage = Mock(DockerImage)
    def licensingSupport = new LicensingSupport(pipeline, null, dockerImage)
    licensingSupport.licensingSupportTypeDeterminer = licensingSupportTypeDeterminer

    when: 'it is asked to update the license report for the master branch'
    licensingSupport.updateLicenseReport('master', 'credentials-id', _)

    then: 'Ruby license finder plugin is invoked'
    1 * pipeline.sh([script: 'mkdir legal', returnStatus: true])
    1 * dockerImage.withRun(_ as String, _ as Closure)

    and: 'a set of ssh git commands is executed to push possible changes of license report to the master branch'
    1 * pipeline.sshagent([credentials: ['credentials-id']], _)

    and: 'corresponding messages are printed'
    1 * pipeline.echo('Generating License Information')
    1 * pipeline.echo('Updating License Information')
  }
}
