package gov.ca.cwds.jenkins.common

import spock.lang.Specification

class ProjectTypesDeterminerSpecification extends Specification {

  class PipelineScript {
    def sh(hash) { }
  }

  def "#determineProjectTypes determines java correctly"() {
    given:
    def pipelineScript = Stub(PipelineScript)
    pipelineScript.sh([script: 'ls -al JavaProject/build.gradle', returnStatus: true]) >> 0

    def projectTypesDeterminer = new ProjectTypesDeterminer(pipelineScript)

    when:
    def projectTypes = projectTypesDeterminer.determineProjectTypes('JavaProject')

    then:
    projectTypes.contains(ProjectTypes.JAVA)
  }

  def "#determineProjectTypes determines javascript correctly"() {
    given:
    def pipelineScript = Stub(PipelineScript)
    pipelineScript.sh([script: 'ls -al JavascriptProject/package.json', returnStatus: true]) >> 0

    def projectTypesDeterminer = new ProjectTypesDeterminer(pipelineScript)

    when:
    def projectTypes = projectTypesDeterminer.determineProjectTypes('JavascriptProject')

    then:
    projectTypes.contains(ProjectTypes.JAVASCRIPT)
  }

  def "#determineProjectTypes determines ruby correctly"() {
    given:
    def pipelineScript = Stub(PipelineScript)
    pipelineScript.sh([script: 'ls -al RubyProject/.ruby-version', returnStatus: true]) >> 0

    def projectTypesDeterminer = new ProjectTypesDeterminer(pipelineScript)

    when:
    def projectTypes = projectTypesDeterminer.determineProjectTypes('RubyProject')

    then:
    projectTypes.contains(ProjectTypes.RUBY)
  }

  def "#determineProjectTypes determines ruby and javascriopt correctly"() {
    given:
    def pipelineScript = Stub(PipelineScript)
    pipelineScript.sh([script: 'ls -al RubyAndJavascriptProject/.ruby-version', returnStatus: true]) >> 0
    pipelineScript.sh([script: 'ls -al RubyAndJavascriptProject/package.json', returnStatus: true]) >> 0

    def projectTypesDeterminer = new ProjectTypesDeterminer(pipelineScript)

    when:
    def projectTypes = projectTypesDeterminer.determineProjectTypes('RubyAndJavascriptProject')

    then:
    projectTypes.containsAll([ProjectTypes.JAVASCRIPT, ProjectTypes.RUBY])
  }
}
