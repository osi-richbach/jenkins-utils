package gov.ca.cwds.jenkins

import spock.lang.Specification

class ChangelogUpdaterSpecification extends Specification {
  class PipeLineScript {

    def sshagent(hash, closure) { }

    def sh(hash) { }

    def ws(closure) { }

    def git(hash) { }

  }

  def "#update runs in a new workspace"() {
    given: 'a changelog updater'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def changelogUpdater = new ChangelogUpdater(pipeline)

    when: 'updating integration in cans'
    changelogUpdater.update('cans', 'cr-01')

    then: 'it creates a new workspace'
    1 * pipeline.ws(_ as Closure)
  }

  def "#update makes a second attempt if the first one fails"() {
    given: 'a manifest updater'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def changelogUpdater = new ChangelogUpdater(pipeline)

    when: 'commiting the change in CARES'
    changelogUpdater.update('cans', 'cr-01')

    then: 'makes a single second attempt if the first one fails'
    1 * pipeline.ws(_ as Closure) >> { throw new IOException() }
    1 * pipeline.ws(_ as Closure) >> { throw new IOException() }
    thrown(IOException)
    0 * pipeline.ws(_ as Closure)
  }

  def "#updateInsideNewWorkSpace writes to the yaml file"() {
    given: 'a changelog updater'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def changelogUpdater = new ChangelogUpdater(pipeline)

    when: 'updating dashboard in preint'
    changelogUpdater.updateInsideNewWorkSpace('dashboard', 'cr-01')

    then: 'it updates the file and commits the change'
    1 * pipeline.git([branch: 'master', credentialsId: 'cr-01', url: 'git@github.com:ca-cwds/dashboard'])
  }

  def "#commitVersionInCares commits the update"() {
    given: 'a changelog updater'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def changelogUpdater = new ChangelogUpdater(pipeline)

    when: 'commiting the change in CARES'
    changelogUpdater.commitChangelogFile('dashboard')

    then: 'it commits as the Jenkins user'
    1 * pipeline.sh([script: 'git status --porcelain', returnStdout: true]) >> 'M preint.yml'
    1 * pipeline.sh([script: 'git config --global user.email cwdsdoeteam@osi.ca.gov'])
    1 * pipeline.sh([script: "git config --global user.name 'Jenkins'"])
    1 * pipeline.sh([script: 'git add CHANGELOG.md'])
    1 * pipeline.sh([script: 'git commit -m \"Update Changelog for dashboard from Jenkins :octocat:\"'])
    1 * pipeline.sh([script: 'git push origin master'])
  }

}
