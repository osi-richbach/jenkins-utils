package gov.ca.cwds.jenkins

import spock.lang.Specification

class ContainerScannerSpecification extends Specification {

  class PipeLineScript {
    def build(hash) {
    }
  }

  def "#sendNotification"() {
    def buildArguments

    given:
    def pipeline = Mock(PipeLineScript)
    def containerScanner = new ContainerScanner(pipeline)

    when:
    containerScanner.sendNotification('appllo', 'a1')

    then:
    1 * pipeline.build(_) >> { arguments -> buildArguments = arguments[0] }
    buildArguments['job'] == 'tenable-scan'
    buildArguments['parameters'][0] == [$class: 'StringParameterValue', name: 'CONTAINER_NAME', value: 'appllo']
    buildArguments['parameters'][1] == [$class: 'StringParameterValue', name: 'CONTAINER_VERSION', value: 'a1']
  }
}
