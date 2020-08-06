package gov.ca.cwds.jenkins.semver

class PullRequestEvent {
  def script

  PullRequestEvent(script) {
    this.script = script
  }

  def getEvent() {
    script.readJSON text: script.env.pull_request_event
  }

  @SuppressWarnings('UnnecessaryGetter')
  def getPRLabels() {
    def eventLabels = getEvent().labels.collect([]) { it.name }
    eventLabels
  }
}
