package gov.ca.cwds.jenkins.licensing

class LicensingSupport {
  public static final String GIT_USER = 'Jenkins'
  public static final String GIT_EMAIL = 'cwdsdoeteam@osi.ca.gov'
  public static final String LICENSE_BUILD_FOLDER = 'build/reports/dependency-license'
  public static final String LICENSE_FOLDER = 'legal'
  public static final String MSG_NO_LICENSING_SUPPORT = 'No known Licensing Support is found in the project'

  def pipeline
  def runtimeGradle
  def dockerImage
  def licensingSupportTypeDeterminer

  LicensingSupport(pipeline, runtimeGradle = null, dockerImage = null) {
    this.pipeline = pipeline
    this.runtimeGradle = runtimeGradle
    this.dockerImage = dockerImage
    this.licensingSupportTypeDeterminer = new LicensingSupportTypeDeterminer(pipeline)
  }

  def updateLicenseReport(branchName, sshCredentialsId, buildMetadata) {
    if (branchName == 'master') {
      generateLicenseReport(buildMetadata)
      pushLicenseReport(sshCredentialsId)
    } else {
      pipeline.echo 'Not working with the master branch. Skipping Update License Report for the other branch.'
    }
  }

  private generateLicenseReport(buildMetadata) {
    pipeline.echo 'Generating License Information'
    def licensingSupportType = licensingSupportTypeDeterminer.determineLicensingSupportType(buildMetadata)
    if (licensingSupportType == LicensingSupportType.JK1_GRADLE_LICENSE_REPORT) {
      if (runtimeGradle == null) {
        pipeline.sh './gradlew generateLicenseReport'
      } else {
        runtimeGradle.run buildFile: 'build.gradle', tasks: 'generateLicenseReport'
      }
      pipeline.sh script: "mkdir ${LICENSE_FOLDER}", returnStatus: true
      pipeline.sh "cp ${LICENSE_BUILD_FOLDER}/licenses.csv ${LICENSE_FOLDER}"
    } else if (licensingSupportType == LicensingSupportType.RUBY_LICENSE_FINDER) {
      pipeline.sh script: "mkdir ${LICENSE_FOLDER}", returnStatus: true
      dockerImage.withRun('-e CI=true') { container ->
        pipeline.sh script: "docker exec -t ${container.id} mkdir ${LICENSE_FOLDER}", returnStatus: true
        pipeline.sh "docker exec -t ${container.id} yarn licenses-report"
        def projectDir = pipeline.sh(script: "docker exec -t ${container.id} pwd", returnStdout: true).trim()
        pipeline.sh "docker cp ${container.id}:${projectDir}/${LICENSE_FOLDER}/licenses.csv ${LICENSE_FOLDER}/"
      }
    } else {
      throw new Exception(MSG_NO_LICENSING_SUPPORT)
    }
  }

  private pushLicenseReport(sshCredentialsId) {
    pipeline.echo 'Updating License Information'
    pipeline.sshagent(credentials: [sshCredentialsId]) {
      runGitSshCommand("git config --global user.name ${GIT_USER}")
      runGitSshCommand("git config --global user.email ${GIT_EMAIL}")
      runGitSshCommand("git add ${LICENSE_FOLDER}")
      runGitSshCommand('git commit -m "updated license info" || true')
      runGitSshCommand('git push origin HEAD:master')
    }
  }

  private runGitSshCommand(command) {
    // the GIT_SSH_COMMAND variable is used to avoid known_hosts addition
    // which would require each machine to have GitHub added in advance
    pipeline.sh script: 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no" ' + command
  }
}
