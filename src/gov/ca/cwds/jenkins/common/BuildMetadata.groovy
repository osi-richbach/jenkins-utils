package gov.ca.cwds.jenkins.common

class BuildMetadata {
  def script
  def jobName
  def buildId
  def projectPath
  def projectTypesDeterminer

  BuildMetadata(script, String jobName, String buildId, String projectPath) {
    this.script = script
    this.jobName = jobName
    this.buildId = buildId
    this.projectPath = projectPath
    this.projectTypesDeterminer = new ProjectTypesDeterminer(script)
  }

  def projectTypes() {
    projectTypesDeterminer.determineProjectTypes(projectPath)
  }
}
