package gov.ca.cwds.jenkins

class GithubConfigProperties {
  def configure(githubUrl) {
    [$class: 'GithubProjectProperty',
      projectUrlStr: githubUrl,
      displayName: '',
    ]
  }
}
