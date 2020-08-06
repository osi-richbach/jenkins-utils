package gov.ca.cwds.jenkins.pr

class PrCommitsFetcher {
  def script

  PrCommitsFetcher(script) {
    this.script = script
  }

  def fetchPrCommits(projectName) {
    def sha = script.env.ghprbActualCommit
    def commitsUrl = "https://api.github.com/repos/ca-cwds/${projectName}/commits?sha=${sha}&per_page=100"
    def response = commitsUrl.toURL().text
    script.readJSON(text: response)
  }

  def fetchPrCommitComments(projectName) {
    fetchPrCommits(projectName).collect { it.commit.message }
  }
}
