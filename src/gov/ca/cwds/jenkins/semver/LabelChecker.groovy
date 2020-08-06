package gov.ca.cwds.jenkins.semver

import groovy.json.JsonSlurper 

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
    def get = new URL("https://api.github.com/repos/ca-cwds/${projectName}/issues/${script.env.ghprbPullId}/labels").openConnection();

    String userCredentials = "cwds-jenkins-dev:${credentials}";
    String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

    get.setRequestProperty ("Authorization", basicAuth);
    get.setRequestProperty ("User-Agent", "cwds/1.0 ( jenkins )")
    
    def response = get.getInputStream().getText()
    script.echo "******************I AM HERE ${response}"
    script.echo "The type is"
    script.echo response.getClass().getName()
    script.echo "That was the type"
    def jsonSlurper = new JsonSlurper()
    Object json = jsonSlurper.parseText(response)
    //def json = script.readJSON(text: response)
    script.echo "have the json"
    def labels = json*.name
    script.echo("****The labels are")
    script.echo(labels)
    script.echo("****The labels are")
    labels
  }
}
