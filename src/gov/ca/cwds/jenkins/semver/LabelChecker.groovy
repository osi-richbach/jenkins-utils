package gov.ca.cwds.jenkins.semver

class LabelChecker {
  def script

  LabelChecker(script) {
    this.script = script
  }

  def check(projectName, credentials, List tagPrefixes = []) {
    List labels = getPRLabels(projectName, credentials)
    if (tagPrefixes) {
      new TagPrefixFinder(tagPrefixes).find(labels)
    }
    new VersionIncrement().increment(labels)
  }

  def getPRLabels(projectName, credentials) {
    //def pullRequestUrl = "https://api.github.com/repos/ca-cwds/${projectName}/issues/${script.env.ghprbPullId}/labels"
    //def response = pullRequestUrl.toURL().text

    // def labels = script.readJSON(text: response)*.name
    // labels


    def get = new URL("https://api.github.com/repos/ca-cwds/${projectName}/issues/${script.env.ghprbPullId}/labels").openConnection();
    get.setRequestProperty ("Authorization", "token: 95bc969a8a27b7c8987e6bb82e164e61e35f6cbd")
    script.echo "******************I AM HERE"
    def response = get.getInputStream().getText()
    script.echo( response )
    def labels = script.readJSON(text: response)*.name
    labels
  }
}
