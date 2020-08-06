package gov.ca.cwds.jenkins

import spock.lang.Specification

class AnsibleCommandRunnerSpecification extends Specification {
  def "#rollbackOldVersionCommand with APP_VERSION"() {
    given: 'an ansible command runner with an APP_VERSION'
    def ansibleCommandRunner = new AnsibleCommandRunner()
    def ansibleCommand = 'ansible-playbook -e APP_VERSION=1.0.0 deploy-cans.yml'

    when: 'rolling back the deploy'
    def updatedCommand = ansibleCommandRunner.rollbackOldVersionCommand(ansibleCommand, '0.9.9')
    then: 'it swaps in the old version'
    updatedCommand == 'ansible-playbook -e APP_VERSION=0.9.9 deploy-cans.yml'
  }

  def "#rollbackOldVersionCommand with APP_NUMBER"() {
    given: 'an ansible command runner with an APP_NUMBER'
    def ansibleCommandRunner = new AnsibleCommandRunner()
    def ansibleCommand = 'ansible-playbook -e CANS_APP_NUMBER=1.0.0 deploy-cans.yml'

    when: 'rolling back the deploy'
    def updatedCommand = ansibleCommandRunner.rollbackOldVersionCommand(ansibleCommand, '0.9.9')
    then: 'it swaps in the old version'
    updatedCommand == 'ansible-playbook -e CANS_APP_NUMBER=0.9.9 deploy-cans.yml'
  }
}
