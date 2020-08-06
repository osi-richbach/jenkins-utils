package gov.ca.cwds.jenkins

class ManifestUpdater {
  public static final String CARES_GITHUB_URL = 'git@github.com:ca-cwds/cws-cares.git'
  public static final String GIT_USER = 'Jenkins'
  public static final String GIT_EMAIL = 'cwdsdoeteam@osi.ca.gov'
  public static final String IGNORE_KNOWN_SSH_HOSTS = 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null' +
     ' -o StrictHostKeyChecking=no"'
  def script

  ManifestUpdater(script) {
    this.script = script
  }

  def update(applicationName, manifestName, credentialsId, version) {
    try {
      updateAttempt(applicationName, manifestName, credentialsId, version)
    } catch (IOException e) {
      updateAttempt(applicationName, manifestName, credentialsId, version)
    }
  }

  def updateAttempt(applicationName, manifestName, credentialsId, version) {
    script.ws {
      updateInsideNewWorkSpace(applicationName, manifestName, credentialsId, version)
    }
  }

  def updateInsideNewWorkSpace(applicationName, manifestName, credentialsId, version) {
    checkoutCares(credentialsId)
    updateManifestFile(applicationName, manifestName, version)
    script.sshagent(credentials: [credentialsId]) { commitVersionInCares(applicationName, version, manifestName) }
  }

  def commitVersionInCares(applicationName, version, manifestName) {
    if (script.sh(script: 'git status --porcelain --untracked-files=no', returnStdout: true)) {
      script.sh(script: "git config --global user.email ${GIT_EMAIL}")
      script.sh(script: "git config --global user.name '${GIT_USER}'")
      script.sh(script: commitMessage(applicationName, version, manifestName))
      script.sh(script: "${IGNORE_KNOWN_SSH_HOSTS} git push origin master")
    }
  }

  private checkoutCares(credentialsId) {
    script.git branch: 'master', credentialsId: credentialsId, url: CARES_GITHUB_URL
  }

  private updateManifestFile(applicationName, manifestName, version) {
    def properties = script.readYaml file: "${manifestName}.yaml"
    properties."${applicationName}" = version
    script.sh("rm ${manifestName}.yaml")
    script.writeYaml file: "${manifestName}.yaml", data: properties
  }

  private commitMessage(applicationName, version, manifestName) {
    "git commit -am \"Update ${applicationName} to ${version} from Jenkins on ${manifestName} :octocat:\""
  }
}
