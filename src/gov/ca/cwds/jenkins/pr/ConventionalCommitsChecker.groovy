package gov.ca.cwds.jenkins.pr

class ConventionalCommitsChecker {
  def script

  ConventionalCommitsChecker(script) {
    this.script = script
  }

  def check(projectName) {
    def prCommitsFetcher = new PrCommitsFetcher(script)
    def commitComments = prCommitsFetcher.fetchPrCommitComments(projectName)
    CommitCommentsAnalyzer.findSemVerIncrement(commitComments)
  }
}
