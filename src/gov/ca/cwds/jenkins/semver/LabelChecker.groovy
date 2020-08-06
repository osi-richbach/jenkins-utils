package gov.ca.cwds.jenkins.semver

class LabelChecker {
  def script

  LabelChecker(script) {
    this.script = script
  }

  def check(projectName, List tagPrefixes = []) {
    List labels = getPRLabels(projectName)
    if (tagPrefixes) {
      new TagPrefixFinder(tagPrefixes).find(labels)
    }
    new VersionIncrement().increment(labels)
  }

  def getPRLabels(projectName) {
    pipeline.withCredentials([string(credentialsId: '433ac100-b3c2-4519-b4d6-207c029a103b', variable: 'TOKEN')]) {
      //def pullRequestUrl = "https://api.github.com/repos/ca-cwds/${projectName}/issues/${script.env.ghprbPullId}/labels"
      //def response = pullRequestUrl.toURL().text

      // def labels = script.readJSON(text: response)*.name
      // labels


      def get = new URL("https://api.github.com/repos/ca-cwds/${projectName}/issues/${script.env.ghprbPullId}/labels").openConnection();
      get.setRequestProperty ("Authorization", "token: ${TOKEN}")
    
      def response = get.getInputStream().getText()

      def labels = script.readJSON(text: response)*.name
      labels

    }
  }
}
