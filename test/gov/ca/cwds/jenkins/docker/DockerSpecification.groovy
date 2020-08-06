package gov.ca.cwds.jenkins.docker

import spock.lang.Specification
import gov.ca.cwds.jenkins.common.BuildMetadata

class DockerSpecification extends Specification {
  def jobName = 'myjob'
  def buildId = '22'

  class PipelineScript {
    def docker

    PipelineScript(docker) {
      this.docker = docker
    }

    def sh(hash) {
    }
  }

  class GlobalDocker {
    def build(imageName, parameters) {
    }

    def image(imageName) {
    }
  }

  class DockerImage {
    def withRun(closure) { }
  }

  def "#createTestingImage implemented correctly"() {
    given:
    def globalDocker = Mock(GlobalDocker)
    def pipelineScript = new PipelineScript(globalDocker)
    def docker = new Docker(pipelineScript)
    def buildMetadata = new BuildMetadata(pipelineScript, 'myjob', '22', './somepath/to/project' )

    when:
    docker.createTestingImage('./somepath/Dockerfile', buildMetadata)

    then:
    1 * globalDocker.build('cwds/myjob:test-build-22', '-f ./somepath/Dockerfile .')
  }

  def "#removeTestingImage implemented correctly"() {
    given:
    def pipelineScript = Mock(PipelineScript)
    def docker = new Docker(pipelineScript)
    def buildMetadata = new BuildMetadata(pipelineScript, 'myjob', '22', './somepath/to/project' )

    when:
    docker.removeTestingImage(buildMetadata)

    then:
    1 * pipelineScript.sh([script: 'docker rmi cwds/myjob:test-build-22', returnStatus: true])
  }

  def "#withTestingImage implemented correctly"() {
    given:
    def dockerImage = Mock(DockerImage)
    def globalDocker = Stub(GlobalDocker)
    globalDocker.image(_) >> dockerImage

    def pipelineScript = new PipelineScript(globalDocker)
    def docker = new Docker(pipelineScript)
    def buildMetadata = new BuildMetadata(pipelineScript, 'myjob', '22', './somepath/to/project' )

    when:
    docker.withTestingImage('some_command', buildMetadata)

    then:
    1 * dockerImage.withRun(_ as Closure)
  }
}
