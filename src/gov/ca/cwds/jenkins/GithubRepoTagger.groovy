package gov.ca.cwds.jenkins

class GithubRepoTagger {
  public static final String GIT_SSH_COMMAND = 'GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o ' +
    'StrictHostKeyChecking=no"'
  public static final String  GIT_USER = 'Jenkins'
  public static final String  GIT_EMAIL = 'cwdsdoeteam@osi.ca.gov'
  def script

  GithubRepoTagger(script) {
    this.script = script
  }

  def tagWith(tag, credentials) {
    script.sshagent(credentials: [credentials]) { tagAndPush(tag) }
  }

  def tagAndPush(tag) {
    def tagStatus = script.sh(script: "git tag ${tag}", returnStatus: true)
    if ( tagStatus != 0) {
      throw new Exception("Unable to tag the repository with tag '${tag}'")
    }
    def configStatus = script.sh(script: configGitCredentialsCommand(), returnStatus: true)
    if ( configStatus != 0) {
      throw new Exception('Unable to config the Jenkins user')
    }
    def pushStatus = script.sh(script: "${GIT_SSH_COMMAND} git push origin ${tag}", returnStatus: true)
    if ( pushStatus != 0) {
      throw new Exception("Unable to push the tag '${tag}'")
    }
  }

  private configGitCredentialsCommand() {
    "${GIT_SSH_COMMAND} git config --global user.email ${GIT_EMAIL}; git config --global user.name ${GIT_USER}"
  }
}
