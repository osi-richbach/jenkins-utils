package gov.ca.cwds.jenkins

import spock.lang.Specification

class ReleaseNotesUpdaterSpecification extends Specification {
  class PipeLineScript {
    def gitChangelog(hash) { }

    def sh(hash) { }

    def ws(closure) { }

    def git(hash) { }
  }

  def "#update runs in a new workspace"() {
    given: 'a Release Notes updater'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def releaseNotesUpdater = new ReleaseNotesUpdater(pipeline)

    when: 'updating release notes'
    releaseNotesUpdater.update('dashboard', 'cr-01')

    then: 'it creates a new workspace'
    1 * pipeline.ws(_ as Closure)
  }

  def "#update makes a second attempt if the first one fails"() {
    given: 'a Release Notes updater'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def releaseNotesUpdater = new ReleaseNotesUpdater(pipeline)

    when: 'committing the changes'
    releaseNotesUpdater.update('dashboard', 'cr-01')

    then: 'makes a single second attempt if the first one fails'
    1 * pipeline.ws(_ as Closure) >> { throw new IOException() }
    1 * pipeline.ws(_ as Closure) >> { throw new IOException() }
    thrown(IOException)
    0 * pipeline.ws(_ as Closure)
  }

  def '#updateInsideNewWorkSpace performs the project checkout'() {
    given: 'a Release Notes Updater'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def releaseNotesUpdater = new ReleaseNotesUpdater(pipeline)

    when: 'updating dashboard in preint'
    releaseNotesUpdater.updateInsideNewWorkSpace('dashboard', 'cr-01')

    then: 'it performs the project checkout'
    1 * pipeline.git([branch: 'master', credentialsId: 'cr-01', url: 'git@github.com:ca-cwds/dashboard'])
    1 * pipeline.gitChangelog([returnType: 'CONTEXT', to: [type: 'REF', value: 'master']]) >> []
  }

  def "#commitReleaseNotesFile commits the update"() {
    given: 'a Release Notes updater'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def releaseNotesUpdater = new ReleaseNotesUpdater(pipeline)

    when: 'committing the changes'
    releaseNotesUpdater.commitReleaseNotesFile('dashboard')

    then: 'it commits as the Jenkins user'
    1 * pipeline.sh([script: 'git status --porcelain', returnStdout: true]) >> 'M RELEASE_NOTES.md'
    1 * pipeline.sh([script: 'git config --global user.email cwdsdoeteam@osi.ca.gov'])
    1 * pipeline.sh([script: "git config --global user.name 'Jenkins'"])
    1 * pipeline.sh([script: 'git add RELEASE_NOTES.md'])
    1 * pipeline.sh([script: "git commit -m 'Update Release Notes for dashboard from Jenkins :octocat:'"])
    1 * pipeline.sh([script: 'git push origin master'])
  }

  def '#releaseNotes generates correct content from gitChangelog'() {
    given: 'a Release Notes Updater'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def releaseNotesUpdater = new ReleaseNotesUpdater(pipeline)

    when: 'calling releaseNotes'
    def releaseNotes = releaseNotesUpdater.releaseNotes()

    then: 'returns correct content'
    1 * pipeline.gitChangelog([returnType: 'CONTEXT', to: [type: 'REF', value: 'master']]) >> [
      ownerName: 'ca-cwds',
      repoName: 'dashboard',
      tags: [
        [
          name: '0.8.0',
          tagTime: '2019-05-01 19:38:22',
          commits: [
            [ messageBodyItems: [ 'feat: Release Notes generation' ] ]
          ]
        ],
        [
          name: '0.7.3',
          tagTime: '2019-04-28 19:31:59',
          commits: [
            [ messageBodyItems: [ 'fix: corrected codenarc' ] ]
          ]
        ],
        [
          name: '0.7.2',
          tagTime: '2019-04-25 19:12:43',
          commits: [
            [ messageBodyItems: [ 'FIT-566' ] ]
          ]
        ]
      ]
    ]
    releaseNotes == '''
# Release Notes for ca-cwds dashboard

## 05/01/2019  0.8.0

### Features

#### Release Notes generation

## 04/28/2019  0.7.3

### Fixes

#### corrected codenarc

## 04/25/2019  0.7.2

\n'''
  }
}
