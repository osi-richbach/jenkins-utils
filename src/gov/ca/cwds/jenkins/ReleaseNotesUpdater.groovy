package gov.ca.cwds.jenkins

import gov.ca.cwds.jenkins.pr.CommitCommentsAnalyzer
import gov.ca.cwds.jenkins.pr.CommitTypeGroups
import groovy.text.GStringTemplateEngine

class ReleaseNotesUpdater {
  public static final String PROJECT_GITHUB_URL = 'git@github.com:ca-cwds/'
  public static final String GIT_USER = 'Jenkins'
  public static final String GIT_EMAIL = 'cwdsdoeteam@osi.ca.gov'
  public static final String MASTER = 'master'
  private static final String RELEASE_NOTES_FILE = 'RELEASE_NOTES.md'
  def script

  ReleaseNotesUpdater(script) {
    this.script = script
  }

  def releaseNotes() {
    def changelogContext = script.gitChangelog returnType: 'CONTEXT',
      to: [type: 'REF', value: MASTER]

    def releasedTags = changelogContext.tags.findAll { it.name != 'Unreleased' }
    def binding = [
      ownerName: changelogContext.ownerName,
      repoName: changelogContext.repoName,
      tags: releasedTags.collect { tag ->
        def commitComments = []
        tag.commits.each { commitComments.addAll(it.messageBodyItems) }
        commitComments = commitComments.unique()
        def groupedComments = CommitCommentsAnalyzer.groupCommitComments(commitComments)
        [
          name: tag.name,
          features: groupedComments.get(CommitTypeGroups.FEATURES).collect { it - ~/^.+:\s*/ },
          fixes: groupedComments.get(CommitTypeGroups.FIXES).collect { it - ~/^.+:\s*/ },
          tagDate: "${tag.tagTime[5..6]}/${tag.tagTime[8..9]}/${tag.tagTime[0..3]}"
        ]
      }
    ]

    def releaseNotesTemplate = '''
# Release Notes for <%= ownerName %> <%= repoName %>
<% tags.each { tag -> %>
## <%= tag.tagDate %>  <%= tag.name %>

<%   if (tag.features) { %>### Features
<%     tag.features.each { feature -> %>
#### <%= feature %>
<%     }
     }
     if (tag.fixes) { %>### Fixes
<%     tag.fixes.each { fix -> %>
#### <%= fix %>
<%     }
     }
   } %>
'''

    def engine = new GStringTemplateEngine()
    def template = engine.createTemplate(releaseNotesTemplate)
    def writer = new StringWriter()
    template.make(binding).writeTo(writer)
    writer.flush()
    writer.toString()
  }

  def update(applicationName, credentialsId) {
    try {
      updateAttempt(applicationName, credentialsId)
    } catch (ignored) {
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
    updateReleaseNotesFile()
    script.sshagent(credentials: [credentialsId]) { commitReleaseNotesFile(applicationName) }
  }

  def commitReleaseNotesFile(applicationName) {
    if (script.sh(script: 'git status --porcelain', returnStdout: true)) {
      script.sh(script: "git config --global user.email ${GIT_EMAIL}")
      script.sh(script: "git config --global user.name '${GIT_USER}'")
      script.sh(script: "git add ${RELEASE_NOTES_FILE}")
      script.sh(script: commitMessage(applicationName))
      script.sh(script: 'git push origin master')
    }
  }

  private checkoutProject(applicationName, credentialsId) {
    script.git branch: MASTER, credentialsId: credentialsId, url: PROJECT_GITHUB_URL + applicationName
  }

  private updateReleaseNotesFile() {
    script.writeFile file: RELEASE_NOTES_FILE, text: releaseNotes()
  }

  private commitMessage(applicationName) {
    "git commit -m 'Update Release Notes for ${applicationName} from Jenkins :octocat:'"
  }
}
