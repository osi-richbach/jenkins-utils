package gov.ca.cwds.jenkins

class ChangelogUpdater {
  public static final String PROJECT_GITHUB_URL = 'git@github.com:ca-cwds/'
  public static final String GIT_USER = 'Jenkins'
  public static final String GIT_EMAIL = 'cwdsdoeteam@osi.ca.gov'
  public static final String MASTER = 'master'
  def script

  ChangelogUpdater(script) {
    this.script = script
  }

  def changelog() {
    def changelogString = script.gitChangelog returnType: 'STRING',
    to: [type: 'REF', value: MASTER],
    template: '''# Changelog
### Changelog for {{ownerName}} {{repoName}}
{{#tags}}
## {{name}}
{{#issues}}
{{#hasIssue}}
{{#hasLink}}
## {{name}} [{{issue}}]({{link}}{{issue}}) {{title}}
{{/hasLink}}
{{^hasLink}}
## {{name}} {{issue}} {{title}}
{{/hasLink}}
{{/hasIssue}}
{{^hasIssue}}
## {{name}}
{{/hasIssue}}

{{#commits}}
### [{{hash}}](https://github.com/{{ownerName}}/{{repoName}}/commit/{{hash}}) {{authorName}} {{commitTime}}

### {{{messageTitle}}}

{{#messageBodyItems}}
* {{.}}
{{/messageBodyItems}}

{{/commits}}

{{/issues}}
{{/tags}}
'''
    changelogString
}

  def update(applicationName, credentialsId) {
    try {
      updateAttempt(applicationName, credentialsId)
    } catch (IOException e) {
      updateAttempt(applicationName, credentialsId)
    }
  }

  def updateAttempt(applicationName, credentialsId) {
    script.ws {
      updateInsideNewWorkSpace(applicationName, credentialsId)
    }
  }

  def updateInsideNewWorkSpace(applicationName, credentialsId) {
    checkoutProject(applicationName, credentialsId)
    updateChangelogFile()
    script.sshagent(credentials: [credentialsId]) { commitChangelogFile(applicationName) }
  }

  def commitChangelogFile(applicationName) {
    if (script.sh(script: 'git status --porcelain', returnStdout: true)) {
      script.sh(script: "git config --global user.email ${GIT_EMAIL}")
      script.sh(script: "git config --global user.name '${GIT_USER}'")
      script.sh(script: 'git add CHANGELOG.md')
      script.sh(script: commitMessage(applicationName))
      script.sh(script: 'git push origin master')
    }
  }

  private checkoutProject(applicationName, credentialsId) {
    script.git branch: MASTER, credentialsId: credentialsId, url: PROJECT_GITHUB_URL + applicationName
  }

  private updateChangelogFile() {
    script.writeFile file: 'CHANGELOG.md', text: changelog()
  }

  private commitMessage(applicationName) {
    "git commit -m \"Update Changelog for ${applicationName} from Jenkins :octocat:\""
  }
}
