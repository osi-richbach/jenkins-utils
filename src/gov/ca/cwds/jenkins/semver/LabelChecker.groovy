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
    get.setRequestProperty ("Authorization", "token: accf1d7851696c1d542d0bf3b1f18d06fe997356")
    script.echo "******************I AM HERE"
    def response = get.getInputStream().getText()
    script.echo( response )
    def labels = script.readJSON(text: response)*.name
    labels
  }
}
