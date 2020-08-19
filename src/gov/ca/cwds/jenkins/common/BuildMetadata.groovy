package gov.ca.cwds.jenkins.common

class BuildMetadata {
  def script
  def jobName
  def buildId
  def projectPath
  def projectTypesDeterminer
  def githubToken

  BuildMetadata(script, String jobName, String buildId, String projectPath, String githubToken) {
    this.script = script
    this.jobName = jobName
    this.buildId = buildId
    this.projectPath = projectPath
    this.projectTypesDeterminer = new ProjectTypesDeterminer(script)
    this.githubToken = githubToken
  }

  def projectTypes() {
    projectTypesDeterminer.determineProjectTypes(projectPath)
  }
}
