package gov.ca.cwds.jenkins.semver

import groovy.json.JsonSlurper 

class LabelChecker {
  def script

  LabelChecker(script) {
    this.script = script
  }

  def check(projectName, List tagPrefixes = [], username = '', accesskey = '' ) {
    List labels = getPRLabels(projectName, username, accesskey)
    if (tagPrefixes) {
      new TagPrefixFinder(tagPrefixes).find(labels)
    }
    new VersionIncrement().increment(labels)
  }

  def getPRLabels(projectName, username, accesskey) {
    def get = new URL("https://api.github.com/repos/ca-cwds/${projectName}/issues/${script.env.ghprbPullId}/labels").openConnection();
    
    if (username?.trim() && accesskey?.trim()) {
      String userCredentials = "${username}:${accesskey}";
      String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
      get.setRequestProperty ("Authorization", basicAuth);
    }
    get.setRequestProperty ("User-Agent", "cwds/1.0 ( jenkins )")
    def response = get.getInputStream().getText()
    def jsonSlurper = new JsonSlurper()
    def labels = jsonSlurper.parseText(response)*.name
    
    labels
  }
}
