package gov.ca.cwds.jenkins

class ContainerScanner {
  def script

  ContainerScanner(script) {
    this.script = script
  }

  @SuppressWarnings('DuplicateStringLiteral')
  def sendNotification(containerName, containerVersion) {
    script.build job: 'tenable-scan', parameters: [
      [$class: 'StringParameterValue', name: 'CONTAINER_NAME', value: containerName],
      [$class: 'StringParameterValue', name: 'CONTAINER_VERSION', value: containerVersion]
    ]
  }
}
