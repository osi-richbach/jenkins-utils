package gov.ca.cwds.jenkins

import spock.lang.Specification

class GithubRepoTaggerSpecification extends Specification {
  class PipeLineScript {

    def sshagent(hash, closure) {
    }

    def sh(hash) {
    }
  }

  def "#tagWith uses sshagent to push tags"() {
    given: 'a GithubRepoTagger'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def githubRepoTagger = new GithubRepoTagger(pipeline)

    when: 'it returns a non-zero return code'
    githubRepoTagger.tagWith('1.1.1', '43-2b')

    then: 'executes using the sshagent'
    1 * pipeline.sshagent([credentials: ['43-2b']], _ as Closure)
  }

  def "#tagAndPush with failure to tag"() {
    given: 'a GithubRepoTagger'
    PipeLineScript pipeline = Stub(PipeLineScript)
    pipeline.sh([script: 'git tag 1.1.1', returnStatus: true]) >> 1
    def githubRepoTagger = new GithubRepoTagger(pipeline)

    when: 'it returns a non-zero return code'
    githubRepoTagger.tagAndPush('1.1.1')

    then: 'an exception is thrown'
    def error = thrown(Exception)
    error.message == "Unable to tag the repository with tag '1.1.1'"
  }

  def "#tagAndPush with failure to config user email"() {
    given: 'a GithubRepoTagger'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def githubRepoTagger = new GithubRepoTagger(pipeline)
    def configCommands = "${githubRepoTagger.GIT_SSH_COMMAND} " +
      "git config --global user.email ${githubRepoTagger.GIT_EMAIL}; " +
      "git config --global user.name ${githubRepoTagger.GIT_USER}"

    when: 'it returns a non-zero return code'
    githubRepoTagger.tagAndPush('1.1.1')

    then: 'an exception is thrown'
    1 * pipeline.sh([script: 'git tag 1.1.1', returnStatus: true]) >> 0
    1 * pipeline.sh([script: configCommands, returnStatus: true]) >> 1
    def error = thrown(Exception)
    error.message == 'Unable to config the Jenkins user'
  }

  def "#tagAndPush with failure to push the tag"() {
    given: 'a GithubRepoTagger'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def githubRepoTagger = new GithubRepoTagger(pipeline)
    def configCommands = "${githubRepoTagger.GIT_SSH_COMMAND} " +
      "git config --global user.email ${githubRepoTagger.GIT_EMAIL}; " +
      "git config --global user.name ${githubRepoTagger.GIT_USER}"

    when: 'it returns a non-zero return code'
    githubRepoTagger.tagAndPush('1.1.1')

    then: 'an exception is thrown'
    1 * pipeline.sh([script: 'git tag 1.1.1', returnStatus: true]) >> 0
    1 * pipeline.sh([script: configCommands, returnStatus: true]) >> 0
    1 * pipeline.sh([script: "${githubRepoTagger.GIT_SSH_COMMAND} git push origin 1.1.1", returnStatus: true]) >> 1
    def error = thrown(Exception)
    error.message == "Unable to push the tag '1.1.1'"
  }

  def "#tagAndPush with success"() {
    given: 'a GithubRepo Trigger'
    PipeLineScript pipeline = Mock(PipeLineScript)
    def githubRepoTagger = new GithubRepoTagger(pipeline)
    def configCommands = "${githubRepoTagger.GIT_SSH_COMMAND} " +
      "git config --global user.email ${githubRepoTagger.GIT_EMAIL}; " +
      "git config --global user.name ${githubRepoTagger.GIT_USER}"

    when: 'tagging and pushing'
    githubRepoTagger.tagAndPush('1.1.1')

    then: 'it tags, configs as Jenkins, and pushes tags'
    1 * pipeline.sh([script: 'git tag 1.1.1', returnStatus: true]) >> 0
    1 * pipeline.sh([script: configCommands, returnStatus: true]) >> 0
    1 * pipeline.sh([script: "${githubRepoTagger.GIT_SSH_COMMAND} git push origin 1.1.1", returnStatus: true]) >> 0
  }

}
