package gov.ca.cwds.jenkins

import gov.ca.cwds.jenkins.docker.Docker
import gov.ca.cwds.jenkins.common.ProjectTypes

class StaticAnalyzer {
  Docker docker
  def script
  def rtGradle

  StaticAnalyzer(docker, rtGradle, script) {
    this.docker = docker
    this.rtGradle = rtGradle
    this.script = script
  }

  def lint(buildMetadata) {
    def projectTypes = buildMetadata.projectTypes()
    if (projectTypes.contains(ProjectTypes.JAVA) ) {
      script.withSonarQubeEnv('Core-SonarQube') {
        rtGradle.run buildFile: 'build.gradle', switches: '--info', tasks: 'sonarqube'
      }
      def qualityGate = script.waitForQualityGate()
      if (qualityGate.status == 'ERROR') {
        throw new Exception('Pipeline aborted due to quality gate failure: ERROR')
      }
    }
    if (projectTypes.contains(ProjectTypes.JAVASCRIPT) ) {
      docker.withTestingImage('npm run lint', buildMetadata)
    }
    if (projectTypes.contains(ProjectTypes.RUBY) ) {
      docker.withTestingImage('rubocop', buildMetadata)
    }
  }
}
