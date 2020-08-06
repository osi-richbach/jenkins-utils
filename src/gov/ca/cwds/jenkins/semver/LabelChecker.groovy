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
    //get.setRequestProperty ("Authorization", "token: 95bc969a8a27b7c8987e6bb82e164e61e35f6cbd")

    String userCredentials = "osi-richbach:a05898aa2929326e2ccecac57b80f4aeefcec3bd";
    String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

    get.setRequestProperty ("Authorization", basicAuth);
    get.setRequestProperty ("User-Agent", "cwds/1.0 ( jenkins )")
    script.echo "******************I AM HERE $credentials"
    def response = get.getInputStream().getText()
    script.echo( response )
    def labels = script.readJSON(text: response)*.name
    labels
  }
}
