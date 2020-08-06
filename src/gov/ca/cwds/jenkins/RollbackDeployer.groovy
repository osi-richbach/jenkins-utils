package gov.ca.cwds.jenkins

class RollbackDeployer {
  public static final String CARES_GITHUB_URL = 'git@github.com:ca-cwds/cws-cares.git'
  public static final String DE_ANSIBLE_GITHUB_URL = 'git@github.com:ca-cwds/de-ansible.git'
  public static final String MASTER = 'master'
  def script
  def manifestUpdater
  def ansibleCommandRunner
  def oldVersion = ''

  RollbackDeployer(script,
                   manifestUpdater = new ManifestUpdater(script),
                   ansibleCommandRunner = new AnsibleCommandRunner()) {
    this.script = script
    this.manifestUpdater = manifestUpdater
    this.ansibleCommandRunner = ansibleCommandRunner
  }

  @SuppressWarnings('CatchException')
  def rollback(applicationName, environment, githubCredentials, ansibleDeploymentScript, closure) {
    script.ws {
      oldVersion = getOldVersionFromManifest(applicationName, environment, githubCredentials)
    }
    try {
      closure.call()
    } catch (Exception exception) {
      script.ws {
        rollbackDeployment(ansibleDeploymentScript, githubCredentials)
      }
      manifestUpdater.update(applicationName, environment, githubCredentials, oldVersion)
      throw exception
    }
  }

  def getOldVersionFromManifest(applicationName, environment, githubCredentials) {
    checkoutCares(githubCredentials)
    def properties = script.readYaml file: "${environment}.yaml"
    properties."${applicationName}"
  }

  def rollbackDeployment(ansibleDeploymentScript, githubCredentials) {
    checkoutDeAnsible(githubCredentials)
    def ansibleRollBackCommand = ansibleCommandRunner.rollbackOldVersionCommand(ansibleDeploymentScript, oldVersion)
    script.sh script: ansibleRollBackCommand
  }

  private checkoutDeAnsible(githubCredentials) {
    script.git branch: MASTER, credentialsId: githubCredentials, url: DE_ANSIBLE_GITHUB_URL
  }
  private checkoutCares(githubCredentials) {
    script.git branch: MASTER, credentialsId: githubCredentials, url: CARES_GITHUB_URL
  }
}
