import gov.ca.cwds.jenkins.pr.ConventionalCommitsChecker

def call(String projectName) {
  def checker = new ConventionalCommitsChecker(this)
  checker.check(projectName)
}
