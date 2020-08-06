package gov.ca.cwds.jenkins.docker

import gov.ca.cwds.jenkins.common.BuildMetadata

class Docker {
  def script

  Docker(script) {
    this.script = script
  }

  def createTestingImage(String pathToDockerfile, BuildMetadata buildMetadata) {
    script.docker.build("${testingImageName(buildMetadata)}", "-f ${pathToDockerfile} .")
  }

  def removeTestingImage(BuildMetadata buildMetadata) {
    script.sh(script: "docker rmi ${testingImageName(buildMetadata)}", returnStatus: true)
  }

  def withTestingImage(String command, BuildMetadata buildMetadata) {
    def dockerImage = script.docker.image("${testingImageName(buildMetadata)}")
    dockerImage.withRun { container -> script.sh "docker exec -t ${container.id} ${command}" }
  }

  private testingImageName(BuildMetadata buildMetadata) {
    "cwds/${buildMetadata.jobName}:test-build-${buildMetadata.buildId}"
  }
}
